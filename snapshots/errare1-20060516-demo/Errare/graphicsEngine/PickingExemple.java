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

//=================================================================================
//Picking 0.2 (Thomas Bladh)
//=================================================================================
//A simple picking example using java/jogl. This is far from a complete solution 
//but it should give you an idea of how to include picking in your assigment 
//solutions.
//
//Notes: * Based on example 13-3 (p 542) in the "OpenGL Programming Guide"
//     * This version should handle overlapping objects correctly.
//---------------------------------------------------------------------------------
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import net.java.games.jogl.Animator;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLDrawableFactory;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.BufferUtils;


class PickingExemple{


	public static void main(String[] args) 
	{
		new PickingExemple();
	}
	
	PickingExemple()
	{
		Frame frame = new Frame("Picking Example");
		GLDrawableFactory factory = GLDrawableFactory.getFactory();
		GLCapabilities capabilities = new GLCapabilities();
		GLCanvas drawable = factory.createGLCanvas(capabilities);
		drawable.addGLEventListener(new Renderer());
 		frame.add(drawable);
		frame.setSize(400, 400);
		final Animator animator = new Animator(drawable);
	    	frame.addWindowListener(new WindowAdapter()
	   	{
    			public void windowClosing(WindowEvent e) 
	       		{
	       			animator.stop();
  	  			System.exit(0);
    			}
	   	});
	    	frame.show();
 		animator.start();	
	}

	static class Renderer implements GLEventListener, MouseListener, MouseMotionListener 
	{
		static final int NOTHING = 0, UPDATE = 1, SELECT = 2;
		int cmd = UPDATE;
		int mouse_x, mouse_y;
	
	    	private GL gl;
		private GLU glu;
 		private GLDrawable gldrawable;
		
		public void init(GLDrawable drawable) 
	    	{
   			gl = drawable.getGL();
			glu = drawable.getGLU();
	      		this.gldrawable = drawable;
   			gl.glEnable(GL.GL_CULL_FACE);
   			gl.glEnable(GL.GL_DEPTH_TEST);
	      		gl.glEnable(GL.GL_NORMALIZE);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	        	drawable.addMouseListener(this);
   			drawable.addMouseMotionListener(this);
	    	}
 	
 		public void reshape(GLDrawable drawable, int x, int y, int width, int height) 
	    	{
			
			float h = (float) height / (float) width;
			gl.glViewport(0, 0, width, height);
   			gl.glMatrixMode(GL.GL_PROJECTION);
     		gl.glLoadIdentity();
			glu.gluOrtho2D(0.0f,1.0f,0.0f,1.0f);
	    	}

	    	public void display(GLDrawable drawable) 
 		{
	    		IntBuffer selectBuffer;
	    		final int bufferSize  = 10;
	    		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
	    				BufferUtils.SIZEOF_INT*bufferSize);
	    		byteBuffer.order(ByteOrder.nativeOrder());
	    		selectBuffer  = byteBuffer.asIntBuffer();
			switch(cmd)
			{
				case UPDATE:
					drawScene();
					break;
				case SELECT:
					int buffsize = 512;
					double x = (double) mouse_x, y = (double) mouse_y;
					int[] viewPort = new int[4];
			    		;
				    	int hits = 0;
					gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort);
			      		gl.glSelectBuffer(buffsize, selectBuffer);
	    				gl.glRenderMode(GL.GL_SELECT);
					gl.glInitNames();
				    	gl.glMatrixMode(GL.GL_PROJECTION);
			   		gl.glPushMatrix();
		    			gl.glLoadIdentity();
		    			glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d, viewPort);
			  		glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
					drawScene();
					gl.glMatrixMode(GL.GL_PROJECTION);
					gl.glPopMatrix();
					gl.glFlush();
				    	hits = gl.glRenderMode(GL.GL_RENDER);
				    
