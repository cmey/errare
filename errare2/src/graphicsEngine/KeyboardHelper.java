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

import userInputEngine.UserInputController;

/**
 * Handles every keyboard user input that is related to the 3D graphics engine.
 * @author cyberchrist
 */
public class KeyboardHelper {

    protected GraphicsEngine ge;	// the caller
    public boolean vfculling = true; 	// view frustum culling
    public boolean wire_display = false;// models as wire
    public static boolean terrain = true;// display the terrain
    public boolean sky = true;			// display the sky
    public boolean fog = false;			// display the fog
    public boolean bloom = true && Extensions.isCGSupported && Extensions.isFBOSupported;	// display the glow effect
    public boolean shadows = false;		// display the shadows
    public boolean reflection = true;	// display the reflection in water
    public boolean far_view = true;		// view frustum really big
    public boolean shadowmap = false;	// display the texture containing the shadow map (point of view of the sun)
    public boolean draw_oculled = false;// display the bounding boxes of occlusion culled objects
    public boolean octree = false;		// display (TODO:full?) octree
    public boolean tremble = false;		// move the viewport a bit in a fast way with random coords
    public boolean fullscreen = false;	// switch to fullscreen
    public boolean blood_test = false;	// try to display bugged blood on models
    public boolean settingOccluder = false;// allows to construct an occluder with the mouse
    public boolean normals = false;		// display normals (per vertex)
    public static boolean VBOs = Extensions.isVBOSupported;	// use vertex_buffer_object
    public static boolean watershader = true && Extensions.isGLSLSupported;	// use the shader for water

    /**
     * Constructor.
     * @param ge the calling GraphicsEngine
     */
    public KeyboardHelper(GraphicsEngine ge) {
        this.ge = ge;
        UserInputController uic = ge.getMain()==null?null:ge.getMain().getUserInputController();
        if(uic==null) uic = ge.uic;
        this.registerKeys(uic);
    }

    /**
     * Registers in the UserInputController the actions that are handled here.
     * Note: refer to the class variables of the same name for a description of each action.
     */
    protected void registerKeys(UserInputController uic) {
        uic.register(ge, "exit");
        uic.register(ge, "cinema");
        uic.register(ge, "fog");
        uic.register(ge, "bloom");
        uic.register(ge, "wire_display");
        uic.register(ge, "rotate_start");
        uic.register(ge, "rotate_view");
        uic.register(ge, "zoom_in");
        uic.register(ge, "zoom_out");
        uic.register(ge, "left");
        uic.register(ge, "right");
        uic.register(ge, "backward");
        uic.register(ge, "forward");
        uic.register(ge, "normals");
        uic.register(ge, "VBOs");
        uic.register(ge, "FBOs");
        uic.register(ge, "watershader");
        uic.register(ge, "octree");
        uic.register(ge, "attack");

        ge.glComp.addMouseListener(uic); // needed to always have the last position of the mouse in order to handle a drag without "jumps"
        ge.glComp.addMouseMotionListener(uic);
        ge.glComp.addMouseWheelListener(uic);
        ge.glComp.addKeyListener(uic);
    }

    /**
     * User input (keyboard) arrival.
     * @return false if we consummed the event
     */
    public boolean invokeKeyEvent(String action) {
        if (action.equals("vfculling")) {
            vfculling = !vfculling;
        } else if (action.equals("wire_display")) {
            wire_display = !wire_display;
        } else if (action.equals("VBOs")) {
            VBOs = !VBOs;
            if (!Extensions.isVBOSupported) {
                VBOs = false;
            }
        } else if (action.equals("FBOs")) {
            Extensions.isFBOSupported = !Extensions.isFBOSupported;
        } else if (action.equals("watershader")) {
            watershader = !watershader;
            if (!Extensions.isGLSLSupported) {
                watershader = false;
            }
        } else if (action.equals("zoom_in")) {
            if (ge.world.cam instanceof FollowCamera) {
                ((FollowCamera) ge.world.cam).zoomin();
            }
        } else if (action.equals("zoom_out")) {
            if (ge.world.cam instanceof FollowCamera) {
                ((FollowCamera) ge.world.cam).zoomout();
            }
        } else if (action.equals("terrain")) {
            terrain = !terrain;
        } else if (action.equals("sky")) {
            sky = !sky;
        } else if (action.equals("fog")) {
            fog = !fog;
        } else if (action.equals("bloom")) {
            bloom = !bloom;
            if (!Extensions.isCGSupported || !Extensions.isFBOSupported) {
                bloom = false;
            }
        } else if (action.equals("shadows")) {
            shadows = !shadows;
        } else if (action.equals("reflection")) {
            reflection = !reflection;
        } else if (action.equals("far_view")) {
            far_view = !far_view;
        } else if (action.equals("shadowmap")) {
            shadowmap = !shadowmap;
        } else if (action.equals("cinema")) {
            ge.getCinema().switchState();
        } else if (action.equals("draw_oculled")) {
            draw_oculled = !draw_oculled;
        } else if (action.equals("octree")) {
            octree = !octree;
        } else if (action.equals("tremble")) {
            tremble = !tremble;
        } else if (action.equals("fullscreen")) {
            fullscreen = !fullscreen;
            if (fullscreen) {
                ge.goFullScreen();
            } else {
                ge.goWindowed();
            }
        } else if (action.equals("blood_test")) {
            blood_test = !blood_test;

        } else if (action.equals("forward")) {
            if (ge.world.cam instanceof EyeCamera) {
                ((EyeCamera) ge.world.cam).forward();
            }
        } else if (action.equals("backward")) {
            if (ge.world.cam instanceof EyeCamera) {
                ((EyeCamera) ge.world.cam).backward();
            }
        } else if (action.equals("settingOccluder")) {
            settingOccluder = !settingOccluder;
        } else if (action.equals("normals")) {
            normals = !normals;
        } else if (action.equals("exit")) {
            System.exit(0);
        } else if (action.equals("attack")) {

        }

        return true; // event not consummed
    }
}
