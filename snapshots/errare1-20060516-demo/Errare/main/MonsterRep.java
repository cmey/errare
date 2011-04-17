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
import gameEngine.Hero;
import gameEngine.ItemCharacteristics;
import gameEngine.Monster;
import graphicsEngine.GraphicalRep;
import guiEngine.GuiRep;

/** 
 * Class which manage the characters
 */
public class MonsterRep extends CharacterRep {
	
	Monster characteristics;
	CharacterPRep physics;
	GraphicalRep graphics;
	
	public MonsterRep(Monster characteristics, CharacterPRep physics, GraphicalRep graphics){
		super(characteristics, physics, graphics);
		this.characteristics = characteristics;
		this.physics = physics;
		this.graphics=graphics;
	}

	public Monster getMonster() {
		return characteristics;
	}

	public void setMonster(Monster characteristics) {
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
