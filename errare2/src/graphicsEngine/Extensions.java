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

import com.sun.opengl.cg.CGcontext;
import com.sun.opengl.cg.CgGL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.media.opengl.GL;

import javax.swing.JOptionPane;
import logger.Logger;

/**
 * Finds the extensions of OpenGL supported by the graphics card.
 * @author Christophe
 */
public class Extensions {

    static String OpenGLVersion;
    static String vendor;
    static final int NVIDIA = 1;
    static final int ATI = 2;
    static int videopath;
    static String renderer;
    static String raw_extensions = "";
    static String[] extensions;
    //static private String atilogo = "medias/Logos/ATILogo.gif";
    //static private String nvlogo = "medias/Logos/NVLogo.gif";
    static int maxTexelUnits;
    static public boolean isVBOSupported;
    static public boolean isGLSLSupported;
    static public boolean isFBOSupported;
    static public boolean isPBufferSupported;
    static public boolean isCGSupported;
    static public boolean isShadowMappingSupported; // is hardware shadow-mapping supported ? (needs extensions GL_shadow and GL_depth_texture)
    static int[] maxTextureUnits = new int[1];
    static int[] maxTextureSize = new int[1];

    public static void initialize() {
        try {
            CGcontext cgContext = CgGL.cgCreateContext();
            isCGSupported = true;
        } catch (Error e) {
            isCGSupported = false;
            JOptionPane.showMessageDialog(null, "Errare can use the CG toolkit to produce better graphics. You should install the CG toolkit (even if your hardware is not NVIDIA).\nIt can be found here : http://developer.nvidia.com/object/cg_toolkit.html#downloads\nHowever, Errare will run without it.", "nvidia-cg-toolkit", JOptionPane.WARNING_MESSAGE);
            Logger.printINFO("You should install the CG toolkit from NVIDIA. It can be found here : http://developer.nvidia.com/object/cg_toolkit.html#downloads");
            //Logger.printErrorERROR(e);
        }
        //Logger.printINFO("CG : "+(isCGSupported?"supported":"unsupported"));
        if (!isCGSupported) {
            Logger.printWARNING("CG not supported");
        }
    }

    static public void readEXT(GL gl) {
        gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS, maxTextureUnits, 0);
        if (maxTextureUnits[0] < 2) {
            Logger.printWARNING("MULTITEXTURE NOT SUPPORTED !");
        }
        gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        try {
            OpenGLVersion = gl.glGetString(GL.GL_VERSION);
            vendor = gl.glGetString(GL.GL_VENDOR);
            renderer = gl.glGetString(GL.GL_RENDERER);
            Logger.printINFO("VENDOR : " + vendor + "  RENDERER : " + renderer + "  OPENGL VERSION : " + OpenGLVersion);
            if (vendor.contains("ATI")) {
                videopath = ATI;
            } else if (vendor.contains("NVIDIA")) {
                videopath = NVIDIA;
            }
            raw_extensions = gl.glGetString(GL.GL_EXTENSIONS);
        } catch (Exception e) {
        //TODO: some systems do not support calls to glGetString (happened under Linux & NVidia)
        }
        extensions = raw_extensions.split(" ");
        
        isVBOSupported = isSupported("GL_ARB_vertex_buffer_object");
        //Logger.printINFO("VBO : "+(isVBOSupported?"supported":"unsupported"));
        if (!isVBOSupported) {
            Logger.printWARNING("VBO not supported");
        }
        isGLSLSupported = OpenGLVersion.compareTo("2.0.0") > 0 || (isSupported("GL_ARB_vertex_shader") && isSupported("GL_ARB_fragment_shader"));
        //Logger.printINFO("GLSL : "+(isGLSLSupported?"supported":"unsupported"));
        if (!isGLSLSupported) {
            Logger.printWARNING("GLSL not supported");
        }
        isFBOSupported = isSupported("GL_EXT_framebuffer_object");
        // TODO: peut etre qu'il faut toujours l'extension (geforce 5200 1.5.1 et pas la fonction)
        //Logger.printINFO("FBO : "+(isGLSLSupported?"supported":"unsupported"));
        if (!isFBOSupported) {
            Logger.printWARNING("FBO not supported");
        }
        isPBufferSupported = OpenGLVersion.compareTo("1.5.0") > 0 || isSupported("GL_WGL_ARB_pbuffer");
        //Logger.printINFO("Pbuffer : "+(isPBufferSupported?"supported":"unsupported"));
        if (!isPBufferSupported) {
            Logger.printWARNING("Pbuffer not supported");
        }
        boolean s1 = Extensions.isSupported("GL_ARB_shadow");
        boolean s2 = Extensions.isSupported("GL_ARB_depth_texture");
        boolean s3 = Extensions.isPBufferSupported;
        isShadowMappingSupported = false; //s1 && s2 && s3;
        if (!isShadowMappingSupported) {
            Logger.printWARNING("ShadowMapping not supported");
        }
    }

    static public void assertPresence(String s) {
        boolean isAvailable = isSupported(s);
        if (!isAvailable) {
            Logger.printFATAL("Your video card doesn't support " + s + " which is required!");
            System.exit(-1);
        }
    }

    static public boolean isSupported(String s) {
        boolean isAvailable = false;
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].equalsIgnoreCase(s)) {
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }

    static public void print_info() {
        Logger.printINFO("OpenGL version: " + OpenGLVersion);
        Logger.printINFO("Vendor: " + vendor);
        Logger.printINFO("Card: " + renderer);
        Logger.printINFO("Extensions: ");
        for (int i = 0; i < extensions.length; i++) {
            Logger.printINFO(extensions[i]);
        }
    //Logger.printINFO("\nJOGL version: "+Version.getVersion());
    }

    static public boolean write_info() {
        boolean alright = true;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("logfile.txt"));
            bw.write("\nOpenGL version: " + OpenGLVersion);
            bw.write("\nVendor: " + vendor);
            bw.write("\nCard: " + renderer);
            bw.write("\nExtensions: ");
            for (int i = 0; i < extensions.length; i++) {
                bw.write("\n" + extensions[i]);
            }
            //	bw.write("\nJOGL version: "+Version.getVersion());
            bw.close();
        } catch (IOException e) {
            alright = false;
        }

        return alright;
    }
    /*
    static public void drawPathLogo(GL gl){
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
