package loader3DS;

import graphicsEngine.MeshData;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class Animation {

	private String name;

	private ArrayList<Transform> transforms;

	private MeshData starting;

	private int[] geometry;

	public Animation(String name) {
		this.name = name;
		transforms = new ArrayList<Transform>();
	}

	public void setStartingMesh(MeshData md) {
		this.starting = md;
	}

	public void setGeometry(int[] geom) {
		this.geometry = geom;
	}

	public void addTransform(Transform t) {
		transforms.add(t);
	}

	public MeshData[] toArray() {
		
		MeshData[] res = {starting};
		return res;
	}

	public String toString() {
		String anim = "";
		for (Transform t : transforms)
			anim += t.toString() + "\n";
		return anim;
	}
}
