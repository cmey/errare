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

import java.awt.Point;
import java.awt.Rectangle;

import javax.xml.parsers.ParserConfigurationException;

import databaseEngine.DatabaseEngine;

public class Equipement {
	
	private Rectangle equipementOpenSite, equipementCloseSite,onOffSite,
	upOpenSite, upCloseSite, lockOpenSite, lockCloseSite, closeOpenSite, closeCloseSite,
	set1OpenSite, set2OpenSite,
	set01OpenSite,set02OpenSite,set03OpenSite,set04OpenSite,set05OpenSite,set06OpenSite,
	set07OpenSite,set08OpenSite,set09OpenSite,set10OpenSite,set11OpenSite,set12OpenSite,
	set13OpenSite,set14OpenSite,set15OpenSite,set16OpenSite,set17OpenSite,set18OpenSite,set19OpenSite,
	nameSite, rankSite,meleeXPSite, rangedXPSite, magicalXPSite,
	lifeSite, manaSite, strenghtSite, dexteritySite, mindSite,
	hitSite, armorSite, meleeResistSite, rangedResistSite, 
	fireResistSite, iceResistSite, thunderResistSite;
	
	/*
	 * headSite, neck01Site, chest01Site, wristSite, beltSite, trousersSite, tibiaSite, feetSite,
	earSite, neck02Site, chest02Site, ring01Site, ring02Site, bracelet01Site, bracelet02Site, pocketSite,
	weapon01Site, weapon02Site, remoteweaponSite, ammunitionSite;
	 */
	
	private String name, rank, meleeXP, rangedXP, magicalXP, life, mana, strenght,
	dexterity, mind, hit, armor, meleeResist, rangedResist, fireResist,
	iceResist, thunderResist;
	
	public static final int SET1=0, SET2=1, MAX_EQUIPEMENT=19, EQUIPEMENT_WIDTH=247, EQUIPEMENT_HEIGHT=481;
	private int activeSet = SET1;
	private boolean visible,drag;
	
	private ActionBar actionBar;
	
	private int [] tabSet1, tabSet2;
	
	private int initialPositionX, initialPositionY;;
	
	private Point dragPoint, DownLeftCornerPosition;
	
