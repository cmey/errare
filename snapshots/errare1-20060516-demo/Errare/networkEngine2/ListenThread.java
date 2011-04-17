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
 * Basic thread for event listenning on the network
 *
 */
public class ListenThread extends Thread {

	/**
	 * set false to stop the thread loop
	 */
	protected boolean run;
	
	public ListenThread() {
		super();
		run = true;
	}
	
	/**
	 * call to end main loop's execution
	 *
	 */
	public void end() {
		run = false;
	}
}
