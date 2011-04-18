package gameEngine;

import genericEngine.Engine;

import java.net.Socket;
import java.util.Hashtable;


/**
 * A game engine that can be either Client or Server.
 * @author Christophe
 *
 */
public abstract class GameEngine implements Engine{
	
	protected static GameEngine game;
	
	/** is set to false by the method quit. used to stop the thread GameEngine*/
	protected boolean continue_thread = true;
	protected Hashtable<String, GameCharacter> gameCharacter;
	
	public GameEngine(){
		gameCharacter = new Hashtable<String, GameCharacter>();
	}
	
	//public abstract void send(ActionClient action, Socket socket);
	
	
	public void addGameCharacter(String id, GameCharacter c){
		gameCharacter.put(id, c);
	}
	public GameCharacter getGameCharacter(String id){
		return gameCharacter.get(id);
	}
	
	public void quit() {
		continue_thread = false;
	}
}
