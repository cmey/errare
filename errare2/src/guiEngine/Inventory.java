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

import genericReps.ItemRep;

import java.awt.Point;
import java.awt.Rectangle;

import javax.xml.parsers.ParserConfigurationException;

import xmlEngine.XmlEngine;



public class Inventory {
	
	private Rectangle inventoryOpenSite;
	private Rectangle inventoryCloseSite;
	private Rectangle inventoryDragBarSite;
	private Rectangle upOpenSite;
	private Rectangle upCloseSite;
	private Rectangle lockOpenSite;
	private Rectangle lockCloseSite;
	private Rectangle closeOpenSite;
	private Rectangle closeCloseSite;
	private Rectangle bag01OpenSite;
	private Rectangle bag02OpenSite;
	private Rectangle bag03OpenSite;
	private Rectangle bag04OpenSite;
	private Rectangle bag05OpenSite;
	private Rectangle bag06OpenSite;
	private Rectangle bag07OpenSite;
	private Rectangle bag08OpenSite;
	private Rectangle bag09OpenSite;
	private Rectangle content01OpenSite;
	private Rectangle content02OpenSite;
	private Rectangle content03OpenSite;
	private Rectangle content04OpenSite;
	private Rectangle content05OpenSite;
	private Rectangle content06OpenSite;
	private Rectangle content07OpenSite;
	private Rectangle content08OpenSite;
	private Rectangle content09OpenSite;
	private Rectangle content10OpenSite;
	private Rectangle content11OpenSite;
	private Rectangle content12OpenSite;
	private Rectangle content13OpenSite;
	private Rectangle content14OpenSite;
	private Rectangle content15OpenSite;
	
	
	private final int headIndex = 0 ;
	private final int neck01Index = 1 ;
	private final int chest01Index = 2 ;
	private final int wristIndex = 3 ;
	private final int beltIndex = 4 ;
	private final int trousersIndex = 5 ;
	private final int tibiaIndex = 6 ;
	private final int feetIndex = 7 ;
	private final int earIndex = 8 ;
	private final int neck02Index = 9 ;
	private final int chest02Index = 10 ;
	private final int ring01Index = 11 ;
	private final int ring02Index = 12 ;
	private final int bracelet01Index = 13 ;
	private final int bracelet02Index = 14 ;
	private final int pocketIndex = 15 ;
	private final int weapon01Index = 16 ;
	private final int weapon02Index = 17 ;
	private final int remoteweaponIndex = 18 ;
	private final int ammunitionIndex = 19 ;
	
	private final int maxEquipment = 20;
	
	public static final int INVENTORY_WIDTH = 157 ;
	public static final int INVENTORY_HEIGHT = 477 ;
	public static final int NB_BAGS = 9 ;
	public static final int BAG_CONTENT = 15 ;
	public static final int INVENTORY_BAG01 = 0 ;
	public static final int INVENTORY_BAG02 = 1 ;
	public static final int INVENTORY_BAG03 = 2 ;
	public static final int INVENTORY_BAG04 = 3 ;
	public static final int INVENTORY_BAG05 = 4 ;
	public static final int INVENTORY_BAG06 = 5 ;
	public static final int INVENTORY_BAG07 = 6 ;
	public static final int INVENTORY_BAG08 = 7 ;
	public static final int INVENTORY_BAG09 = 8 ;
	private boolean visible;
	private boolean drag;
	
	private ItemRep [] equipment;
	private ItemRep [][] bag;
	private GuiRep [][] bagGui;
	
	private Point dragPoint;
	private Point DownLeftCornerPosition;
	
	private ActionBar actionBar;
	
	private int initialPositionX;
	private int initialPositionY;
	
	private int activeBag=INVENTORY_BAG01;
	
