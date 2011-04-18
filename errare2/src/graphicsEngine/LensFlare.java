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
import javax.media.opengl.glu.GLU;

import geom.Point;
import geom.Vector;

public class LensFlare extends Graphics{

	OneFlare bigGlow;
	OneFlare streaks;
	OneFlare smallGlow;
	OneFlare halo;
	Point sun_2d_coords;
	Vector vector;
	Point screen_center;
	Vector oposite;
	
	Point CamPos;
	Point SunPos;
	Vector vLightSourceToCamera;
	Vector m_DirectionVector;
	float length;
	Vector ptIntersect;
	Vector vLightSourceToIntersect;
	public float glowFactor;
	
	public LensFlare(){
		try {
			bigGlow = new OneFlare("data/images/sky/big_glow.jpg");
			streaks = new OneFlare("data/images/sky/streaks.jpg");
			smallGlow = new OneFlare("data/images/sky/glow.jpg");
			halo = new OneFlare("data/images/sky/halo.jpg");
		} catch (IOException e) {
			throw new RuntimeException("Error while loading Lens Flare textures: "+e);
		}
		
		screen_center = new Point();
		oposite = new Vector();
	}
	
	public void draw(GL gl, GLU glu, Frustum fru){
		gl.glPushAttrib(GL.GL_ENABLE_BIT | GL.GL_CURRENT_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glDepthMask(false);
		
		if( (sun_2d_coords = fru.IsOccluded_Coords(SunPos,gl,glu)) != null ) // Check If The Center Of The Flare Is Occluded
		{
			screen_center.x = ge.winwidth/2;
			screen_center.y = ge.winheight/2;
			
			vector = new Vector(sun_2d_coords,screen_center);
			vector.normalize();
			oposite = Vector.add(vector,new Vector(sun_2d_coords));
			
			while(true){
				oposite.add(vector);
				if(oposite.x >= ge.winwidth || oposite.x <= 0){
					break;
				}
				if(oposite.y >= ge.winheight || oposite.y <= 0){
					break;
				}
			}
			
			Point opp = new Point(oposite.x, oposite.y, oposite.z);
			length = new Vector(sun_2d_coords,opp).getMagnitude();
			Vector v_sun_2d = new Vector(sun_2d_coords);
			
			// Render The Large Hazy Glow
			bigGlow.render(gl,0.60f, 0.60f, 0.8f, 1.0f, v_sun_2d, 80);
			// Render The Streaks
			streaks.render(gl,0.60f, 0.60f, 0.8f, 1.0f, v_sun_2d, 160);
			// Render The Small Glow
			smallGlow.render(gl,0.8f, 0.8f, 1.0f, 0.5f, v_sun_2d, 150);
			// Lets Compute A Point That Is 20% Away From Light Source
			
			
			
			
			Vector pt = new Vector(vector);
			pt.mult(length * 0.1f);
			pt.add(v_sun_2d);				
										
			smallGlow.render(gl,0.9f, 0.6f, 0.4f, 0.5f, pt, 30);		// Render The Small Glow

			pt = new Vector(vector);
			pt.mult(length * 0.15f);
			pt.add(v_sun_2d);	// Lets Compute A Point That Is 30%
								// Away From The Light Source In The
								// Direction Of The Intersection Point
		
			
			halo.render(gl,0.8f, 0.5f, 0.6f, 0.5f, pt, 80);		// Render The Halo
		
			pt = new Vector(vector);
			pt.mult(length * 0.175f);
			pt.add(v_sun_2d);		// Lets Compute A Point That Is 35%
								// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.9f, 0.2f, 0.1f, 0.5f, pt, 40);		// Render The Halo

			pt = new Vector(vector);
			pt.mult(length * 0.285f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.7f, 0.4f, 0.5f, pt, 80);		// Render The Halo
		
			pt = new Vector(vector);
			pt.mult(length * 0.2755f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.9f, 0.9f, 0.2f, 0.5f, pt, 40);		// Render The Small Glow

			pt = new Vector(vector);
			pt.mult(length * 0.4775f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.93f, 0.82f, 0.73f, 0.5f, pt, 50);	// Render The Small Glow
		
			pt = new Vector(vector);
			pt.mult(length * 0.49f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.6f, 0.5f, 0.5f, pt, 70);		// Render The Halo

			pt = new Vector(vector);
			pt.mult(length * 0.65f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.7f, 0.8f, 0.3f, 0.5f, pt, 90);		// Render The Small Glow
		
			pt = new Vector(vector);
			pt.mult(length * 0.63f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.4f, 0.3f, 0.2f, 0.5f, pt, 70);		// Render The Small Glow

			pt = new Vector(vector);
			pt.mult(length * 0.8f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.5f, 0.5f, 0.5f, pt, 70);		// Render The Halo
		
			pt = new Vector(vector);
			pt.mult(length * 0.78f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.8f, 0.5f, 0.1f, 0.5f, pt, 30f);		// Render The Small Glow

			pt = new Vector(vector);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.5f, 0.5f, 0.7f, 0.5f, pt, 80f);		// Render The Halo
		
			pt = new Vector(vector);
			pt.mult(length * 0.975f);
			pt.add(v_sun_2d);						// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.4f, 0.1f, 0.9f, 0.5f, pt, 100);		// Render The Small Glow
			
			increaseGlowFactor();
		}else{
			decreaseGlowFactor();
		}

		gl.glPopAttrib();		
	}
	
