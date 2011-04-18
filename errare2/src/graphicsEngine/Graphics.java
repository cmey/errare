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

import java.util.Hashtable;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import logger.Logger;

/**
 * The super class of all 3D objects. Essentially contains the texture loading management.
 * @author Cyberchrist
 */
public abstract class Graphics{
	
	transient private static Hashtable<Integer,Graphics> reptable = new Hashtable<Integer,Graphics>(); // links numbers to reps
	transient private static int ID=0; // number of graphicalReps
	private int localID; // this graphicalRep's number
	static protected GraphicsEngine ge; // currently all graphical reps share a single graphics engine
	transient protected boolean prem; // initialisation time?
        transient protected boolean isDisplayListCreated; // display list already created?
	transient static protected GLUquadric quadric = new GLU().gluNewQuadric();
	protected static int quadtree_hack_loopnumber;
	protected int quadtree_hack_objnumber;
	
		
	/**
	 * Loads a file into his representation in memory.
	 * Autodetects the format of the file and loads it the appropriate way.
	 * Note for models: the file must be accompagnated by the texture files with the same name!
	 * i.e. If you want to load the file "~/terminator1.md2" it should almost certainly come with the texture "~/terminator1_1.jpg"
	 * @param filename path to file
	 */
	public Graphics() {
		this.init();
		prem=true;
		
		if(GraphicsEngine.DEBUG) Logger.printINFO("new graphicalRep of ID "+localID);
	}

	private void initID(){
		localID=ID;
		reptable.put(localID,this);
		ID++;
	}

	protected void init(){
		this.initID();
	}

	
	/**
	 * Translate and rotate the model.
	 * @param gl OpenGL context
	 * @param px translate over x
	 * @param py translate over y
	 * @param pz translate over z
	 * @param rx rotate over x axis
	 * @param ry rotate over y axis
	 * @param rz rotate over z axis
	 */
	protected void doPosition(GL gl, float px, float py, float pz, float rx, float ry, float rz){
		gl.glTranslatef(px, py, pz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
	}
	
	
	transient private float modelview[] = new float[16];
	
	/**
	 * Activate billboard state of this model.
	 * Simply PushMatrix before calling this, and pop after the draw.
	 * @param gl
	 */
	protected void Billboard(GL gl){
		/*********** BILLBOARDING ************/
		gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, modelview,0);
		int i, j;
		for( i=0; i<3; i++ ) 
			for( j=0; j<3; j++ ) {
				if ( i==j )
					modelview[i*4+j] = 1.0f;
				else
					modelview[i*4+j] = 0.0f;
			}
		gl.glLoadMatrixf(modelview,0);
		/********** END OF BILLBOARDING *******/
	}
	
	
	
	
	
	
	
	
	/**
	 * Generic method that draws an object to the screen (must be redefined in extended class)
	 * @param gl gl context
	 */
	protected abstract void draw(GLAutoDrawable gld);
	
	protected abstract void drawGeometryOnly(GLAutoDrawable gld);
	
	protected abstract void drawByTriangles(GLAutoDrawable gld);
	
	/*protected void drawInLoop(GLAutoDrawable gld,float px, float py, float pz, float rx, float ry, float rz){
		if(this.quadtree_hack_objnumber != quadtree_hack_loopnumber){
			if(GraphicsEngine.gl_selection_mode)
				drawByTriangles(gld,px,py,pz,rx,ry,rz);
			else
				draw(gld,px,py,pz,rx,ry,rz);
			this.quadtree_hack_objnumber = quadtree_hack_loopnumber;
		}
	}*/
	
	
	
	/********************************* GETTERS SETTERS *********************************/
	public int getID(){
		return this.localID;
	}
	
	public String toString(){
		return "Graphics";
	}
	
	public static void setEngine(GraphicsEngine engine){
		ge = engine;
	}
	
}
