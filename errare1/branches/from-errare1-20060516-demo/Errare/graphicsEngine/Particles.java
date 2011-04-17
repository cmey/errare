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

import java.awt.Point;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;


public abstract class Particles extends GraphicalRep {
	
	protected int num = 50;
	protected float psize = 100;
	protected float spin;
	protected int loop;
	protected star[] stars;
	protected float obj_x,obj_z; //position of the object
	
	
	protected class star{
		public int r, g, b;
		public float dist;
		public float angle;
	}
	
	
	

	public Particles(String rep3D_filename) {
		super(rep3D_filename, GraphicalRep.FORMAT_PARTICLE);
		
	}
	
	
	
	
	/**
	 * Draws the object to the screen given it's position and orientation.
	 * @param gl gl context (given by the GraphicsEngine's display method)
	 * @param px position on x axis
	 * @param py position on y axis
	 * @param pz position on z axis
	 * @param rx rotation over x axis
	 * @param ry rotation over y axis
	 * @param rz rotation over z axis
	 */
	protected void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz){
		GL gl = gld.getGL();
		py+=40; 
		px+=30;
		gl.glPushMatrix();
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		doPosition(gl,obj_x-px,0,obj_z-pz,0,0,0);
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		gl.glEnable(GL.GL_ALPHA_TEST);
		
		//gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE); // Set The Blending Function For Translucency
		gl.glEnable(GL.GL_BLEND);	
		if(prem){
			prem=false;
			doExpandTexture(gl);
		}
		
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		
		gl.glDepthMask(false); 
		
		drawParticles(gl);
		GraphicsEngine.voxel_drawn += num*2;
		
		gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_ALPHA_TEST);
		//gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthMask(true);
		
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
	public void setPosition(Point p){
		obj_x=p.x;
		obj_z=p.y;
	}
	
	protected abstract void drawParticles(GL gl);
	public abstract void tick();
	public abstract void die();
	public abstract void reset();
}
