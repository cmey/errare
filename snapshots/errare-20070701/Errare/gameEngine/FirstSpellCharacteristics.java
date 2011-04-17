package gameEngine;

public class FirstSpellCharacteristics extends SpellCharacteristics {
	
	private long ratio;
	
	FirstSpellCharacteristics(String name, int min, int max, int dot, int cooldown_dot, int time_dot,
			int cast_time, int cooldown, int cost, SpellCharacteristics[] next, long xp, long ratio){
		super(name, min, max, dot, cooldown_dot, time_dot, cast_time, cooldown,
				cost, 0 /*it's the first spell : level 0 and no son*/, null, next, true, xp);
		this.ratio = ratio;
	}
	
	public long getRatio(){
		return ratio;
	}

}
