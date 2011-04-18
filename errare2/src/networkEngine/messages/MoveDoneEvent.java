package networkEngine.messages;

import geom.Point;

/**
 * A message that the server sends to a client, telling that
 * a character has moved. The client receiving this message
 * should update his own representation of the character,
 * setting the position to the new value, optionnaly by
 * triggering an animation helping to understand that the
 * character is moving : "run".
 * 
 * @author cyberchrist
 *
 */
public class MoveDoneEvent extends DoneEvent{

	/*
	 * who has moved
	 */
	private int who;
	
	/*
	 * the position as of now
	 */
	private Point position;
	
	
	public MoveDoneEvent() {
		
	}
	
}
