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

public class Vector3D {
	public float x, y, z;
	
	public Vector3D(Point3D a, Point3D b) {
		x=b.x-a.x;
		y=b.y-a.y;
		z=b.z-a.z;
	}
	
	public Vector3D(Vector3D a, Vector3D b) {
		x=a.y*b.z-a.z*b.y;
		y=a.z*b.x-a.x*b.z;
		z=a.x*b.y-a.y*b.x;
	}
	
	public String toString() {
		return "Vector3D: "+x+" "+y+" "+z;
	}
}