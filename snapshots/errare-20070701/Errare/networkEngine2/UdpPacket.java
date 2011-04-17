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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Superclass of any datapacket that will be sent over the udp connections
 * @author linxsam
 */
public class UdpPacket extends Packet {
	
	private static final long serialVersionUID = -7050485684194812037L;

	/**
	 * a new generick udppacket useless in this forme
	 * @param id the source clients ID
	 */
	public UdpPacket(String id) {
		super(id);
	}
	
	/**
	 * write the udppacket to a bite array
	 * @return the byteArray representation of the udpPacket i.e. to send it or
	 *         null if error
	 */
	public byte[] toArray() {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(this);
			byte[] b = bao.toByteArray();
			return b;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
		return null;
	}

	/**
	 * Creates a new UdpPacket from it's byteArray representation i.e. for
	 * recover of a received packet
	 * 
	 * @param b
	 *            the byteArray representation of the UdpPacket
	 * @return null if a error has occured
	 */
	public static UdpPacket fromArray(byte[] b) {
		try {
			ByteArrayInputStream bai = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bai);
			UdpPacket p = (UdpPacket) ois.readObject();
			return p;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			NetworkEngine.LOG.printFATAL(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
		return null;
	}

}
