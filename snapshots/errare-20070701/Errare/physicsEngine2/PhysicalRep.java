package physicsEngine2;

import java.awt.Color;

import main2.Rep;
import geom.Point;
import geom.Vector;

public class PhysicalRep extends BoundingBox{
	
	private Rep rep;
	
	private float mass;

	private Vector speed;
	
	private boolean moving;
	
	public PhysicalRep(Point leftFrontBottom, Point rightBackTop, float mass) {
		super(leftFrontBottom, rightBackTop);
		this.mass=mass;
		speed=new Vector(0, 0, 0);
		moving=false;
	}
	
	public float getMass() {
		return mass;
	}

	public boolean step() {
		speed.add(new Vector(0, -PhysicsEngine.G, 0));
		this.translate(speed);
		
		
		for(BoundingBox t : this.getOctree().getSelfAndDescendantContent()) {
			if(this!=t)
				t.setColor(Color.YELLOW);
			if(this != t && t.intersects(this)) {
				t.setColor(Color.BLUE);
				this.translate(-speed.x, -speed.y, -speed.z);
				speed=new Vector(0, 0, 0);
				moving=false;
				return false;
			}
		}
		
		return true;
	}
	
	public void addForce(Vector params, int duration) {
		if(!moving) {
			params.mult(duration/mass);
			speed.add(params);
			moving=true;
		}
	}

	public Rep getRep() {
		return rep;
	}

	public void setRep(Rep rep) {
		this.rep = rep;
	}

	



}
