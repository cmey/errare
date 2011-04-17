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

package main;

import physicsEngine.ItemPRep;
import gameEngine.ItemCharacteristics;
import graphicsEngine.GraphicalRep;
import guiEngine.GuiRep;

/** 
 * Class which manage the characters
 */
public class ItemRep implements Rep {
	
	ItemCharacteristics characteristics;
	ItemPRep physics;
	GraphicalRep graphics;
	GuiRep gui;
	
	public ItemRep(ItemCharacteristics characteristics, ItemPRep physics, GraphicalRep graphics, GuiRep gui){
		this.characteristics = characteristics;
		this.physics = physics;
		this.graphics=graphics;
		this.gui=gui;
	}

	public ItemCharacteristics getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(ItemCharacteristics characteristics) {
		this.characteristics = characteristics;
	}

	public GraphicalRep getGraphics() {
		return graphics;
	}

	public void setGraphics(GraphicalRep graphics) {
		this.graphics = graphics;
	}

	public GuiRep getGui() {
		return gui;
	}

	public void setGui(GuiRep gui) {
		this.gui = gui;
	}

	public ItemPRep getPhysics() {
		return physics;
	}

	public void setPhysics(ItemPRep physics) {
		this.physics = physics;
	}
	

}
