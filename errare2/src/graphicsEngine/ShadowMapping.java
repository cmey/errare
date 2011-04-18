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
import geom.Matrix;

import java.nio.ByteBuffer;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.glu.GLU;


public class ShadowMapping extends Graphics{
	
	protected GLPbuffer pbuffer;
	private int depth_format;
	transient public int shadowMapSize; // size of the shadow map texture
	transient public int[] internal_index; // index in video memory
	transient public ByteBuffer texturedata; //the image converted to a format compatible with OpenGL
	protected float nearZ = 10; // start of interval of depth to capture
	protected float farZ = 300; // end of interval of depth to capture
	
	float[] biasMatrixBuffer = {0.5f, 0.0f, 0.0f, 0.0f,
		    0.0f, 0.5f, 0.0f, 0.0f,
		    0.0f, 0.0f, 0.5f, 0.0f,
		    0.5f, 0.5f, 0.5f, 1.0f}; //bias from [-1, 1] to [0, 1]
	Matrix biasMatrix = new Matrix(4,4);
	
	public ShadowMapping(){
		shadowMapSize = 512;
		internal_index = new int[1];
		biasMatrix.setArray(biasMatrixBuffer);
		
		
		/*shadowMapData8Bit = ByteBuffer.allocateDirect(
				shadowMapSize * shadowMapSize);
		for(int i=0;i<shadowMapData8Bit.capacity();i++){
			shadowMapData8Bit.put((byte)0);
		}
		depthRampData8Bit = ByteBuffer.allocateDirect(256);
		for(int i=0;i<depthRampData8Bit.capacity();i++){
			depthRampData8Bit.put((byte)i);
		}*/
	}
	
 //       void firstPass(GL gl) {
            //		First pass of shadow mapping - from light's point of view
/*	    shadowmapping.pbuffer.display();
        //	  	2nd pass of shadow mapping - draw from camera's point of view
        gl.glDepthMask(false);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadMatrixf(world.cam.cameraProjectionMatrixBuffer,0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadMatrixf(world.cam.cameraViewMatrixBuffer,0);
        gl.glViewport(0, 0, winwidth, winheight);
        // use dim light to represent shadowed areas
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, world.sun.getPositionBuffer(),0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, world.sun.LightdimAmbient,0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, world.sun.LightdimDiffuse,0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, world.sun.LightBlack,0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        processOctreeGeometryOnly(gld,gl,glu,world.root);	// <-- draw meshes : 2nd pass !!!!
        gl.glDepthMask(true);
        //	  	3rd pass
        // draw with bright light
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, world.sun.LightWhite,0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, world.sun.LightWhite,0);
        // calculate texture matrix for projection
        // this matrix takes us from eye space to the light's clip space
        // it is postmultiplied by the inverse of the current view matrix when specifying texgen
        Matrix textureMatrix = shadowmapping.biasMatrix.times(world.sun.lightProjectionMatrix.times(world.sun.lightViewMatrix));
        //Set up texture coordinate generation.
        gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_EYE_LINEAR);
        gl.glTexGenfv(GL.GL_S, GL.GL_EYE_PLANE, textureMatrix.GetRow(0),0);
        gl.glEnable(GL.GL_TEXTURE_GEN_S);
        gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_EYE_LINEAR);
        gl.glTexGenfv(GL.GL_T, GL.GL_EYE_PLANE, textureMatrix.GetRow(1),0);
        gl.glEnable(GL.GL_TEXTURE_GEN_T);
        gl.glTexGeni(GL.GL_R, GL.GL_TEXTURE_GEN_MODE, GL.GL_EYE_LINEAR);
        gl.glTexGenfv(GL.GL_R, GL.GL_EYE_PLANE, textureMatrix.GetRow(2),0);
        gl.glEnable(GL.GL_TEXTURE_GEN_R);
        gl.glTexGeni(GL.GL_Q, GL.GL_TEXTURE_GEN_MODE, GL.GL_EYE_LINEAR);
        gl.glTexGenfv(GL.GL_Q, GL.GL_EYE_PLANE, textureMatrix.GetRow(3),0);
        gl.glEnable(GL.GL_TEXTURE_GEN_Q);
        //	bind & enable shadow map texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, shadowmapping.internal_index[0]);
        gl.glEnable(GL.GL_TEXTURE_2D);
        //Enable shadow comparison
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_MODE_ARB, GL.GL_COMPARE_R_TO_TEXTURE);
        //Shadow comparison should be true (ie not in shadow) if r<=texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_FUNC_ARB, GL.GL_LEQUAL);
        //Shadow comparison should generate an INTENSITY result
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_DEPTH_TEXTURE_MODE_ARB, GL.GL_INTENSITY);
        // set alpha test to discard false comparisons
        gl.glAlphaFunc(GL.GL_GEQUAL, 0.99f);
        gl.glEnable(GL.GL_ALPHA_TEST);
        processOctreeGeometryOnly(gld,gl,glu,world.root);	// <-- draw meshes : 3rd pass !!!!
         */
 //       }
	
