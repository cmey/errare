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


/*
 * Created on 12 sept. 2005
 *
 * 
 * bugs :
 * on va gagner un lvl, on switch l'arme et on lvlup ds une autre cat�gorie (marche sur les attaques � distance du fait du d�calage attaque/calcul)
 */

/**
 * @author DjSteaK
 *
 * this class manage the character controlled by the player : the hero
 */
public class Hero extends Characters {
	
	/** the experience required to gain a point of strength*/
	public static final double EXPERIENCE_STRENGTH_REQUIRED = 5000;
	/** the experience required to gain a point of dexterity*/
	public static final double EXPERIENCE_DEXTERITY_REQUIRED = 5000;
	/** the experience required to gain a point of mind*/
	public static final double EXPERIENCE_MIND_REQUIRED = 5000;
	
	/** the experience required to gain a level (melee, ranged or magical) */
	public static final double EXPERIENCE_LEVEL_REQUIRED = 10000;
	
	/**
	 * the penality or the bonus a hero gain/lost when he kills a monster stronger
	 * or weaker than him
	 */
	public static final double PENALITY_BONUS_OF_EXPERIENCE = 10;
	
	/** the percentage of experience won whitch increase the mind of a magician*/
	public static final int EXPERIENCE_MAGICAL_MIND = 70;
	/** the percentage of experience won whitch increase the dexterity of a magician*/
	public static final int EXPERIENCE_MAGICAL_DEXTERITY = 20;
	/** the percentage of experience won whitch increase the strength of a magician*/
	public static final int EXPERIENCE_MAGICAL_STRENGTH = 10;
	
	/** the percentage of experience won whitch increase the dexterity of a ranged fighter*/
	public static final int EXPERIENCE_RANGED_DEXTERITY = 70;
	/** the percentage of experience won whitch increase the strength of a ranged fighter*/
	public static final int EXPERIENCE_RANGED_STRENGTH = 15;
	/** the percentage of experience won whitch increase the mind of a ranged fighter*/
	public static final int EXPERIENCE_RANGED_MIND = 15;
	
	/** the percentage of experience won whitch increase the strength of a melee fighter*/
	public static final int EXPERIENCE_MELEE_STRENGTH = 70;
	/** the percentage of experience won whitch increase the dexterity of a melee fighter*/
	public static final int EXPERIENCE_MELEE_DEXTERITY = 20;
	/** the percentage of experience won whitch increase the mind of a melee fighter*/
	public static final int EXPERIENCE_MELEE_MIND = 10;
	
	/** the percentage maximum a hero can have against elements (thunder, ice and fire)*/
	public static final double MAX_ELEMENT_RESIST = 75;
	/** the percentage maximum a hero can have against elements physical damage
	 * the damage reduce percentage of the armor is not concerned and added after*/
	public static final double MAX_PHYSICAL_RESIST = 45;
	/** the percentage maximum a hero can block with a shield*/
	public static final double MAX_BLOCK_CHANCES = 50;
	
	/** the experience given by a Hero in PvP mode*/
	public static final double BASE_EXPERIENCE_GIVEN_BY_A_HERO = 100;
	
	
	/*
	 * these variables permitt you to know the resistance of all the objects equipped by the hero
	 * it's very useful when this resist is highter than the maximum allowed : if an
	 * item is unequipped, the resist of the hero might stay at the maximum : but we have
	 * to save the bonus of resistance 'unused'
	 */
	/** the thunder resistance of all the objects equipped by the hero*/
	private int inventory_thunder_resist;
	/** the ice resistance of all the objects equipped by the hero*/
	private int inventory_ice_resist;
	/** the fire resistance of all the objects equipped by the hero*/
	private int inventory_fire_resist;
	/** the physical resistance of all the objects equipped by the hero*/
	private int inventory_physical_resist;
	/** the block chances of all the objects equipped by the hero*/
	private int inventory_block_chances;
	
	/** the strength of all the objects equipped by the hero (plus his base of strength)*/
	private int inventory_strength;
	/** the dexterity of all the objects equipped by the hero (plus his base of dexterity)*/
	private int inventory_dexterity;
	/** the mind of all the objects equipped by the hero (plus his base of mind)*/
	private int inventory_mind;

