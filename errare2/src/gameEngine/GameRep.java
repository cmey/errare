package gameEngine;

import genericReps.CharacterRep;
import genericReps.Rep;
import graphicsEngine.GraphicalRep;
import physicsEngine.PhysicalRep;

public abstract class GameRep {

	static GameEngine ge; // currently all gamereps share a single GameEngine
	Rep rep; // saves a Rep to get access to other reps (physical, graphical, etc..) of this object.

	public void setRep(Rep rep){
		this.rep = rep;
	}
	
	public Rep getRep(){
		return this.rep;
	}
}
