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

import xmlEngine.XmlEngine;


public class Equipement {
	
	private Rectangle equipementOpenSite;
	private Rectangle equipementCloseSite;
	private Rectangle equipementDragBarSite;
	private Rectangle upOpenSite;
	private Rectangle upCloseSite;
	private Rectangle lockOpenSite;
	private Rectangle lockCloseSite;
	private Rectangle closeOpenSite;
	private Rectangle closeCloseSite;
	private Rectangle set1OpenSite;
	private Rectangle set2OpenSite;
	private Rectangle set01OpenSite;
	private Rectangle set02OpenSite;
	private Rectangle set03OpenSite;
	private Rectangle set04OpenSite;
	private Rectangle set05OpenSite;
	private Rectangle set06OpenSite;
	private Rectangle set07OpenSite;
	private Rectangle set08OpenSite;
	private Rectangle set09OpenSite;
	private Rectangle set10OpenSite;
	private Rectangle set11OpenSite;
	private Rectangle set12OpenSite;
	private Rectangle set13OpenSite;
	private Rectangle set14OpenSite;
	private Rectangle set15OpenSite;
	private Rectangle set16OpenSite;
	private Rectangle set17OpenSite;
	private Rectangle set18OpenSite;
	private Rectangle set19OpenSite;
	private Rectangle set20OpenSite;
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
	
	public static final int SET1 = 0 ;
	public static final int SET2 = 1 ;
	public static final int MAX_EQUIPEMENT = 20 ;
	public static final int EQUIPEMENT_WIDTH = 248 ;
	public static final int EQUIPEMENT_HEIGHT = 477 ;
	private int activeSet = SET1;
	private boolean visible;
	private boolean drag;
	
	private ActionBar actionBar;
	
	private GuiRep[] tabSet1;
	private GuiRep[] tabSet2;
	
	private int initialPositionX;
	private int initialPositionY;;
	
	private Point dragPoint;
	private Point DownLeftCornerPosition;
	
