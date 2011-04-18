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

import gameEngine.GameRep;
import genericEngine.Engine;
import genericReps.CharacterRep;
import genericReps.Rep;
import geom.AABox;
import geom.Point;
import geom.Sphere;
import geom.Triangle;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.TraceGL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;


import org.w3c.dom.Node;

import logger.Logger;
import main.ClientMain;
import physicsEngine.Octree;
import physicsEngine.PhysicalRep;
import userInputEngine.UserInputController;

/**
 * The class that manages the rendering.
 * @author cyberchrist
 */
public class GraphicsEngine implements Engine, Runnable {

    public static boolean DEBUG; // display usefull infos
    // rendering containers :
    protected GLComponent glComp; // the GL window context
    public int winwidth,  winheight; // the size of the window
    protected ClientMain main; // a link to all other engines
    protected GraphicsDevice device;	// the rendering device used and configured
    protected JFrame frame; // the JFrame in which we render the viewport

    // visualization volume :
    protected Frustum fru; // the volume paralellepipeda that we visualize
    public boolean frustum_just_changed;
    protected float view_distance = 100f; // distance to back plane
    protected int perspectiv = 60; // perspective angle
    protected Point camposition;
    protected Point camlookingat;
    
    // usual graphics objects :
    protected GLUquadric quadric;
    public World world; // a world containing a sun, a camera, a sky and the root of the scene
    protected Grass grass_auto;
    protected Border border_auto;
    public Text2D text2d;
    public Text3D text3d;
    protected Robot robot;
    protected Picking picking;
    Fog fog;
    //protected Cursor cursor;
    private WiiSword wiisword;
    
    // effects :
    Light scenelight;
    protected ShadowMapping shadowmapping;
    protected Bloom bloom;
    Reflection reflection;
    public Texture refraction;
    protected LensFlare lens;
    protected Cinema cinema;
    
    // input events helpers :
    protected UserInputController uic;
    protected MouseHelper mouseHelper;
    public static KeyboardHelper keyboardHelper;
    protected FPSHelper fpsHelper; // used ?	
    protected CharacterRep mainChar;
    protected long slept = 88888;
    protected static int triangles_drawn;
    protected float x,  y,  z,  sx,  sy,  sz,  sr;
    protected Set<Rep> li;
    protected List<Graphics> lid;
    public int fpscount;
    public int actual_fps;
    protected boolean setLeftBorderAction;
    protected boolean setRightBorderAction;
    public boolean spell_casting;
    protected float mcx;
    protected float mcz; //position of main gameCharacter
    protected float mcy;
    protected float rond_picking_size;
    protected boolean terrain_found = false;
    protected float aspect;
    boolean init;

    public GraphicsEngine(JFrame f, UserInputController userinputcontroller, boolean debug) {
        DEBUG = debug;
        frame = f;
        uic = userinputcontroller;
        graphicsEngineInit();
    }
    
    public GraphicsEngine(JFrame f, boolean debug){
        DEBUG = debug;
        frame = f;
        graphicsEngineInit();
    }

    public GraphicsEngine(ClientMain main) {
        this.main = main;
        frame = main.getJFrame();
        graphicsEngineInit();
    }

    /*
    public GraphicsEngine(EditorMain main){
    DEBUG = true;
    this.main = null;
    frame = main.getJFrame();
    graphicsEngineInit();
    registerKeys(main.getUserInputController());
    }*/
    protected void graphicsEngineInit() {
        if (DEBUG) {
            Logger.printINFO("Starting GraphicsEngine...");
        }
        Graphics.setEngine(this);

        glComp = new GLComponent(60, this);
        
        //octree2terrain = new Hashtable<Octree, Terrain>();
        text2d = new Text2D();
        text3d = new Text3D();
        //border_auto = new Border("data/images/sky/border.png");
        cinema = new Cinema();
        lens = new LensFlare();
        //cursor = new Cursor("data/images/other/cursor.png");
        shadowmapping = new ShadowMapping();
        picking = new Picking(world);
        scenelight = new Light();
        fog = new Fog(this);
        reflection = new Reflection(this);
        wiisword = new WiiSword();
        
        try {
            robot = new Robot();
        } catch (Exception e) {
            Logger.printExceptionERROR(e);
        }
    }

