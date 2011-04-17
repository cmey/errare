package graphicsEngine;

/**
 * @author Troubleshooting
 * 
 */

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.swing.JFrame;
import net.java.games.jogl.Animator;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLDrawableFactory;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.BufferUtils;
import net.java.games.jogl.util.GLUT;

public class PickingExample extends JFrame implements GLEventListener, MouseListener {

    private IntBuffer selectionBuffer;  // stores the picking result
    
    private GL gl;
    private GLU glu;
    private GLUT glut = new GLUT();

    private int mode= GL.GL_RENDER;     // mode of operation (GL_RENDER, GL_SELECT)
    
    private GLCanvas canvas;

    private Animator anim;
    private float ratio;

    private double mouseY;              // mouse click position on the canvas
    private double mouseX;
    
    public PickingExample() {
        canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
        canvas.addGLEventListener(this);
        
        // register listener to catch mouse button events
        canvas.addMouseListener(this);          
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(canvas, BorderLayout.CENTER);

        // terminate app if window closed 
        this.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent arg0) {
                anim.stop();
                System.exit(0);
            }
            
            public void windowOpened(WindowEvent arg0) {}
            public void windowClosed(WindowEvent arg0) {}
            public void windowIconified(WindowEvent arg0) {}
            public void windowDeiconified(WindowEvent arg0) {}
            public void windowActivated(WindowEvent arg0) {}
            public void windowDeactivated(WindowEvent arg0) {}
        });
        
        setSize(500,500);
        setVisible(true);
        
        anim = new Animator(canvas);            // calls display() periodically
        anim.start();        
    }
    
    public void init(GLDrawable arg0) {

        final int bufferSize  = 10;             // 10 hits shall be stored
        
        // allocate fast memory. Necessary since glSelectBuffer takes IntBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
                    BufferUtils.SIZEOF_INT*bufferSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        selectionBuffer  = byteBuffer.asIntBuffer();        
        
        gl = arg0.getGL();
        glu = arg0.getGLU();
        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
    }

    public void display(GLDrawable arg0) {
        
        gl.glLoadIdentity();
        glu.gluLookAt(5,5,10, 3,3,0, 0,0,1);
        
        if (mode == GL.GL_RENDER) {
            
            // only clear the buffers when in GL_RENDER mode. Avoids flickering
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            
            drawQuad();
            drawSphere();
            drawCube();
            
        } else displaySelection();
        
    }

    private void displaySelection() {
        int[] viewport = new int[4];
        int hits = 0;
        
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
        // the hits will be stored in the selection buffer after.
        // Note it's called *before* we change the render mode
        
        gl.glSelectBuffer(selectionBuffer.capacity(), selectionBuffer);

        gl.glRenderMode(GL.GL_SELECT);
        
        gl.glInitNames();                       // clears name stack
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();                      // saves current proj. matrix
        
        gl.glLoadIdentity();                    // resets the proj. matrix
        
        glu.gluPickMatrix(mouseX, viewport[3] - mouseY, 
                5.0d, 5.0d, viewport);          // restricts drawing to around
                                                // the cursor

        glu.gluPerspective(45, ratio,1, 200);   // sets up view frustrum. Makes sure that
                                                // that you set it up the same way
                                                // as you set up the frustum in GL_RENDER 
                                                // mode!
        
        gl.glMatrixMode(GL.GL_MODELVIEW);       // now back to model view so that we
                                                // can transform our objects
        
        gl.glPushName(20);                      // assign '20' to the quad
        drawQuad();

        gl.glLoadName(30);                      // assign '30' to the sphere
        drawSphere();

        gl.glLoadName(40);                      // assign '40' to the cube
        drawCube();
        
        gl.glMatrixMode(GL.GL_PROJECTION);      
        gl.glPopMatrix();                       // restore the previous proj. matrix
        
        gl.glMatrixMode(GL.GL_MODELVIEW);       // back again in default mode

        hits = gl.glRenderMode(GL.GL_RENDER);   // back to GL_RENDER mode
        
        mode = GL.GL_RENDER;                    // next display() call produces
                                                // visible result again
        processHits(hits, selectionBuffer);
        
        
    }
    
    
    // I copied this method without changes from the mentioned base class.
    // It extracts the data in the selection buffer and writes it on the console.
    public void processHits(int hits, IntBuffer buffer) {
        System.out.println("---------------------------------");
        System.out.println(" HITS: " + hits);
        int offset = 0;
        int names;
        float z1, z2;
        for (int i = 0; i < hits; i++) {
            System.out.println("- - - - - - - - - - - -");
            System.out.println(" hit: " + (i + 1));
            names = buffer.get(offset);
            offset++;
            z1 = (float) buffer.get(offset) / 0x7fffffff;
            offset++;
            z2 = (float) buffer.get(offset) / 0x7fffffff;
            offset++;
            System.out.println(" number of names: " + names);
            System.out.println(" z1: " + z1);
            System.out.println(" z2: " + z2);
            System.out.println(" names: ");

            for (int j = 0; j < names; j++) {
                System.out.print("  " + buffer.get(offset));
                if (j == (names - 1)) {
                    System.out.println("<-");
                } else {
                    System.out.println();
                }
                offset++;
            }
            System.out.println("- - - - - - - - - - - -");
        }
        System.out.println("--------------------------------- ");
    }

    private void drawQuad() {
        gl.glColor3f(0, 1, 1);
        gl.glBegin(GL.GL_QUADS);
        
            gl.glVertex3f(0f, 0f, 0f);
            gl.glVertex3f(0f, 2f, 0f);
            gl.glVertex3f(2f, 2f, 0f);
            gl.glVertex3f(2f, 0f, 0f);
        
        gl.glEnd();
    }

    private void drawSphere() {
        
        gl.glPushMatrix();                          // saves MODELVIEW matrix
        
        gl.glTranslatef(5, 1, 0);
        
        gl.glColor3f(1, 1, 0);
        glut.glutSolidSphere(glu, 1, 10, 10);		// easier than making it by myself...
        
        gl.glPopMatrix();                           // restores previous
                                                    // MODELVIEW MATRIX
        
    }    
    
    private void drawCube() {
        
        gl.glPushMatrix();                          // saves MODELVIEW matrix
        
        gl.glTranslatef(1, 5, 0);
        
        gl.glColor3f(0, 0, 1);
        glut.glutSolidCube(gl, 1);
        
        gl.glPopMatrix();                           // restores previous
                                                    // MODELVIEW MATRIX
        
    }    
    
    public void reshape(GLDrawable drawable, int x, int y, int width,
            int height) {

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        
        ratio = (float)width/height;
        glu.gluPerspective(45,ratio,1, 200);
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();         
    }

    public void displayChanged(GLDrawable arg0, boolean arg1, boolean arg2) {
    }

    public static void main(String args[]) {
        new PickingExample();
    }

    // comes with MouseListener, invoked if user hits a mouse button
    public void mousePressed(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
        mode = GL.GL_SELECT;
    }

    // no use for the following events (of MouseListener) 
    public void mouseClicked(MouseEvent arg0) {}
    public void mouseReleased(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}
    
}