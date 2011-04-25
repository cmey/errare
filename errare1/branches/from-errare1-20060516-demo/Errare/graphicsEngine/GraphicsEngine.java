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

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;

import main.CharacterRep;
import main.HeroRep;
import main.Main;
import main.Rep;
import physicsEngine.Cube;
import physicsEngine.CubeTree;
import physicsEngine.Point3D;

public class GraphicsEngine implements Runnable, GLEventListener {
	
	static {
        // setting this true causes window events not to get sent on Linux if you run from inside Eclipse
        GLProfile.initSingleton( false );
    }
	
	public static boolean DEBUG = true;
	int debug_displacement;
	
	private float shadow_nearZ = 100;
	private float shadow_farZ = 5000;
	public float lookX, lookY, lookZ, camX, camY, camZ = 0f;
	private MouseHelper mouseHelper;
	public static KeyboardHelper keyboardHelper;
	private FPSHelper fpsHelper;
	private static GraphicsDevice device;
	private static JFrame frame;
	private int slept;
	public static int voxel_drawn;
	private float x,y,z,sx,sy,sz,sr;
	
	private static GLCanvas glc;
	static public int window_width, window_height;
	private float view_distance = 10000f;
	private Set<Rep> li;
	private CubeTree cubetree;
	private List<GraphicalRep> lid;
	private Frustum fru;
	private Main main;
	private HeroRep mainChar;
	
	private int mode;
	private static final int MODE_NORMAL = 0;
	private static final int MODE_PICKING = 1;
	public int fpscount;
	public int actual_fps;
	
	public static boolean frustum_just_changed;
	private boolean setLeftBorderAction;
	private boolean setRightBorderAction;
	public boolean spell_casting;
	
	//FX
	private Glow glow;
	
	private MouseEvent click_event;
	private Point click_event_pt;
	private int xMouse;
	private int yMouse;
	private float mcx,mcy,mcz; //position of main character
	
	private double intersecX, intersecY, intersecZ;
	private float rond_picking_size;
	private boolean terrain_found = false;
	
	//private IntBuffer selectionBuffer; 
	private float aspect;
	protected GLUquadric quadric;
	
	public static Skybox skybox;
	private Heightmap heightmap_auto;
	private Grass herbe_auto;
	private Border border_auto;
	public static Sun sun;
	private Text2d text2d;
	private ShadowMap shadowmap;
	private Cinema cinema;
	//private LensFlare lens;
	private Robot robot;
	private Cursor cursor;
	private static Particles active_spell;
	
	Matrix lightProjectionMatrix = new Matrix(4,4);
	Matrix lightViewMatrix = new Matrix(4,4);
	
	
	public GraphicsEngine(JFrame f){
		DEBUG=true;
		frame=f;
		GraphicalRep.DEBUG=true;
		GraphicsEngineInit();
	}
	
	public GraphicsEngine(Main main){
		DEBUG=false;
		GraphicalRep.DEBUG=false;
		this.main = main;
		frame=main.getJFrame();
		GraphicsEngineInit();
	}
	
	private void GraphicsEngineInit() {
		System.out.println("Starting game...");
		
		lid = new LinkedList<GraphicalRep>();
		li = new HashSet<Rep>();
		
		skybox = new Skybox();
		text2d = new Text2d("data/images/other/font.png");
		herbe_auto = new Grass("data/images/floor/grass1.png");
		heightmap_auto = new Heightmap("data/images/other/heightmap.png",1000);
		border_auto = new Border("data/images/sky/border.png");
		sun = new Sun("data/images/sky/sun1.png");
		shadowmap = new ShadowMap("data/images/other/shadowmapundef.png");
		glow = new Glow();
		cinema = new Cinema();
		//lens = new LensFlare();
		cursor = new Cursor("data/images/other/cursor.png");
		
		try{
			robot = new Robot();
		}catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		
		fpsHelper = new FPSHelper(this);
		mouseHelper = new MouseHelper(this);
		keyboardHelper = new KeyboardHelper(mouseHelper);
		
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities capabilities=new GLCapabilities(glprofile);
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		capabilities.setStencilBits(3);
		
		glc = new GLCanvas(capabilities);
		glc.addGLEventListener(this);
		
		
		//glc.setSize(1024,768);
		glc.setVisible(true);
		//window_width = frame.getWidth();
		//window_height = frame.getHeight();
		
		/*
		 glj = GLDrawableFactory.getFactory().createGLJPanel(capabilities);
		 glj.addGLEventListener(this);
		 glj.addMouseListener(mouseHelper);
		 glj.addMouseMotionListener(mouseHelper);
		 glj.addMouseWheelListener(mouseHelper);
		 glj.addKeyListener(keyboardHelper);
		 glj.setVisible(true);
		 glj.requestFocusInWindow();
		 width = glj.getWidth();
		 height = glj.getHeight();
		 */
	}
	
	
	
