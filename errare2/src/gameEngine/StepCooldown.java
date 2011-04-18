package gameEngine;

public class StepCooldown extends Step {
	
	public StepCooldown(Action action, int time){
		super(action);
		timer = time + System.currentTimeMillis();
	}

	
	public boolean perform() {
		return timer < System.currentTimeMillis();
	}

}
