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

public class Text2D extends GraphicalRep{

	private int list_chars;
	int fontsize;
	boolean rebuild;
	
	public Text2D(String filename) {
		super(filename, GraphicalRep.FORMAT_TEXT);
		fontsize = 16;
		rebuild=true;
	}

	public void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {}
	
	
	public void BuildFont(GL gl){
		//gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE);
		//gl.glEnable(GL.GL_BLEND);
		
		if(prem){
			prem=false;
			doExpandTexture(gl);
		}
		
		if(rebuild){
			float	cx;						// Holds Our X Character Coord
			float	cy;						// Holds Our Y Character Coord

			if(!gl.glIsList(list_chars))
				list_chars=gl.glGenLists(256);	// Creating 256 Display Lists
			for (int loop=0; loop<256; loop++){
				cx=(float)(loop%16)/16.0f;// X Position Of Current Character
				cy=(float)(loop/16)/16.0f;// Y Position Of Current Character
				
				gl.glNewList(list_chars+loop,GL.GL_COMPILE);
				gl.glBegin(GL.GL_QUADS);
				
				gl.glTexCoord2f(cx,1f-cy-0.0625f);// Texture Coord (Bottom Left)
				gl.glVertex2i(0,0);			// Vertex Coord (Bottom Left)
				gl.glTexCoord2f(cx+0.0625f,1f-cy-0.0625f);// Texture Coord (Bottom Right)
				gl.glVertex2i(fontsize,0);			// Vertex Coord (Bottom Right)
				gl.glTexCoord2f(cx+0.0625f,1f-cy);// Texture Coord (Top Right)
				gl.glVertex2i(fontsize,fontsize);			// Vertex Coord (Top Right)
				gl.glTexCoord2f(cx,1f-cy);		// Texture Coord (Top Left)
				gl.glVertex2i(0,fontsize);			// Vertex Coord (Top Left)
				gl.glEnd();						// Done Building Our Quad (Character)
				gl.glTranslated(fontsize*10/16,0,0);		// Move To The Right Of The Character
				gl.glEndList();
			}
			
		}
	}
	
	
	
	public void setFontSize(int size, GL gl){
		this.fontsize = size;
		rebuild=true;
		this.BuildFont(gl);
	}
	
	
	
	
	public void glPrint(GL gl,int x, int y, String s){
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
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
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	
	
	public void glPrintc(GL gl,int x, int y, String s, float r, float b, float g){
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(r,b,g,1);
		gl.glEnable(GL.GL_BLEND);
		gl.glTranslated(x,y,0);
		gl.glListBase(list_chars-32);
		char[] array = s.toCharArray();
		for(int i=0; i<array.length; i++)
				gl.glCallList(list_chars+array[i]-32);
		gl.glDisable(GL.GL_BLEND);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glColor4f(1,1,1,1);
	}

}
