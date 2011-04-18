package graphicsEngine;

import geom.Vector;

public class EyeCamera extends Camera {

	private float floorAngle;
	private float floorDist;
	private float verticalAngle;
	
	public EyeCamera(){
		super();
		floorAngle = 1;
		verticalAngle = 1;
		floorDist = 1;
	}
	
	
    @Override
	public void rotateY(float angle) {
		floorAngle+=angle;
		
		lookingAt.z = location.z + (float)Math.sin(floorAngle)*floorDist;
		lookingAt.x = location.x + (float)Math.cos(floorAngle)*floorDist;
			
	}
	
	
    @Override
	public void rotateXZ(float angle) {
		verticalAngle-=angle;
		if(verticalAngle>=Math.PI/2)
			verticalAngle = (float)Math.PI/2 - 0.0001f;
		else if(verticalAngle<=-Math.PI/2) {
			verticalAngle = -(float)Math.PI/2 + 0.0001f;
		}
		
		lookingAt.y = location.y+(float)Math.sin(verticalAngle);
		
		floorDist = (float)Math.cos(verticalAngle);
		
		rotateY(0);
	}
	
	
	
	private void move(float value) {
		Vector direction = new Vector(location, lookingAt);
		direction.normalize();
		direction.mult(value);
		
		location.translate(direction);
		lookingAt.translate(direction);
	}
	
	public void forward() {
		this.move(EyeCamera.STD_MOVE_SPEED);
	}
	
	
	public void backward() {
		this.move(EyeCamera.STD_MOVE_SPEED*-1);
	}
	
	
	public void strafeLeft() {
		
	}
	
	public void strafeRight() {
		
	}
	
    @Override
	public void update() {
		
	}
}
