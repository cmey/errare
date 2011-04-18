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

package graphicsEngine.glsl;


import graphicsEngine.GraphicsEngine;

import javax.media.opengl.GL;
import logger.Logger;

public class LinkedShader {

	private VertexShader vs;
	private FragmentShader fs;
	private int programHandle;
	
	public LinkedShader(VertexShader v, FragmentShader f){
		this.vs = v;
		this.fs = f;
	}
	
	public void create(GL gl){
		this.vs.create(gl);
		this.fs.create(gl);
		this.programHandle = gl.glCreateProgramObjectARB();
	}
	
	public void compile(GL gl) {
		vs.compileShader(gl);
		fs.compileShader(gl);
        gl.glAttachObjectARB(programHandle, vs.handle);
        gl.glAttachObjectARB(programHandle, fs.handle);
        gl.glLinkProgramARB(programHandle);
        //gl.glValidateProgram(programHandle);
        GLSLShader.CheckShaderObjectInfoLog(gl, programHandle);
        int[] linkcheck = new int[1];
        gl.glGetObjectParameterivARB(programHandle,GL.GL_OBJECT_LINK_STATUS_ARB,linkcheck,0);
        if ( linkcheck[0] == GL.GL_FALSE ) {
            Logger.printFATAL("A Linking error occured in the Shader program ID: "+programHandle);
            System.exit(-1);
        } else
            if(GraphicsEngine.DEBUG) Logger.printINFO("Shader Program ID:"+programHandle+" Linked Successfully");
    }
	
	
	public void bind(GL gl){
		gl.glUseProgramObjectARB(programHandle);
	}
	
	public void unbind(GL gl){
		gl.glUseProgramObjectARB(0);
	}
	
	public void free(GL gl){
		
	}
	
	public int getHandle(){
		return this.programHandle;
	}
	
	/*  Note that the variables must actually be used
    in the shader for this to work.  Also, you have to call this after
    using the shader.
	 */
	public int AllocateUniform(GL gl, String name) {
		int uloc = gl.glGetUniformLocationARB(programHandle, name);
		if ( uloc == -1 ){
			throw new IllegalArgumentException("The uniform \"" + name + "\" does not exist in the Shader Program.");
		} else
			if(GraphicsEngine.DEBUG) Logger.printINFO("Uniform " + name + " was found in the Shader Program and allocated.");
		return uloc;
	}

}
