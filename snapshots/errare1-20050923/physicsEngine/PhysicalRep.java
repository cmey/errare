package physicsEngine;

import java.awt.Rectangle;

import main.Rep;



/**
 * @author trueblue
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("unchecked")
public abstract class PhysicalRep extends Rectangle {
	
	public enum type {CHARACTER, MAINCHARACTER, MOBJECT, NMOBJECT};
	
	protected PhysicsEngine map;
	private Rep rep;
	private int[] area;
	
	private int id;
	private static int ID=0;
	
	public PhysicalRep(int x, int y, int width, int height, PhysicsEngine map, Rep rep) {
		super(x, y, width, height);
		this.map = map;
		this.rep = rep;
		id = ID++;
		
		map.addElement(this);
	}
	
	public abstract int getLevel();
	
	public Rep getRep() {
		return rep;
	}
	
	public void setArea(int[] a) {
		area = a;
	}
	
	public int[] getArea() {
		return area;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof PhysicalRep))
			return false;
		else
			return ((PhysicalRep)o).id == id;
	}

	public int getId() {
		return id;
	}
}
