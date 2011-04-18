package networkEngine.messages;

import gameEngine.GameCharacter;

/**
 * A message that the server sends to a client, telling that a new character
 * has entered the area of this client. The client receiving this message
 * should create a new Character object, using the information given in
 * this message. This requires that this message contains enough information
 * for the client to recreate the Character on his side.
 * So the client must manage a new unique ID for this character, the new
 * unique ID is given altogether in this message.
 * 
 * @author cyberchrist
 */
public class NewCharacterInYourAreaDoneEvent extends DoneEvent{

	/*
	 * the uniqueID of this new character
	 * it should be the account-ID for players, and a static-ID for mobs
	 */
	private int who;
	
	/*
	 * all the information the client needs to know to instanciate the new character on the client side
	 */
	private GameCharacter newChar;
	
	
	public NewCharacterInYourAreaDoneEvent() {
		
	}
	
}
