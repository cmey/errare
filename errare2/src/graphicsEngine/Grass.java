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


public class Grass extends Graphics {

	
	transient private int list_herbe;
	transient private int list_herbes;
	transient private int vent_herbe;
	Texture tex;
	
	

	
	public void drawHerbe(GL gl, int px, int py, int pz, int rx, int ry, int rz) {
		gl.glPushMatrix();
		
		//gl.glDisable(GL.GL_CULL_FACE);
		
		gl.glEnable(GL.GL_BLEND);
		if(prem) {
			prem=false;
			tex.doExpandTexture(gl);
			
			//compiler herbe
			if(!gl.glIsList(list_herbe))
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
		gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);
		
		
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




	public String toString(){
		return "Grass";
	}

	@Override
	protected void draw(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}




	@Override
	protected void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
	
}
