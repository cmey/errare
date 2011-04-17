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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author linxsam
 * class for security duty in the server
 */
public class Secured {
	
	private final static MessageDigest ciph = digest();
	
	private static MessageDigest digest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
		return null;
	}
	
	/**
	 * generates a MD5 hash
	 * @param tmp the byte array to hash
	 * @return the generated hashcode shielded into a hex representation String
	 */
	public static String md5Hash(byte[] tmp) {
		byte[] b = ciph.digest(tmp);
		String s = "";
		for(int i = 0 ; i< b.length;i++) {
			int j = b[i] & 0xFF;
			s = s + Integer.toHexString(j);
		}
		NetworkEngine.LOG.printDEBUG("Generated hash: "+s);
		return s;
	}
	
	/**
	 * queries the database for player password
	 * @param id the player's id
	 * @return the players md5hashed password
	 */
	public static String getPassFor(String id) {
		return "password"; // TODO seek password in database
	}
	
}
