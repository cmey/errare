package main;

import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicsEngine;
import physicsEngine.PhysicalRep;

public class Rep {
	private PhysicalRep physicalRep;
	private GraphicalRep graphicalRep;
	
	public Rep(Main main, String graphicalRepFile, PhysicalRep.type type, int pX, int pY, int pWidth, int pHeight) {
		physicalRep = main.getPhysicsEngine().load(type, pX, pY, pWidth, pHeight, this);
		graphicalRep = new GraphicalRep(graphicalRepFile);
	}
	
	public PhysicalRep getPhysicsRep() {
		return physicalRep;
	}
	
	public GraphicalRep getGraphicalRep() {
		return graphicalRep;
	}
	
	/*public boolean setGraphicalRep(GraphicalRep r) {
		this.graphicalRep = r;
		return true;
	}*/
}
 