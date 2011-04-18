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

import java.io.Serializable;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.BufferUtil;

import geom.Point;
import geom.Sphere;

@SuppressWarnings("serial")
public class MeshData implements Serializable{

	public String name;
	transient public boolean VBOinitialized;
	transient public int[] VBOvertices;
	transient public int[] VBOnormals;
	
	public float[] vertices; // must be 3*MaxValue(topology) long (vertices consists of xyz chunks)
	public float [] normals; // must be vertices.length long (there is one normal per vertex) and normals consists of xyz chunks
	
	/* boundaries of current shape */
	protected Point sphereCenter;
	protected float radius;
	protected float verticeXmaximum, verticeXminimum;
	protected float verticeYmaximum, verticeYminimum;
	protected float verticeZmaximum, verticeZminimum;
	
	
	
	public MeshData(float[] vertices, float[] normals) {
		this.vertices = vertices;
		// calculate boundaries
		verticeXmaximum = Float.MIN_VALUE; verticeXminimum = Float.MAX_VALUE;
		verticeYmaximum = Float.MIN_VALUE; verticeYminimum = Float.MAX_VALUE;
		verticeZmaximum = Float.MIN_VALUE; verticeZminimum = Float.MAX_VALUE;
		for(int i=0; i<vertices.length; i+=3){
			if(vertices[i]>verticeXmaximum) verticeXmaximum = vertices[i];
			if(vertices[i]<verticeXminimum) verticeXminimum = vertices[i];
			if(vertices[i+1]>verticeYmaximum) verticeYmaximum = vertices[i+1];
			if(vertices[i+1]<verticeYminimum) verticeYminimum = vertices[i+1];
			if(vertices[i+2]>verticeZmaximum) verticeZmaximum = vertices[i+2];
			if(vertices[i+2]<verticeZminimum) verticeZminimum = vertices[i+2];
		}
		this.sphereCenter = new Point((verticeXmaximum+verticeXminimum) / 2,(verticeYmaximum+verticeYminimum) / 2, (verticeZmaximum+verticeZminimum) / 2);
		Point max = new Point(verticeXmaximum, verticeZmaximum, verticeZmaximum);
		this.radius = max.distance(sphereCenter);
		
		this.normals = normals;
	}

	/**
	 * Gives the bounds of the object's mesh. Respectively : x then z then y and respectively minimum
	 * then maximum. For a 2D rep, only the first four values are useful.
	 * @return Xmin, Xmax, Ymin, Ymax, Zmin, Zmax
	 */
	public float[] getGraphicalBoundaries(){
		float[] bounds = {verticeXminimum, verticeXmaximum, verticeYminimum, verticeYmaximum, verticeZminimum, verticeZmaximum};
		return bounds;
	}
	
	public Sphere getBoundingSphere(){
		return new Sphere(sphereCenter, radius);
	}
	
	
	protected void createVBOs(GL gl){
		if(!Extensions.isVBOSupported) return;
		// vertex data
		VBOvertices = new int[1];
		gl.glGenBuffersARB(1, this.VBOvertices, 0);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, VBOvertices[0]);
		gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, vertices.length * BufferUtil.SIZEOF_FLOAT, FloatBuffer.wrap(vertices), GL.GL_STATIC_DRAW_ARB);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
		
		// normal data
		VBOnormals = new int[1];
		gl.glGenBuffersARB(1, this.VBOnormals, 0);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, VBOnormals[0]);
		gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, normals.length * BufferUtil.SIZEOF_FLOAT, FloatBuffer.wrap(normals), GL.GL_STATIC_DRAW_ARB);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
		this.VBOinitialized = true;
	}
	
	/**
	 * free from video memory
	 * @param gl
	 */
	protected void free(GL gl){
		this.VBOinitialized = false;
		gl.glDeleteBuffersARB(1, this.VBOvertices,0);
		gl.glDeleteBuffersARB(1, this.VBOnormals,0);
	}
	
	/**
	 * free this object from main memory
	 * (deprecated method : you must prefer to set the MeshData object to null directly)
	 */
	protected void free(){
		this.vertices = null;
		this.normals = null;
		//this.name = null;
		this.sphereCenter = null;
	}
	
}
