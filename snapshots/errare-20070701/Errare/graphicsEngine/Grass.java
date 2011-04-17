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

public class Grass extends GraphicalRep {

	
	private int list_herbe;
	private int list_herbes;
	private int vent_herbe;
	
	
	public Grass(String filename) {
		super(filename, GraphicalRep.FORMAT_GRASS);
	}
	
	

	
	public void drawHerbe(GL gl, int px, int py, int pz, int rx, int ry, int rz) {
		gl.glPushMatrix();
		
		//gl.glDisable(GL.GL_CULL_FACE);
		
		gl.glEnable(GL.GL_BLEND);
		if(prem) {
			prem=false;
			doExpandTexture(gl);
			
			//compiler herbe
			if(!gl.glIsTexture(list_herbe))
				list_herbe = gl.glGenLists(1);
			gl.glNewList(list_herbe, GL.GL_COMPILE);
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0,0);gl.glVertex3d (0,0,0);
			gl.glTexCoord2f(10,0);gl.glVertex3d (1000,0,0);
			gl.glTexCoord2f(10,1);gl.glVertex3d (1000,15,0);
			gl.glTexCoord2f(0,1);gl.glVertex3d (0,15,0);
			gl.glEnd();
			gl.glEndList();
			
			//compiler les herbes
			//list_herbes = gl.glGenLists(1);
			//gl.glNewList(list_herbes, GL.GL_COMPILE);
			
			//gl.glEndList();
		}
		
		
		
		// dessiner herbes
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		
		
		gl.glTranslated(-500,0,-500);
		gl.glPushMatrix();
		for(int x=0; x<50; x++) {
			gl.glPushMatrix();
			gl.glRotated(Math.sin((vent_herbe++)/2)*10,1,0,0);
			//
			gl.glCallList(list_herbe);
			gl.glPopMatrix();
			gl.glTranslatef(0,0,20);
		}
		gl.glPopMatrix();
		gl.glTranslated(0,0,1000);
		gl.glRotated(90,0,1,0);
		for(int x=0; x<50; x++) {
			gl.glPushMatrix();
			gl.glRotated(Math.sin((vent_herbe++)/2)*10,1,0,0);
			//
			gl.glCallList(list_herbe);
			gl.glPopMatrix();
			gl.glTranslatef(0,0,20);
		}
		
		//gl.glEnable(GL.GL_CULL_FACE);
		gl.glDisable(GL.GL_BLEND);
		gl.glPopMatrix();
	}




	@Override
	protected void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		// TODO Auto-generated method stub
		
	}
	
}
