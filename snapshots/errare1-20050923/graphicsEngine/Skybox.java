package graphicsEngine;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLDrawableFactory;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLU;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Skybox extends JPanel implements GLEventListener{
	private GLCanvas canvas;
	private ByteBuffer texture1;
	private ByteBuffer texture2;
	private ByteBuffer texture3;
	private ByteBuffer texture4;
	private ByteBuffer texture5;
	private ByteBuffer texture6;
	private int[] textures;
	private int tWidth;
	private int tHeight;
	private float angle;
	private int skyboxList;
	
	/**
	 * Constructor of the Panel.
	 * The constructor creates a GLCanvas and then adds it to itself.
	 */
	public Skybox() {

		super();		
		texture1 = loadTexture(texture1,"graphicsEngine/Textures/ft.jpg");
		texture2 = loadTexture(texture2,"graphicsEngine/Textures/lt.jpg");
		texture3 = loadTexture(texture3,"graphicsEngine/Textures/bk.jpg");
		texture4 = loadTexture(texture4,"graphicsEngine/Textures/rt.jpg");
		texture5 = loadTexture(texture5,"graphicsEngine/Textures/up.jpg");
		texture6 = loadTexture(texture6,"graphicsEngine/Textures/dn.jpg");
		
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setHardwareAccelerated(true);      //We want hardware accelleration
		capabilities.setDoubleBuffered(true);           //And double buffering
		canvas = GLDrawableFactory.getFactory().createGLCanvas(capabilities);
		
		canvas.addGLEventListener(this);
		
		this.add(canvas);
		this.setSize(800, 300);
		canvas.setSize(800, 300);//We want the JPanel and the GLCanvas to have the same size
		canvas.setVisible(true);//This is somehow necessary
	}
	
	/**
	 * The init method will be called when the GLCanvas is constructed.
	 * Here we put the basic settings that will only be called once.
	 * @param glDrawable
	 */
	public void init(GLDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL.GL_FASTEST);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		textures = new int[6];
		gl.glGenTextures(6, textures);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture2);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture3);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture4);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture5);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture6);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		skyboxList = gl.glGenLists(1);
		gl.glNewList(skyboxList, GL.GL_COMPILE);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		
		//face
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,-1,-0.99); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (1,-1,-0.99); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,1,-0.99); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-1,1,-0.99);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		
		//gauche
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-0.99,-1,1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-0.99,-1,-1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (-0.99,1,-1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-0.99,1,1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
		
		//fond
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (1,-1,0.99); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-1,-1,0.99); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (-1,1,0.99); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (1,1,0.99);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
		
		//droite
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (0.99,-1,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (0.99,-1,1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (0.99,1,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (0.99,1,-1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
		
		//haut
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,0.99,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (1,0.99,-1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,0.99,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-1,0.99,1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
		
		//bas
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,-0.99,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-1,-0.99,1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,-0.99,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (1,-0.99,-1); 
		gl.glEnd();
		gl.glEndList();
		
	}
	
	public void display(GLDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		GLU glu = glDrawable.getGLU();
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(60,(float)width/(float)height,0.1,100); 
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glTranslatef(0, 0, 0);
		gl.glRotatef(angle, 0, 1, 0);        
		
		gl.glCallList(skyboxList);
		
		gl.glDisable(GL.GL_TEXTURE_2D);
	}
	
	public void reshape(GLDrawable glDrawable, int i, int i1, int i2, int i3) {
	}
	
	public void displayChanged(GLDrawable glDrawable, boolean b, boolean b1) {
	}
	
	public ByteBuffer loadTexture(ByteBuffer texture, String fn) {
		try {
			BufferedImage buff = ImageIO.read(new File(fn));
			tHeight = buff.getHeight();
			tWidth = buff.getWidth();
			Raster r = buff.getRaster();
			int[] img = null;
			img = r.getPixels(0, 0, 512, 512, img);
			
			texture = ByteBuffer.allocateDirect(tWidth * tHeight * 3);
			for (int y = 0; y < 512; y++)
				for (int x = 0; x < 512; x++) {
					texture.put((byte) img[(y * 512 + x) * 3]);
					texture.put((byte) img[(y * 512 + x) * 3 + 1]);
					texture.put((byte) img[(y * 512 + x) * 3 + 2]);
				}
			tHeight=512;
			tWidth=512;
			
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use Options | File Templates.
		}
		return texture;
	}
	
	public void timeStep(long time) {
		angle+=0.1;
		canvas.repaint();
	}
	
	public static class Test{
		public static void main(String[] args) {
			JFrame jf=new JFrame("skybox");
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Skybox panel=new Skybox();
			jf.getContentPane().add(panel);
			jf.setSize(800,300);
			jf.setVisible(true);
			while(true)
			{
				panel.timeStep(20);
				try {
					Thread.sleep(1000/30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}

