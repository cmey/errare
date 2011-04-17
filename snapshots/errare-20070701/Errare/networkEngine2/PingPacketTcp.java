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
 * packet for latency determination of the tcp connection
 * @author linxsam
 *
 */
public class PingPacketTcp extends TcpPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113147130248416774L;
	/**
	 * the packets creation timestamp
	 */
	private long t0;
	
	/**
	 * pingpacket for ping evaluation
	 * @param id
	 * @param dest
	 */
	public PingPacketTcp(String id, String dest) {
		super(id, dest);
		t0 = System.currentTimeMillis();
	}
	
	/**
	 * @return the latency of the connection tis packet was sent over
	 */
	public long getLatency() {
		return System.currentTimeMillis() - t0;
	}

}
