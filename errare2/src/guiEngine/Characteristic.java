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
	
	private Rectangle characteristicSite;
	private Rectangle onOffSite;
	private Rectangle nameSite;
	private Rectangle rankSite;
	private Rectangle meleeXPSite;
	private Rectangle rangedXPSite;
	private Rectangle magicalXPSite;
	private Rectangle lifeSite;
	private Rectangle manaSite;
	private Rectangle strenghtSite;
	private Rectangle dexteritySite;
	private Rectangle mindSite;
	private Rectangle hitSite;
	private Rectangle armorSite;
	private Rectangle meleeResistSite;
	private Rectangle rangedResistSite;
	private Rectangle fireResistSite;
	private Rectangle iceResistSite;
	private Rectangle thunderResistSite;
	
	private String name;
	private String rank;
	private String meleeXP;
	private String rangedXP;
	private String magicalXP;
	private String life;
	private String mana;
	private String strenght;
	private String dexterity;
	private String mind;
	private String hit;
	private String armor;
	private String meleeResist;
	private String rangedResist;
	private String fireResist;
	private String iceResist;
	private String thunderResist;
	
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
