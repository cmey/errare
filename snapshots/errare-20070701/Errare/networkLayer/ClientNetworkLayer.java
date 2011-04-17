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
import networkLayer.messages.*;

import logger.*;

/**
 * Implements the Network Layer services for the client, with Singleton pattern.
 * 
 * This means that a client has to connect to a server, and then it can freely
 * send messages to the server via this service, no need to handle network
 * specific stuff in other parts of the application.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ClientNetworkLayer {
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

	/** Singleton instance */
	private static ClientNetworkLayer INSTANCE;

	/** The socket that holds our connection with the server */
	private Socket socket;

	/** The socket that reads incoming messages from the server */
	private SocketReader socketReader;

	/** Object taking care of dispatching incoming data to the listeners */
	IncomingMessageDispatcher incomingMessageDispatcher;

	/** Structure holding listeners. */
	Hashtable<String, NetworkMessageListener> messageListeners = new Hashtable<String, NetworkMessageListener>();

	/** Access to the singleton instance */
	public static ClientNetworkLayer instance() {
		if (INSTANCE == null) {
			INSTANCE = new ClientNetworkLayer();
		}
		return INSTANCE;
	}

	/** Default private constructor */
	private ClientNetworkLayer() {
		incomingMessageDispatcher = new IncomingMessageDispatcher(
				messageListeners);
	}

	/**
	 * Connect to the server.
	 * 
	 * @param address
	 *            Address of the server (hostname or IP)
	 * @param port
	 *            Port where the server is listening to new connections
	 * @return Whether the operation succeeded
	 */
	public boolean connectToServer(String address, int port) {
		try {
			socket = new Socket(address, port);
			socketReader = new SocketReader(socket);
			socketReader.start();
			Logger.printINFO(this.getClass().getName(),
					"Successfully connected to: "
							+ socket.getInetAddress().getHostAddress() + ":"
							+ socket.getPort());
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't connect to server: " + e);
			return false;
		}

		return true;
	}

	/**
	 * Disconnect from server.
	 */
	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't disconnect from server: " + e);
		}
	}

	/**
	 * To check whether we are connected to the server.
	 * 
	 * @return Whether we are connected to the server
	 */
	public boolean isConnected() {
		return socket.isConnected();
	}

	/**
	 * To send a message to the server.
	 * 
	 * @param msg
	 *            The message that we want to send.
	 * @return Whether the message was sent succesfully.
	 */
	public boolean sendMessage(AbstractNetworkMessage msg) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket
					.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			oos.close();
			socket.getOutputStream().flush();
		} catch (Exception e) {
			Logger.printERROR(this.getClass().getName(),
					"Couldn't send message to server: " + e);
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
