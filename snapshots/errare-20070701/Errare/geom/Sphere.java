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

package geom;

public class Sphere extends Debug {
	public Point center;
	public float rad;
	
	public Sphere() {}
	
	public Sphere(Point center, float rad) {
		this.center = center;
		this.rad = rad;
	}

	public void translate(Vector v) {
		center.translate(v);
	}
	
	public void translate(float x, float y, float z) {
		center.translate(x, y, z);
	}
	
	public boolean intersects(Sphere s) {
		return s.center.distance(center) <= s.rad+rad;
	}

}
