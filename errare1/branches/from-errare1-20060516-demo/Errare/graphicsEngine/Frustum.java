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

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

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
	float[] comboMatrix;
	
	public Frustum(){
		near= new Plane();
		far= new Plane();
		top= new Plane();
		bottom= new Plane();
		left= new Plane();
		right= new Plane();
	}
	public Frustum(Plane n, Plane f, Plane t, Plane b, Plane l, Plane r){
		this.near=n;
		this.far=f;
		this.top=t;
		this.bottom=b;
		this.left=f;
		this.right=r;
	}
	
	public void draw(GLDrawable gld){
		
	}
	
	public boolean PointInFrustum( float x, float y, float z )
	{
	   int p;

	   for( p = 0; p < 6; p++ )
	      if( frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3] <= 0 )
	         return false;
	   return true;
	}

	public boolean SphereInFrustum( float x, float y, float z, float radius )
	{
	   int p;

	   for( p = 0; p < 6; p++ )
	      if( frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3] <= -radius )
	         return false;
	   return true;
	}
	
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
	 * You must Give center of cube and half of cube's length
	 * @param x
	 * @param y
	 * @param z
	 * @param size half of the cube's length
	 * @return
	 */
	public boolean CubeInFrustum( float x, float y, float z, float size )
	{
	   int p;

	   for( p = 0; p < 6; p++ )
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


	public boolean IsOccluded(Point3D p, GL2 gl, GLU glu)
	{
		int [] viewport = new int[4];							// Space For Viewport Data
		double [] mvmatrix = new double[16];					// Space For Transform Matrix
		double[] projmatrix = new double[16];
		double[] win = new double[3];;						// Space For Returned Projected Coords
		
		double flareZ;							// Here We Will Store The Transformed Flare Z
		IntBuffer bufferZ = IntBuffer.allocate(3);							// Here We Will Store The Read Z From The Buffer

		gl.glGetIntegerv (GL.GL_VIEWPORT, viewport, 0);					// Get Actual Viewport
		gl.glGetDoublev (GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);				// Get Actual Model View Matrix
		gl.glGetDoublev (GL2.GL_PROJECTION_MATRIX, projmatrix, 0);			// Get Actual Projection Matrix

		// This Asks OGL To Guess The 2D Position Of A 3D Point Inside The Viewport
		glu.gluProject(
				p.X, 
				p.Y, 
				p.Z, 
				mvmatrix, 0,
				projmatrix, 0,
				viewport, 0,
				win, 0);
		flareZ = win[0];

		// We Read Back One Pixel From The Depth Buffer (Exactly Where Our Flare Should Be Drawn)
		gl.glReadPixels((int)win[0], (int)win[0],1,1,GL2.GL_DEPTH_COMPONENT, GL.GL_FLOAT, bufferZ);

		// If The Buffer Z Is Lower Than Our Flare Guessed Z Then Don't Draw
		// This Means There Is Something In Front Of Our Flare
		if (bufferZ.get() < flareZ)
			return true;
		else
			return false;
	}

	
	public Point3D IsOccluded_Coords(Point3D p, GL2 gl, GLU glu)
	{
		int [] viewport = new int[4];							// Space For Viewport Data
		double [] mvmatrix = new double[16];					// Space For Transform Matrix
		double[] projmatrix = new double[16];
		double[] win = new double[3];					// Space For Returned Projected Coords
		double flareZ;							// Here We Will Store The Transformed Flare Z
		IntBuffer bufferZ = IntBuffer.allocate(3);							// Here We Will Store The Read Z From The Buffer

		gl.glGetIntegerv (GL.GL_VIEWPORT, viewport, 0);					// Get Actual Viewport
		gl.glGetDoublev (GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);				// Get Actual Model View Matrix
		gl.glGetDoublev (GL2.GL_PROJECTION_MATRIX, projmatrix, 0);			// Get Actual Projection Matrix

		// This Asks OGL To Guess The 2D Position Of A 3D Point Inside The Viewport
		glu.gluProject(
				p.X, 
				p.Y, 
				p.Z, 
				mvmatrix, 0,
				projmatrix, 0,
				viewport, 0,
				win, 0);
		flareZ = win[0];

		// We Read Back One Pixel From The Depth Buffer (Exactly Where Our Flare Should Be Drawn)
		gl.glReadPixels((int)win[0], (int)win[1],1,1,GL2.GL_DEPTH_COMPONENT, GL.GL_FLOAT, bufferZ);

		// If The Buffer Z Is Lower Than Our Flare Guessed Z Then Don't Draw
		// This Means There Is Something In Front Of Our Flare
		if (bufferZ.get() < flareZ)
			return null;
		else
			return new Point3D((int)win[0], (int)win[1], (int)(win[2]));
	}

	
	/**
	 * Here's a version of the SphereInFrustum() function that returns 0
	 * if the sphere is totally outside, 1 if it's partially inside,
	 * and 2 if it's totally inside.
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return
	 */
	public int SphereInFrustum_ForTree( float x, float y, float z, float radius )
	{
	   int p;
	   int c = 0;
	   float d;

	   for( p = 0; p < 6; p++ )
	   {
	      d = frustum[p][0] * x + frustum[p][1] * y + frustum[p][2] * z + frustum[p][3];
	      if( d <= -radius )
	         return 0;
	      if( d > radius )
	         c++;
	   }
	   return (c == 6) ? 2 : 1;
	}

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



	public boolean PolygonInFrustum( int numpoints, Point3D[] pointlist )
	{
	   int f, p;

	   for( f = 0; f < 6; f++ )
	   {
	      for( p = 0; p < numpoints; p++ )
	      {
	         if( frustum[f][0] * pointlist[p].X + frustum[f][1] * pointlist[p].Y + frustum[f][2] * pointlist[p].Z + frustum[f][3] > 0 )
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
	public boolean testIntersectSphere(Point3D center, float radius){
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

		//System.out.println(true);
		return true;
	}
	*/
	
	public void setMatrix(float[] m){
		comboMatrix=m;
	}
	
	private float[][] frustum = new float[6][4];
	public void ExtractFrustum(GL2 gl)
	{
	   float[]   proj = new float[16];
	   float[]   modl = new float[16];
	   float[]   clip = new float[16];
	   float   t;

	   /* Get the current PROJECTION matrix from OpenGL */
	   gl.glGetFloatv( GL2.GL_PROJECTION_MATRIX, proj, 0);

	   /* Get the current MODELVIEW matrix from OpenGL */
	   gl.glGetFloatv( GL2.GL_MODELVIEW_MATRIX, modl, 0);

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


	
	
	/*
	public void extractPlanes(){
		// left clipping plane
		left.a = comboMatrix[3] + comboMatrix[0];
		left.b = comboMatrix[7] + comboMatrix[4];
		left.c = comboMatrix[11] + comboMatrix[8];
		left.d = comboMatrix[15] + comboMatrix[12];
		left.NormalizePlane();
		// Right clipping plane
		right.a = comboMatrix[3] - comboMatrix[0];
		right.b = comboMatrix[7] - comboMatrix[4];
		right.c = comboMatrix[11] - comboMatrix[8];
		right.d = comboMatrix[15] - comboMatrix[12];
		right.NormalizePlane();
		// Top clipping plane
		top.a = comboMatrix[3] - comboMatrix[1];
		top.b = comboMatrix[7] - comboMatrix[5];
		top.c = comboMatrix[11] - comboMatrix[9];
		top.d = comboMatrix[15] - comboMatrix[13];
		top.NormalizePlane();
		// Bottom clipping plane
		bottom.a = comboMatrix[3] + comboMatrix[1];
		bottom.b = comboMatrix[7] + comboMatrix[5];
		bottom.c = comboMatrix[11] + comboMatrix[9];
		bottom.d = comboMatrix[15] + comboMatrix[13];
		bottom.NormalizePlane();
		// Near clipping plane
		near.a = comboMatrix[3] + comboMatrix[2];
		near.b = comboMatrix[7] + comboMatrix[6];
		near.c = comboMatrix[11] + comboMatrix[10];
		near.d = comboMatrix[15] + comboMatrix[14];
		near.NormalizePlane();
		// Far clipping plane
		far.a = comboMatrix[3] - comboMatrix[2];
		far.b = comboMatrix[7] - comboMatrix[6];
		far.c = comboMatrix[11] - comboMatrix[10];
		far.d = comboMatrix[15] - comboMatrix[14];
		far.NormalizePlane();
	}
	*/
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
		
		public Point3D normal(){
			return new Point3D(a,b,c);
		}
		
		public float distance(){
			double res;
			res = d / (Math.sqrt(a*a + b*b + c*c));
			return (float)res;
		}
	}
}
