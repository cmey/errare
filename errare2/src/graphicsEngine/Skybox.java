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

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import logger.Logger;

public class Skybox implements GLEventListener, Runnable{
	transient private static GLCanvas canvas;
	transient private boolean TEST;
	transient private ByteBuffer texture1;
	transient private ByteBuffer texture2;
	transient private ByteBuffer texture3;
	transient private ByteBuffer texture4;
	transient private ByteBuffer texture5;
	transient private ByteBuffer texture6;
	transient private int MapSize;
	transient private int[] textures;
	transient private int tWidth;
	transient private int tHeight;
	transient private float angle;
	transient private int skyboxList;
	
	
	/**
	 * Constructor of the Panel.
	 * The constructor creates a GLCanvas and then adds it to itself.
	 */
	public Skybox(){
		TEST=false;
		load();
	}
	
	public Skybox(boolean b){
		TEST=true;
		load();
	}
	
	private void load(){
		texture1 = loadTexture(texture1,"data/images/skybox/desert/desert_rt.png");
		texture2 = loadTexture(texture2,"data/images/skybox/desert/desert_bk.png");
		texture3 = loadTexture(texture3,"data/images/skybox/desert/desert_lf.png");
		texture4 = loadTexture(texture4,"data/images/skybox/desert/desert_ft.png");
		texture5 = loadTexture(texture5,"data/images/skybox/desert/desert_up.png");
		texture6 = loadTexture(texture6,"data/images/skybox/desert/desert_dn.png");
	}
	
	/**
	 * The init method will be called when the GLCanvas is constructed.
	 * Here we put the basic settings that will only be called once.
	 * @param glDrawable
	 */
	public void init(GLAutoDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		//gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//gl.glShadeModel(GL.GL_FASTEST);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		textures = new int[6];
		gl.glGenTextures(6, textures,0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture2.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture3.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture4.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture5.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture6.rewind());
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		
		if(!gl.glIsList(skyboxList))
			skyboxList = gl.glGenLists(1);
		gl.glNewList(skyboxList, GL.GL_COMPILE);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
		
		//face
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,-1,-0.999); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (1,-1,-0.999); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,1,-0.999); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-1,1,-0.999);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
		
		//gauche
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-0.999,-1,1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-0.999,-1,-1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (-0.999,1,-1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-0.999,1,1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
		
		//fond
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (1,-1,0.999); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-1,-1,0.999); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (-1,1,0.999); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (1,1,0.999);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
		
		//droite
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (0.999,-1,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (0.999,-1,1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (0.999,1,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (0.999,1,-1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
		
		//haut
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,0.999,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (1,0.999,-1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,0.999,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (-1,0.999,1);
		gl.glEnd();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
		
		//bas
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,1);gl.glVertex3d (-1,-0.999,-1); 
		gl.glTexCoord2i(1,1);gl.glVertex3d (-1,-0.999,1); 
		gl.glTexCoord2i(1,0);gl.glVertex3d (1,-0.999,1); 
		gl.glTexCoord2i(0,0);gl.glVertex3d (1,-0.999,-1); 
		gl.glEnd();
		gl.glEndList();
		
	}
	
	public void display(GLAutoDrawable glDrawable) {
		GL gl = glDrawable.getGL();
		
		if(TEST)
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		gl.glPushAttrib(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		if(TEST)
			gl.glRotatef(angle, 0, 1, 0);  
		else
			gl.glTranslatef(MapSize/2,0,MapSize/2);
		
		if(!TEST)
			gl.glScalef(MapSize/2,MapSize/2,MapSize/2);
		gl.glCallList(skyboxList);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
	public void reshape(GLAutoDrawable glDrawable, int i, int i1, int i2, int i3) {
		GL gl = glDrawable.getGL();
		GLU glu = new GLU();
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60,(float)width/(float)height,0.1,100); 
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
	}
	
	public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
	}
	
	public ByteBuffer loadTexture(ByteBuffer texture, String fn) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fn);
			BufferedImage buff = ImageIO.read(is);
			tHeight = buff.getHeight();
			tWidth = buff.getWidth();
			if(tHeight != 512 || tWidth != 512){
				Logger.printFATAL("Skybox must be 512x512 textures!");
				Logger.printFATAL("Yours is: "+tHeight+"x"+tWidth);
				System.exit(-1);
			}
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
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
	

	public void setMapSize(int mapsize){
		this.MapSize=mapsize;
	}
	
	public GLCanvas getGLCanvas(){
		return canvas;
	}
	
	public void setGLCanvas(GLCanvas c) {
		this.canvas=c;
	}
	
	public void run()
	{
		while(true){
			try {
				Thread.sleep(1000/60);
				timeStep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void timeStep(long time) {
		angle+=0.1;
		canvas.repaint();
	}
	
	public static void main(String[] args) {
			JFrame jf=new JFrame("skybox");
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Skybox sb=new Skybox(true);
			
			GLCapabilities capabilities = new GLCapabilities();
			capabilities.setHardwareAccelerated(true);      //We want hardware acceleration
			capabilities.setDoubleBuffered(true);           //And double buffering
			canvas = new GLCanvas(capabilities);
			
			canvas.addGLEventListener(sb);
			
			canvas.setSize(1024,150);//We want the JPanel and the GLCanvas to have the same size
			canvas.setVisible(true);//This is somehow necessary
			jf.getContentPane().add(sb.getGLCanvas());
			jf.setSize(1024,150);
			jf.setVisible(true);
			
			
			
			while(true)
			{
				sb.timeStep(20);
				try {
					Thread.sleep(1000/30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}

	

	
}

