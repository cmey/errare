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

import net.java.games.jogl.GL;

public class Particles_Ring extends Particles{
	private int how_many = 50;
	protected float psize = 100;
	
	public Particles_Ring(String rep3D_filename) {
		super(rep3D_filename);
		num = how_many;
		stars = new star[num];
		for(loop=0; loop<num; loop++){
			stars[loop] = new star();
			stars[loop].angle =(float) 360 / num * loop ;
			stars[loop].dist = (float)Math.random()*10;
			stars[loop].r = (int)(Math.random()*1000)%256;
			stars[loop].g = (int)(Math.random()*1000)%256;
			stars[loop].b = (int)(Math.random()*1000)%256;
		}
	}
	
	@Override
	protected void drawParticles(GL gl) {
		for(loop=num-1; loop>=0; loop--){               // Loop Through All The Stars
			gl.glPushMatrix();    // Reset The View Before We Draw Each Star
			gl.glRotatef(stars[loop].angle,0.0f,1.0f,0.0f);    // Rotate To The Current Stars Angle
			gl.glTranslatef(stars[loop].dist,0.0f,0.0f);       // Move Forward On The X Plane
			gl.glRotatef(-stars[loop].angle,0.0f,1.0f,0.0f);   // Cancel The Current Stars Angle
			gl.glColor4ub((byte)stars[loop].r,(byte)stars[loop].g,(byte)stars[loop].b,(byte)255);
			
			gl.glRotatef(spin,0.0f,0.0f,1.0f);
			Billboard(gl);
			
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-psize,-psize, 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( psize,-psize, 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( psize, psize, 0.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-psize, psize, 0.0f);
			gl.glEnd();
			
			
			gl.glPopMatrix();
			
		}
	}
	
	public void tick(){
		spin+=1f;
		for(int i=0; i<stars.length;i++){
			stars[i].angle+= 10;
			stars[i].dist+=5f;
		
			if(stars[i].dist>500){
				die();
			}
		}
	}
	
	public void die(){
		num=0;
	}
	
	public void reset(){
		num = how_many;
		for(int i=0; i<stars.length;i++){
			stars[i].dist=(float)Math.random()*10;
		}
	}
	
	
}
