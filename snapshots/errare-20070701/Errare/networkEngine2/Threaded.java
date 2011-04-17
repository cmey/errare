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
 * customized thread extension
 */
public class Threaded extends Thread {
	
	protected boolean run;

	/**
	 * a new threaded element
	 */
	public Threaded() {
		super();
		run = true;
	}
	
	/**
	 * tels the element to stop
	 *
	 */
	public void end() {
		run = false;
	}

}
