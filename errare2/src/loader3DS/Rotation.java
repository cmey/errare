package loader3DS;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

public class Rotation extends Transform {

	public float rot, x, y, z;

	public Quaternion q;
	
	public Rotation(float rot, float x, float y, float z) {
		this.rot = -rot;
		this.x = x;
		this.y = y;
		this.z = z;
		q = new Quaternion(-rot,x,y,z);
	}

	public Matrix4f getMatrix() {
		AxisAngle4f axes = new AxisAngle4f(x, y, z, -rot);
		Matrix4f m = new Matrix4f();
		Matrix4f rm = new Matrix4f();

		m.set(axes);
		rm.rotX((float) -Math.PI / 2);
		Matrix4f orientation = new Matrix4f();
		orientation.mul(rm, m);
		return orientation;
	}
	
	public String toString(){
		return "Rotation";
	}

}
