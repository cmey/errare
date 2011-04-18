package loader3DS;

import java.util.ArrayList;
import java.util.Hashtable;

public class Node {

	private int id;

	public String objectName;

	private Hashtable<Integer, Transform> transforms;

	public float pivotX;

	public float pivotY;

	public float pivotZ;

	public int hierarchy;

	public ArrayList<Node> children;

	public Node parent;

	public boolean hasparent;

	public Node(int id) {
		this.id = id;
		transforms = new Hashtable<Integer, Transform>();
		children = new ArrayList<Node>();
	}

	public void setPivot(float x, float y, float z) {
		this.pivotX = x;
		this.pivotY = y;
		this.pivotZ = z;
	}

	public void addTransform(int i, Transform t) {
		transforms.put(i, t);
	}

	public int getId() {
		return id;
	}

	public Transform getTransformAt(int key) {
		return transforms.get(key);
	}

	public String toString() {
		String res = "";
		if (hierarchy == -1)
			res = "Root : " + hierarchy;
		else
			res = " Node " + hierarchy+ " "+objectName;

		if (!isLeaf()) {
			res+="\rLes fils de "+objectName+" sont : ";
			for(Node n : children){
				res+=n.objectName+" ";
			}
			res+=children;
		}else{
			res+=" est une feuille";
		}
		
		return res;
	}

	private boolean isLeaf() {
		return children.isEmpty();
	}

}
