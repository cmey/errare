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

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;


public class Cinema extends Graphics{

	private static int MAX_CINEMA_SIZE = 100;
	private static int a_size;
	private static boolean cinema_on;
	
	public Cinema(){
		cinema_on=false;
	}
	
	public static void cinemaOn(){
		cinema_on=true;
	}
	
	public static void cinemaOff(){
		cinema_on=false;
	}
	
	public void switchState(){
		cinema_on = !cinema_on;
	}
	
	public void draw_cinema(GL gl){

		MAX_CINEMA_SIZE = ge.winheight/8;
		if(a_size>0){
			gl.glDisable(GL.GL_DEPTH_TEST);
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glOrtho(0,ge.winwidth,0,ge.winheight,-1,1);
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glDisable(GL.GL_TEXTURE_2D);
			gl.glDisable(GL.GL_CULL_FACE);
			gl.glColor3f(0,0,0);
			gl.glRecti(0,0,ge.winwidth,a_size);
			gl.glRecti(0,ge.winheight,ge.winwidth,ge.winheight-a_size);
			gl.glColor3f(1,1,1);
			gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glEnable(GL.GL_DEPTH_TEST);
		}	
			if(cinema_on && a_size < MAX_CINEMA_SIZE)
				a_size++;
			else if(!cinema_on && a_size > 0)
				a_size--;
		

	}
	
	public static boolean getCinemaState(){
		return cinema_on;
	}

	@Override
	protected void draw(GLAutoDrawable gld) {
		draw_cinema(gld.getGL());
	}

	@Override
	protected
	void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
}
