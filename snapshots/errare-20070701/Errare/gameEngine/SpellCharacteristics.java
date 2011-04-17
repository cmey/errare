package gameEngine;

public class SpellCharacteristics {
	
	/**
	 * for calculating the level of masteries of a spell
	 * 
	 * threshold_masteries[level][level of masteries] = experience total required
	 */
	private static long[][] threshold_masteries;
	
	private String name;
	private int minimum;
	private int maximum;
	/** damage over time*/
	private int dot;
	private int cooldown_dot;
	private int time_dot;
	
	private int cast_time;
	private int cooldown;
	private int cost_mana;
	private int level;
	private SpellCharacteristics[] previous;
	private SpellCharacteristics[] next;
	/**	indicates if the sons are all needed (and = true) or just one (and = false) */
	private boolean and;
	private long experience;
	//TODO la maitrise
	
	SpellCharacteristics(String name, int min, int max, int dot, int cooldown_dot, int time_dot,
			int cast_time, int cooldown, int cost, int lvl, SpellCharacteristics[] previous,
			SpellCharacteristics[] next, boolean and, long xp){
		this.name = name;
		minimum = min;
		maximum = max;
		this.dot = dot;
		cost_mana = cost;
		level = lvl;
		this.previous = previous;
		this.and = and;
		experience = xp;
	}
	
	public long getExperience(){
		return experience;
	}
	
	public void calculateExperience(){
		//TODO calculer Xp du sort, initialiser la masteries
	}

}
