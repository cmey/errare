package md5Loader;

import javax.vecmath.Point3f;

public class Weight {

	protected int index;
	protected int joint;
	protected Point3f pos;
	protected float bias;

	public Weight(int weightIndex, int joint, float bias, float x, float y, float z) {
		this.index = weightIndex;
		this.joint = joint;
		this.bias = bias;
		pos = new Point3f(x,y,z);
	}
	
	@Override
	public String toString() {
		return index+" "+joint+" "+bias+" "+pos;
	}

}
