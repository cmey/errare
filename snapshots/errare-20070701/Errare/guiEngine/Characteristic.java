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

import java.awt.Rectangle;

public class Characteristic {
	
	private Rectangle characteristicSite,onOffSite,
	nameSite, rankSite,meleeXPSite, rangedXPSite, magicalXPSite,
	lifeSite, manaSite, strenghtSite, dexteritySite, mindSite,
	hitSite, armorSite, meleeResistSite, rangedResistSite, 
	fireResistSite, iceResistSite, thunderResistSite;
	
	private String name, rank, meleeXP, rangedXP, magicalXP, life, mana, strenght,
	dexterity, mind, hit, armor, meleeResist, rangedResist, fireResist,
	iceResist, thunderResist;
	
	private boolean visible;
	
	public Characteristic() {
		
		characteristicSite = new Rectangle(0,0,0,0);
		// ...
		
		visible=false;
	}

	public void open() {
		
		visible=true;		
	}

}
