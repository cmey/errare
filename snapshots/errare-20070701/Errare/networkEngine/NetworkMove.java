/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Antoine PIERRONNET

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package networkEngine;

import java.io.Serializable;

public class NetworkMove implements Serializable{

	private static final long serialVersionUID = -4844125388049570876L;
	
	private int id;
	private int x;
	private int y;

	public NetworkMove(int id, int x, int y) {
		this.id=id;
		this.x=x;
		this.y=y;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	

}
