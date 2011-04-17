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

import main.ItemRep;

public class Inventory {
	
	private Rectangle inventoryOpenSite,inventoryCloseSite,
	upOpenSite, upCloseSite, lockOpenSite, lockCloseSite, closeOpenSite, closeCloseSite,
	bag01OpenSite,bag02OpenSite,bag03OpenSite,bag04OpenSite,bag05OpenSite,bag06OpenSite,
	bag07OpenSite,bag08OpenSite,bag09OpenSite,content01OpenSite,content02OpenSite,content03OpenSite,
	content04OpenSite,content05OpenSite,content06OpenSite,content07OpenSite,content08OpenSite,
	content09OpenSite,content10OpenSite,content11OpenSite,content12OpenSite,content13OpenSite,
	content14OpenSite,content15OpenSite;
	
	
	private final int headIndex = 0, neck01Index = 1, chest01Index = 2, wristIndex = 3, beltIndex = 4, 
	trousersIndex = 5, tibiaIndex = 6, feetIndex = 7, earIndex = 8, neck02Index = 9, chest02Index = 10,
	ring01Index = 11, ring02Index = 12, bracelet01Index = 13, bracelet02Index = 14, pocketIndex = 15, 
	weapon01Index = 16,	weapon02Index = 17, remoteweaponIndex = 18, ammunitionIndex = 19;
	
	private final int maxEquipment = 20;
	
	public static final int INVENTORY_WIDTH=163, INVENTORY_HEIGHT=480, NB_BAGS = 9, BAG_CONTENT = 15,
	INVENTORY_BAG01=0, INVENTORY_BAG02=1, INVENTORY_BAG03=2, INVENTORY_BAG04=3, INVENTORY_BAG05=4, INVENTORY_BAG06=5,
	INVENTORY_BAG07=6, INVENTORY_BAG08=7, INVENTORY_BAG09=8;
	private boolean visible,drag;
	
	private ItemRep [] equipment;
	private ItemRep [][] bag;
	
	private Point dragPoint, DownLeftCornerPosition;
	
	private ActionBar actionBar;
	
	private int initialPositionX, initialPositionY;
	
	private int activeBag=INVENTORY_BAG01;
	
