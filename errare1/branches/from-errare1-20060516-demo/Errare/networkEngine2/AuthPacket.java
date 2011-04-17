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
 * packet that is sent over the network during the initial handshake
 * containes the udp ports the other side has to send to
 * is used by the server to send the initial challenge
 * is used by the client to send the challenge's reply
 */
public class AuthPacket extends TcpPacket {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -893204419179005284L;

	/**
	 * the port number the client listens to
	 */
	private int udp1,udp2;
	
	/**
	 * when sent from server it contains the "challenge"
	 * when sent from client it contains it's reply to the "challenge"
	 */
	private String signature;

	/**
	 * creates a new tcppacket for authetification purpose
	 * @param id the senders ID
	 * @param challenge the generated signature
	 * @param udp1 the client's or the server's udp1 port
	 * @param udp2 the client/server'S udp2 port
	 */
	public AuthPacket(String id, String challenge, int udp1, int udp2) {
		super(id);
		signature = challenge;
		this.udp1 = udp1;
		this.udp2 = udp2;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the signature.
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @return Returns the udp1.
	 */
	public int getUdp1() {
		return udp1;
	}

	/**
	 * @return Returns the udp2.
	 */
	public int getUdp2() {
		return udp2;
	}

}
