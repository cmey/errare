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
