/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Guillaume PERSONNETTAZ

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package gameEngine;

import main.CharacterRep;

// gerer les FACTIONS !!!!  si il peut attaquer.
/*
 * Created on 6 sept. 2005
 *
 */

/**
 * @author DjSteaK
 *
 * this class manage all the characteristics of the characters
 */
public abstract class Characters {
	
	/** base life of a character */
	public static final int BASE_LIFE = 50;
	/** base mana of a character */
	public static final int BASE_MANA = 25;
	/** the chances to hit an ennemy who has the same level than the character in percentage*/
	public static final int DEFAUT_CHANCES_TO_HIT = 75;
	/** the penality to hit an ennemy who has the same level than the character in percentage*/
	public static final int PENALITY_CHANCE_TO_HIT = 5;
	
	/** life maximum given by a point of strength */
	public static final int LIFE_BY_POINT_OF_STRENGTH = 25;
	/** life maximum given by a point of dexterity */
	public static final int LIFE_BY_POINT_OF_DEXTERITY = 15;
	/** life maximum given by a point of mind */
	public static final int LIFE_BY_POINT_OF_MIND = 5;
	
	/** mana maximum given by a point of strength */
	public static final int MANA_BY_POINT_OF_STRENGTH = 5;
	/** mana maximum given by a point of dexterity */
	public static final int MANA_BY_POINT_OF_DEXTERITY = 15;
	/** mana maximum given by a point of mind */
	public static final int MANA_BY_POINT_OF_MIND = 25;
	
	/** character's name */
	protected String name;
	/** character's current life */
	protected int life;
	/** character's current mana */
	protected int mana;
	/** character's maximum life */
	protected int life_max;
	/** character's maximum mana */
	protected int mana_max;
	/** character's gold */
	protected int gold;
	
	/**
	 * the strength of the character
	 * each point of strenght increase the melee damage by 1%
	 */
	protected int strength;
	/**
	 * the dexterity of the character
	 * each point of dexterity increase the ranged damage by 1%
	 */
	protected int dexterity;
	/**
	 * the mind of the character
	 * each point of mind increase the magical damage by 1%
	 */
	protected int mind;
	
	/** character's experience in strength */
	protected double experience_strength;
	/** character's experience in dexterity */
	protected double experience_dexterity;
	/** character's experience in mind */
	protected double experience_mind;
	
	/*
	 * level use, chances of hurt calculation :
	 * normal chance (both characters have the same level) : 75%
	 * the ennemy has a level higther than the character : -5% of chances by level
	 * the character has a level higther than the ennemy : +5% of chances by level
	 */
	/** the magical level is the chances to hurt an ennemi with a magic spell */
	protected int magical_level;
	/** the ranged level is the chances to hurt an ennemi with ranged weapon */
	protected int ranged_level;
	/** the melee level is the chances to hurt an ennemi with melee weapon */
	protected int melee_level;
	
	/** character's experience in magic */
	protected double experience_magical;
	/** character's experience in ranged combat */
	protected double experience_ranged;
	/** character's experience in melee */
	protected double experience_melee;
	
	/** character's fire resist*/
	protected int fire_resist;
	/** character's ice resist*/
	protected int ice_resist;
	/** character's thunder resist*/
	protected int thunder_resist;
	/** character's physical resist*/
	protected int physical_resist; // by a magic attribute, not by the armor
	
	/**
	 * the chances to block a physical attack, it's a percentage
	 * block chances is calculated after the chances to hurt
	 */
	protected int block_chances;
	
	/* Armor use, reduce damage :
	 * 25ln((armor+300)/300) = % reduce damage (easy, no ?)*/
	/** the armor only reduce physical damages */
	protected int armor;
	
	/**
	 * character's armor
	 * the type of attack used by the character
	 */
	protected int attack_type; // 0 : melee, 1 : ranged, 2 : magical ; the default attack is melee 
	
	/** the physical minimum and maximum damage */
	protected int damage_physical_min, damage_physical_max; // the physical damage depend of the attack type (ranged for a ranged attack, melee for a melee attack)
	/** the fire minimum and maximum damage */
	protected int damage_fire_min, damage_fire_max;
	/** the ice minimum and maximum damage */
	protected int damage_ice_min, damage_ice_max;
	/** the thunder minimum and maximum damage */
	protected int damage_thunder_min, damage_thunder_max;
	/** the faction of a character*/
	protected String faction;
	/** indicates if the character is busy or not*/
	protected boolean busy;
	
