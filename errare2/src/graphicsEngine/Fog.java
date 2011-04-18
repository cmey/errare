package graphicsEngine;

import javax.media.opengl.GL;

/**
 * @author christophe
 * @date 26 mars 2008
 */
public class Fog {
    
    GraphicsEngine ge;
    
    Fog (GraphicsEngine ge) {
        this.ge = ge;
    }

    void enableFog(GL gl) {
        float[] fogcolor = {0.5f, 0.5f, 0.5f, 1.0f};
        gl.glClearColor(fogcolor[0], fogcolor[1], fogcolor[2], fogcolor[3]);				// We'll Clear To The Color Of The Fog ( Modified )
        gl.glColor4f(1, 1, 1, 1);
        gl.glFogi(GL.GL_FOG_MODE, GL.GL_LINEAR);			// Fog Mode
        gl.glFogfv(GL.GL_FOG_COLOR, fogcolor, 0);				// Set Fog Color
        //  gl.glFogf(GL.GL_FOG_DENSITY, 0.001f);				// How Dense Will The Fog Be
        gl.glHint(GL.GL_FOG_HINT, GL.GL_NICEST);			// Fog Hint Value
        gl.glFogf(GL.GL_FOG_START, 70f / 100f * ge.view_distance);	// Fog Start Depth
        gl.glFogf(GL.GL_FOG_END, ge.view_distance);			// Fog End Depth
        gl.glEnable(GL.GL_FOG);								// Enables GL_FOG
    }

    
}
