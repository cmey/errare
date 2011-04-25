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

import main.Rep;


/**
 * @author trueblue
 * 
 * Physical representation or an object.
 * 
 */
public abstract class PhysicalRep{
	
	private int x, y, z, w, h;
	
	private int cx, cy, cw, ch;
	
	protected Cube cube;
	
	private float angle=0;
	
	/**PhysicsEngine where the object evoluates*/
	protected PhysicsEngine physicsEngine;

	/**Unique id*/
	private int id;

	/**Counter that is incrementated at each object creation to generate an unique id*/
	private static int ID=0;
	
	/**
	 * Called by the physical representation when created.
	 * @param x x coordinate of the object
	 * @param y y coordinate of the object
	 * @param w width of the object
	 * @param h height of the object
	 */
	protected PhysicalRep(int x, int y, int w, int h, PhysicsEngine physicsEngine) {
		if(x<0 || y<0 || x+w>=physicsEngine.getWidth() || y+h>=physicsEngine.getHeight())
			throw new IllegalArgumentException("Object does not fit in the map !");
		
		
		this.cx=x;
		this.cy=y;
		this.cw=w;
		this.ch=h;
		
		this.w=w*PhysicsEngine.CELLDIM;
		this.h=h*PhysicsEngine.CELLDIM;
		this.x=x*PhysicsEngine.CELLDIM;
		this.y=y*PhysicsEngine.CELLDIM;
		this.z=physicsEngine.getTerrainHeightAt(this.x, this.y);
		
		
		this.physicsEngine = physicsEngine;
		id = ID++;
		
		cube=new Cube(new Point3D(this.x, this.y, this.z+this.w), new Point3D(this.x+this.w, this.y+this.w, this.z));
		
	}
	
	public void move(int x, int y) {
		
		this.x=x;
		this.y=y;
		this.z=physicsEngine.getTerrainHeightAt(x, y);
		
		cx = x/PhysicsEngine.CELLDIM;
		cy = y/PhysicsEngine.CELLDIM;
		
		cube.move(new Point3D(this.x, this.y, this.z+this.w), new Point3D(this.x+this.w, this.y+this.w, this.z));
	}
	
	public void setLocation(int cx, int cy) {
		this.cx=cx;
		this.cy=cy;
		
		this.x=cx*PhysicsEngine.CELLDIM;
		this.y=cy*PhysicsEngine.CELLDIM;
		this.z=physicsEngine.getTerrainHeightAt(x, y);
	}
	
	/**
	 * Determines if this character is in contact with an other character.
	 * @param c character to check if it is in contact whith this character
	 * @return true if this character is in contact with the specified character
	 */
	public boolean isInContact(PhysicalRep c) {
		int dx = c.cx-this.cx;
		int dy = c.cy-this.cy;
		
		return dx<=2 && dx>=-2 && dy<=2 && dy>=-2;
		
	}


	/**
	 * @return true if the specified object has the same reference as this object
	 */
	public boolean equals(Object o) {
		return (o==this);
	}

	/**
	 * Returns the unique id of this object.
	 * @return id of the object
	 */
	public int getId() {
		return id;
	}

	public int ch() {
		return ch;
	}

	public int cw() {
		return cw;
	}

	public int cx() {
		return cx;
	}

	public int cy() {
		return cy;
	}

	public int h() {
		return h;
	}

	public int w() {
		return w;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}
	
	public int z() {
		return z;
	}
	
	public void setAngle(float angle) {
		this.angle=angle;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public abstract Rep getRep();
	
	public Cube getCube() {
		return cube;
	}
	
}
