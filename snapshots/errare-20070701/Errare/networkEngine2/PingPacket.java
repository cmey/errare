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
 * packet for latency determination of the udp connection
 * @author linxsam
 *
 */
public class PingPacket extends UdpPacket {

	private static final long serialVersionUID = -4476932391081407280L;
	/**
	 * the packets factoring timestamp
	 */
	private long t0;
	private String destID;
	
	/**
	 * factors a new ping packet, it is timestamped within the constructor
	 * so the best way to remain accurate is to send it directly after construction
	 * @param id the source clients ID
	 */
	public PingPacket(String id, String dest) {
		super(id);
		t0 = System.currentTimeMillis();
		destID = dest;
	}
	
	/**
	 * @return the destination ID of the ping
	 */
	public String getDest() {
		return destID;
	}
	
	/**
	 * @return returns the time difference between factoring and this method call
	 */
	public long getLatency() {
		return System.currentTimeMillis() - t0;
	}

}
