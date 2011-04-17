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

import java.util.ArrayList;
import java.util.PriorityQueue;

import physicsEngine2.OccluderList.Element;

/**
 * This class describes a cube.
 * 
 * @author trueblue
 *
 */
public class Cube extends Debug {
	
	/**Left front bottom point of the cube*/
	private Point leftFrontBottom;
	
	/**Right back top point of the cube*/
	private Point rightBackTop;
	
	/**The 8 Points defining the cube*/
	private Point[] points8;
	
	/**Bounding sphere of the cube*/
	private Sphere sphere;
	
	/**Triangles list that compose this Cube*/
	private ArrayList<Triangle> trianglesComposing;

	/**
	 * Creates a cube.
	 * @param leftFrontBottom left front bottom point of the cube
	 * @param rightBackTop right back top point of the cube
	 */
	public Cube(Point leftFrontBottom, Point rightBackTop) {
		this.leftFrontBottom=leftFrontBottom;
		this.rightBackTop=rightBackTop;
		this.points8 = new Point[8];
		
		this.trianglesComposing = new ArrayList<Triangle>();
		this.computeTrianglesComposingThisCube();
		
		float innerRadX = (rightBackTop.x-leftFrontBottom.x)/2;
		float innerRadY = (rightBackTop.y-leftFrontBottom.y)/2;
		float innerRadZ = (rightBackTop.z-leftFrontBottom.z)/2;
		float rad = innerRadX*innerRadX+innerRadY*innerRadY+innerRadZ*innerRadZ;
		rad = (float) Math.sqrt(rad);
		Point center = new Point(leftFrontBottom.x+innerRadX, leftFrontBottom.y+innerRadY, leftFrontBottom.z+innerRadZ);
		sphere = new Sphere(center, rad);
	}
	
	public boolean contains(Point p) {
		return  p.x>=this.leftFrontBottom.x
				&& p.y>=this.leftFrontBottom.y
				&& p.z>=this.leftFrontBottom.z
				&& p.x<=this.rightBackTop.x
				&& p.y<=this.rightBackTop.y
				&& p.z<=this.rightBackTop.z;
	}
	
	public boolean contains(Cube c) {
		return c.leftFrontBottom.x>=this.leftFrontBottom.x
		&& c.leftFrontBottom.y>=this.leftFrontBottom.y
		&& c.leftFrontBottom.z>=this.leftFrontBottom.z
		&& c.rightBackTop.x<=this.rightBackTop.x
		&& c.rightBackTop.y<=this.rightBackTop.y
		&& c.rightBackTop.z<=this.rightBackTop.z;
	}
	
	public boolean intersects(Cube c) {
		return  c.leftFrontBottom.x<=this.rightBackTop.x
		&& c.leftFrontBottom.y<=this.rightBackTop.y
		&& c.leftFrontBottom.z<=this.rightBackTop.z
		
		&& c.rightBackTop.x>=this.leftFrontBottom.x
		&& c.rightBackTop.y>=this.leftFrontBottom.y
		&& c.rightBackTop.z>=this.leftFrontBottom.z;
	}
	
	public void translate(Vector v) {
		leftFrontBottom.translate(v);
		rightBackTop.translate(v);
		compute8Points();
		sphere.translate(v);
	}
	
	private void compute8Points(){
		points8[0] = leftFrontBottom;
		points8[1] = new Point(leftFrontBottom.x, rightBackTop.y, leftFrontBottom.z);
		points8[2] = new Point(rightBackTop.x, leftFrontBottom.y, leftFrontBottom.z);
		points8[3] = new Point(rightBackTop.x, rightBackTop.y, leftFrontBottom.z);
		points8[4] = new Point(rightBackTop.x, leftFrontBottom.y, rightBackTop.z);
		points8[5] = rightBackTop;
		points8[6] = new Point(leftFrontBottom.x, leftFrontBottom.y, rightBackTop.z);
		points8[7] = new Point(leftFrontBottom.x, rightBackTop.y, rightBackTop.z);
	}
	
	private void computeTrianglesComposingThisCube(){
		compute8Points();
		//surround
		Triangle t = new Triangle(points8[0], points8[1], points8[2]);
		t.setNormal(new Vector(0, 0, -1));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[1], points8[2], points8[3]);
		t.setNormal(new Vector(0, 0, -1));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[2], points8[3], points8[4]);
		t.setNormal(new Vector(1, 0, 0));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[3], points8[4], points8[5]);
		t.setNormal(new Vector(1, 0, 0));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[4], points8[5], points8[6]);
		t.setNormal(new Vector(0, 0, 1));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[5], points8[6], points8[7]);
		t.setNormal(new Vector(0, 0, 1));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[6], points8[7], points8[0]);
		t.setNormal(new Vector(-1, 0, 0));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[7], points8[0], points8[1]);
		t.setNormal(new Vector(-1, 0, 0));
		trianglesComposing.add(t);
		//top
		t = new Triangle(points8[1], points8[3], points8[7]);
		t.setNormal(new Vector(0, 1, 0));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[3], points8[7], points8[5]);
		t.setNormal(new Vector(0, 1, 0));
		trianglesComposing.add(t);
		
		//bottom
		t = new Triangle(points8[0], points8[2], points8[6]);
		t.setNormal(new Vector(0, -1, 0));
		trianglesComposing.add(t);
		
		t = new Triangle(points8[2], points8[6], points8[4]);
		t.setNormal(new Vector(0, -1, 0));
		trianglesComposing.add(t);
	}
	
	public ArrayList<Triangle> getTrianglesComposingList(){
		return trianglesComposing;
	}
	
	public ArrayList<Triangle> getVisibleTriangles(Point cameraPos){
		ArrayList<Triangle> result = new ArrayList<Triangle>();
		
		Vector viewerToTriangle;
		float dotProdResult;
		
		for(Triangle t : getTrianglesComposingList()){
			viewerToTriangle = new Vector(cameraPos, t.points[0]);
			dotProdResult = Vector.dotProduct(t.getNormal(),viewerToTriangle);
			if(dotProdResult<0){
				//polygon faces the viewer
				result.add(t);
			}
		}
		return result;
	}
	
	public boolean isOccludedBy(Element occluder, Point cameraPos){
		for(Point cubeP : this.getPoints8()){
			if(!cubeP.isOccludedBy(occluder.getTriangles(), cameraPos)){
				
				return false;
			}
		}
		return true;
	}
	
	public boolean isOccludedBy(PriorityQueue<Element> occluder_list, Point cameraPos){
		for(Element e : occluder_list){
			if(this.isOccludedBy(e,cameraPos))
				return true;
		}
		return false;
	}
	
	public void translate(float x, float y, float z) {
		leftFrontBottom.translate(x, y, z);
		rightBackTop.translate(x, y, z);
		compute8Points();
		sphere.translate(x, y, z);
	}
	
	public void setLocation(Point location) {
		Vector translation = new Vector(location.x-sphere.center.x, location.x-sphere.center.y, location.z-sphere.center.z);
		sphere.center = location;
		leftFrontBottom.translate(translation);
		rightBackTop.translate(translation);
		compute8Points();
	}
	
	public Point getLocation() {
		return sphere.center;
	}

	public Point getLeftFrontBottom() {
		return leftFrontBottom;
	}

	public Point getRightBackTop() {
		return rightBackTop;
	}

	public Sphere getSphere() {
		return sphere;
	}
	
	public Point[] getPoints8(){
		return points8;
	}
	
}
