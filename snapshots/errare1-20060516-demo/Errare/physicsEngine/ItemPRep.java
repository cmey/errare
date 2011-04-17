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

import main.ItemRep;
import main.SettingRep;




/**
 * @author trueblue
 * 
 * Physical representation of an item(object that can be picked by a character)
 * It is represented as a square.
 */
public class ItemPRep extends PhysicalRep {

	private static final long serialVersionUID = 1L;
	
	private ItemRep rep;
	
	private boolean isPicked;
	/**
	 * Creates a new item
	 * @param x x coordinate of the item
	 * @param y y coordinate of the item
	 * @param w width of the item
	 * @param h height of the item
	 * @param physicsEngine physicsEngine on which the item evoluates
	 */
	public ItemPRep(int x, int y, PhysicsEngine physicsEngine) {
		super(x, y, 1, 1, physicsEngine);
		
		if(!physicsEngine.addItem(this)) {
			throw new IllegalArgumentException("Something is already located at ("+x+";"+y+")");
		}
		
		isPicked=false;
		/*else {
			physicsEngine.getCubeTree().add(cube);
		}*/
	}

	public ItemRep getRep() {
		return rep;
	}

	public void setRep(ItemRep rep) {
		this.rep = rep;
		cube.setRep(rep);
	}
	
	public boolean isPicked() {
		return isPicked;
	}
	
	public void setPicked(boolean b) {
		isPicked=b;
	}
	
}
