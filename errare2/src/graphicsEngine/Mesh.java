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
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.BufferUtil;


public abstract class Mesh implements Serializable{

	protected String path; // path to the file on disk
	protected boolean VBOinitialized;
	protected boolean textured = true;

	/** first dimension : when multitexturing, each texture has an entry here
	 *  second dimension : all indexed (u,v) pairs for that texture.
	 */
	protected float[][] texcoords; // indexed by topology. size must be equal to max element value*2 of topology. and equal to meshData's vertices length/3*2.
	protected int[] topology;	// list of indices representing the points of the triangles
	
	// VBO names :
	protected int[] VBOindices;	// name of the VBO for the indices
 	protected int[] VBOtexcoords;	// names if the VBO's for the texcoords
 	
 	
 	
 	protected void constructVBOnames(){
		if(!Extensions.isVBOSupported) return;
		if(textured) { this.VBOtexcoords = new int[texcoords.length]; } // one VBOtexcoords for each texture
		this.VBOindices = new int[1];
	}
	
 	abstract protected void createVBOs(GL gl);
 	
	protected void createVBOsSuper(GL gl){
		constructVBOnames();
		if(!Extensions.isVBOSupported) return;
		// texcoords data
		// for every texture, get a separate VBO for the texcoords
		if(textured) {
			gl.glGenBuffersARB(texcoords.length, this.VBOtexcoords, 0);
			// for every texture
			for(int i=0; i<texcoords.length; i++){
				if(texcoords[i]==null) continue;
				// fill the corresponding VBO with texcoords
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, VBOtexcoords[i]);
				gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, texcoords[i].length * BufferUtil.SIZEOF_FLOAT, FloatBuffer.wrap(texcoords[i]), GL.GL_STATIC_DRAW_ARB);
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
			}
		}
		// indices data
		gl.glGenBuffersARB(1, this.VBOindices, 0);
		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, VBOindices[0]);
		gl.glBufferDataARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, topology.length * BufferUtil.SIZEOF_INT, IntBuffer.wrap(topology), GL.GL_STATIC_DRAW_ARB);
		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
		this.VBOinitialized = true;
	}
	
	/**
	 * free this mesh from video memory
	 * @param gl
	 */
	abstract protected void free(GL gl);
	
	/**
	 * must only be used by the heritage of Mesh.
	 * partialy free mesh from video memory
	 * @param gl
	 */
	protected void freeMeshSuper(GL gl){
		gl.glDeleteBuffersARB(1, VBOindices, 0);
		if(textured) gl.glDeleteBuffersARB(texcoords.length, VBOtexcoords, 0);
	}
	
	/**
	 * free this object from main memory
	 * (deprecated method : you must prefer to set the Mesh variable to null directly)
	 */
	abstract protected void free();
	
	/**
	 * must only be used by the heritage of Mesh.
	 * partialy free mesh from main memory
	 * (deprecated : prefer set the Mesh variable to null directly)
	 */
	protected void freeMeshSuper(){
		
	}
	
	abstract protected MeshData getMeshData();
	
	abstract public float[] getGraphicalBoundaries();
	
	abstract protected Mesh collectorClone();
	
	public int[] getTopology(){
		return topology;
	}
	
	public void setFilename(String path){
		this.path = path;
	}
}
