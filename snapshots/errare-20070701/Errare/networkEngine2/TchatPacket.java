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
 * @author linxsam
 *
 */
public class TchatPacket extends TcpPacket {
	
	private static final long serialVersionUID = -1847693115233044764L;
	private String text;
	
	/**
	 * instanciates a new packet for tchat messaging in errare
	 * @param id the senders ID
	 * @param dest the destinations ID, can be a client, or a tchatroom
	 * @param text the text message to pass
	 */
	public TchatPacket(String id, String dest, String text) {
		super(id, dest);
		this.text = text;
	}
	
	/**
	 * @return the sended text
	 */
	public String getText() {
		return text;
	}

}
