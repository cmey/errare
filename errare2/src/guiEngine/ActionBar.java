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

import geom.Circle;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;

import soundEngine.SoundEngine;

public class ActionBar {
	
	private final int MAX_ACTION=20;
	
	private Chat chat;
	private Inventory inventory;
	private Equipement equipement;
	private Focus focus;
	
	private Rectangle actionBarSite;
	private Rectangle barSite;
	private Rectangle mapSite;
	private Rectangle generalChatSite;
	private Rectangle guildChatSite;
	private Rectangle groupChatSite;
	private Rectangle privateChatSite;
	private Rectangle inventorySite;
	private Rectangle EquipementSit;
	private Rectangle meleeXP;
	private Rectangle rangedXP;
	private Rectangle magicalXP;
	private Rectangle questSite;
	private Rectangle optionSite;
	private Rectangle action1Site;
	private Rectangle action2Site;
	private Rectangle action3Site;
	private Rectangle action4Site;
	private Rectangle action5Site;
	private Rectangle action6Site;
	private Rectangle action7Site;
	private Rectangle action8Site;
	private Rectangle action9Site;
	private Rectangle action10Site;
	private Rectangle action11Site;
	private Rectangle action12Site;
	private Rectangle action13Site;
	private Rectangle action14Site;
	private Rectangle action15Site;
	private Rectangle action16Site;
	private Rectangle action17Site;
	private Rectangle action18Site;
	private Rectangle action19Site;
	private Rectangle action20Site;
	private Rectangle previousSite;
	private Rectangle nextSite;
	private Rectangle zoneSite;
	private Rectangle end;

	private Circle actionMapSite;
	private Circle worldSite;
	
	//private int [] tabCase = new int[MAX_CASE];
	private int [] tabAction;;
	
	private ArrayList comboList;
	private boolean visible;
	private boolean clickOnQuest;
	private boolean clickOnOption;
	private boolean clickOnPrevious;
	private boolean clickOnNext;
	private boolean clickOnZone;
	private boolean clickOnWorld;
	 
	private GuiEngine engine;
	private SoundEngine soundEngine;
	
	private int screenHeight;
	private int screenWidth;
	
	public static final int MagnetismPixelLenght = 30;
	public static final int ACTIONBAR_HEIGHT = 40 ;
	public static final int ACTIONBAR_WIDTH = 1024 ;
	public static final int ACTIONBARMAP_HEIGHT = 163 ;
	
