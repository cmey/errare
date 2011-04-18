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


public class Triangle extends Debug implements Serializable{
	
	private static final long serialVersionUID = -4417938249585379418L;
	
	public Point[] points;
	private Vector normal;
	
	public Triangle() {
		points = new Point[3];
		
		computeNormal();
		normal.normalize();
	}
	
	public  Triangle(Point a, Point b, Point c) {
		points = new Point[3];
		points[0]=a;
		points[1]=b;
		points[2]=c;
		
		computeNormal();
		normal.normalize();
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
		
	}
	
	public void translate(float x, float y, float z) {
		points[0].translate(x, y, z);
		points[1].translate(x, y, z);
		points[2].translate(x, y, z);
		
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
	
	
	public Vector getNormal(){
		return normal;
	}
	
	
	
	/**
	 * Get the closest point of the 3 points of the triangle to the pick point
	 */
	public Point closestPoint(Point pick){
		float distance = points[0].distanceSquare(pick);
		Point ret = points[0];
		
		float distanceTemp = points[1].distanceSquare(pick);
		if(distanceTemp < distance){
			ret = points[1];
			distance = distanceTemp;
		}
		
		distanceTemp = points[2].distanceSquare(pick);
		if(distanceTemp < distance){
			ret = points[2];
		}
		return ret;
	}
	
	/**
	 * Thanks to Tomas Akenine-Moller
	 * http://jgt.akpeters.com/papers/AkenineMoller01/tribox.html
	 * 
	 * @param b
	 * @return
	 */
	public boolean overlap(AABox b) {
		/*    use separating axis theorem to test overlap between triangle and box */
		/*    need to test for overlap in these directions: */
		/*    1) the {x,y,z}-directions (actually, since we use the AABB of the triangle */
		/*       we do not even need to test these) */
		/*    2) normal of the triangle */
		/*    3) crossproduct(edge from tri, {x,y,z}-directin) */
		/*       this gives 3x3=9 more tests */
		//float axis[3];
		//float min,max,d,p0,p1,p2,rad,fex,fey,fez;  
		//float normal[3],e0[3],e1[3],e2[3];
		
		
		/* move everything so that the boxcenter is in (0,0,0) */
		Point boxCenter = b.getCenter();
		Point v0 = new Point(points[0].x-boxCenter.x, points[0].y-boxCenter.y, points[0].z-boxCenter.z);
		Point v1 = new Point(points[1].x-boxCenter.x, points[1].y-boxCenter.y, points[1].z-boxCenter.z);
		Point v2 = new Point(points[2].x-boxCenter.x, points[2].y-boxCenter.y, points[2].z-boxCenter.z);
		
		
		/* compute triangle edges */
		Vector e0 = new Vector(points[1], points[0]);
		Vector e1 = new Vector(points[2], points[1]);
		Vector e2 = new Vector(points[0], points[2]);
		
		float[] boxhalfsize = new float[3];
		boxhalfsize[0] = (b.getRightBackTop().x-b.getLeftFrontBottom().x)/2;
		boxhalfsize[1] = (b.getRightBackTop().y-b.getLeftFrontBottom().y)/2;
		boxhalfsize[2] = (b.getRightBackTop().z-b.getLeftFrontBottom().z)/2;
		
		
		float min, max;
		
		/* Bullet 3:  */
		/*  test the 9 tests first (this was faster) */
		float fex = Math.abs(e0.x);
		float fey = Math.abs(e0.y);
		float fez = Math.abs(e0.z);		   
		float p0 = e0.z*v0.y - e0.y*v0.z;			       	   
		float p2 = e0.z*v2.y - e0.y*v2.z;			       	   
		if(p0<p2) {min=p0; max=p2;} else {min=p2; max=p0;} 
		float rad = fez * boxhalfsize[1] + fey * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		p0 = -e0.z*v0.x + e0.x*v0.z;		      	   
		p2 = -e0.z*v2.x + e0.x*v2.z;	       	       	   
		if(p0<p2) {min=p0; max=p2;} else {min=p2; max=p0;} 
		rad = fez * boxhalfsize[0] + fex * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		float p1 = e0.y*v1.x - e0.x*v1.y;			           
		p2 = e0.y*v2.x - e0.x*v2.y;			       	   
		if(p2<p1) {min=p2; max=p1;} else {min=p1; max=p2;} 
		rad = fey * boxhalfsize[0] + fex * boxhalfsize[1];   
		if(min>rad || max<-rad) return false;
		
		fex = Math.abs(e1.x);
		fey = Math.abs(e1.y);
		fez = Math.abs(e1.z);		   
		p0 = e1.z*v0.y - e1.y*v0.z;			       	   
		p2 = e1.z*v2.y - e1.y*v2.z;			       	   
		if(p0<p2) {min=p0; max=p2;} else {min=p2; max=p0;} 
		rad = fez * boxhalfsize[1] + fey * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		p0 = -e1.z*v0.x + e1.x*v0.z;		      	   
		p2 = -e1.z*v2.x + e1.x*v2.z;	       	       	   
		if(p0<p2) {min=p0; max=p2;} else {min=p2; max=p0;} 
		rad = fez * boxhalfsize[0] + fex * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		p0 = e1.y*v0.x - e1.x*v0.y;				   
		p1 = e1.y*v1.x - e1.x*v1.y;			           
		if(p0<p1) {min=p0; max=p1;} else {min=p1; max=p0;} 
		rad = fey * boxhalfsize[0] + fex * boxhalfsize[1];   
		if(min>rad || max<-rad) return false;
		
		fex = Math.abs(e2.x);
		fey = Math.abs(e2.y);
		fez = Math.abs(e2.z);			   
		p0 = e2.z*v0.y - e2.y*v0.z;			           
		p1 = e2.z*v1.y - e2.y*v1.z;			       	   
		if(p0<p1) {min=p0; max=p1;} else {min=p1; max=p0;} 
		rad = fez * boxhalfsize[1] + fey * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		p0 = -e2.z*v0.x + e2.x*v0.z;		      	   
		p1 = -e2.z*v1.x + e2.x*v1.z;	     	       	   
		if(p0<p1) {min=p0; max=p1;} else {min=p1; max=p0;} 
		rad = fez * boxhalfsize[0] + fex * boxhalfsize[2];   
		if(min>rad || max<-rad) return false;
		
		p1 = e2.y*v1.x - e2.x*v1.y;			           
		p2 = e2.y*v2.x - e2.x*v2.y;			       	   
		if(p2<p1) {min=p2; max=p1;} else {min=p1; max=p2;} 
		rad = fey * boxhalfsize[0] + fex * boxhalfsize[1];   
		if(min>rad || max<-rad) return false;
		
		/* Bullet 1: */
		/*  first test overlap in the {x,y,z}-directions */
		/*  find min, max of the triangle each direction, and test for overlap in */
		/*  that direction -- this is equivalent to testing a minimal AABB around */
		/*  the triangle against the AABB */
		
		/* test in X-direction */
		
		
		min = max = v0.x;   
		if(v1.x<min) min=v1.x;
		if(v1.x>max) max=v1.x;
		if(v2.x<min) min=v2.x;
		if(v2.x>max) max=v2.x;
		
		
		if(min>boxhalfsize[0] || max<-boxhalfsize[0]) return false;
		
		/* test in Y-direction */
		min = max = v0.y;   
		if(v1.y<min) min=v1.y;
		if(v1.y>max) max=v1.y;
		if(v2.y<min) min=v2.y;
		if(v2.y>max) max=v2.y;
		
		if(min>boxhalfsize[1] || max<-boxhalfsize[1]) return false;
		
		/* test in Z-direction */
		min = max = v0.z;   
		if(v1.z<min) min=v1.z;
		if(v1.z>max) max=v1.z;
		if(v2.z<min) min=v2.z;
		if(v2.z>max) max=v2.z;
		
		if(min>boxhalfsize[2] || max<-boxhalfsize[2]) return false;
		
		/* Bullet 2: */
		/*  test if the box intersects the plane of the triangle */
		/*  compute plane equation of triangle: normal*x+d=0 */
		Vector mynormal = Vector.crossProduct(e0,e1);
		float d = -Vector.dotProduct(mynormal, new Vector(v0));  /* plane eq: normal.x+d=0 */
		if(!AABoxOverlapUtils.planeBoxOverlap(new float[] {mynormal.x, mynormal.y, mynormal.z},d,boxhalfsize))
            return false;
		
		return true;   /* box and triangle overlaps */
		
	}
	
	private static class AABoxOverlapUtils {
		public static boolean planeBoxOverlap(float[] normal,float d, float[] maxbox)
		{
			int q;
			float vmin[] = new float[3],vmax[] = new float[3];
			for(q=0;q<=2;q++)
			{
				if(normal[q]>0.0f)
				{
					vmin[q]=-maxbox[q];
					vmax[q]=maxbox[q];
				}
				else
				{
					vmin[q]=maxbox[q];
					vmax[q]=-maxbox[q];
				}
			}
			if(Vector.dotProduct(new Vector(normal), new Vector(vmin))+d>0.0f) return false;
			if(Vector.dotProduct(new Vector(normal), new Vector(vmax))+d>=0.0f) return true;
			
			return false;
		}
		
		
		
		
	}
	
	
	
	
	public String toString() {
		return "("+points[0]+" "+points[1]+" "+points[2]+")";
	}
}