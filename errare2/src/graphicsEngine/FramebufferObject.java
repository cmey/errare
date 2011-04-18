package graphicsEngine;

import javax.media.opengl.GL;

import logger.Logger;

public class FramebufferObject {

	/**
	 * Check if the binded frame buffer object (OpenGL related) is ok.
	 * @param gl OpenGL context
	 * @return true only when the binded framebufferobject has a complete ok status
	 */
	static protected boolean CheckFramebufferStatus(GL gl){
		// Check framebuffer completeness at the end of initialization.
		int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
		switch(status) {
		case GL.GL_FRAMEBUFFER_COMPLETE_EXT:
			Logger.printDEBUG("FBO created with status ok");
			return true;
		case GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
			Logger.printERROR("FBO defect with UNSUPPORTED FORMAT status");
			return false;
		case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
			Logger.printERROR("FBO defect with INCOMPLETE MISSING ATTACHMENT status");
			return false;
		case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
			Logger.printERROR("FBO defect with INCOMPLETE DIMENSIONS status");
			return false;
		case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
			Logger.printERROR("FBO defect with INCOMPLETE FORMATS status");
			return false;
		case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
			Logger.printERROR("FBO defect with INCOMPLETE DRAW BUFFER status");
			return false;
		case GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
			Logger.printERROR("FBO defect with INCOMPLETE READ BUFFER status");
			return false;
		default:
			Logger.printERROR("FBO defect with UNKNOWN status "+status);
			return false;
		}
	}
}