	/** Indicates the rep of a character*/
	protected CharacterRep rep;
	

	/** References the game engines*/
	protected GameEngine game;
	
	/**
	 * create a character
	 * @param name name of the character
	 */
	public Characters(String name, GameEngine game) {
		
		this.name = name;
		this.game = game;
		
		strength = 0;
		dexterity = 0;
		mind = 0;
		
		gold = 0;
		
		life_max = BASE_LIFE;
		mana_max = BASE_MANA;
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
		
		damage_physical_min = 1;
		damage_physical_max = 2;
		damage_fire_min = 0;
		damage_fire_max = 0;
		damage_ice_min = 0;
		damage_ice_max = 0;
		damage_thunder_min = 0;
		damage_thunder_max = 0;
		
		faction = null;
		busy = false;
		rep = null;

	}
	
	/**
	 * this method manage a fight
	 * @param ennemy ennemy attacked
	 */
	public void attack(Characters ennemy){
		if(hasHit(ennemy)){
			int melee = 0, ranged = 0, fire = 0, ice = 0, thunder = 0;
			if(attack_type == 0)
				melee = (int) (rand(damage_physical_min, damage_physical_max) * (1+(strength/100)));
			else if(attack_type == 1)
				ranged = (int) (rand(damage_physical_min, damage_physical_max) * (1+(dexterity/100)));
			fire = rand(damage_fire_min, damage_fire_max);
			ice = rand(damage_ice_min, damage_ice_max);
			thunder = rand(damage_thunder_min, damage_thunder_max);
			if(attack_type == 2){
				fire *= (1+mind/100);
				ice *= (1+mind/100);
				thunder *= (1+mind/100);
			}
			double experience_won = ennemy.isAttacked(melee, ranged, fire, ice, thunder, getCharacterLevel());
			experienceDistributer(experience_won, ennemy.getCharacterLevel());
		}
	}

	/**
	 * this method check if the character is dead
	 * @return true if he is dead, false else
	 */
	public boolean isDead(){ return (life <= 0); }
	
	/**
	 * this method calculates the real damage taken by a character
	 * @param melee melee damage of the attack
	 * @param ranged ranged damage of the attack
	 * @param fire fire damage of the attack
	 * @param ice ice damage of the attack
	 * @param thunder thunder damage of the attack 
	 * @return the real damage taken
	 */
	/*
	 * the shield blocks physical damages but not the magical damages
	 */
	public int damageTaken(int melee, int ranged, int fire, int ice, int thunder){
		int damage_total = 0;
		if(rand(1, 100)>= block_chances)
			damage_total += melee * (100- (reduceDamageByArmor()+physical_resist)) /100 + ranged * (100- (reduceDamageByArmor()+physical_resist)) /100;
		damage_total += fire * (100 - fire_resist) / 100;
		damage_total += ice * (100 - ice_resist) / 100;
		damage_total += thunder * (100 - thunder_resist) / 100;
		return damage_total;
	}
	
	/**
	 * this method calculates the magical bonus gained with the item
	 * @param bonus bonus to calculate
	 * @param line the line of the array (cf ItemCharacteristics.MAGICAL_BONUS for more details)
	 * @return the magical bonus gained
	 */
	public int magicalAttributGained(double bonus, int line){
		double res = bonus;
		for(int i = 0; i < ItemCharacteristics.NUMBER_OF_CHARACTERISTICS; i++){
			res += res * ItemCharacteristics.MAGICAL_BONUS[line][i] / 100;
		}
		return (int)res;
	}
	
