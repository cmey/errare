package loader3DS;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

public class Translation extends Transform {


	protected float x;
	protected float y;
	protected float z;

	public Translation(float x,float y,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	@Override
	public Vector3f getMatrix() {
		return new Vector3f(x,y,z);
	}
	
	public String toString(){
		return "Translation ";
	}

}
