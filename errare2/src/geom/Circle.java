/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Arnaud KNOBLOCH

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package geom;


import java.awt.*;

/**
 * This class describes a Circle.
  */
public class Circle
    extends java.awt.Point {
    
    private int radius;
   
    /** 
     * Constrruct a new default Circle.  The default Circle
     * has x=0, y=0, radius = 1
     */
    public Circle() {
	// Automatic call to Point();
	radius = 1;
    }

    /**
     * Construct a new Circle at the specified corrdinate,
     * with the specified radius and color.
     * 
     * @param x the x coordinate of the new Circle
     * @param y the y coordinate of the new Circle
     * @param r the radius of the new Circle
     */
    
    public Circle(int x, int y, int r) {
	super(x, y);  
	radius = r;
    }
    
    /**
     * Get the radius of this Circle.
     * 
     * @return the radius of this Circle.
     */
    public int getRadius() {
	return radius;
    }
    
    /**
     * Grow the radius of this Circle by the specified amount.
     * 
     * @param gr the amount by which to grow the radius.
     */
    public void grow(int gr) {
	radius = radius + gr;
    }
    
    /**
     * Get the area of this Circle.
     * 
     * @returm the area of this Circle.
     */
    public double getArea() {
	return Math.PI * radius * radius;
    }
}


