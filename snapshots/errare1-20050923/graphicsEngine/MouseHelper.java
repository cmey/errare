package graphicsEngine;
/**
 * Mouse helper by Cyberchrist.
 * Implements simple mouse rotation/zoom.
 */

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import physicsEngine.PhysicsEngine;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLU;

public class MouseHelper implements MouseListener, MouseMotionListener
{
    private float angleX, angleY;
    private float angleX0, angleY0;
    private float dist;
    private float dist0;
    private float mov0, mov;
    private int mouseX, mouseY;
    private int lastButton;
    private PhysicsEngine pe;
    private GraphicsEngine ge;
    private GL gl;
    private GLU glu;
    /**
     * Construct new mousehelper.
     * @param dist Initial distance to camera
     */
    public MouseHelper(float dist, GraphicsEngine ge)
    {
        this.dist = dist;
        this.ge=ge;
    }
    
    /**
     * Construct new mousehelper.     
     */
    public MouseHelper()
    {     
    }
    
    /**
     * Update camera.
     * @param gl GL object
     */
    public void updateCamera(GL gl, GLU glu)
    {
    	this.gl=gl;
    	this.glu=glu;
        gl.glTranslatef(mov, 0.0f, -dist);
        gl.glRotated(angleX, 1, 0, 0);
        gl.glRotated(angleY, 0, 1, 0);
    }
    
    public void mouseClicked(MouseEvent e) {
    	mouseX=e.getX();
    	mouseY=e.getY();
    	
    	double[] modelView = new double[16];
    	double[] projection = new double[16];
    	int[] viewport = new int[4];

    	 gl.glRenderMode(GL.GL_SELECT);
    	 
    	viewport[0]=1;
    	viewport[1]=2;
    	/* SOUCIS ICI */
		gl.glGetDoublev(gl.GL_MODELVIEW_MATRIX, modelView);
		gl.glGetDoublev(gl.GL_PROJECTION_MATRIX, projection);
		gl.glGetIntegerv(gl.GL_VIEWPORT, viewport);
		System.out.println(viewport[0]+" "+viewport[1]+" "+viewport[2]+" "+viewport[3]);
		/* SOUCIS ICI */

		double winX; winX=e.getX();
		double winY; winY=e.getY();
		winY = (double)viewport[3] - (double)winY;
		double winZ; winZ=0.1;
		//ge.getGL().glReadPixels( (int)winX, (int)winY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, winZ );
		double[] objX = new double[1];
		double[] objY = new double[1];
		double[] objZ = new double[1];
		glu.gluUnProject(
		winX, winY, winZ,
		modelView,
		projection,
		viewport,
		objX, objY, objZ
		);
		System.out.println(objX[0] + " " + objY[0] + " " + objZ[0]);
		
    	double intersecX=objX[0]-(objY[0]/(ge.getLookY()-ge.getCamY()))*(ge.getLookX()-ge.getCamX());
    	double intersecZ=objZ[0]-(objY[0]/(ge.getLookY()-ge.getCamY()))*(ge.getLookZ()-ge.getCamZ());
    	
    	System.out.println("intersecX: "+intersecX+"\nintersecZ: "+intersecZ);
    	
    	Point pt=new Point((int)intersecX,(int)intersecZ);
    	ge.getMain().getPhysicsEngine().setClick(e, pt);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) 
    {
    	lastButton = e.getButton();
        if (lastButton == MouseEvent.BUTTON1) 
        {
            angleX0 = angleX; angleY0 = angleY;
            mouseX = e.getX(); mouseY = e.getY();
        } 
        else if (lastButton == MouseEvent.BUTTON3) 
        {
            dist0 = dist;
            mouseY = e.getY();
            mouseX = e.getX();
            mov0 = mov;
        }
    }

    public void mouseReleased(MouseEvent e) {  }

    // MouseMotionListener 
    public void mouseDragged(MouseEvent e) 
    {
        int dx, dy, dz;
        if (lastButton == MouseEvent.BUTTON1) 
        {
            dx = mouseX - e.getX(); dy = mouseY - e.getY();
            angleY = angleY0 - dx;
            angleX = angleX0 - dy;
        }
        if (lastButton == MouseEvent.BUTTON3) 
        {
            dz = mouseY - e.getY();
            dist = dist0 + dz;
            dx = mouseX - e.getX();
            mov = mov0 + dx;
        }
    }

    public void mouseMoved(MouseEvent e) {}
}