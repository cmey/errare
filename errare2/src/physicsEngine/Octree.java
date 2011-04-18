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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import geom.AABox;
import geom.Point;
import geom.Triangle;

/**
 * Implements an octree. See http://en.wikipedia.org/wiki/Octree if you don't know what an octree is ;)
 * 
 * @author trueblue
 *
 */
public class Octree implements Serializable{
	
	public static final int MAX_TRIANGLES_PER_NODE=20;
	public static final float BOUNDING_BOX_OVERLAPING_RATIO=0.05f;
	
	private AABox boundingBox;
	
	private transient Octree[] children;
	
	private transient Octree parent;
	
	private int level;
	
	private ArrayList<PhysicalRep> content;
	
	private ArrayList<Triangle> triangles;
	
	private int selfAndDescendantContentCount;
	
	private int selfAndDescendantTrianglesCount;
	
	/**
	 * Creates an octree.
	 * @param c AABox surrounding the octree.
	 * @param parent Octree which is the parent of the created octree.
	 * @param nbLevels Number of recursive levels of the octree.
	 */
	public Octree(AABox c, Octree parent, int nbLevels) {
		this.boundingBox=c;
		
		content = new ArrayList<PhysicalRep>();
		selfAndDescendantContentCount = 0;
		
		triangles = new ArrayList<Triangle>();
		selfAndDescendantTrianglesCount = 0;
		
		this.parent=parent;
		if(parent==null)
			level=0;
		else {
			this.level=parent.getLevel()+1;
		}
		
		if(nbLevels>0) {
			nbLevels--;
			createChildren(nbLevels);
		}
	}
	

	private void createChildren(int nbLevels) {
		Point origin = boundingBox.getLeftFrontBottom();
		Point end = boundingBox.getRightBackTop();
		float xBBSize = boundingBox.getRightBackTop().x-origin.x;
		float yBBSize = boundingBox.getRightBackTop().y-origin.y;
		float zBBSize = boundingBox.getRightBackTop().z-origin.z;
		
		float bBSize = Math.max(Math.max(xBBSize, yBBSize), zBBSize);
		float halfBBSize = bBSize/2 + bBSize*BOUNDING_BOX_OVERLAPING_RATIO;
		
		children = new Octree[8];
		
		children[0] = new Octree(new AABox(new Point(origin.x, origin.y, origin.z), 
				new Point(origin.x+halfBBSize, origin.y+halfBBSize, origin.z+halfBBSize)), this, nbLevels);
		
		children[1] = new Octree(new AABox(new Point(origin.x, end.y-halfBBSize, origin.z), 
				new Point(origin.x+halfBBSize, origin.y+bBSize, origin.z+halfBBSize)), this, nbLevels);
		
		
		children[2] = new Octree(new AABox(new Point(end.x-halfBBSize, end.y-halfBBSize, origin.z), 
				new Point(origin.x+bBSize, origin.y+bBSize, origin.z+halfBBSize)), this, nbLevels);
		
		
		children[3] = new Octree(new AABox(new Point(end.x-halfBBSize, origin.y, origin.z), 
				new Point(origin.x+bBSize, origin.y+halfBBSize, origin.z+halfBBSize)), this, nbLevels);
		
		
		children[4] = new Octree(new AABox(new Point(origin.x, origin.y, end.z-halfBBSize), 
				new Point(origin.x+halfBBSize, origin.y+halfBBSize, origin.z+bBSize)), this, nbLevels);
		
		
		children[5] = new Octree(new AABox(new Point(origin.x, end.y-halfBBSize, end.z-halfBBSize), 
				new Point(origin.x+halfBBSize, origin.y+bBSize, origin.z+bBSize)), this, nbLevels);
		
		
		children[6] = new Octree(new AABox(new Point(end.x-halfBBSize, end.y-halfBBSize, end.z-halfBBSize), 
				new Point(origin.x+bBSize, origin.y+bBSize, origin.z+bBSize)), this, nbLevels);
		
		
		children[7] = new Octree(new AABox(new Point(end.x-halfBBSize, origin.y, end.z-halfBBSize), 
				new Point(origin.x+bBSize, origin.y+halfBBSize, origin.z+bBSize)), this, nbLevels);
	}
	
	/**
	 * Returns the bounding box surounding the octree.
	 * @return the bounding box surounding the octree
	 */
	public AABox getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Returns the size of the edges of the bounding box which surrounds the octree.
	 * @return the size of the edges of the bounding box which surrounds the octree
	 */
	/*public float getBoundingBoxSize() {
		return boundingBoxSize;
	}*/
	
	/**
	 * Returns the octree's children array.
	 * @return the octree's children array
	 */
	public Octree[] getChildren() {
		return children;
	}
	
