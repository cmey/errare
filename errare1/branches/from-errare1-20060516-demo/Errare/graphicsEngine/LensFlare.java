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
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

public class LensFlare{

	OneFlare bigGlow;
	OneFlare streaks;
	OneFlare smallGlow;
	OneFlare halo;
	Point3D sun_2d_coords;
	Point3D vector;
	Point3D screen_center;
	Point3D oposite;
	
	Point3D vCamPos;
	Point3D vSunPos;
	Point3D vLightSourceToCamera;
	Point3D m_DirectionVector;
	float length;
	Point3D ptIntersect;
	Point3D vLightSourceToIntersect;
	
	public LensFlare(){
		bigGlow = new OneFlare("data/images/sky/big_glow.jpg");
		streaks = new OneFlare("data/images/sky/streaks.jpg");
		smallGlow = new OneFlare("data/images/sky/glow.jpg");
		halo = new OneFlare("data/images/sky/halo.jpg");
		screen_center = new Point3D();
		oposite = new Point3D();
	}
	
	public void draw(GL2 gl, GLU glu, Frustum fru){
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glDepthMask(false);
		
		if ((sun_2d_coords = fru.IsOccluded_Coords(vSunPos,gl,glu)) != null)				// Check If The Center Of The Flare Is Occluded
		{
			screen_center.X = GraphicsEngine.window_width/2;
			screen_center.Y = GraphicsEngine.window_height/2;
			
			vector = screen_center.substract(sun_2d_coords);
			vector.normalize();
			oposite = vector.add_rnew(sun_2d_coords);
			
			while(true){
				oposite.add(vector);
				if(oposite.X >= GraphicsEngine.window_width || oposite.X <= 0){
					break;
				}
				if(oposite.Y >= GraphicsEngine.window_height || oposite.Y <= 0){
					break;
				}
			}
			
			length = oposite.substract(sun_2d_coords).Magnitude();
			
			
			// Render The Large Hazy Glow
			bigGlow.render(gl,0.60f, 0.60f, 0.8f, 1.0f, sun_2d_coords, 80);
			// Render The Streaks
			streaks.render(gl,0.60f, 0.60f, 0.8f, 1.0f, sun_2d_coords, 160);
			// Render The Small Glow
			smallGlow.render(gl,0.8f, 0.8f, 1.0f, 0.5f, sun_2d_coords, 150);
			// Lets Compute A Point That Is 20% Away From Light Source
			
			Point3D pt = vector.mult(length * 0.1f);
			pt.add(sun_2d_coords);					
										
			smallGlow.render(gl,0.9f, 0.6f, 0.4f, 0.5f, pt, 30);		// Render The Small Glow

			pt = vector.mult(length * 0.15f);	// Lets Compute A Point That Is 30%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.8f, 0.5f, 0.6f, 0.5f, pt, 80);		// Render The Halo
		
			pt = vector.mult(length * 0.175f);	// Lets Compute A Point That Is 35%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.9f, 0.2f, 0.1f, 0.5f, pt, 40);		// Render The Halo

			pt = vector.mult(length * 0.285f);	// Lets Compute A Point That Is 57%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.7f, 0.4f, 0.5f, pt, 80);		// Render The Halo
		
			pt = vector.mult(length * 0.2755f);	// Lets Compute A Point That Is 55.1%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.9f, 0.9f, 0.2f, 0.5f, pt, 40);		// Render The Small Glow

			pt = vector.mult(length * 0.4775f);	// Lets Compute A Point That Is 95.5%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.93f, 0.82f, 0.73f, 0.5f, pt, 50);	// Render The Small Glow
		
			pt = vector.mult(length * 0.49f);	// Lets Compute A Point That Is 98%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.6f, 0.5f, 0.5f, pt, 70);		// Render The Halo

			pt = vector.mult(length * 0.65f);	// Lets Compute A Point That Is 130%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.7f, 0.8f, 0.3f, 0.5f, pt, 90);		// Render The Small Glow
		
			pt = vector.mult(length * 0.63f);	// Lets Compute A Point That Is 126%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.4f, 0.3f, 0.2f, 0.5f, pt, 70);		// Render The Small Glow

			pt = vector.mult(length * 0.8f);		// Lets Compute A Point That Is 160%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.7f, 0.5f, 0.5f, 0.5f, pt, 70);		// Render The Halo
		
			pt = vector.mult(length * 0.78f);	// Lets Compute A Point That Is 156.5%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.8f, 0.5f, 0.1f, 0.5f, pt, 30f);		// Render The Small Glow

			pt = vector.mult(length * 1f);		// Lets Compute A Point That Is 200%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			halo.render(gl,0.5f, 0.5f, 0.7f, 0.5f, pt, 80f);		// Render The Halo
		
			pt = vector.mult(length * 0.975f);	// Lets Compute A Point That Is 195%
			pt.add(sun_2d_coords);					// Away From The Light Source In The
										// Direction Of The Intersection Point
		
			smallGlow.render(gl,0.4f, 0.1f, 0.9f, 0.5f, pt, 100);		// Render The Small Glow
			
		}

		gl.glPopAttrib();		
	}
	
	public void setVectors(float camX, float camY, float camZ, float lookX, float lookY, float lookZ, float sunX, float sunY, float sunZ){
		vCamPos = new Point3D (camX, camY, camZ);
		
		vSunPos = new Point3D (sunX, sunY, sunZ);
		// Compute The Vector That Points From The Light Source To The Camera
		vLightSourceToCamera = vCamPos.substract(vSunPos);
		
		length = vLightSourceToCamera.Magnitude();
		m_DirectionVector = new Point3D(lookX-camX,lookY-camY,lookZ-camZ);
		m_DirectionVector.normalize();
		
		ptIntersect = m_DirectionVector.mult(length);
		ptIntersect.add(vCamPos);
		
		// Lets Compute The Vector That Points To The Intersect
		vLightSourceToIntersect = ptIntersect.substract(vSunPos);
		// Point From The Light Source
		length = vLightSourceToIntersect.Magnitude();
		vLightSourceToIntersect.normalize();
	}
	
	
	private class OneFlare extends GraphicalRep{

		public OneFlare(String filename){
			super(filename, GraphicalRep.FORMAT_LENSFLARE);
			
		}
		
		public void draw (GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz){
			
		}
		
		public void render(GL2 gl,float r, float g, float b, float a, Point3D p, float scale){
			
			if(prem) {
				doExpandTexture(gl);
				prem=false;
			}

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			
			gl.glTranslatef(p.X,p.Y,0);
			
			Billboard(gl);
			
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);				// Bind To The Big Glow Texture
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
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			
		}
	}
}
