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
import javax.media.opengl.glu.GLU;

import com.sun.opengl.cg.CGcontext;
import com.sun.opengl.cg.CGeffect;
import com.sun.opengl.cg.CGparameter;
import com.sun.opengl.cg.CGpass;
import com.sun.opengl.cg.CGtechnique;
import com.sun.opengl.cg.CgGL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import logger.Logger;
import main.ResourceLocator;


/**
 * Special effect called Bloom. It's a glow of the whole screen.
 * @author Christophe
 */
public class Bloom extends Graphics{
	
	private int[] internal_index;		// 2 textures
	private int[] depthRenderbuffer;	// 1 depth renderbuffer
	private int[] fbo;				// 2 fbo's
	
	private boolean valid = false;
	private boolean activated = false;
	private float bfactor;
	private int vpx, vpy, vpw, vph;
	private int glowWSize, glowHSize;
	
	private CGcontext     cgContext;
	private CGeffect      cgEffect;
	private CGtechnique   cgTechnique;
	private CGpass        cgPassFilterH;
	private CGpass        cgPassFilterV;
	private CGpass        cgPassBlend;
	private CGparameter   srcSampler;
	private CGparameter   tempSampler;
	private CGparameter   finalSampler;
	private CGparameter   verticalDir;
	private CGparameter   horizontalDir;
	private CGparameter   cgBlendFactor;
	private CGparameter   cgGlowFactor;
	
	public Bloom(){		
			fbo = new int[3];
			internal_index = new int[3];
			depthRenderbuffer = new int[2];
			glowWSize = 256; glowHSize = 256;
			bfactor = 0.4f;
			vpx = 0; vpy = 0; vpw = ge.getWidth(); vph = ge.getHeight();
			valid = false;
	}
	
