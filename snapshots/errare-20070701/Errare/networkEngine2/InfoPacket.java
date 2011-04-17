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
 * data transaction packet for the tcp connection
 * @author linxsam
 */
public class InfoPacket<E> extends TcpPacket {
	
	private static final long serialVersionUID = 352826296844438998L;
	/**
	 * the payload the packet will carry over the network
	 */
	private E data;
	
	/**
	 * @param id source clients ID
	 * @param dest destination clients ID
	 * @param payload the payload the packet will carry
	 */
	public InfoPacket(String id, String dest, E payload) {
		super(id, dest);
		data = payload;
	}
	
	/**
	 * @return returns the payload the packet has carried over the network
	 */
	public E getData() {
		return data;
	}

}
