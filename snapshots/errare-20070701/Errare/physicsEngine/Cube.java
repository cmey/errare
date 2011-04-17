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

import main.CharacterRep;
import main.Rep;


public class Cube {
	
	private Point3D leftFrontTop;
	private Point3D rightBackBottom;
	
	private CubeTree tree;
	
	private Rep rep;
	private Point3D center;
	private float rad;
	private float innerRad;

	


	public Cube(Point3D leftFrontTop, Point3D rightBackBottom) {
		this.leftFrontTop=leftFrontTop;
		this.rightBackBottom=rightBackBottom;
		
		innerRad = Math.abs(leftFrontTop.x-rightBackBottom.x)/2;
		rad = innerRad*innerRad+innerRad*innerRad;
		rad = (float) Math.sqrt(rad);
		center = new Point3D(leftFrontTop.x+innerRad, leftFrontTop.y+innerRad, leftFrontTop.z-innerRad);
		
	}
	
	public boolean contains(Point3D p) {
		return  p.x>=this.leftFrontTop.x
				&& p.y>=this.leftFrontTop.y
				&& p.z<=this.leftFrontTop.z
				&& p.x<=this.rightBackBottom.x
				&& p.y<=this.rightBackBottom.y
				&& p.z>=this.rightBackBottom.z;
	}
	
	public boolean contains(Cube c) {
		return c.leftFrontTop.x>=this.leftFrontTop.x
		&& c.leftFrontTop.y>=this.leftFrontTop.y
		&& c.leftFrontTop.z<=this.leftFrontTop.z
		&& c.rightBackBottom.x<=this.rightBackBottom.x
		&& c.rightBackBottom.y<=this.rightBackBottom.y
		&& c.rightBackBottom.z>=this.rightBackBottom.z;
	}
	
	/*public boolean intersectsOrContains(Cube c) {
		return c.contains(rightBackBottom) || c.contains(rightFrontTop) 
		|| c.contains(leftFrontBottom) || c.contains(rightFrontBottom) 
		|| c.contains(leftBackBottom) || c.contains(leftBackTop) 
		|| c.contains(rightBackTop)
		
		|| this.contains(c.rightBackBottom) || this.contains(c.rightFrontTop) 
		|| this.contains(c.leftFrontBottom) || this.contains(c.rightFrontBottom) 
		|| this.contains(c.leftBackBottom) || this.contains(c.leftBackTop) 
		|| this.contains(c.rightBackTop);
	}*/
	
	public boolean equals(Object o) {
		return this==o;
	}
	
	public void removeFromTree() {
		tree.remove(this);
	}


	public void setTree(CubeTree tree) {
		this.tree = tree;
	}
	
	//TODO: ONLY FOR DEBUG !
	public CubeTree getTree() {
		return tree;
	}


	public void move(Point3D leftFrontTop, Point3D rightBackBottom) {
		this.leftFrontTop=leftFrontTop;
		this.rightBackBottom=rightBackBottom;
		
		
		center = new Point3D(leftFrontTop.x+innerRad, leftFrontTop.y+innerRad, leftFrontTop.z-innerRad);
		//init();
		
		//tree.moveCubeInTree(this); //TODO: remove from method add in physicsEngine and uncomment here
		
		//System.out.println(tree.getLevel());
	}
	
	public Point3D getCenter() {
		return center;
	}
	
	public float getRadius() {
		return rad;
	}
	
	public Rep getRep() {
		return rep;
	}
	
	public void setRep(Rep rep) {
		this.rep=rep;
	}

}
