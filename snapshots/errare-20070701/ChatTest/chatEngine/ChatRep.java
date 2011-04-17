package chatEngine;

/**
 * A person in the chat. For demonstration purpose. It must be seen analogous to a Rep in Errare.
 * @author Christophe
 */
public class ChatRep{
	
	/**
	 * Who talks
	 */
	String name;
	
	public ChatRep(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
