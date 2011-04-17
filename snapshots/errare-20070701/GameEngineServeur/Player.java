import java.util.LinkedList;


public class Player extends Character {
	/**
	 * Area where the player is that is connected or not
	 */
	/*
	 * La zone dans lequel est le joueur qu'il soit connect� ou pas
	 */
	private Area currentArea;
	/**
	 * Name of player
	 */
	/*
	 * Le nom du joueur
	 */
	private String name;
	/**
	 * 
	 * @param area
	 * @param name
	 * @param x
	 * @param y
	 * @param z
	 */
	/*
	 * 
	 */
	public Player(Area area, String name, int x,int y, int z){
		super(x,y,z);
		this.currentArea=area;
		this.name=name;
	}
	/**
	 * 
	 * @param area1
	 * @param area2
	 */
	public void moveBetween(Area area1, Area area2) {
		area1.getPlayers().remove(this);
		area2.getPlayers().add(this);
		area2.setActive(true);
		this.currentArea=area2;
	}
	/**
	 * 
	 * @param linkedList
	 */
	public void aggro(LinkedList<? extends Character> linkedList){
		
	}
	/**
	 * 
	 *
	 */
	public void disconnect(){
		// on deconnect toujours le player
		this.currentArea.getPlayers().remove(this);
		currentArea.setActive(false);
	}
	/**
	 * 
	 *
	 */
	public void connect(){
		// on ne connect le player au monde que s'il n'est pas deja connectee
		if(!this.currentArea.getPlayers().contains(this)){
			this.currentArea.getPlayers().add(this);
			currentArea.setActive(true);
		}		
	}
	/**
	 * 
	 * @return 
	 */
	public String toString(){
		return name;
	}
	/**
	 * This methode collect Item from Dead character
	 * @return
	 */
	public Bag collect(NoPlayer character){
		return character.getItemDroppedList();
	}
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}