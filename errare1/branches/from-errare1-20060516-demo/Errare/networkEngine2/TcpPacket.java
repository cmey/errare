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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * superclass for raw TcpPacket definition
 * used to send informations over the tcp link
 */
public class TcpPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536808421808323157L;
	/**
	 * sender's networkID -> from Client.IDR
	 */
	private String iD;
	
	/**
	 * new raw TCP packet
	 * @param id the sender's networkID
	 */
	public TcpPacket(String id) {
		iD = id;
	}
	
	/**
	 * calculates the packet's size
	 * @return the byte size of the packet (-1 is something went wrong)
	 */
	public int getSize() {		
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(this);
			byte[] b = bao.toByteArray();
			return b.length;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * get the networkID the packet came from
	 * @return sender's ID
	 */
	public String getID() {
		return iD;
	}
	

}
