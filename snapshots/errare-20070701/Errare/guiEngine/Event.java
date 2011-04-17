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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import databaseEngine.DatabaseEngine;

import main.ItemRep;

/**
 * GuiEvent class
 * Controle key and mouse action for the Gui
 * @author Ak
 */

public class Event implements MouseListener, MouseMotionListener, KeyListener  {
	
	// Instance of the GuiEngine
	private GuiEngine engine;
	
	// Numbers associated with the 3 interfaces witch can be visible or not
	private static int chatCount, equipementCount, inventoryCount;
	
	private int chatKeyCode, equipementKeyCode, inventoryKeyCode;
	
	private String message;
	/**
	 * GuiEvent constructor
	 * @param engine the GuiEngine
	 */
	
	public Event(GuiEngine engine) {
		
		// Initilisation
		this.engine=engine;
		this.chatCount=0;
		this.equipementCount=0;
		this.inventoryCount=0;
		this.message="";
		
		// We access to the database
		try {
			chatKeyCode = new Integer(new DatabaseEngine().getString("keys.gui.chat")).intValue();
			equipementKeyCode = new Integer(new DatabaseEngine().getString("keys.gui.equipement")).intValue();
			inventoryKeyCode =new Integer(new DatabaseEngine().getString("keys.gui.inventory")).intValue();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * KeyType fonction
	 * Do nothing here
	 */
	
	public void keyTyped(KeyEvent ke) {}
	
	/**
	 * keyPressed fonction
	 * @param e KeyEvent
	 * Use when a key is pressed
	 */
	
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {

				try {
					engine.getDatabaseEngine().set("chat.position_x", (int)engine.getActionBar().getChat().getPosition().getX());
					engine.getDatabaseEngine().set("chat.position_y", (int)engine.getActionBar().getChat().getPosition().getY());
					engine.getDatabaseEngine().set("chat.open", engine.getActionBar().getChat().isVisible());
				
					engine.getDatabaseEngine().set("equipement.position_x", (int)engine.getActionBar().getEquipement().getPosition().getX());
					engine.getDatabaseEngine().set("equipement.position_y", (int)engine.getActionBar().getEquipement().getPosition().getY());
					engine.getDatabaseEngine().set("equipement.open", engine.getActionBar().getEquipement().isVisible());
					
					engine.getDatabaseEngine().set("inventory.position_x", (int)engine.getActionBar().getInventory().getPosition().getX());
					engine.getDatabaseEngine().set("inventory.position_y", (int)engine.getActionBar().getInventory().getPosition().getY());
					engine.getDatabaseEngine().set("inventory.open", engine.getActionBar().getInventory().isVisible());
					
				} catch (TransformerFactoryConfigurationError err) {
					System.err.println("DataBase Error (Save 1) : "+err.getMessage());
				} catch (TransformerException err) {
					System.err.println("DataBase Error (Save 2) : "+err.getMessage());
				}
				
			System.exit(0);
			
			
		}else if (engine.getActionBar().getChat().isTyping()) {
			message+=e.getKeyChar();
			engine.setMsgType(message);
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					engine.getActionBar().getChat().addMessage(message,engine.getActionBar().getChat().getActiveChannel());				
					message="";
					engine.setMsgType(message);
					//engine.getActionBar().getChat().notTypeTexte();
					engine.sendChatMessage(true);
				}
			}
		// If the keyCode is the key for showing the chat
		else if (e.getKeyCode()==chatKeyCode) {
			chatCount++;
			if (chatCount % 2 == 1) {
				engine.getActionBar().openChat(engine.getActionBar().getChat().getActiveChannel());
			}else {
				engine.getActionBar().closeChat();
			}
		}
		// If the keyCode is the key for showing the equipement of the hero
		else if (e.getKeyCode()==equipementKeyCode) {
			equipementCount++;
			if (equipementCount % 2 == 1) {
				engine.getActionBar().openEquipement();
			}else {
				engine.getActionBar().closeEquipement();
			}
		}
		// If the keyCode is the key for showing the inventory of the hero
		else if (e.getKeyCode()==inventoryKeyCode) {
			inventoryCount++;
			if (inventoryCount % 2 == 1) {
				engine.getActionBar().openInventory();
			}else {
				engine.getActionBar().closeInventory();
			}
		}else if (e.getKeyCode()==KeyEvent.VK_F1){
			if (engine.isDebugMode()) engine.setDebugMode(false);
			else engine.setDebugMode(true);
			
		}
		
		if(engine.isDEV())engine.getCanvas().repaint();
		
	}
	
