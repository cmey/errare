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

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import geom.Matrix;
import geom.Point;

public abstract class Camera extends Graphics{

	public Point location;
	public Point lookingAt;	
	static public float STD_MOVE_SPEED = 10;
	
	/**
	 * Construct a camera with a given position
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Camera(float posX, float posY, float posZ){
		location = new Point(posX, posY, posZ);
		lookingAt = new Point(location.x, location.y, location.z+1);
	}
	
	/**
	 * Construct a camera with a default position at 100 100 100
	 */
	public Camera() {
		this(0,0,0);
	}


	public Point getLocation() {
		return location;
	}

	public float[] getPositionBuffer(){
		float[] positionbuffer = {location.x,location.y,location.z,1};
		return positionbuffer;
	}

	public Point getLookingAt() {
		return lookingAt;
	}
	
	
	Matrix cameraProjectionMatrix = new Matrix(4,4);
	Matrix cameraViewMatrix = new Matrix(4,4);
	float[] cameraProjectionMatrixBuffer = new float[16];
	float[] cameraViewMatrixBuffer = new float[16];
	
	protected void calculateAndSaveMatrices(GLAutoDrawable gld, int winw, int winh){
		GL gl = gld.getGL();
		GLU glu = new GLU();
		
		gl.glPushMatrix();
	    gl.glLoadIdentity();
	    glu.gluPerspective(45.0f, (float)winw/winh, 1f, ge.view_distance);
	    gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, cameraProjectionMatrixBuffer,0);
	    cameraProjectionMatrix.setArray(cameraProjectionMatrixBuffer);
	    gl.glLoadIdentity();
	    glu.gluLookAt(location.x, location.y, location.z,
	    lookingAt.x, lookingAt.y, lookingAt.z,
	    0.0f, 1.0f, 0.0f);
	    gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, cameraViewMatrixBuffer,0);
	    cameraViewMatrix.setArray(cameraViewMatrixBuffer);
	    gl.glPopMatrix();
	}

	@Override
	protected void draw(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected
	void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
	
	
	// redefined in heritage
	public void rotateY(float angle) {}
	public void rotateXZ(float angle) {}
	public void update() {}
}
