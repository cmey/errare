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

public class ActionBar {
	
	private final int MAX_ACTION=18;
	
	private Chat chat;
	private Inventory inventory;
	private Equipement equipement;
	
	private Rectangle actionBarSite, barSite, mapSite,
	generalChatSite, guildChatSite, groupChatSite, privateChatSite,inventorySite,EquipementSit,
	meleeXP, rangedXP, magicalXP,
	case1Site,case2Site,
	action1Site,action2Site,action3Site,action4Site,action5Site,action6Site,action7Site,action8Site,action9Site,action10Site,
	action11Site,action12Site,action13Site,action14Site,action15Site,action16Site,action17Site,action18Site,
	backward,forward,
	end;
	private Circle actionMapSite;
	
	//private int [] tabCase = new int[MAX_CASE];
	private int [] tabAction;;
	
	private ArrayList comboList;
	private boolean visible;
	 
	private GuiEngine engine;
	
	private int screenHeight, screenWidth;
	
	public static final int MagnetismPixelLenght = 30;
	public static final int ACTIONBAR_HEIGHT = 40, ACTIONBAR_WIDTH = 1024, ACTIONBARMAP_HEIGHT = 153;
	
	public ActionBar(GuiEngine engine) {
		
		this.engine = engine;	
		
		screenHeight = 768;
		screenWidth = 1024;
		
		tabAction = new int[MAX_ACTION];
		for (int i=0;i<MAX_ACTION;i++)tabAction[i]=0;
		
		System.out.println("H : "+engine.getScreenHeight()+" - W : "+engine.getScreenWidth());
		
		actionBarSite = new Rectangle((screenWidth-ACTIONBAR_WIDTH)/2,screenHeight-50,1024,50);
		actionMapSite = new Circle((screenWidth-ACTIONBAR_WIDTH)/2+906,screenHeight-87,68);
					
		case1Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+10,screenHeight-35,30,29);
		case2Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+43,screenHeight-35,30,29);
		
		action1Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+85,screenHeight-35,30,29);
		action2Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+118,screenHeight-35,30,29);
		action3Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+151,screenHeight-35,30,29);
		action4Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+184,screenHeight-35,30,29);
		action5Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+217,screenHeight-35,30,29);
		action6Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+250,screenHeight-35,30,29);
		action7Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+283,screenHeight-35,30,29);
		action8Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+316,screenHeight-35,30,29);	
		action9Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+358,screenHeight-35,30,29);
		action10Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+391,screenHeight-35,30,29);
		action11Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+424,screenHeight-35,30,29);
		action12Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+457,screenHeight-35,30,29);
		action13Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+490,screenHeight-35,30,29);
		action14Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+523,screenHeight-35,30,29);
		action15Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+556,screenHeight-35,30,29);
		action16Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+589,screenHeight-35,30,29);
		action17Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+622,screenHeight-35,30,29);
		action18Site = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+655,screenHeight-35,30,29);
		
		backward = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+688,screenHeight-35,14,29);
		forward = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+705,screenHeight-35,14,29);

		end = new Rectangle(((screenWidth-ACTIONBAR_WIDTH)/2)+984,screenHeight-35,14,29);
		
		// ...
		
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
	
	public int getActionAtThisPoint(Point p) {
		int res=-1;
		
		//System.out.println(case1Site.x+" <= "+p.x+" <= "+(case1Site.x+case1Site.width)+" and "+case1Site.y+" <= "+p.y+" <= "+(case1Site.y+case1Site.width));
		res = (case1Site.contains(p)) ? 0 : -1;
		if (res==-1) res = (action1Site.contains(p)) ? 11 : -1;
		if (res==-1) res = (action2Site.contains(p)) ? 12 : -1;
		if (res==-1) res = (action3Site.contains(p)) ? 13 : -1;
		if (res==-1) res = (action4Site.contains(p)) ? 14 : -1;
		if (res==-1) res = (action5Site.contains(p)) ? 15 : -1;
		if (res==-1) res = (action6Site.contains(p)) ? 16 : -1;
		if (res==-1) res = (action7Site.contains(p)) ? 17 : -1;
		if (res==-1) res = (action8Site.contains(p)) ? 18 : -1;
		if (res==-1) res = (action9Site.contains(p)) ? 19 : -1;
		if (res==-1) res = (action10Site.contains(p)) ? 20 : -1;
		if (res==-1) res = (backward.contains(p)) ? 21 : -1;
		if (res==-1) res = (forward.contains(p)) ? 22 : -1;
		if (res==-1) res = (end.contains(p)) ? 23 : -1;

		return res;
		
	}
	
	public void updateXP() {
		
	}
	
	public void openChat(int channel) {
		chat.openChannel(channel);
	}
	
	public void openEquipement() {
		equipement.open();
	}
	
	public void openInventory() {
		inventory.open();
	}
	
	public void closeChat() {
		chat.close();
	}
	
	public void closeEquipement() {
		equipement.close();
	}
	
	public void closeInventory() {
		inventory.close();
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


	public Rectangle getCase1Site() {
		return case1Site;
	}

	public Rectangle getCase2Site() {
		return case2Site;
	}



}