	/**
	 * this method creates a hero : the character controlled by the player
	 * @param name
	 */
	public Hero(String name, GameEngine game) {
		super(name, game);
		inventory_thunder_resist = thunder_resist;
		inventory_ice_resist = ice_resist;
		inventory_fire_resist = fire_resist;
		inventory_physical_resist = physical_resist;
		inventory_strength = 0;
		inventory_dexterity = 0;
		inventory_mind = 0;
		inventory_block_chances = 0;
		
		damage_physical_min = 20;
		damage_physical_max = 25;
	}
	
	
	/* if 2 or 3 competences have the same level, the characteristics' experiences isn't calculated
	 * so only the competence used gain experience
	 * the penality of experience is based on the best level of a character
	 */
	public void experienceDistributer(double experience_won, int level_ennemy) {
		experience_won *= (1 + (level_ennemy - getCharacterLevel())*PENALITY_BONUS_OF_EXPERIENCE/100);
		if(experience_won > 0){
			switch (getCharacterFirstCompetence()) {
			case 2 :
				experience_magical += experience_won;
				experience_mind += experience_won * EXPERIENCE_MAGICAL_MIND / 100;
				experience_dexterity += experience_won * EXPERIENCE_MAGICAL_DEXTERITY / 100;
				experience_strength += experience_won * EXPERIENCE_MAGICAL_STRENGTH / 100;
				break;
			case 1 :
				experience_ranged += experience_won;
				experience_mind += experience_won * EXPERIENCE_RANGED_MIND / 100;
				experience_dexterity += experience_won * EXPERIENCE_RANGED_DEXTERITY / 100;
				experience_strength += experience_won * EXPERIENCE_RANGED_STRENGTH / 100;
				break;
			case 0 :
				experience_ranged += experience_won;
				experience_mind += experience_won * EXPERIENCE_MELEE_MIND / 100;
				experience_dexterity += experience_won * EXPERIENCE_MELEE_DEXTERITY / 100;
				experience_strength += experience_won * EXPERIENCE_MELEE_STRENGTH / 100;
				break;
			default:
				switch (attack_type) {
				case 2 :
					experience_magical += experience_won;
					break;
				case 1 :
					experience_ranged += experience_won;
					break;
				case 0 :
					experience_ranged += experience_won;
					break;
				}
			break;
			}
			lvlUp();
		}
	}
	
	public void attack(Characters ennemy){
		if(hasHit(ennemy)){
			//System.out.println("paf!");
			int melee = 0, ranged = 0, fire = 0, ice = 0, thunder = 0;
			if(attack_type == 0){
				melee = (int) (rand(damage_physical_min, damage_physical_max) * (1+((double)inventory_strength/100)));
			}
			else if(attack_type == 1)
				ranged = (int) (rand(damage_physical_min, damage_physical_max) * (1+(inventory_dexterity/100)));
			fire = rand(damage_fire_min, damage_fire_max);
			ice = rand(damage_ice_min, damage_ice_max);
			thunder = rand(damage_thunder_min, damage_thunder_max);
			if(attack_type == 2){
				fire *= (1+inventory_mind/100);
				ice *= (1+inventory_mind/100);
				thunder *= (1+inventory_mind/100);
			};
			double experience_won = ennemy.isAttacked(melee, ranged, fire, ice, thunder, getCharacterLevel());
			System.out.println("XP : "+experience_won);
			experienceDistributer(experience_won, ennemy.getCharacterLevel());
		}
	}
	