	public void initshadowmap(GLAutoDrawable gld){
		if(!Extensions.isShadowMappingSupported) { return; }
		// init pbuffer
	    GLCapabilities caps = new GLCapabilities();
	    caps.setDoubleBuffered(false);
	    pbuffer = GLDrawableFactory.getFactory().createGLPbuffer(caps, null, shadowMapSize, shadowMapSize, gld.getContext());
	    pbuffer.addGLEventListener(new ShadowPbufferListener());
	}
	
	@Override
	public void draw(GLAutoDrawable gld) {
		/*GL gl = gld.getGL();
		gl.glPushMatrix();
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_LUMINANCE8, shadowMapSize,shadowMapSize,
				0, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE, texturedata);
		// PAS UTILISABLE AVEC PBUFFER
		gl.glColor4f(1,1,1,1);
		gl.glDisable(GL.GL_BLEND);
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-100,-100, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 100,-100, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 100, 100, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-100, 100, 0.0f);
		gl.glEnd();
		
		gl.glPopMatrix();*/
	}
	

	
	/**
	 * Read the depth buffer into the shadow map texture
	 * @param gl
	 */
	protected void readShadowMap(GL gl){
	    gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
	    gl.glCopyTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, 0, 0, shadowMapSize, shadowMapSize);
	    //TODO: should use pbuffer.bindTexture() instead;
	}
	
	
    @Override
	public String toString(){
		return "ShadowMap";
	}
	
	@Override
	protected
	void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
	
	class ShadowPbufferListener implements GLEventListener{

		public void display(GLAutoDrawable gld) {
			GL gl = gld.getGL();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		    gl.glMatrixMode(GL.GL_PROJECTION);
		    gl.glLoadMatrixf(ge.world.sun.lightProjectionMatrixBuffer,0);
		    gl.glMatrixMode(GL.GL_MODELVIEW);
		    gl.glLoadMatrixf(ge.world.sun.lightViewMatrixBuffer,0);
		    
		    ge.processOctreeGeometryOnly(gld,gl,new GLU(),ge.world.root);	// <-- draw meshes : 1st pass !!!!
		    
			readShadowMap(gl);
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
			// not yet implemented in JOGL API
		}

		public void init(GLAutoDrawable gld) {
			GL gl = gld.getGL();
			gld.setGL(new DebugGL(gl));
			
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LEQUAL);						// The Type Of Depth Testing To Do
			gl.glClearDepth(1.0f);
			// Use viewport the same size as the shadow map
		    gl.glViewport(0, 0, shadowMapSize, shadowMapSize);
		    // Disable color writes, and use flat shading for speed
		    gl.glColorMask(true, true, true, true);
		    gl.glShadeModel(GL.GL_FLAT);
		    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		    //TODO : displace the vertices a bit to avoid Zfighting shadows on front faces
			
			int[] depth_bits = new int[1];
		    gl.glGetIntegerv(GL.GL_DEPTH_BITS, depth_bits, 0);
		    if (depth_bits[0] == 16)  depth_format = GL.GL_DEPTH_COMPONENT16_ARB;
		    else                      depth_format = GL.GL_DEPTH_COMPONENT24_ARB;
		    // Create the shadow map texture
		    gl.glGenTextures(1, internal_index,0);
		    gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
		    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, depth_format, shadowMapSize, shadowMapSize, 0,
		        GL.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_INT, null);
		    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		    //gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_MODE_ARB, GL.GL_COMPARE_R_TO_TEXTURE_ARB);
		    //gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_FUNC_ARB, GL.GL_LEQUAL);
		    
		}

		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO: maybe something to do here ?
		}
		
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
}
