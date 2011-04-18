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


public class Skydome extends Graphics{
	
	transient private int theta,phi;
	transient private int dtheta = 15/3;
	transient private int dphi = 15/3;
	transient public static float DTOR = (float) (Math.PI/180f);
	transient private int numVertices = (int)((360/dtheta)*(45/dphi)*4);
	transient private DomeVertex[] vertices;
	transient private float radius = 10000;
	private Texture tex;
	public static final String SKY_CLEAR = "data/images/skydome/sky2.jpg";
	transient private float dv = (float)1/(float)((90.0/dphi));
	transient private float du = (float)1/(float)((360.0/dtheta));
	
	public Skydome(String filename){
		super();
		try {
			tex = TextureFactory.loadTexture(filename);
		} catch (IOException e) {
			Logger.printERROR("SKY "+SKY_CLEAR+ ": file not found !");
		}
		
		vertices = new DomeVertex[numVertices];
		for(int i = 0; i<vertices.length;i++){
			vertices[i] = new DomeVertex();
		}
		create_vertex_array();
	}
	
	
	private void create_vertex_array(){
		float dy=radius/3;
		
		int n = 0;
		float v=0;
		for (phi=0; phi <= 45 - dphi; phi += (int)dphi) {
			float u=0;
			for (theta=0; theta <= 360 - dtheta; theta += (int)dtheta) {
				vertices[n].x = (float) (radius * Math.sin(phi*DTOR) * Math.cos(DTOR*theta));
				vertices[n].z = (float) (radius * Math.sin(phi*DTOR) * Math.sin(DTOR*theta));
				vertices[n].y = (float) (radius * Math.cos(phi*DTOR) - radius/2 ) -  dy;
				
				vertices[n].v = (float) (1-v);
				vertices[n].u = (float) u;
				n++;
				vertices[n].x = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.cos(theta*DTOR));
				vertices[n].z = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.sin(theta*DTOR));
				vertices[n].y = (float) (radius * Math.cos((phi+dphi)*DTOR) - radius/2 ) - dy;
				
				vertices[n].v = (float) (1-(v+dv));
				vertices[n].u = (float) u;
				n++;
				vertices[n].x = (float) (radius * Math.sin(DTOR*phi) * Math.cos(DTOR*(theta+dtheta)));
				vertices[n].z = (float) (radius * Math.sin(DTOR*phi) * Math.sin(DTOR*(theta+dtheta)));
				vertices[n].y = (float) (radius * Math.cos(DTOR*phi) - radius/2 ) - dy;
				
				vertices[n].v = (float) (1-v);
				vertices[n].u = (float) (u+du);
				n++;
				if (phi > -90 && phi < 90) {
					vertices[n].x = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.cos(DTOR*(theta+dtheta)));
					vertices[n].z = (float) (radius * Math.sin((phi+dphi)*DTOR) * Math.sin(DTOR*(theta+dtheta)));
					vertices[n].y = (float) (radius * Math.cos((phi+dphi)*DTOR) - radius/2 ) -dy;
					
					vertices[n].v = (float) (1-(v+dv));
					vertices[n].u = (float) (u+du);
					n++;
				}
				u+=du;
			}
			v+=dv;
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
	protected void draw(GLAutoDrawable gld) {
		GL gl = gld.getGL();
		
		if(prem) {
			tex.doExpandTexture(gl);
			prem=false;
		}
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		for(int i=0;i<vertices.length;i++){
			gl.glTexCoord2f(0.5f+vertices[i].u, vertices[i].v);
			gl.glVertex3f(vertices[i].x,vertices[i].y,vertices[i].z);
		}
		gl.glEnd();
		
	}
	
	public String toString(){
		return "Skydome";
	}
	
	public void init(GL gl){
		
	}


	@Override
	protected
	void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
	}


	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		GL gl = gld.getGL();
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		for(int i=0;i<vertices.length;i++){
			gl.glVertex3f(vertices[i].x,vertices[i].y,vertices[i].z);
		}
		gl.glEnd();
	}
}