	public void init(GL gl){
        try {
            if ( (!Extensions.isFBOSupported) || (!Extensions.isCGSupported) ) {
                valid = false;
                return;
            }
            
            gl.glGenFramebuffersEXT(3, fbo, 0);
            gl.glGenTextures(3, internal_index, 0);
            gl.glGenRenderbuffersEXT(2, depthRenderbuffer, 0);
            // TEXTURES
            for (int n = 0; n < 2; ++n) {
                gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[n]);
                gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[n]);
                gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, glowWSize, glowHSize, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
                // SEEMS VERY IMPORTANT for the FBO to be valid. ARGL.
                gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
                gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
                gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, internal_index[n], 0);
                valid = FramebufferObject.CheckFramebufferStatus(gl);
                if (!valid) {
                    return;
                }
            }
            gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[2]);
            gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[2]);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, vpw, vph, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
            
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
            gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, internal_index[2], 0);

            // initialize depth renderbuffer
            gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[1]);
            gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthRenderbuffer[0]);
            gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT24, glowWSize, glowHSize);
            gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthRenderbuffer[0]);
            valid = FramebufferObject.CheckFramebufferStatus(gl);
            if (!valid) {
                return;
            }
            gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[2]);
            gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthRenderbuffer[1]);
            gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT24, vpw, vph);
            gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthRenderbuffer[1]);
            valid = FramebufferObject.CheckFramebufferStatus(gl);
            if (!valid) {
                return;
            }
            gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
            // CGFX things
            cgContext = CgGL.cgCreateContext();
            CgGL.cgGLRegisterStates(cgContext);

            String sourceFile = "graphicsEngine/cg/Bloom.cgfx";
            String source = "";
            String sourceItem = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(ResourceLocator.getRessourceAsStream(sourceFile)));
            while ((sourceItem = br.readLine()) != null) {
                source+= (sourceItem+"\n");
            }
            source.substring(0, source.length()-1);
            cgEffect = CgGL.cgCreateEffect(cgContext, source, null);
            if (!CgGL.cgIsEffect(cgEffect)) {
                String msg = CgGL.cgGetLastListing(cgContext);
                String msg2 = CgGL.cgGetErrorString(CgGL.cgGetError());
                Logger.printERROR("Cannot create CGEffect object : " + msg + " AND " + msg2);
                valid = false;
                return;
            }

            cgTechnique = CgGL.cgGetNamedTechnique(cgEffect, "Filter");
            cgPassFilterH = CgGL.cgGetNamedPass(cgTechnique, "verticalPass");
            cgPassFilterV = CgGL.cgGetNamedPass(cgTechnique, "horizontalPass");
            cgPassBlend = CgGL.cgGetNamedPass(cgTechnique, "drawFinal");
            cgBlendFactor = CgGL.cgGetNamedEffectParameter(cgEffect, "blendFactor");
            cgGlowFactor = CgGL.cgGetNamedEffectParameter(cgEffect, "glowFactor");
            srcSampler = CgGL.cgGetNamedEffectParameter(cgEffect, "srcSampler");
            tempSampler = CgGL.cgGetNamedEffectParameter(cgEffect, "tempSampler");
            finalSampler = CgGL.cgGetNamedEffectParameter(cgEffect, "finalSampler");
            verticalDir = CgGL.cgGetNamedEffectParameter(cgEffect, "verticalDir");
            horizontalDir = CgGL.cgGetNamedEffectParameter(cgEffect, "horizontalDir");

            CgGL.cgGLSetParameter2f(verticalDir, 0, 1.0f / (float) vph);
            CgGL.cgGLSetParameter2f(horizontalDir, 1.0f / (float) vpw, 0);

            valid = true;
        } catch (Exception ex) {
            valid = false;
            Logger.printExceptionERROR(ex);
        } catch (Error e) {
            valid = false;
            Logger.printErrorERROR(e);
        }
	}
	
	public void renderGlow(GL gl){
		if(!valid) return;
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		
		valid = CgGL.cgValidateTechnique(cgTechnique);
		if(!valid){
			String msg = CgGL.cgGetErrorString(CgGL.cgGetError());
			Logger.printERROR("Cannot validate CGTechnique : " + msg);
			return;
		}
		// intermediate stage : blurring horizontal
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[1]);
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
		gl.glViewport(0, 0, glowWSize, glowHSize);
		
		CgGL.cgGLSetupSampler(srcSampler, internal_index[0]);
		CgGL.cgSetPassState(cgPassFilterH);

		FULLSCRQUAD(gl);
		gl.glPopAttrib();
		//CgGL.cgResetPassState(cgPassFilterH);
		// intermediate stage : blurring vertical
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT); 
		gl.glViewport(0,0,glowWSize, glowHSize);

		CgGL.cgGLSetupSampler(srcSampler, internal_index[1]);
		CgGL.cgSetPassState(cgPassFilterV);

		FULLSCRQUAD(gl);
		gl.glPopAttrib();
		//CgGL.cgResetPassState(cgPassFilterV);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		// Final stage : Blend the final texture to the screen
		CgGL.cgGLSetupSampler(tempSampler, internal_index[0]);

		CgGL.cgSetPassState(cgPassBlend);
		gl.glBlendColor(bfactor,bfactor,bfactor,bfactor);

		FULLSCRQUAD(gl);
		
		CgGL.cgResetPassState(cgPassBlend);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glEnable(GL.GL_BLEND);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glAlphaFunc(GL.GL_GREATER,0.1f);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glEnable(GL.GL_CULL_FACE);
	}
	
	private void FULLSCRQUAD(GL gl){
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,0);
		gl.glVertex2i(-1, -1);
		gl.glTexCoord2i(1,0);
		gl.glVertex2i(1, -1);
		gl.glTexCoord2i(1,1);
		gl.glVertex2i(1, 1);
		gl.glTexCoord2i(0,1);
		gl.glVertex2i(-1, 1);
		gl.glEnd();
	}
	
	protected void activate(GL gl){
		if(!valid) return;
		activated=true;
		
		// render in the main frame buffer
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[2]);
		gl.glPushAttrib(GL.GL_VIEWPORT_BIT | GL.GL_COLOR_BUFFER_BIT);
		gl.glViewport(vpx, vpy, vpw, vph);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glClearColor(0,0,0,1);
	}
	
	protected void deactivate(GL gl, GLU glu){
		if(!valid) return;
		if(!activated) return;
		activated = false;
		
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
		gl.glViewport(0, 0, glowWSize, glowHSize);
		gl.glClearColor(0,0,0,0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-1, 1, -1, 1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		// render for the glow fbo
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[2]);
		FULLSCRQUAD(gl);
		
		// render for the screen
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		gl.glPopAttrib();
		//gl.glViewport(vpx, vpy, vpw, vph);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		FULLSCRQUAD(gl);
		
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		
	}
	
	protected void changeBlendFactor(float f){
		bfactor = f;
	}
	
	/**
	 * Change the resolution of the bloom texture and fbo's renderbuffer.
	 * Counter-intuitively, low resolutions can give better results.
	 * @param w width of the bloom texture
	 * @param h height of the bloom texture
	 */
	protected void changeResolution(GL gl, int w, int h){
		if(!valid) return;
		
		// update 2 textures
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_index[1]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		
		// update depth render buffer
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthRenderbuffer[0]);
		gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT24, w, h);
		
		glowWSize = w; glowHSize = h;
		valid = FramebufferObject.CheckFramebufferStatus(gl);
	}
	
	/**
	 * OpenGL viewport area to render to, with the renderGlow() method. 
	 * @param x x coordinate of lower left corner of area
	 * @param y y coordinate of lower left corner of area
	 * @param w width of area
	 * @param h height of area
	 */
	protected void setWindowViewport(int x, int y, int w, int h){
		vpx = x; vpy = y; vpw = w; vph = h;
	}
	
	/**
	 * Tells if the framebuffer object is valid
	 * @return true only when this framebuffer object is complete
	 */
	protected boolean isValid(){
		return valid;
	}

	@Override
	protected void draw(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
	}
}
