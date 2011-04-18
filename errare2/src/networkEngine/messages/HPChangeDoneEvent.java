package networkEngine.messages;

/**
 * A message that the server sends to a client, telling that a character
 * has got it's HP modified. The client that receives this message should
 * update his own representation of the concerned character, setting the
 * new value of Hit Points.
 * 
 * If the value of HPnow is 0, this message means that the character is now dead.
 * 
 * @author cyberchrist
 *
 */
public class HPChangeDoneEvent extends DoneEvent{

	/*
	 * who has got his HP's changed
	 */
	private int who;
	
	/*
	 * the value of his HP now
	 */
	private int HPnow;
	
	
	public HPChangeDoneEvent() {
		
	}
	
	

	/**
	 * @return the hPnow
	 */
	public int getHPnow() {
		return HPnow;
	}

	/**
	 * @param pnow the hPnow to set
	 */
	public void setHPnow(int pnow) {
		HPnow = pnow;
	}

	/**
	 * @return the who
	 */
	public int getWho() {
		return who;
	}

	/**
	 * @param who the who to set
	 */
	public void setWho(int qui) {
		this.who = qui;
	}
	
}
