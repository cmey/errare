/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package physicsEngine;


import java.util.ArrayList;
import java.util.List;

import main.CharacterRep;


/**
 * @author trueblue
 * 
 * Physical representation of a character (as a square).
 */
public class CharacterPRep extends PhysicalRep {

	private static final long serialVersionUID = 1L;
	
	/**List of followers that are notified when a new DirectMovement is executed by the character*/
	private List<CharacterPRep> followers;
	
	/**Guide which the character is following*/
	private CharacterPRep guide;
	
	private CharacterRep rep;
	
	private boolean running;

	
	
	/**
	 * Creates a new Character
	 * @param x x coordinate of the character
	 * @param y y coordinate of the character
	 * @param w width of the caracter
	 * @param h height of the character
	 * @param physicsEngine physicsEngine on which the character moves
	 * @param guiInventory inventoryEngine which is used is case the character is the main character
	 */
	public CharacterPRep(int x, int y, PhysicsEngine physicsEngine){
		super(x, y, 1, 1, physicsEngine);
		followers=new ArrayList<CharacterPRep>();
		
		running=false;
		
		
		if(!physicsEngine.addCharacter(this)) {
			throw new IllegalArgumentException("Something is already located at ("+x+";"+y+")");
		}
		/*else {
			physicsEngine.getCubeTree().add(cube);
		}*/
		
			
	}
	
	

	/**
	 * Method called when the charater picks up an item.
	 * @param item item picked up
	 *//*
	public void pick(ItemPRep item) {
		//TODO: isPicked should not be necessary to call
		if(!item.isPicked() && physicsEngine.getGuiInventory().pick(item.getRep())) {
			physicsEngine.removeItem(item);
			item.setPicked(true);
		}
	}*/

	/**
	 * Method called when the charater drops an item.
	 * @param item droped
	 */
	public void drop(ItemPRep item) {
		
		
		int x=cx();
		int y=cy();
		int diff=1;
		item.setLocation(x+physicsEngine.random(diff), y+physicsEngine.random(diff));
		
		while(!physicsEngine.addItem(item)) {
			diff++;
			item.setLocation(x+physicsEngine.random(diff), y+physicsEngine.random(diff));
		}
		
		item.setPicked(false);

	}
	
	public CharacterRep getRep() {
		return rep;
	}



	public void setRep(CharacterRep r) {
		this.rep=r;
		cube.setRep(r);
	}



	
	
	/**
	 * Adds a follower.
	 * @param follower follower to add
	 */
	public void addFollower(CharacterPRep follower) {
		followers.add(follower);
	}
	
	/**
	 * Removes a follower.
	 * @param follower follower to remove
	 */
	public void removeFollower(CharacterPRep follower) {
		followers.remove(follower);
	}
	
	/**
	 * Returns the followers list.
	 * @return list of folllowers
	 */
	public List<CharacterPRep> getFollowers() {
		return followers;
	}
	
	/**
	 * Adds a guide.
	 * @param guide guide to add
	 */
	public void setGuide(CharacterPRep guide) {
		this.guide=guide;
	}
	
	/**
	 * Removes a guide.
	 * @param guide guide to remove
	 */
	public void removeGuide(CharacterPRep guide) {
		guide=null;
	}
	
	/**
	 * Returns the list of guides.
	 * @return list of guides
	 */
	public CharacterPRep getGuide() {
		return guide;
	}



	public boolean isRunning() {
		return running;
	}



	public void setRunning(boolean running) {
		this.running = running;
	}


	public void destroy() {
		physicsEngine.destroyCharacter(this);
	}

	
	public int z() {
		return super.z()+25;
	}
}
