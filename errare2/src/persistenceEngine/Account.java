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

package persistenceEngine;

import gameEngine.GameCharacter;

import java.util.*;

/**
 * Player account. A player account is composed by e-mail address (acting like
 * an username), password, and maybe some other user information. Each player
 * can have a limited ammount of Characters registered.
 * 
 * @author mafm@users.sourceforge.net
 */
public class Account {

	private String email;

	private String password;

	private Date dateCreated;
	
	private List<GameCharacter> chars;

	public Account(String email, String password) {
		this.email = email;
		this.password = password;
		this.dateCreated = new Date();
		chars = new LinkedList<GameCharacter>();
	}

	public String getName() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}
	
	public List<GameCharacter> getCharacters() {
		return chars;
	}

	public int getNumberOfCharacters() {
		return chars.size();
	}
	
	public void addCharacter(GameCharacter c) {
		chars.add(c);
	}
	
	public void removeCharacter(GameCharacter c) {
		chars.remove(c);
	}

	public String toString() {
		return "Account: " + email + " password MD5: " + password
				+ " characters: " + chars.toString();
	}
}
