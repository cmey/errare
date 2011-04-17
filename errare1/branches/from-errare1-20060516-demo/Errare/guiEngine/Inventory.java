package guiEngine;

import java.awt.Rectangle;

import main.ItemRep;

public class Inventory {

	private Rectangle inventorySite,onOffSite,
	informationSite, equipment01Site, equipment02Site,
	headSite, neck01Site, chest01Site, wristSite, beltSite, trousersSite, tibiaSite, feetSite,
	earSite, neck02Site, chest02Site, ring01Site, ring02Site, bracelet01Site, bracelet02Site, pocketSite,
	weapon01Site, weapon02Site, remoteweaponSite, ammunitionSite;
	
	private final int headIndex = 0, neck01Index = 1, chest01Index = 2, wristIndex = 3, beltIndex = 4, 
	trousersIndex = 5, tibiaIndex = 6, feetIndex = 7, earIndex = 8, neck02Index = 9, chest02Index = 10,
	ring01Index = 11, ring02Index = 12, bracelet01Index = 13, bracelet02Index = 14, pocketIndex = 15, 
	weapon01Index = 16,	weapon02Index = 17, remoteweaponIndex = 18, ammunitionIndex = 19;
	
	private final int maxEquipment = 20;
	private final int nbBags = 3;
	private final int bagWidth = 5;
	private final int bagHeight = 2;
	
	private boolean visible;
	
	private ItemRep [] equipment;
	private ItemRep [] bag01;
	private ItemRep [] bag02;
	private ItemRep [] bag03;
	
	
	public Inventory() {
		
		informationSite = new Rectangle(0,0,0,0);
		// ...
		visible = false;
		equipment = new ItemRep[maxEquipment];
		bag01 = new ItemRep[bagWidth*bagHeight];
		bag02 = new ItemRep[bagWidth*bagHeight];
		bag03 = new ItemRep[bagWidth*bagHeight];
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
		}else if (!bagFull(bag01)) {
			for (int i=0;i<bag01.length;i++) {
				if (bag01[i] == null) {
					bag01[i] = item;
					itemPick=true;
				}
			}
		}else if (!bagFull(bag02)) {
			for (int i=0;i<bag02.length;i++) {
				if (bag02[i] == null) {
					bag02[i] = item;
					itemPick=true;
				}
			}
		}else if (!bagFull(bag03)) {
			for (int i=0;i<bag03.length;i++) {
				if (bag03[i] == null) {
					bag03[i] = item;
					itemPick=true;
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

	    full = bagFull(bag01);
		if (full==true) full = bagFull(bag02);
		else return false;
		if (full==true) full = bagFull(bag03);
		else return false;
		return full;
	}


	public void open() {
		
		visible=true;	
	}



	

}