	/**
	 * this method equip an item on the character.
	 * @param i the item to equip
	 */
	public void equip(ItemCharacteristics rep){
		
		ItemCharacteristics i = rep;
		life_max += magicalAttributGained(i.getMagical_life(), ItemCharacteristics.MAGICAL_LIFE);
		mana_max += magicalAttributGained(i.getMagical_mana(), ItemCharacteristics.MAGICAL_MANA);
		inventory_strength += magicalAttributGained(i.getMagical_strength(), ItemCharacteristics.MAGICAL_STRENGTH);
		inventory_dexterity += magicalAttributGained(i.getMagical_dexterity(), ItemCharacteristics.MAGICAL_DEXTERITY);
		inventory_mind += magicalAttributGained(i.getMagical_mind(), ItemCharacteristics.MAGICAL_MIND);
		inventory_fire_resist += magicalAttributGained(i.getMagical_fire_resist(), ItemCharacteristics.MAGICAL_FIRE_RESIST);
		inventory_thunder_resist += magicalAttributGained(i.getMagical_thunder_resist(), ItemCharacteristics.MAGICAL_THUNDER_RESIST);
		inventory_ice_resist += magicalAttributGained(i.getMagical_ice_resist(), ItemCharacteristics.MAGICAL_ICE_RESIST);
		inventory_physical_resist += magicalAttributGained(i.getMagical_physical_resist(), ItemCharacteristics.MAGICAL_PHYSICAL_RESIST);
		inventory_block_chances += magicalAttributGained(i.getMagical_block_chances(), ItemCharacteristics.MAGICAL_BLOCK);
		armor += magicalAttributGained(i.getMagical_armor(), ItemCharacteristics.MAGICAL_ARMOR);
		damage_physical_min += magicalAttributGained(i.getMagical_physical_damage_min(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MIN);
		damage_physical_max += magicalAttributGained(i.getMagical_physical_damage_max(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MAX);
		damage_fire_min += magicalAttributGained(i.getMagical_fire_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_fire_max += magicalAttributGained(i.getMagical_fire_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_ice_min += magicalAttributGained(i.getMagical_ice_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_ice_max += magicalAttributGained(i.getMagical_ice_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_thunder_min += magicalAttributGained(i.getMagical_thunder_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_thunder_max += magicalAttributGained(i.getMagical_thunder_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		
		//checks for the maximum stats
		if(inventory_thunder_resist > MAX_ELEMENT_RESIST) thunder_resist = (int)MAX_ELEMENT_RESIST;
		else thunder_resist = inventory_thunder_resist;
		if(inventory_fire_resist > MAX_ELEMENT_RESIST) fire_resist = (int)MAX_ELEMENT_RESIST;
		else fire_resist = inventory_fire_resist;
		if(inventory_ice_resist > MAX_ELEMENT_RESIST) ice_resist = (int)MAX_ELEMENT_RESIST;
		else ice_resist = inventory_ice_resist;
		if(inventory_physical_resist > MAX_PHYSICAL_RESIST) physical_resist = (int)MAX_PHYSICAL_RESIST;
		else physical_resist = inventory_physical_resist;
		if(inventory_block_chances > MAX_BLOCK_CHANCES) block_chances = (int)MAX_BLOCK_CHANCES;
		else block_chances = inventory_block_chances;
	}
	
	public void unequip(ItemCharacteristics rep){
		
		ItemCharacteristics i = rep;
		life_max -= magicalAttributGained(i.getMagical_life(), ItemCharacteristics.MAGICAL_LIFE);
		mana_max -= magicalAttributGained(i.getMagical_mana(), ItemCharacteristics.MAGICAL_MANA);
		inventory_strength -= magicalAttributGained(i.getMagical_strength(), ItemCharacteristics.MAGICAL_STRENGTH);
		inventory_dexterity -= magicalAttributGained(i.getMagical_dexterity(), ItemCharacteristics.MAGICAL_DEXTERITY);
		inventory_mind -= magicalAttributGained(i.getMagical_mind(), ItemCharacteristics.MAGICAL_MIND);
		inventory_fire_resist -= magicalAttributGained(i.getMagical_fire_resist(), ItemCharacteristics.MAGICAL_FIRE_RESIST);
		inventory_thunder_resist -= magicalAttributGained(i.getMagical_thunder_resist(), ItemCharacteristics.MAGICAL_THUNDER_RESIST);
		inventory_ice_resist -= magicalAttributGained(i.getMagical_ice_resist(), ItemCharacteristics.MAGICAL_ICE_RESIST);
		inventory_physical_resist -= magicalAttributGained(i.getMagical_physical_resist(), ItemCharacteristics.MAGICAL_PHYSICAL_RESIST);
		inventory_block_chances -= magicalAttributGained(i.getMagical_block_chances(), ItemCharacteristics.MAGICAL_BLOCK);
		armor -= magicalAttributGained(i.getMagical_armor(), ItemCharacteristics.MAGICAL_ARMOR);
		damage_physical_min -= magicalAttributGained(i.getMagical_physical_damage_min(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MIN);
		damage_physical_max -= magicalAttributGained(i.getMagical_physical_damage_max(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MAX);
		damage_fire_min -= magicalAttributGained(i.getMagical_fire_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_fire_max -= magicalAttributGained(i.getMagical_fire_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_ice_min -= magicalAttributGained(i.getMagical_ice_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_ice_max -= magicalAttributGained(i.getMagical_ice_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		damage_thunder_min -= magicalAttributGained(i.getMagical_thunder_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN);
		damage_thunder_max -= magicalAttributGained(i.getMagical_thunder_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		
		//checks for the maximum stats
		if(inventory_thunder_resist < MAX_ELEMENT_RESIST) thunder_resist = inventory_thunder_resist;
		if(inventory_fire_resist < MAX_ELEMENT_RESIST) fire_resist = inventory_fire_resist;
		if(inventory_ice_resist < MAX_ELEMENT_RESIST) ice_resist = inventory_ice_resist;
		if(inventory_physical_resist < MAX_PHYSICAL_RESIST) physical_resist = inventory_physical_resist;
		if(inventory_block_chances < MAX_BLOCK_CHANCES) block_chances = inventory_block_chances;
	}
	
	/** 
	 * this method creates the description of an item
	 * @param ir item to describe
	 * @return an array with the String and his color
	 */
	/*
	 * Type of the array returned : Object[2][7] :  - Object[0][n] : String
	 * 											    - Object[1][n] : Color of the String
	 * 		item's name 				(blue if magic, white else)
	 * 		item's type 				(white)
	 * 		item's damage/defense/block (white)
	 * 		item's strength required	(white or red)
	 * 		item's dexterity required	(white or red)
	 * 		item's mind required		(white or red)
	 * 		item's magical attributes	(blue)
	 * 
	 */
	public String[] getDescription(ItemCharacteristics ir){
		String[] ret = new String[5];
		ItemCharacteristics item = ir;
		
		ret[0] = item.getName();
		
		ret[1] = item.getType();
		
		if(item.getDamage_min() == 0){
			ret[2] = "Armor : "+item.getArmor();
			if(block_chances != 0)
				ret[2] += " - Block chances : "+item.getBlock_chances();
		}else ret[2] = item.getDamage_type()+" damage : "+item.getDamage_min()+" - "+item.getDamage_max();
		
		if(item.getStrength_required() != 0) ret[3] = " Strength required : "+item.getStrength_required();
		if(item.getDexterity_required() != 0) ret[3] += "\nDexterity required : "+item.getDexterity_required();
		if(item.getMind_required() != 0) ret[3] += "\nMind required : "+item.getMind_required();
		
		ret[4] = "";
		if(item.getMagical_life() != 0) ret[4] += "\nLife +"+magicalAttributGained(item.getMagical_life(), ItemCharacteristics.MAGICAL_LIFE);
		if(item.getMagical_mana() != 0) ret[4] += "\nMana +"+magicalAttributGained(item.getMagical_mana(), ItemCharacteristics.MAGICAL_MANA);
		if(item.getMagical_armor() != 0) ret[4] += "\nArmor +"+magicalAttributGained(item.getMagical_armor(), ItemCharacteristics.MAGICAL_ARMOR);
		if(item.getMagical_block_chances() != 0) ret[4] += "\nBlock +"+magicalAttributGained(item.getMagical_block_chances(), ItemCharacteristics.MAGICAL_BLOCK);
		if(item.getMagical_strength() != 0) ret[4] += "\nStrength +"+magicalAttributGained(item.getMagical_strength(), ItemCharacteristics.MAGICAL_STRENGTH);
		if(item.getMagical_dexterity() != 0) ret[4] += "\nDexterity +"+magicalAttributGained(item.getMagical_dexterity(), ItemCharacteristics.MAGICAL_DEXTERITY);
		if(item.getMagical_mind() != 0) ret[4] += "\nMind +"+magicalAttributGained(item.getMagical_mind(), ItemCharacteristics.MAGICAL_MIND);
		if(item.getMagical_fire_damage_min() != 0) ret[4] += "\nFire damage : "+magicalAttributGained(item.getMagical_fire_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN)+" - "+magicalAttributGained(item.getMagical_fire_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		if(item.getMagical_ice_damage_min() != 0) ret[4] += "\nIce damage : "+magicalAttributGained(item.getMagical_ice_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN)+" - "+magicalAttributGained(item.getMagical_ice_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		if(item.getMagical_thunder_damage_min() != 0) ret[4] += "\nThunder damage : "+magicalAttributGained(item.getMagical_thunder_damage_min(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MIN)+" - "+magicalAttributGained(item.getMagical_thunder_damage_max(), ItemCharacteristics.MAGICAL_ELEMENTARY_DAMAGE_MAX);
		if(item.getMagical_physical_damage_min() != 0) ret[4] += "\nPhysical damage : "+magicalAttributGained(item.getMagical_physical_damage_min(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MIN)+" - "+magicalAttributGained(item.getMagical_physical_damage_max(), ItemCharacteristics.MAGICAL_PHYSICAL_DAMAGE_MAX);
		if(item.getMagical_fire_resist() != 0) ret[4] += "\nFire resist +"+magicalAttributGained(item.getMagical_fire_resist(), ItemCharacteristics.MAGICAL_FIRE_RESIST)+"%";
		if(item.getMagical_ice_resist() != 0) ret[4] += "\nIce resist +"+magicalAttributGained(item.getMagical_ice_resist(), ItemCharacteristics.MAGICAL_ICE_RESIST)+"%";
		if(item.getMagical_thunder_resist() != 0) ret[4] += "\nThunder resist +"+magicalAttributGained(item.getMagical_thunder_resist(), ItemCharacteristics.MAGICAL_THUNDER_RESIST)+"%";
		if(item.getMagical_physical_resist() != 0) ret[4] += "\nPhysical resist +"+magicalAttributGained(item.getMagical_physical_resist(), ItemCharacteristics.MAGICAL_PHYSICAL_RESIST)+"%";
		return ret;
	}
	
	public int getStrength(){
		return (inventory_strength + mind);
	}
	
	public int getDexterity(){
		return (inventory_dexterity + mind);
	}
	
	public int getMind(){
		return (inventory_mind + mind);
	}
	
	protected void setManaLifeMax(){
		life_max = BASE_LIFE + inventory_strength * LIFE_BY_POINT_OF_STRENGTH + inventory_dexterity * LIFE_BY_POINT_OF_DEXTERITY + inventory_mind * LIFE_BY_POINT_OF_MIND;
		mana_max = BASE_MANA + inventory_strength * MANA_BY_POINT_OF_STRENGTH + inventory_dexterity * MANA_BY_POINT_OF_DEXTERITY + inventory_mind * MANA_BY_POINT_OF_MIND;
	}
	
	protected double isAttacked(int melee, int ranged, int fire, int ice, int thunder, int level_attacker){
		int damage = damageTaken(melee, ranged, fire, ice, thunder);
		double experience;
		if (life < damage) damage = life;
		life -= damage;
		System.out.println("le hero perd "+damage+" reste "+life);
		experience = (damage/life_max * BASE_EXPERIENCE_GIVEN_BY_A_HERO);
		return experience; //the return value is the experience gained on PvP mode
	}
	
	/**
	 * this method search the main competence of the Hero
	 * if the 3 or just 2 competences has the same level,
	 * @return the main competence of the Hero. 0 : melee, 1 : ranged, 2 : magical. if the 3 or just 2 competences has the same level -1 is returned
	 */
	// this method is useful for distribute the experience between competences and characteristics
	// 0 : melee, 1 : ranged, 2 : magical
	private int getCharacterFirstCompetence(){
		int result = -1;
		if(magical_level > ranged_level && magical_level > melee_level) result = 2; //magician
		if(ranged_level > magical_level && ranged_level > melee_level) result = 1; //rogue
		if(melee_level > ranged_level && melee_level > magical_level) result = 0; //warrior
		return result;
	}
	
	/**
	 * this method manage the levels up (aptitudes and characteristics)
	 */
	private void lvlUp(){
		int dex = 0;
		int str = 0;
		int mnd = 0;
		if ( experience_dexterity >= EXPERIENCE_DEXTERITY_REQUIRED ){
			dexterity ++;
			experience_dexterity -= EXPERIENCE_DEXTERITY_REQUIRED;
			dex = 1;
		}
		if ( experience_strength >= EXPERIENCE_STRENGTH_REQUIRED ){
			strength ++;
			experience_strength -= EXPERIENCE_STRENGTH_REQUIRED;
			str = 1;
		}
		if ( experience_mind >= EXPERIENCE_MIND_REQUIRED ){
			mind ++;
			experience_mind -= EXPERIENCE_MIND_REQUIRED;
			mnd = 1;
		}
		if (dex == 1 || str == 1 || mnd == 1){
			setManaLifeMax();
//			updateBonuses();
		}
		switch (attack_type) {
		case 2 :
			if (experience_magical >= EXPERIENCE_LEVEL_REQUIRED){
				experience_magical -= EXPERIENCE_LEVEL_REQUIRED;
				magical_level ++;
			}
			break;
		case 1 :
			if (experience_ranged >= EXPERIENCE_LEVEL_REQUIRED){
				experience_ranged -= EXPERIENCE_LEVEL_REQUIRED;
				ranged_level ++;
			}
			break;
		case 0 :
			if (experience_melee >= EXPERIENCE_LEVEL_REQUIRED){
				experience_melee -= EXPERIENCE_LEVEL_REQUIRED;
				melee_level ++;
			}
			break;
		}
		
	}
	
	public double getExperience_ranged() {
		return experience_ranged;
	}

}
