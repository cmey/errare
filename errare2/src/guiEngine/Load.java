/*Errare is a free and crossplatform MMORPG project.
 Copyright (C) 2006  Arnaud KNOBLOCH
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.*/

package guiEngine;

import gameEngine.GameEngine;
import genericReps.ItemRep;
import geom.Circle;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Text2D;
import graphicsEngine.Texture;
import graphicsEngine.TextureFactory;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import com.sun.opengl.util.GLUT;

import logger.Logger;
import main.ClientMain;
import soundEngine.SoundEngine;
import xmlEngine.XmlEngine;

/**
 * Class GuiEngine
 * Create, display, control graphics users interfaces
 * @author Ak
 */

public class Load {
	
	
	// Boolean indicate if the engine is link to the main or not (developpement)
	private boolean DEV=false;
	
	// A new GLCanvas to test in developpement
	private GLCanvas canvas;
	

	private int selectCursor;
	
	// The tab associate with the texture of items'display list
	private int[] texture;

	private GuiEngine engine;
	
	private int screenWidth,screenHeight;
	
	public int x;
	
	public static final float [] LOADING_BORDER_COLOR = {1.0f,1.0f,1.0f};
	public static final float [] LOADING_COLOR = {0.0f,0.0f,1.0f};
	public static final int LOADING_WIDTH = 400;
	public static final int LOADING_HEIGHT = 40;
	
	public int LOADING_X;
	public int LOADING_Y;
	
	private int percentage;
	
	private int billface01,billface02,tombofmalhukand01,tombofmalhukand02;
	
	private static final int BILLFACE_01_WIDTH=700,BILLFACE_01_HEIGHT=525,
							BILLFACE_02_WIDTH=800,BILLFACE_02_HEIGHT=440,
							TOMBOFMALHUKAND_01_WIDTH=800,TOMBOFMALHUKAND_01_HEIGHT=600,
							TOMBOFMALHUKAND_02_WIDTH=640,TOMBOFMALHUKAND_02_HEIGHT=480;
	
	private int background;
	
	private GLUT glut;
	private int font;
	
	/** The tab that contain the path of each texture */
	private String[] texturePathTab;
	
	/**
	 * GuiEngine Constructor
	 * Constructor for developpement
	 * Create a Listener onto the GLCanvas,a new soundEngine,
	 * a new xmlEngine and a new gameEngine
	 */
	
	public Load(GuiEngine engine){
		
		this.engine=engine;

		LOADING_X = (engine.getScreenWidth()/2)-(LOADING_WIDTH/2); 
		LOADING_Y = (engine.getScreenHeight()/2)-(LOADING_HEIGHT/2); 

		percentage=0;
		texture = new int[5];
		background = (int) (Math.random()*10);
		
		glut = new GLUT();
		font = GLUT.BITMAP_HELVETICA_18;
		
		}
	
	
	
	/**
	 * glBeginOrtho fonction
	 * @param gLDrawable the GLDrawable object
	 * Initialise the environnement
	 * Set the point 0,0 to the top left (like java and not like openGL)
	 * Set objects in the right position
	 **/
	
	public void glBeginOrtho(GLAutoDrawable gLDrawable) {
		
		GL gl = gLDrawable.getGL();
		GLU glu = new GLU();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		// The left lower corner (0,0) will be attached with that one of the window 
		glu.gluOrtho2D(
				0, (int) gLDrawable.getWidth(),
				0, (int) gLDrawable.getHeight());
		
		// Set the point of creation of the image in the left higher corner
		gl.glScaled(1,-1,1);
		
		// met le rep�re en haut � gauche
		gl.glTranslatef(0,-(int) gLDrawable.getHeight(),0); 
		
	}
	
	
	/**
	 * displayGUI fonction
	 * @param gLDrawable the GLDrawable object
	 * Display all graphics users interfaces in the right places
	 */
	
