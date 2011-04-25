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
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

public class ShadowMap extends GraphicalRep{
	
	public int shadowMapSize;
	private ByteBuffer shadowMapData8Bit;
	private ByteBuffer depthRampData8Bit;
	
	
	public ShadowMap(String filename){
		super(filename, GraphicalRep.FORMAT_SHADOWMAP);
		shadowMapSize = 256;
		shadowMapData8Bit = ByteBuffer.allocateDirect(
				shadowMapSize * shadowMapSize);
		for(int i=0;i<shadowMapData8Bit.capacity();i++){
			shadowMapData8Bit.put((byte)0);
		}
		depthRampData8Bit = ByteBuffer.allocateDirect(256);
		for(int i=0;i<depthRampData8Bit.capacity();i++){
			depthRampData8Bit.put((byte)i);
		}
	}
	
	
	@Override
	public void draw(GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		GL2 gl = gld.getGL().getGL2();
		gl.glPushMatrix();
		doPosition(gl,px, py, pz, 0,0,0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_LUMINANCE8, shadowMapSize,shadowMapSize,
				0, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE, shadowMapData8Bit.rewind());
		
		gl.glColor4f(1,1,1,1);
		gl.glDisable(GL.GL_BLEND);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-100,-100, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 100,-100, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 100, 100, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-100, 100, 0.0f);
		gl.glEnd();
		
		gl.glPopMatrix();
	}
	

	
	
	public void readShadowMap(GL2 gl, GLU glu){
		//Extensions.assertPresence("GL_ARB_depth_texture");
		//Extensions.assertPresence("GL_ARB_shadow");
		//boolean[] b_null = null; si HW-SM-supported
		if(prem) {
			prem=false;
			if(!gl.glIsTexture(internal_texture1[0]))
				gl.glGenTextures(1, internal_texture1, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_ALPHA8, shadowMapSize,shadowMapSize,
					0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, shadowMapData8Bit.rewind());
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		
			if(!gl.glIsTexture(internal_texture2[0]))
				gl.glGenTextures(1, internal_texture2, 0);
			gl.glBindTexture(GL2.GL_TEXTURE_1D, internal_texture2[0]);
			gl.glTexImage1D(GL2.GL_TEXTURE_1D, 0, GL2.GL_ALPHA8, 256,
					0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE,depthRampData8Bit.rewind());
			gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
			depthRampData8Bit = null; //free memory: we will only need the texture
			
		}
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		
		//Read back the depth values into the shadow map texture image data
		gl.glReadPixels(0, 0, shadowMapSize, shadowMapSize,
				GL2.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE, shadowMapData8Bit.rewind());
		//Update texture into video memory
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_ALPHA8, shadowMapSize,shadowMapSize,
				0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, shadowMapData8Bit.rewind());
	}
	
}
