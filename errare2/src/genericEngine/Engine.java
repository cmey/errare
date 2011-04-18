/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package genericEngine;

public interface Engine {

	/**
	 * Called when an registered mouse event has occured.
	 * @param action action string
	 * @return false if the engine captures the event, true otherwise
	 */
	public boolean invokeMouseEvent(String action, int x, int y);

	/**
	 * Called when an registered key event has occured.
	 * @param action action string
	 * @return false if the engine captures the event, true otherwise
	 */
	public boolean invokeKeyEvent(String action);
	
	/**
	 * Called when the program exits.
	 *
	 */
	public void quit();
	
	
	public void run();
}
