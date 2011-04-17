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
import java.io.Serializable;

/**
 * superclass for raw UDPPacket definition
 * used to send informations over the udp link
 * Added : size field for statistics
 */
public class UdpPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8339917303185741055L;

	/**
	 * senders networkID
	 */
	protected String iD;

	/**
	 * the packet size may not be very accurate
	 */
	private int size;

	/**
	 * builds a raw udp packet with senders networkID default size set to 0
	 * @param id the senders networkID
	 */
	public UdpPacket(String id) {
		iD = id;
		size = 0;
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
			size = b.length;
			return b;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * the size of the packet in bytes, this value is only updated once the "toArray()" method has been called
	 * @return
	 */
	public int size() {
		return size;
	}

}
