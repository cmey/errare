package graphicsEngine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import main.Main;
import main.Rep;
import net.java.games.jogl.Animator;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLDrawableFactory;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLU;
import physicsEngine.MainCharacter;
import physicsEngine.PhysicalRep;

public class GraphicsEngine extends JPanel implements Runnable, GLEventListener{
	public float lookX = 0f;
	public float lookY = 0f;
	public float lookZ = 0f;
	public float camX = 0f;
	public float camY = 0f;
	public float camZ = 0f;
	private MouseHelper mouseHelper = new MouseHelper(100, this);
	private KeyboardHelper keyboardHelper = new KeyboardHelper();
	private GLCanvas glc;
	private GL gl;
	private GLU glu;
	private int width, height;
	private int nb_frames;
	private List<Rep> li;
	private Rep currentRep;
	private GraphicalRep currentGRep;
	private PhysicalRep currentPRep;
	private Main main;
	private MainCharacter mainChar;
	private boolean tex2change;
	
	public GraphicsEngine(Main main){
		this.main = main;
		li = new LinkedList();
		GLCapabilities capabilities=new GLCapabilities();
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		glc = GLDrawableFactory.getFactory().createGLCanvas(capabilities);
		glc.addGLEventListener(this);
		glc.addMouseListener(mouseHelper);
		glc.addMouseMotionListener(mouseHelper);
		glc.addKeyListener(keyboardHelper);
		this.add(glc);
		this.setSize(640,480);
		glc.setSize(640,480);
		glc.setVisible(true);
		width = glc.getWidth();
		height = glc.getHeight();
		//Animator animator = new Animator(glc);
		//animator.start();
		glc.requestFocus();
		Thread t = new Thread(this);
		t.start();
		Thread tkey = new Thread(keyboardHelper);
		tkey.start();
		System.out.println("Starting game...");
	}
	
	public void init(GLDrawable glDrawable) {
		gl = glDrawable.getGL();
		glu = glDrawable.getGLU();
		gl.glViewport(0,0,width,height);
		gl.glMatrixMode(GL.GL_PROJECTION); 
		gl.glLoadIdentity(); 
		glu.gluPerspective(45,(float)width/(float)height,0.1,1000);
		gl.glMatrixMode(GL.GL_MODELVIEW); 
		gl.glLoadIdentity(); 
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glClearColor(1,1,1,1);
		gl.glColor3f(0,1,1);
		glc.requestFocusInWindow();
		
		float[] LightAmbient = { 0.5f , 0.5f , 0.5f , 1.0f };
		float[] LightDiffuse = { 1.0f , 1.0f , 1.0f , 1.0f };
		float[] LightPosition = { 0.0f, 0.0f, 2.0f, 1.0f };
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, LightAmbient);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, LightDiffuse);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, LightPosition);
		gl.glEnable(GL.GL_LIGHT1);
		//gl.glEnable(GL.GL_LIGHTING);
	}

	public void display(GLDrawable glDrawable) {
		gl=glDrawable.getGL();
		glu=glDrawable.getGLU();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		camX=100;camY=500;camZ=500; lookX=lookY=lookZ=1;
		glu.gluLookAt(camX, camY, camZ, lookX, lookY, lookZ, 0,1,0);	
		
		mouseHelper.updateCamera(gl,glu);
		keyboardHelper.updateCamera(gl);
		for (Rep currentRep : li) {
			currentGRep = currentRep.getGraphicalRep();
			currentPRep = currentRep.getPhysicsRep();
			if(currentGRep.getFormat()==1) { //.MD2
				currentGRep.drawMD2(gl,glu,currentPRep.x,currentPRep.y,0,0,0,0);
			}
			if(currentGRep.getFormat()==2) { //.errare
				currentGRep.drawERA(gl,glu,currentPRep.x,currentPRep.y,0,0,0,0);
			}
		}
	}

	public void reshape(GLDrawable glDrawable, int x, int y, int width, int height) 
	{
		GL gl = glDrawable.getGL();
		gl.glViewport(x, y, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();                        
		float aspect = (float)((float) width / (float) height);
		glDrawable.getGLU().gluPerspective(45, aspect, 0.1f, 5000.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity(); 
		glc.requestFocusInWindow();
	}

	public void setDisplayList(List <Rep> li){
		// appelé par Martin: physicsRep
		this.li=li;
	}

	public void setMainChar(Rep r) {
		PhysicalRep pr = r.getPhysicsRep();
		if(pr instanceof MainCharacter)
			mainChar = (MainCharacter)pr;
	}

	
	public void displayChanged(GLDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			glc.repaint();
		}
	}
	
	public float getCamX(){
		return camX;
	}
	public float getCamY(){
		return camY;
	}
	public float getCamZ(){
		return camZ;
	}
	public float getLookX(){
		return lookX;
	}
	public float getLookY(){
		return lookY;
	}
	public float getLookZ(){
		return lookZ;
	}
	public GLU getGlu(){
		return glu;
	}
	
	public GL getGL(){
		return gl;
	}	
	public Main getMain(){
		return main;
	}
	
public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
	}
	//picking
	//appeler SetPick(Point2D p, boolean b) chez Martin:physicsRep 
}
