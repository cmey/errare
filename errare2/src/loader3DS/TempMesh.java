package loader3DS;

import java.util.Hashtable;

public class TempMesh {

	private float[] vertices;
	
	private int[] topology;

	private float[] texcoords;
	
	private TempMesh child;
	
	public float[] lastVertices;
	
	Hashtable<Integer, Transform> transforms;
	
	public TempMesh( float[] vertices,int[] topology,float[] texcoords){
		this.vertices = vertices;
		this.topology = topology;
		this.texcoords = texcoords;
		transforms =  new Hashtable<Integer, Transform>();
	}
	
	public int[] getTopology(){
		return topology;
	}
	
	public float[] getVertices(){
		return vertices;
	}

	public float[] getTexCoords(){
		return texcoords;
	}
	
	public void addChild(TempMesh child) {
		if(this.child==null) this.child=child;
		else
			this.child.addChild(child);
	}

	public float[] getVertices(int i) {
		if(lastVertices==null) return vertices;
		return lastVertices;
	}
	
	public void addTransform(int key, Transform t){
		transforms.put(key,t);
	}
	
	public Transform getTransformAt(int key){
		return transforms.get(key);
	}

	
}
