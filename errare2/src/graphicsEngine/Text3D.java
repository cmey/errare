package graphicsEngine;

import javax.media.opengl.GL;

public class Text3D extends Text2D {

	
	public void glPrint(GL gl, String s){
		glPrintc(gl,1,0,s,1,1,1);
	}
	
    @Override
	public void glPrintc(GL gl,int x, int y, String s, float r, float b, float g){
		gl.glPushAttrib(GL.GL_ENABLE_BIT | GL.GL_CURRENT_BIT | GL.GL_COLOR_BUFFER_BIT); // current bit for color saving
		gl.glColor4f(r,b,g,1);
		gl.glPushClientAttrib(GL.GL_CLIENT_VERTEX_ARRAY_BIT);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_BLEND);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY );
		gl.glBindTexture(GL.GL_TEXTURE_2D, font.internal_index[0]);
		gl.glPushMatrix();
		Billboard(gl);
		gl.glTranslatef(-10, 2.5f, 1);
		gl.glScalef(0.1f, 0.1f, 0.1f);
		gl.glListBase(list_chars-32);
		char[] array = s.toCharArray();
		for(int i=0; i<array.length; ++i) {
                    assert(gl.glIsList(list_chars+array[i]-32));
                    gl.glCallList(list_chars+array[i]-32);
                }
		gl.glPopMatrix();
		gl.glPopClientAttrib();
		gl.glPopAttrib();
	}
	
    @Override
	public String toString(){
		return "Text3D";
	}
}
