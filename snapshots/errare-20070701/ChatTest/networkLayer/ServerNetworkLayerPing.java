/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package networkLayer;

import java.net.*;
import java.util.*;
import logger.*;

/**
 * Implements the Network Layer Ping service for the server, with Singleton
 * pattern.
 * 
 * A server would attach to a socket in a given address:port, and it will reply
 * to the incoming connections immediately. Theoretically the data to reply with
 * is not important since the client won't read it, but for safety we can reply
 * with the same data received (if it has any meaning it would be a timestamp).
 * We don't need to do anything else, the client is the one which would
 * calculate and show the values.
 * 
 * When stopping, the listener and all the Ponger threads are cleared, and we
 * could start the process again.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ServerNetworkLayerPing {
	/**
	 * Internal class with a thread to reply as soon as possible to any incoming
	 * data.
	 */
	private class Ponger extends Thread {
		/** Socket to the pong the clients. */
		Socket socket;

		/** Var to control whether to continue ponging */
		boolean continuePonging = true;

		/**
		 * Default constructor
		 * 
		 * @param socket
		 *            Socket to the ping client.
		 */
		Ponger(Socket socket) {
			this.socket = socket;
		}

		/** Run waiting from incoming data */
		public void run() {
			while (continuePonging) {
				try {
					int i = socket.getInputStream().read();
					socket.getOutputStream().write(i);
					socket.getOutputStream().flush();
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					// nothing
				} catch (Exception e) {
					Logger.printINFO(this.getClass().getName(),
							"Dead ping connection: "
									+ socket.getInetAddress().getHostAddress()
									+ ":" + socket.getPort());
					listener.removeDeadConnection(this);
					finalize();
				}
			}
		}

		/** Clean up */
		public void finalize() {
			try {
				continuePonging = false;
				socket.close();
			} catch (Exception e) {
				Logger.printERROR(this.getClass().getName(),
						"When finalizing: " + e);
			}
		}
	}

	/**
	 * Internal class with a thread to wait for incoming ping connections.
	 */
	private class Listener extends Thread {
		/** Socket to listen for pings from the clients. */
		ServerSocket serverSocket;

		/** Objects replying to incoming pings */
		private Vector<Ponger> pongers = new Vector<Ponger>();

		/** Var to control whether to continue listening */
		boolean continueListening = true;

		/**
		 * Default constructor
		 * 
		 * @param serverSocket
		 *            Server Socket.
		 */
		Listener(ServerSocket serverSocket) {
			this.serverSocket = serverSocket;
		}

		/** Run waiting from incoming data */
		public void run() {
			while (continueListening) {
				try {
					Socket s = serverSocket.accept();
					Ponger ponger = new Ponger(s);
					ponger.start();
					synchronized (this) {
						pongers.add(ponger);
					}
					Logger.printINFO(this.getClass().getName(),
							"Receiving ping connection: "
									+ s.getInetAddress().getHostAddress() + ":"
									+ s.getPort());
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					// nothing
				} catch (Exception e) {
					Logger.printERROR(this.getClass().getName(),
							"When receiving ping connection: " + e);
				}
			}
		}

		/** Remove dead connection (when client closed socket) */
		public void removeDeadConnection(Ponger ponger) {
			synchronized (this) {
				pongers.remove(ponger);
			}
		}

		/** Clean up */
		public void finalize() {
			try {
				continueListening = false;
				serverSocket.close();
				for (Ponger ponger : pongers) {
					ponger.finalize();
				}
			} catch (Exception e) {
				Logger.printERROR(this.getClass().getName(),
						"When finalizing: " + e);
			}
		}
	}

	/** Singleton instance */
	private static ServerNetworkLayerPing INSTANCE;

	/** Object listening to incoming pings */
	private Listener listener;

	/** Access to the singleton instance */
	public static ServerNetworkLayerPing instance() {
		if (INSTANCE == null) {
			INSTANCE = new ServerNetworkLayerPing();
		}
		return INSTANCE;
	}

	/** Default private constructor */
	private ServerNetworkLayerPing() {
	}

	/** Start listening for ping connections */
	public void startListening(String address, int port) {
		try {
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(address, port), 8);
			listener = new Listener(serverSocket);
			listener.start();
			Logger.printINFO(this.getClass().getName(),
					"Successfully starting ping listener: "
							+ serverSocket.getInetAddress().getHostAddress()
							+ ":" + serverSocket.getLocalPort());
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't bind listener (for ping): " + e);
		}
	}

	/** Clean up */
	public void finalize() {
		listener.finalize();
		Logger.printINFO(this.getClass().getName(),
				"Stopped listener and ponger servers");
	}
}
