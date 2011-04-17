package physicsEngine;

import java.awt.Point;

/**
 * @author trueblue
 * 
 * Définit un mouvement de personnage
 */
public class DirectMovement {
	/** Personnage a deplacer */
	private Character character;

	/** Destination */
	private Point dest;

	private double X, Y;

	private double x, y;

	private PhysicsEngine map;
	
	private DirectMovement nextMov;

	private double prevDist=Double.MAX_VALUE;
	
	private PhysicalRep elementAction;
	/**
	 * Cree un mouvement.
	 * 
	 * @param c
	 *            personnage a deplacer
	 * @param p
	 *            destination
	 */
	public DirectMovement(Character c, Point p, PhysicsEngine m, DirectMovement n, PhysicalRep e) {
		character = c;
		dest = p;
		map = m;
		nextMov = n;
		elementAction = e;

	}

	/**
	 * Retourne la destination du personnage.
	 * 
	 * @return destination
	 */
	public Point getDest() {
		return dest;
	}

	/**
	 * Retourne le personnage a deplacer.
	 * 
	 * @return personnage a deplacer
	 */
	public Character getChar() {
		return character;
	}

	/**
	 * Execute le mouvement.
	 * 
	 * @throws MovementFinishedException
	 */
	public void execute() throws MovementFinishedException {


		x += X;
		y += Y;

		character.x = (int) x;
		character.y = (int) y;

		if (!map.isMovementOK(character, character.getLevel())) {
			
			x -= 2*X;
			y -= 2*Y;
			
			character.x = (int) x;
			character.y = (int) y;
			
			
			
			throw new MovementFinishedException();

		} else {
			map.updateAreas(character);
		}

		double currDist = Math.hypot(character.x - dest.x, character.y - dest.y);
		if (currDist > prevDist) {
			character.x = dest.x;
			character.y = dest.y;
			
			if(nextMov != null) {
				nextMov.start();
			}
			
			System.out.println(elementAction);
			if(elementAction != null && elementAction instanceof MObject) {
				character.pick((MObject)elementAction);
			}
			
			throw new MovementFinishedException();
		}
		prevDist = currDist;

	}

	public void start() {
		X = (1 / Math.hypot(character.x - dest.x, character.y - dest.y))
				* (dest.x - character.x) + character.x;
		Y = (1 / Math.hypot(character.x - dest.x, character.y - dest.y))
				* (dest.y - character.y) + character.y;
		X = X - character.x;
		Y = Y - character.y;

		x = character.x;
		y = character.y;
		
		map.addMove(this);

	}


}
