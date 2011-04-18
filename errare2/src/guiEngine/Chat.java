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
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import xmlEngine.XmlEngine;


public class Chat {
	
	private Rectangle chatDragBarSite;
	private Rectangle chatOpenSite;
	private Rectangle chatCloseSite;
	private Rectangle upOpenSite;
	private Rectangle upCloseSite;
	private Rectangle lockOpenSite;
	private Rectangle lockCloseSite;
	private Rectangle closeOpenSite;
	private Rectangle closeCloseSite;
	private Rectangle generalCloseChannelSite;
	private Rectangle generalOpenChannelSite;
	private Rectangle guildCloseChannelSite;
	private Rectangle guildOpenChannelSite;
	private Rectangle groupCloseChannelSite;
	private Rectangle groupOpenChannelSite;
	private Rectangle privateCloseChannelSite;
	private Rectangle privateOpenChannelSite;
	private Rectangle upwardsSite;
	private Rectangle scrollSite;
	private Rectangle downwardsSite;
	private Rectangle emotesOpenSite;
	private Rectangle emotesCloseSite;
	private Rectangle editableOpenSite;
	
	public static final int GENERAL_CHANNEL = 0 ;
	public static final int GUILD_CHANNEL = 1 ;
	public static final int GROUP_CHANNEL = 2 ;
	public static final int PRIVATE_CHANNEL = 3 ;
	public static final int CHAT_WIDTH = 380 ;
	public static final int CHAT_HEIGHT = 230 ;
	
	private int activeChannel = GENERAL_CHANNEL ; // -1 if no channel is activated
	
	private boolean visible;
	private boolean drag;
	private boolean activated;
	
	private String bufferGeneral;
	private String bufferGuild;
	private String bufferGroup;
	private String bufferPrivate; 
	private final String separator = "<>";
	private final int bufferMaxSize = 5000;
	
	private String[] generalMsg;
	private String[] guildMsg;
	private String[] groupMsg;
	private String[] privateMsg;
	
	private Point dragPoint;
	private Point DownLeftCornerPosition;
	
	private ActionBar actionBar;
	
	private int initialPositionX;
	private int initialPositionY;
			
