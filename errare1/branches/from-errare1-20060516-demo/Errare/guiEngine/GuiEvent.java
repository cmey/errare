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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.ItemRep;

/**
 * GuiEvent class
 * Controle key and mouse action for the Gui
 * @author Ak
 */

public class GuiEvent implements MouseListener, MouseMotionListener, KeyListener  {
	
	// Instance of the GuiEngine
	private GuiEngine engine;
	
	// Numbers associated with the 3 interfaces witch can be visible or not
	private static int inventoryCount,pauseCount,characteristicsCount;
	
	private int inventoryKeyCode,characteristicsKeyCode,pauseKeyCode,potion1KeyCode,
	potion2KeyCode,potion3KeyCode,potion4KeyCode;
	
	/**
	 * GuiEvent constructor
	 * @param engine the GuiEngine
	 */
	
	public GuiEvent(GuiEngine engine) {
		
		// Initilisation
		this.engine=engine;
		this.inventoryCount=0;
		this.pauseCount=0;
		this.characteristicsCount=0;
		inventoryKeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.inventory")).intValue();
		characteristicsKeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.characteristics")).intValue();
	pauseKeyCode =new Integer(engine.getDatabaseEngine().getString("keys.gui.pause")).intValue();
	potion1KeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.potion1")).intValue();
	potion2KeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.potion2")).intValue();
	potion3KeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.potion3")).intValue();
	potion4KeyCode = new Integer(engine.getDatabaseEngine().getString("keys.gui.potion4")).intValue();
	}
	
	/**
	 * KeyType fonction
	 * Do nothing here
	 */
	
	public void keyTyped(KeyEvent ke) {		
	}
	
	/**
	 * keyPressed fonction
	 * @param e KeyEvent
	 * Use when a key is pressed
	 */
	
	public void keyPressed(KeyEvent e) {
		
		// We access to the database
		
		// If the keyCode is the key for showing the Inventory
		if (e.getKeyCode()==inventoryKeyCode) {
			inventoryCount++;
			if (inventoryCount % 2 == 1) {
				engine.setInventoryOn(true);
			}else {
				engine.setInventoryOn(false);
			}
			// If the keyCode is the key for showing the chracteristics of the hero
		}else if (e.getKeyCode()==characteristicsKeyCode) {
			characteristicsCount++;
			if (characteristicsCount % 2 == 1) {
				engine.setCharacteristicsOn(true);
			}else {
				engine.setCharacteristicsOn(false);
			}
			// If the keyCode is the key to set pause
		}else if (e.getKeyCode()==pauseKeyCode) {
			pauseCount++;
			if (pauseCount % 2 == 1) {
				engine.setPauseOn(true);
			}else {
				engine.setPauseOn(false);
			}
			// If the keyCode is the key to use the first potion
		}else if (e.getKeyCode()==potion1KeyCode) {
			ItemRep[] beltTab  = engine.getBelt().getItemBelt();
			if (beltTab[0]!=null) {
				switch (beltTab[0].getGui().getType()) {
				case 1:engine.updateLife(15);engine.getHero().addMana(10);beltTab[0]=null;engine.updateBelt(beltTab);break;
				case 2:engine.updateLife(20);beltTab[0]=null;engine.updateBelt(beltTab);break;
				case 3:engine.updateMana(15);beltTab[0]=null;engine.updateBelt(beltTab);break;
				}
			}
			// If the keyCode is the key to use the second potion
		}else if (e.getKeyCode()==potion2KeyCode) {
			ItemRep[] beltTab  = engine.getBelt().getItemBelt();
			if (beltTab[1]!=null) {
				switch (beltTab[1].getGui().getType()) {
				case 1:engine.updateLife(15);engine.updateMana(5);beltTab[1]=null;engine.updateBelt(beltTab);break;
				case 2:engine.updateLife(20);beltTab[1]=null;engine.updateBelt(beltTab);break;
				case 3:engine.updateMana(10);beltTab[1]=null;engine.updateBelt(beltTab);break;
				}
			}
			// If the keyCode is the key to use the third potion
		}else if (e.getKeyCode()==potion3KeyCode) {
			ItemRep[] beltTab  = engine.getBelt().getItemBelt();
			if (beltTab[2]!=null) {
				switch (beltTab[2].getGui().getType()) {
				case 1:engine.updateLife(15);engine.updateMana(5);beltTab[2]=null;engine.updateBelt(beltTab);break;
				case 2:engine.updateLife(20);beltTab[2]=null;engine.updateBelt(beltTab);break;
				case 3:engine.updateMana(10);beltTab[2]=null;engine.updateBelt(beltTab);break;
				}
			}
			// If the keyCode is the key to use the fourth potion
		}else if (e.getKeyCode()==potion4KeyCode) {
			ItemRep[] beltTab  = engine.getBelt().getItemBelt();
			if (beltTab[3]!=null) {
				switch (beltTab[3].getGui().getType()) {
				case 1:engine.updateLife(15);engine.updateMana(5);beltTab[3]=null;engine.updateBelt(beltTab);break;
				case 2:engine.updateLife(20);beltTab[3]=null;engine.updateBelt(beltTab);break;
				case 3:engine.updateMana(10);beltTab[3]=null;engine.updateBelt(beltTab);break;
				}
			}
			// TODO TO TEST HERE DELETE IT !
		}else if (e.getKeyChar()=='(') {
			engine.updateLife(-20);
			engine.updateMana(-10);
		}
		if(engine.isDEV())engine.getCanvas().repaint();
	}
	