	/**
	 * this method equip an item on the character
	 * @param i the item to equip
	 */
	public void equip(ItemCharacteristics rep){
		
		ItemCharacteristics i = rep;
		life_max += magicalAttributGained(i.getMagical_life(), ItemCharacteristics.MAGICAL_LIFE);
		mana_max += magicalAttributGained(i.getMagical_mana(), ItemCharacteristics.MAGICAL_MANA);
		strength += magicalAttributGained(i.getMagical_strength(), ItemCharacteristics.MAGICAL_STRENGTH);
		dexterity += magicalAttributGained(i.getMagical_dexterity(), ItemCharacteristics.MAGICAL_DEXTERITY);
		mind += magicalAttributGained(i.getMagical_mind(), ItemCharacteristics.MAGICAL_MIND);
		fire_resist += magicalAttributGained(i.getMagical_fire_resist(), ItemCharacteristics.MAGICAL_FIRE_RESIST);
		thunder_resist += magicalAttributGained(i.getMagical_thunder_resist(), ItemCharacteristics.MAGICAL_THUNDER_RESIST);
		ice_resist += magicalAttributGained(i.getMagical_ice_resist(), ItemCharacteristics.MAGICAL_ICE_RESIST);
		physical_resist += magicalAttributGained(i.getMagical_physical_resist(), ItemCharacteristics.MAGICAL_PHYSICAL_RESIST);
		block_chances += magicalAttributGained(i.getMagical_block_chances(), ItemCharacteristics.MAGICAL_BLOCK);
		armor += magicalAttributGained(i.getMagical_armor(), ItemCharacteristics.MAGICAL_ARMOR);
		damage_physical_min += magicalAttributGained(i.getMagical_physical_damage_min(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MIN);
		damage_physical_max += magicalAttributGained(i.getMagical_physical_damage_max(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MAX);
		damage_fire_min += magicalAttributGained(i.getMagical_fire_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_fire_max += magicalAttributGained(i.getMagical_fire_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_ice_min += magicalAttributGained(i.getMagical_ice_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_ice_max += magicalAttributGained(i.getMagical_ice_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_thunder_min += magicalAttributGained(i.getMagical_thunder_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_thunder_max += magicalAttributGained(i.getMagical_thunder_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		
	}
	
	/**
	 * this method unequip an item on the character.
	 * @param i the item to equip
	 */
	public void unequip(ItemCharacteristics rep){
		
		ItemCharacteristics i = rep;
		life_max -= magicalAttributGained(i.getMagical_life(), ItemCharacteristics.MAGICAL_LIFE);
		mana_max -= magicalAttributGained(i.getMagical_mana(), ItemCharacteristics.MAGICAL_MANA);
		strength -= magicalAttributGained(i.getMagical_strength(), ItemCharacteristics.MAGICAL_STRENGTH);
		dexterity -= magicalAttributGained(i.getMagical_dexterity(), ItemCharacteristics.MAGICAL_DEXTERITY);
		mind -= magicalAttributGained(i.getMagical_mind(), ItemCharacteristics.MAGICAL_MIND);
		fire_resist -= magicalAttributGained(i.getMagical_fire_resist(), ItemCharacteristics.MAGICAL_FIRE_RESIST);
		thunder_resist -= magicalAttributGained(i.getMagical_thunder_resist(), ItemCharacteristics.MAGICAL_THUNDER_RESIST);
		ice_resist -= magicalAttributGained(i.getMagical_ice_resist(), ItemCharacteristics.MAGICAL_ICE_RESIST);
		physical_resist -= magicalAttributGained(i.getMagical_physical_resist(), ItemCharacteristics.MAGICAL_PHYSICAL_RESIST);
		block_chances -= magicalAttributGained(i.getMagical_block_chances(), ItemCharacteristics.MAGICAL_BLOCK);
		armor -= magicalAttributGained(i.getMagical_armor(), ItemCharacteristics.MAGICAL_ARMOR);
		damage_physical_min -= magicalAttributGained(i.getMagical_physical_damage_min(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MIN);
		damage_physical_max -= magicalAttributGained(i.getMagical_physical_damage_max(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MAX);
		damage_fire_min -= magicalAttributGained(i.getMagical_fire_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_fire_max -= magicalAttributGained(i.getMagical_fire_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_ice_min -= magicalAttributGained(i.getMagical_ice_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_ice_max -= magicalAttributGained(i.getMagical_ice_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_thunder_min -= magicalAttributGained(i.getMagical_thunder_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_thunder_max -= magicalAttributGained(i.getMagical_thunder_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		
	}
	
	/**
	 * @return Returns the faction.
	 */
	public String getFaction() {
		return faction;
	}

	/**
	 * @param faction The faction to set.
	 */
	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	/**
	 * @return Returns if the character is busy.
	 */
	public boolean isBusy() {
		return busy;
	}

	/**
	 * @param busy The busy to set.
	 */
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	/**
	 * set the rep
	 * @param c the character rep
	 */
	public void setRep(CharacterRep c){
		rep = c;
	}
	
	public CharacterRep getRep(){
		return rep;
	}
	
	public void addLife(int life){
		this.life += life;
		if(this.life > life_max) this.life = life_max;
	}
	
	public void addMana(int mana){
		this.mana += mana;
		if(this.mana > mana_max) this.mana = mana_max;
	}
	
	public int getGold(){
		return gold;
	}
	
	public String getName(){
		return name;
	}
	
	public int getStrength(){
		return strength;
	}
	
	public int getDexterity(){
		return dexterity;
	}
	
	public int getMind(){
		return mind;
	}
	
	public int getDamageMaxTotal(){
		return (damage_fire_max + damage_ice_max + damage_physical_max + damage_thunder_max);
	}
	
	public int getDamageMinTotal(){
		return (damage_fire_min + damage_ice_min + damage_physical_min + damage_thunder_min);
	}
	
	public int getArmor(){
		return armor;
	}
	
	public int getFireResist(){
		return fire_resist;
	}
	
	public int getIceResist(){
		return ice_resist;
	}
	
	public int getThunderResist(){
		return thunder_resist;
	}
	
	public int getPhysicalResist(){
		return physical_resist;
	}
	
	public int getLifeMax(){
		return life_max;
	}
	
	public int getManaMax(){
		return mana_max;
	}
	
	public int getLife(){
		return life;
	}
	
	public int getMana(){
		return mana;
	}
	
	/**
	 * method which distribue the experience won between the competence (melee...) and the characteristics(strenght...)
	 * @param experience_won
	 */
	public abstract void experienceDistributer(double experience_won, int level_ennemy);
	
	/**
	 * this method manage a combat
	 * @param melee melee damage of the attack
	 * @param ranged ranged damage of the attack
	 * @param fire fire damage of the attack
	 * @param ice ice damage of the attack
	 * @param thunder thunder damage of the attack
	 * @param level_attacker the level of the attacker
	 * @return the experience won by the attacker (here, work only for the hero : ennemy gain 0 experience per hit)
	 */
	protected abstract double isAttacked(int melee, int ranged, int fire, int ice, int thunder, int level_attacker);
	
	/**
	 * method which get the character maximum level between the 3 competences (melee, ranged, magical)
	 * @return the character maximum level
	 */
	protected int getCharacterLevel(){
		return Math.max(magical_level, Math.max(ranged_level, melee_level));
	}
	
	/**
	 * this method takes a random number between the 2 parameters
	 * @param min the number minimum of both
	 * @param max the number maximum of both
	 * @return a random number between the 2 parameters
	 */
	protected int rand(int min, int max) {
		int ret = min;
		if(max > min)
			ret += game.getMainLink().getRandom().nextInt(max - min);
		//System.out.println("RAND"+ret);
		return ret;
	}
	
	/**
	 * this method recaculate the mana maximum and the life maximum
	 * it is called mainly before the hero gain a point of characteristic
	 */
	protected void setManaLifeMax(){
		life_max = BASE_LIFE + strength * LIFE_BY_POINT_OF_STRENGTH + dexterity * LIFE_BY_POINT_OF_DEXTERITY + mind * LIFE_BY_POINT_OF_MIND;
		mana_max = BASE_MANA + strength * MANA_BY_POINT_OF_STRENGTH + dexterity * MANA_BY_POINT_OF_DEXTERITY + mind * MANA_BY_POINT_OF_MIND;
	}
	
	
	/**
	 * this method decides if the character has hit his ennemy
	 * @param ennemy the ennemy the hero try to hit
	 * @return true the hero has hit him, false he hasn't
	 */
	/*
	 * hero's chances to hit is calculated with the level of the attack (melee for melee attack...)
	 * but the penality of your attack depend on the ennemy's maximum level
	 */
	protected boolean hasHit(Characters ennemy) {
		return rand(1, 100) < (DEFAUT_CHANCES_TO_HIT + (PENALITY_CHANCE_TO_HIT * (getAttackLevel() - ennemy.getCharacterLevel())) );
	}
	
	
	/**
	 * this method caculate the damage reduce percentage of the armor
	 * it avoid to have some complicated calculus
	 * @return damage reduce percentage
	 */
	protected int reduceDamageByArmor(){
		return (int) (25 * Math.log((armor+300)/300));
	}
	
	/**
	 * this method gets the level of the competence used (melee level for melee attck for instance)
	 * @return the level corresponding to the attack used
	 */
	private int getAttackLevel(){
		int result = 0;
		switch (attack_type) {
		case 2 :
			result = magical_level;
			break;
		case 1 :
			result = ranged_level;
			break;
		case 0 :
			result = melee_level;
			break;
		}
		return result;
	}



}
