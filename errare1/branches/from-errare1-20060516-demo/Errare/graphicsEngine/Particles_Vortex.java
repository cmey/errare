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

public class Particles_Vortex extends Particles{

	public Particles_Vortex(String rep3D_filename) {
		super(rep3D_filename);
		stars = new star[num];
		for(loop=0; loop<num; loop++){
			stars[loop] = new star();
			stars[loop].angle=0.0f;
			stars[loop].dist =((float)loop/num)*50.0f;
			stars[loop].r = (int)(Math.random()*1000)%256;
			stars[loop].g = (int)(Math.random()*1000)%256;
			stars[loop].b = (int)(Math.random()*1000)%256;
		}
	}

	@Override
	protected void drawParticles(GL gl) {
		for(loop=num-1; loop>=0; loop--){                   // Loop Through All The Stars
			gl.glPushMatrix();                              // Reset The View Before We Draw Each Star
			gl.glRotatef(stars[loop].angle,0.0f,1.0f,0.0f);    // Rotate To The Current Stars Angle
			gl.glTranslatef(stars[loop].dist,0.0f,0.0f);       // Move Forward On The X Plane
			gl.glRotatef(-stars[loop].angle,0.0f,1.0f,0.0f);   // Cancel The Current Stars Angle
			gl.glRotatef(spin,0.0f,0.0f,1.0f);
			gl.glColor4ub((byte)stars[loop].r,(byte)stars[loop].g,(byte)stars[loop].b,(byte)255);
			
			
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
		spin+=0.1f;
		for(loop=num-1; loop>=0; loop--){
		stars[loop].angle+=(float)loop/num;
		stars[loop].dist-=0.01f;
		
		if(stars[loop].dist<0.0f){
			stars[loop].dist+=5.0f;
			stars[loop].r = (int)(Math.random()*1000)%256;
			stars[loop].g = (int)(Math.random()*1000)%256;
			stars[loop].b = (int)(Math.random()*1000)%256;
		}
		}
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
