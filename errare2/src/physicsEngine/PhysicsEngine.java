/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package physicsEngine;

import genericEngine.Engine;
import geom.AABox;
import geom.Point;
import geom.Triangle;
import geom.Vector;
import graphicsEngine.Camera;
import graphicsEngine.GraphicsEngine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import logger.Logger;
import main.ClientMain;

import org.w3c.dom.Node;

import userInputEngine.UserInputController;

public class PhysicsEngine implements Engine {
	
	public static final int WORLD_SIZE=300;
        public static final boolean DEBUG = false;
	private Octree octree;
        private ClientMain main;
        private GraphicsEngine graphicsEngine;
        private PhysicalRep mainChar;

	private boolean forwardKeyPressed;
	private boolean backwardKeyPressed;
	private boolean leftKeyPressed;
	private boolean rightKeyPressed;
	private boolean jumpKeyPressed;

        // reps to check against for collision
        // TODO: only adds into this? memory leak here?
	private HashSet<PhysicalRep> movingReps;
        

        
	/**
         * Used by the normal game
         * @param main
         */
	public PhysicsEngine(ClientMain main) {
		this.main=main;
		registerKeys(this.main.getUserInputController());
		graphicsEngine = main.getGraphicsEngine();
                movingReps = new HashSet<PhysicalRep>();
	}
	
        
        /**
         * Used by Test in PhysicsEngine
         * @param userInputController
         * @param ge
         */
	public PhysicsEngine(UserInputController userInputController, GraphicsEngine ge) {
		registerKeys(userInputController);
		graphicsEngine = ge;
                movingReps = new HashSet<PhysicalRep>();
	}
	
        
        /**
         * When we know the size of the world, create the octree structure for
         * the world
         * TODO: do not use nblevels anymore, the number of levels in the
         * octree structure should be dynamic, based on the number of
         * triangles in one AABox (~ 1000 triangles ?)
         * @param x 000 corner of the world
         * @param y 000 corner of the world
         * @param z 000 corner of the world
         * @param size length (in every dimension because the octree is forced to be a box)
         * @param nblevels number of hierarchies for the octree subdivisions
         */
        public void createOctree(float x, float y, float z, float size, int nblevels) {
                // no parent
		octree = new Octree(new AABox(new Point(x, y, z), new Point(x+size, y+size, z+size)), null, nblevels);
	}
	
        
        /**
         * Binds keyboard keys to actions
         * @param userInputControler
         */
	private void registerKeys(UserInputController userInputControler) {
		userInputControler.register(this, "left_pressed");
		userInputControler.register(this, "right_pressed");
		userInputControler.register(this, "forward_pressed");
		userInputControler.register(this, "backward_pressed");
		userInputControler.register(this, "jump_pressed");
		
		userInputControler.register(this, "left_released");
		userInputControler.register(this, "right_released");
		userInputControler.register(this, "forward_released");
		userInputControler.register(this, "backward_released");
		userInputControler.register(this, "jump_released");
	}
	
	
        /**
         * Place a model in the octree.
         * Optionally also his triangles, for collision.
         * @param rep the model to place in the octree
         * @param triangles the triangles of the model for collision to place in the octree (use only for moving objects)
         * @throws java.lang.IllegalArgumentException
         */
	public void add(PhysicalRep rep, Triangle[] triangles) throws IllegalArgumentException {
		if(!octree.addContent(rep))
			throw new IllegalArgumentException("Model does not fit in octree");
		rep.setPhysicsEngine(this);
		if(triangles!=null) {
			for(Triangle t : triangles) {
				if(!octree.addTriangle(t))
					throw new IllegalArgumentException("Model does not fit in octree");
			}
		}
	}
	
        
        /**
         * Apply a force vector to a model
         * @param rep the model
         * @param param the vector of force to apply
         */
	public void addMove(PhysicalRep rep, Vector param) {
		rep.addMove(param);
		movingReps.add(rep);
	}
	
        
        /**
         * Apply a jump to a model
         * @param rep the model
         * @param power
         */
	public void addJump(PhysicalRep rep, float power) {
        boolean isNowMoving = rep.addJump(power);
		if(isNowMoving)
			movingReps.add(rep);
	}
	
	
        /**
         * Receive the list of triangles, extracted from the octree,
         * that are inside or in contact with the given model
         * @param rep the model
         * @return
         */
	public ArrayList<Triangle> getOverlapingTriangles(PhysicalRep rep) {
		ArrayList<Triangle> triList = new ArrayList<Triangle>();
		/*Octree octree = rep.getOctree();
		while(octree!=null) {
			for(Triangle tri : octree.getTriangles()) {
				if(tri.overlap(rep)) {
					triList.add(tri);
				}
				
			}
			octree = octree.getParent();	
		}
		getOverlapingTrianglesHelper(rep, rep.getOctree(), triList);*/
		getOverlapingTriangles_recurs(rep, octree, triList);
                return triList;
	}
	
        
        /**
         * Complete the list of triangles, extracted from the given octree cube,
         * that are inside or in contact with the given model
         * @param rep the model
         * @param tree the octree cube to scan
         * @param triList the list of triangles to complete
         */
        private void getOverlapingTriangles_recurs(PhysicalRep rep, Octree tree, List<Triangle> triList) {
		if(tree.getBoundingBox().contains(rep) || tree.getBoundingBox().intersects(rep)) {
			for(Triangle tri : tree.getTriangles()) {
				if(tri.overlap(rep)) {
					triList.add(tri);
				}
			}

			if(tree.hasChildren()) {
				for(Octree child : tree.getChildren())
					if(child.getSelfAndDescendantTrianglesCount()>0)
						getOverlapingTriangles_recurs(rep, child, triList);
			}
		}
	}

        
        /**
         * Here should be done all heavy calculations.
         */
	public void run() {
            // do actions based on keys that were pressed
            processKeys();
            // apply movement to moving models and check for collision
            processMoves();
	}


