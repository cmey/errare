package networkEngine.messages;

/**
 * A message that the server sends to a client, telling that someone
 * started an attack. The client receiving this message should
 * start an animation for his own representation of the concerned
 * character, then fall back to the default "walk" animation.
 * @author cyberchrist
 *
 */
public class AttackMoveDoneEvent extends DoneEvent{

	/*
	 * who has started an attack sequence
	 */
	private int who;
	
	/*
	 * which type of attack, or what spell, has been started
	 * currently defaults to the single attack in the game : a kick
	 */
	private int type;
	
	
	public AttackMoveDoneEvent() {
		
	}
	
}
