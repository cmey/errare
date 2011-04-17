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

public class Border extends GraphicalRep{

	
	private float bdt; //Border Tex Displacement
	private float bdti; //Border Tex Displacement
	
	
	
	
	public Border(String filename) {
		super(filename, GraphicalRep.FORMAT_BORDER);
	}
	
	

	@Override
	public void draw(GLDrawable gld,float px, float py, float pz, float rx, float ry, float rz) {
		
		
	}
	
	
	
	
	
	public void LeftBorderAction(GL gl) {
		gl.glPushMatrix();
		gl.glDepthMask(false);
		if(prem) {
			doExpandTexture(gl);
			prem=false;
		}
		
		if(isAlphaTextured){
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glEnable(GL.GL_BLEND); // Enable blending for transparency
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glLoadIdentity();
		gl.glTranslated(-2,0,-1);
		bdt=(float) Math.abs(Math.cos(bdti+=0.005));
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0,bdt);gl.glVertex3d (-1,-1,-1); 
		gl.glTexCoord2f(bdt,bdt);gl.glVertex3d (1,-1,-1); 
		gl.glTexCoord2f(bdt,0);gl.glVertex3d (1,1,-1); 
		gl.glTexCoord2f(0,0);gl.glVertex3d (-1,1,-1);
		gl.glEnd();
		
		if(isAlphaTextured){
			gl.glDisable(GL.GL_BLEND); // End of transparency
			gl.glEnable(GL.GL_DEPTH_TEST);
		}
		gl.glDepthMask(true);
		gl.glPopMatrix();
	}
	
	public void RightBorderAction(GL gl) {
		gl.glPushMatrix();
		gl.glDepthMask(false);
		if(prem) {
			doExpandTexture(gl);
			prem=false;
		}
		
		
		if(isAlphaTextured){
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glEnable(GL.GL_BLEND);   // Enable blending for transparency
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glLoadIdentity();
		gl.glTranslated(2,0,-1);
		bdt=(float) Math.abs(Math.cos(bdti+=0.005));
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0,bdt);gl.glVertex3d (-1,-1,-1); 
		gl.glTexCoord2f(bdt,bdt);gl.glVertex3d (1,-1,-1); 
		gl.glTexCoord2f(bdt,0);gl.glVertex3d (1,1,-1); 
		gl.glTexCoord2f(0,0);gl.glVertex3d (-1,1,-1);
		gl.glEnd();
		
		if(isAlphaTextured){
			gl.glDisable(GL.GL_BLEND); // End of transparency 
			gl.glEnable(GL.GL_DEPTH_TEST);
		}
		gl.glDepthMask(true);
		gl.glPopMatrix();
	}
	

}