	public ActionBar(GuiEngine engine) {
		
		this.engine = engine;	
		
		focus = new Focus();
		
		screenWidth = engine.getScreenWidth();
		screenHeight = engine.getScreenHeight();

		tabAction = new int[MAX_ACTION];
		for (int i=0;i<MAX_ACTION;i++)tabAction[i]=0;
		
		actionBarSite = new Rectangle((screenWidth-ACTIONBAR_WIDTH)/2,screenHeight-50,1024,50);
		actionMapSite = new Circle((screenWidth-ACTIONBAR_WIDTH)/2+945,screenHeight-92,65);
					
		questSite = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+8,screenHeight-41,32,32);
		optionSite = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+45,screenHeight-41,32,32);
		
		action1Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+86,screenHeight-41,32,33);
		action2Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+124,screenHeight-41,32,33);
		action3Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+162,screenHeight-41,32,33);
		action4Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+199,screenHeight-41,32,33);
		action5Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+236,screenHeight-41,32,33);
		action6Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+273,screenHeight-41,32,33);
		action7Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+310,screenHeight-41,32,33);
		action8Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+347,screenHeight-41,32,33);	
		action9Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+384,screenHeight-41,32,33);
		action10Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+421,screenHeight-41,32,33);
		action11Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+463,screenHeight-41,32,33);
		action12Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+501,screenHeight-41,32,33);
		action13Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+538,screenHeight-41,32,33);
		action14Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+575,screenHeight-41,32,33);
		action15Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+612,screenHeight-41,32,33);
		action16Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+649,screenHeight-41,32,33);
		action17Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+686,screenHeight-41,32,33);
		action18Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+723,screenHeight-41,32,33);
		action19Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+760,screenHeight-41,32,33);
		action20Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+797,screenHeight-41,32,33);
		
		previousSite = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+833,screenHeight-40,16,32);
		nextSite = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+851,screenHeight-40,16,32);

		worldSite = new Circle(((screenWidth-ACTIONBAR_WIDTH)/2)+987,screenHeight-137,14);
		zoneSite = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+887,screenHeight-35,116,22);
		
		soundEngine = engine.getSoundEngine();
		soundEngine.play("data/sounds/gui/openActionBar.ogg",false);
		
		chat = new Chat(this);
		inventory = new Inventory(this);
		equipement = new Equipement(this);
		
		comboList = new ArrayList();
	}
	
	public void addActionToComboList() {
		
	}
	
	public void addAction(int action, int id) {
		
		if (tabAction[id]==0){
			tabAction[id]=action;
		}
	}
	
	public void deleteAction(int id) {
		
		tabAction[id]=0;
	}
	
	public void doAction (int id) {
		
		if (tabAction[id]!=0){
			engine.setDebugAction("Use Action : "+(id+1));
		}else {
			engine.setDebugAction("No Action here");
		}
	}
	
	
	public void updateXP() {
		
	}
	
	public void openChat(int channel) {
		chat.openChannel(channel);
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}
	
	public void openEquipement() {
		equipement.open();
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}
	
	public void openInventory() {
		inventory.open();
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}
	
	public void closeChat() {
		chat.close();
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}
	
	public void closeEquipement() {
		equipement.close();
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}
	
	public void closeInventory() {
		inventory.close();
		soundEngine.play("data/sounds/gui/openWindow.ogg",false);
	}

	public void setPriority(int window) {
		
		focus.setPriority(window);
	}
	
	public int getPriority(int window) {
		
		return focus.getPriority(window);
	} 
	
	public int[] getTabAction() {
		return tabAction;
	}

	public Chat getChat() {
		return chat;
	}

	public Equipement getEquipement() {
		return equipement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public Rectangle getActionBarSite() {
		return actionBarSite;
	}

	public Circle getActionMapSite() {
		return actionMapSite;
	}

	public Rectangle getAction1Site() {
		return action1Site;
	}

	public Rectangle getAction2Site() {
		return action2Site;
	}

	public Rectangle getAction3Site() {
		return action3Site;
	}

	public Rectangle getAction4Site() {
		return action4Site;
	}

	public Rectangle getAction5Site() {
		return action5Site;
	}

	public Rectangle getAction6Site() {
		return action6Site;
	}

	public Rectangle getAction7Site() {
		return action7Site;
	}

	public Rectangle getAction8Site() {
		return action8Site;
	}

	public Rectangle getAction9Site() {
		return action9Site;
	}

	public Rectangle getAction10Site() {
		return action10Site;
	}
	
	public Rectangle getAction11Site() {
		return action11Site;
	}

	public Rectangle getAction12Site() {
		return action12Site;
	}

	public Rectangle getAction13Site() {
		return action13Site;
	}

	public Rectangle getAction14Site() {
		return action14Site;
	}

	public Rectangle getAction15Site() {
		return action15Site;
	}

	public Rectangle getAction16Site() {
		return action16Site;
	}

	public Rectangle getAction17Site() {
		return action17Site;
	}

	public Rectangle getAction18Site() {
		return action18Site;
	}


	public Rectangle getQuestSite() {
		return questSite;
	}

	public Rectangle getOptionSite() {
		return optionSite;
	}

	public static int getACTIONBAR_WIDTH() {
		return ACTIONBAR_WIDTH;
	}

	public Rectangle getAction19Site() {
		return action19Site;
	}

	public Rectangle getAction20Site() {
		return action20Site;
	}

	public Rectangle getPreviousSite() {
		return previousSite;
	}

	public Rectangle getNextSite() {
		return nextSite;
	}

	public Circle getWorldSite() {
		return worldSite;
	}

	public Rectangle getZoneSite() {
		return zoneSite;
	}

	public boolean isClickOnNext() {
		return clickOnNext;
	}

	public void setClickOnNext(boolean clickOnNext) {
		this.clickOnNext = clickOnNext;
	}

	public boolean isClickOnPrevious() {
		return clickOnPrevious;
	}

	public void setClickOnPrevious(boolean clickOnPrevious) {
		this.clickOnPrevious = clickOnPrevious;
	}

	public boolean isClickOnWorld() {
		return clickOnWorld;
	}

	public void setClickOnWorld(boolean clickOnWorld) {
		this.clickOnWorld = clickOnWorld;
	}

	public boolean isClickOnZone() {
		return clickOnZone;
	}

	public void setClickOnZone(boolean clickOnZone) {
		this.clickOnZone = clickOnZone;
	}

	public boolean isClickOnQuest() {
		return clickOnQuest;
	}

	public void setClickOnQuest(boolean clickOnQuest) {
		this.clickOnQuest = clickOnQuest;
	}

	public boolean isClickOnOption() {
		return clickOnOption;
	}

	public void setClickOnOption(boolean clickOnOption) {
		this.clickOnOption = clickOnOption;
	}

	public GuiEngine getEngine() {
		return this.engine;
	}


}
