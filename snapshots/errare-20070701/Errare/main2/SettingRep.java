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

public class SettingRep<P extends PhysicalRep, G extends GraphicalRep> extends Rep{
	
	private P physicalRep;
	private G graphicalRep;
	
	public SettingRep(P physicalRep, G graphicalRep, int id) {
		super(id);
		this.physicalRep=physicalRep;
		this.graphicalRep=graphicalRep;
	}

	public G getGraphicalRep() {
		return graphicalRep;
	}
	
	public P getPhysicalRep() {
		return physicalRep;
	}

	public void setGraphicalRep(G graphicalRep) {
		this.graphicalRep = graphicalRep;
	}

	public void setPhysicalRep(P physicalRep) {
		this.physicalRep = physicalRep;
	}

}
