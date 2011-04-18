package gameEngine;

public class StepAttack extends Step {

	public StepAttack(Action action){
		super(action);
		timer = 0; // instant action
	}

	public boolean perform() {
		action.creator.computeDamage(action.target.getDamage());
		
		//sound test
		//GameEngine.game.send(new ActionClient("sound", "data/sounds/gui/select.ogg", null), action.creator.getSocket());
		return true;
	}
	

}
