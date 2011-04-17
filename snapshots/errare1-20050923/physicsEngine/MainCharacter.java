/*
 * Created on 19 août 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package physicsEngine;

import main.Rep;


/**
 * @author trueblue
 * 
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class MainCharacter extends Character {


	/**
	 * @param map
	 */
	public MainCharacter(int x, int y, int w, int h, PhysicsEngine map, Rep rep) {
		super(x, y, w, h, map, rep);
	}

	@SuppressWarnings("unchecked")
	public void pick(MObject object) {
		super.pick(object);
		//GUIEngine.updateList(getItemList());
		//TODO: Pour Arnaud 

	}

	@SuppressWarnings("unchecked")
	public void drop(MObject object) {
		super.drop(object);
	}

	

	

}
