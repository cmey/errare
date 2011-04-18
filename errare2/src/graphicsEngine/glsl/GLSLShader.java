package graphicsEngine.glsl;

import graphicsEngine.Extensions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import graphicsEngine.GraphicsEngine;
import java.io.InputStreamReader;
import javax.media.opengl.GL;
import logger.Logger;
import main.ResourceLocator;

/**
 * This is the super-class of VertexShader and FragmentShader.
 * @author Christophe
 */
public abstract class GLSLShader {

	protected String filename;
	protected String sourceCode = "";
	protected int handle;
	
	
	public GLSLShader(String filename){
		this.filename = filename;
		BufferedReader brv;
		try {
			brv = new BufferedReader(new InputStreamReader(ResourceLocator.getRessourceAsStream(filename)));
		
		String line;
		while ((line=brv.readLine()) != null) {
		  sourceCode += line + "\n";
		}
		} catch (FileNotFoundException e) {
			System.err.println("File not found for the Shader Source File: "+filename);
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("An error occured while reading the Shader Source File: "+filename);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	protected abstract void create(GL gl);
	
	
	public void compileShader(GL gl) {
		gl.glShaderSourceARB(this.handle,1,new String[]{sourceCode},new int[]{sourceCode.length()},0);
		gl.glCompileShaderARB(this.handle);
		CheckShaderObjectInfoLog(gl, this.handle);
		int[] compilecheck = new int[1];
		gl.glGetObjectParameterivARB(this.handle,GL.GL_OBJECT_COMPILE_STATUS_ARB,compilecheck,0);
		if ( compilecheck[0] == GL.GL_FALSE ) {
			Logger.printERROR("A compilation error occured in the Shader Source File: "+filename);
			
		} else
			if(GraphicsEngine.DEBUG) Logger.printINFO("Shader Source File: "+filename+" Compiled Successfully");
	}
	
	

	// it's static for LinkedShader to call it easily (LinkedShader is not a sort of GLSLShader)
	public static void CheckShaderObjectInfoLog(GL gl, int ID) {
        int[] check = new int[1];
        gl.glGetObjectParameterivARB(ID,GL.GL_OBJECT_INFO_LOG_LENGTH_ARB,check,0);
        int logLength = check[0];
        if ( logLength <= 1 ) {
            if(GraphicsEngine.DEBUG) Logger.printINFO("Shader GL_Object_Info_Log is Clean");
            return;
        }
        byte[] compilecontent = new byte[logLength+1];
        gl.glGetInfoLogARB(ID,logLength,check,0,compilecontent,0);
        String infolog = new String(compilecontent);
        Logger.printINFO("\nGL_Object_Info_Log of Shader Object ID: " + ID);
        Logger.printINFO(infolog);
        Logger.printINFO("--- end of shader log ------------------");
    }

	
	
	public int getHandle(){
		return this.handle;
	}

	
	public void free(GL gl){

	}
	
	public static boolean CheckShaderExtensions(GL gl) {
		return Extensions.isGLSLSupported;
	}
	
}