    protected void setupHelpers() {
        fpsHelper = new FPSHelper(this);
        mouseHelper = new MouseHelper(this);
        keyboardHelper = new KeyboardHelper(this);
    }

    public void init(GLAutoDrawable drawable) {
        Logger.printINFO("init.");
        final GL gl = drawable.getGL();
        // have OpenGL notice us when errors occur
	drawable.setGL(new DebugGL(gl));
//	drawable.setGL(new TraceGL(gl, System.err));
        GLU glu = new GLU();
        
        Extensions.readEXT(gl);
        
        setupHelpers();

        try {
            Logger.printINFO("Available accelerated memory : " + glComp.getGraphicsConfiguration().getDevice().getAvailableAcceleratedMemory());
        } catch (Exception e) {
            Logger.printExceptionERROR(e);
        }

        if (DEBUG) {
            Extensions.print_info();
        }

        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);						// The Type Of Depth Testing To Do
        gl.glClearDepth(1.0f);

        //gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        //gl.glEnable(GL.GL_AUTO_NORMAL);
        //gl.glEnable(GL.GL_NORMALIZE);
        gl.glAlphaFunc(GL.GL_GREATER, 0.1f);
        gl.glEnable(GL.GL_ALPHA_TEST);
        // Depth Buffer Setup
        //gl.glClearStencil(0);								// Clear The Stencil Buffer To 0
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);	// Really Nice Perspective Calculations
        gl.glLineWidth(2);
        gl.glPointSize(5);

        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);
        gl.glEnable(GL.GL_CULL_FACE);

        //fog.enableFog(gl);
        //scenelight.enableLighting(gl);
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();


        if (main != null && main.getGuiEngine() != null) {
            main.getGuiEngine().initGUI(drawable);
        }

        shadowmapping.initshadowmap(drawable);
        world.sky.init(gl);
        text2d.BuildFont(gl);
        text3d.BuildFont(gl);
        quadric = glu.gluNewQuadric();

        fru = new Frustum();
        frustum_just_changed = true;
        //heightmap_auto.draw_terrain(gl);

        if (world != null && world.heightmap != null && world.heightmap.water != null) {
            reflection.init(gl);
        }

        bloom = new Bloom();
        bloom.setWindowViewport(glComp.getBounds().x, glComp.getBounds().y, glComp.getBounds().width, glComp.getBounds().height);
        bloom.init(gl);
        bloom.changeBlendFactor(0.4f);
        //bloom.changeResolution(gl,512,512);

        picking.initGLPickingBuffer(gl);
        
        Logger.printINFO("end of init.");
        init = true;