	/**
	 * Returns the PhysicalReps contained in the current node of the octree.
	 * @return the PhysicalReps contained in the current node of the octree
	 */
	public ArrayList<PhysicalRep> getContent() {
		return content;
	}
	
	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}
	
	/**
	 * Returns the PhysicalReps contained in the current node of the octree and it's descendants.
	 * @return the PhysicalReps contained in the current node of the octree and it's descendants
	 */
	public ArrayList<PhysicalRep> getSelfAndDescendantContent() {
		return getSelfAndDescendantContent(new ArrayList<PhysicalRep>(getSelfAndDescendantContentCount()));
	}
	
	private ArrayList<PhysicalRep> getSelfAndDescendantContent(ArrayList<PhysicalRep> l) {
		l.addAll(content);
		if(children!=null) {
			for(Octree child : children) {
				child.getSelfAndDescendantContent(l);
			}
		}
		return l;
	}
	
	/**
	 * Returns the number of PhysicalReps contained in the current node of the octree and it's descendants.
	 * @return the number of PhysicalReps contained in the current node of the octree and it's descendants
	 */
	public int getSelfAndDescendantContentCount() {
		return selfAndDescendantContentCount;
	}
	
	public int getSelfAndDescendantTrianglesCount() {
		return selfAndDescendantTrianglesCount;
	}
	
	/**
	 * Adds a PhysicalRep in the octree. The PhysicalRep is propagated down the octree until its size exceeds the size of the current node.
	 * @param pr PhysicalRep added to the octree
	 * @return true if the PhysicalRep fits in the current node or one of its descendants, false otherwise
	 */
	public boolean addContent(PhysicalRep pr) {
		if(boundingBox.contains(pr)) {
			selfAndDescendantContentCount++;
			if(children==null) {
				content.add(pr);
				pr.setOctree(this);
			}
			else {
				boolean added = false;
				for(Octree child : children) {
					if(child.addContent(pr)) {
						added=true;
						break;
					}
				}
				if(!added) {
					content.add(pr);
					pr.setOctree(this);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean addTriangle(Triangle tri) {
		if(boundingBox.contains(tri)) {
			selfAndDescendantTrianglesCount++;
			if(children==null) {
				triangles.add(tri);
				if(triangles.size()>MAX_TRIANGLES_PER_NODE) {
					createChildren(0);
					ArrayList<Triangle> triList = triangles;
					triangles = new ArrayList<Triangle>();
					for(Triangle t : triList) {
						addTriangle(t);
					}
				}
			}
			else {
				boolean added = false;
				for(Octree child : children) {
					if(child.addTriangle(tri)) {
						added=true;
						break;
					}
				}
				if(!added) {
					triangles.add(tri);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	

	private void decrementselfAndDescendantContentCount() {
		selfAndDescendantContentCount--;
		if(parent!=null) {
			parent.decrementselfAndDescendantContentCount();
		}
	}
	
	/**
	 * Removes a PhysicalRep from the current node. If the PhysicalRep is not contained in the node, it does nothing.
	 * @param pr PhysicalRep to remove
	 */
	public void removeContent(PhysicalRep pr) {
		content.remove(pr);
		decrementselfAndDescendantContentCount();
	}
	
	private void addMovedContent(PhysicalRep pr) {
		if(!boundingBox.contains(pr)) {
			selfAndDescendantContentCount--;
			if(parent!=null)
				parent.addMovedContent(pr);
			else {
				selfAndDescendantContentCount++;
				content.add(pr);
				pr.setOctree(this);
			}
		}
		else {
			content.add(pr);
			pr.setOctree(this);
		}
	}
	
	/**
	 * Moves a PhysicalRep in the octree.
	 * @param pr PhysicalRep which coordinates have changed
	 */
	public void moveContent(PhysicalRep pr) {
		if(!boundingBox.contains(pr) && parent!=null) {
			content.remove(pr);
			selfAndDescendantContentCount--;
			parent.addMovedContent(pr);
		}
		else {
			if(children!=null) {
				boolean added=false;
				
				for(Octree child : children) {
					if(child.addContent(pr)) {
						added=true;
						break;
					}
				}
				if(added) {
					content.remove(pr);
				}
			}
		}
		
	}
	
	/**
	 * Sets the parent of the current node.
	 * @param tree
	 */
	public void setParent(Octree tree) {
		parent=tree;
	}
	
	/**
	 * Returns the level of the current node. The root has for level number 0. It's children have 1, etc.
	 * @return the level of the current node
	 */
	public int getLevel() {
		return level;
	}
	
	
	/**
	 * Returns the parent node of the current node.
	 * @return the parent node
	 */
	public Octree getParent() {
		return parent;
	}


	public boolean hasChildren() {
		return children!=null;
	}
	
}