					processHits(hits, selectBuffer);
					cmd = UPDATE;
					break;
			}
		}

		public void processHits(int hits, IntBuffer buffer)
		{
			System.out.println("---------------------------------");
			System.out.println(" HITS: " + hits);
			int offset = 0;
			int names;
			float z1, z2;
			for (int i=0;i<hits;i++)
			{
				System.out.println("- - - - - - - - - - - -");
				System.out.println(" hit: " + (i + 1));
				names = buffer.get(offset); offset++;
				z1 = (float) buffer.get(offset) / 0x7fffffff; offset++;
				z2 = (float) buffer.get(offset) / 0x7fffffff; offset++;
				System.out.println(" number of names: " + names);
				System.out.println(" z1: " + z1);
				System.out.println(" z2: " + z2);
				System.out.println(" names: ");

				for (int j=0;j<names;j++)
				{
					System.out.print("       " + buffer.get(offset)); 
					if (j==(names-1))
						System.out.println("<-");
					else
						System.out.println();
					offset++;
				}
				System.out.println("- - - - - - - - - - - -");
			}
			System.out.println("---------------------------------");
		}
		
		public int viewPortWidth()
		{
			int[] viewPort = new int[4];
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort);
			return viewPort[2];
		}

		public int viewPortHeight()
		{
			int[] viewPort = new int[4];
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort);
			return viewPort[3];
		}

		public void drawScene()
		{
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			// Colors
   			float red[] =   {1.0f,0.0f,0.0f,1.0f};
 	  		float green[] = {0.0f,1.0f,0.0f,1.0f};
	      		float blue[] =  {0.0f,0.0f,1.0f,1.0f};
	
			// Red rectangle
			GLRectangleEntity r1 = new GLRectangleEntity(gl, glu);
			r1.x = 0.15f;
			r1.y = 0.25f;
			r1.w = 0.4f;
			r1.h = 0.4f;
			r1.c = red;
			r1.id = 10;
			r1.draw();

			// Green rectangle
			GLRectangleEntity r2 = new GLRectangleEntity(gl, glu);
			r2.x = 0.35f;
			r2.y = 0.45f;
			r2.w = 0.4f;
			r2.h = 0.4f;
			r2.c = green;
			r2.id = 20;
			r2.draw();

			// Blue rectangle
			GLRectangleEntity r3 = new GLRectangleEntity(gl, glu);
			r3.x = 0.45f;
			r3.y = 0.15f;
			r3.w = 0.4f;
			r3.h = 0.4f;
			r3.c = blue;
			r3.id = 30;
			r3.draw();

			gl.glFlush();
		}

		public void displayChanged(GLDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
				
 		public void mousePressed(MouseEvent e) 
		{
			cmd = SELECT;
			mouse_x = e.getX();
			mouse_y = e.getY();
		}

		public void mouseEntered(MouseEvent e) {}
 		public void mouseExited(MouseEvent e) {}
	    	public void mouseReleased(MouseEvent e) {}
 		public void mouseClicked(MouseEvent e) {}
	    	public void mouseDragged(MouseEvent e) {}
 		public void mouseMoved(MouseEvent e) {}
 
		public abstract class GLEntity
		{
			float x, y, z;
			float[] c;
			int id = 0;
			boolean outline = false;
			GL gl;
			GLU glu;
			public GLEntity(GL gl, GLU glu)
			{
				this.gl = gl;
				this.glu = glu;
			}
			public void draw()
			{
				gl.glPushName(id);
				_draw();
			}
			public abstract void _draw();
		}

		public class GLRectangleEntity extends GLEntity
		{
			float w = 0.1f;
			float h = 0.1f;
			public GLRectangleEntity(GL gl, GLU glu)
			{
				super(gl, glu);
			}
			public void _draw()
			{
				if (outline)
					gl.glPolygonMode(GL.GL_FRONT, GL.GL_LINE);
				else
					gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);

				gl.glColor4fv(c);
				gl.glBegin(GL.GL_POLYGON);
					gl.glVertex3f(x, y, z);
					gl.glVertex3f(x + w, y, z);
					gl.glVertex3f(x + w, y + h, z);
					gl.glVertex3f(x, y + h, z);
				gl.glEnd();
			}			
		}
	}
}



