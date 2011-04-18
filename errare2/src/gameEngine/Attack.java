package gameEngine;

import java.util.ArrayList;

public class Attack extends Action{
	
	public Attack(GameCharacter createur, GameCharacter cible){
		id = "attack";
		current_step = 0;
		creator = createur;
		target = cible;
		steps = new ArrayList<Step>(2);
		steps.add(0, new StepAttack(this));
		steps.add(1, new StepCooldown(this, 1000));
		
	}

	public boolean perform() {
		if(steps.get(current_step).perform()) current_step++;
		return current_step >= steps.size();
	}
}
