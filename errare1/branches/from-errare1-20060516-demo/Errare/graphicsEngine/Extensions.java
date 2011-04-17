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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.java.games.jogl.GL;
import net.java.games.jogl.Version;

public class Extensions {

	static String version;
	static String vendor;
	static String videopath;
	static String renderer;
	static String raw_extensions;
	static String[] extensions;
	static private String atilogo = "medias/Logos/ATILogo.gif";
	static private String nvlogo = "medias/Logos/NVLogo.gif";
	static int maxTexelUnits;
	
	public Extensions(){
		
	}
	
	static public void readEXT(GL gl){
		version = gl.glGetString(GL.GL_VERSION);
		vendor = gl.glGetString(GL.GL_VENDOR);
		if(vendor.contains("ATI"))
			videopath = "ATI";
		else if(vendor.contains("NVIDIA"))
			videopath = "NVIDIA";
		renderer = gl.glGetString(GL.GL_RENDERER);
		raw_extensions = gl.glGetString(GL.GL_EXTENSIONS);
		extensions = raw_extensions.split(" ");
	}
	
	static public void assertPresence(String s){
		boolean isAvailable = false;
		for(int i=0;i<extensions.length;i++)
			if(extensions[i].equals(s)){
				isAvailable = true;
				break;
			}
		if(!isAvailable){
			System.out.println("Your video card doesn't support "+s+" which is required!");
			System.exit(-1);
		}
	}
	
	static public boolean isSupported(String s){
		boolean isAvailable = false;
		for(int i=0;i<extensions.length;i++)
			if(extensions[i].equals(s)){
				isAvailable = true;
				break;
			}
		return isAvailable;
	}
	
	static public void print_info(){
		System.out.println("OpenGL version: "+version);
		System.out.println("Vendor: "+vendor);
		System.out.println("Card: "+renderer);
		System.out.println("Extensions: ");
		for(int i=0;i<extensions.length;i++)
			System.out.println(extensions[i]);
		System.out.println("\nJOGL version: "+Version.getVersion());
	}
	
	static public boolean write_info(){
		boolean alright=true;;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("logfile.txt"));
			bw.write("\nOpenGL version: "+version);
			bw.write("\nVendor: "+vendor);
			bw.write("\nCard: "+renderer);
			bw.write("\nExtensions: ");
			for(int i=0;i<extensions.length;i++)
				bw.write("\n"+extensions[i]);
			bw.write("\nJOGL version: "+Version.getVersion());
			bw.close();
		} catch (IOException e) {
			alright=false;
		}
		
		return alright;
	}
	
	/*
	static public void drawLogoPath(GL gl){
		gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture[0]);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0,GraphicsEngine.window_width,0,GraphicsEngine.window_height,-1,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glEnable(GL.GL_BLEND);
		gl.glTranslated(x,y,0);
		gl.glCallList(list_logopath);
		gl.glDisable(GL.GL_BLEND);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	*/
}
