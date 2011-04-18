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

import javax.media.opengl.GL;

@SuppressWarnings("serial")
public class StaticMesh extends Mesh implements Serializable {
	
	private MeshData meshdata;



	public StaticMesh(int[] topology, float[] vertices, float[] normals, float[][] texcoords) {
		// call the other constructor
		this(topology, new MeshData(vertices, normals), texcoords);
	}
	
	public StaticMesh(int[] topology, MeshData meshdata, float[][] texcoords) {
		this.topology = topology;
		this.meshdata = meshdata;
		this.texcoords = texcoords;
		if(texcoords == null || texcoords[0]==null) { this.textured = false; }
	}

	@Override
	protected MeshData getMeshData() {
		return meshdata;
	}

	@Override
	public float[] getGraphicalBoundaries() {
		return meshdata.getGraphicalBoundaries();
	}
	
	@Override
	protected StaticMesh collectorClone() {
		StaticMesh r = new StaticMesh(this.topology, this.meshdata, this.texcoords);
		r.setFilename(this.path);
		return r;
	}
	
	protected void createVBOs(GL gl){
		if(!Extensions.isVBOSupported) return;
		meshdata.createVBOs(gl);
		super.createVBOsSuper(gl);
	}
	
	protected void free(GL gl){
		meshdata.free(gl);
		this.freeMeshSuper(gl);
	}
	
	protected void free(){
		meshdata.free();
		this.freeMeshSuper();
	}
	
}
