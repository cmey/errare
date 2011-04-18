/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package networkEngine.messages;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import logger.*;

/**
 * Simple Login message.
 * 
 * @author mafm@users.sourceforge.net
 */
public class LoginNetworkMessage extends TryEvent {
	/** Username registered in the server */
	public String username;

	/** Password for the user, in hashed form. */
	public String pwdHash;

	/** Default constructor */
	public LoginNetworkMessage() {
	}

	/**
	 * Constuctor to simplify the setup of the message
	 * 
	 * @param username
	 *            The username.
	 * @param rawPassword
	 *            The password in raw form, it will be converted to the hash
	 *            (which is what the server stores).
	 */
	public LoginNetworkMessage(String username, String rawPassword) {
		this.username = username;
		this.pwdHash = getHashedPassword(rawPassword);
	}

	/**
	 * Helper method to get the hashed password, as used by the server.
	 * 
	 * @param rawPassword
	 *            The raw string of the password.
	 * @return Hash of the password, in String form.
	 */
	public String getHashedPassword(String rawPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] rawHash = digest.digest(rawPassword.getBytes());
			String hashed = new String();
			for (int i = 0; i < rawHash.length; i++) {
				String singleByte = Integer.toHexString(rawHash[i] & 0xFF);
				if (singleByte.length() == 1) {
					singleByte = "0" + singleByte;
				}
				hashed += singleByte;
			}
			if (hashed.length() != 32) {
				Logger.printERROR("Hash string has not the proper size (32): "
								+ hashed.length());
			}
			return hashed;
		} catch (NoSuchAlgorithmException e) {
			Logger.printERROR(e.getMessage());
			return null;
		}
	}

	/** To help debugging */
	public String toString() {
		return "[" + getClass().getSimpleName() + "|" + username + "|"
				+ pwdHash + "]";
	}
}
