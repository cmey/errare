package guiEngine;

import java.awt.Rectangle;
import java.util.StringTokenizer;

public class Chat {
	
	
	private Rectangle chatSite, displaySite, writeSite,
	generalChannelSite, guildChannelSite, groupChannelSite, privateChannelSite,
	upwardsSite, scrollSite, downwardsSite, emotesSite;
	
	private final int generalChannel = 0, guildChannel = 1, groupChannel = 2, privateChannel = 3;
	
	private int activeChannel; // -1 if no channel is activated
	
	private boolean visible;
	
	private String bufferGeneral, bufferGuild, bufferGroup, bufferPrivate; 
	private final String separator = "<>";
	private final int bufferMaxSize = 5000;
	
	public Chat() {
		
		chatSite = new Rectangle(0,0,0,0);
		// ...
		
		activeChannel=-1;
		visible=false;
	}
	
	public void openChannel(int channel) {
		
		if (!visible) {
			activeChannel=channel;
			visible=true;
		}
	}
	public void changeChannel(int newChannel) {
		
		if (visible) activeChannel=newChannel;
	}
	
	public void closeChannel() {
		
		if (visible) {
			activeChannel=-1;
			visible=false;
		}
	}
	
	public void scrollUp(int value) {
		
	}
	
	public void scrollDown(int value) {
		
	}
	
	public void addMessage(String message, int channel) {
		
		switch (channel) {
		case 0: 
			if (bufferGeneral.length()+message.length()>bufferMaxSize) {
				bufferGeneral = bufferGeneral.substring(0,bufferGeneral.length()-message.length()-separator.length());
			}
			bufferGeneral+=separator+message;
			uploadChat(channel);
			break;
		case 1: 
			if (bufferGuild.length()+message.length()>bufferMaxSize) {
				bufferGuild = bufferGuild.substring(0,bufferGuild.length()-message.length()-separator.length());
			}
			bufferGuild+=separator+message;
			uploadChat(channel);
			break;
		case 2: 
			if (bufferGroup.length()+message.length()>bufferMaxSize) {
				bufferGroup = bufferGroup.substring(0,bufferGroup.length()-message.length()-separator.length());
			}
			bufferGroup+=separator+message;
			uploadChat(channel);
			break;
		case 3: 
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
		
		switch (channel) {
		case 0: 
			tokenizer = new StringTokenizer(bufferGeneral);
			while (tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				// Les afficher ici
			}
			break;
		case 1: 
			tokenizer = new StringTokenizer(bufferGuild);
			while (tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				// Les afficher ici
			}
			break;
		case 2: 
			tokenizer = new StringTokenizer(bufferGroup);
			while (tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				// Les afficher ici
			}
			break;
		case 3: 
			tokenizer = new StringTokenizer(bufferPrivate);
			while (tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				// Les afficher ici
			}
			break;
			
		default : break;
		
		}
	}
}
