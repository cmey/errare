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

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

import main.Rep;
import physicsEngine.CharacterPRep;
import physicsEngine.PhysicalRep;


/**
 * The super class of all 3D objects. Essentially contains the texture loading management.
 * @author Cyberchrist
 */
public abstract class GraphicalRep{
	
	public static boolean DEBUG = true;
	protected String mesh_filename;
	protected String texture_filename;
	protected boolean isAlphaTextured;
	private boolean png_texture;
	protected int format; /* 1=.md2  2=.errare  3=.3ds*/
	public static final int FORMAT_MD2 = 1;
	public static final int FORMAT_3DS = 3;
	public static final int FORMAT_ERRARE = 2;
	public static final int FORMAT_BORDER = 4;
	public static final int FORMAT_PARTICLE = 6;
	public static final int FORMAT_SUN = 7;
	public static final int FORMAT_TEXT = 8;
	public static final int FORMAT_SHADOWMAP = 9;
	public static final int FORMAT_TERRAIN = 5;
	public static final int FORMAT_WATER = 10;
	public static final int FORMAT_FLOOR = 11;
	public static final int FORMAT_GRASS = 12;
	public static final int FORMAT_LENSFLARE = 13;
	protected static int ID=0;
	protected int localID;
	CharacterPRep mainCharCR;
	Rep mainRep;
	PhysicalRep physicalRep;
	
	public static int quadtree_hack_loopnumber;
	private int quadtree_hack_objnumber;
	public boolean cast_shadow;
	public boolean cast_reflection;
	
	//Index lists for vertices
	protected int[] indices;
	protected int[] uvIndices;
	protected float[] uvs;
	protected AnimFrame interpolatedFrame;
	// Frame array
	protected AnimFrame[] frames;
	// Start and end frames
	protected int startFrame = 0;
	protected int endFrame = 0;
	// Current and next animation frame
	protected int curAnimFrame = 0;
	protected int nextAnimFrame = 1;
	// Animation timings
	protected long lastAnimTime = 0;
	// Animation FPS for number of interpolations
	protected int fps = 30;
	protected int tWidth; //texture size in width
	protected int tHeight;//texture size in height
	protected int[] internal_texture1; //index in video memory
	protected int[] internal_texture2; //index in video memory
	protected ByteBuffer texture1; //the image converted to a format compatible with OpenGL
	protected boolean prem;
	
	/* boundaries of current shape */
	protected Point3D sphereCenter;
	protected float radius;
	protected float verticeXmaximum, verticeXminimum;
	protected float verticeYmaximum, verticeYminimum;
	protected float verticeZmaximum, verticeZminimum;
	public float angle; /* of rotation of the current model */
	static private float modelview[] = new float[16];
	
	// Texture Collector : internal_index of texture + data
	static protected Hashtable<String, TextureCollectorItem> tc = new Hashtable<String, TextureCollectorItem>();
	protected class TextureCollectorItem{
		public int[] internal_index;
		public boolean isalpha;
		public ByteBuffer data;
		public int tWidth, tHeight;
	}
	
