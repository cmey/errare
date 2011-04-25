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

import main.ItemRep;

/*
 * Created on 15 sept. 2005
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
public class ItemCharacteristics {
	
	/** the life percentage bonus by point of characteristics*/
	public static final int MAGICAL_LIFE = 0;
	
	/** the mana percentage bonus by point of characteristics*/
	public static final int MAGICAL_MANA = 1;
	
	/** the strength percentage bonus by point of characteristics*/
	public static final int MAGICAL_STRENGTH = 2;
	
	/** the dexterity percentage bonus by point of characteristics*/
	public static final int MAGICAL_DEXTERITY = 3;
	
	/** the mind percentage bonus by point of characteristics*/
	public static final int MAGICAL_MIND = 4;
	
	/** the fire resist percentage bonus by point of characteristics*/
	public static final int MAGICAL_FIRE_RESIST = 5;
	
	/** the thunder resist percentage bonus by point of characteristics*/
	public static final int MAGICAL_THUNDER_RESIST = 6;
	
	/** the ice resist percentage bonus by point of characteristics*/
	public static final int MAGICAL_ICE_RESIST = 7;
	
	/** the physical resist percentage bonus by point of characteristics*/
	public static final int MAGICAL_PHYSICAL_RESIST = 8;
	
	/** the block percentage bonus by point of characteristics*/
	public static final int MAGICAL_BLOCK = 9;
	
	/** the armor percentage bonus by point of characteristics*/
	public static final int MAGICAL_ARMOR = 10;
	
	/** the physical damage minimum percentage bonus by point of characteristics*/
	public static final int MAGICAL_PHYSICAL_DAMAGE_MIN = 11;
	
	/** the physical damage maximum percentage bonus by point of characteristics*/
	public static final int MAGICAL_PHYSICAL_DAMAGE_MAX = 12;
	
	/** the elementary damage minimum (fire, ice and thunder) percentage bonus by point of characteristics*/
	public static final int MAGICAL_ELEMENTARY_DAMAGE_MIN = 13;
	/** the elementary damage maximum (fire, ice and thunder) percentage bonus by point of characteristics*/
	public static final int MAGICAL_ELEMENTARY_DAMAGE_MAX = 14;
	
	/** the strength column index in the array*/
	public static final int STRENGTH_COLUMN = 0;
	/** the dexterity column index in the array*/
	public static final int DEXTERITY_COLUMN = 1;
	/** the mind column index in the array*/
	public static final int MIND_COLUMN = 2;
	
	/** number of characteristics*/
	public static final int NUMBER_OF_CHARACTERISTICS = 3;
	
	/** the array of the bonus percentage to a magical attribut by point of characteristics*/
	public static final double[][] MAGICAL_BONUS = { {5,2,1},
	                                                 {1,2,5},
	                                                 {5,2,1},
	                                                 {2,5,1},
	                                                 {1,2,5},
	                                                 {1,2,4},
	                                                 {1,2,4},
	                                                 {1,2,4},
	                                                 {4,2,1},
	                                                 {2,0,5},
	                                                 {1,2,6},
	                                                 {5,3,1},
	                                                 {5,3,1},
	                                                 {1,3,5},
	                                                 {1,3,5}};
	
	/** indicates if the item is magic or not*/
	private boolean magic;
	/** item's name */
	private String name;
	/** gold number (only if this item is gold)*/
	private int gold;
	
	/** item's life bonus*/
	private int magical_life;
	/** item's mana bonus*/
	private int magical_mana;
	/** the strength bonus of the item */
	private int magical_strength;
	/** the dexterity bonus of the item */
	private int magical_dexterity;
	/** the mind bonus of the item */
	private int magical_mind;
	
	/** item's fire resist*/
	private int magical_fire_resist;
	/** item's ice resist*/
	private int magical_ice_resist;
	/** item's thunder resist*/
	private int magical_thunder_resist;
	/** item's physical resist*/
	private int magical_physical_resist;
	
	/** item's chances to block a physical attack */
	private int magical_block_chances;
	
	/** item's magical armor */
	private int magical_armor;
	
	/** item's physical minimum and maximum damage */
	private int magical_physical_damage_min, magical_physical_damage_max; // the physical damage depend of the attack type (ranged for a ranged attack, melee for a melee attack)
	/** item's fire minimum and maximum damage */
	private int magical_fire_damage_min, magical_fire_damage_max;
	/** item's ice minimum and maximum damage */
	private int magical_ice_damage_min, magical_ice_damage_max;
	/** item's thunder minimum and maximum damage */
	private int magical_thunder_damage_min, magical_thunder_damage_max;
	
	/** item's type*/
	private String type;
	/** item's armor*/
	private int armor;
	/** item's damage type*/
	private String damage_type;
	/** item's damage minimum*/
	private int damage_min;
	/** item's damage maximum*/
	private int damage_max;
	/** item's strength required*/
	private int strength_required;
	/** item's dexterity required*/
	private int dexterity_required;
	/** item's mind required*/
	private int mind_required;
	/** item's block chances*/
	private int block_chances;
	/** item's zone effect*/
	private int zone_effect;
	
	/** the rep o an item*/
	private ItemRep rep;
	
	public ItemCharacteristics(){
		//recherche SQL...
	}
	
	public ItemCharacteristics(boolean magic, String name, int gold, int magical_life, int magical_mana, int magical_strength, int magical_dexterity,
			int magical_mind, int magical_fire_resist, int magical_ice_resist, int magical_thunder_resist, int magical_physical_resist, int magical_block_chances,
			int magical_armor, int magical_physical_damage_min, int magical_physical_damage_max, int magical_fire_damage_min, int magical_fire_damage_max,
			int magical_ice_damage_min, int magical_ice_damage_max, int magical_thunder_damage_min, int magical_thunder_damage_max, String type,
			int armor, String damage_type, int damage_min, int damage_max, int strength_required, int dexterity_required, int mind_required,
			int block_chances, int zone_effect){
		this.magic = magic;
		this.name = name;
		this.gold = gold;
		this.magical_life = magical_life;
		this.magical_mana = magical_mana;
		this.magical_strength = magical_strength;
		this.magical_dexterity = magical_dexterity;
		this.magical_mind = magical_mind;
		this.magical_fire_resist = magical_fire_resist;
		this.magical_ice_resist = magical_ice_resist;
		this.magical_thunder_resist = magical_thunder_resist;
		this.magical_physical_resist = magical_physical_resist;
		this.magical_block_chances = magical_block_chances;
		this.magical_armor = magical_armor;
		this.magical_physical_damage_min = magical_physical_damage_min;
		this.magical_physical_damage_max = magical_physical_damage_max;
		this.magical_fire_damage_min = magical_fire_damage_min;
		this.magical_fire_damage_max = magical_fire_damage_max;
		this.magical_ice_damage_min = magical_ice_damage_min;
		this.magical_ice_damage_max = magical_ice_damage_max;
		this.magical_thunder_damage_min = magical_thunder_damage_min;
		this.magical_thunder_damage_max = magical_thunder_damage_max;
		this.type = type;
		this.armor = armor;
		this.damage_type = damage_type;
		this.damage_min = damage_min;
		this.damage_max = damage_max;
		this.strength_required = strength_required;
		this.dexterity_required = dexterity_required;
		this.mind_required = mind_required;
		this.block_chances = block_chances;
		this.zone_effect = zone_effect;
		rep = null;
	}

	/**
	 * @return Returns if the item is magic.
	 */
	public boolean isMagic() {
		return magic;
	}

	/**
	 * @return Returns the armor.
	 */
	public int getArmor() {
		return armor;
	}

	/**
	 * @return Returns the block chances.
	 */
	public int getBlock_chances() {
		return block_chances;
	}

	/**
	 * @return Returns the damage maximum.
	 */
	public int getDamage_max() {
		return damage_max;
	}

	/**
	 * @return Returns the damage minimum.
	 */
	public int getDamage_min() {
		return damage_min;
	}

	/**
	 * @return Returns the type of damage.
	 */
	public String getDamage_type() {
		return damage_type;
	}

	/**
	 * @return Returns the dexterity required.
	 */
	public int getDexterity_required() {
		return dexterity_required;
	}

	/**
	 * @return Returns the gold.
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * @return Returns the magical armor.
	 */
	public int getMagical_armor() {
		return magical_armor;
	}

	/**
	 * @return Returns the magical block chances.
	 */
	public int getMagical_block_chances() {
		return magical_block_chances;
	}

	/**
	 * @return Returns the magical fire damage maximum.
	 */
	public int getMagical_fire_damage_max() {
		return magical_fire_damage_max;
	}

	/**
	 * @return Returns the magical fire damage minimum.
	 */
	public int getMagical_fire_damage_min() {
		return magical_fire_damage_min;
	}

	/**
	 * @return Returns the magical ice damage maximum.
	 */
	public int getMagical_ice_damage_max() {
		return magical_ice_damage_max;
	}

	/**
	 * @return Returns the magical ice damage minimum.
	 */
	public int getMagical_ice_damage_min() {
		return magical_ice_damage_min;
	}

	/**
	 * @return Returns the magical physical damage maximum.
	 */
	public int getMagical_physical_damage_max() {
		return magical_physical_damage_max;
	}

	/**
	 * @return Returns the magical physical damage minimum.
	 */
	public int getMagical_physical_damage_min() {
		return magical_physical_damage_min;
	}

	/**
	 * @return Returns the magical thunder damage maximum.
	 */
	public int getMagical_thunder_damage_max() {
		return magical_thunder_damage_max;
	}

	/**
	 * @return Returns the magical thunder damage minimum.
	 */
	public int getMagical_thunder_damage_min() {
		return magical_thunder_damage_min;
	}

	/**
	 * @return Returns the magical dexterity.
	 */
	public int getMagical_dexterity() {
		return magical_dexterity;
	}

	/**
	 * @return Returns the magical fire resist.
	 */
	public int getMagical_fire_resist() {
		return magical_fire_resist;
	}

	/**
	 * @return Returns the magical ice resist.
	 */
	public int getMagical_ice_resist() {
		return magical_ice_resist;
	}

	/**
	 * @return Returns the magical life.
	 */
	public int getMagical_life() {
		return magical_life;
	}

	/**
	 * @return Returns the magical mana.
	 */
	public int getMagical_mana() {
		return magical_mana;
	}

	/**
	 * @return Returns the magical mind.
	 */
	public int getMagical_mind() {
		return magical_mind;
	}

	/**
	 * @return Returns the magical physical esist.
	 */
	public int getMagical_physical_resist() {
		return magical_physical_resist;
	}

	/**
	 * @return Returns the magical strength.
	 */
	public int getMagical_strength() {
		return magical_strength;
	}

	/**
	 * @return Returns the magical thunder resist.
	 */
	public int getMagical_thunder_resist() {
		return magical_thunder_resist;
	}

	/**
	 * @return Returns the mind required.
	 */
	public int getMind_required() {
		return mind_required;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the strength required.
	 */
	public int getStrength_required() {
		return strength_required;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the zone effect of a spell.
	 */
	public int getZone_effect() {
		return zone_effect;
	}
	
	public void setRep(ItemRep i){
		rep = i;
	}
	
}
