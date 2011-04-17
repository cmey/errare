package physicsEngine;

import java.awt.Rectangle;

import main.Rep;


/**
 * @author trueblue
 * 
 * Représentation d'un objet non deplacable.
 */
public class NMObject extends PhysicalRep {

	/**
	 * Cree un objet non deplacable.
	 *  
	 */
	public NMObject(int x, int y, int w, int h, PhysicsEngine map, Rep rep) {
		super(x, y, w, h, map, rep);
	}

	public String toString() {
		return "Objet non deplacable";
	}

	public int getLevel() {
		return 2;
	}
	
}