	public Chat(ActionBar actionBar) {
		
		this.actionBar = actionBar;
		try {
			initialPositionX = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("chat.position_x")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("chat.position_x")).intValue();
			initialPositionY = actionBar.getEngine().isDEVELOPPEMENT() ? 
					new Integer(new XmlEngine().getString("chat.position_y")).intValue() :
					new Integer(actionBar.getEngine().getMain().getXmlEngine().getString("chat.position_y")).intValue();
			if (new XmlEngine().getString("chat.open").compareTo("true")==0) {
				visible = true;
			}else {
				visible = false;
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		bufferGeneral="";
		bufferGuild="";
		bufferGroup="";
		bufferPrivate="";
		
		chatOpenSite = new Rectangle(initialPositionX,initialPositionY-CHAT_HEIGHT,CHAT_WIDTH,CHAT_HEIGHT);
		chatCloseSite = new Rectangle(initialPositionX,initialPositionY-30,CHAT_WIDTH,20);
		chatDragBarSite = new Rectangle(initialPositionX,initialPositionY-CHAT_HEIGHT,CHAT_WIDTH,25);
		generalCloseChannelSite = new Rectangle(initialPositionX+92,initialPositionY-25,11,11);
		generalOpenChannelSite = new Rectangle(initialPositionX+92,initialPositionY-225,11,11);
		guildCloseChannelSite = new Rectangle(initialPositionX+153,initialPositionY-25,11,11);
		guildOpenChannelSite = new Rectangle(initialPositionX+153,initialPositionY-225,11,11);
		groupCloseChannelSite = new Rectangle(initialPositionX+213,initialPositionY-25,11,11);
		groupOpenChannelSite = new Rectangle(initialPositionX+213,initialPositionY-225,11,11);
		privateCloseChannelSite = new Rectangle(initialPositionX+271,initialPositionY-25,11,11);
		privateOpenChannelSite = new Rectangle(initialPositionX+271,initialPositionY-225,11,11);	
		upCloseSite = new Rectangle(initialPositionX+316,initialPositionY-27,13,14);
		upOpenSite = new Rectangle(initialPositionX+316,initialPositionY-227,13,14);
		lockCloseSite = new Rectangle(initialPositionX+334,initialPositionY-27,13,14);
		lockOpenSite = new Rectangle(initialPositionX+334,initialPositionY-227,13,14);
		closeCloseSite = new Rectangle(initialPositionX+352,initialPositionY-27,13,14);
		closeOpenSite = new Rectangle(initialPositionX+352,initialPositionY-227,13,14);
		emotesOpenSite = new Rectangle(initialPositionX+9,initialPositionY-45,25,23);
		editableOpenSite = new Rectangle(initialPositionX+41,initialPositionY-44,330,22);
	
		activated=false;
		drag=false;
		DownLeftCornerPosition = new Point(initialPositionX,initialPositionY);
		
		generalMsg = new String[100];
		guildMsg = new String[100];
		groupMsg = new String[100];
		privateMsg = new String[100];
		for (int i=0;i<100;i++) {
			generalMsg[i]="";
			guildMsg[i]="";
			groupMsg[i]="";
			privateMsg[i]="";
		}
	}
	
	public Rectangle getEditableOpenSite() {
		return editableOpenSite;
	}

	public void openChannel(int channel) {
		
		if (!visible) {
			activeChannel=channel;
			visible=true;
			this.setDefaultPosition();
			
		}
	}
	
	public void changeChannel(int newChannel) {
		
		activeChannel=newChannel;
	}
	
	public void close() {
		
		if (visible) {
			visible=false;
			this.setDefaultPosition();
		}
	}
	
	public void scrollUp(int value) {
		
	}
	
	public void scrollDown(int value) {
		
	}
	
	public void addMessage(String message, int channel) {
		
		switch (channel) {
		case GENERAL_CHANNEL: 
			if (bufferGeneral.length()+message.length()>bufferMaxSize) {
				bufferGeneral = bufferGeneral.substring(0,bufferGeneral.length()-message.length()-separator.length());
			}
			bufferGeneral+=separator+message;
			uploadChat(channel);
			break;
		case GUILD_CHANNEL: 
			if (bufferGuild.length()+message.length()>bufferMaxSize) {
				bufferGuild = bufferGuild.substring(0,bufferGuild.length()-message.length()-separator.length());
			}
			bufferGuild+=separator+message;
			uploadChat(channel);
			break;
		case GROUP_CHANNEL: 
			if (bufferGroup.length()+message.length()>bufferMaxSize) {
				bufferGroup = bufferGroup.substring(0,bufferGroup.length()-message.length()-separator.length());
			}
			bufferGroup+=separator+message;
			uploadChat(channel);
			break;
		case PRIVATE_CHANNEL: 
			if (bufferPrivate.length()+message.length()>bufferMaxSize) {
				bufferPrivate = bufferPrivate.substring(0,bufferPrivate.length()-message.length()-separator.length());
			}
			bufferPrivate+=separator+message;
			uploadChat(channel);
			break;
			
		default : break;
		
		}
	}
	
	public void uploadChat (int channel) {
		
		StringTokenizer tokenizer;
		int i;
		
		switch (channel) {
		case GENERAL_CHANNEL: 
			tokenizer = new StringTokenizer(bufferGeneral,"<>");
			i = 0;
			while (tokenizer.hasMoreTokens()){
				generalMsg[i] = tokenizer.nextToken();
				i++;
			}
			break;
		case GUILD_CHANNEL: 
			tokenizer = new StringTokenizer(bufferGuild,"<>");
			i = 0;
			while (tokenizer.hasMoreTokens()){
				guildMsg[i] = tokenizer.nextToken();
				i++;
			}
			break;
		case GROUP_CHANNEL: 
			tokenizer = new StringTokenizer(bufferGroup,"<>");
			i = 0;
			while (tokenizer.hasMoreTokens()){
				groupMsg[i] = tokenizer.nextToken();
				i++;
			}
			break;
		case PRIVATE_CHANNEL: 
			tokenizer = new StringTokenizer(bufferPrivate,"<>");
			i = 0;
			while (tokenizer.hasMoreTokens()){
				privateMsg[i] = tokenizer.nextToken();
				i++;
			}
			break;
			
		default : break;
		
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
		if (p.getY()-CHAT_HEIGHT<=0) {
			p = new Point((int)p.getX(),CHAT_HEIGHT);
		}
		// Right
		if (p.getX()+CHAT_WIDTH>=actionBar.getScreenWidth()) {
			p = new Point(actionBar.getScreenWidth()-CHAT_WIDTH,(int)p.getY());
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

	public int getActiveChannel() {
		return activeChannel;
	}

	public boolean isVisible() {
		return visible;
	}

	public Rectangle getChatOpenSite() {
		return chatOpenSite;
	}

	public Rectangle getChatCloseSite() {
		return chatCloseSite;
	}
	
	public Rectangle getUpOpenSite() {
		return upOpenSite;
	}
	
	public Rectangle getUpCloseSite() {
		return upCloseSite;
	}

	public Rectangle getCloseOpenSite() {
		return closeOpenSite;
	}

	public Rectangle getGeneralCloseChannelSite() {
		return generalCloseChannelSite;
	}

	public Rectangle getGeneralOpenChannelSite() {
		return generalOpenChannelSite;
	}

	public Rectangle getGroupCloseChannelSite() {
		return groupCloseChannelSite;
	}

	public Rectangle getGroupOpenChannelSite() {
		return groupOpenChannelSite;
	}

	public Rectangle getGuildCloseChannelSite() {
		return guildCloseChannelSite;
	}

	public Rectangle getGuildOpenChannelSite() {
		return guildOpenChannelSite;
	}

	public Rectangle getLockCloseSite() {
		return lockCloseSite;
	}

	public Rectangle getLockOpenSite() {
		return lockOpenSite;
	}

	public Rectangle getPrivateCloseChannelSite() {
		return privateCloseChannelSite;
	}

	public Rectangle getPrivateOpenChannelSite() {
		return privateOpenChannelSite;
	}

	public String getBufferGeneral() {
		return bufferGeneral;
	}

	public Rectangle getEmotesOpenSite() {
		return emotesOpenSite;
	}

	public Point getPosition() {
		return DownLeftCornerPosition;
	}

	public void setPosition(Point DownLeftCornerPosition) {
		
		this.DownLeftCornerPosition = DownLeftCornerPosition;
	
		chatOpenSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-CHAT_HEIGHT,CHAT_WIDTH,CHAT_HEIGHT);
		chatDragBarSite = new Rectangle((int)getPosition().getX(),(int)getPosition().getY()-CHAT_HEIGHT,CHAT_WIDTH,25);
		generalOpenChannelSite = new Rectangle((int)getPosition().getX()+92,(int)getPosition().getY()-225,11,11);
		guildOpenChannelSite = new Rectangle((int)getPosition().getX()+153,(int)getPosition().getY()-225,11,11);
		groupOpenChannelSite = new Rectangle((int)getPosition().getX()+213,(int)getPosition().getY()-225,11,11);
		privateOpenChannelSite = new Rectangle((int)getPosition().getX()+271,(int)getPosition().getY()-225,11,11);	
		upOpenSite = new Rectangle((int)getPosition().getX()+316,(int)getPosition().getY()-227,13,14);
		lockOpenSite = new Rectangle((int)getPosition().getX()+334,(int)getPosition().getY()-227,13,14);
		closeOpenSite = new Rectangle((int)getPosition().getX()+352,(int)getPosition().getY()-227,13,14);
		emotesOpenSite = new Rectangle((int)getPosition().getX()+9,(int)getPosition().getY()-45,25,23);
		editableOpenSite = new Rectangle((int)getPosition().getX()+41,(int)getPosition().getY()-44,330,22);		
	}

	public void setDefaultPosition() {
				
		DownLeftCornerPosition = new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+20,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
		
		chatDragBarSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+20,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-CHAT_HEIGHT,CHAT_WIDTH,25);
		
		chatCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+20,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-30,CHAT_WIDTH,20);
		chatOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+20,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-CHAT_HEIGHT,CHAT_WIDTH,CHAT_HEIGHT);
		
		generalCloseChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+112,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,11,11);
		generalOpenChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+112,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-225,11,11);
		
		guildCloseChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+173,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,11,11);
		guildOpenChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+173,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-225,11,11);
		
		groupCloseChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+233,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,11,11);
		groupOpenChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+233,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-225,11,11);
		
		privateCloseChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+291,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-25,11,11);
		privateOpenChannelSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+291,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-225,11,11);	
		
		upCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+336,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		upOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+336,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-227,13,14);
		
		lockCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+354,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		lockOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+354,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-227,13,14);
		
		closeCloseSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+372,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-27,13,14);
		closeOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+372,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-227,13,14);

		emotesOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+29,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-45,25,23);
		editableOpenSite = new Rectangle((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+61,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT-44,330,22);

	}

	public Point getDefaultPosition() {
		return new Point((actionBar.getScreenWidth()-ActionBar.ACTIONBAR_WIDTH)/2+20,actionBar.getScreenHeight()-ActionBar.ACTIONBAR_HEIGHT);
	}

	public Rectangle getCloseCloseSite() {
		return closeCloseSite;
	}


	public String[] getMessage(int channel) {
		
		switch (channel){
		case GENERAL_CHANNEL:
			return generalMsg;
		case GUILD_CHANNEL:
			return guildMsg;
		case GROUP_CHANNEL:
			return groupMsg;
		case PRIVATE_CHANNEL:
			return privateMsg;
		default :
			return generalMsg;
		}
	}

	public Rectangle getChatDragBarSite() {
		return chatDragBarSite;
	}

	protected boolean isActivated() {
		return activated;
	}

	protected void setActivated(boolean activated) {
		this.activated = activated;
	}









}
