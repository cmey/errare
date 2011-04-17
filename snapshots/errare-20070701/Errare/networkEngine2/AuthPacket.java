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
 * packet used for the initial authetification process
 * @author linxsam
 *
 */
public class AuthPacket extends TcpPacket {

	private static final long serialVersionUID = 6368560570137658008L;
	private int udp1,udp2;
	private String signature;
	/**
	 * packet used to pass connection information
	 * @param id senders ID
	 * @param dest destination ID
	 * @param sign generated signature
	 * @param p1 udp1 port number
	 * @param p2 udp2 port number
	 */
	public AuthPacket(String id, String dest, String sign, int p1, int p2) {
		super(id, dest);
		udp1 = p1;
		udp2 = p2;
		signature = sign;
	}
	
	/**
	 * @return the generated signature
	 */
	public String getSignature() {
		return signature;
	}
	
	/**
	 * @return udp1 port number
	 */
	public int getUdp1() {
		return udp1;
	}
	
	/**
	 * @return udp2 port number
	 */
	public int getUdp2() {
		return udp2;
	}
	
	

}
