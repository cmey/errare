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

import java.io.*;
import java.net.*;
import java.util.*;
import logger.*;
import networkLayer.messages.*;

/**
 * Implements the Network Layer services for the server, with Singleton pattern.
 * 
 * The server listens to incoming client connections, and creates a socket for
 * them and starts to send and receive data.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ServerNetworkLayer {
	/**
	 * Internal class with a thread to reply as soon as possible to any incoming
	 * data.
	 */
	private class SocketReader extends Thread {
		/** Socket to the pong the clients. */
		Socket socket;

		/** Var to control whether to continue ponging */
		boolean continueReading = true;

		/**
		 * Default constructor
		 * 
		 * @param socket
		 *            Socket to the ping server.
		 */
		SocketReader(Socket socket) {
			this.socket = socket;
		}

		/** Run waiting from incoming data */
		public void run() {
			while (continueReading) {
				try {
					ObjectInputStream ois = new ObjectInputStream(socket
							.getInputStream());
					AbstractNetworkMessage msg = (AbstractNetworkMessage) ois
							.readObject();
					ois.close();
					incomingMessageDispatcher.dispatchMessage(msg);
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					// nothing
				} catch (Exception e) {
					Logger.printINFO(this.getClass().getName(),
							"Dead connection: "
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
				continueReading = false;
				socket.close();
			} catch (Exception e) {
				Logger.printERROR(this.getClass().getName(),
						"When finalizing: " + e);
			}
		}
	}

	/**
	 * Internal class with a thread to wait for incoming connections.
	 */
	private class Listener extends Thread {
		/** Socket to listen for connections from the clients. */
		ServerSocket serverSocket;

		/** Objects reading incoming data */
		private Vector<SocketReader> socketReaders = new Vector<SocketReader>();

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
					Logger.printINFO(this.getClass().getName(),
							"Receiving connection: "
									+ s.getInetAddress().getHostAddress() + ":"
									+ s.getPort());
					SocketReader sR = new SocketReader(s);
					sR.start();
					synchronized (this) {
						socketReaders.add(sR);
					}
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					// nothing
				} catch (Exception e) {
					Logger.printERROR(this.getClass().getName(),
							"When receiving connection: " + e);
				}
			}
		}

		/** Remove dead connection (when client closed socket) */
		public void removeDeadConnection(SocketReader ponger) {
			synchronized (this) {
				socketReaders.remove(ponger);
			}
		}

		/** Clean up */
		public void finalize() {
			try {
				continueListening = false;
				serverSocket.close();
				for (SocketReader sR : socketReaders) {
					sR.finalize();
				}
			} catch (Exception e) {
				Logger.printERROR(this.getClass().getName(),
						"When finalizing: " + e);
			}
		}
	}

	/** Singleton instance */
	private static ServerNetworkLayer INSTANCE;

	/** Structure holding listeners. */
	Hashtable<String, NetworkMessageListener> messageListeners = new Hashtable<String, NetworkMessageListener>();

	/** Server socket, listening to new connections. */
	ServerSocket serverSocket;

	/** Object listening to incoming connections */
	private Listener listener;

	/** Object taking care of dispatching incoming data to the listeners */
	IncomingMessageDispatcher incomingMessageDispatcher;

	/** Access to the singleton instance */
	public static ServerNetworkLayer instance() {
		if (INSTANCE == null) {
			INSTANCE = new ServerNetworkLayer();
		}
		return INSTANCE;
	}

	/** Default private constructor */
	private ServerNetworkLayer() {
		incomingMessageDispatcher = new IncomingMessageDispatcher(
				messageListeners);
	}

	/**
	 * Bind server to listen to incoming connections.
	 * 
	 * @param address
	 *            Address of the server (hostname or IP)
	 * @param port
	 *            Port where the server is listening to new connections
	 */
	public void startListening(String address, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(address, port), 8);
			listener = new Listener(serverSocket);
			listener.start();
			Logger.printINFO(this.getClass().getName(),
					"Successfully bound to: "
							+ serverSocket.getInetAddress().getHostAddress()
							+ ":" + serverSocket.getLocalPort());
		} catch (Exception e) {
			Logger.printFATAL(this.getClass().getName(),
					"Couldn't bind server: " + e);
			System.exit(1);
		}
	}

	/**
	 * Disconnect the server.
	 */
	public void disconnect() {
		try {
			listener.finalize();
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't disconnect server: " + e);
		}
	}

	/**
	 * To send a message to the server.
	 * 
	 * @param msg
	 *            The message that we want to send.
	 * @return Whether the message was sent succesfully.
	 */
	public boolean sendMessage(Socket socket, AbstractNetworkMessage msg) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket
					.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			oos.close();
			socket.getOutputStream().flush();
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't send message to socket ("
							+ socket.getInetAddress().getHostAddress() + ":"
							+ socket.getPort() + "): " + e);
			return false;
		}
		return true;
	}

	/**
	 * Register a NetworkMessageListener for the given message.
	 * 
	 * @see IncomingMessageListener.register()
	 * 
	 * @param msg
	 *            The message that we want to subscribe to.
	 * @param listener
	 *            The listener to be notified when receiving such a message.
	 */
	public void register(AbstractNetworkMessage msg,
			NetworkMessageListener listener) {
		incomingMessageDispatcher.register(msg, listener);
	}

	/**
	 * Unregister a NetworkMessageListener for the given message.
	 * 
	 * @see IncomingMessageListener.unregister()
	 * 
	 * @param msg
	 *            The message that we want to subscribe to.
	 * @param listener
	 *            The listener to be notified when receiving such a message.
	 */
	public void unregister(AbstractNetworkMessage msg,
			NetworkMessageListener listener) {
		incomingMessageDispatcher.unregister(msg, listener);
	}

	/** Clean up */
	public void finalize() {
		disconnect();
	}
}
