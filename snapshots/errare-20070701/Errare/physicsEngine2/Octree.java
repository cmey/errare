package physicsEngine2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import geom.Cube;
import geom.Point;
import geom.Vector;

public class Octree implements Comparable<Octree>{
	private BoundingBox boundingBox;
	
	private Octree[] children;
	
	private ArrayList<BoundingBox> content;
	private ArrayList<BoundingBox> occluders;
	
	
	
	private int globalContentCount;
	private int globalOccluderCount;
	
	private Octree parent;
	
	public Octree(BoundingBox c, float minSize, Octree parent) {
		this.boundingBox=c;
		
		
		content = new ArrayList<BoundingBox>();
		globalContentCount = 0;
		occluders = new ArrayList<BoundingBox>();
		globalOccluderCount = 0;
		
		this.parent=parent;
		
		Point origin = c.getLeftFrontBottom();
		float boundingBoxSize = c.getRightBackTop().x-origin.x;
		if(boundingBoxSize>minSize) {
			float halfTreeboundingBoxSize = boundingBoxSize/2;
			children = new Octree[8];
			children[0] = new Octree(new BoundingBox(new Point(origin.x, origin.y, origin.z), 
					new Point(origin.x+halfTreeboundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z+halfTreeboundingBoxSize)), minSize, this);
			
			children[1] = new Octree(new BoundingBox(new Point(origin.x, origin.y+halfTreeboundingBoxSize, origin.z), 
					new Point(origin.x+halfTreeboundingBoxSize, origin.y+boundingBoxSize, origin.z+halfTreeboundingBoxSize)), minSize, this);
			
			children[2] = new Octree(new BoundingBox(new Point(origin.x+halfTreeboundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z), 
					new Point(origin.x+boundingBoxSize, origin.y+boundingBoxSize, origin.z+halfTreeboundingBoxSize)), minSize, this);
			
			children[3] = new Octree(new BoundingBox(new Point(origin.x+halfTreeboundingBoxSize, origin.y, origin.z), 
					new Point(origin.x+boundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z+halfTreeboundingBoxSize)), minSize, this);
			
			
			children[4] = new Octree(new BoundingBox(new Point(origin.x, origin.y, origin.z+halfTreeboundingBoxSize), 
					new Point(origin.x+halfTreeboundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z+boundingBoxSize)), minSize, this);
			
			children[5] = new Octree(new BoundingBox(new Point(origin.x, origin.y+halfTreeboundingBoxSize, origin.z+halfTreeboundingBoxSize), 
					new Point(origin.x+halfTreeboundingBoxSize, origin.y+boundingBoxSize, origin.z+boundingBoxSize)), minSize, this);
			
			children[6] = new Octree(new BoundingBox(new Point(origin.x+halfTreeboundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z+halfTreeboundingBoxSize), 
					new Point(origin.x+boundingBoxSize, origin.y+boundingBoxSize, origin.z+boundingBoxSize)), minSize, this);
			
			children[7] = new Octree(new BoundingBox(new Point(origin.x+halfTreeboundingBoxSize, origin.y, origin.z+halfTreeboundingBoxSize), 
					new Point(origin.x+boundingBoxSize, origin.y+halfTreeboundingBoxSize, origin.z+boundingBoxSize)), minSize, this);
		}
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public Octree[] getChildren() {
		return children;
	}
	
	public Octree[] getChildren(Point p) {
		for(Octree child : children) {
			child.getBoundingBox().setComparePoint(p);
		}
		Arrays.sort(children);
		return children;
	}
	
	public ArrayList<BoundingBox> getContent() {
		return content;
	}
	
	public ArrayList<BoundingBox> getSelfAndDescendantContent() {
		return getSelfAndDescendantContent(new ArrayList<BoundingBox>(getGlobalContentCount()));
	}
	
	private ArrayList<BoundingBox> getSelfAndDescendantContent(ArrayList<BoundingBox> c) {
		c.addAll(content);
		if(children!=null) {
			for(Octree child : children) {
				child.getSelfAndDescendantContent(c);
			}
		}
		return c;
	}
	
	public int getGlobalContentCount() {
		return globalContentCount;
	}
	
	public ArrayList<BoundingBox> getOccluders() {
		return occluders;
	}
	
	public int getGlobalOccluderCount() {
		return globalOccluderCount;
	}
	
	public boolean addContent(BoundingBox c) {
		if(boundingBox.contains(c)) {
			globalContentCount++;
			if(children==null) {
				content.add(c);
				c.setOctree(this);
			}
			else {
				boolean added = false;
				for(Octree child : children) {
					if(child.addContent(c)) {
						added=true;
						break;
					}
				}
				if(!added) {
					content.add(c);
					c.setOctree(this);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	private void decrementGlobalContentCount() {
		globalContentCount--;
		if(parent!=null) {
			parent.decrementGlobalContentCount();
		}
	}
	
	public void removeContent(BoundingBox c) {
		content.remove(c);
		decrementGlobalContentCount();
	}

	private void addMovedContent(BoundingBox c) {
		if(!boundingBox.contains(c)) {
			globalContentCount--;
			if(parent!=null) //TODO:to remove
				parent.addMovedContent(c);
			else {
				globalContentCount++; //to remove
				content.add(c); //to remove
				c.setOctree(this); //to remove
			}
		}
		else {
			content.add(c);
			c.setOctree(this);
		}
	}
	
	public void moveContent(BoundingBox c) {
		if(!boundingBox.contains(c) && parent!=null) {
			content.remove(c);
			globalContentCount--;
			parent.addMovedContent(c);
		}
		else {
			if(children!=null) {
				boolean added=false;
				
				for(Octree child : children) {
					if(child.addContent(c)) {
						added=true;
						break;
					}
				}
				if(added) {
					content.remove(c);
				}
			}
		}
		
	}
	
	public boolean addOccluder(BoundingBox c) {
		if(boundingBox.contains(c)) {
			globalOccluderCount++;
			if(children==null) {
				occluders.add(c);
				c.setOctree(this);
			}
			else {
				boolean added = false;
				for(Octree child : children) {
					if(child.addOccluder(c)) {
						added=true;
						break;
					}
				}
				if(!added) {
					occluders.add(c);
					c.setOctree(this);
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	private void decrementGlobalOccluderCount() {
		globalOccluderCount--;
		if(parent!=null)
			parent.decrementGlobalOccluderCount();
	}
	
	public void removeOccluder(BoundingBox c) {
		occluders.remove(c);
		decrementGlobalOccluderCount();
	}

	public int compareTo(Octree o) {
		return boundingBox.compareTo(o.boundingBox);
	}

}
