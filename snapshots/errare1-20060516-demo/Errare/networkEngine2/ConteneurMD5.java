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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 *
 */
public class ConteneurMD5 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6612538500580362005L;
	private UdpPacket data;
	private byte[] ar;
	private int size;
	
	/**
	 * provides ready to use md5 digest for byte arrays
	 * @param tmp the array to get the hash from
	 * @return
	 */
	public static byte[] md5Hash(byte[] tmp) {
		try {
			return MessageDigest.getInstance("MD5").digest(tmp);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * new md5 container for "secured" udp traffic
	 * @param paquet the payload (unsigned packet)
	 * @param userID the userID to sign the packet
	 */
	public ConteneurMD5(UdpPacket paquet, byte[] userID) {
		data = paquet;
		try {
			byte[] tmp = new byte[data.toArray().length+userID.length];
			int j = 0;
			for(int i = data.toArray().length; i < tmp.length; i++) {
				tmp[i] = userID[j];
				j++;
			}
			ar = MessageDigest.getInstance("MD5").digest(tmp);
			size = data.size()+ar.length;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * checks if the packet signature is genuine
	 * @param paquet the data that has been extracted
	 * @param userID the userID used to generate the signature
	 * @return
	 */
	public int compareTo(UdpPacket paquet, byte[] userID) {
		byte[] tmp = new byte[data.toArray().length+userID.length];
		int j = 0;
		for(int i = data.toArray().length; i < tmp.length; i++) {
			tmp[i] = userID[j];
			j++;
		}
		String a,b;
		b = "";
		a = new String(ar);
		try {
			b = new String(MessageDigest.getInstance("MD5").digest(tmp));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a.compareTo(b);
	}
	
	/**
	 * the total container size
	 * @return
	 */
	public int getsize() {
		return size;
	}
	
	/**
	 * the unsigned udppacket
	 * @return
	 */
	public UdpPacket getData() {
		return data;
	}
	
	/**
	 * 
	 * @return the byteArray representation to send it or
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
	 * Creates a new md5 Container from it's byteArray representation i.e. for
	 * recover of a received packet
	 * 
	 * @param b
	 *            the byteArray representation
	 * @return null if a error has occured
	 */
	public static ConteneurMD5 fromArray(byte[] b) {
		try {
			ByteArrayInputStream bai = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bai);
			ConteneurMD5 cnt = (ConteneurMD5) ois.readObject();
			return cnt;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
