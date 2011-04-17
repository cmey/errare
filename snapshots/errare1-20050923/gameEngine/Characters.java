package gameEngine;
/*
 * Created on 6 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author DjSteaK
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Characters {
	
	private static double EXPERIENCE_STRENGTH_REQUIRED = 500;
	private static double EXPERIENCE_DEXTERITY_REQUIRED = 500;
	private static double EXPERIENCE_MIND_REQUIRED = 500;
	
	private String name;
	private int life;
	private int mana;
	private int life_max;
	private int mana_max;
	
	private int gold;
	
	/**
	 * The strength of the character
	 * Each point of strenght increase the life by 25, the mana by 5, ane the melee damage by 1%
	 */
	private int strength;
	/**
	 * The dexterity of the character
	 * Each point of dexterity increase the life by 15, the mana by 15, ane the ranged damage by 1%
	 */
	private int dexterity;
	/**
	 * The mind of the character
	 * Each point of mind increase the life by 5, the mana by 25, ane the magical damage by 1%
	 */
	private int mind;
	
	private double experience_strength;
	private double experience_dexterity;
	private double experience_mind;
	
	/*
	 * Level use, chances of hurt calculation :
	 * normal chance (both characters have the same level) : 75%
	 * the ennemy has a level higther than the character : -5% of chances by level
	 * the character has a level higther than the ennemy : +5% of chances by level
	 */
	/**
	 * The magical level is the chances to hurt an ennemi with a magic spell
	 */
	private int magical_level;
	/**
	 * The ranged level is the chances to hurt an ennemi with ranged weapon
	 */
	private int ranged_level;
	/**
	 * The melee level is the chances to hurt an ennemi with melee weapon
	 */
	private int melee_level;
	
	
	private double fire_resist;
	private double ice_resist;
	private double thunder_resist;
	private double physical_resist;
	
	/**
	 * The chances to block a physical attack
	 * Block chances is calculated before the chances to hurt
	 */
	private double block_chances;
	
	/*
	 * Armor use, reduce damage :
	 * 
	 */
	/**
	 * The armor only reduce physical damages
	 */
	private int armor;
	
	private int attack_type; // 0 : melee, 1 : ranged, 2 : magical ; the default attack is melee 
	
	private int damage_physical_min, damage_physical_max; // the physical damage depend of the attack type
	private int damage_fire_min, damage_fire_max;
	private int damage_ice_min, damage_ice_max;
	private int damage_thunder_min, damage_thunder_max;
	
	
	
	
	

	/**
	 * Create a character
	 * @param name name of the character
	 */
	public Characters(String name) {
		super();
		this.name = name;
		
		strength = 0;
		dexterity = 0;
		mind = 0;
		
		gold = 0;
		
		life_max = 50;
		mana_max = 25;
		life = life_max;
		mana = mana_max;
		
		magical_level = 0;
		ranged_level = 0;
		melee_level = 0;
		
		experience_strength = 0;
		experience_dexterity = 0;
		experience_mind = 0;
		
		fire_resist = 0;
		ice_resist = 0;
		thunder_resist = 0;
		physical_resist = 0;
		
		block_chances = 0;
		
		armor = 0;
		
		attack_type = 0;
		
		damage_physical_min = 0;
		damage_physical_max = 0;
		damage_fire_min = 0;
		damage_fire_max = 0;
		damage_ice_min = 0;
		damage_ice_max = 0;
		damage_thunder_min = 0;
		damage_thunder_max = 0;

	}
	
	/**
	 * Method 
	 * @param ennemy ennemy attacked
	 * @param type type of attack (magic, ranged or melee)
	 */
	public void attack(Character ennemy, String type){
		
	}
	
	public boolean isDead(){ return (life == 0); }
	
	public void isAttacked(int physic, int ranged, int fire, int ice, int thunder){
		
	}
	
	/**
	 * Method which distribue the experience won between the competence (melee...) and the characteristics(strenght...)
	 * @param experience_won
	 */
	public void experienceDistributer(int experience_won){
		
	}
	
	/**
	 * Method which get the character max level between the 3 competences (melee, ranged, magical)
	 * @return the character max level
	 */
	private int getCharacterLevel(){
		return 0;
	}
}
