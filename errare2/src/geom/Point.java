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

import java.io.Serializable;
import java.util.ArrayList;

public class Point implements Serializable{
	
	
	private static final long serialVersionUID = -3820624836425978125L;
	
	public float x;
	public float y;
	public float z;
	
	public Point(){
		this(0,0,0);
	}
	
	public Point(float x, float y, float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Point(Point p) {
		this.x=p.x;
		this.y=p.y;
		this.z=p.z;
	}
	
	public void translate (float x,float y,float z){
		this.x+=x;
		this.y+=y;
		this.z+=z;
	}
	
	public void translate (Vector v){
		this.x+=v.x;
		this.y+=v.y;
		this.z+=v.z;
	}
	
	public float distance(Point p) {
		float distx = p.x - this.x;
		float disty = p.y - this.y;
		float distz = p.z - this.z;
		
		return (float) Math.sqrt(distx*distx + disty*disty + distz*distz);
	}
	
	public float distanceSquare(Point p) {
		float distx = p.x - this.x;
		float disty = p.y - this.y;
		float distz = p.z - this.z;
		
		return distx*distx + disty*disty + distz*distz;
	}
	
	/**
	 * Occlusion test by the method "point vu, point cache" ;-)
	 * @param occluder a triangle potentially occluding this point
	 * @param cameraPos position of the camera
	 * @return true if this point is occluded by the given triangle
	 */
	public boolean isOccludedBy(geom.Triangle occluder, Point cameraPos){
		Vector IA = new Vector(cameraPos, occluder.points[0]);
		Vector IB = new Vector(cameraPos, occluder.points[1]);
		Vector IC = new Vector(cameraPos, occluder.points[2]);
		Vector IM = new Vector(cameraPos, this);
		//constructing the system matrix
		//- row for lambda1
		float[] row1 = new float[5];
		row1[0] = IA.x; row1[1] = IB.x; row1[2] = IC.x; row1[3] = -IM.x; row1[4] = 0;
		//- row for lambda2
		float[] row2 = new float[5];
		row2[0] = IA.y; row2[1] = IB.y; row2[2] = IC.y; row2[3] = -IM.y; row2[4] = 0;
		//- row for lambda3
		float[] row3 = new float[5];
		row3[0] = IA.z; row3[1] = IB.z; row3[2] = IC.z; row3[3] = -IM.z; row3[4] = 0;
		//- row for k
		float[] row4 = new float[5];
		row4[0] = 1; row4[1] = 1; row4[2] = 1; row4[3] = 0; row4[4] = 1;
		
		float[][] system = new float[4][5];
		system[0] = row1;
		system[1] = row2;
		system[2] = row3;
		system[3] = row4;
		//- solving
		Solver.solve(system);
		//results are in the last column of each row
		
		//System.out.println("geom.Point.isOccludedBy: Visual checking : Lambda1 + Lambda2 + Lambda3 = 1 ? "+(row1[4] + row2[4] + row3[4]));// assertion to discard when we are sure everything works perfectly
		
		//- is the point closer as the triangle?
		if(row4[4] > 1){
			// triangle is too far
			return false; // this point is not occluded
		}else if(row4[4] < 0){
			// TODO: pathological case to treat further (maybe)
			return false; // secure
		}else{
			// good aligned : we need further testing
			if(row1[4] >= 0  &&  row2[4] >= 0  &&  row3[4] >= 0){
				return true; // this point is occluded
			}else{
				return false; // this point is not occluded (triangle is askew)
			}
		}
	}

	public boolean isOccludedBy(ArrayList<Triangle> triangles, Point cameraPos) {
		for(Triangle t : triangles) {
			if(this.isOccludedBy(t, cameraPos)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "("+x+":"+y+":"+z+")";
	}
	
	public Point clone(){
		return new Point(this.x,this.y,this.z);
	}
}