package physicsEngine;

import java.awt.Rectangle;

import main.Rep;


/**
 * @author trueblue
 * 
 * Representation d'un objet deplacable par un joueur
 */
public class MObject extends PhysicalRep {


	private boolean isPicked;

	private Character isPickedBy;

	
	/**
	 * Cree un objet deplacable par un joueur
	 *  
	 */
	public MObject(int x, int y, int w, int h, PhysicsEngine map, Rep rep) {
		super(x, y, w, h, map, rep);
		isPicked = false;
	}

	/**
	 * @param character
	 */
	public void pick(Character c) {
		isPicked = true;
		isPickedBy = c;

	}

	public boolean isPicked() {
		return isPicked;
	}

	public Character getPickedByWho() {
		return isPickedBy;
	}

	public String toString() {
		return "Objet deplacable";
	}

	/**
	 * @param c
	 *  
	 */
	public void drop(Character c) {
		x = c.x;
		y = c.y;
		isPicked = false;
		isPickedBy = null;

	}

	public int getLevel() {
		return 1;
	}
	
}