//        frame.requestFocus();
//        glc.requestFocus();
//        glc.requestFocusInWindow();
    }


    public void display(GLAutoDrawable gld) {
        if(!init){
            init(gld);
            return;
        }
        
        final GL gl = gld.getGL();
        GLU glu = new GLU();

        GraphicsEngine.triangles_drawn = 0;

        
        start_Display_event(gl);

        
        gl.glEnable(GL.GL_DEPTH_TEST);
        if (keyboardHelper.fog) {
            gl.glEnable(GL.GL_FOG);
        } else {
            gl.glDisable(GL.GL_FOG);
        }


        setPerspective(gl, glu);


        world.cam.update();
        camposition = world.cam.getLocation();
        camlookingat = world.cam.getLookingAt();
        //world.sun.updateSun();

        
        if (world != null && world.heightmap != null && world.heightmap.water != null) {
            reflection.createReflectionTexture(gl, gld, glu);
            //createRefractionTexture(gl, gld, glu);
        }
        
        
        picking.selectionPass(gl, glu);

        if (keyboardHelper.tremble) {
            //System.out.println("tremble!");
            gl.glViewport((int) (Math.random() * 10), (int) (Math.random() * 10), winwidth, winheight);
            int a = (int) (Math.random() * 10 - 5);
            int b = (int) (Math.random() * 10 - 5);
            robot.mouseMove(mouseHelper.lastX + a,
                    mouseHelper.lastY + b);
        } else {
            gl.glViewport(0, 0, winwidth, winheight);
        }

        if (keyboardHelper.wire_display) {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        } else {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        }
        
        //gl.glColor3f(1,0,0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (keyboardHelper.bloom) {
            bloom.activate(gl);
        }

        setPerspective(gl, glu);
        gl.glLoadIdentity();
        //gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor4f(1, 1, 1, 1);

        if (mainChar != null) {
            if (mcx != mainChar.getPhysicalRep().getCenter().x) {
                frustum_just_changed = true;
            }
            mcx = mainChar.getPhysicalRep().getCenter().x;
            if (mcy != mainChar.getPhysicalRep().getCenter().z) {
                frustum_just_changed = true;
            }
            mcy = mainChar.getPhysicalRep().getCenter().z;
            if (mcy != mainChar.getPhysicalRep().getCenter().y) {
                frustum_just_changed = true;
            }
            mcz = mainChar.getPhysicalRep().getCenter().y;
        }

        glu.gluLookAt(camposition.x, camposition.y, camposition.z, camlookingat.x, camlookingat.y, camlookingat.z, 0, 1, 0);

        if(fru!=null) fru.ExtractFrustum(gl);

        world.cam.calculateAndSaveMatrices(gld, winwidth, winheight);
        world.sun.calculateAndSaveMatrices(gld);

        //gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_BLEND);
        gl.glColor4f(1f, 1f, 1f, 1f);
        gl.glScalef(1f, 1f, 1f);
        if (world.heightmap != null) {
            world.heightmap.draw(gld);
        }
        world.sky.draw(gld);
        world.sun.draw(gld);

        gl.glColor4f(1f, 1f, 1f, 1f);        
        
        if (KeyboardHelper.VBOs) {
            gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        }

        wiisword.draw(gld);
        
        processOctreeFull(gld, gl, glu, world.root);

        if (fru.sphereInFrustumOrFrustumInSphere(world.sun.posX, world.sun.posY, world.sun.posZ, world.sun.sunsize)) {
            lens.setVectors(mcx + world.cam.location.x, mcy + world.cam.location.y, mcz + world.cam.location.z, mcx + world.cam.lookingAt.x, mcy + world.cam.lookingAt.y, mcz + world.cam.lookingAt.z, world.sun.posX, world.sun.posY, world.sun.posZ);
            lens.draw(gl, glu, fru);
        } else {
            lens.decreaseGlowFactor();
        }

        if (KeyboardHelper.VBOs) {
            gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        }
        
        if (keyboardHelper.bloom) {
            bloom.deactivate(gl, glu);
        }

        if (keyboardHelper.shadows) {
//			shadow(gld);
        }

        if (keyboardHelper.reflection) {
//				reflection(gld);
        }
        terrain_found = true;

        gl.glDisable(GL.GL_FOG);


        //if(keyboardHelper.sky)
        //skybox.display(gld);

        //drawTreeOverlay(gld,debugTreeOverlay);
		/*for(Element e : occluders_list.getList()){
        for(Triangle t :e.getTriangles())
        drawThisTriangle(gl, t);
        }*/
        //gl.glEnable(GL.GL_DEPTH_TEST);

        end_3D_event(gl);

        bloom.changeBlendFactor(lens.glowFactor);
        if (keyboardHelper.bloom) {
            bloom.renderGlow(gl);
        }
        if (keyboardHelper.shadowmap) {
            shadowmapping.draw(gld);
        }
        //if(setLeftBorderAction) {
        //	border_auto.LeftBorderAction(gl);
        //}
        //if(setRightBorderAction) {
        //	border_auto.RightBorderAction(gl);
        //}

        //gl.glDisable(GL.GL_TEXTURE_2D);

        //picking.drawCursor(gl);

        //gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glLoadIdentity();
        gl.glColor4f(1, 1, 1, 1);

        cinema.draw_cinema(gl);

        if (main != null && main.getGuiEngine() != null) {
            gl.glDepthMask(false);
            gl.glDisable(GL.GL_DEPTH_TEST);
            main.getGuiEngine().displayGUI(gld);
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glDepthMask(true);
        }

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        if (DEBUG) {
            text2d.glPrint(gl, 50, 20, "GraphicsEngine DEBUG");
        }

        text2d.glPrint(gl, 5, 30, "fps: " + actual_fps);
        text2d.glPrint(gl, 5, 5, "tris: " + triangles_drawn / 1000 + "K");

        /** for performance : comment it,
         * for a jprofiler special test : uncomment it */
        finish(gl);
        fpscount++;
    }
    

    /**
     * Display all the models in the octree with the mesh only (no texturing, no lighting)
     * @param gl
     * @param glu
     */
    protected void processOctreeGeometryOnly(GLAutoDrawable gld, GL gl, GLU glu, Octree node) {
        // initialy to_test_further must be true
        // here meshOnly is true
        processOctree(gld, gl, glu, node, true, true);
    }
    
    
    /**
     * Display all the models in the octree with textures, normals and everything
     * @param gl
     * @param glu
     */
    protected void processOctreeFull(GLAutoDrawable gld, GL gl, GLU glu, Octree node) {
        // initialy to_test_further must be true
        // here meshOnly is false
        processOctree(gld, gl, glu, node, true, false);
    }
    
    
    /**
     * Display models and optimize with the help of the octree.
     * @param gld
     * @param gl
     * @param glu
     * @param node
     * @param to_test_further
     * @param meshOnly
     */
    private void processOctree(GLAutoDrawable gld, GL gl, GLU glu, Octree node, boolean to_test_further, boolean meshOnly){
        //int culling = isVisible(node);
        int culling = 2;
        if (to_test_further) {
            switch(culling){
                case 0:
                    return;
                case 1:
                    to_test_further = true;
                    break;
                case 2:
                    to_test_further = false;
                    break;
                case 3:
                    return;
            }
            if (keyboardHelper.octree && !meshOnly) {
                // absolute coords, there should be no displacement yet
                drawThisCube(gl, node.getBoundingBox());
            }
        }

        for (PhysicalRep p : node.getContent()) {
            //if (to_test_further && !isVisible(p)) {
            //    continue;
            //}
            GraphicalRep g = p.getRep().getGraphicalRep();
            gl.glPushMatrix();
            if (g.getShowAABox() && !meshOnly) {
                // absolute coords, there should be no displacement yet
                g.drawPhysicalAABox(gl);
            }

            gl.glMultMatrixf(p.getMatrix(), 0);
            
            // relative coords
            if(meshOnly) g.drawGeometryOnly(gld);
            else g.draw(gld);
            gl.glPopMatrix();
        }

        if (node.getChildren() != null) {
            for (Octree c : node.getChildren()) {
                processOctree(gld, gl, glu, c, to_test_further, meshOnly);
            }
        }
    }


    /**
     * Checks if the given node is worth exploring, ie
     * if this Octree node is visible to the camera (in the visualization frustum)
     * AND it has content.
     * @param node
     * @return 0 if the sphere is totally outside, 1 if it's partially inside,
     * 2 if it's totally inside, and 3 if the node has no content.
     */
    public int isVisible(Octree node) {
        if (node.getSelfAndDescendantContentCount() <= 0) {
            return 3;
        }
        return fru.SphereInFrustum_ForTree(node.getBoundingBox().getSphere());
    //TODO: && fru.AABoxInFrustum(node.getBoundingBox()) , this method should be coded first
    }

    /**
     * Checks if the given physicalRep is in the visualization frustum
     * AT THE CONDITION THAT THE CENTER IS CORRECT IN THE PHYSICAL-REP !
     * @param prep a physicalRep. the center of the physicalRep must be equal to (0,0,0) in the modeling program that made the model
     * @return true if the PhysicalRep is visible to the camera
     */
    public boolean isVisible(PhysicalRep prep) {
        Point c = prep.getCenter();
        Sphere s = prep.getRep().getGraphicalRep().mesh.getMeshData().getBoundingSphere();
        Sphere sph = s.clone();
        sph.translate(c.x, c.y, c.z);
        return fru.sphereInFrustumOrFrustumInSphere(sph);
    //return true;
    }

    protected void setPerspective(GL gl, GLU glu) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        start_3D_event(gl, glu);
        if (keyboardHelper.far_view) {
            glu.gluPerspective(perspectiv, aspect, 1f, view_distance * 1000);
        } else {
            glu.gluPerspective(perspectiv, aspect, 1f, view_distance);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    protected void start_Display_event(GL gl) {
    }

    protected void end_3D_event(GL gl) {
    }

    protected void start_3D_event(GL gl, GLU glu) {
    }

    /**
     * recursif
     * @param gld OpenGL context
     * @param tree current node of the octree from the scene
     */
    /*	protected void drawTreeOverlay(GLAutoDrawable gld, Octree tree){
    // PLP? : Pas La Peine ?
    if(!fru.SphereInFrustum(tree.getBoundingBox().getSphere().center.x,
    tree.getBoundingBox().getSphere().center.y,
    tree.getBoundingBox().getSphere().center.z,
    tree.getBoundingBox().getSphere().rad)) {
    return;
    }
    // Are we Occluded ?
    if(tree.getBoundingBox().isOccludedBy(occluders_list.getList(), cameraPos)) {
    return;
    }
    // forme physique de ce noeud
    /*tree.getBoundingBox().setVisible(true);
    tree.getBoundingBox().setColor(Color.RED);
    drawThisCube(gld.getGL(), tree.getBoundingBox() );*/
    // bounding sphere de ce noeud
    //drawThisSphere(gld.getGL(), gld.getGLU(), tree.getBoundingBox().getSphere() );
    // le *Contenu utile* de ce noeud
    // - les occluders
    //for(physicsEngine.BoundingBox occ : tree.getOccluders()){
    //	if(fru.SphereInFrustum(occ.getSphere().center.x,
    //			occ.getSphere().center.y,
    //			occ.getSphere().center.z,
    //			occ.getSphere().rad)){
    //drawThisCube(gld.getGL(),occ);
    //		occluders_list.add(occ,cameraPos);
    //	}
    //}
    // - les models
    //for(geom.Cube c : tree.getContent()){
    //	if(!c.isOccludedBy(occluders_list.getList(), cameraPos)) {
    //		drawThisCube(gld.getGL(), c);
    //		drawThisSphere(gld.getGL(),gld.getGLU(),c.getSphere());
    //	}
    //}
    // - terrain par les triangles (proche de root) */
    /*
    if(tree.getLevel() >= EditorEngine.PATCH_LEVEL){
    Terrain terrain = octree2terrain.get(tree);
    if(gl_selection_mode){
    terrain.drawByTriangles_GLSelection(gld.getGL());
    }else{
    terrain.draw(gld.getGL());
    }
    }
    // recursivite vers ses enfants
    if(tree.getChildren() != null)
    for(Octree o : tree.getChildren(cameraPos)){
    if(o!=null)
    drawTreeOverlay(gld, o);
    }
    }
     */
    /**
     * Dessine un cube
     * @param gl
     * @param cube
     */
    public void drawThisCube(GL gl, AABox cube) {
        //if(!cube.isVisible()) 
        //	return;
        gl.glPushAttrib(GL.GL_ENABLE_BIT | GL.GL_POLYGON_BIT | GL.GL_CURRENT_BIT);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        gl.glDisable(GL.GL_CULL_FACE);
        //gl.glLoadIdentity();
        gl.glDisable(GL.GL_TEXTURE_2D);
        //if(cube.isTransparent()){
        //gl.glDisable(GL.GL_DEPTH_TEST);
        //gl.glEnable(GL.GL_BLEND);
        gl.glColor4f(1f, 0f, 1f, 1f);
        //}else{
        //	gl.glDisable(GL.GL_BLEND);
        //	gl.glColor4f(color.getRed(),color.getGreen(),color.getBlue(),1);
        //}

        Point[] points = cube.getPoints8();

        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // front face
        gl.glVertex3f(points[2].x, points[2].y, points[2].z);
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);
        gl.glVertex3f(points[1].x, points[1].y, points[1].z);

        gl.glVertex3f(points[1].x, points[1].y, points[1].z); // top face
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);

        gl.glVertex3f(points[6].x, points[6].y, points[6].z); // back face
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);

        gl.glVertex3f(points[2].x, points[2].y, points[2].z); // right face
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);
        gl.glVertex3f(points[5].x, points[5].y, points[5].z);
        gl.glVertex3f(points[3].x, points[3].y, points[3].z);

        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // left face
        gl.glVertex3f(points[1].x, points[1].y, points[1].z);
        gl.glVertex3f(points[7].x, points[7].y, points[7].z);
        gl.glVertex3f(points[6].x, points[6].y, points[6].z);

        gl.glVertex3f(points[0].x, points[0].y, points[0].z); // bottom face
        gl.glVertex3f(points[6].x, points[6].y, points[6].z);
        gl.glVertex3f(points[4].x, points[4].y, points[4].z);
        gl.glVertex3f(points[2].x, points[2].y, points[2].z);
        gl.glEnd();
        gl.glPopAttrib();
    }

    /**
     * Dessine un geom.Triangle
     * @param gl
     * @param cube
     */
    public void drawThisTriangle(GL gl, geom.Triangle triangle) {
        if (!triangle.isVisible()) {
            return;
        }
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
        gl.glPushMatrix();
        gl.glDisable(GL.GL_TEXTURE_2D);
        if (triangle.isWire()) {
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        }
        Color color = triangle.getColor();
        if (triangle.isTransparent()) {
            gl.glDisable(GL.GL_DEPTH_TEST);
            gl.glEnable(GL.GL_BLEND);
            gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 0.2f);
        } else {
            gl.glDisable(GL.GL_BLEND);
            gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 1);
        }
        //System.out.println("triangle: x1="+triangle.points[0].x+" y1="+triangle.points[0].y+" z1="+triangle.points[0].z);
        //System.out.println("          x2="+triangle.points[1].x+" y2="+triangle.points[1].y+" z2="+triangle.points[1].z);
        //System.out.println("          x3="+triangle.points[2].x+" y3="+triangle.points[2].y+" z3="+triangle.points[2].z);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(triangle.points[0].x, triangle.points[0].y, triangle.points[0].z);
        gl.glVertex3f(triangle.points[1].x, triangle.points[1].y, triangle.points[1].z);
        gl.glVertex3f(triangle.points[2].x, triangle.points[2].y, triangle.points[2].z);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glPopAttrib();
    }

    /**
     * Dessine un geom.Sphere
     * @param gl
     * @param glu
     * @param sphere
     */
    public void drawThisSphere(GL gl, GLU glu, geom.Sphere sphere) {
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
        gl.glPushMatrix();
        gl.glDisable(GL.GL_TEXTURE_2D);
        //Color color = sphere.getColor();
        //gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glColor4f(0, 0, 1, 0.2f);
        geom.Point center = sphere.center;
        gl.glTranslatef(center.x, center.y, center.z);
        //System.out.println("sphere center: "+center.x);
        glu.gluSphere(quadric, sphere.rad, 10, 10);
        gl.glPopMatrix();
        gl.glPopAttrib();
    }

    public void finish(GL gl) {
        gl.glFinish();			// Wait The GL Pipeline before continuing
    }

    public void reflection(GLAutoDrawable gld) {
    //GL gl = gld.getGL();
    /****************************** REFLECTION **************************************************/
    // We Supose That All Frame Buffers Are Cleared
/*		gl.glColorMask(false,false,false,false); // No Draw
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_STENCIL_TEST);
    gl.glStencilFunc(GL.GL_ALWAYS, 3, 0xffffffff);// Always Passes, 1 Bit Plane, 1 As Mask
    gl.glStencilOp(GL.GL_ZERO,GL.GL_KEEP, GL.GL_REPLACE);
    // Set Stencil To 1 Where ReflectING Region Is
    heightmap_auto.water.draw(gld,0,0,0,0,0,0);
    //(Intersection) Set Stencil To 0,
    // Where Reflecting Region Is Masked By Some Objects
    gl.glStencilOp(GL.GL_ZERO, GL.GL_KEEP, GL.GL_ZERO);
    // Zero If Test Fails (Here It Always Passes),
    // Keep If Test Passes But Depth-Test Fails, Zero If Test Passes
    heightmap_auto.draw_terrain(gl);
    // Draw ReflectED Objects
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    gl.glColorMask(true,true,true,true);
    gl.glStencilFunc(GL.GL_LEQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
    gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);	// Don't Change
    gl.glPushMatrix();
    gl.glScalef(1.0f, -1.0f, 1.0f);			// Mirror Y Axis
    gl.glEnable(GL.GL_CLIP_PLANE0);		// Enable Clip Plane For Removing Artifacts (When The Object Crosses The Floor)
    double eqr[] = {0.0f,1.0f, 0.0f, 0.0f};
    gl.glClipPlane(GL.GL_CLIP_PLANE0, eqr,0);	// Equation For Reflected Objects
    drawScene(gld,true);
    heightmap_auto.draw_terrain(gl);
    sun.draw(gld,0,0,0,0,0,0);
    gl.glPopMatrix();
    gl.glDisable(GL.GL_CLIP_PLANE0);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    gl.glStencilFunc(GL.GL_EQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
    gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);	// Don't Change
    // THE REFLECTING REGION GETS BLENDED
    heightmap_auto.water.draw(gld,0,0,0,0,0,0);
    // DISPLAY THE REST BUT DONT TOUCH THE REFLECT :)
    //gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    gl.glStencilFunc(GL.GL_NOTEQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
    gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);	// Don't Change
    heightmap_auto.draw_terrain(gl);
    terrain_found = true;
    heightmap_auto.water.draw(gld,0,0,0,0,0,0);
    gl.glDisable(GL.GL_STENCIL_TEST);
    /********************************************************************************************/
    }

    public void click(MouseEvent e) {
        picking.click(e);
    }

    /**
     * OpenGL Method : the screen resolution has changed.
     * @param gld OpenGL context
     * @param x horizontal coord of the top-left corner of the screen
     * @param y vertical coord of the top-left corner of the screen
     * @param w width of the screen
     * @param h height of the screen
     */
    public void reshape(GLAutoDrawable gld, int x, int y, int w, int h) {
        assert(0==x);
        assert(0==y);
        winwidth = w;
        winheight = h;
        final GL gl = gld.getGL();
        gl.glViewport(x, y, winwidth, winheight);

        if (bloom != null)
            bloom.setWindowViewport(x, y, w, h);
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        aspect = (float) winwidth / (float) winheight;
        (new GLU()).gluPerspective(perspectiv, aspect, 1f, view_distance);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void setDisplayList(Set<Rep> liparam) {
        this.li = liparam;
    }

    public void debugsetDisplayList(LinkedList<Graphics> lidparam) {
        //avoid adding 2 or more terrains(and borders) to the scene
        boolean terrain_exist = false;
        boolean border_exist = false;
        for (Graphics gr : lidparam) {
            if (gr instanceof Heightmap) {
                terrain_exist = true;
                break;
            }
            if (gr instanceof Border) {
                border_exist = true;
                break;
            }

        }
        if (!terrain_exist && !border_exist) {
            this.lid = lidparam;
        } else {
            throw new RuntimeException("setDisplayList ne doit pas passer de Terrain ou de Border!");
        }
    }

    public void info_sleeptime(long slp) {
        this.slept = slp;
    //System.out.println(slp);
    }

    public void goFullScreen() {
        throw new RuntimeException("Not yet implemented !");
    }

    public void goWindowed() {
        throw new RuntimeException("Not yet implemented !");
    }

    /**
     * The Main sends a run() tick to every engine sequentialy and approx every 1/60 second.
     */
    public void run() {
        if (fpsHelper != null) {
            fpsHelper.tick();
        }
        //heightmap_auto.water.tick();
        if (mouseHelper != null) {
            mouseHelper.tick();
        }
        //particles_tick();
        
        this.glComp.display();
        //Thread.yield();
    }

    public ClientMain getMain() {
        return main;
    }

    public void setView_Distance(float howmuch) {
        view_distance = howmuch;
    }

    public GLComponent getGLComponent() {
        return glComp;
    }

    public void setLeftBorderAction() {
        this.setLeftBorderAction = true;
    }

    public void setRightBorderAction() {
        this.setRightBorderAction = true;
    }

    public void setStopBorderAction() {
        this.setLeftBorderAction = false;
        this.setRightBorderAction = false;
    }

    public void setMainChar(CharacterRep<GameRep, PhysicalRep, GraphicalRep> rep) {
        mainChar = rep;
    }
    /*public void setHeightMap(float[][] map, int map_size){
    heightmap_auto.setHeightMap_fromPhysical(map, map_size);
    }*/

    public void setHeightMap(Heightmap map) {
        this.world.heightmap = map;
    }

    public void setModifiedHeightmaps(int indexstartx, int indexendx, int indexstarty, int indexendy) {
        this.world.heightmap.notifyModifiedHeightmaps(indexstartx, indexendx, indexstarty, indexendy);
    }

    public Text2D getText2D() {
        return text2d;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public int getWidth() {
        return winwidth;
    }

    public int getHeight() {
        return winheight;
    }

    public void changeWorld(World world) {
        this.world = world;
    }

    protected void addTriangleNameInSelectionMode(Triangle t) {
        picking.addTriangleNameInSelectionMode(t);
    }

    /**
     * User input (keyboard) arrival.
     * @return false if we consumed the event
     */
    public boolean invokeKeyEvent(String action) {
        return keyboardHelper.invokeKeyEvent(action);
    }

    /**
     * User input (mouse) arrival.
     * @return false if we consumed the event
     */
    public boolean invokeMouseEvent(String action, int x, int y) {
        return mouseHelper.invokeMouseEvent(action, x, y);
    }

    public void quit() {

    }

    public GraphicalRep loadObject(Node node) throws Exception {
        float xrot = 0, yrot = 0, zrot = 0;
        String path = null;

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equalsIgnoreCase("path")) {
                    path = child.getTextContent();
                } else if (child.getNodeName().equalsIgnoreCase("xrot")) {
                    xrot = Float.parseFloat(child.getTextContent());
                } else if (child.getNodeName().equalsIgnoreCase("yrot")) {
                    yrot = Float.parseFloat(child.getTextContent());
                } else if (child.getNodeName().equalsIgnoreCase("zrot")) {
                    zrot = Float.parseFloat(child.getTextContent());
                }
            }
        }

        GraphicalRep g = GraphicalRepFactory.load(path);
        g.rotate(xrot, yrot, zrot);
        return g;
    }

    private void createRefractionTexture(GL gl, GLAutoDrawable gld, GLU glu) {
    /** *************************** **/
    /** CREATING REFRACTION TEXTURE **/
    /*gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
    gl.glViewport(0,0, world.heightmap.water.texture_quality, world.heightmap.water.texture_quality);
    if(world.heightmap!=null) world.heightmap.draw_terrain(gl);
    if(KeyboardHelper.VBOs)
    gl.glEnableClientState(GL.GL_VERTEX_ARRAY );
    processOctreeFull(gld,gl,glu,world.root);
    if(KeyboardHelper.VBOs)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY );
    gl.glBindTexture(GL.GL_TEXTURE_2D,this.refraction.internal_index[0]);
    gl.glCopyTexSubImage2D(GL.GL_TEXTURE_2D,0, 0,0, 0,0, world.heightmap.water.texture_quality, world.heightmap.water.texture_quality);
     */
    }
}
