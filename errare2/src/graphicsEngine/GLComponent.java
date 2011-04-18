package graphicsEngine;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import logger.Logger;

/**
 * A graphical component that can be added in Swing.
 * The receiver of GL callbacks.
 * @author christophe
 */
public class GLComponent extends GLJPanel implements GLEventListener {

    private GLU glu;
    private FPSAnimator animator;
    GraphicsEngine ge;

    public GLComponent(int fps, GraphicsEngine ge) {
        super(getCapabilities());
        addGLEventListener(this);
        glu = new GLU();

        this.ge = ge;

        animator = new FPSAnimator(this, fps);
    }

    private static GLCapabilities getCapabilities() {
        GLCapabilities caps = new GLCapabilities();
        //caps.setDoubleBuffered(true);
        //caps.setHardwareAccelerated(true);
        Logger.printINFO("Using capabilities : " + caps);
        return caps;
    }


    // ------------ OPENGL CALLBACKS --------------


    public void init(GLAutoDrawable drawable) {
        ge.init(drawable);
        animator.start();
    }

    public void display(GLAutoDrawable drawable) {
        final GL gl = drawable.getGL();
        ge.display(drawable);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        ge.reshape(drawable, x, y, width, height);
    }


    // ----------------- ANIMATOR -----------------

    public void setFPS(int fps) {
        animator.stop();
        animator = new FPSAnimator(this, fps);
        animator.start();
    }

    public void kill() {
        animator.stop();
    }
}
