/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Arnaud KNOBLOCH

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package guiEngine;

import main.ItemRep;

/**
 * Class GuiBelt
 * @author Ak
 * Create and control the belt of the hero
 */

public class GuiBelt {
	
	// The belt
	private ItemRep[] tabBelt;
	
	// The weapons stats ( first weapon and spell )
	private int[] tabWeaponsStates;
	
	/**
	 * GuiBelt Constructo
	 * @param engine the GuiEngine
	 * Create a Belt
	 */
	
	public GuiBelt(GuiEngine engine) {
		
		tabBelt = new ItemRep[4];
		tabWeaponsStates = new int[2];
	}
	
	/**
	 * getItemBelt Fonction
	 * @return tabBelt the potions in the belt of the character
	 */
	
	public ItemRep [] getItemBelt() {
		return tabBelt;
	}
	
	/**
	 * getGuiBelt Fonction
	 * @return tabBelt the potions in the belt of the character
	 */
	
	public GuiRep [] getGuiBelt() {
		ItemRep[] tabItem  = tabBelt.clone();
		GuiRep[] tab = new GuiRep[tabItem.length];
		for (int i=0;i<tabItem.length;i++) {
			if (tabItem[i]!=null) {
				assert tabItem[i]!=null : "Belt null";
				tab[i]=tabItem[i].getGui();
			}
		}
		return tab;
	}
	
	/**
	 * updateBelt fonction
	 * @param beltTab ItemRep[]
	 * Update the content of the belt
	 */
	
	public void updateBelt(ItemRep[] beltTab) {
		this.tabBelt=beltTab;	
	}
	
	/**
	 * updateWeapons fonction
	 * @param tabWeaponsStates int[]
	 * Update the weapons states
	 */
	
	public void updateWeapons(int [] tabWeaponsStates) {
		this.tabWeaponsStates= tabWeaponsStates;	
	}

	/* ----- GET ----- */
	
	public int[] getWeaponsStatesTab() {
		return tabWeaponsStates;
	}
}