	public void setVectors(float camX, float camY, float camZ, float lookX, float lookY, float lookZ, float sunX, float sunY, float sunZ){
		CamPos = new Point (camX, camY, camZ);
		
		SunPos = new Point (sunX, sunY, sunZ);
		// Compute The Vector That Points From The Light Source To The Camera
		vLightSourceToCamera = new Vector(SunPos, CamPos);
		
		length = vLightSourceToCamera.getMagnitude();
		m_DirectionVector = new Vector(lookX-camX,lookY-camY,lookZ-camZ);
		m_DirectionVector.normalize();
		m_DirectionVector.mult(length);
		
		ptIntersect = m_DirectionVector;
		ptIntersect.add(new Vector(CamPos));
		
		// Lets Compute The Vector That Points To The Intersect
		vLightSourceToIntersect = new Vector(SunPos,new Point(ptIntersect.x, ptIntersect.y, ptIntersect.z));
		// Point From The Light Source
		length = vLightSourceToIntersect.getMagnitude();
		vLightSourceToIntersect.normalize();
	}
	
	
	public void decreaseGlowFactor(){
		glowFactor-=0.01f;
		if(glowFactor<=0.2f) glowFactor=0.2f;
	}
	
	
	public void increaseGlowFactor(){
		glowFactor+=0.05f;
		if(glowFactor>=1.0f) glowFactor=1.0f;
	}
	
	
	private class OneFlare extends Graphics{
		
		Texture tex;
		
		public OneFlare(String filename) throws IOException{
			tex = TextureFactory.loadTexture(filename);
		}
		
		public void render(GL gl,float r, float g, float b, float a, Vector p, float scale){
			
			if(prem) {
				tex.doExpandTexture(gl);
				prem=false;
			}

			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glOrtho(0,ge.winwidth,0,ge.winheight,-1,1);
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			
			gl.glTranslatef(p.x,p.y,0);
			
			Billboard(gl);
			
			gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);				// Bind To The Big Glow Texture
			gl.glColor4f(r, g, b, a);							// Set The Color Since The Texture Is A Gray Scale
			gl.glBegin(GL.GL_TRIANGLE_STRIP);						// Draw The Big Glow On A Triangle Strip
				gl.glTexCoord2f(0.0f, 0.0f);					
				gl.glVertex2f(-scale, -scale);
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f(-scale, scale);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f(scale, -scale);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f(scale, scale);
			gl.glEnd();										
			
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_MODELVIEW);
			
		}

		@Override
		protected void draw(GLAutoDrawable gld) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void drawByTriangles(GLAutoDrawable gld) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void drawGeometryOnly(GLAutoDrawable gld) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public String toString(){
		return "LensFlare";
	}

	@Override
	protected void draw(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawByTriangles(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}
}