	public Inventory(ActionBar actionBar) {
		
		this.actionBar = actionBar;
		
		try {
			initialPositionX = new Integer(new DatabaseEngine().getString("inventory.position_x")).intValue();
			initialPositionY = new Integer(new DatabaseEngine().getString("inventory.position_y")).intValue();
			if (new DatabaseEngine().getString("inventory.open").compareTo("true")==0) {
				visible = true;
			}else {
				visible = false;
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		equipment = new ItemRep[maxEquipment];
		bag = new ItemRep[NB_BAGS][BAG_CONTENT];
		
		inventoryOpenSite = new Rectangle(initialPositionX,initialPositionY-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
		inventoryCloseSite = new Rectangle(initialPositionX,initialPositionY-30,INVENTORY_WIDTH,20);
		upCloseSite = new Rectangle(initialPositionX+101,initialPositionY-25,15,15);
		upOpenSite = new Rectangle(initialPositionX+101,initialPositionY-477,15,15);
		lockCloseSite = new Rectangle(initialPositionX+119,initialPositionY-25,15,15);
		lockOpenSite = new Rectangle(initialPositionX+119,initialPositionY-477,15,15);
		closeCloseSite = new Rectangle(initialPositionX+137,initialPositionY-25,15,15);
		closeOpenSite = new Rectangle(initialPositionX+137,initialPositionY-477,15,15);
		bag01OpenSite = new Rectangle(initialPositionX+24,initialPositionY-423,34,34);
		bag02OpenSite = new Rectangle(initialPositionX+64,initialPositionY-423,34,34);
		bag03OpenSite = new Rectangle(initialPositionX+104,initialPositionY-423,34,34);
		bag04OpenSite = new Rectangle(initialPositionX+24,initialPositionY-383,34,34);
		bag05OpenSite = new Rectangle(initialPositionX+64,initialPositionY-383,34,34);
		bag06OpenSite = new Rectangle(initialPositionX+104,initialPositionY-383,34,34);
		bag07OpenSite = new Rectangle(initialPositionX+24,initialPositionY-343,34,34);
		bag08OpenSite = new Rectangle(initialPositionX+64,initialPositionY-343,34,34);
		bag09OpenSite = new Rectangle(initialPositionX+104,initialPositionY-343,34,34);
		content01OpenSite = new Rectangle(initialPositionX+25,initialPositionY-261,34,34);
		content02OpenSite = new Rectangle(initialPositionX+65,initialPositionY-261,34,34);
		content03OpenSite = new Rectangle(initialPositionX+105,initialPositionY-261,34,34);
		content04OpenSite = new Rectangle(initialPositionX+25,initialPositionY-220,34,34);
		content05OpenSite = new Rectangle(initialPositionX+65,initialPositionY-220,34,34);
		content06OpenSite = new Rectangle(initialPositionX+105,initialPositionY-220,34,34);
		content07OpenSite = new Rectangle(initialPositionX+25,initialPositionY-179,34,34);
		content08OpenSite = new Rectangle(initialPositionX+65,initialPositionY-179,34,34);
		content09OpenSite = new Rectangle(initialPositionX+105,initialPositionY-179,34,34);
		content10OpenSite = new Rectangle(initialPositionX+25,initialPositionY-138,34,34);
		content11OpenSite = new Rectangle(initialPositionX+65,initialPositionY-138,34,34);
		content12OpenSite = new Rectangle(initialPositionX+105,initialPositionY-138,34,34);
		content13OpenSite = new Rectangle(initialPositionX+25,initialPositionY-97,34,34);
		content14OpenSite = new Rectangle(initialPositionX+65,initialPositionY-97,34,34);
		content15OpenSite = new Rectangle(initialPositionX+105,initialPositionY-97,34,34);
		
		drag=false;
		
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
	}
	
	
	public boolean pick(ItemRep item) {
		
		boolean equipmentEmpty=false;
		boolean itemPick=false;
		
		switch (item.getGui().getType()) {
		case 1: equipmentEmpty = (equipment[headIndex] == null) ? true : false; break;
		// ...
		default: break;
		}
		if (equipmentEmpty) {
			itemPick=true;
		}else if (!inventoryFull()) {
			for (int i=0;i<bag.length;i++) {
				for (int j=0;j<bag[0].length;j++) {
					if (bag[i][j] == null) {
						bag[i][j] = item;
						itemPick=true;
					}
				}
			}
		}
		return itemPick;
	}
	
	public void drop(ItemRep item) {
		
	}
	
	public boolean bagFull(ItemRep [] bag) {
		
		boolean full=true;
		
		for (ItemRep item : bag) {
			full = (item == null) ? false : true;
		}	
		return full;
	}
	
	
	
	public boolean inventoryFull() {
		
		boolean full=true;
		
		for (int i=0;i<NB_BAGS;i++) {
			full = bagFull(bag[i]);
			if (!full) return false;
		}
		return full;
	}
	
	
	public void open() {
		

		if (!visible) {
			visible=true;			
			DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
			inventoryOpenSite = new Rectangle(initialPositionX,initialPositionY-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
			upOpenSite = new Rectangle(initialPositionX+101,initialPositionY-477,15,15);
			lockOpenSite = new Rectangle(initialPositionX+119,initialPositionY-477,15,15);
			closeOpenSite = new Rectangle(initialPositionX+137,initialPositionY-477,15,15);
			bag01OpenSite = new Rectangle(initialPositionX+24,initialPositionY-423,34,34);
			bag02OpenSite = new Rectangle(initialPositionX+64,initialPositionY-423,34,34);
			bag03OpenSite = new Rectangle(initialPositionX+104,initialPositionY-423,34,34);
			bag04OpenSite = new Rectangle(initialPositionX+24,initialPositionY-383,34,34);
			bag05OpenSite = new Rectangle(initialPositionX+64,initialPositionY-383,34,34);
			bag06OpenSite = new Rectangle(initialPositionX+104,initialPositionY-383,34,34);
			bag07OpenSite = new Rectangle(initialPositionX+24,initialPositionY-343,34,34);
			bag08OpenSite = new Rectangle(initialPositionX+64,initialPositionY-343,34,34);
			bag09OpenSite = new Rectangle(initialPositionX+104,initialPositionY-343,34,34);
			content01OpenSite = new Rectangle(initialPositionX+25,initialPositionY-261,34,34);
			content02OpenSite = new Rectangle(initialPositionX+65,initialPositionY-261,34,34);
			content03OpenSite = new Rectangle(initialPositionX+105,initialPositionY-261,34,34);
			content04OpenSite = new Rectangle(initialPositionX+25,initialPositionY-220,34,34);
			content05OpenSite = new Rectangle(initialPositionX+65,initialPositionY-220,34,34);
			content06OpenSite = new Rectangle(initialPositionX+105,initialPositionY-220,34,34);
			content07OpenSite = new Rectangle(initialPositionX+25,initialPositionY-179,34,34);
			content08OpenSite = new Rectangle(initialPositionX+65,initialPositionY-179,34,34);
			content09OpenSite = new Rectangle(initialPositionX+105,initialPositionY-179,34,34);
			content10OpenSite = new Rectangle(initialPositionX+25,initialPositionY-138,34,34);
			content11OpenSite = new Rectangle(initialPositionX+65,initialPositionY-138,34,34);
			content12OpenSite = new Rectangle(initialPositionX+105,initialPositionY-138,34,34);
			content13OpenSite = new Rectangle(initialPositionX+25,initialPositionY-97,34,34);
			content14OpenSite = new Rectangle(initialPositionX+65,initialPositionY-97,34,34);
			content15OpenSite = new Rectangle(initialPositionX+105,initialPositionY-97,34,34);
		}
		
	}
	
public void changeBag(int newBag) {
		
		activeBag=newBag;
	}

	public void close() {
		
		if (visible) {
			visible=false;
			DownLeftCornerPosition = new Point(661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
			inventoryCloseSite = new Rectangle(661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-30,INVENTORY_WIDTH,20);
			upCloseSite = new Rectangle(762,actionBar.getScreenHeight()-65,15,15);
			lockCloseSite = new Rectangle(780,actionBar.getScreenHeight()-65,15,15);
			closeCloseSite = new Rectangle(798,actionBar.getScreenHeight()-65,15,15);
		}
	
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
			if (p.getY()-INVENTORY_HEIGHT<=0) {
				p = new Point((int)p.getX(),INVENTORY_HEIGHT);
			}
			// Right
			if (p.getX()+INVENTORY_WIDTH>=actionBar.getScreenWidth()) {
				p = new Point(actionBar.getScreenWidth()-INVENTORY_WIDTH,(int)p.getY());
			}
			
			// Down
			if (p.getY()+ActionBar.ACTIONBAR_HEIGHT>=actionBar.getScreenHeight()) {
				p = new Point((int)p.getX(),actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
			}
			
			setPosition(p);
			drag=false;
			
		}
		
	}
	
	public boolean isDragged() {
		
		return drag;
	}
	
	public Point getDragPoint() {
		return dragPoint;
		
	}
	
	public Point getPosition() {
		return DownLeftCornerPosition;
	}
	
	public void setPosition(Point DownLeftCornerPosition) {
		
		this.DownLeftCornerPosition = DownLeftCornerPosition;
	
		inventoryOpenSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
		inventoryCloseSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-30,INVENTORY_WIDTH,20);
		upCloseSite = new Rectangle((int)getPosition().getX()+101,(int)getPosition().getY()-25,15,15);
		upOpenSite = new Rectangle((int)getPosition().getX()+101,(int)getPosition().getY()-477,15,15);
		lockCloseSite = new Rectangle((int)getPosition().getX()+119,(int)getPosition().getY()-25,15,15);
		lockOpenSite = new Rectangle((int)getPosition().getX()+119,(int)getPosition().getY()-477,15,15);
		closeCloseSite = new Rectangle((int)getPosition().getX()+137,(int)getPosition().getY()-25,15,15);
		closeOpenSite = new Rectangle((int)getPosition().getX()+137,(int)getPosition().getY()-477,15,15);		
		bag01OpenSite = new Rectangle((int)getPosition().getX()+24,(int)getPosition().getY()-423,34,34);
		bag02OpenSite = new Rectangle((int)getPosition().getX()+64,(int)getPosition().getY()-423,34,34);
		bag03OpenSite = new Rectangle((int)getPosition().getX()+104,(int)getPosition().getY()-423,34,34);
		bag04OpenSite = new Rectangle((int)getPosition().getX()+24,(int)getPosition().getY()-383,34,34);
		bag05OpenSite = new Rectangle((int)getPosition().getX()+64,(int)getPosition().getY()-383,34,34);
		bag06OpenSite = new Rectangle((int)getPosition().getX()+104,(int)getPosition().getY()-383,34,34);
		bag07OpenSite = new Rectangle((int)getPosition().getX()+24,(int)getPosition().getY()-343,34,34);
		bag08OpenSite = new Rectangle((int)getPosition().getX()+64,(int)getPosition().getY()-343,34,34);
		bag09OpenSite = new Rectangle((int)getPosition().getX()+104,(int)getPosition().getY()-343,34,34);
		content01OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-261,34,34);
		content02OpenSite = new Rectangle((int)getPosition().getX()+65,(int)getPosition().getY()-261,34,34);
		content03OpenSite = new Rectangle((int)getPosition().getX()+105,(int)getPosition().getY()-261,34,34);
		content04OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-220,34,34);
		content05OpenSite = new Rectangle((int)getPosition().getX()+65,(int)getPosition().getY()-220,34,34);
		content06OpenSite = new Rectangle((int)getPosition().getX()+105,(int)getPosition().getY()-220,34,34);
		content07OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-179,34,34);
		content08OpenSite = new Rectangle((int)getPosition().getX()+65,(int)getPosition().getY()-179,34,34);
		content09OpenSite = new Rectangle((int)getPosition().getX()+105,(int)getPosition().getY()-179,34,34);
		content10OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-138,34,34);
		content11OpenSite = new Rectangle((int)getPosition().getX()+65,(int)getPosition().getY()-138,34,34);
		content12OpenSite = new Rectangle((int)getPosition().getX()+105,(int)getPosition().getY()-138,34,34);
		content13OpenSite = new Rectangle((int)getPosition().getX()+25,(int)getPosition().getY()-97,34,34);
		content14OpenSite = new Rectangle((int)getPosition().getX()+65,(int)getPosition().getY()-97,34,34);
		content15OpenSite = new Rectangle((int)getPosition().getX()+105,(int)getPosition().getY()-97,34,34);
		
	}
	
