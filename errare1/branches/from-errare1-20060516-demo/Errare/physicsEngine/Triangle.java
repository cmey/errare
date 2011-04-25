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


public class Triangle {

	private Vector3D n;
	private Point3D a;
	public Point3D[] points;
	
	public  Triangle(Point3D a, Point3D b, Point3D c) {
		points = new Point3D[3];
		points[0]=a;
		points[1]=b;
		points[2]=c;
		
		Vector3D ab = new Vector3D(a, b);
		Vector3D ac = new Vector3D(a, c);
		n = new Vector3D(ab, ac);
		
		this.a=a;
	}
	
	public float solve(int x, int y) {
		float z = -((x-a.x)*n.x+(y-a.y)*n.y)/n.z + a.z;
		return z;
	}
	
	
	public static void main(String args[]) {
		Triangle t = new Triangle(new Point3D(0,0,306), new Point3D(0,400,417), new Point3D(400,0,382));
		System.out.println(t.solve(0, 0));
	}
	
}
