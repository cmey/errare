package md5Loader;

import java.util.ArrayList;

import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;

public class Joint {

	protected String name;
	
	protected Point3f pos;
	
	protected Quat4f orient;

	protected int parent;
	
	protected Joint dad;
	
	protected ArrayList<Joint> children;
	
	public Joint(String name, int parent, Point3f pos, float x,float y, float z){
		this.name = name;
		this.parent = parent;
		this.pos = pos;
		orient = new Quat4f(x,y,z,computeW(x, y, z));
		children = new ArrayList<Joint>();
	}
	
	private float computeW(float x,float y,float z){
		float t = 1.0f - (x * x) - (y * y) - (z * z);
		float w;
		if (t < 0.0f)
		  {
		    w = 0.0f;
		  }
		else
		  {
		    w = -(float)Math.sqrt(t);
		  }
		return w;
	}

	public int getParent() {
		return parent;
	}


	public void setParent(Joint joint) {
		this.dad = joint;
	}
	
	public void addChild(Joint child){
		children.add(child);
	}
	
	public String toString() {
		String res = "";
		if(parent == -1)
			res +=" root "+parent;
		else
			res = "child of"+dad;
		return res;
	}
	
}
