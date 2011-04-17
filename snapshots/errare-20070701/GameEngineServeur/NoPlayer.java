
public class NoPlayer extends Character {
	/**
	 * 
	 */
	private boolean cycle;
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public NoPlayer(int x, int y, int z) {
		super(x, y, z);
	}
	public boolean getCycle() {
		return cycle;
	}
	/**
	 * 
	 * @param b
	 */
	public void setCycle(boolean b){
		this.cycle=b;
	}
	public Bag getItemDroppedList() {
		// TODO générateur de drop
		return null;
	}
}
