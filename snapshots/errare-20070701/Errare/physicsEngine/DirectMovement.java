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

import java.awt.Point;
import java.util.Set;

/**
 * @author trueblue
 * 
 * Class that calculates and executes a direct movement between two points.
 * The character will follow the line defined by it's starting point and it's destination.
 */
public class DirectMovement {
	
	/**Character to move*/
	private CharacterPRep character;

	/**Destination*/
	private Point dest;

	/**Distance the character x coordinate will be added at each call of execute()*/
	private double xMove; 
	
	/**Distance the character y coordinate will be added at each call of execute()*/
	private double yMove;

	/**Long representation of the x coordinate of the character to prevent rounding erros witch occure with integers*/
	private double x;
	
	/**Long representation of the y coordinate of the character to prevent rounding erros witch occure with integers*/
	private double y;

	/**PhysicsEngine where the movement is executed*/
	private PhysicsEngine physicsEngine;
	
	/**Indicates if the movement will be finished at the next call of execute*/
	private boolean nearlyFinished;
	
	/**Angle of the orientation of the character*/
	private double angle;
	
	/**Speed of the character*/
	private int speed=10;

	
	/**
	 * Creates a direct movement between the initial position of the character and the destination point.
	 * @param character character which moves
	 * @param destination destination of the character
	 * @param physicsEngine physicsEngine on which the movement is executed
	 */
	public DirectMovement(CharacterPRep character, Point destination, PhysicsEngine physicsEngine) {
		this.character = character;
		this.dest = new Point(destination.x*PhysicsEngine.CELLDIM, destination.y*PhysicsEngine.CELLDIM);
		this.physicsEngine = physicsEngine;
		nearlyFinished=false;
		
		
		xMove = (1 / Math.hypot(character.x() - dest.x, character.y() - dest.y))
		* (dest.x - character.x()) + character.x();
		yMove = (1 / Math.hypot(character.x() - dest.x, character.y() - dest.y))
		* (dest.y - character.y()) + character.y();
		xMove = xMove - character.x();
		yMove = yMove - character.y();
		
		
		angle=Math.atan2(yMove, xMove);
		angle = -(angle/Math.PI)*180;
		
		if(!Double.isNaN(angle))
			character.setAngle(new Float(angle));
		
		
		x = character.x();
		y = character.y();
		
		if(dest.x==character.x()&&dest.y==character.y()) {
			nearlyFinished=true;
		}
		

	}

	

	/**
	 * Executes the movement;
	 * 
	 * @throws MovementFinishedException when the movement is finished
	 */
	public void execute() throws MovementFinishedException {
		
		if(nearlyFinished) {
			physicsEngine.removeCharacter(character);
			character.move(dest.x, dest.y);
			if(!physicsEngine.isMovementOK(character)) {
				character.move((int) x, (int) y);
			}
			physicsEngine.addCharacter(character);
			
			throw new MovementFinishedException();
		}

		x += speed*xMove;
		y += speed*yMove;
		
		physicsEngine.removeCharacter(character);
		character.move((int) x, (int) y);
		

		if (!physicsEngine.isMovementOK(character)) {
			
			x -= speed*xMove;
			y -= speed*yMove;
			
			character.move((int) x, (int) y);
			physicsEngine.addCharacter(character);
			
			throw new MovementFinishedException();

		}
		
		physicsEngine.addCharacter(character);

		
		if(Math.abs(character.x()-dest.x)<speed && Math.abs(character.y()-dest.y)<speed)
			nearlyFinished = true;

	}
	
	
	/**
	 * Returns the destination of the character.
	 * @return the destination point of this movement
	 */
	public Point getDest() {
		return dest;
	}

	/**
	 * Returns the characters which moves.
	 * @return the character of this movement
	 */
	public CharacterPRep getChar() {
		return character;
	}


}
