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

import networkLayer.messages.*;
import logger.*;

/**
 * Simple class implementing a demo client.
 * 
 * @author mafm@users.sourceforge.net
 */
public class ClientExample implements PingListener, NetworkMessageListener {

	// implementing PingListener
	public void handlePingDelay(String address, int port, long milliseconds) {
		Logger.printINFO("main", "ping reply: " + address + ":" + port + ", "
				+ milliseconds + "ms");
	}

	// implementing NetworkMessageListener -- not used here
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

		Logger.printINFO("main", "Client Starting");

		ClientExample client = new ClientExample();

		// start pinging the target server
		ClientNetworkLayerPing.instance().addServer(args[0],
				Integer.parseInt(args[1]), client);

		// busy wait so it pings for a few seconds
		int a = 12;
		for (int i = 5; i < 1000 * 1000; i++) {
			for (int j = 5; j < 5 * 1000; j++) {
				if (i > args.length)
					a = i + j + a;
			}
		}
		System.err.println("result: " + a);

		// stop pinging
		ClientNetworkLayerPing.instance().stopAndReset();

		// we don't handle any message at the moment
		// ClientNetworkLayer.instance().register(args[0], this);

		// connect to the chosen server, we have nothing to choose this time
		boolean connected = ClientNetworkLayer.instance().connectToServer(
				args[0], Integer.parseInt(args[2]));

		// send a test message that should be handled by the server
		LoginNetworkMessage msg = new LoginNetworkMessage();
		msg.username = "mafm";
		msg.pwdHash = "lalala";
		ClientNetworkLayer.instance().sendMessage(msg);
	}
}