	public void setDefaultPosition() {
		
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
		inventoryOpenSite = new Rectangle(initialPositionX,initialPositionY-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
		inventoryCloseSite = new Rectangle(initialPositionX,initialPositionY-30,INVENTORY_WIDTH,20);
		upCloseSite = new Rectangle(initialPositionX+106,initialPositionY-25,15,15);
		upOpenSite = new Rectangle(initialPositionX+106,initialPositionY-477,15,15);
		lockCloseSite = new Rectangle(initialPositionX+204,initialPositionY-25,15,15);
		lockOpenSite = new Rectangle(initialPositionX+204,initialPositionY-477,15,15);
		closeCloseSite = new Rectangle(initialPositionX+222,initialPositionY-25,15,15);
		closeOpenSite = new Rectangle(initialPositionX+222,initialPositionY-477,15,15);		
		bag01OpenSite = new Rectangle(initialPositionX+24,initialPositionY-423,34,34);
		bag02OpenSite = new Rectangle(initialPositionX+64,initialPositionY-423,34,34);
		bag03OpenSite = new Rectangle(initialPositionX+104,initialPositionY-423,34,34);
		bag04OpenSite = new Rectangle(initialPositionX+24,initialPositionY-383,34,34);
		bag05OpenSite = new Rectangle(initialPositionX+64,initialPositionY-383,34,34);
		bag06OpenSite = new Rectangle(initialPositionX+104,initialPositionY-383,34,34);
		bag07OpenSite = new Rectangle(initialPositionX+24,initialPositionY-343,34,34);
		bag08OpenSite = new Rectangle(initialPositionX+64,initialPositionY-343,34,34);
		bag09OpenSite = new Rectangle(initialPositionX+104,initialPositionY-343,34,34);
		content01OpenSite = new Rectangle(initialPositionX+25,initialPositionY-261,34,34);
		content02OpenSite = new Rectangle(initialPositionX+65,initialPositionY-261,34,34);
		content03OpenSite = new Rectangle(initialPositionX+105,initialPositionY-261,34,34);
		content04OpenSite = new Rectangle(initialPositionX+25,initialPositionY-220,34,34);
		content05OpenSite = new Rectangle(initialPositionX+65,initialPositionY-220,34,34);
		content06OpenSite = new Rectangle(initialPositionX+105,initialPositionY-220,34,34);
		content07OpenSite = new Rectangle(initialPositionX+25,initialPositionY-179,34,34);
		content08OpenSite = new Rectangle(initialPositionX+65,initialPositionY-179,34,34);
		content09OpenSite = new Rectangle(initialPositionX+105,initialPositionY-179,34,34);
		content10OpenSite = new Rectangle(initialPositionX+25,initialPositionY-138,34,34);
		content11OpenSite = new Rectangle(initialPositionX+65,initialPositionY-138,34,34);
		content12OpenSite = new Rectangle(initialPositionX+105,initialPositionY-138,34,34);
		content13OpenSite = new Rectangle(initialPositionX+25,initialPositionY-97,34,34);
		content14OpenSite = new Rectangle(initialPositionX+65,initialPositionY-97,34,34);
		content15OpenSite = new Rectangle(initialPositionX+105,initialPositionY-97,34,34);
	}
	
	public Point getDefaultPosition() {
		return new Point((actionBar.getScreenWidth()-1024)/2+20,actionBar.getScreenHeight());
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public ItemRep[][] getBag() {
		return bag;
	}


	public Rectangle getBag01OpenSite() {
		return bag01OpenSite;
	}


	public Rectangle getBag02OpenSite() {
		return bag02OpenSite;
	}


	public Rectangle getBag03OpenSite() {
		return bag03OpenSite;
	}


	public Rectangle getBag04OpenSite() {
		return bag04OpenSite;
	}


	public Rectangle getBag05OpenSite() {
		return bag05OpenSite;
	}


	public Rectangle getBag06OpenSite() {
		return bag06OpenSite;
	}


	public Rectangle getBag07OpenSite() {
		return bag07OpenSite;
	}


	public Rectangle getBag08OpenSite() {
		return bag08OpenSite;
	}


	public Rectangle getBag09OpenSite() {
		return bag09OpenSite;
	}


	public Rectangle getCloseCloseSite() {
		return closeCloseSite;
	}


	public Rectangle getCloseOpenSite() {
		return closeOpenSite;
	}


	public Rectangle getContent01OpenSite() {
		return content01OpenSite;
	}


	public Rectangle getContent02OpenSite() {
		return content02OpenSite;
	}


	public Rectangle getContent03OpenSite() {
		return content03OpenSite;
	}


	public Rectangle getContent04OpenSite() {
		return content04OpenSite;
	}


	public Rectangle getContent05OpenSite() {
		return content05OpenSite;
	}


	public Rectangle getContent06OpenSite() {
		return content06OpenSite;
	}


	public Rectangle getContent07OpenSite() {
		return content07OpenSite;
	}


	public Rectangle getContent08OpenSite() {
		return content08OpenSite;
	}


	public Rectangle getContent09OpenSite() {
		return content09OpenSite;
	}


	public Rectangle getContent10OpenSite() {
		return content10OpenSite;
	}


	public Rectangle getContent11OpenSite() {
		return content11OpenSite;
	}


	public Rectangle getContent12OpenSite() {
		return content12OpenSite;
	}


	public Rectangle getContent13OpenSite() {
		return content13OpenSite;
	}


	public Rectangle getContent14OpenSite() {
		return content14OpenSite;
	}


	public Rectangle getContent15OpenSite() {
		return content15OpenSite;
	}


	public Rectangle getInventoryCloseSite() {
		return inventoryCloseSite;
	}


	public Rectangle getInventoryOpenSite() {
		return inventoryOpenSite;
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


	public int getActiveBag() {
		return activeBag;
	}
	
	
}
