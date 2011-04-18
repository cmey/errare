package graphicsEngine;

import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import logger.Logger;


public class Cursor extends Graphics{

	private int size = 10;
	private int h = -5;
	private Texture tex;
	public static final String STD_CURSOR = "data/images/other/cursor.png";
	
	public Cursor(String filename) {
		try {
			tex = TextureFactory.loadTexture(STD_CURSOR);
		} catch (IOException e) {
			Logger.printERROR("STD_CURSOR "+STD_CURSOR+ ": file not found !");
			System.exit(0);
		};
	}

	protected void drawByTriangles(GLAutoDrawable gld) {
		
	}
	@Override
	protected void draw(GLAutoDrawable gld) {
		GL gl = gld.getGL();
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		if(prem){
			prem=false;
			tex.doExpandTexture(gl);
		}

		gl.glBindTexture(GL.GL_TEXTURE_2D, tex.internal_index[0]);
		
		gl.glDepthMask(false);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3f(-size,h,-size);gl.glTexCoord2i(0,0);
		gl.glVertex3f(-size,h,size);gl.glTexCoord2i(0,1);
		gl.glVertex3f(size,h,size);gl.glTexCoord2i(1,1);
		gl.glVertex3f(size,h,-size);gl.glTexCoord2i(1,0);
		
		gl.glEnd();
		
		gl.glPopAttrib();
	}
	
	public String toString(){
		return "Cursor";
	}

	@Override
	protected void drawGeometryOnly(GLAutoDrawable gld) {
		// TODO Auto-generated method stub
		
	}

}
