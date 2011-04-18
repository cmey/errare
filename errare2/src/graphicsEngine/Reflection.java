package graphicsEngine;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * @author christophe
 * @date 26 mars 2008
 */
public class Reflection {

    GraphicsEngine ge;
    Texture reflection;
    int[] fbo;
    boolean init;
    
    Reflection(GraphicsEngine ge) {
        this.ge = ge;
    }
    
    void init(GL gl) {
        reflection = TextureFactory.createRenderTexture(ge.world.heightmap.water.texture_quality);
            //refraction = TextureFactory.createRenderTexture(512);
            reflection.doExpandTexture(gl);
            //refraction.doExpandTexture(gl);

            if (Extensions.isFBOSupported) {
                int nbFBOs = 1; // frame buffer object
                fbo = new int[nbFBOs];
                gl.glGenFramebuffersEXT(nbFBOs, fbo, 0);
                int nbDRBs = 1; // depth render buffer
                int[] depthRenderBuffer = new int[nbDRBs];
                gl.glGenRenderbuffersEXT(nbDRBs, depthRenderBuffer, 0);
                gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
                // initialize color texture
                gl.glBindTexture(GL.GL_TEXTURE_2D, reflection.internal_index[0]);
                gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT,
                        GL.GL_COLOR_ATTACHMENT0_EXT,
                        GL.GL_TEXTURE_2D, reflection.internal_index[0], 0);

                // initialize depth renderbuffer
                gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthRenderBuffer[0]);
                gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT,
                        GL.GL_DEPTH_COMPONENT24_ARB, ge.world.heightmap.water.texture_quality, ge.world.heightmap.water.texture_quality);
                gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT,
                        GL.GL_DEPTH_ATTACHMENT_EXT,
                        GL.GL_RENDERBUFFER_EXT, depthRenderBuffer[0]);
                // Check framebuffer completeness at the end of initialization.
                FramebufferObject.CheckFramebufferStatus(gl);
            }
            init = true;
    }
    
    void createReflectionTexture(GL gl, GLAutoDrawable gld, GLU glu) {
        if(!init)
            init(gl);
        /** *************************** **/
        /** CREATING REFLECTION TEXTURE **/
        if (Extensions.isFBOSupported) {
            gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            //}else{
            //	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
            //}
            gl.glLoadIdentity();
            gl.glColor4f(1, 1, 1, 1);
            if (ge.world.heightmap != null) {
                gl.glViewport(0, 0,ge.world.heightmap.water.texture_quality,ge.world.heightmap.water.texture_quality);
                glu.gluLookAt(ge.camposition.x, ge.camposition.y, ge.camposition.z, ge.camlookingat.x, ge.camlookingat.y, ge.camlookingat.z, 0, 1, 0);
                gl.glPushMatrix();
                gl.glTranslatef(0.0f, 2f *ge.world.heightmap.water.sea_level, 0.0f);
                gl.glScalef(1f, -1f, 1f);
                gl.glEnable(GL.GL_CLIP_PLANE0);
                gl.glClipPlane(GL.GL_CLIP_PLANE0,ge.world.heightmap.water.getClipPlane());
                gl.glFrontFace(GL.GL_CW);
                if (ge.world.heightmap != null) {
                   ge.world.heightmap.draw_terrain(gl);
                }
                gl.glFrontFace(GL.GL_CCW);
                if (KeyboardHelper.VBOs) {
                    gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
                }
                ge.processOctreeFull(gld, gl, glu,ge.world.root);
                if (KeyboardHelper.VBOs) {
                    gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
                }
                ge.world.sun.draw(gld);
                gl.glPopMatrix();
                gl.glDisable(GL.GL_CLIP_PLANE0);
                gl.glBindTexture(GL.GL_TEXTURE_2D, this.reflection.internal_index[0]);
                if (Extensions.isFBOSupported) {
                    gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
                }
            //else
            //	gl.glCopyTexSubImage2D(GL.GL_TEXTURE_2D,0, 0,0, 0,0,world.heightmap.water.texture_quality,world.heightmap.water.texture_quality);
            }
        }
    }
}
