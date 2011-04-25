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

import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * Special effect called Glow. Today's fashion.
 * @author Christophe
 */
public class Glow {
	
	private int[] internal_texture;
	public int glowSize = 256;
	private ByteBuffer glowData8Bit;
	
	public Glow(){
		glowData8Bit = ByteBuffer.allocateDirect(glowSize * glowSize * 3);
		internal_texture = new int[1];
	}
	
	public void renderGlow(GL2 gl, int times, int pixel_offset){
		
		if(gl.glIsTexture(internal_texture[0])){
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
			//gl.glAlphaFunc(GL.GL_GREATER,0.3f);
			//gl.glEnable(GL.GL_ALPHA_TEST);

			gl.glDisable(GL2.GL_ALPHA_TEST);
	        float alpha = 0.1f;											// Starting Alpha Value
	        float alphainc = alpha / times;								// Fade Speed For Alpha Blending
	        alpha = 0;
	        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);							// Set Blending Mode
	        
			gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture[0]);
			
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glEnable(GL.GL_BLEND);
			
			//gl.glColor4f(1,1,1,0.6f);
			/*
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2i(0,0);
			gl.glVertex3f(0,0,0);
			gl.glTexCoord2i(0,1);
			gl.glVertex3f(0,GraphicsEngine.window_height,0);
			gl.glTexCoord2i(1,1);
			gl.glVertex3f(GraphicsEngine.window_width,GraphicsEngine.window_height,0);
			gl.glTexCoord2i(1,0);
			gl.glVertex3f(GraphicsEngine.window_width,0,0);
			gl.glEnd();
			*/
			gl.glBegin(GL2.GL_QUADS);										// Begin Drawing Quads
	        float circle = 0f;
			for (int num = 0; num < times; num++)						// Number Of Times To Render Blur
			{
				alpha += alphainc;							// Gradually Decrease alpha (Gradually Fading Image Out)
				for(int i=0;i<4;i++){
					gl.glColor4f(1.0f, 1.0f, 1.0f, alpha);					// Set The Alpha Value (Starts At 0.2)
					gl.glTexCoord2f(0, 0);						// Texture Coordinate	( 0, 1 )
					gl.glVertex2f((float)Math.cos(circle)*pixel_offset, (float)Math.sin(circle)*pixel_offset);									// First Vertex		(   0,   0 )
					
					gl.glTexCoord2f(0, 1);						// Texture Coordinate	( 0, 0 )
					gl.glVertex2f((float)Math.cos(circle)*pixel_offset, GraphicsEngine.window_height+(float)Math.sin(circle)*pixel_offset);									// Second Vertex	(   0, 480 )
					
					gl.glTexCoord2f(1, 1);						// Texture Coordinate	( 1, 0 )
					gl.glVertex2f(GraphicsEngine.window_width+(float)Math.cos(circle)*pixel_offset, GraphicsEngine.window_height+(float)Math.sin(circle)*pixel_offset);								// Third Vertex		( 640, 480 )
					
					gl.glTexCoord2f(1, 0);						// Texture Coordinate	( 1, 1 )
					gl.glVertex2f(GraphicsEngine.window_width+(float)Math.cos(circle)*pixel_offset, (float)Math.sin(circle)*pixel_offset);									// Fourth Vertex	( 640,   0 )
					circle += 360/times;
				}
				pixel_offset -= pixel_offset/times;
			}
			gl.glEnd();			
			gl.glColor4f(1,1,1,1);
			
			gl.glDisable(GL.GL_BLEND);
			gl.glPopMatrix();
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
			gl.glPopAttrib();
		}
	}
	
	
	public void readWindow(GL2 gl){
		gl.glEnable(GL.GL_TEXTURE_2D);
		if(!gl.glIsTexture(internal_texture[0])){
			gl.glGenTextures(1, internal_texture, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture[0]);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, glowSize, glowSize,
					0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, glowData8Bit.rewind());
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture[0]);		
		
		gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, 0,0,
				glowSize,glowSize,0);
		
	}
}
