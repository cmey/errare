package networkEngine2;

import java.io.Serializable;
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

/**
 * Superclass of any networked communication packets
 * it only defines the packets source client ID
 * @author linxsam
 */
public class Packet implements Serializable {
	
	private static final long serialVersionUID = 7248806439697539224L;
	/**
	 * the source clients ID
	 */
	private String iD;
	
	/**
	 * the source clients ID
	 * @param id
	 */
	public Packet(String id) {
		iD = id;
	}
	
	/**
	 * @return returns this packets source clients ID
	 */
	public String getID() {
		return iD;
	}
}
