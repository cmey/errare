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
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

public class Text2d extends GraphicalRep{

	private int list_chars;
	
	public Text2d(String filename) {
		super(filename, GraphicalRep.FORMAT_TEXT);
	}

	public void draw(GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {}
	
	
	public void BuildFont(GL2 gl){
		//gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE);
		//gl.glEnable(GL.GL_BLEND);
		
		if(prem){
			prem=false;
			doExpandTexture(gl);
			float	cx;						// Holds Our X Character Coord
			float	cy;						// Holds Our Y Character Coord

			if(!gl.glIsTexture(list_chars))
				list_chars=gl.glGenLists(256);	// Creating 256 Display Lists
			for (int loop=0; loop<256; loop++){
				cx=(float)(loop%16)/16.0f;// X Position Of Current Character
				cy=(float)(loop/16)/16.0f;// Y Position Of Current Character
				
				gl.glNewList(list_chars+loop,GL2.GL_COMPILE);
				gl.glBegin(GL2.GL_QUADS);
				
				gl.glTexCoord2f(cx,1f-cy-0.0625f);// Texture Coord (Bottom Left)
				gl.glVertex2i(0,0);			// Vertex Coord (Bottom Left)
				gl.glTexCoord2f(cx+0.0625f,1f-cy-0.0625f);// Texture Coord (Bottom Right)
				gl.glVertex2i(16,0);			// Vertex Coord (Bottom Right)
				gl.glTexCoord2f(cx+0.0625f,1f-cy);// Texture Coord (Top Right)
				gl.glVertex2i(16,16);			// Vertex Coord (Top Right)
				gl.glTexCoord2f(cx,1f-cy);		// Texture Coord (Top Left)
				gl.glVertex2i(0,16);			// Vertex Coord (Top Left)
				gl.glEnd();						// Done Building Our Quad (Character)
				gl.glTranslated(10,0,0);		// Move To The Right Of The Character
				gl.glEndList();
			}
			
		}
	}
	
	
	
	
	public void glPrint(GL2 gl,int x, int y, String s){
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(1,1,1,1);
		gl.glEnable(GL.GL_BLEND);
		gl.glTranslated(x,y,0);
		gl.glListBase(list_chars-32);
		char[] array = s.toCharArray();
		for(int i=0; i<array.length; i++)
				gl.glCallList(list_chars+array[i]-32);
		gl.glDisable(GL.GL_BLEND);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

}
