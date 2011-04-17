package chatEngine;

/**
 * The message moving across the network, containing the text that someone has typed,
 * and that goes to the server and then back to all the clients.
 * @author Christophe
 */
public class ChatTextMessage extends NetworkMessage{

	String text;
	ChatRep who;
	
	public ChatTextMessage(String text){
		this.text = text;
	}
	
	public void setWhoSends(ChatRep who){
		this.who = who;
	}
	
	public String getText(){
		return this.text;
	}
}
