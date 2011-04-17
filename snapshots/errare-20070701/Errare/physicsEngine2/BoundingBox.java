package physicsEngine2;

import geom.Cube;
import geom.Point;
import geom.Sphere;
import geom.Vector;

public class BoundingBox extends Cube implements Comparable<BoundingBox> {
	private int compareDist;
	private Octree tree;
	
	public BoundingBox(Point leftFrontBottom, Point rightBackTop) {
		super(leftFrontBottom, rightBackTop);
	}
	
	public void setComparePoint(Point p) {
		Point center = getSphere().center;
		float xdist = center.x-p.x;
		float ydist = center.y-p.y;
		float zdist = center.z-p.z;
		compareDist = (int) (xdist*xdist + ydist*ydist + zdist*zdist);
	}

	public int compareTo(BoundingBox t) {
		return compareDist-t.compareDist;
	}
	
	public void translate(Vector v) {
		super.translate(v);
		
		tree.moveContent(this);
	}
	
	public void setLocation(Point location) {
		super.setLocation(location);
		
		tree.moveContent(this);
	}
	
	public void translate(float x, float y, float z) {
		translate(new Vector(x, y, z));
	}
	
	public void setOctree(Octree t) {
		tree = t;
	}
	
	public Octree getOctree() {
		return tree;
	}

}
