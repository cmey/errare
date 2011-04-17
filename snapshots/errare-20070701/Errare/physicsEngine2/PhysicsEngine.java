package physicsEngine2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import main2.*;

import geom.Point;
import geom.Vector;

public class PhysicsEngine implements Engine {
	
	public static final float G=0.5f;
	
	private Octree octree;

	private Set<PhysicalRep> moves;

	private Main main;

	private PhysicalRep mainChar;
	
	public PhysicsEngine(Main main) {
		this.main=main;
		
		float size=10000;
		octree = new Octree(new BoundingBox(new Point(0, 0, 0), new Point(size, size, size)), size/8, null);
		moves = new HashSet<PhysicalRep>();
		
		main.getUserInputControler().register(this, "left");
		main.getUserInputControler().register(this, "right");
		main.getUserInputControler().register(this, "up");
		main.getUserInputControler().register(this, "down");
		main.getUserInputControler().register(this, "jump");
	}
	
	public Octree getOctree() {
		return octree;
	}
	
	public void add(PhysicalRep rep) {
		octree.addContent(rep);
	}
	
	public void setMove(PhysicalRep rep, Vector params) {
		
		/*if(rep==mainChar) {
			main.getNetworkEngine().setMainCharMove(params);
		}*/
		
		rep.addForce(params, 100);
		
		moves.add(rep);
	}

	public void run() {
		for(Iterator<PhysicalRep> it = moves.iterator(); it.hasNext();) {
			PhysicalRep rep = it.next();
			if(!rep.step())
				it.remove();
			
		}
		
	}

	public void setMainChar(PhysicalRep mainChar) {
		this.mainChar = mainChar;
		
	}

	public boolean invokeKeyEvent(String action) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean invokeMouseEvent(String action, int x, int y) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
