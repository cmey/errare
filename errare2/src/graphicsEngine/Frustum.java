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

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import geom.AABox;
import geom.Point;
import geom.Sphere;

/**
 * A representation for the visualisation volume defined by 6 planes.
 * @author Christophe
 * (http://www.flipcode.com/articles/article_frustumculling.shtml)
 */
public class Frustum {
	Plane near;
	Plane far;
	Plane top;
	Plane bottom;
	Plane left;
	Plane right;
	private float[][] frustum = new float[6][4]; // also called ComboMatrix
	
	/**
	 * Constructor for a visualisation volume defined by 6 planes.
	 * Default = everything in the "zero-plane" a=b=c=d=0.
	 */
	public Frustum(){
		near= new Plane();
		far= new Plane();
		top= new Plane();
		bottom= new Plane();
		left= new Plane();
		right= new Plane();
	}
	
	/**
	 * Constructor for a visualisation volume defined by 6 given planes.
	 * @param n near plane
	 * @param f far plane
	 * @param t top plane
	 * @param b bottom plane
	 * @param l left plane
	 * @param r right plane
	 */
	public Frustum(Plane n, Plane f, Plane t, Plane b, Plane l, Plane r){
		this.near=n;
		this.far=f;
		this.top=t;
		this.bottom=b;
		this.left=f;
		this.right=r;
	}
	
	public void draw(GLAutoDrawable gld){
		
	}
	
