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

import graphicsEngine.GraphicalRep;
import physicsEngine2.PhysicalRep;

public abstract class Rep {
	
	private int id;
	
	public Rep(int id) {
		this.id=id;
	}
	
	public abstract PhysicalRep getPhysicalRep();
	public abstract GraphicalRep getGraphicalRep();
	
	public int getId() {
		return id;
	}
}