	public Equipement(ActionBar actionBar) {
		
		this.actionBar = actionBar;
		
		try {
			initialPositionX = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("equipement.position_x")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("equipement.position_x")).intValue();
			initialPositionY = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("equipement.position_y")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("equipement.position_y")).intValue();
			if (new XmlEngine().getString("equipement.open").compareTo("true")==0) {
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
		
		tabSet1 = new GuiRep[MAX_EQUIPEMENT];
		tabSet2 = new GuiRep[MAX_EQUIPEMENT];
		
		equipementOpenSite = new Rectangle(initialPositionX,initialPositionY-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
		equipementCloseSite = new Rectangle(initialPositionX,initialPositionY-30,EQUIPEMENT_WIDTH,20);	
		equipementDragBarSite = new Rectangle(initialPositionX,initialPositionY-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,25);
		set1OpenSite = new Rectangle(initialPositionX+25,initialPositionY-437,93,22);
		set2OpenSite = new Rectangle(initialPositionX+130,initialPositionY-437,93,22);
		upCloseSite = new Rectangle(initialPositionX+186,initialPositionY-27,13,14);
		upOpenSite = new Rectangle(initialPositionX+186,initialPositionY-474,13,14);
		lockCloseSite = new Rectangle(initialPositionX+204,initialPositionY-27,13,14);
		lockOpenSite = new Rectangle(initialPositionX+204,initialPositionY-474,13,14);
		closeCloseSite = new Rectangle(initialPositionX+222,initialPositionY-27,13,14);
		closeOpenSite = new Rectangle(initialPositionX+222,initialPositionY-474,13,14);
		
		set01OpenSite = new Rectangle(initialPositionX+13,initialPositionY-405,33,33);
		set02OpenSite = new Rectangle(initialPositionX+13,initialPositionY-367,33,33); 
		set03OpenSite = new Rectangle(initialPositionX+13,initialPositionY-329,33,33); 
		set04OpenSite = new Rectangle(initialPositionX+13,initialPositionY-291,33,33); 
		set05OpenSite = new Rectangle(initialPositionX+13,initialPositionY-253,33,33); 
		set06OpenSite = new Rectangle(initialPositionX+13,initialPositionY-215,33,33); 
		set07OpenSite = new Rectangle(initialPositionX+13,initialPositionY-177,33,33); 
		set08OpenSite = new Rectangle(initialPositionX+13,initialPositionY-139,33,33); 
		set09OpenSite = new Rectangle(initialPositionX+46,initialPositionY-90,33,33); 
		set10OpenSite = new Rectangle(initialPositionX+87,initialPositionY-90,33,33); 
		set11OpenSite = new Rectangle(initialPositionX+128,initialPositionY-90,33,33); 
		set12OpenSite = new Rectangle(initialPositionX+169,initialPositionY-90,33,33); 
		set13OpenSite = new Rectangle(initialPositionX+202,initialPositionY-405,33,33); 
		set14OpenSite = new Rectangle(initialPositionX+202,initialPositionY-367,33,33); 
		set15OpenSite = new Rectangle(initialPositionX+202,initialPositionY-329,33,33); 
		set16OpenSite = new Rectangle(initialPositionX+202,initialPositionY-291,33,33); 
		set17OpenSite = new Rectangle(initialPositionX+202,initialPositionY-253,33,33); 
		set18OpenSite = new Rectangle(initialPositionX+202,initialPositionY-215,33,33); 
		set19OpenSite = new Rectangle(initialPositionX+202,initialPositionY-177,33,33);  
		set20OpenSite = new Rectangle(initialPositionX+202,initialPositionY-139,33,33); 
	
	}

	// TODO CHANGE GUIREP TO ITEMREP
	public void addEquipement(GuiRep equipement, int id, int set) {
		
		if (set==SET1) {
			if (tabSet1[id]==null){
				tabSet1[id]=equipement;
			}
		}else if (set==SET2) {
			if (tabSet2[id]==null){
				tabSet2[id]=equipement;
			}
		}
	}
	
	public void removeEquipement(int id,int set) {
		if (set==SET1) {
			if (tabSet1[id]!=null){
				tabSet1[id]=null;
			}
		}else if (set==SET2) {
			if (tabSet2[id]!=null){
				tabSet2[id]=null;
			}
		}
	}
	
	public void changeSet(int set) {
		
		if (visible) activeSet=set;
	}
	
	public boolean equipementFull() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void open() {
		
		if (!visible) {
			visible=true;	
			this.setDefaultPosition();
		}		
	}
	
	public void close() {
		
		if (visible) {
			visible=false;
			this.setDefaultPosition();
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
		equipementDragBarSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,25);
		set1OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-437,93,22);
		set2OpenSite = new Rectangle((int)getPosition().getX()+130,(int)getPosition().getY()-437,93,22);
		upCloseSite = new Rectangle((int)getPosition().getX()+186,(int)getPosition().getY()-65,13,14);
		upOpenSite = new Rectangle((int)getPosition().getX()+186,(int)getPosition().getY()-474,13,14);
		lockCloseSite = new Rectangle((int)getPosition().getX()+204,(int)getPosition().getY()-65,13,14);
		lockOpenSite = new Rectangle((int)getPosition().getX()+204,(int)getPosition().getY()-474,13,14);
		closeCloseSite = new Rectangle((int)getPosition().getX()+222,(int)getPosition().getY()-65,13,14);
		closeOpenSite = new Rectangle((int)getPosition().getX()+222,(int)getPosition().getY()-474,13,14);
		
		set01OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-405,33,33);
		set02OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-367,33,33); 
		set03OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-329,33,33); 
		set04OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-291,33,33); 
		set05OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-253,33,33); 
		set06OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-215,33,33); 
		set07OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-177,33,33); 
		set08OpenSite = new Rectangle((int)getPosition().getX()+13,(int)getPosition().getY()-139,33,33); 
		set09OpenSite = new Rectangle((int)getPosition().getX()+46,(int)getPosition().getY()-90,33,33); 
		set10OpenSite = new Rectangle((int)getPosition().getX()+87,(int)getPosition().getY()-90,33,33); 
		set11OpenSite = new Rectangle((int)getPosition().getX()+128,(int)getPosition().getY()-90,33,33); 
		set12OpenSite = new Rectangle((int)getPosition().getX()+169,(int)getPosition().getY()-90,33,33); 
		set13OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-405,33,33); 
		set14OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-367,33,33); 
		set15OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-329,33,33); 
		set16OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-291,33,33); 
		set17OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-253,33,33); 
		set18OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-215,33,33); 
		set19OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-177,33,33);  
		set20OpenSite = new Rectangle((int)getPosition().getX()+202,(int)getPosition().getY()-139,33,33); 
		
	}

	public void setDefaultPosition() {
		
		DownLeftCornerPosition = new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
				
		equipementOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-477,EQUIPEMENT_WIDTH,EQUIPEMENT_HEIGHT);
		equipementCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-30,EQUIPEMENT_WIDTH,20);
		
		equipementDragBarSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-EQUIPEMENT_HEIGHT,EQUIPEMENT_WIDTH,25);
		
		set1OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+25,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-437,93,22);
		set2OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+130,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-437,93,22);
		upCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+186,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		upOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+186,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);
		lockCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+204,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		lockOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+204,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);
		closeCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+222,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		closeOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+222,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);
		
		set01OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-405,33,33);
		set02OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-367,33,33); 
		set03OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-329,33,33); 
		set04OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-291,33,33); 
		set05OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-4253,33,33); 
		set06OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-215,33,33); 
		set07OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-177,33,33); 
		set08OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+13,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-139,33,33); 
		set09OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+46,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-90,33,33); 
		set10OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+87,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-90,33,33); 
		set11OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+128,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-90,33,33); 
		set12OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+169,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-90,33,33); 
		set13OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-405,33,33); 
		set14OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-367,33,33); 
		set15OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-329,33,33); 
		set16OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-291,33,33); 
		set17OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-253,33,33); 
		set18OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-215,33,33); 
		set19OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-177,33,33);  
		set20OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406+202,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-139,33,33); 
	}

	public Point getDefaultPosition() {
		return new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+406,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
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

	public GuiRep[] getTabSet1() {
		return tabSet1;
	}

	public GuiRep[] getTabSet2() {
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

	public Rectangle getSet20OpenSite() {
		return set20OpenSite;
	}
	
	public Rectangle getEquipementDragBarSite() {
		return equipementDragBarSite;
	}

	
}