	/**
	 * Tests if a point lies in the visualisation volume.
	 * @param x point pos on x
	 * @param y point pos on y
	 * @param z point pos on z
	 * @return true if Point Is in Frustum.
	 */
	public boolean PointInFrustum( float x, float y, float z )
	{
	   int p;

	   for( p = 0; p < 6; p++ )
	      if( frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3] <= 0 )
	         return false;
	   return true;
	}


	/**
	 * Tests if a sphere lies in the visualisation volume.
	 * @param x sphere center pos on x
	 * @param y sphere center pos on y
	 * @param z sphere center pos on z
	 * @param radius sphere radius
	 * @return true if the Sphere Is in Frustum.
	 */
	public boolean sphereInFrustumOrFrustumInSphere( float x, float y, float z, float radius )
	{
	   int p;
	   int count = 0;
	   for( p = 0; p < 6; p++ )
	      if( frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3] <= -radius-0.1f )
	         count++;
	   if(count==6) return true; // FRUSTUM IN SPHERE
	   else if (count>1) return false; // SPHERE ASIDE THE FRUSTUM
	   else return true; // SPHERE IN FRUSTUM
	}
	public boolean sphereInFrustumOrFrustumInSphere( Sphere s){
		return sphereInFrustumOrFrustumInSphere(s.center.x,s.center.y,s.center.z,s.rad);
	}
	
	/**
	 * Tests if a sphere lies in the visualisation volume, also
	 * gives the distance between the external shape of the Sphere
	 * and the Camera.
	 * @param x sphere center pos on x
	 * @param y sphere center pos on y
	 * @param z sphere center pos on z
	 * @param radius sphere radius
	 * @return distance from Camera to Sphere shape
	 */
	public float SphereInFrustum_LOD( float x, float y, float z, float radius )
	{
	   int p;
	   float d = 0f;

	   for( p = 0; p < 6; p++ )
	   {
	      d = frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3];
	      if( d <= -radius )
	         return 0;
	   }
	   return d + radius;
	}

	/**
	 * Tests if a Cube intersects the visualisation volume.
	 * You must Give center of cube and half of cube's length
	 * @param x cube center pos on x
	 * @param y cube center pos on y
	 * @param z cube center pos on z
	 * @param size half of the cube's length
	 * @return true if a part of the cube lies in the frustum
	 */
	public boolean CubeInFrustum( float x, float y, float z, float size )
	{
	   for(int p = 0; p < 6; p++ )
	   {
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         continue;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         continue;
	      return false;
	   }
	   return true;
	}
	
	public boolean AABoxInFrustum(AABox box){
		//TODO: find an algo that test for : AABox in Frustum and Frustum in AAbox (like with the sphere)
		return sphereInFrustumOrFrustumInSphere(box.getSphere());
	}

	/**
	 * Tests if a point is occluded by the geometry rendered so far.
	 * It uses OpenGL Z-buffer to test that.
	 * It does not actually draw the point on-screen.
	 * @param p the point to be tested
	 * @param gl openGL context
	 * @param glu glu context
	 * @return false if the point would not be occluded
	 */
	public boolean IsOccluded(Point p, GL gl, GLU glu)
	{
		int [] viewport = new int[4];							// Space For Viewport Data
		double [] mvmatrix = new double[16];					// Space For Transform Matrix
		double[] projmatrix = new double[16];
		double[] win;						// Space For Returned Projected Coords
		win = new double[3]; // winx winy winz
		double flareZ;							// Here We Will Store The Transformed Flare Z
		float[] bufferZ = new float[1];							// Here We Will Store The Read Z From The Buffer

		gl.glGetIntegerv (GL.GL_VIEWPORT, viewport,0);					// Get Actual Viewport
		gl.glGetDoublev (GL.GL_MODELVIEW_MATRIX, mvmatrix,0);				// Get Actual Model View Matrix
		gl.glGetDoublev (GL.GL_PROJECTION_MATRIX, projmatrix,0);			// Get Actual Projection Matrix

		// This Asks OGL To Guess The 2D Position Of A 3D Point Inside The Viewport
		glu.gluProject(
				p.x, 
				p.y, 
				p.z, 
				mvmatrix, 0,
				projmatrix, 0,
				viewport, 0,
				win, 0);
		flareZ = win[2];

		// We Read Back One Pixel From The Depth Buffer (Exactly Where Our Point Should Be Drawn)
		gl.glReadPixels((int)win[0], (int)win[1],1,1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, FloatBuffer.wrap(bufferZ));

		// If The Buffer Z Is Lower Than Our Point Guessed Z Then Don't Draw
		// This Means There Is Something In Front Of Our Point
		if (bufferZ[0] < flareZ)
			return true;
		else
			return false;
	}

	
	/**
	 * Tests if a point is occluded by the geometry rendered so far,
	 * and returns the 2D position on-screen where it would be projected.
	 * It uses OpenGL Z-buffer to test that.
	 * It does not actually draw the point on-screen.
	 * @param p the point to be tested
	 * @param gl openGL context
	 * @param glu glu context
	 * @return the 2D position on-screen where the point would be projected if non occluded
	 */
	public Point IsOccluded_Coords(Point p, GL gl, GLU glu)
	{
		int [] viewport = new int[4];							// Space For Viewport Data
		double [] mvmatrix = new double[16];					// Space For Transform Matrix
		double[] projmatrix = new double[16];
		double[] win;						// Space For Returned Projected Coords
		win = new double[3]; // winx winy winz
		double flareZ;							// Here We Will Store The Transformed Flare Z
		float[] bufferZ = new float[1];							// Here We Will Store The Read Z From The Buffer

		gl.glGetIntegerv (GL.GL_VIEWPORT, viewport,0);					// Get Actual Viewport
		gl.glGetDoublev (GL.GL_MODELVIEW_MATRIX, mvmatrix,0);				// Get Actual Model View Matrix
		gl.glGetDoublev (GL.GL_PROJECTION_MATRIX, projmatrix,0);			// Get Actual Projection Matrix

		// This Asks OGL To Guess The 2D Position Of A 3D Point Inside The Viewport
		glu.gluProject(
				p.x, 
				p.y, 
				p.z, 
				mvmatrix, 0, 
				projmatrix, 0,
				viewport, 0,
				win, 0);
		flareZ = win[2];

		// We Read Back One Pixel From The Depth Buffer (Exactly Where Our Flare Should Be Drawn)
		gl.glReadPixels((int)win[0], (int)win[1],1,1,GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, FloatBuffer.wrap(bufferZ));

		// If The Buffer Z Is Lower Than Our Flare Guessed Z Then Don't Draw
		// This Means There Is Something In Front Of Our Flare
		if (bufferZ[0] < flareZ)
			return null;
		else
			return new Point((int)win[0], (int)win[1], (int)(win[2]));
	}

	
	/**
	 * Here's a version of the SphereInFrustum() function that returns 0
	 * if the sphere is totally outside, 1 if it's partially inside,
	 * and 2 if it's totally inside.
	 * @param x sphere center pos on x
	 * @param y sphere center pos on y
	 * @param z sphere center pos on z
	 * @param radius sphere radius
	 * @return 0: outside, 1:partial, 2:inside
	 */
	public int SphereInFrustum_ForTree( float x, float y, float z, float radius )
	{
	   int c = 0;
	   float d;

	   for(int p = 0; p < 6; p++ )
	   {
	      d = frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3];
	      if( d <= -radius )
	         return 0;
	      if( d > radius )
	         c++;
	   }
	   return (c == 6) ? 2 : 1;
	}
	public int SphereInFrustum_ForTree(Sphere s){
		return SphereInFrustum_ForTree(s.center.x,s.center.y,s.center.z,s.rad);
	}

	
	/**
	 * Here's a version of the CubeInFrustum() function that returns 0
	 * if the cube is totally outside, 1 if it's partially inside,
	 * and 2 if it's totally inside.
	 * @param x cube center pos on x
	 * @param y cube center pos on y
	 * @param z cube center pos on z
	 * @param size half of the cube's length
	 * @return 0: outside, 1:partial, 2:inside
	 */
	public int CubeInFrustum_ForTree( float x, float y, float z, float size )
	{
	   int p;
	   int c;
	   int c2 = 0;

	   for( p = 0; p < 6; p++ )
	   {
	      c = 0;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         c++;
	      if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0 )
	         c++;
	      if( c == 0 )
	         return 0;
	      if( c == 8 )
	         c2++;
	   }
	   return (c2 == 6) ? 2 : 1;
	}



	public boolean PolygonInFrustum( int numpoints, Point[] pointlist )
	{
	   int f, p;

	   for( f = 0; f < 6; f++ )
	   {
	      for( p = 0; p < numpoints; p++ )
	      {
	         if( frustum[f][0] * pointlist[p].x + frustum[f][1] * pointlist[p].y + frustum[f][2] * pointlist[p].z + frustum[f][3] > 0 )
	            break;
	      }
	      if( p == numpoints )
	         return false;
	   }
	   return true;
	}



	
	/**
	 * 
	 * @param center
	 * @param radius
	 * @return false=OUT true=IN OR INTERSECT
	 */
	/*
	public boolean testIntersectSphere(Point center, float radius){
		float distance;
		distance= right.normal().dot_prod(center) + right.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;
		distance= left.normal().dot_prod(center) + left.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;
		distance= bottom.normal().dot_prod(center) + bottom.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;
		distance= top.normal().dot_prod(center) + top.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;
		distance= near.normal().dot_prod(center) + near.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;
		distance= far.normal().dot_prod(center) + far.distance();
		if(distance < -radius)
			return false;
		else if(Math.abs(distance) < radius)
			return true;

		return true;
	}
	*/
	
	public void ExtractFrustum(GL gl)
	{
	   float[]   proj = new float[16];
	   float[]   modl = new float[16];
	   float[]   clip = new float[16];
	   float   t;

	   /* Get the current PROJECTION matrix from OpenGL */
	   gl.glGetFloatv( GL.GL_PROJECTION_MATRIX, proj ,0);

	   /* Get the current MODELVIEW matrix from OpenGL */
	   gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, modl ,0);

	   /* Combine the two matrices (multiply projection by modelview) */
	   clip[ 0] = modl[ 0] * proj[ 0] + modl[ 1] * proj[ 4] + modl[ 2] * proj[ 8] + modl[ 3] * proj[12];
	   clip[ 1] = modl[ 0] * proj[ 1] + modl[ 1] * proj[ 5] + modl[ 2] * proj[ 9] + modl[ 3] * proj[13];
	   clip[ 2] = modl[ 0] * proj[ 2] + modl[ 1] * proj[ 6] + modl[ 2] * proj[10] + modl[ 3] * proj[14];
	   clip[ 3] = modl[ 0] * proj[ 3] + modl[ 1] * proj[ 7] + modl[ 2] * proj[11] + modl[ 3] * proj[15];

	   clip[ 4] = modl[ 4] * proj[ 0] + modl[ 5] * proj[ 4] + modl[ 6] * proj[ 8] + modl[ 7] * proj[12];
	   clip[ 5] = modl[ 4] * proj[ 1] + modl[ 5] * proj[ 5] + modl[ 6] * proj[ 9] + modl[ 7] * proj[13];
	   clip[ 6] = modl[ 4] * proj[ 2] + modl[ 5] * proj[ 6] + modl[ 6] * proj[10] + modl[ 7] * proj[14];
	   clip[ 7] = modl[ 4] * proj[ 3] + modl[ 5] * proj[ 7] + modl[ 6] * proj[11] + modl[ 7] * proj[15];

	   clip[ 8] = modl[ 8] * proj[ 0] + modl[ 9] * proj[ 4] + modl[10] * proj[ 8] + modl[11] * proj[12];
	   clip[ 9] = modl[ 8] * proj[ 1] + modl[ 9] * proj[ 5] + modl[10] * proj[ 9] + modl[11] * proj[13];
	   clip[10] = modl[ 8] * proj[ 2] + modl[ 9] * proj[ 6] + modl[10] * proj[10] + modl[11] * proj[14];
	   clip[11] = modl[ 8] * proj[ 3] + modl[ 9] * proj[ 7] + modl[10] * proj[11] + modl[11] * proj[15];

	   clip[12] = modl[12] * proj[ 0] + modl[13] * proj[ 4] + modl[14] * proj[ 8] + modl[15] * proj[12];
	   clip[13] = modl[12] * proj[ 1] + modl[13] * proj[ 5] + modl[14] * proj[ 9] + modl[15] * proj[13];
	   clip[14] = modl[12] * proj[ 2] + modl[13] * proj[ 6] + modl[14] * proj[10] + modl[15] * proj[14];
	   clip[15] = modl[12] * proj[ 3] + modl[13] * proj[ 7] + modl[14] * proj[11] + modl[15] * proj[15];

	   /* Extract the numbers for the RIGHT plane */
	   frustum[0][0] = clip[ 3] - clip[ 0];
	   frustum[0][1] = clip[ 7] - clip[ 4];
	   frustum[0][2] = clip[11] - clip[ 8];
	   frustum[0][3] = clip[15] - clip[12];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[0][0] * frustum[0][0] + frustum[0][1] * frustum[0][1] + frustum[0][2] * frustum[0][2] );
	   frustum[0][0] /= t;
	   frustum[0][1] /= t;
	   frustum[0][2] /= t;
	   frustum[0][3] /= t;

	   /* Extract the numbers for the LEFT plane */
	   frustum[1][0] = clip[ 3] + clip[ 0];
	   frustum[1][1] = clip[ 7] + clip[ 4];
	   frustum[1][2] = clip[11] + clip[ 8];
	   frustum[1][3] = clip[15] + clip[12];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[1][0] * frustum[1][0] + frustum[1][1] * frustum[1][1] + frustum[1][2] * frustum[1][2] );
	   frustum[1][0] /= t;
	   frustum[1][1] /= t;
	   frustum[1][2] /= t;
	   frustum[1][3] /= t;

	   /* Extract the BOTTOM plane */
	   frustum[2][0] = clip[ 3] + clip[ 1];
	   frustum[2][1] = clip[ 7] + clip[ 5];
	   frustum[2][2] = clip[11] + clip[ 9];
	   frustum[2][3] = clip[15] + clip[13];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[2][0] * frustum[2][0] + frustum[2][1] * frustum[2][1] + frustum[2][2] * frustum[2][2] );
	   frustum[2][0] /= t;
	   frustum[2][1] /= t;
	   frustum[2][2] /= t;
	   frustum[2][3] /= t;

	   /* Extract the TOP plane */
	   frustum[3][0] = clip[ 3] - clip[ 1];
	   frustum[3][1] = clip[ 7] - clip[ 5];
	   frustum[3][2] = clip[11] - clip[ 9];
	   frustum[3][3] = clip[15] - clip[13];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[3][0] * frustum[3][0] + frustum[3][1] * frustum[3][1] + frustum[3][2] * frustum[3][2] );
	   frustum[3][0] /= t;
	   frustum[3][1] /= t;
	   frustum[3][2] /= t;
	   frustum[3][3] /= t;

	   /* Extract the FAR plane */
	   frustum[4][0] = clip[ 3] - clip[ 2];
	   frustum[4][1] = clip[ 7] - clip[ 6];
	   frustum[4][2] = clip[11] - clip[10];
	   frustum[4][3] = clip[15] - clip[14];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[4][0] * frustum[4][0] + frustum[4][1] * frustum[4][1] + frustum[4][2] * frustum[4][2] );
	   frustum[4][0] /= t;
	   frustum[4][1] /= t;
	   frustum[4][2] /= t;
	   frustum[4][3] /= t;

	   /* Extract the NEAR plane */
	   frustum[5][0] = clip[ 3] + clip[ 2];
	   frustum[5][1] = clip[ 7] + clip[ 6];
	   frustum[5][2] = clip[11] + clip[10];
	   frustum[5][3] = clip[15] + clip[14];

	   /* Normalize the result */
	   t = (float) Math.sqrt( frustum[5][0] * frustum[5][0] + frustum[5][1] * frustum[5][1] + frustum[5][2] * frustum[5][2] );
	   frustum[5][0] /= t;
	   frustum[5][1] /= t;
	   frustum[5][2] /= t;
	   frustum[5][3] /= t;
	}

	/**
	 * A representation for the plane equation.
	 * @author Christophe
	 */
	private class Plane{
		float a;
		float b;
		float c;
		float d;
		
		public Plane(){
			
		}
		
		public Plane(float a, float b, float c, float d){
			this.a=a;
			this.b=b;
			this.c=c;
			this.d=d;
		}
		
		public void NormalizePlane(){
			float mag;
			mag = (float)Math.sqrt(a*a + b*b + c*c);
			a=a/mag;
			b=b/mag;
			c=c/mag;
			d=d/mag;
		}
		
		public Point normal(){
			return new Point(a,b,c);
		}
		
		public float distance(){
			double res;
			res = d / (Math.sqrt(a*a + b*b + c*c));
			return (float)res;
		}
	}
}