	// Mesh Collector : uv + uvindex + meshframes
	static protected Hashtable<String, MeshCollectorItem> mc = new Hashtable<String, MeshCollectorItem>();
	protected class MeshCollectorItem{
		public AnimFrame[] frames;
		public int[] indices;
		public int[] uvIndices;
		public float[] uvs;
		public AnimFrame interpolatedFrame;
		public float verticeXmaximum, verticeXminimum;
		public float verticeYmaximum, verticeYminimum;
		public float verticeZmaximum, verticeZminimum;
		public Point3D sphereCenter;
		public float radius;
	}
	protected class AnimFrame{
		public String name;
		public float[] vertices;
		public int [] normal;
		public boolean initialized;
		public int displaylist;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Loads a file into his representation in memory.
	 * Autodetects the format of the file and loads it the appropriate way.
	 * Note for models: the file must be accompagnated by a jpg texture file with the same name!
	 * i.e. If you want to load the file "~/model_001.md2" it must come with the texture "~/model_001.jpg"
	 * @param filename path to file
	 */
	public GraphicalRep(String filename, int FORMAT) {
		localID=ID;
		ID++;
		format=FORMAT;
		prem=true;
		if(DEBUG) System.out.println();
		
		//sera rempli au moment de l'"expand texture"
		internal_texture1 = new int[1];
		internal_texture2 = new int[1];
		
		load_texture(filename);
	}
	
	
	/**
	 * Basicaly Loads a texture into memory. 
	 * There is a helper: if it cannot find the file, it searches
	 * for the same basename but with a different extension.
	 * Memory and Speed Optimisation: if the texture has already been loaded in the past,
	 * it will use the that texture again (avoids loading the same file
	 * multiple times)
	 * @param filename physical name of the texture image
	 */
	public void load_texture(String filename){
		
		String ext = filename.substring(filename.length()-4,filename.length());
		if(ext.equals(".jpg") || ext.equals(".png")){
			load_texture2(filename);
		}else if(ext.equals(".md2")){
			filename = filename.substring(0,filename.length()-4);
			filename = filename.concat(".png");
			load_texture2(filename);
		}else{
			bad_texture("unsupported file extension");
		}
		
		if(texture1==null){System.out.println("texture load ERROR BADDDD!"); System.exit(-1);}
		
		
		if(DEBUG) System.out.println("Objet "+localID+" - "+"("+texture_filename+") (PNG:"+png_texture+") "+"(type "+format+") "+"(size T="+tWidth+" O="+verticeXmaximum+" "+verticeYmaximum+" "+verticeZmaximum+")");
	}
	
	
	private void load_texture2(String filename){
		texture_filename = filename;
		if(tc.containsKey(filename)){
			if(DEBUG) System.out.println("j'ai économisé un loadTexture !");
			// ensuite le premier objet qui passera par openGL sera "expanded"
			texture1 = tc.get(filename).data;
			tWidth = tc.get(filename).tWidth;
			tHeight = tc.get(filename).tHeight;
			isAlphaTextured = tc.get(filename).isalpha;
			if(texture1==null) bad_texture("je get un texture1 null!!");
		}else{				
			
			try {
				texture1 = loadTexture(filename);
				TextureCollectorItem texitem = new TextureCollectorItem();
				texitem.data = texture1;
				texitem.isalpha = isAlphaTextured;
				texitem.tWidth=tWidth;
				texitem.tHeight=tHeight;
				//je suis dans un etat ou j'ai les donnees en RAM mais je dois encore m'occuper
				//de les envoyer sur la carte graphique : "expand texture"
				tc.put(filename,texitem);
			} catch (IOException e) {
				bad_texture("Error reading texture!");
			}
			
		}
	}
	
	
	
	/**
	 * Handles texture loading errors.
	 * @param msg Some information about the error
	 */
	private void bad_texture(String msg){
		System.out.println("\nId:"+localID+" Texture ("+texture_filename+") criticaly not loaded : "+msg);
		System.exit(-1);
	}
	
	
	
	public void setPhysicalRep_pointer(PhysicalRep pr){
		physicalRep=pr;
	}
	public PhysicalRep getPhysicalRep(){
		return physicalRep;
	}
	
	protected void doPosition(GL2 gl, float px, float py, float pz, float rx, float ry, float rz){
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
	}
	
