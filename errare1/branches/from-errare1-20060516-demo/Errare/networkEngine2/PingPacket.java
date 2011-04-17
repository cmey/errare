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
 * packet used for pinging over the tcp connexion
 *
 */
public class PingPacket extends TcpPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2880376668580056489L;
	
	/**
	 * the initial time stamp from the packets creation
	 */
	private long t0;

	/**
	 * build's a raw TCPPacket with added timestamp for latency calculation
	 * @param id the sender's networkID, so any peer knows where it came from
	 */
	public PingPacket(String id) {
		super(id);
		t0 = System.currentTimeMillis();
	}
	
	/**
	 * returns the latency of the connection (current time minus
	 * creation time of the packet
	 * @return the time difference between the current time and the creation time
	 */
	public long getLatency() {
		return System.currentTimeMillis() - t0;
	}


}