        /**
         * Apply movement to moving models and check for collision
         */
	private void processMoves() {
		for(Iterator<PhysicalRep> it = movingReps.iterator(); it.hasNext();) {
            boolean isStillMoving = it.next().applyMovements();
			if(!isStillMoving) {
                // removes from movingReps
                //it.remove(); // TODO: memory leak
			}
		}
	}

        
        /**
         * Sets the player's model.
         * @param mainChar the unique model that represents the player
         * and that the player can play
         */
	public void setMainChar(PhysicalRep mainChar) {
		this.mainChar = mainChar;
		mainChar.getRep().getGraphicalRep().setAnimationFPS(20);
	}

        
        /**
         * Invoked by keyboard strokes by the player.
         * @param action string-encoded action that was triggered
         * @return true if this event is considered as consummed,
         * false otherwise which means this permits the next engine in the list
         * to receive this event too.
         */
	public boolean invokeKeyEvent(String action) {
		if(PhysicsEngine.DEBUG) Logger.printINFO(action);
		
		if(action.equals("forward_pressed"))
			forwardKeyPressed = true;
		else if(action.equals("backward_pressed"))
			backwardKeyPressed = true;
		else if(action.equals("left_pressed"))
			leftKeyPressed = true;
		else if(action.equals("right_pressed"))
			rightKeyPressed = true;
		else if(action.equals("jump_pressed"))
			jumpKeyPressed = true;
		
		else if(action.equals("forward_released"))
			forwardKeyPressed = false;
		else if(action.equals("backward_released"))
			backwardKeyPressed = false;
		else if(action.equals("left_released"))
			leftKeyPressed = false;
		else if(action.equals("right_released"))
			rightKeyPressed = false;
		else if(action.equals("jump_released"))
			jumpKeyPressed = false;
		
		return true;
	}
	
	private void processKeys() {
        float coef;
		if(mainChar.isInTheAir())
            coef = 0.05f;
        else
            coef = 0.5f;
			
			if(forwardKeyPressed) {
				Camera cam = graphicsEngine.world.cam;
				Vector move = new Vector(cam.getLocation(), cam.getLookingAt());
				mainChar.setLookingDirection(move);
                                
				move.y=0;
				move.normalize();
				move.mult(coef);
				//move.y=2;
				addMove(mainChar, move);
			}
			if(backwardKeyPressed) {
				Camera cam = graphicsEngine.world.cam;
				Vector move = new Vector(cam.getLookingAt(), cam.getLocation());
				mainChar.setLookingDirection(Vector.mult(move, -1));
				
				move.y=0;
				move.normalize();
				move.mult(coef);
				//move.y=2;
				addMove(mainChar, move);
			}

			if(rightKeyPressed) {
				Camera cam = graphicsEngine.world.cam;
				Vector move = new Vector(cam.getLocation(), cam.getLookingAt());
				mainChar.setLookingDirection(move);
				
				move.y=0;
				move = Vector.crossProduct(move, new Vector(0, 1, 0));
				move.normalize();
				
				move.mult(coef);
				//move.y=2;
				addMove(mainChar, move);
			}
			if(leftKeyPressed) {
				Camera cam = graphicsEngine.world.cam;
				Vector move = new Vector(cam.getLocation(), cam.getLookingAt());
				mainChar.setLookingDirection(move);
				
				move.y=0;
				move = Vector.crossProduct(move, new Vector(0, 1, 0));
				move.normalize();
				
				move.mult(-coef);
				//move.y=2;
				addMove(mainChar, move);
			}

			if(jumpKeyPressed) {
				addJump(mainChar, PhysicalRep.JUMPPOWER);
			}
	}
	

	//unused
	public boolean invokeMouseEvent(String action, int x, int y) {
		return true;
	}

	public void quit() {
	}

        
        /**
         * Loads an object from an xml node describing the position,
         * the size, the orientation, etc... of the object.
         * @param node
         * @return
         */
	public PhysicalRep loadObject(Node node) {
		
		float x=0, y=0, z=0, xsize=0, ysize=0, zsize=0;
		
		for(int i=0; i<node.getChildNodes().getLength(); i++) {
			Node child = node.getChildNodes().item(i);
			if(child.getNodeType()==Node.ELEMENT_NODE) {
				if(child.getNodeName().equalsIgnoreCase("x")) {
					x = Float.parseFloat(child.getTextContent());
				}
				else if(child.getNodeName().equalsIgnoreCase("y")) {
					y = Float.parseFloat(child.getTextContent());
				}
				else if(child.getNodeName().equalsIgnoreCase("z")) {
					z = Float.parseFloat(child.getTextContent());
				}
				else if(child.getNodeName().equalsIgnoreCase("xsize")) {
					xsize = Float.parseFloat(child.getTextContent());
				}
				else if(child.getNodeName().equalsIgnoreCase("ysize")) {
					ysize = Float.parseFloat(child.getTextContent());
				}
				else if(child.getNodeName().equalsIgnoreCase("zsize")) {
					zsize = Float.parseFloat(child.getTextContent());
				}
			}
		}
		
		if(PhysicsEngine.DEBUG) Logger.printINFO(x+" "+y+" "+z+" "+xsize+" "+ysize+" "+zsize);
		PhysicalRep prep = new PhysicalRep(new Point(x, y, z), new Point(x+xsize, y+ysize, z+zsize));
		
		return prep;
	}
	
        
        public Octree getOctree() {
		return octree;
	}
	
}
