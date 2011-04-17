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
import net.java.games.jogl.GLDrawable;

public class Skydome extends GraphicalRep{
	
	int MapSize = 1000;
	
	private int theta,phi;
	private int dtheta = 15;
	private int dphi = 15;
	private float DTOR = (float) (Math.PI/180f);
	private int numVertices = (int)((360/dtheta)*(90/dphi)*4);
	private DomeVertex[] Vertices;
	private float radius;
	
	public Skydome(String filename){
		super(filename,0);
		Vertices = new DomeVertex[numVertices];
		create_vertex_array();
	}
	
	public void create_vertex_array(){
		int n = 0;
		for (phi=0; phi <= 90 - dphi; phi += (int)dphi) {
			for (theta=0; theta <= 360 - dtheta; theta += (int)dtheta) {
				Vertices[n].x = (float) (radius * Math.sin(phi*DTOR) * Math.cos(DTOR*theta));
				Vertices[n].y = (float) (radius * Math.sin(phi*DTOR) * Math.sin(DTOR*theta));
				Vertices[n].z = (float) (radius * Math.cos(phi*DTOR));
				n++;
				Vertices[n].x = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.cos(theta*DTOR));
				Vertices[n].y = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.sin(theta*DTOR));
				Vertices[n].z = (float) (radius * Math.cos((phi+dphi)*DTOR));
				n++;
				Vertices[n].x = (float) (radius * Math.sin(DTOR*phi) * Math.cos(DTOR*(theta+dtheta)));
				Vertices[n].y = (float) (radius * Math.sin(DTOR*phi) * Math.sin(DTOR*(theta+dtheta)));
				Vertices[n].z = (float) (radius * Math.cos(DTOR*phi));
				n++;
				if (phi > -90 && phi < 90) {
					Vertices[n].x = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.cos(DTOR*(theta+dtheta)));
					Vertices[n].y = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.sin(DTOR*(theta+dtheta)));
					Vertices[n].z = (float) (radius * Math.cos((phi+dphi)*DTOR));
					n++;
				}
			}
		}
	}
	
	
	/**
	 * Structure for one vertex of the dome.
	 * @author Christophe
	 */
	private class DomeVertex{
		float x,y,z;
		float u,v;
		float r,g,b,a;
	}


	@Override
	protected void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		GL gl = gld.getGL();
		gl.glPushMatrix();
		gl.glPushAttrib(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		gl.glTranslatef(MapSize/2,0,MapSize/2);
		gl.glScalef(MapSize,MapSize,MapSize);
		
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		for(int i=0;i<Vertices.length;i++){
			gl.glVertex3f(Vertices[i].x,Vertices[i].y,Vertices[i].z);
		}
		gl.glEnd();
		
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
}
