package gameEngine;

import java.net.Socket;

/**
 * Represents a gameCharacter in the world (playable or not).
 * @author Christophe
 *
 */
public class GameCharacter extends GameRep{

	private String id;
	private int uniqueID;

	private Socket socket;
	
	private String name;
	private String gender;
	private String race;
	private double base_life;
	private double life;
	private double base_mana;
	private double mana;
	private double armor;
	private double block_chances;
	private double experience;
	
	

	public GameCharacter(String name){
		this.name = name;
		id = name;
		socket = null;
		
		base_life = 100;
		life = base_life;
		base_mana = 50;
		mana = base_mana;
		armor = 0;
		block_chances = 0;
		experience = 0;
	}
	
	public int getID() {
		return this.uniqueID;
	}
	
	public String getRace() {
		return this.race;
	}

	public String getGender() {
		return this.gender;
	}
	
	public void attack(GameCharacter target){
		//GameEngine.game.send(new ActionClient("attack", this.id, target.id), null);
	}
	
	public double getDamage(){
		return 1.0;
	}
	
	public double computeDamage(double physical){
		life -= physical;
		return 0; // for xp
	}
	
	public void xpGained(double px){
		experience += px;
	}
	
	public void setSocket(Socket sock){
		socket = sock;
	}

	public double getArmor() {
		return armor;
	}

	public double getBase_life() {
		return base_life;
	}

	public double getBase_mana() {
		return base_mana;
	}

	public double getBlock_chances() {
		return block_chances;
	}

	public double getExperience() {
		return experience;
	}

	public String getId() {
		return id;
	}

	public double getLife() {
		return life;
	}

	public double getMana() {
		return mana;
	}

	public String getName() {
		return name;
	}

	public Socket getSocket() {
		return socket;
	}
}
