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

/**
 * superclass for any packe that will be sent over the tcp connections between the clients
 * @author linxsam
 */
public class TcpPacket extends Packet {
	
	private static final long serialVersionUID = -3459795716800092651L;
	/**
	 * this packets destination client ID
	 */
	private String destID;

	/**
	 * instanciates a new tcppacket
	 * @param id the source clients ID
	 * @param dest the destination clients ID
	 */
	public TcpPacket(String id, String dest) {
		super(id);
		destID = dest;
	}
	
	/**
	 * @return returns the destination clients ID
	 */
	public String getDest() {
		return destID;
	}
	
	/**
	 * change the packets id useful for forwarding
	 * @param id the new destID
	 */
	public void setDest(String id) {
		destID = id;
	}

}
