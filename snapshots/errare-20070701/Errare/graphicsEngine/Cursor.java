package graphicsEngine;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLDrawable;

public class Cursor extends GraphicalRep{

	private int size = 10;
	private int h = -5;
	
	public Cursor(String filename) {
		super(filename, GraphicalRep.FORMAT_ERRARE);
	}

	@Override
	protected void draw(GLDrawable gld, float px, float py, float pz, float rx, float ry, float rz) {
		GL gl = gld.getGL();
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		if(prem){
			prem=false;
			doExpandTexture(gl);
		}
		gl.glRotatef(90,1,0,0);
		gl.glDepthMask(false);
		gl.glBegin(GL.GL_QUADS);
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