	float[] LightdimAmbient = { 0.2f , 0.2f , 0.2f };
	float[] LightbrightAmbient = { 0.8f , 0.8f , 0.8f };
	float[] LightbrightDiffuse = { 1.0f , 1.0f , 1.0f };
	float[] LightdimDiffuse = { 0.2f, 0.2f, 0.2f };
	float[] LightPosition = { 0.0f, 100.0f, 0.0f, 1.0f };
	public int[] maxTexelUnits;
	
	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		GLU glu = new GLU();
		
		Extensions.readEXT(gl);
		if(DEBUG) Extensions.print_info();
		//Extensions.write_info();
		
		maxTexelUnits = new int[1];
		gl.glGetIntegerv(GL2.GL_MAX_TEXTURE_UNITS,maxTexelUnits, 0);
		if(maxTexelUnits[0]<2 || !Extensions.isSupported("GL_ARB_multitexture")
				|| !Extensions.isSupported("GL_EXT_texture_env_combine")){
			System.out.println("MULTITEXTURE NOT SUPPORTED !");
		}
		
		//Extensions.assertPresence("GL_ARB_shadow");
		//Extensions.assertPresence("GL_ARB_depth_texture");
		//Extensions.assertPresence("GL_NV_register_combiners");
		//Extensions.assertPresence("GL_EXT_texture_env_combine");
		
