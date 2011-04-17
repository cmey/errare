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

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;

public class Water extends GraphicalRep{
	
	protected static float MAP_SIZE = 1000; //unités OpenGL
	private int decalage;
	private int list_water;
	private boolean must_recompile;
	private int texrepet = 10;
	private int sea_level = -2;
	private int image_sea_threshold = 0;
	private float phase = 0f;
	private int count;
	private Heightmap heightmap;
	
	public Water(String filename, int map_size, Heightmap caller) {
		super(filename,GraphicalRep.FORMAT_WATER);
		MAP_SIZE = map_size;
		this.heightmap = caller;
		decalage = 0;
	}
	
	
	public void draw(GLDrawable gld, float x, float y, float z, float rx, float ry, float rz){
		GL gl = gld.getGL();
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();
		if(prem){
			prem=false;
			doExpandTexture(gl);
			compile_water(gl);
		}
		if(must_recompile){
			must_recompile=false;
			compile_water(gl);
		}
		
		gl.glColor4f(1,1,1,0.4f);
		gl.glEnable(GL.GL_BLEND);
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		
		gl.glCallList(list_water);
		
		gl.glPopAttrib();
		gl.glPopMatrix();
		GraphicsEngine.voxel_drawn += heightmap.nb_div*heightmap.nb_div*2;
	}
	
	protected void tick(){
		/*
		count = (count+1) % 60;
		if(count == 0){
			phase += 0.005f;
			must_recompile =true;
		}
		*/
	}
	
	private void drawVertex(GL gl, int i, int j){
		gl.glTexCoord2f((float)i/(heightmap.inter.length-heightmap.step)*texrepet,
						(float)j/(heightmap.inter[0].length-heightmap.step)*texrepet);
		//gl.glNormal3f(inter_norm[i/step][j/step].X,inter_norm[i/step][j/step].Y,inter_norm[i/step][j/step].Z);
		gl.glVertex3f(i/heightmap.step * heightmap.step_world
				+ decalage
				, sea_level,
				j/heightmap.step * heightmap.step_world
				+ decalage);
		//System.out.println(" "+ (-MAP_SIZE/2 + i/step * step_world) +" "+ (inter[i][j]) +" "+ (-MAP_SIZE/2 + j/step * step_world));
		
	}
	
	private void compile_water(GL gl){
		if(!gl.glIsList(list_water)){
			list_water = gl.glGenLists(1);
		}

		gl.glNewList(list_water, GL.GL_COMPILE);
		
		gl.glBegin(GL.GL_TRIANGLES);
		for (int i=0;i<heightmap.inter.length-heightmap.step;i+=heightmap.step) {
			for (int j=0;j<heightmap.inter[0].length-heightmap.step;j+=heightmap.step) {
				if(areaBelowSeaLevel(heightmap.inter,i,j,heightmap.step)){
					// triangle 1
					drawVertex(gl, i,j);
					drawVertex(gl, i+heightmap.step,j);
					drawVertex(gl, i+heightmap.step,j+heightmap.step);
					
					// triangle 2
					drawVertex(gl, i,j);
					drawVertex(gl, i+heightmap.step,j+heightmap.step);
					drawVertex(gl, i,j+heightmap.step);
				}
			}
		}
		gl.glEnd();
		
		/*
		gl.glTexCoord2d(Math.sin(phase)+0,Math.cos(phase)+0);
		gl.glVertex3f(0 +decalage,sea_level,0 +decalage);
		gl.glTexCoord2d(Math.sin(phase)+0,Math.cos(phase)+texrepet);
		gl.glVertex3f(0 +decalage,sea_level,MAP_SIZE +decalage);
		gl.glTexCoord2d(Math.sin(phase)+texrepet,Math.cos(phase)+texrepet);
		gl.glVertex3f(MAP_SIZE +decalage,sea_level,MAP_SIZE +decalage);
		gl.glTexCoord2d(Math.sin(phase)+texrepet,Math.cos(phase)+0);
		gl.glVertex3f(MAP_SIZE +decalage,sea_level,0 +decalage);
		*/
		
		gl.glEndList();
	}
	
	private boolean areaBelowSeaLevel(float[][] inter, int i, int j, int d){
		
		if(inter[i][j]<image_sea_threshold){
			return true;
		//}else if(i-d>=0 && j-d>=0 && inter[i-d][j-d]<image_sea_threshold){
		//	return true;
		//}else if(i>0 && inter[i-d][j]<image_sea_threshold){
		//	return true;
		//}else if(j-d>=0 && inter[i][j-d]<image_sea_threshold){
		//	return true;
		}else if((i+d)<inter.length && inter[i+d][j]<image_sea_threshold){
			return true;
		}else if((j+d)<inter[0].length && inter[i][j+d]<image_sea_threshold){
			return true;
		}else if((i+d)<inter.length && (j+d)<inter[0].length && inter[i+d][j+d]<image_sea_threshold){
			return true;
		//}else if(i-d>=0 && (j+d)<inter[0].length && inter[i-d][j+d]<image_sea_threshold){
		//	return true;
		//}else if(j-d>0 && (i+d)<inter.length && inter[i+d][j-d]<image_sea_threshold){
		//	return true;
		}else
			return false;
	}
	
	
	public void setWater_fromPhysical(Heightmap caller, int map_size, int decalage){
		MAP_SIZE=map_size;
		this.decalage = decalage;
		this.heightmap=caller;
		must_recompile=true;
	}
	
}
