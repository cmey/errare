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
 * Implements the Network Layer Ping service for the client, with Singleton
 * pattern.
 * 
 * A client is meant to use this class by giving the list of server address and
 * port to connect to, one at a time, and from that point onwards the class
 * takes care of pinging and waiting for replies. The class notifies of delays
 * in ping replies via the listener provided when adding the server. The
 * information retrieved would be typically shown in a table on the GUI, besides
 * the server.
 * 
 * When stopping (which typically should be done after selecting a server and
 * connecting succesfully), all the Pinger threads are cleared, and we could
 * start the process again.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ClientNetworkLayerPing {
	/**
	 * Internal class with a thread to ping periodically a server and
	 * dispatching the result to the appropriate listener.
	 */
	private class Pinger extends Thread {
		/** Socket to the ping server. */
		Socket socket;

		/** Receives the information about ping replies. */
		PingListener listener;

		/** Var to control whether to continue pinging */
		boolean continuePinging = true;

		/**
		 * Default constructor
		 * 
		 * @param socket
		 *            Socket to the ping server.
		 * @param listener
		 *            Receives the information about ping replies.
		 */
		Pinger(Socket socket, PingListener listener) {
			this.socket = socket;
			this.listener = listener;
		}

		/** Run waiting from incoming data */
		public void run() {
			while (continuePinging) {
				try {
					long sent = System.currentTimeMillis();
					socket.getOutputStream().write(0);
					socket.getOutputStream().flush();
					socket.getInputStream().read(); // ignoring what's read
					long received = System.currentTimeMillis();
					listener.handlePingDelay(socket.getInetAddress()
							.getHostName(), socket.getPort(),
							(received - sent) / 2);
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					// nothing
				} catch (Exception e) {
					Logger.printERROR(this.getClass().getName(),
							"When sending/receiving ping: " + e);
				}
			}
		}

		/** Clean up */
		public void finalize() {
			try {
				continuePinging = false;
				socket.close();
			} catch (Exception e) {
				Logger.printERROR(this.getClass().getName(),
						"When finalizing: " + e);
			}
		}
	}

	/** Singleton instance */
	private static ClientNetworkLayerPing INSTANCE;

	/** Structure holding the Pingers */
	Vector<Pinger> pingers = new Vector<Pinger>();

	/** Access to the singleton instance */
	public static ClientNetworkLayerPing instance() {
		if (INSTANCE == null) {
			INSTANCE = new ClientNetworkLayerPing();
		}
		return INSTANCE;
	}

	/** Default private constructor */
	private ClientNetworkLayerPing() {
	}

	/**
	 * Add a server to be pinged.
	 * 
	 * @param address
	 *            The address of the server.
	 * @param port
	 *            The port that the server is listening to.
	 * @param listener
	 *            The listener that will receive the ping reply information.
	 */
	public void addServer(String address, int port, PingListener listener) {
		Logger.printDEBUG(this.getClass().getName(),
				"Adding server to be pinged: " + address + ", " + port);
		try {
			Socket socket = new Socket(address, port);
			socket.setSoTimeout(5 * 1000); // 5 seconds
			Pinger pinger = new Pinger(socket, listener);
			pinger.start();
			pingers.add(pinger);
			Logger.printINFO(this.getClass().getName(),
					"Successfully connected to server (for ping): "
							+ socket.getInetAddress().getHostAddress() + ":"
							+ socket.getPort());
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't connect to server (for ping): " + e);
		}
	}

	/** Stop pinging. */
	public void stopAndReset() {
		for (Pinger pinger : pingers) {
			pinger.finalize();
		}
		pingers.clear();

		Logger.printINFO(this.getClass().getName(), "Stopped pinging servers");
	}

	/** Clean up */
	public void finalize() {
		stopAndReset();
	}
}
