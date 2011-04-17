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

import logger.*;
import networkLayer.messages.*;

/**
 * Simple class implementing a demo server.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ServerExample implements NetworkMessageListener {

	// implementing NetworkMessageListener
	public void handleNetworkMessage(AbstractNetworkMessage msg) {
		Logger.printINFO("main", "received message reply: " + msg);
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			Logger.printFATAL("main", "Number of arguments not valid: "
					+ args.length);
			Logger
					.printINFO("main",
							"Usage: ./programName remoteHostName remotePingPort remotePort");
			System.exit(1);
		}

		Logger.printINFO("main", "Server Starting");

		ServerExample server = new ServerExample();

		// instantiating ping server
		ServerNetworkLayerPing.instance().startListening(args[0],
				Integer.parseInt(args[1]));

		// registering listener and instantiate network server
		LoginNetworkMessage loginMsg = new LoginNetworkMessage();
		ServerNetworkLayer.instance().register(loginMsg, server);
		ServerNetworkLayer.instance().startListening(args[0],
				Integer.parseInt(args[2]));
	}
}
