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

import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import logger.Logger;

/**
 * An ingame sun, which is also a light source, orbiting around the planet.
 * @author Christophe
 */
public class Sun extends Light{
	
	public int revolutionDuration = 15000; // number of miliseconds for the Sun to do a complete revolution
	transient public int maxRevolutionRadius=50;
	transient public int sunsize = 5;
	transient public long time;
	transient public boolean has_just_changed; //will be used to motion from one hour to another
	transient private Texture tex;
	public static final String STD_SUN = "data/images/sky/sun1.png";
	
	
	protected void drawByTriangles(GLAutoDrawable gld) {
	}
	
	public Sun(){
		posX = 1000;
		posY = 500;
		posZ = 1000;
		try {
			tex = TextureFactory.loadTexture(STD_SUN);
		} catch (IOException e) {
			Logger.printERROR("STD_SUN "+STD_SUN+ ": file not found !");
			System.exit(0);
		}
	}
	
	
	public void setMapSize(int map_size){
		posZ = map_size/2;
		maxRevolutionRadius = map_size/8;
	}
	
	
	public void draw(GLAutoDrawable gld){
		GL gl = gld.getGL();
		gl.glPushMatrix();
		doPosition(gl,posX, posY, posZ, 0,0,0);
		
		if(prem) {
			tex.doExpandTexture(gl);
			prem=false;
		}
		gl.glPushAttrib(GL.GL_ENABLE_BIT);
		gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);
		
		gl.glEnable(GL.GL_BLEND);   // Enable blending for transparency
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glColor4f(1,1,1,1);
		
		Billboard(gl);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-sunsize,-sunsize, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( sunsize,-sunsize, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( sunsize, sunsize, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-sunsize, sunsize, 0.0f);
		gl.glEnd();
		
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
	
	
	public void updateSun(){
		
		long previous_time = time;
		time = System.currentTimeMillis();
		time = time%revolutionDuration;
		if(previous_time != time){
			has_just_changed=true;
		}else{
			has_just_changed=false;
		}
		
		posX = (int) (maxRevolutionRadius * Math.cos((double)time/revolutionDuration*Math.PI*2));
		posY = (int) (maxRevolutionRadius * Math.sin((double)time/revolutionDuration*Math.PI*2));
	}
	
	public String toString(){
		return "Sun";
	}
}