	/**
	 * KeyReleased fonction
	 * Do nothing here
	 */
	
	public void keyReleased(KeyEvent ke) {}
	
	/**
	 * mouseDragged fonction
	 * Do nothing here
	 */
	
	public void mouseDragged(MouseEvent e) {
//		We consumed the event
		consumeEvent(e);
		
		Point p = e.getPoint();
		ActionBar actionBar = engine.getActionBar();
		Chat chat = engine.getActionBar().getChat();
		Equipement equipement = engine.getActionBar().getEquipement();
		Inventory inventory = engine.getActionBar().getInventory();
		
		int d = ActionBar.MagnetismPixelLenght;
		
		engine.setCursorPos(e.getPoint());
		
if (actionBar.getActionBarSite().contains(p)) {
			
			if (actionBar.getAction1Site().contains(p))  {
				engine.setActionSelected(0);
			}else if (actionBar.getAction2Site().contains(p))  {
				engine.setActionSelected(1);
			}else if (actionBar.getAction3Site().contains(p))  {
				engine.setActionSelected(2);
			}else if (actionBar.getAction4Site().contains(p))  {
				engine.setActionSelected(3);
			}else if (actionBar.getAction5Site().contains(p))  {
				engine.setActionSelected(4);
			}else if (actionBar.getAction6Site().contains(p))  {
				engine.setActionSelected(5);
			}else if (actionBar.getAction7Site().contains(p))  {
				engine.setActionSelected(6);
			}else if (actionBar.getAction8Site().contains(p))  {
				engine.setActionSelected(7);
			}else if (actionBar.getAction9Site().contains(p))  {
				engine.setActionSelected(8);
			}else if (actionBar.getAction10Site().contains(p))  {
				engine.setActionSelected(9);
			}else if (actionBar.getAction11Site().contains(p))  {
				engine.setActionSelected(10);
			}else if (actionBar.getAction12Site().contains(p))  {
				engine.setActionSelected(11);
			}else if (actionBar.getAction13Site().contains(p))  {
				engine.setActionSelected(12);
			}else if (actionBar.getAction14Site().contains(p))  {;
				engine.setActionSelected(13);
			}else if (actionBar.getAction15Site().contains(p))  {
				engine.setActionSelected(14);
			}else if (actionBar.getAction16Site().contains(p))  {
				engine.setActionSelected(15);
			}else if (actionBar.getAction17Site().contains(p))  {
				engine.setActionSelected(16);
			}else if (actionBar.getAction18Site().contains(p))  {
				engine.setActionSelected(17);
			}
			
		}else if ((chat.isVisible() && chat.getChatOpenSite().contains(p))) { 			
			
			if (!engine.getActionBar().getChat().isDragged() && !engine.getActionBar().getEquipement().isDragged()) {
				engine.getActionBar().getChat().dragIt(p);
			}
		}else if ((equipement.isVisible() && equipement.getEquipementOpenSite().contains(p))) { 			
			
			if (!engine.getActionBar().getEquipement().isDragged() && !engine.getActionBar().getChat().isDragged()) {
				engine.getActionBar().getEquipement().dragIt(p);
			}
		}else if ((inventory.isVisible() && inventory.getInventoryOpenSite().contains(p))) { 			
			
			if (!engine.getActionBar().getInventory().isDragged()) {
				engine.getActionBar().getInventory().dragIt(p);
			}
		}
		
			// Magnetism effect
		
		
		if (engine.getActionBar().getChat().isDragged()) {
			
			int x= (int)(p.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX());
			int y= (int)(p.getY()-actionBar.getChat().getDragPoint().getY()+actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT);
			
				//Left
			if ((x>=0 && x<=d) && (y>=d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
				// Top
			}else if ((y>=0 && y<=d) && (x>=d && x+Chat.CHAT_WIDTH <=engine.getScreenWidth()-d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
				// Right
			}else if ((x+Chat.CHAT_WIDTH<=engine.getScreenWidth() && x+Chat.CHAT_WIDTH>=engine.getScreenWidth()-d) && (y>=d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()));	
				// Down
			}else if ((y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x>=d && x+Chat.CHAT_WIDTH<=engine.getScreenWidth()-d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
			}
			// Top left corner
			if ((x>=0 && x<=d) && (y>=0 && y<=d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
			}
			// Top right corner
			if ((x+Chat.CHAT_WIDTH<=engine.getScreenWidth() && x+Chat.CHAT_WIDTH>=engine.getScreenWidth()-d) && (y>=0 && y<=d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));	

			}
			// Down right corner
			if ((y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x+Chat.CHAT_WIDTH<=engine.getScreenWidth() && x+Chat.CHAT_WIDTH>=engine.getScreenWidth()-d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()+(engine.getScreenHeight()-y)));
			}
			// Down left corner
			if ((x>=0 && x<=d) && (y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d)){
				engine.getActionBar().getChat().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(engine.getScreenHeight()-y)));	
			}
		}else if (engine.getActionBar().getEquipement().isDragged()) {

			int x= (int)(p.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX());
			int y= (int)(p.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT));
			
