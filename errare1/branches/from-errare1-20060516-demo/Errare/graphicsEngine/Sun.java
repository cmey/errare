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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

public class Sun extends GraphicalRep{
	
	public int sunX, sunY, sunZ;
	public int sunsize = 100;
	private Calendar calendar;
	public int hour24;
	public boolean has_just_changed; //will be used to motion from one hour to another
	public int rayonMax=400;
	
	
	public Sun(String filename){
		super(filename, GraphicalRep.FORMAT_SUN);
	}
	
	public void setMapSize(int map_size){
		sunZ = map_size/2;
		rayonMax = map_size/8;
	}
	
	
	public void draw(GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz){
		GL2 gl = gld.getGL().getGL2();
		gl.glPushMatrix();
		doPosition(gl,sunX, sunY, sunZ, 0,0,0);
		
		if(prem) {
			doExpandTexture(gl);
			prem=false;
		}
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		//System.out.println(internal_texture[0]);
		
		gl.glEnable(GL.GL_BLEND);   // Enable blending for transparency
		gl.glColor4f(1,1,1,1);
		
		Billboard(gl);
		
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-sunsize,-sunsize, 0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( sunsize,-sunsize, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( sunsize, sunsize, 0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-sunsize, sunsize, 0.0f);
		gl.glEnd();
		
		
		
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
	
	
	public void updateSun(){
		//hour24 = calendar.get(Calendar.HOUR_OF_DAY); // 0..23
		calendar = new GregorianCalendar();
		int previous_hour24 = hour24;
		hour24 = calendar.get(Calendar.SECOND);             // 0..59
		if(previous_hour24 != hour24){
			has_just_changed=true;
		}else{
			has_just_changed=false;
		}
		//TODO changer 59 -> 23 quand je mettrai les heures
		sunX = (int) (rayonMax * Math.cos((double)hour24/59*Math.PI*2));
		sunY = (int) (rayonMax * Math.sin((double)hour24/59*Math.PI*2));
	}
}