		/** pour avoir les messages d'erreur d'opengl */
		//glDrawable.setGL(new DebugGL(glDrawable.getGL()));
		//glDrawable.setGL(new TraceGL(glDrawable.getGL(), System.err));
		
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);						// The Type Of Depth Testing To Do
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		//gl.glEnable(GL2.GL_AUTO_NORMAL);
		//gl.glEnable(GL2.GL_NORMALIZE);
		gl.glAlphaFunc(GL2.GL_GREATER,0.1f);
		gl.glEnable(GL2.GL_ALPHA_TEST);
		gl.glClearDepth(1.0f);								// Depth Buffer Setup
		gl.glClearStencil(0);								// Clear The Stencil Buffer To 0
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);	// Really Nice Perspective Calculations
		gl.glLineWidth(4);
		
		gl.glFrontFace(GL2.GL_CCW);
		//gl.glCullFace(GL2.GL_FRONT);
		//gl.glEnable(GL2.GL_CULL_FACE);	
		
		gl.glClearColor(0.5f,0.5f,0.5f,1.0f);				// We'll Clear To The Color Of The Fog ( Modified )
		
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);			// Fog Mode
		float [] fogcolor = {0.5f, 0.5f, 0.5f, 1.0f};
		gl.glFogfv(GL2.GL_FOG_COLOR, fogcolor, 0);				// Set Fog Color
		//  gl.glFogf(GL2.GL_FOG_DENSITY, 0.001f);				// How Dense Will The Fog Be
		gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);			// Fog Hint Value
		gl.glFogf(GL2.GL_FOG_START, 70f/100f*view_distance);	// Fog Start Depth
		gl.glFogf(GL2.GL_FOG_END, view_distance);			// Fog End Depth
		gl.glEnable(GL2.GL_FOG);								// Enables GL_FOG
		
		
		//Set up materials
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		float[] white = {1f,1f,1f,1f};
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, white, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 32.0f);
		
		
		
		//float[] LightSpecular = { 1.0f , 1.0f , 1.0f , 1.0f };
		float[] black = {0f,0f,0f,0f};
		
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, LightdimAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, LightdimDiffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, black, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, LightbrightAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, LightbrightDiffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, white, 0);
		
		//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, LightSpecular);
		//gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, global_ambient);
		
		//gl.glEnable(GL2.GL_LIGHT1);
		//gl.glEnable(GL2.GL_LIGHTING);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//gl.glPolygonMode( GL2.GL_BACK, GL2.GL_LINE );			// Back Face Is Filled In
		//gl.glPolygonMode( GL2.GL_FRONT, GL2.GL_LINE );			// Front Face Is Drawn With Lines
		if(!DEBUG)
			//main.getGuiEngine().initGUI(glDrawable);
		glc.addMouseListener(mouseHelper);
		glc.addMouseMotionListener(mouseHelper);
		glc.addMouseWheelListener(mouseHelper);
		glc.addKeyListener(keyboardHelper);
		
		skybox.init(glDrawable);
		text2d.BuildFont(gl);
		Cinema.cinemaOn();
		quadric = glu.gluNewQuadric();
		camX=1;camY=10;camZ=mouseHelper.rayonMax; lookX=lookY=lookZ=0;
		
		fru = new Frustum();
		frustum_just_changed =true;
		glc.requestFocus();
		heightmap_auto.draw_terrain(gl);
	}
	
	
	public void display(GLAutoDrawable gld) {
		voxel_drawn=0;
		GL2 gl=gld.getGL().getGL2();
		GLU glu= new GLU();
		
		gl.glEnable(GL2.GL_DEPTH_TEST);
		if(keyboardHelper.fog_on){
			gl.glEnable(GL2.GL_FOG);
		}else{
			gl.glDisable(GL2.GL_FOG);
		}
		
		if(keyboardHelper.far_view){
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45, aspect, 1f, view_distance*10);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
		}else{
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45, aspect, 1f, view_distance);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
		}
		
		
		if(keyboardHelper.tremble){
			System.out.println("tremble!");
			gl.glViewport((int)(Math.random()*10),(int)(Math.random()*10),window_width,window_height);
			int a = (int)(Math.random()*10-5);
			int b = (int)(Math.random()*10-5);
			robot.mouseMove(mouseHelper.cursorX + a,
					mouseHelper.cursorY + b);
		}else{
			gl.glViewport(0,0,window_width,window_height);
		}
		
		
		if(keyboardHelper.wire_mode)
			gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_LINE );
		else
			gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_FILL );
		
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glColor4f(1,1,1,1);
		
		if(mainChar != null) {
			if(mcx!=mainChar.getPhysics().x()) frustum_just_changed=true;
			mcx=mainChar.getPhysics().x();
			if(mcy!=mainChar.getPhysics().z()) frustum_just_changed=true;
			mcy=mainChar.getPhysics().z();
			if(mcy!=mainChar.getPhysics().y()) frustum_just_changed=true;
			mcz=mainChar.getPhysics().y();
		}else{	//we are in Test : center the view
			mcx=heightmap_auto.MAP_SIZE/2;
			mcy=0;
			mcz=heightmap_auto.MAP_SIZE/2;
		}
		
		if(!DEBUG && (mcy+camY)<=(10+main.getPhysicsEngine().getTerrainHeightAt((int)(mcx+camX),(int)(mcz+camZ)))){
			camY=(10+main.getPhysicsEngine().getTerrainHeightAt((int)(mcx+camX),(int)(mcz+camZ)))-mcy;
		}
		glu.gluLookAt(mcx+camX, mcy+camY, mcz+camZ, mcx+lookX, mcy+lookY, mcz+lookZ, 0,1,0);
		
		if(keyboardHelper.glow_on){
			gl.glViewport(0,0, glow.glowSize, glow.glowSize);
			heightmap_auto.draw(gld);
			drawScene(gld,false,false);
			glow.readWindow(gl);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
			gl.glViewport(0,0,window_width,window_height);
		}
		
		
		if(frustum_just_changed){
			frustum_just_changed=false;
			fru.ExtractFrustum(gl);
		}
		
		if(keyboardHelper.shadow_mode){
			shadow(gld);
		}else{				
			
			if(keyboardHelper.reflection_on)
				reflection(gld);
			else{
				heightmap_auto.draw_terrain(gl);
				heightmap_auto.water.draw(gld);
				terrain_found=true;
			}
			drawScene(gld,false,true);
			
			if(mainChar!=null && keyboardHelper.display_tree){
				gl.glDisable(GL2.GL_TEXTURE_2D);
				gl.glEnable(GL2.GL_BLEND);
				gl.glPushMatrix();
				gl.glColor4f(0,0,1,0.5f);
				Point3D pmc = mainChar.getPhysics().getCube().getCenter();
				float rmc = mainChar.getPhysics().getCube().getRadius();
				gl.glTranslatef(pmc.x,pmc.z,pmc.y);
				glu.gluSphere(quadric,rmc,14,14);
				gl.glPopMatrix();
				pmc = mainChar.getPhysics().getCube().getTree().getCenter();
				rmc = mainChar.getPhysics().getCube().getTree().getRadius();
				gl.glPushMatrix();
				gl.glColor4f(1,0,0,0.5f);
				gl.glTranslatef(pmc.x,pmc.z,pmc.y);
				glu.gluSphere(quadric,rmc,14,14);
				gl.glPopMatrix();
				
				gl.glColor4f(1,1,1,1);
				gl.glEnable(GL2.GL_TEXTURE_2D);
			}
		}
		gl.glDisable(GL2.GL_FOG);
		
		sun.updateSun();		
		//sun.draw(gld,0,0,0,0,0,0);
		
		skybox.display(gld);
		
		/*
		if(fru.SphereInFrustum(sun.sunX,sun.sunY,sun.sunZ,100)){
			lens.setVectors(mcx+camX, mcy+camY, mcz+camZ, mcx+lookX, mcy+lookY, mcz+lookZ, sun.sunX, sun.sunY, sun.sunZ);
			lens.draw(gl,glu,fru);
		}
		*/
		
		if(keyboardHelper.glow_on)
			glow.renderGlow(gl,3,10);
		
		
		if(keyboardHelper.draw_shadowmap)
			shadowmap.draw(gld,0,100,-200,0,0,0);
		
		if(setLeftBorderAction) {
			border_auto.LeftBorderAction(gl);
		}
		if(setRightBorderAction) {
			border_auto.RightBorderAction(gl);
		}
		
		
		//gl.glDisable(GL2.GL_TEXTURE_2D);
		
		gl.glPushMatrix();
		gl.glColor4f(1,1,1,1);
		gl.glTranslated(intersecX, intersecY, intersecZ);
		//gl.glTranslatef(0,5,0); //je fais un cylinder de 5 de haut : je translate de 5
		//gl.glRotated(90,1,0,0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		cursor.draw(gld);
		//glu.gluCylinder(quadric, rond_picking_size,rond_picking_size,5,10,1);
		//rond_picking_size=(rond_picking_size+0.5f)%15f;
		//gl.glColor3f(1,1,1);
		gl.glPopMatrix();
		
		//gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glLoadIdentity();
		gl.glColor4f(1,1,1,1);
		
		
		cinema.draw_cinema(gl);
		
		
		if(mode==MODE_PICKING) {
			gestionPicking(click_event, gl, glu);
			mode=GraphicsEngine.MODE_NORMAL;
		}
		
		if(!DEBUG){
			gl.glDepthMask(false);
			//main.getGuiEngine().displayGUI(gld);
			gl.glDepthMask(true);
		}
		
		gl.glPolygonMode( GL2.GL_FRONT_AND_BACK, GL2.GL_FILL );
		if(DEBUG)
			text2d.glPrint(gl,50,20,"GraphicsEngine Demo");
		text2d.glPrint(gl,400,20,"culling: "+keyboardHelper.culling_on);
		
		text2d.glPrint(gl,50,40,"fps: "+actual_fps+"  triangles: "+voxel_drawn/3 +"  IDLE (sleeptime): "+slept/600000);
		
		/** for performance : comment it,
		 * for a jprofiler special test : uncommented */
		//finish(gl);
		
		fpscount++;
	}
	
	public void drawSceneTree(GLAutoDrawable gld, boolean for_reflect){
		GraphicalRep.quadtree_hack_loopnumber++;
		recurs(gld,for_reflect,this.cubetree);
	}
	
	public void recurs(GLAutoDrawable gld, boolean for_reflect, CubeTree t){
		if(t.getNodeContentsCount()<t.getTreeContentsCount()){
			for(CubeTree ct : t.getChilds()){
				sx = ct.getCenter().x;
				sy = ct.getCenter().z;
				sz = ct.getCenter().y;
				sr = ct.getRadius();
				if(for_reflect)
					sy=-sy;
				if(keyboardHelper.culling_on){
					if(fru.SphereInFrustum(sx,sy,sz,sr)){
						recurs(gld,for_reflect,ct);
					}
				}else{
					recurs(gld,for_reflect,ct);
				}
			}
		}
		if(t.getNodeContentsCount()>0) {
			ArrayList<Cube> l = t.getNodeContents();
			for(Cube c : l){
				sx = c.getCenter().x;
				sy = c.getCenter().z;
				sz = c.getCenter().y;
				sr = c.getRadius();
				if(for_reflect)
					sy=-sy;
				if(keyboardHelper.culling_on){
					if(fru.SphereInFrustum(sx,sy,sz,sr)){
						Rep r = c.getRep();
						r.getGraphics().drawInLoop(gld,r.getPhysics().x(),r.getPhysics().z(),r.getPhysics().y(),0,(int)r.getPhysics().getAngle(),0);
					}
				}else{
					Rep r = c.getRep();
					r.getGraphics().drawInLoop(gld,r.getPhysics().x(),r.getPhysics().z(),r.getPhysics().y(),0,(int)r.getPhysics().getAngle(),0);
				}
			}
			
		}
	}
	
	public void drawScene(GLAutoDrawable gld, boolean for_reflect, boolean advance_anim){
		if(advance_anim) {MD2.forward=true;} else {MD2.forward=false;}
		if(!DEBUG){
			drawSceneTree(gld,for_reflect);
		}else{
			GraphicalRep.quadtree_hack_loopnumber++;
			debug_displacement = -200;
			for(GraphicalRep grdt : lid){
				debug_displacement+=20;
				if(keyboardHelper.culling_on){
					if(grdt instanceof MD2){
						sx=grdt.sphereCenter.X + heightmap_auto.MAP_SIZE/2 + (debug_displacement+20);
						sy=grdt.sphereCenter.Y;
						if(for_reflect)
							sy=-sy;
						sz=grdt.sphereCenter.Z + heightmap_auto.MAP_SIZE/2;
						sr=grdt.radius;
						if(fru.SphereInFrustum(sx,sy,sz,sr)){
							grdt.drawInLoop(gld,heightmap_auto.MAP_SIZE/2 + debug_displacement,0,heightmap_auto.MAP_SIZE/2,0,0,0);
						}
					}else{
						grdt.drawInLoop(gld,heightmap_auto.MAP_SIZE/2 + debug_displacement,0,heightmap_auto.MAP_SIZE/2,0,0,0);
					}
				}else{
					grdt.drawInLoop(gld,heightmap_auto.MAP_SIZE/2 + debug_displacement,0,heightmap_auto.MAP_SIZE/2,0,0,0);
				}
			}
		}
		
		
		
	}
	
	
	public void finish(GL gl){
		gl.glFinish();			// Wait The GL Pipeline before continuing
	}
	
	
	float[] cameraProjectionMatrixBuffer = new float[16];
	float[] cameraViewMatrixBuffer = new float[16];
	float[] lightProjectionMatrixBuffer = new float[16];
	float[] lightViewMatrixBuffer = new float[16];
	public void shadow(GLAutoDrawable gld){
		GL2 gl = gld.getGL().getGL2();
		GLU glu = new GLU();
		if(sun.has_just_changed){
			//RENDER SCENE FROM LIGHT'S POINT OF VIEW
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
			//gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glColorMask(false,false,false,false);
			gl.glViewport(0, 0, shadowmap.shadowMapSize, shadowmap.shadowMapSize);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45, 1f, shadow_nearZ, shadow_farZ);
			gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, lightProjectionMatrixBuffer, 0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			glu.gluLookAt(sun.sunX, sun.sunY, sun.sunZ, mcx, mcy, mcz, 0,1,0);
			gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, lightViewMatrixBuffer, 0);
			gl.glPolygonOffset(1.1f, 4.0f);
			gl.glCullFace(GL2.GL_FRONT);
			gl.glEnable(GL2.GL_CULL_FACE);
			gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
			drawScene(gld,false,false);
			heightmap_auto.draw(gld);
			gl.glPopAttrib();
			
			
			// CAPTURE DEPTH BUFFER FROM LIGHT'S POINT OF VIEW
			shadowmap.readShadowMap(gl,glu);
			gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			gl.glViewport(0,0,window_width,window_height);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45, aspect, 1f, view_distance);
			gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, cameraProjectionMatrixBuffer, 0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			glu.gluLookAt(mcx+camX, camY, mcz+camZ, mcx+lookX, mcy+lookY, mcz+lookZ, 0,1,0);
			gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, cameraViewMatrixBuffer, 0);
		}
		
		// Draw ALL shadowed
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		LightPosition[0]=sun.sunX;
		LightPosition[1]=sun.sunY;
		LightPosition[2]=sun.sunZ;
		LightPosition[3]=1f;
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LightPosition, 0);
		gl.glPopMatrix();
		gl.glEnable(GL2.GL_LIGHT0);	//LIGHT0 is set up to be dark
		gl.glEnable(GL2.GL_LIGHTING);
		drawScene(gld,false,false);
		heightmap_auto.draw(gld);
		gl.glPopAttrib();
		
		
		// Draw Unshadowed parts
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, LightPosition, 0);
		gl.glEnable(GL2.GL_LIGHT1); //LIGHT1 is set up to be bright
		
		float[] biasMatrixBuffer2D =   {0.5f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.5f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f};
		
		Matrix biasMatrix2D = new Matrix(4,4);
		biasMatrix2D.setArray(biasMatrixBuffer2D);
		Matrix lightProjectionMatrix = new Matrix(4,4);
		lightProjectionMatrix.setArray(lightProjectionMatrixBuffer);
		Matrix lightViewMatrix = new Matrix(4,4);
		lightViewMatrix.setArray(lightViewMatrixBuffer);
		
		//Set up texture units
		//Unit 0
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, shadowmap.internal_texture1[0]);
		
		//Set up tex coord generation - s, t, q coords required
		Matrix textureProjectionMatrix2D = new Matrix(4,4);
		textureProjectionMatrix2D = biasMatrix2D.times(lightProjectionMatrix.times(lightViewMatrix));
		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
		gl.glTexGenfv(GL2.GL_S, GL2.GL_EYE_PLANE, textureProjectionMatrix2D.GetRow(0), 0);
		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
		gl.glTexGenfv(GL2.GL_T, GL2.GL_EYE_PLANE, textureProjectionMatrix2D.GetRow(1), 0);
		gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		gl.glTexGeni(GL2.GL_Q, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
		gl.glTexGenfv(GL2.GL_Q, GL2.GL_EYE_PLANE, textureProjectionMatrix2D.GetRow(3), 0);
		gl.glEnable(GL2.GL_TEXTURE_GEN_Q);
		
		//Unit 1
		gl.glActiveTexture(GL2.GL_TEXTURE2);
		gl.glEnable(GL2.GL_TEXTURE_1D);
		gl.glBindTexture(GL2.GL_TEXTURE_1D, shadowmap.internal_texture2[0]);
		//Set up tex coord generation - s, q coords required
		float[] biasMatrixBuffer1D =   {0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.5f, 0.0f, 0.0f, 0.0f,
				0.5f, 0.0f, 0.0f, 1.0f};
		Matrix biasMatrix1D = new Matrix(4,4);
		biasMatrix1D.setArray(biasMatrixBuffer1D);
		
		Matrix textureProjectionMatrix1D = new Matrix(4,4);
		textureProjectionMatrix1D = biasMatrix1D.times(lightProjectionMatrix.times(lightViewMatrix));
		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
		gl.glTexGenfv(GL2.GL_S, GL2.GL_EYE_PLANE, textureProjectionMatrix1D.GetRow(0), 0);
		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glTexGeni(GL2.GL_Q, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
		gl.glTexGenfv(GL2.GL_Q, GL2.GL_EYE_PLANE, textureProjectionMatrix1D.GetRow(3), 0);
		gl.glEnable(GL2.GL_TEXTURE_GEN_Q);
		
		
		//Set up texture combining
		//unit 0
		//alpha=texture alpha
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_ALPHA, GL2.GL_TEXTURE);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_ALPHA, GL2.GL_SRC_ALPHA);
		
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_ALPHA, GL2.GL_REPLACE);
		//color=primary color
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_RGB, GL2.GL_PRIMARY_COLOR);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_RGB, GL2.GL_SRC_COLOR);
		
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_RGB, GL2.GL_REPLACE);
		
		//unit 1
		//alpha =previous (add signed) (1-texture)
		gl.glActiveTexture(GL2.GL_TEXTURE2);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_ALPHA, GL2.GL_PREVIOUS);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_ALPHA, GL2.GL_SRC_ALPHA);
		
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_ALPHA, GL2.GL_ADD_SIGNED);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE1_ALPHA, GL2.GL_TEXTURE);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND1_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		//color=primary color
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_RGB, GL2.GL_PREVIOUS);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_RGB, GL2.GL_SRC_COLOR);
		
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_RGB, GL2.GL_REPLACE);
		
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		
		//Use alpha test to reject shadowed fragments
		gl.glAlphaFunc(GL2.GL_GEQUAL, 0.5f);
		gl.glEnable(GL2.GL_ALPHA_TEST);
		
		drawScene(gld,false,false);
		heightmap_auto.draw(gld);
		gl.glPopAttrib();
		gl.glDisable(GL2.GL_ALPHA_TEST);
	}
	
	
	
	public void reflection(GLAutoDrawable gld){
		GL2 gl = gld.getGL().getGL2();
		/****************************** REFLECTION **************************************************/			
		
		// We Supose That All Frame Buffers Are Cleared
		gl.glColorMask(false,false,false,false); // No Draw
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_STENCIL_TEST);
		gl.glStencilFunc(GL2.GL_ALWAYS, 3, 0xffffffff);// Always Passes, 1 Bit Plane, 1 As Mask
		
		gl.glStencilOp(GL2.GL_ZERO,GL2.GL_KEEP, GL2.GL_REPLACE);
		// Set Stencil To 1 Where ReflectING Region Is
		heightmap_auto.water.draw(gld,0,0,0,0,0,0);
		
		//(Intersection) Set Stencil To 0,
		// Where Reflecting Region Is Masked By Some Objects
		gl.glStencilOp(GL2.GL_ZERO, GL2.GL_KEEP, GL2.GL_ZERO);
		// Zero If Test Fails (Here It Always Passes),
		// Keep If Test Passes But Depth-Test Fails, Zero If Test Passes
		heightmap_auto.draw_terrain(gl);
		
		
		// Draw ReflectED Objects
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		gl.glColorMask(true,true,true,true);
		gl.glStencilFunc(GL2.GL_LEQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
		gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);	// Don't Change
		
		gl.glPushMatrix();
		gl.glScalef(1.0f, -1.0f, 1.0f);			// Mirror Y Axis
		
		gl.glEnable(GL2.GL_CLIP_PLANE0);		// Enable Clip Plane For Removing Artifacts (When The Object Crosses The Floor)
		double eqr[] = {0.0f,1.0f, 0.0f, 0.0f};
		gl.glClipPlane(GL2.GL_CLIP_PLANE0, eqr, 0);	// Equation For Reflected Objects
		drawScene(gld,true,false);
		heightmap_auto.draw_terrain(gl);
		sun.draw(gld,0,0,0,0,0,0);
		gl.glPopMatrix();
		gl.glDisable(GL2.GL_CLIP_PLANE0);
		
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		gl.glStencilFunc(GL2.GL_EQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
		gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);	// Don't Change
		
		// THE REFLECTING REGION GETS BLENDED
		heightmap_auto.water.draw(gld,0,0,0,0,0,0);
		
		
		// DISPLAY THE REST BUT DONT TOUCH THE REFLECT :)
		//gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glStencilFunc(GL2.GL_NOTEQUAL, 2, 0xffffffff);// We Draw Only Where The Stencil Is 1
		gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);	// Don't Change
		heightmap_auto.draw_terrain(gl);
		terrain_found = true;
		heightmap_auto.water.draw(gld,0,0,0,0,0,0);
		gl.glDisable(GL2.GL_STENCIL_TEST);
		
		
		/********************************************************************************************/			
	}
	
	
	public void click(MouseEvent e) {
		if(!terrain_found) System.out.println("Warning : picking is unstable without a terrain !");
		this.click_event=e;
		this.xMouse = e.getX();
		this.yMouse = e.getY();
		this.mode = GraphicsEngine.MODE_PICKING;
	}
	
	public void click_spell(MouseEvent e) {
		this.xMouse = e.getX();
		this.yMouse = e.getY();
		this.mode = GraphicsEngine.MODE_PICKING;
		this.spell_casting = true;
	}
	
	
	private void gestionPicking(MouseEvent e, GL2 gl, GLU glu) {
		glu.gluLookAt(mcx+camX, mcy+camY, mcz+camZ, mcx+lookX, mcy+lookY, mcz+lookZ, 0,1,0);
		double[] modelview = new double[16];
		double[] projection = new double[16];
		int[] viewport = new int[4];
		
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelview, 0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0);
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		
		
		double winX = xMouse;
		double winY = yMouse;
		winY = (double)viewport[3] - (double)winY;
		FloatBuffer winZ = FloatBuffer.allocate(1);
		gl.glReadPixels ((int)winX, (int)winY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, winZ);
		//winZ[0]=100;
		double[] obj = new double[3];
		//System.out.print("mouse pos: "+winX +" "+winY+" "+winZ[0]);    	  
		
		glu.gluUnProject(
				winX, winY, winZ.get(0),
				modelview, 0,
				projection, 0,
				viewport, 0,
				obj, 0
		);
		
		//if(DEBUG) System.out.print("   3Dclik: "+(int)objX[0] + " " + (int)objY[0] + " " + (int)objZ[0]);
		
		//intersecX=(mcx+camX)+(camY/(camY-objY[0]))*(objX[0]-(mcx+camX));
		//intersecZ=(mcz+camZ)+(camY/(camY-objY[0]))*(objZ[0]-(mcz+camZ));
		
		intersecX = obj[0];
		intersecY = obj[1];
		intersecZ = obj[2];
		
		if(DEBUG) System.out.println("   intersecX: "+(int)intersecX+" intersecZ: "+(int)intersecZ);
		
		this.click_event_pt=new Point((int)intersecX,(int)intersecZ);
		
		if(spell_casting){
			if(active_spell!=null)
				active_spell.setPosition(click_event_pt);
			click_event_pt=null;
			spell_casting=false;
		}
	}
	