	public Inventory(ActionBar actionBar) {
		
		this.actionBar = actionBar;
		
		try {
			initialPositionX = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("inventory.position_x")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("inventory.position_x")).intValue();
			initialPositionY = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("inventory.position_y")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("inventory.position_y")).intValue();
			if (new XmlEngine().getString("inventory.open").compareTo("true")==0) {
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
		bagGui = new GuiRep[NB_BAGS][BAG_CONTENT];
		
		inventoryOpenSite = new Rectangle(initialPositionX,initialPositionY-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
		inventoryCloseSite = new Rectangle(initialPositionX,initialPositionY-30,INVENTORY_WIDTH,20);
		inventoryDragBarSite = new Rectangle(initialPositionX,initialPositionY-INVENTORY_HEIGHT,INVENTORY_WIDTH,25);
		upCloseSite = new Rectangle(initialPositionX+101,initialPositionY-27,13,14);
		upOpenSite = new Rectangle(initialPositionX+101,initialPositionY-474,13,14);
		lockCloseSite = new Rectangle(initialPositionX+119,initialPositionY-27,13,14);
		lockOpenSite = new Rectangle(initialPositionX+119,initialPositionY-474,13,14);
		closeCloseSite = new Rectangle(initialPositionX+137,initialPositionY-27,13,14);
		closeOpenSite = new Rectangle(initialPositionX+137,initialPositionY-474,13,14);
		bag01OpenSite = new Rectangle(initialPositionX+21,initialPositionY-413,33,33);
		bag02OpenSite = new Rectangle(initialPositionX+62,initialPositionY-413,33,33);
		bag03OpenSite = new Rectangle(initialPositionX+103,initialPositionY-413,33,33);
		bag04OpenSite = new Rectangle(initialPositionX+21,initialPositionY-373,33,33);
		bag05OpenSite = new Rectangle(initialPositionX+62,initialPositionY-373,33,33);
		bag06OpenSite = new Rectangle(initialPositionX+103,initialPositionY-373,33,33);
		bag07OpenSite = new Rectangle(initialPositionX+21,initialPositionY-332,33,33);
		bag08OpenSite = new Rectangle(initialPositionX+62,initialPositionY-332,33,33);
		bag09OpenSite = new Rectangle(initialPositionX+103,initialPositionY-332,33,33);
		content01OpenSite = new Rectangle(initialPositionX+21,initialPositionY-249,33,33);
		content02OpenSite = new Rectangle(initialPositionX+62,initialPositionY-249,33,33);
		content03OpenSite = new Rectangle(initialPositionX+103,initialPositionY-249,33,33);
		content04OpenSite = new Rectangle(initialPositionX+21,initialPositionY-209,33,33);
		content05OpenSite = new Rectangle(initialPositionX+62,initialPositionY-209,33,33);
		content06OpenSite = new Rectangle(initialPositionX+103,initialPositionY-209,33,33);
		content07OpenSite = new Rectangle(initialPositionX+21,initialPositionY-169,33,33);
		content08OpenSite = new Rectangle(initialPositionX+62,initialPositionY-169,33,33);
		content09OpenSite = new Rectangle(initialPositionX+103,initialPositionY-169,33,33);
		content10OpenSite = new Rectangle(initialPositionX+21,initialPositionY-129,33,33);
		content11OpenSite = new Rectangle(initialPositionX+62,initialPositionY-129,33,33);
		content12OpenSite = new Rectangle(initialPositionX+103,initialPositionY-129,33,33);
		content13OpenSite = new Rectangle(initialPositionX+21,initialPositionY-89,33,33);
		content14OpenSite = new Rectangle(initialPositionX+62,initialPositionY-89,33,33);
		content15OpenSite = new Rectangle(initialPositionX+103,initialPositionY-89,33,33);
		
		drag=false;
		
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
	}
	
	
	public boolean pick(ItemRep item) {
		
		boolean equipmentEmpty=false;
		boolean itemPick=false;
		
		switch (item.getGuiRep().getType()) {
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
	
	// TODO DELETE THIS AFTER TEST
public boolean pickGui(GuiRep item) {
		
		boolean equipmentEmpty=false;
		boolean itemPick=false;
		
	if (!inventoryFull()) {
			for (int i=0;i<bagGui.length;i++) {
				for (int j=0;j<bagGui[0].length;j++) {
					if (bagGui[i][j] == null && !itemPick) {
						bagGui[i][j] = item;
						itemPick=true;
					}
				}
			}
		}
		return itemPick;
	}

public boolean dropGui(GuiRep item,int bag) {
	
	boolean itemPick=false;
	
	for (int j=0;j<bagGui[0].length;j++) {
		if (bagGui[bag][j] == null && !itemPick) {
			bagGui[bag][j] = item;
			itemPick=true;
		}
	}
	return itemPick;
}

public void pickGui(GuiRep item,int bag, int content) {
	
	bagGui[bag][content] = null;
	
}

	public boolean dropGui(GuiRep item,int bag,int content) {
		bagGui[bag][content] = item;
		return true;
	}
	
	public boolean bagFull(GuiRep [] bag) {
		
		boolean full=true;
		
		for (GuiRep item : bag) {
			full = (item == null) ? false : true;
		}	
		return full;
	}
	
	
	
	public boolean inventoryFull() {
		
		boolean full=true;
		
		for (int i=0;i<NB_BAGS;i++) {
			full = bagFull(bagGui[i]);
			if (!full) return false;
		}
		return full;
	}
	
	
	public void open() {		

		if (!visible) {
			visible=true;			
			this.setDefaultPosition();
		}		
	}
	
public void changeBag(int newBag) {
		
		activeBag=newBag;
	}

	public void close() {
		
		if (visible) {
			visible=false;
			this.setDefaultPosition();
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
		inventoryDragBarSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-INVENTORY_HEIGHT,INVENTORY_WIDTH,25);
		upCloseSite = new Rectangle((int)getPosition().getX()+101,(int)getPosition().getY()-21,13,14);
		upOpenSite = new Rectangle((int)getPosition().getX()+101,(int)getPosition().getY()-474,13,14);
		lockCloseSite = new Rectangle((int)getPosition().getX()+119,(int)getPosition().getY()-21,13,14);
		lockOpenSite = new Rectangle((int)getPosition().getX()+119,(int)getPosition().getY()-474,13,14);
		closeCloseSite = new Rectangle((int)getPosition().getX()+137,(int)getPosition().getY()-21,13,14);
		closeOpenSite = new Rectangle((int)getPosition().getX()+137,(int)getPosition().getY()-474,13,14);		

		bag01OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-413,33,33);
		bag02OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-413,33,33);
		bag03OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-413,33,33);
		bag04OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-373,33,33);
		bag05OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-373,33,33);
		bag06OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-373,33,33);
		bag07OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-332,33,33);
		bag08OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-332,33,33);
		bag09OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-332,33,33);
		content01OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-249,33,33);
		content02OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-249,33,33);
		content03OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-249,33,33);
		content04OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-209,33,33);
		content05OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-209,33,33);
		content06OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-209,33,33);
		content07OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-169,33,33);
		content08OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-169,33,33);
		content09OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-169,33,33);
		content10OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-129,33,33);
		content11OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-129,33,33);
		content12OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-129,33,33);
		content13OpenSite = new Rectangle((int)getPosition().getX()+21,(int)getPosition().getY()-89,33,33);
		content14OpenSite = new Rectangle((int)getPosition().getX()+62,(int)getPosition().getY()-89,33,33);
		content15OpenSite = new Rectangle((int)getPosition().getX()+103,(int)getPosition().getY()-89,33,33);
		
	}
	
	public void setDefaultPosition() {
		
		DownLeftCornerPosition = new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
		
		inventoryOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-INVENTORY_HEIGHT,INVENTORY_WIDTH,INVENTORY_HEIGHT);
		inventoryCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-30,INVENTORY_WIDTH,20);
		inventoryDragBarSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-INVENTORY_HEIGHT,INVENTORY_WIDTH,25);
		
		upCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+101,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,13,14);
		upOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+101,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);
		lockCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+119,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,13,14);
		lockOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+119,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);
		closeCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+137,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,13,14);
		closeOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+137,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-474,13,14);		

		bag01OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-413,33,33);
		bag02OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-413,33,33);
		bag03OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-413,33,33);
		bag04OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-373,33,33);
		bag05OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-373,33,33);
		bag06OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-373,33,33);
		bag07OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-332,33,33);
		bag08OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-332,33,33);
		bag09OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-332,33,33);
		content01OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-249,33,33);
		content02OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-249,33,33);
		content03OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-249,33,33);
		content04OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-209,33,33);
		content05OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-209,33,33);
		content06OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-209,33,33);
		content07OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-169,33,33);
		content08OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-169,33,33);
		content09OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-169,33,33);
		content10OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-129,33,33);
		content11OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-129,33,33);
		content12OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-129,33,33);
		content13OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+21,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-89,33,33);
		content14OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+62,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-89,33,33);
		content15OpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661+103,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-89,33,33);
		
	}
	
	public Point getDefaultPosition() {
		return new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+661,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
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


	public GuiRep[][] getBagGui() {
		return bagGui;
	}


	public Rectangle getInventoryDragBarSite() {
		return inventoryDragBarSite;
	}
	
	
}
