package networkEngine.messages;

/**
 * A message that the server sends to a client, telling that
 * a character has rotated. The client receiving this message
 * should update his own representation of the character,
 * setting the rotation to the new value, optionnaly by
 * triggering an animation helping to understand that the
 * character is rotating.
 * 
 * @author cyberchrist
 *
 */
public class RotateDoneEvent extends DoneEvent{
	/*
	 * who has got his orientation changed
	 */
	private int who;
	
	/*
	 * the value of his orientation now (around the vertical axis)
	 */
	private float orientation;
	
	
	public RotateDoneEvent() {
		
	}
	
}
