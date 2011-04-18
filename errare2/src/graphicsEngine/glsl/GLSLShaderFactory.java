package graphicsEngine.glsl;

import java.io.IOException;

public class GLSLShaderFactory {

	public static LinkedShader loadGLSLShader(String vertexShaderSourcefilename, String fragmentShaderSourcefilename){
		
		VertexShader vs = new VertexShader(vertexShaderSourcefilename);
		FragmentShader fs = new FragmentShader(fragmentShaderSourcefilename);
		
		LinkedShader shader = new LinkedShader(vs,fs);
		return shader;
	}
}
