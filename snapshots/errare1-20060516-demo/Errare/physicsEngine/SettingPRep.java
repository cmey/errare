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

package physicsEngine;

import main.Rep;
import main.SettingRep;


/**
 * @author trueblue
 * 
 * Physical representation of a setting object.
 * It is represented as a square.
 */
public class SettingPRep extends PhysicalRep {

	private static final long serialVersionUID = 1L;
	
	private SettingRep rep;
	
	
	public SettingRep getRep() {
		return rep;
	}


	public void setRep(SettingRep rep) {
		this.rep = rep;
		cube.setRep(rep);
	}


	/**
	 * Creates a new setting
	 * @param x x coordinate of the setting
	 * @param y y coordinate of the setting
	 * @param w width of the setting
	 * @param h height of the setting
	 * @param physicsEngine physicsEngine on which the setting evoluates
	 */
	public SettingPRep(int x, int y, int w, int h, PhysicsEngine physicsEngine) {
		super(x, y, w, h, physicsEngine);
		
		if(!physicsEngine.addSetting(this)) {
			throw new IllegalArgumentException("Something is already located at ("+x+";"+y+")");
		}
		/*else {
			physicsEngine.getCubeTree().add(cube);
		}*/
	}

}
