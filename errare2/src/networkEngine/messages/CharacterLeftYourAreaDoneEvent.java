package networkEngine.messages;

/**
 * A message that the server sends to a client, telling that a character
 * has left the area of this client : he is now either too far, or occluded.
 * The client receiving this message
 * can unload from memory the Character indicated by the given unique ID,
 * OR keep the character in memory, acting as a cache, saving a further load
 * in the case of the same character entering again in the area.
 *
 * @author cyberchrist
 */


public class CharacterLeftYourAreaDoneEvent extends DoneEvent{

	/*
	 * who has quit our area
	 */
	private int who;
	
	
	public CharacterLeftYourAreaDoneEvent() {
		
	}
}