	/**
	 * KeyReleased fonction
	 * Do nothing here
	 */
	
	public void keyReleased(KeyEvent ke) {	
	}
	
	/**
	 * mouseDragged fonction
	 * Do nothing here
	 */
	
	public void mouseDragged(MouseEvent e) {
		// To test if we wonsumed or not the event
		consumeEvent(e);
	}
	
	
	/**
	 * mouseMoved fonction
	 * @param e MouseEvent
	 * Send the cursor position to the GuiEngine
	 */
	
	public void mouseMoved(MouseEvent e) {
		
		
		engine.setCursorPos(e.getPoint());
		if(engine.isDEV())engine.getCanvas().repaint();
		
	}
	
	/**
	 * mouseClicked fonction
	 * @param e MouseEvent
	 * Use when there is a mouse click (right,left or midle)
	 */
	
	public void mouseClicked(MouseEvent e) {
		
		// To test if we consumed or not the event
		consumeEvent(e);
		
		// The 4 buttons in the statistics (to open others interfaces)
		Rectangle bInventory = new Rectangle(engine.getScreenWidth()-256+74,9,32,32);
		Rectangle bCharacteristics = new Rectangle(engine.getScreenWidth()-256+119,9,32,32);
		Rectangle bPause = new Rectangle(engine.getScreenWidth()-256+164,9,32,32);
		//Rectangle pause = new Rectangle(209,9,32,32); // TODO
		
		// Rectangle Statistics plus Inventory
		Rectangle statInven = new Rectangle(engine.getScreenWidth()-256,0,256,614);
		
		// The belt and the 4 potion's cases
		Rectangle heroBelt = new Rectangle (engine.getScreenWidth()/2-124,engine.getScreenHeight()-64,256,64);
		Rectangle potion1 = new Rectangle(engine.getScreenWidth()/2-124+60,engine.getScreenHeight()-64+14,30,35);
		Rectangle potion2 = new Rectangle(engine.getScreenWidth()/2-124+95,engine.getScreenHeight()-64+14,30,35);
		Rectangle potion3 = new Rectangle(engine.getScreenWidth()/2-124+130,engine.getScreenHeight()-64+14,30,35);
		Rectangle potion4 = new Rectangle(engine.getScreenWidth()/2-124+165,engine.getScreenHeight()-64+14,30,35);
	
		// We get the content of the intentory and of the hero
		GuiRep[] tabGuiBelt = engine.getBelt().getGuiBelt();
		ItemRep[] tabItemBelt = engine.getBelt().getItemBelt();
		
		// If is the Inventory button
		if (bInventory.contains(e.getPoint())) {
			inventoryCount++;
			if (inventoryCount % 2 == 1) {
				engine.setInventoryOn(true);
			}else {
				engine.setInventoryOn(false);
			}
			// If is the characteristics button
		}else if (bCharacteristics.contains(e.getPoint())) {
			characteristicsCount++;
			if (characteristicsCount % 2 == 1) {
				engine.setCharacteristicsOn(true);
			}else {
				engine.setCharacteristicsOn(false);
			}
			// If is the pause button	
		}else if (bPause.contains(e.getPoint())) {
			pauseCount++;
			if (pauseCount % 2 == 1) {
				engine.setPauseOn(true);
			}else {
				engine.setPauseOn(false);
			}
		}
		
		// If Inventory is visible
		if (engine.isInventoryOn()) {
			
			// The Inventory
			Rectangle inventory = new Rectangle(engine.getScreenWidth()-256+8,102+254,241,101);
			
			// Creation of all area	
			Rectangle helm  = new Rectangle(engine.getScreenWidth()-256+97,101+4,61,61);
			Rectangle amulette  = new Rectangle(engine.getScreenWidth()-256+164,101+35,31,31);
			Rectangle fWeapon  = new Rectangle(engine.getScreenWidth()-256+11,101+69,68,103);
			Rectangle armor  = new Rectangle(engine.getScreenWidth()-256+86,101+69,83,101);
			Rectangle sWeapon  = new Rectangle(engine.getScreenWidth()-256+176,101+69,68,103);
			Rectangle gloves  = new Rectangle(engine.getScreenWidth()-256+5,101+181,61,61);
			Rectangle ring1  = new Rectangle(engine.getScreenWidth()-256+70,101+181,31,31);
			Rectangle belt  = new Rectangle(engine.getScreenWidth()-256+103,101+181,48,29);
			Rectangle ring2  = new Rectangle(engine.getScreenWidth()-256+154,101+181,31,31);
			Rectangle boots  = new Rectangle(engine.getScreenWidth()-256+176,101+181,61,61);
			
			// We get the content of the intentory and of the hero
			GuiRep[][] tabGuiInventory  = engine.getInventory().getGuiInventory();
			ItemRep[][] tabItemInventory = engine.getInventory().getItemInventory();
			
			GuiRep[] tabGuiOnCharacter = engine.getInventory().getGuiOnCharacter();
			ItemRep[] tabItemOnChracter = engine.getInventory().getItemOnCharacter();
			
			// Click in Inventory
			if (inventory.contains(e.getPoint())) {
				for (int i=0;i<tabGuiInventory.length;i++) {
					for (int j=0;j<tabGuiInventory[0].length;j++) {
						// If the case contains a object (top left case)
						if ((tabGuiInventory[i][j] != null)&&(tabGuiInventory[i][j].getType()!=0)) {
							// We create the object zone
							Rectangle zone= new Rectangle(engine.getScreenWidth()-256+8+(j*20),102+254+(i*20),tabGuiInventory[i][j].getWidthPixel(),tabGuiInventory[i][j].getHeightPixel());	
							// If we clicked on a object with the left clicked
							if (zone.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
								// Pick the item
								if(!engine.isItemSelected()) {
									// Play sound
									engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
									engine.setItemSelectInventory(i,j);	
									// Switch between two items
								}else if (engine.isItemSelected()) {
									// If there is few space to switch
									if (engine.getItemSelect().getGui().getWidthPixel()<=zone.width
											&& engine.getItemSelect().getGui().getHeightPixel()<=zone.height) {
										// TODO CHANGE SOUND TO ENGINE ?
										engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
										engine.setItemSelectChangeInventory(i,j);	
									}
								}
							}else if (zone.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON3) {
								// TODO Description null
								/*String[] description = engine.getHero().getDescription(tabItemInventory[i][j].getCharacteristics());
								for (int k=0;k<5;i++) System.out.println("Description"+k+" :"+description[k]);*/
							}							
							// If the zone is empty
						}else if (tabGuiInventory[i][j] == null && engine.isItemSelected() && e.getButton() == MouseEvent.BUTTON1){
								// The area of the item selected (Inventory representation)
								Rectangle zone= new Rectangle(engine.getScreenWidth()-256+8+(j*20),102+254+(i*20),engine.getItemSelect().getGui().getWidthPixel(),engine.getItemSelect().getGui().getHeightPixel());	
								if (zone.contains(e.getPoint())) {			
									int nb=0;
									int h=engine.getItemSelect().getGui().getHeightCase();
									int w=engine.getItemSelect().getGui().getWidthCase();
									// We checked if there is the place for the item
									for (int k=0;k<h;k++) {
										for (int l=0;l<w;l++) {
											// We checked if the zone is out of the Inventory
											if (((i+k)<tabGuiInventory.length)&&((j+l)<tabGuiInventory[0].length)) {
												if ((tabGuiInventory[i+k][j+l]==null))  {
												nb++;
											}
											}
										}
									}
									// If yes (zone area is <= item select area) we put the item into the Inventory
									if (nb==((h)*(w))) {
										// Play sound
										engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
										engine.toInventory(i,j);
										i=99;j=99; // To exit
									}
								}
						}
					}
				}
			}else if (helm.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a helm
				// Item back on character
				if (tabGuiOnCharacter[0]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==13 || 
						engine.getItemSelect().getGui().getType()==14 ||engine.getItemSelect().getGui().getType()==15)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(0);
					// On select
					// Item pick
				}else if (tabGuiOnCharacter[0]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(0);
					// Switch between 2 items
				}else if (tabGuiOnCharacter[0]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==13 || 
						engine.getItemSelect().getGui().getType()==14 ||engine.getItemSelect().getGui().getType()==15)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(0);
				}
			}else if (amulette.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				//	 If it is a amulet
				if (tabGuiOnCharacter[1]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==4 || 
						engine.getItemSelect().getGui().getType()==5 ||engine.getItemSelect().getGui().getType()==6)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(1);
					// On select
				}else if (tabGuiOnCharacter[1]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(1);
				}else if (tabGuiOnCharacter[1]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==4 || 
						engine.getItemSelect().getGui().getType()==5 ||engine.getItemSelect().getGui().getType()==6)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(1);
				}
			}else if (fWeapon.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a weapon
				if (tabGuiOnCharacter[2]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()>=29 && 
						engine.getItemSelect().getGui().getType()<=36)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(2);
					// We change the icon (weapons states)
					engine.weaponsStatesChange(engine.getItemSelect().getGui().getType());
					// On select
				}else if (tabGuiOnCharacter[2]!=null && !engine.isItemSelected()) {
					engine.setItemSelectCharacter(2);
					// We set the icon weapons states to null
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.weaponsStatesChange(0);
				}else if (tabGuiOnCharacter[2]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()>=29 && 
						engine.getItemSelect().getGui().getType()<=36)) {
					// We change the icon (weapons states)
					engine.weaponsStatesChange(engine.getItemSelect().getGui().getType());
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(2);

				}
			}else if (armor.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is an armor
				if (tabGuiOnCharacter[3]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==22 || 
						engine.getItemSelect().getGui().getType()==23 ||engine.getItemSelect().getGui().getType()==24 ||engine.getItemSelect().getGui().getType()==25)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(3);
					// On select
				}else if (tabGuiOnCharacter[3]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(3);
				}else if (tabGuiOnCharacter[3]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==22 || 
						engine.getItemSelect().getGui().getType()==23 ||engine.getItemSelect().getGui().getType()==24 ||engine.getItemSelect().getGui().getType()==25)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(3);
				}
			}else if (sWeapon.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a second Weapon
				if (tabGuiOnCharacter[4]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==26 || 
						engine.getItemSelect().getGui().getType()==27 ||engine.getItemSelect().getGui().getType()==28)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(4);
					// On select
				}else if (tabGuiOnCharacter[4]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(4);
				}else if (tabGuiOnCharacter[4]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==26 || 
						engine.getItemSelect().getGui().getType()==27 ||engine.getItemSelect().getGui().getType()==28)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(4);
				}
			}else if (gloves.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is gloves
				if (tabGuiOnCharacter[5]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==19 || 
						engine.getItemSelect().getGui().getType()==20 ||engine.getItemSelect().getGui().getType()==21)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(5);
					// On select
				}else if (tabGuiOnCharacter[5]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(5);
				}else if (tabGuiOnCharacter[5]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==19 || 
						engine.getItemSelect().getGui().getType()==20 ||engine.getItemSelect().getGui().getType()==21)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(5);
				}
			}else if (ring1.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a ring
				if (tabGuiOnCharacter[6]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==7 || 
						engine.getItemSelect().getGui().getType()==8 ||engine.getItemSelect().getGui().getType()==9)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(6);
					// On select
				}else if (tabGuiOnCharacter[6]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(6);
				}else if (tabGuiOnCharacter[6]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==7 || 
						engine.getItemSelect().getGui().getType()==8 ||engine.getItemSelect().getGui().getType()==9)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(6);
				}
			}else if (belt.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a belt
				if (tabGuiOnCharacter[7]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==10 || 
						engine.getItemSelect().getGui().getType()==11 ||engine.getItemSelect().getGui().getType()==12)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(7);
					// On select
				}else if (tabGuiOnCharacter[7]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(7);
				}else if (tabGuiOnCharacter[7]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==10 || 
						engine.getItemSelect().getGui().getType()==11 ||engine.getItemSelect().getGui().getType()==12)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(7);
				}
			}else if (ring2.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is a ring
				if (tabGuiOnCharacter[8]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==7 || 
						engine.getItemSelect().getGui().getType()==8 ||engine.getItemSelect().getGui().getType()==9)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(8);
					// On select
				}else if (tabGuiOnCharacter[8]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(8);
				}else if (tabGuiOnCharacter[8]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==7 || 
						engine.getItemSelect().getGui().getType()==8 ||engine.getItemSelect().getGui().getType()==9)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(8);
				}
			}else if (boots.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				// If it is boots
				if (tabGuiOnCharacter[9]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==16 || 
						engine.getItemSelect().getGui().getType()==17 ||engine.getItemSelect().getGui().getType()==18)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toCharacter(9);
					// On select
				}else if (tabGuiOnCharacter[9]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectCharacter(9);
				}else if (tabGuiOnCharacter[9]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==16 || 
						engine.getItemSelect().getGui().getType()==17 ||engine.getItemSelect().getGui().getType()==18)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeCharacter(9);
				}
			}			
		}
		// Click in Belt 
		if (heroBelt.contains(e.getPoint())) {
			// We place the potion in the true place if it's empty
			if (potion1.contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
				//	Potion back to the belt
				if (tabGuiBelt[0]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toBelt(0);
					// Pick a potion
				}else if (tabGuiBelt[0]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectBelt(0);
					// Switch between 2 potions
				}else if (tabGuiBelt[0]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeBelt(0);
				}
				
			}else if (potion2.contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
				//	If the item selected is a potion
				if (tabGuiBelt[1]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toBelt(1);
				}else if (tabGuiBelt[1]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectBelt(1);
				}else if (tabGuiBelt[1]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeBelt(1);
				}
			}else if (potion3.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				//	If the item selected is a potion
				if (tabGuiBelt[2]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toBelt(2);
				}else if (tabGuiBelt[2]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectBelt(2);
				}else if (tabGuiBelt[2]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeBelt(2);
				}
			}else if (potion4.contains(e.getPoint())&& e.getButton() == MouseEvent.BUTTON1) {
				//	If the item selected is a potion
				if (tabGuiBelt[3]==null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itemback.wav",false);
					engine.toBelt(3);
				}else if (tabGuiBelt[3]!=null && !engine.isItemSelected()) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectBelt(3);
				}else if (tabGuiBelt[3]!=null && engine.isItemSelected() && (engine.getItemSelect().getGui().getType()==1 || 
						engine.getItemSelect().getGui().getType()==2 ||engine.getItemSelect().getGui().getType()==3)) {
					engine.getSoundEngine().play("medias/Sounds/shortSounds/itempick.ogg",false);
					engine.setItemSelectChangeBelt(3);
				}
			}
		} // Click out
		if (!statInven.contains(e.getPoint()) && !heroBelt.contains(e.getPoint())) {
			if (engine.isItemSelected()) {
				// Drop item
				engine.getInventory().drop(engine.getItemSelect());
				engine.getSoundEngine().play("medias/Sounds/shortSounds/bang.wav",false);
				engine.deleteItemSelect();
			}
		}
		
		if(engine.isDEV())engine.getCanvas().repaint();
	}
	
	
	/**
	 * mousePressed fonction
	 * Do nothing here
	 */
	
	public void mousePressed(MouseEvent me) {	
	}
	
	/**
	 * mouseReleased fonction
	 * Do nothing here
	 */
	
	public void mouseReleased(MouseEvent me) {	
	}
	
	/**
	 * mouseEntered fonction
	 * Do nothing here
	 */
	
	public void mouseEntered(MouseEvent me) {
	}
	
	/**
	 * mouseExited fonction
	 * Do nothing here
	 */
	
	public void mouseExited(MouseEvent me) {
	}
	
	/**
	 * consumeEvent fonction
	 * @param e MouseEvent
	 Test if we must consume the event and so the hero will not move
	 */
	
	public void consumeEvent(MouseEvent e) {
		
		// Rectangle Statistics plus Inventory
		Rectangle statInven = new Rectangle(engine.getScreenWidth()-256,0,256,614);	
		// The belt
		Rectangle belt = new Rectangle (engine.getScreenWidth()/2-124,engine.getScreenHeight()-64,256,64);
		// The hero statistics
		Rectangle statistics = new Rectangle (engine.getScreenWidth()-256,0,256,101);
		
		// If click on the belt or on the statistics
		if (statistics.contains(e.getPoint()) || belt.contains(e.getPoint())) {
			// We must consumed the event, thank to that the hero will not move !
			e.consume();		
		}
		
		// If click on statistique or in Inventory
		if (engine.isInventoryOn() && statInven.contains(e.getPoint())) {
				e.consume();
		}
		
		// If drop with the Inventory visible
		if (!statInven.contains(e.getPoint()) && !belt.contains(e.getPoint()) && engine.isItemSelected() && engine.isInventoryOn()) {
			e.consume();
		}
		
		// If drop with the Inventory invisible
		if (!statistics.contains(e.getPoint()) && !belt.contains(e.getPoint()) && engine.isItemSelected() && !engine.isInventoryOn()) {
			e.consume();
		}
	}
}