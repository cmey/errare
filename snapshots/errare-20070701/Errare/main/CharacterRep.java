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

import physicsEngine.CharacterPRep;
import physicsEngine.ItemPRep;
import gameEngine.Characters;
import gameEngine.ItemCharacteristics;
import graphicsEngine.GraphicalRep;
import guiEngine.GuiRep;

/** 
 * Class which manages the characters
 */
public class CharacterRep implements Rep {
	
	Characters characteristics;
	CharacterPRep physics;
	GraphicalRep graphics;
	
	public CharacterRep(Characters characteristics, CharacterPRep physics, GraphicalRep graphics){
		this.characteristics = characteristics;
		this.physics = physics;
		this.graphics=graphics;
	}

	public Characters getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(Characters characteristics) {
		this.characteristics = characteristics;
	}

	public GraphicalRep getGraphics() {
		return graphics;
	}

	public void setGraphics(GraphicalRep graphics) {
		this.graphics = graphics;
	}

	public CharacterPRep getPhysics() {
		return physics;
	}

	public void setPhysics(CharacterPRep physics) {
		this.physics = physics;
	}
	

}