				//Left
			if ((x>=0 && x<=d) && (y>=d)){
				engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
				// Top
			}else if ((y>=0 && y<=d) && (x>=d && x+Equipement.EQUIPEMENT_WIDTH<=engine.getScreenWidth()-d)){
				engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
				// Right
			}else if ((x+Equipement.EQUIPEMENT_WIDTH<=engine.getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=engine.getScreenWidth()-d) && (y>=d)){
				engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()));	
				// Down
			}else if ((y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x>=d && x+Equipement.EQUIPEMENT_WIDTH<=engine.getScreenWidth()-d)){
				engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
			}
				//	Top left corner
				
			if ((x>=0 && x<=d) && (y>=0 && y<=d)){
					engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
				}
				// Top right corner
				if ((x+Equipement.EQUIPEMENT_WIDTH<=engine.getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=engine.getScreenWidth()-d) && (y>=0 && y<=d)){
					engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));	

				}
				// Down right corner
				if ((y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x+Equipement.EQUIPEMENT_WIDTH<=engine.getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=engine.getScreenWidth()-d)){
					engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()+(engine.getScreenHeight()-y)));
				}
				// Down left corner
				if ((x>=0 && x<=d) && (y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d)){
					engine.getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(engine.getScreenHeight()-y)));	
				}
		}else if (engine.getActionBar().getInventory().isDragged()) {

			int x= (int)(p.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX());
			int y= (int)(p.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT));
			
				//Left
			if ((x>=0 && x<=d) && (y>=d)){
				engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
				// Top
			}else if ((y>=0 && y<=d) && (x>=d && x+Inventory.INVENTORY_WIDTH<=engine.getScreenWidth()-d)){
				engine.getActionBar().getInventory().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
				// Right
			}else if ((x+Inventory.INVENTORY_WIDTH<=engine.getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=engine.getScreenWidth()-d) && (y>=d)){
				engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()));
				engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()));	
				// Down
			}else if ((y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x>=d && x+Inventory.INVENTORY_WIDTH<=engine.getScreenWidth()-d)){
				engine.getActionBar().getInventory().dragIt(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
				engine.setCursorPos(new Point((int)p.getX(),(int)p.getY()+(engine.getScreenHeight()-y)));
			}
				//	Top left corner
				
			if ((x>=0 && x<=d) && (y>=0 && y<=d)){
					engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
				}
				// Top right corner
				if ((x+Inventory.INVENTORY_WIDTH<=engine.getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=engine.getScreenWidth()-d) && (y>=0 && y<=d)){
					engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));	

				}
				// Down right corner
				if ((y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d) && (x+Inventory.INVENTORY_WIDTH<=engine.getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=engine.getScreenWidth()-d)){
					engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()+(engine.getScreenHeight()-y)));
				}
				// Down left corner
				if ((x>=0 && x<=d) && (y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=engine.getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=engine.getScreenHeight()-d)){
					engine.getActionBar().getInventory().dragIt(new Point((int)p.getX()+(engine.getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					engine.setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(engine.getScreenHeight()-y)));	
				}
		}
		
		if(engine.isDEV())engine.getCanvas().repaint();
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
		
		// We consumed the event
		consumeEvent(e);
		
		Point p = e.getPoint();
		ActionBar actionBar = engine.getActionBar();
		Chat chat = engine.getActionBar().getChat();
		Equipement equipement = engine.getActionBar().getEquipement();
		Inventory inventory = engine.getActionBar().getInventory();
		
		if (actionBar.getActionBarSite().contains(p)) {
			
			if (actionBar.getAction1Site().contains(p))  {
				engine.getActionBar().doAction(0);
			}else if (actionBar.getAction2Site().contains(p))  {
				engine.getActionBar().doAction(1);
			}else if (actionBar.getAction3Site().contains(p))  {
				engine.getActionBar().doAction(2);
			}else if (actionBar.getAction4Site().contains(p))  {
				engine.getActionBar().doAction(3);
			}else if (actionBar.getAction5Site().contains(p))  {
				engine.getActionBar().doAction(4);
			}else if (actionBar.getAction6Site().contains(p))  {
				engine.getActionBar().doAction(5);
			}else if (actionBar.getAction7Site().contains(p))  {
				engine.getActionBar().doAction(6);
			}else if (actionBar.getAction8Site().contains(p))  {
				engine.getActionBar().doAction(7);
			}else if (actionBar.getAction9Site().contains(p))  {
				engine.getActionBar().doAction(8);
			}else if (actionBar.getAction10Site().contains(p))  {
				engine.getActionBar().doAction(9);
			}else if (actionBar.getAction11Site().contains(p))  {
				engine.getActionBar().doAction(10);
			}else if (actionBar.getAction12Site().contains(p))  {
				engine.getActionBar().doAction(11);
			}else if (actionBar.getAction13Site().contains(p))  {
				engine.getActionBar().doAction(12);
			}else if (actionBar.getAction14Site().contains(p))  {
				engine.getActionBar().doAction(13);
			}else if (actionBar.getAction15Site().contains(p))  {
				engine.getActionBar().doAction(14);
			}else if (actionBar.getAction16Site().contains(p))  {
				engine.getActionBar().doAction(15);
			}else if (actionBar.getAction17Site().contains(p))  {
				engine.getActionBar().doAction(16);
			}else if (actionBar.getAction18Site().contains(p))  {
				engine.getActionBar().doAction(17);
			}
			
		}else if (actionBar.getActionMapSite().distance(p)<=actionBar.getActionMapSite().getRadius()) {
					
			System.out.println("In the Map...");
			
		}else if (chat.isVisible() && chat.getChatOpenSite().contains(p)) { // If the click is in the chat
			
			if (chat.getGeneralOpenChannelSite().contains(p)) {
				engine.getActionBar().getChat().changeChannel(Chat.GENERAL_CHANNEL);
			}else if (chat.getGuildOpenChannelSite().contains(p)) {
				engine.getActionBar().getChat().changeChannel(Chat.GUILD_CHANNEL);
			}else if (chat.getGroupOpenChannelSite().contains(p)) {
				engine.getActionBar().getChat().changeChannel(Chat.GROUP_CHANNEL);
			}else if (chat.getPrivateOpenChannelSite().contains(p)) {
				engine.getActionBar().getChat().changeChannel(Chat.PRIVATE_CHANNEL);
			}else if (chat.getEmotesOpenSite().contains(p)) {
				// EMOTES
			}else if (chat.getEditableOpenSite().contains(p)) {
				engine.getActionBar().getChat().typeText();
			}else if (chat.getUpOpenSite().contains(p)) {
				engine.getActionBar().getChat().setDefaultPosition();
				engine.getActionBar().openChat(Chat.GENERAL_CHANNEL);
			}else if (chat.getLockOpenSite().contains(p)) {
				engine.getActionBar().getChat().setDefaultPosition();
			}else if (chat.getCloseOpenSite().contains(p)) {
				chatCount++;
				engine.getActionBar().getChat().setDefaultPosition();
				engine.getActionBar().closeChat();
			}
			
		}else if (!chat.isVisible() && chat.getChatCloseSite().contains(p)) {
					
			if (chat.getGeneralCloseChannelSite().contains(p)) {
				System.out.println("sdfdsfdsdsfdsffds");
				engine.getActionBar().getChat().openChannel(Chat.GENERAL_CHANNEL);
			}else if (chat.getGuildCloseChannelSite().contains(p)) {
				engine.getActionBar().getChat().openChannel(Chat.GUILD_CHANNEL);
			}else if (chat.getGroupCloseChannelSite().contains(p)) {
				engine.getActionBar().getChat().openChannel(Chat.GROUP_CHANNEL);
			}else if (chat.getPrivateCloseChannelSite().contains(p)) {
				engine.getActionBar().getChat().openChannel(Chat.PRIVATE_CHANNEL);
			}else if (chat.getUpCloseSite().contains(p)) {
				engine.getActionBar().openChat(Chat.GENERAL_CHANNEL);
			}else if (chat.getLockCloseSite().contains(p)) {
			
			}else if (chat.getCloseCloseSite().contains(p)) {
	
			}
			
		}else if(equipement.isVisible() && equipement.getEquipementOpenSite().contains(p)) { // If the click is in the equipement
			
			if (equipement.getCloseOpenSite().contains(p)) {		
				equipementCount++;
				engine.getActionBar().closeEquipement();
			}else if (equipement.getSet1OpenSite().contains(p)) {
				engine.getActionBar().getEquipement().changeSet(Equipement.SET1);
			}else if (equipement.getSet2OpenSite().contains(p)) {
				engine.getActionBar().getEquipement().changeSet(Equipement.SET2);
			}
			
		}else if(!equipement.isVisible() && equipement.getEquipementCloseSite().contains(p)) {
			
			if (equipement.getUpCloseSite().contains(p)) {	
				equipementCount++;
				engine.getActionBar().openEquipement();
			}
			
		}else if(inventory.isVisible() && inventory.getInventoryOpenSite().contains(p)) { // If the click is in the inventory
			if (inventory.getCloseOpenSite().contains(p)) {		
				inventoryCount++;
				engine.getActionBar().closeInventory();
			}else if (inventory.getBag01OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG01);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG02);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG03);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG04);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG05);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG06);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG07);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG08);
			}else if (inventory.getBag02OpenSite().contains(p)) {
				engine.getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG09);
			}
			
		}else if(!inventory.isVisible() && inventory.getInventoryCloseSite().contains(p)) {
			
			if (inventory.getUpCloseSite().contains(p)) {	
				inventoryCount++;
				engine.getActionBar().openInventory();
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
	
	public void mouseReleased(MouseEvent e) {	
		
		// We consumed the event
		consumeEvent(e);
		
		Point p = e.getPoint();
		ActionBar actionBar = engine.getActionBar();
		
		if (engine.getActionBar().getChat().isDragged()) {
			engine.getActionBar().getChat().dropIt(engine.getCursorPos());
		}else if (engine.getActionBar().getEquipement().isDragged()) {
			engine.getActionBar().getEquipement().dropIt(engine.getCursorPos());
		}else if (engine.getActionBar().getInventory().isDragged()) {
			engine.getActionBar().getInventory().dropIt(engine.getCursorPos());
		}
		
		if (actionBar.getActionBarSite().contains(p)) {
			
			if (actionBar.getAction1Site().contains(p))  {
				engine.setActionNotSelected(0);
			}else if (actionBar.getAction2Site().contains(p))  {
				engine.setActionNotSelected(1);
			}else if (actionBar.getAction3Site().contains(p))  {
				engine.setActionNotSelected(2);
			}else if (actionBar.getAction4Site().contains(p))  {
				engine.setActionNotSelected(3);
			}else if (actionBar.getAction5Site().contains(p))  {
				engine.setActionNotSelected(4);
			}else if (actionBar.getAction6Site().contains(p))  {
				engine.setActionNotSelected(5);
			}else if (actionBar.getAction7Site().contains(p))  {
				engine.setActionNotSelected(6);
			}else if (actionBar.getAction8Site().contains(p))  {
				engine.setActionNotSelected(7);
			}else if (actionBar.getAction9Site().contains(p))  {
				engine.setActionNotSelected(8);
			}else if (actionBar.getAction10Site().contains(p))  {
				engine.setActionNotSelected(9);
			}else if (actionBar.getAction11Site().contains(p))  {
				engine.setActionNotSelected(10);
			}else if (actionBar.getAction12Site().contains(p))  {
				engine.setActionNotSelected(11);
			}else if (actionBar.getAction13Site().contains(p))  {
				engine.setActionNotSelected(12);
			}else if (actionBar.getAction14Site().contains(p))  {;
				engine.setActionNotSelected(13);
			}else if (actionBar.getAction15Site().contains(p))  {
				engine.setActionNotSelected(14);
			}else if (actionBar.getAction16Site().contains(p))  {
				engine.setActionNotSelected(15);
			}else if (actionBar.getAction17Site().contains(p))  {
				engine.setActionNotSelected(16);
			}else if (actionBar.getAction18Site().contains(p))  {
				engine.setActionNotSelected(17);
			}
			
		}
		if(engine.isDEV())engine.getCanvas().repaint();
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
		
	}
}