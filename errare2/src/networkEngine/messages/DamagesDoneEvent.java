package networkEngine.messages;

/**
 * A message that the server sends to a client, telling that
 * a character was damaged. The client that receives this
 * message should display over the concerned character
 * the amount of damages that was done onto it.
 * @author cyberchrist
 *
 */
public class DamagesDoneEvent extends DoneEvent{
    
    public DamagesDoneEvent(){
        //super(MessageClassChoice.DamagesDoneEvent);
    }

	/*
	 * who has been damaged
	 */
	private int who;
	
	/*
	 * how much dmg has been done
	 */
	private int HPnow;
	
}