	public Equipement(ActionBar actionBar) {
		
		this.actionBar = actionBar;
		
		try {
			initialPositionX = new Integer(new DatabaseEngine().getString("equipement.position_x")).intValue();
			initialPositionY = new Integer(new DatabaseEngine().getString("equipement.position_y")).intValue();
			if (new DatabaseEngine().getString("equipement.open").compareTo("true")==0) {
				visible = true;
			}else {
				visible = false;
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
		tabSet1 = new int[MAX_EQUIPEMENT];
		tabSet2 = new int[MAX_EQUIPEMENT];
		
		equipementOpenSite = new Rectangle(initialPositionX,initialPositionY-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
		equipementCloseSite = new Rectangle(initialPositionX,initialPositionY-30,EQUIPEMENT_WIDTH,20);	
		set1OpenSite = new Rectangle(initialPositionX+30,initialPositionY-440,85,16);
		set2OpenSite = new Rectangle(initialPositionX+133,initialPositionY-440,85,16);
		upCloseSite = new Rectangle(initialPositionX+186,initialPositionY-25,15,15);
		upOpenSite = new Rectangle(initialPositionX+186,initialPositionY-477,15,15);
		lockCloseSite = new Rectangle(initialPositionX+204,initialPositionY-25,15,15);
		lockOpenSite = new Rectangle(initialPositionX+204,initialPositionY-477,15,15);
		closeCloseSite = new Rectangle(initialPositionX+222,initialPositionY-25,15,15);
		closeOpenSite = new Rectangle(initialPositionX+222,initialPositionY-477,15,15);
		
		set01OpenSite = new Rectangle(initialPositionX+17,initialPositionY-416,34,34);
		set02OpenSite = new Rectangle(initialPositionX+17,initialPositionY-377,34,34); 
		set03OpenSite = new Rectangle(initialPositionX+17,initialPositionY-338,34,34); 
		set04OpenSite = new Rectangle(initialPositionX+17,initialPositionY-299,34,34); 
		set05OpenSite = new Rectangle(initialPositionX+17,initialPositionY-260,34,34); 
		set06OpenSite = new Rectangle(initialPositionX+17,initialPositionY-221,34,34); 
		set07OpenSite = new Rectangle(initialPositionX+17,initialPositionY-182,34,34); 
		set08OpenSite = new Rectangle(initialPositionX+17,initialPositionY-143,34,34); 
		set09OpenSite = new Rectangle(initialPositionX+66,initialPositionY-123,34,34); 
		set10OpenSite = new Rectangle(initialPositionX+106,initialPositionY-123,34,34); 
		set11OpenSite = new Rectangle(initialPositionX+146,initialPositionY-123,34,34); 
		set12OpenSite = new Rectangle(initialPositionX+196,initialPositionY-416,34,34); 
		set13OpenSite = new Rectangle(initialPositionX+196,initialPositionY-377,34,34); 
		set14OpenSite = new Rectangle(initialPositionX+196,initialPositionY-338,34,34); 
		set15OpenSite = new Rectangle(initialPositionX+196,initialPositionY-299,34,34); 
		set16OpenSite = new Rectangle(initialPositionX+196,initialPositionY-260,34,34); 
		set17OpenSite = new Rectangle(initialPositionX+196,initialPositionY-221,34,34); 
		set18OpenSite = new Rectangle(initialPositionX+196,initialPositionY-182,34,34); 
		set19OpenSite = new Rectangle(initialPositionX+196,initialPositionY-143,34,34); 
		
	
	}

	public void addEquipement(int equipement, int id, int set) {
		
		if (set==SET1) {
			if (tabSet1[id]==0){
				tabSet1[id]=equipement;
			}
		}else if (set==SET2) {
			if (tabSet2[id]==0){
				tabSet2[id]=equipement;
			}
		}
	}
	
	public void changeSet(int set) {
		
		if (visible) activeSet=set;
	}
	
	public void open() {
		
		if (!visible) {
			visible=true;			
			DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
			
			equipementOpenSite = new Rectangle(initialPositionX,initialPositionY-477,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
			set1OpenSite = new Rectangle(initialPositionX+30,initialPositionY-440,85,16);
			set2OpenSite = new Rectangle(initialPositionX+133,initialPositionY-440,85,16);
			upOpenSite = new Rectangle(initialPositionX+186,initialPositionY-477,15,15);
			lockOpenSite = new Rectangle(initialPositionX+204,initialPositionY-477,15,15);
			closeOpenSite = new Rectangle(initialPositionX+222,initialPositionY-477,15,15);
			
			set01OpenSite = new Rectangle(initialPositionX+17,initialPositionY-416,34,34);
			set02OpenSite = new Rectangle(initialPositionX+17,initialPositionY-377,34,34); 
			set03OpenSite = new Rectangle(initialPositionX+17,initialPositionY-338,34,34); 
			set04OpenSite = new Rectangle(initialPositionX+17,initialPositionY-299,34,34); 
			set05OpenSite = new Rectangle(initialPositionX+17,initialPositionY-260,34,34); 
			set06OpenSite = new Rectangle(initialPositionX+17,initialPositionY-221,34,34); 
			set07OpenSite = new Rectangle(initialPositionX+17,initialPositionY-182,34,34); 
			set08OpenSite = new Rectangle(initialPositionX+17,initialPositionY-143,34,34); 
			set09OpenSite = new Rectangle(initialPositionX+66,initialPositionY-123,34,34); 
			set10OpenSite = new Rectangle(initialPositionX+106,initialPositionY-123,34,34); 
			set11OpenSite = new Rectangle(initialPositionX+146,initialPositionY-123,34,34); 
			set12OpenSite = new Rectangle(initialPositionX+196,initialPositionY-416,34,34); 
			set13OpenSite = new Rectangle(initialPositionX+196,initialPositionY-377,34,34); 
			set14OpenSite = new Rectangle(initialPositionX+196,initialPositionY-338,34,34); 
			set15OpenSite = new Rectangle(initialPositionX+196,initialPositionY-299,34,34); 
			set16OpenSite = new Rectangle(initialPositionX+196,initialPositionY-260,34,34); 
			set17OpenSite = new Rectangle(initialPositionX+196,initialPositionY-221,34,34); 
			set18OpenSite = new Rectangle(initialPositionX+196,initialPositionY-182,34,34); 
			set19OpenSite = new Rectangle(initialPositionX+196,initialPositionY-143,34,34); 
		}
				
	}
	
	public void close() {
		
		if (visible) {
			visible=false;
			DownLeftCornerPosition = new Point(406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
			
			equipementCloseSite = new Rectangle(406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-30,EQUIPEMENT_WIDTH,20);
			upCloseSite = new Rectangle(593,actionBar.getScreenHeight()-65,15,15);
			lockCloseSite = new Rectangle(611,actionBar.getScreenHeight()-65,15,15);
			closeCloseSite = new Rectangle(629,actionBar.getScreenHeight()-65,15,15);
			
		}
		
	}

	public boolean isVisible() {
		return visible;
	}

	public void dragIt(Point point) {
		
		if (!drag) {
			drag=true;
			dragPoint = point;
		}
	}

	public void dropIt(Point point) {
		
		if (drag) {
			
			// Down left corner
		Point p = new Point((int)(point.getX()-getDragPoint().getX()+getPosition().getX()),(int)(point.getY()-getDragPoint().getY()+getPosition().getY()));
		
		// Left
		if (p.getX()<=0) {
			p = new Point(0,(int)p.getY());
		}
		// Top
		if (p.getY()-EQUIPEMENT_HEIGHT<=0) {
			p = new Point((int)p.getX(),EQUIPEMENT_HEIGHT);
		}
		// Right
		if (p.getX()+EQUIPEMENT_WIDTH>=actionBar.getScreenWidth()) {
			p = new Point(actionBar.getScreenWidth()-EQUIPEMENT_WIDTH,(int)p.getY());
		}
		
		// Down
		if (p.getY()+ActionBar.ACTIONBAR_HEIGHT>=actionBar.getScreenHeight()) {
			p = new Point((int)p.getX(),actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
		}
	
		setPosition(p);
		drag=false;
		
		}	
	}
	
	public Point getPosition() {
		return DownLeftCornerPosition;
	}

	public void setPosition(Point DownLeftCornerPosition) {
		
		this.DownLeftCornerPosition = DownLeftCornerPosition;
		
		equipementOpenSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
		equipementCloseSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-70,EQUIPEMENT_WIDTH,20);
		set1OpenSite = new Rectangle((int)getPosition().getX()+30,(int)getPosition().getY()-440,85,16);
		set2OpenSite = new Rectangle((int)getPosition().getX()+133,(int)getPosition().getY()-440,85,16);
		upCloseSite = new Rectangle((int)getPosition().getX()+186,(int)getPosition().getY()-65,15,15);
		upOpenSite = new Rectangle((int)getPosition().getX()+186,(int)getPosition().getY()-477,15,15);
		lockCloseSite = new Rectangle((int)getPosition().getX()+204,(int)getPosition().getY()-65,15,15);
		lockOpenSite = new Rectangle((int)getPosition().getX()+204,(int)getPosition().getY()-477,15,15);
		closeCloseSite = new Rectangle((int)getPosition().getX()+222,(int)getPosition().getY()-65,15,15);
		closeOpenSite = new Rectangle((int)getPosition().getX()+222,(int)getPosition().getY()-477,15,15);
	
		set01OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-416,34,34);
		set02OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-377,34,34); 
		set03OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-338,34,34); 
		set04OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-299,34,34); 
		set05OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-260,34,34); 
		set06OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-221,34,34); 
		set07OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-182,34,34); 
		set08OpenSite = new Rectangle((int)getPosition().getX()+17,(int)getPosition().getY()-143,34,34); 
		set09OpenSite = new Rectangle((int)getPosition().getX()+66,(int)getPosition().getY()-123,34,34); 
		set10OpenSite = new Rectangle((int)getPosition().getX()+106,(int)getPosition().getY()-123,34,34); 
		set11OpenSite = new Rectangle((int)getPosition().getX()+146,(int)getPosition().getY()-123,34,34); 
		set12OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-416,34,34); 
		set13OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-377,34,34); 
		set14OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-338,34,34); 
		set15OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-299,34,34); 
		set16OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-260,34,34); 
		set17OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-221,34,34); 
		set18OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-182,34,34); 
		set19OpenSite = new Rectangle((int)getPosition().getX()+196,(int)getPosition().getY()-143,34,34); 
	}

	public void setDefaultPosition() {
		
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
		equipementOpenSite = new Rectangle(initialPositionX,initialPositionY-477,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
		equipementCloseSite = new Rectangle(initialPositionX,initialPositionY-30,EQUIPEMENT_WIDTH,20);
		set1OpenSite = new Rectangle(initialPositionX+30,initialPositionY-440,85,16);
		set2OpenSite = new Rectangle(initialPositionX+133,initialPositionY-440,85,16);
		upCloseSite = new Rectangle(initialPositionX+186,initialPositionY-65,15,15);
		upOpenSite = new Rectangle(initialPositionX+186,initialPositionY-477,15,15);
		lockCloseSite = new Rectangle(initialPositionX+204,initialPositionY-65,15,15);
		lockOpenSite = new Rectangle(initialPositionX+204,initialPositionY-477,15,15);
		closeCloseSite = new Rectangle(initialPositionX+222,initialPositionY-65,15,15);
		closeOpenSite = new Rectangle(initialPositionX+222,initialPositionY-477,15,15);
		
		set01OpenSite = new Rectangle(initialPositionX+17,initialPositionY-416,34,34);
		set02OpenSite = new Rectangle(initialPositionX+17,initialPositionY-377,34,34); 
		set03OpenSite = new Rectangle(initialPositionX+17,initialPositionY-338,34,34); 
		set04OpenSite = new Rectangle(initialPositionX+17,initialPositionY-299,34,34); 
		set05OpenSite = new Rectangle(initialPositionX+17,initialPositionY-260,34,34); 
		set06OpenSite = new Rectangle(initialPositionX+17,initialPositionY-221,34,34); 
		set07OpenSite = new Rectangle(initialPositionX+17,initialPositionY-182,34,34); 
		set08OpenSite = new Rectangle(initialPositionX+17,initialPositionY-143,34,34); 
		set09OpenSite = new Rectangle(initialPositionX+66,initialPositionY-123,34,34); 
		set10OpenSite = new Rectangle(initialPositionX+106,initialPositionY-123,34,34); 
		set11OpenSite = new Rectangle(initialPositionX+146,initialPositionY-123,34,34); 
		set12OpenSite = new Rectangle(initialPositionX+196,initialPositionY-416,34,34); 
		set13OpenSite = new Rectangle(initialPositionX+196,initialPositionY-377,34,34); 
		set14OpenSite = new Rectangle(initialPositionX+196,initialPositionY-338,34,34); 
		set15OpenSite = new Rectangle(initialPositionX+196,initialPositionY-299,34,34); 
		set16OpenSite = new Rectangle(initialPositionX+196,initialPositionY-260,34,34); 
		set17OpenSite = new Rectangle(initialPositionX+196,initialPositionY-221,34,34); 
		set18OpenSite = new Rectangle(initialPositionX+196,initialPositionY-182,34,34); 
		set19OpenSite = new Rectangle(initialPositionX+196,initialPositionY-143,34,34); 
	}

	public Point getDefaultPosition() {
		return new Point((actionBar.getScreenWidth()-1024)/2+20,actionBar.getScreenHeight());
	}
	
	public boolean isDragged() {

		return drag;
	}
	
	public Point getDragPoint() {
		return dragPoint;
		
	}
	
	public Rectangle getCloseCloseSite() {
		return closeCloseSite;
	}

	public Rectangle getCloseOpenSite() {
		return closeOpenSite;
	}

	public Rectangle getEquipementCloseSite() {
		return equipementCloseSite;
	}

	public Rectangle getEquipementOpenSite() {
		return equipementOpenSite;
	}

	public Rectangle getLockCloseSite() {
		return lockCloseSite;
	}

	public Rectangle getLockOpenSite() {
		return lockOpenSite;
	}

	public Rectangle getUpCloseSite() {
		return upCloseSite;
	}

	public Rectangle getUpOpenSite() {
		return upOpenSite;
	}

	public int getActiveSet() {
		return activeSet;
	}

	public Rectangle getSet1OpenSite() {
		return set1OpenSite;
	}

	public Rectangle getSet2OpenSite() {
		return set2OpenSite;
	}

	public int[] getTabSet1() {
		return tabSet1;
	}

	public int[] getTabSet2() {
		return tabSet2;
	}

	public Rectangle getSet01OpenSite() {
		return set01OpenSite;
	}

	public Rectangle getSet02OpenSite() {
		return set02OpenSite;
	}

	public Rectangle getSet03OpenSite() {
		return set03OpenSite;
	}

	public Rectangle getSet04OpenSite() {
		return set04OpenSite;
	}

	public Rectangle getSet05OpenSite() {
		return set05OpenSite;
	}

	public Rectangle getSet06OpenSite() {
		return set06OpenSite;
	}

	public Rectangle getSet07OpenSite() {
		return set07OpenSite;
	}

	public Rectangle getSet08OpenSite() {
		return set08OpenSite;
	}

	public Rectangle getSet09OpenSite() {
		return set09OpenSite;
	}

	public Rectangle getSet10OpenSite() {
		return set10OpenSite;
	}

	public Rectangle getSet11OpenSite() {
		return set11OpenSite;
	}

	public Rectangle getSet12OpenSite() {
		return set12OpenSite;
	}

	public Rectangle getSet13OpenSite() {
		return set13OpenSite;
	}

	public Rectangle getSet14OpenSite() {
		return set14OpenSite;
	}

	public Rectangle getSet15OpenSite() {
		return set15OpenSite;
	}

	public Rectangle getSet16OpenSite() {
		return set16OpenSite;
	}

	public Rectangle getSet17OpenSite() {
		return set17OpenSite;
	}

	public Rectangle getSet18OpenSite() {
		return set18OpenSite;
	}

	public Rectangle getSet19OpenSite() {
		return set19OpenSite;
	}
	
}
