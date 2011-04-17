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


public class Triangle extends Debug {
	public Point[] points;
	private Vector normal;
	private Edge[] edges3;
	
	public Triangle() {
		points = new Point[3];
		edges3 = new Edge[3];
	}
	
	public  Triangle(Point a, Point b, Point c) {
		points = new Point[3];
		edges3 = new Edge[3];
		points[0]=a;
		points[1]=b;
		points[2]=c;
		
		edges3[0] = new Edge(a,b);
		edges3[1] = new Edge(b,c);
		edges3[2] = new Edge(c,a);
	}
	
	public Triangle(Edge ab, Edge bc, Edge ca){
		edges3[0] = ab;
		edges3[1] = bc;
		edges3[2] = ca;
		
		points = computePoints3Given3Edges(ab,bc,ca);
	}
	
	public void computeNormal() {
		normal = Vector.crossProduct(new Vector(points[0], points[1]), new Vector(points[0], points[2]));
	}
	
	public void setNormal(Vector v) {
		this.normal=v;
	}
	
	public void translate(Vector v) {
		points[0].translate(v);
		points[1].translate(v);
		points[2].translate(v);
		
		//normal = Vector.crossProduct(new Vector(points[0], points[1]), new Vector(points[0], points[2]));
	}
	
	public void translate(float x, float y, float z) {
		points[0].translate(x, y, z);
		points[1].translate(x, y, z);
		points[2].translate(x, y, z);
		
		//normal = Vector.crossProduct(new Vector(points[0], points[1]), new Vector(points[0], points[2]));
	}
	
	public float solveZ(int x, int y) {
		return -((x-points[0].x)*normal.x+(y-points[0].y)*normal.y)/normal.z + points[0].z;
	}
	
	public float solveY(int x, int z) {
		return -((x-points[0].x)*normal.x+(z-points[0].z)*normal.z)/normal.y + points[0].y;
	}
	
	public float solveX(int y, int z) {
		return -((y-points[0].y)*normal.y+(z-points[0].z)*normal.z)/normal.x + points[0].x;
	}
	
	private Point[] computePoints3Given3Edges(Edge ab, Edge bc, Edge ca){
		Point[] ret = new Point[3];
		if(ab.p2==bc.p1 && bc.p2==ca.p1 && ca.p2==ab.p1){
			Point a = ca.p2;
			Point b = bc.p1;
			Point c = ca.p1;
			Point[] ret1 = {a,b,c};
			return ret1;
		}
		return ret;
	}
	
	public Vector getNormal(){
		return normal;
	}
	
	public Edge[] getEdges3(){
		return edges3;
	}
	
	public float getDValueOfPlane(){
		return 0;
	}
}
