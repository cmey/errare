package physicsEngine;


import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import main.Rep;


/**
 * @author trueblue
 * 
 * Representation d'un personnage du jeu
 */
public class Character extends  PhysicalRep {

	private Set<MObject> itemList;
	
	
	/**
	 * Cree un personnage.
	 *  
	 */
	@SuppressWarnings("unchecked")
	public Character(int x, int y, int w, int h, PhysicsEngine map, Rep rep) {
		super(x, y, w, h, map, rep);
		itemList = Collections.synchronizedSet(new HashSet());
			
	}

	/**
	 * @param object
	 */
	public void pick(MObject object) {
		System.out.println("pick");
		Rectangle r = (Rectangle) object.clone();
		r.grow(2, 2);
		if(r.intersects(this) || r.contains(this) || this.contains(r)) {
			itemList.add(object);
			object.pick(this);
			map.removeElement(object);
		}

	}

	/**
	 * @param elem
	 */
	public void drop(MObject object) {
		itemList.remove(object);
		object.drop(this);
		map.addElement(object);

	}

	@SuppressWarnings("unchecked")
	public Set<MObject> getItemList() {
		return itemList;

	}


	public String toString() {
		return "Personnage";
	}

	
	public int getLevel() {
		return 2;
	}

}