	protected void doExpandTexture(GL2 gl){
		if(!tc.containsKey(texture_filename)) {
			System.out.println("chris a fait une faute immonde lors du chargement de texture (from doExpandTexture)");
			System.exit(-1);
		}
		isAlphaTextured = tc.get(texture_filename).isalpha;
		
		if(tc.get(texture_filename).internal_index != null){
			if(DEBUG) System.out.println("j'ai économisé une expansion de texture! ");
			internal_texture1=tc.get(texture_filename).internal_index;
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
		}else{
			gl.glGenTextures(1, internal_texture1, 0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, internal_texture1[0]);
			
			if(isAlphaTextured)
				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, tWidth, tHeight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture1.rewind());
			else
				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, tWidth, tHeight, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture1.rewind());
			
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			
			//je fais un set par effet de bord
			tc.get(texture_filename).internal_index=internal_texture1;
		}			
	}
	
	
	static protected void Billboard(GL2 gl){
		/*********** BILLBOARDING ************/
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, modelview, 0);
		int i, j;
		for( i=0; i<3; i++ ) 
			for( j=0; j<3; j++ ) {
				if ( i==j )
					modelview[i*4+j] = 1.0f;
				else
					modelview[i*4+j] = 0.0f;
			}
		gl.glLoadMatrixf(modelview, 0);
		/********** END OF BILLBOARDING *******/
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Generic method that draws an object to the screen (must be redefined in extended class)
	 * Avoids drawing this object multiple times in the same render loop
	 * @param gl gl context
	 * @param px position on x axis
	 * @param py position on y axis
	 * @param pz position on z axis
	 * @param rx rotation on x axis
	 * @param ry rotation on y axis
	 * @param rz rotation on z axis
	 */
	protected abstract void draw(GLAutoDrawable gld,float px, float py, float pz, float rx, float ry, float rz);
	
	protected void drawInLoop(GLAutoDrawable gld,float px, float py, float pz, float rx, float ry, float rz){
		if(this.quadtree_hack_objnumber != quadtree_hack_loopnumber){
			draw(gld,px,py,pz,rx,ry,rz);
			this.quadtree_hack_objnumber = quadtree_hack_loopnumber;
		}
	}
	
	protected void draw(GLAutoDrawable gld){
		drawInLoop(gld,0,0,0,0,0,0);
	}
	
	/**
	 * Gives the bounds of the object's mesh. Respectively : x then z then y and respectively minimum
	 * then maximum. For a 2D rep, only the first four values are useful.
	 * @return t[0]=xmin , t[1]=xmax, t[2]=zmin, t[3]=zmax, t[4]=ymin, t[5]=ymax
	 */
	public float[] getPhysicalBounds() {
		float[] res = new float[6];
		res[0]=verticeXminimum;
		res[1]=verticeXmaximum;
		res[2]=verticeZminimum;
		res[3]=verticeZmaximum;
		res[4]=verticeYminimum;
		res[5]=verticeYmaximum;
		return res;
	}
	
	
	
	
	
	
	public String getCurrentAnimationName(){
		return null;
		//must be overriden
	}
	public void drawStencil(GL gl, int px, int py, int pz, int rx, int ry, int rz) {
		//must be overriden
	}
	public void startAnimation(String name) {
		//must be overriden
	}
	public void resetAnim(){
		//must be overriden 
	}
	public void LeftBorderAction(GL gl){
		//must be overriden
	}
	public void RightBorderAction(GL gl){
		//must be overriden
	}
	public LinkedList<String> getAnimationsNames() {
		//must be overriden
		return null;
	}
	
	
	
	
	
	/**
	 * Loads a texture file into memory (RAM)
	 * @param texture the global variable ByteBuffer I/O'ed (usable by openGL)
	 * @param fn filename of the texture. > ITS WIDTH AND HEIGHT MUST BE 2^x PIXELS ! (openGL usable format) <
	 * @return
	 * @throws IOException 
	 */
	private ByteBuffer loadTexture(String fn) throws IOException {
		if( fn.substring(fn.length()-4,fn.length()) .equals(".png"))
			png_texture=true;
		else
			png_texture=false;
		
		Texture tex = new Texture(fn);
		isAlphaTextured = tex.isAlpha;
		tWidth = tex.tWidth;
		tHeight = tex.tHeight;
		
		return tex.texturedata;
	}
	
	
	private boolean isPowerOfTwo(int v){
		if(Math.log(v)/Math.log(2) == (int)(Math.log(v)/Math.log(2))){
			return true;
		}else
			return false;
	}
	
	
	/*************************************** GETTERS ************************************/
	public int getFormat(){
		return format;
	}
	public int getID(){
		return this.localID;
	}
	public static String getStringFromFormat(int f){
		switch(f){
		case FORMAT_MD2:
			return "FORMAT_MD2";
		case FORMAT_3DS:
			return "FORMAT_3DS";
		case FORMAT_ERRARE:
			return "FORMAT_ERRARE";
		case FORMAT_BORDER:
			return "FORMAT_BORDER";
		case FORMAT_PARTICLE:
			return "FORMAT_PARTICLE";
		case FORMAT_SUN:
			return "FORMAT_SUN";
		case FORMAT_TEXT:
			return "FORMAT_TEXT";
		case FORMAT_SHADOWMAP:
			return "FORMAT_SHADOWMAP";
		case FORMAT_TERRAIN:
			return "FORMAT_TERRAIN";
		case FORMAT_WATER:
			return "FORMAT_WATER";
		case FORMAT_FLOOR:
			return "FORMAT_FLOOR";
		case FORMAT_GRASS:
			return "FORMAT_GRASS";
		default:
			return null;
		}
	}
	
	
	
	
	/*************************************** SETTERS ************************************/
	public void setRep(Rep mainRep){
		this.mainRep=mainRep;
	}
	public void setAngle(float angle){
		this.angle=angle;
	}
	
}
