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

public class Vector {
	public float x;
	public float y;
	public float z;
	
	public Vector() {
		x=0;
		y=0;
		z=0;
	}
	
	public Vector(float x, float y, float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vector(Point p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public Vector(Point start, Point end) {
		x=end.x-start.x;
		y=end.y-start.y;
		z=end.z-start.z;
	}
	
	public void normalize(){
		float d = (float) Math.sqrt(x*x+y*y+z*z); 
			if (d == 0.0) { 
			    return; 
			} 
			x /= d; 
			y /= d; 
			z /= d;  
	}
	
	public float getMagnitude(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	
	public void add(Vector p) {
		x+=p.x;
		y+=p.y;
		z+=p.z;
	}
	
	public void substract(Vector p){
		x-=p.x;
		y-=p.y;
		z-=p.z;
	}
	
	public void mult(float length){
		x*=length;
		y*=length;
		z*=length;
	}
	
	
	public static Vector crossProduct(Vector p1, Vector p2){
		float x2 = p1.y*p2.z - p1.z*p2.y; 
		float y2 = p1.z*p2.x - p1.x*p2.z; 
		float z2 = p1.x*p2.y - p1.y*p2.x;
		return new Vector(x2,y2,z2);
	}
	
	public static float dotProduct(Vector p1, Vector p2){
		return p1.x*p2.x + p1.y*p2.y + p1.z*p2.z;
	}
	
	public static Vector add(Vector p1, Vector p2){
		return new Vector(p1.x+p2.x,p1.y+p2.y,p1.z+p2.z);
	}
	
	public static Vector substract(Vector p1, Vector p2){
		return new Vector(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
	}
	
	public static Vector mult(Vector p, float length){
		return new Vector(
				p.x*length,
				p.y*length,
				p.z*length);
	}
	
	

}
