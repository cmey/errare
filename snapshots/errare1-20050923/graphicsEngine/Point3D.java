package graphicsEngine;

/**
 * 
 * @author Cyberchrist
 *
 */
public class Point3D {
	public float X;
	public float Y;
	public float Z;
	
	public Point3D(){
		X=0;
		Y=0;
		Z=0;
	}
	
	public Point3D(float x, float y, float z){
		this.X=x;
		this.Y=y;
		this.Z=z;
	}
	public void tl (float x,float y,float z){
		this.X+=x;
		this.Y+=y;
		this.Z+=z;
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		Z = z;
	}
}
