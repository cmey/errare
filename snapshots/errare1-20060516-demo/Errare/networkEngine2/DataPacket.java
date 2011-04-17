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
 * Packet designed for data transport over the network
 * currently for general purpose but may be more specialyzed in later versions
 */
public class DataPacket<E> extends TcpPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4234464484618352599L;
	/**
	 * the data the packet has to transport
	 */
	private E data;
	/**
	 * a new TcpPacket for data transport
	 * @param id the sender's networkID
	 * @param payload the data to be transported over the network
	 */
	public DataPacket(String id, E payload) {
		super(id);
		data = payload;
	}
	
	public E getPayload() {
		return data; 
	}
	

}
