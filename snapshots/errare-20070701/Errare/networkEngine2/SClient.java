/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Sebastien FISCHER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/
package networkEngine2;

import java.net.Socket;

/**
 * @author linxsam
 *
 */
public class SClient extends Client {

	private Server server;
	protected String remoteID;
	
	/**
	 * server side client endpoint
	 * @param soc the socket leading to the remote client
	 * @param s the server the client is running on
	 */
	public SClient(Socket soc,Server s) {
		super(soc, "");
		server = s;
		remoteID = "";
	}
	
	/**
	 * @return the remote identifying id
	 */
	public String getRemoteID() {
		return remoteID;
	}
	
	/**
	 * forward the received packet to the main game system
	 * @param p
	 */
	public void forwardto(TcpPacket p) {
		server.forward(p);
	}
	

}