	public void displayGUI(GLAutoDrawable gLDrawable){
			
		GL gl = gLDrawable.getGL();
		
		// If we test we must clear the buffer
		
		// Reset The View
		gl.glLoadIdentity();         
		
		// Initialise the environnement (position)
		glBeginOrtho(gLDrawable);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		
		gl.glEnable(GL.GL_BLEND);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		engine.getMain().getGraphicsEngine().reshape(gLDrawable, 0, 0, engine.getScreenWidth(), engine.getScreenHeight());
	
		switch(background) {

		case 1:
			gl.glTranslated((int)(engine.getScreenWidth()/2)-BILLFACE_01_WIDTH/2,(int)(engine.getScreenHeight()/2)-BILLFACE_01_HEIGHT/2,0);
			gl.glCallList(billface01);
			break;
		case 2:
			gl.glTranslated((int)(engine.getScreenWidth()/2)-BILLFACE_02_WIDTH/2,(int)(engine.getScreenHeight()/2)-BILLFACE_02_HEIGHT/2,0);
			gl.glCallList(billface02);
			break;
		case 3:
			gl.glTranslated((int)(engine.getScreenWidth()/2)-TOMBOFMALHUKAND_01_WIDTH/2,(int)(engine.getScreenHeight()/2)-TOMBOFMALHUKAND_01_HEIGHT/2,0);
			gl.glCallList(tombofmalhukand01);
			break;
		case 4:
			gl.glTranslated((int)(engine.getScreenWidth()/2)-TOMBOFMALHUKAND_02_WIDTH/2,(int)(engine.getScreenHeight()/2)-TOMBOFMALHUKAND_02_HEIGHT/2,0);
			gl.glCallList(tombofmalhukand02);
			break;
		default :
			gl.glTranslated((int)(engine.getScreenWidth()/2)-TOMBOFMALHUKAND_02_WIDTH/2,(int)(engine.getScreenHeight()/2)-TOMBOFMALHUKAND_02_HEIGHT/2,0);
			gl.glCallList(tombofmalhukand02);
		}

		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glColor3f(0.0f,0.0f,1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(LOADING_X,LOADING_Y);
		gl.glVertex2d(LOADING_X,LOADING_Y+LOADING_HEIGHT);
		gl.glVertex2d(LOADING_X+LOADING_WIDTH,LOADING_Y+LOADING_HEIGHT);
		gl.glVertex2d(LOADING_X+LOADING_WIDTH,LOADING_Y);
		gl.glEnd();
		gl.glPopMatrix();
		
		if ((LOADING_X+1+x)<=(LOADING_X+LOADING_WIDTH)) {
			gl.glColor3f(LOADING_BORDER_COLOR[0], LOADING_BORDER_COLOR[1], LOADING_BORDER_COLOR[2]);
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			percentage=(x*100)/LOADING_WIDTH;
			gl.glRasterPos2i(LOADING_X+x,LOADING_Y+LOADING_HEIGHT/2-8);
			glut.glutBitmapString(font, percentage+" %");
			gl.glPopAttrib();
			
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glColor3f(1.0f,0.0f,0.0f);
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2d(LOADING_X+1,LOADING_Y+1);
			gl.glVertex2d(LOADING_X+1,LOADING_Y+LOADING_HEIGHT-2);
			gl.glVertex2d(LOADING_X+x,LOADING_Y+LOADING_HEIGHT-2);
			gl.glVertex2d(LOADING_X+x,LOADING_Y+1);
			gl.glEnd();
			gl.glPopMatrix();
		}else {
			gl.glColor3f(LOADING_BORDER_COLOR[0], LOADING_BORDER_COLOR[1], LOADING_BORDER_COLOR[2]);
			
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			gl.glRasterPos2i(LOADING_X+LOADING_WIDTH,LOADING_Y+LOADING_HEIGHT/2-8);
			glut.glutBitmapString(font, "100 %");
			gl.glPopAttrib();
			
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glColor3f(1.0f,0.0f,0.0f);
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex2d(LOADING_X+1,LOADING_Y+1);
			gl.glVertex2d(LOADING_X+1,LOADING_Y+LOADING_HEIGHT-2);
			gl.glVertex2d(LOADING_X+LOADING_WIDTH,LOADING_Y+LOADING_HEIGHT-2);
			gl.glVertex2d(LOADING_X+LOADING_WIDTH,LOADING_Y+1);
			gl.glEnd();
			gl.glPopMatrix();
		}
	
		
		gl.glPopAttrib();
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
		
		
		if (DEV)canvas.repaint();
		
	}

	
	/**
	 * reshapeGui fonction
	 * @param gLdrawable the GLDrawable fonction
	 * @param xstart the x start
	 * @param ystart the y start
	 * @param width the width
	 * @param height the hieght
	 */
	
	public void reshapeGUI(GLAutoDrawable gLdrawable,
			int xstart,int ystart,
			int width, int height){
		
		GL gl = gLdrawable.getGL();
		GLU glu = new GLU();
		
		height = (height == 0) ? 1 : height;
		
		gl.glViewport(0,0,width,height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45,(float)width/height,1,1000);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * displayChangedGUI fonction
	 * @param gLDrawable
	 * @param modeChanged boolean
	 * @param deviceChanged boolean
	 * NOT WORK WITH JAVA
	 */
	
	public void displayChangedGUI(GLAutoDrawable gLDrawable,
			boolean modeChanged,
			boolean deviceChanged){}

	
	/**
	 * buildLists fonction
	 * @param gLDrawable the GLDrawable object
	 * Build the display list (faster)
	 */
	
	public void buildLists(GLAutoDrawable gLDrawable){
		
		Logger.printINFO("Build display lists...");

		int cpt=0;
		
		GL gl = gLDrawable.getGL();
			
		billface01 = gl.glGenLists(5);
		gl.glNewList(billface01,GL.GL_COMPILE); 
		draw(gLDrawable,1024,1024,cpt++); 
		gl.glEndList();
		
		billface02 = billface01+1;
		gl.glNewList(billface02,GL.GL_COMPILE); 
		draw(gLDrawable,1024,1024,cpt++);
		gl.glEndList();
	
		tombofmalhukand01 = billface02+1;
		gl.glNewList(tombofmalhukand01,GL.GL_COMPILE); 
		draw(gLDrawable,1024,1024,cpt++); 
		gl.glEndList();
		
		tombofmalhukand02 = tombofmalhukand01+1;
		gl.glNewList(tombofmalhukand02,GL.GL_COMPILE); 
		draw(gLDrawable,1024,1024,cpt++);
		gl.glEndList();
	}
	
	/**
	 * draw fonction
	 * @param gLDrawable the GLDrawable object
	 * @param w the width of the texture
	 * @param h the height of th texture
	 * @param t the type of the texture
	 * Draw a square with a texture (type)
	 */
	
	public void draw(GLAutoDrawable gLDrawable, int w, int h, int t){
		
		GL gl = gLDrawable.getGL();
		
		gl.glPushMatrix();	
		// Set the image in the right postion
		gl.glTranslated(0,h,0);
		gl.glScaled(1,-1d,0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[t]);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,0);gl.glVertex2i(0,0);
		gl.glTexCoord2i(1,0);gl.glVertex2i(w,0);
		gl.glTexCoord2i(1,1);gl.glVertex2i(w,h);
		gl.glTexCoord2i (0,1);gl.glVertex2i(0,h);  
		gl.glEnd();
		
		gl.glPopMatrix();
		
		gl.glLoadIdentity();
		
	}
	
	
	/**
	 * loadGLTexture fonction
	 * @param gLDrawable the GLDrawable object
	 */
	
	public void loadGLTexture(GLAutoDrawable gLDrawable){
		
		GL gl = gLDrawable.getGL();

		gl.glGenTextures(72/* NBTEXTURES */, texture, 0);

		Logger.printINFO("Loading textures...");
		Texture textureLoaded = null;
		int cpt = 0;

		/* --- items textures --- */
		TextureFactory textureLoader = new TextureFactory();
		texturePathTab = new String[4/* NBTEXTURES */];
		texturePathTab[cpt++] = "data/images/gui/loading/billface_01.png";
		texturePathTab[cpt++] = "data/images/gui/loading/billface_02.png";
		texturePathTab[cpt++] = "data/images/gui/loading/tombofmalhukand_01.png";
		texturePathTab[cpt++] = "data/images/gui/loading/tombofmalhukand_02.png";
		cpt = 0;
		for (String texturePath : texturePathTab) {
			try {
				textureLoaded = textureLoader.loadTexture(texturePath);
				textureLoaded.doExpandTexture(gl);
				texture[cpt] = textureLoaded.internal_index[0];
				gl.glBindTexture(GL.GL_TEXTURE_2D, texture[cpt++]);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		

;
	
	}
	
		
}