//	obsolete
	/*
	 public void processSelection(GLDrawable drawable, float x, float y) {
	 GL gl = drawable.getGL();
	 GLU glu = drawable.getGLU();
	 int[] selectBuff = new int[64];
	 int hits = 0;
	 int[] viewport = new int[4];
	 
	 gl.glSelectBuffer(64, selectBuff);
	 gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
	 gl.glMatrixMode(GL2.GL_PROJECTION);
	 gl.glPushMatrix();
	 
	 gl.glRenderMode(GL2.GL_SELECT);
	 gl.glLoadIdentity();
	 glu.gluPickMatrix(x, viewport[3] - y, 5, 5, viewport);
	 glu.gluPerspective(30.0f, fAspect, 1.0, 50);
	 draw(drawable);
	 hits = gl.glRenderMode(GL2.GL_RENDER);
	 if (hits > 0) {
	 int count = selectBuff[0];
	 pickedName = selectBuff[3];
	 System.out.println(count + " " + pickedName);
	 
	 } else {
	 System.out.println("no hit");
	 pickedName = 0;
	 }
	 
	 gl.glMatrixMode(GL2.GL_PROJECTION);
	 gl.glPopMatrix();
	 gl.glMatrixMode(GL2.GL_MODELVIEW);
	 picked = false;
	 
	 } */
	
	
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) 
	{
		window_width=w;
		window_height=h;
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glViewport(x, y, window_width, window_height);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		aspect = (float)((float) window_width / (float) window_height);
		new GLU().gluPerspective(45, aspect, 1f, view_distance);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); 
		
		mouseHelper.setGLGLU(gl);
	}
	
	public void setDisplayList(CubeTree t){
		this.cubetree = t;
	}
	
	public void setDisplayList(Set<Rep> liparam){
		// appel� par Martin: physicsRep
		
		// avoid adding 2 or more terrains(and borders) to the scene
		/*
		 boolean terrain_exist = false;
		 boolean border_exist = false;
		 for(Rep gr : liparam){
		 if(gr.getGraphics().getFormat()==GraphicalRep.FORMAT_TERRAIN){
		 terrain_exist = true;
		 break;
		 }
		 if(gr.getGraphics().getFormat()==GraphicalRep.FORMAT_BORDER){
		 border_exist = true;
		 break;
		 }
		 
		 }
		 
		 if(!terrain_exist && !border_exist)
		 */
		this.li=liparam;
		/*
		 else{
		 System.out.println("setDisplayList ne doit pas passer de Terrain ou de Border! (c'est deja cree en interne)");
		 System.out.println("System halted.");
		 System.exit(0);
		 }	
		 */
	}
	
	public void debugsetDisplayList(LinkedList<GraphicalRep> lidparam) {
		//avoid adding 2 or more terrains(and borders) to the scene
		boolean terrain_exist = false;
		boolean border_exist = false;
		for(GraphicalRep gr : lidparam){
			if(gr.getFormat()==GraphicalRep.FORMAT_TERRAIN){
				terrain_exist = true;
				break;
			}
			if(gr.getFormat()==GraphicalRep.FORMAT_BORDER){
				border_exist = true;
				break;
			}
			
		}
		if(!terrain_exist && !border_exist)
			this.lid=lidparam;
		else{
			System.out.println("setDisplayList ne doit pas passer de Terrain ou de Border! (c'est déjà créé en interne)");
			System.out.println("System halted.");
			System.exit(0);
		}	
	}
	
	
	public void displayChanged(GLDrawable arg0, boolean arg1, boolean arg2) {
	}
	
	public static void cast_spell(){
		if(active_spell!=null){
			active_spell.reset();
			System.out.println("cast!");
		}
	}
	
	
	public void FPSHelper() {
		actual_fps = fpscount;
		fpscount=0;
	}
	
	public void info_sleeptime(int slp){
		this.slept = slp;
	}
	
	private void particles_tick(){
		if(DEBUG){
			for(GraphicalRep grdt : lid){
				if(grdt instanceof Particles){
					((Particles)grdt).tick();
					active_spell = (Particles_Ring)grdt;
				}
			}
		}else{
			for(Rep grt : li){
				if(grt instanceof Particles){
					((Particles)grt).tick();
				}
			}
		}
	}
	
	public static void goFullScreen(){
		
		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if(device.isFullScreenSupported()){ //FS EXCLUSIVE MODE
			frame.dispose();
			frame = new JFrame();
			frame.setSize(new Dimension(1024,768));
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame); //frame renders in FSE Mode
			frame.add(glc);
			frame.setVisible(true);
			DisplayMode[] dp = device.getDisplayModes();
			try {
				BufferedReader fr = new BufferedReader(new FileReader("config.ini"));
				int cwidth = Integer.parseInt(fr.readLine());
				int cheight = Integer.parseInt(fr.readLine());
				int cbpp = Integer.parseInt(fr.readLine());
				int cfreq = Integer.parseInt(fr.readLine());
				fr.close();
				boolean screenmode_available = false;
				for(int i=0;i<dp.length;i++){
					if(dp[i].getWidth()==cwidth && dp[i].getHeight()==cheight
							&& dp[i].getBitDepth()==cbpp && dp[i].getRefreshRate()==cfreq){
						device.setDisplayMode(dp[i]);
						screenmode_available = true;
						break;
					}
				}
				if(!screenmode_available){
					Error.println("Check your config.ini because this screen mode is NOT supported by your system !");
					System.exit(0);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Error.println("File not found (config.ini) !");
				System.exit(0);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Error.println("Error parsing config.ini !");
				System.exit(0);
			} catch (IOException e) {
				Error.println("Error reading config.ini !");
				System.exit(0);
				e.printStackTrace();
			}
			
		}
	}
	
	public static void goWindowed(){
		frame.setUndecorated(false);
		device.setFullScreenWindow(null);
	}
	
	
	public void run() {
		fpsHelper.tick();
		heightmap_auto.water.tick();
		mouseHelper.tick();
		keyboardHelper.tick();
		particles_tick();
		glc.display();
		//Thread.yield();
		//glj.repaint();
		
		if(!DEBUG && click_event!=null && click_event_pt!=null){
			main.getPhysicsEngine().setClick(click_event, click_event_pt);
			click_event=null;
			click_event_pt=null;
			frustum_just_changed=true;
		}
		if(keyboardHelper.create_blood_test)
			setBlood(mainChar);
	}
	
	public void setBlood(CharacterRep who){
		if(who.getGraphics() instanceof MD2){
			((MD2)(who.getGraphics())).Blood(100);
		}else{
			System.out.println("warning:GraphicsEngine.setBlood parameter must be of type MD2!");
		}
	}
	
	public Main getMain(){
		return main;
	}
	
	public void setView_Distance(float howmuch){
		view_distance = howmuch;
	}
	
	public GLCanvas getGLCanvas() {
		return glc;
	}
//	public GLJPanel getGLJPanel() {
//	return glj;
//	}
	
	public void setLeftBorderAction() {
		this.setLeftBorderAction=true;
	}
	public void setRightBorderAction() {
		this.setRightBorderAction=true;
	}
	public void setStopBorderAction() {
		this.setLeftBorderAction=false;
		this.setRightBorderAction=false;
	}
	public void setMainChar(HeroRep mainCharP) {
		mainChar = mainCharP;
	}
	public void setHeightMap(float[][] map, int map_size){
		heightmap_auto.setHeightMap_fromPhysical(map, map_size);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
