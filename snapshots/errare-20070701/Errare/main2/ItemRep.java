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

package main2;

import gameEngine.GameRep;
import graphicsEngine.GraphicalRep;
import guiEngine.GuiRep;
import physicsEngine2.PhysicalRep;

public class ItemRep<A extends GameRep, P extends PhysicalRep, G extends GraphicalRep, I extends GuiRep> extends Rep{
	
	private A gameRep;
	private P physicalRep;
	private G graphicalRep;
	private I guiRep;
	
	public ItemRep(A gameRep, P physicalRep, G graphicalRep, I guiRep, int id) {
		super(id);
		this.gameRep=gameRep;
		this.physicalRep=physicalRep;
		this.graphicalRep=graphicalRep;
		this.guiRep=guiRep;
	}
	
	public A getGameRep() {
		return gameRep;
	}

	public G getGraphicalRep() {
		return graphicalRep;
	}

	public I getGuiRep() {
		return guiRep;
	}

	public P getPhysicalRep() {
		return physicalRep;
	}

	public void setGameRep(A gameRep) {
		this.gameRep = gameRep;
	}

	public void setGraphicalRep(G graphicalRep) {
		this.graphicalRep = graphicalRep;
	}

	public void setGuiRep(I guiRep) {
		this.guiRep = guiRep;
	}

	public void setPhysicalRep(P physicalRep) {
		this.physicalRep = physicalRep;
	}

}
