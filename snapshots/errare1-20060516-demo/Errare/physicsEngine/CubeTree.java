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

import java.util.ArrayList;
import java.util.Hashtable;
import main.Rep;

public class CubeTree extends Cube{
	
	private CubeTree[] childs;
	
	private ArrayList<Cube> contents;
	private CubeTree parent;
	
	private int treeContentsCount;
	private boolean hasChilds;

	private int level;

	
	public CubeTree(Point3D leftFrontTop, Point3D rightBackBottom, float minsize, CubeTree parent, int level) throws Exception {
		super(leftFrontTop, rightBackBottom);

		
		if(Math.abs(leftFrontTop.x-rightBackBottom.x)<=minsize) {
			throw new Exception("Minsize reached");
		}
		
		this.level=level;
		this.parent=parent;
		treeContentsCount=0;
		childs=new CubeTree[8];
		hasChilds=true;
		contents = new ArrayList<Cube>();
		
		float currentsize = (Math.abs(leftFrontTop.x-rightBackBottom.x));
		float childsize = (currentsize/2);
		try {
			childs[0]=new CubeTree(new Point3D(leftFrontTop.x, leftFrontTop.y, leftFrontTop.z-childsize), 
					new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+childsize, leftFrontTop.z-currentsize), minsize, this, level+1);
			childs[1]=new CubeTree(new Point3D(leftFrontTop.x+childsize, leftFrontTop.y, leftFrontTop.z-childsize), 
					new Point3D(leftFrontTop.x+currentsize, leftFrontTop.y+childsize, leftFrontTop.z-currentsize), minsize, this, level+1);
			childs[2]=new CubeTree(new Point3D(leftFrontTop.x, leftFrontTop.y, leftFrontTop.z), 
					new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+childsize, leftFrontTop.z-childsize), minsize, this, level+1);
			childs[3]=new CubeTree(new Point3D(leftFrontTop.x+childsize, leftFrontTop.y, leftFrontTop.z), 
					new Point3D(leftFrontTop.x+currentsize, leftFrontTop.y+childsize, leftFrontTop.z-childsize), minsize, this, level+1);
			
			childs[4]=new CubeTree(new Point3D(leftFrontTop.x, leftFrontTop.y+childsize, leftFrontTop.z-childsize), 
					new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+currentsize, leftFrontTop.z-currentsize), minsize, this, level+1);
			childs[5]=new CubeTree(new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+childsize, leftFrontTop.z-childsize), 
					new Point3D(leftFrontTop.x+currentsize, leftFrontTop.y+currentsize, leftFrontTop.z-currentsize), minsize, this, level+1);
			childs[6]=new CubeTree(new Point3D(leftFrontTop.x, leftFrontTop.y+childsize, leftFrontTop.z), 
					new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+currentsize, leftFrontTop.z-childsize), minsize, this, level+1);
			childs[7]=new CubeTree(new Point3D(leftFrontTop.x+childsize, leftFrontTop.y+childsize, leftFrontTop.z), 
					new Point3D(leftFrontTop.x+currentsize, leftFrontTop.y+currentsize, leftFrontTop.z-childsize), minsize, this, level+1);
			
		}catch(Exception e) {
			hasChilds=false;
			childs=null;
		}
	}
	
	public boolean add(Cube c) {
		
		if(!this.contains(c)) {
			return false;
		}
		else {
			treeContentsCount++;
			boolean succes=false;
			if(this.hasChilds()) {
				for(CubeTree t : childs) {
					if(t.add(c)) {
						succes=true;
						break;
					}
				}
			}
			if(!succes) {
				contents.add(c);
				c.setTree(this);
			}
			return true;
		}
	}
	
	public boolean hasChilds() {
		return hasChilds;
	}
	
	public CubeTree[] getChilds() {
		return childs;
	}
	
	public ArrayList<Cube> getNodeContents() {
		return contents;
	}
	
	public int getNodeContentsCount() {
		return contents.size();
	}
	
	public int getTreeContentsCount() {
		return treeContentsCount;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void remove(Cube c) { //REMOVES ONLY IN CURRENT NODE
		if(contents.remove(c)) {
			decrementObjectsInTreeCount();

			//System.err.println("destroy character");
		}
		
	}
	
	private void decrementObjectsInTreeCount() {
		treeContentsCount--;
		if(parent!=null) {
			parent.decrementObjectsInTreeCount();
		}
	}

	public void moveCubeInTree(Cube c) { //NO TEST TO CHECK IF CUBE IS CONTAINED IN NODE, use with care
		if(this.contains(c)) {
			boolean succes=false;
			if(this.hasChilds()) {
				for(CubeTree t : childs) {
					if(t.add(c)) {
						succes=true;
						break;
					}
				}
			}
			if(succes) {
				contents.remove(c);
			}
		}
		else {
			if(parent!=null) {
				treeContentsCount--;
				contents.remove(c);
				parent.moveCubeInTreeRecur(c);
			}
		}
	}

	private void moveCubeInTreeRecur(Cube c) {
		if(this.contains(c)) {
			contents.add(c);
		}
		else {
			if(parent!=null) {
				treeContentsCount--;
				parent.moveCubeInTreeRecur(c);
			}
			else {
				contents.add(c);
			}
		}
		
	}
	
	

}
