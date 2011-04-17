package networkEngine2;

import geom.Vector;

public class MoveEvent extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3539321181657482049L;
	private Vector moveVect;
	private int speed;
	
	public final static int STAY = 0, WALK = 1, RUN = 2;
	
	/**
	 * a move event for move porpagation between network and gameEngine
	 * @param id the moving clients id
	 * @param vect the vector representing the move
	 * @param moveSpeed the move speed: STAY, WALK or RUN
	 */
	public MoveEvent(String id, Vector vect, int moveSpeed) {
		super(id);
		moveVect = vect;
		speed = moveSpeed;
	}
	
	/**
	 * @return the vector that represents the move
	 */
	public Vector getVector() {
		return moveVect;
	}
	
	/**
	 * @return the string representation of the vector and the used speed for signature generation
	 */
	public String toString() {
		return ""+moveVect.x+moveVect.y+moveVect.z+speed;
	}

}
