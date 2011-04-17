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

import java.util.*;
import logger.*;
import networkLayer.messages.*;

/**
 * Class with a thread to check periodically for incoming data from the network
 * and dispatching the message to the appropriate listener.
 * 
 * @author mafm@users.sourceforge.net
 */
public class IncomingMessageDispatcher extends Thread {
	/** Structure holding listeners. */
	Hashtable<String, NetworkMessageListener> listeners;

	/**
	 * Default constructor
	 * 
	 * @param listeners
	 *            The structure holding the listeners for the incoming messages.
	 */
	IncomingMessageDispatcher(
			Hashtable<String, NetworkMessageListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Register a NetworkMessageListener for the given message.
	 * 
	 * @param msg
	 *            The message that we want to subscribe to.
	 * @param listener
	 *            The listener to be notified when receiving such a message.
	 */
	public synchronized void register(AbstractNetworkMessage msg,
			NetworkMessageListener listener) {
		if (listeners.get(msg.getClass().getName()) != null) {
			Logger.printERROR(this.getClass().getName(),
					"Registering listener which was already present: "
							+ msg.getClass().getName());
		} else {
			listeners.put(msg.getClass().getName(), listener);
			Logger.printDEBUG(this.getClass().getName(),
					"Registering listener: " + msg.getClass().getName() + ", "
							+ listener.getClass().getName());
		}
	}

	/**
	 * Unregister a NetworkMessageListener for the given message.
	 * 
	 * @param msg
	 *            The message that we want to subscribe to.
	 * @param listener
	 *            The listener to be notified when receiving such a message.
	 */
	public synchronized void unregister(AbstractNetworkMessage msg,
			NetworkMessageListener listener) {
		if (listeners.get(msg.getClass().getName()) != null) {
			listeners.remove(msg.getClass().getName());
			Logger.printDEBUG(this.getClass().getName(),
					"Unregistering listener succesfully: "
							+ msg.getClass().getName());
		} else {
			Logger.printERROR(this.getClass().getName(),
					"Unregistering listener which was not found, for message: "
							+ msg.getClass().getName());
		}
	}

	/**
	 * Dispatch the message to the proper listener.
	 * 
	 * @param msg
	 */
	public synchronized void dispatchMessage(AbstractNetworkMessage msg) {
		NetworkMessageListener listener = listeners.get(msg.getClass()
				.getName());
		if (listener == null) {
			Logger.printERROR(this.getClass().getName(),
					"Listener not found for message: "
							+ msg.getClass().getName());
		} else {
			listener.handleNetworkMessage(msg);
			Logger.printDEBUG(this.getClass().getName(),
					"Network message handled: " + msg.getClass().getName());
		}
	}
}
