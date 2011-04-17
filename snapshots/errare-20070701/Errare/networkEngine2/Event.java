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

import java.io.Serializable;

/**
 * Event superclass, for event buffering
 * @author linxsam
 *
 */
public class Event implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2947989549350424702L;
	private String iD;
	
	/**
	 * instanciates a generic event only qualified by the performer's id
	 * @param id the performer's id
	 */
	public Event(String id) {
		iD = id;
	}
	
	/**
	 * @return the event's origin ID
	 */
	public String getID() {
		return iD;
	}
	
	/**
	 * string representation for the event
	 */
	public String toString() {
		return "generic event";
	}

	
}
