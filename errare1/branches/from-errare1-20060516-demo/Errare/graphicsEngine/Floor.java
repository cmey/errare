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

public class Floor extends GraphicalRep{
	private int FLOOR_SIZE = 1000;
	private int list_terrain;
	
	
	public Floor(String filename) {
		super(filename,GraphicalRep.FORMAT_FLOOR);	
	}
	
	
	
	
	public void drawSol(GL gl, int px, int py, int pz, int rx, int ry, int rz) {
		gl.glPushMatrix();
		gl.glEnable(GL.GL_TEXTURE_2D);
		doPosition(gl,px,py,pz,rx,ry,rz);
		
		if(prem) {
			prem=false;
			doExpandTexture(gl);
			
			//compiler sol
			if(!gl.glIsTexture(list_terrain))
			list_terrain = gl.glGenLists(1);
			gl.glNewList(list_terrain, GL.GL_COMPILE);
			//gl.glDisable(GL.GL_TEXTURE_2D);
			//gl.glColor3f(0,1,0);
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2i(0,0);
			gl.glVertex3f(0,0,0);
			gl.glTexCoord2i(0,1);
			gl.glVertex3f(0,0,FLOOR_SIZE);
			gl.glTexCoord2i(1,1);
			gl.glVertex3f(FLOOR_SIZE,0,FLOOR_SIZE);
			gl.glTexCoord2i(1,0);
			gl.glVertex3f(FLOOR_SIZE,0,0);
			
			gl.glEnd();
			//gl.glColor3f(1,1,1);
			//gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glEndList();
			
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		// dessiner le sol tout vert
		gl.glCallList(list_terrain);		
		
		gl.glPopMatrix();
	}




	@Override
	protected void draw(GLDrawable gld,float px, float py, float pz, float rx, float ry, float rz) {
		// TODO Auto-generated method stub
		
	}
	
	
}
