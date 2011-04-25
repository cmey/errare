package graphicsEngine;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

public class Cursor extends GraphicalRep{

	private int size = 10;
	private int h = -5;
	
	public Cursor(String filename) {
		super(filename, GraphicalRep.FORMAT_ERRARE);
	}

	@Override
	protected void draw(GLAutoDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		GL2 gl = gld.getGL().getGL2();
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		if(prem){
			prem=false;
			doExpandTexture(gl);
		}
		gl.glRotatef(90,1,0,0);
		gl.glDepthMask(false);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor4f(1,1,1,0);
		gl.glVertex3f(-size,-size,h);gl.glTexCoord2i(0,0);
		gl.glVertex3f(size,-size,h);gl.glTexCoord2i(1,0);
		gl.glVertex3f(size,size,h);gl.glTexCoord2i(1,1);
		gl.glVertex3f(-size,size,h);gl.glTexCoord2i(0,1);
		gl.glColor4f(1,1,1,1);
		gl.glVertex3f(-size,-size,h);gl.glTexCoord2i(0,0);
		gl.glVertex3f(size,-size,h);gl.glTexCoord2i(1,0);
		gl.glVertex3f(size,size,h);gl.glTexCoord2i(1,1);
		gl.glVertex3f(-size,size,h);gl.glTexCoord2i(0,1);
		gl.glEnd();
		
		gl.glPopMatrix();
		gl.glPopAttrib();
	}

}
