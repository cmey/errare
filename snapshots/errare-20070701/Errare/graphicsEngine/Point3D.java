/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package graphicsEngine;

/**
 * 
 * @author Cyberchrist
 *
 */
public class Point3D {
	public float X;
	public float Y;
	public float Z;
	
	public Point3D(){
		X=0;
		Y=0;
		Z=0;
	}
	
	public Point3D(float x, float y, float z){
		this.X=x;
		this.Y=y;
		this.Z=z;
	}
	
	/**
	 * translate
	 * @param x
	 * @param y
	 * @param z
	 */
	public void tl (float x,float y,float z){
		this.X+=x;
		this.Y+=y;
		this.Z+=z;
	}
	
	/**
	 * Normalise le vecteur
	 */
	public void normalize(){
		float d = (float) Math.sqrt(X*X+Y*Y+Z*Z); 
			if (d == 0.0) { 
			    return; 
			} 
			X /= d; Y /= d; Z /= d;  
	}
	
	/**
	 * Produit vectoriel des deux vecteurs, retour d'un new vecteur normalisé
	 * @param p
	 */
	public Point3D cross_prod(Point3D p){
		float x2 = Y*p.Z - Z*p.Y; 
		float y2 = Z*p.X - X*p.Z; 
		float z2 = X*p.Y - Y*p.X; 
		Point3D out = new Point3D(x2,y2,z2);
		out.normalize();
		return out;
	}
	
	public void add(Point3D p){
		X+=p.X;
		Y+=p.Y;
		Z+=p.Z;
	}
	
	public Point3D add_rnew(Point3D p){
		return new Point3D(X+p.X,Y+p.Y,Z+p.Z);
	}
	
	public Point3D substract(Point3D p){
		return new Point3D(X-p.X,Y-p.Y,Z-p.Z);
	}
	
	/**
	 * Produit scalaire
	 * @return
	 */
	public float dot_prod(Point3D p){
		return X*p.X + Y*p.Y + Z*p.Z;
	}
	
	public Point3D mult(float length){
		return new Point3D(
				X*length,
				Y*length,
				Z*length);
	}
	
	public float Magnitude(){
		return (float) Math.sqrt(X*X+Y*Y+Z*Z);
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		Z = z;
	}
}
