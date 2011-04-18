package gameEngine;

import java.net.Socket;
import networkEngine.NetworkMessageListener;

import networkEngine.messages.AbstractNetworkMessage;
import main.ClientMain;

public class GameEngineClient extends GameEngine implements NetworkMessageListener {

	ClientMain main;
	Playable myCharacter; // the gameCharacter controlled by the player
	
	public GameEngineClient(ClientMain main){
		super();
		this.main = main;
		main.getUserInputController().register(this, "attack");
		//ClientNetworkLayer.instance().connectToServer("localhost", 2000);
		GameEngine.game = this;
	}
	
	public void addGameCharacter(GameCharacter gameCharacter){
		
	}

	public void setMainChar(GameCharacter c){
		this.addGameCharacter(c);
	}
	
	public void attackTry(GameCharacter attacker){
		/*
		playAttack(attacker);
		AttackEvent eattack = new AttackEvent(main.getNetworkEngine().getMyID(),"gameEngineServer");
		main.getNetworkEngine().bufferOutEvent(eattack); // send the information "attack" to the server
		*/
	}

	void playAttack(GameCharacter attacker){
		attacker.getRep().getGraphicalRep().playAnimationOnce("attack");
		main.getSoundEngine().play("attack.ogg", false);
	}
	
	
	public boolean invokeKeyEvent(String action) {
		if(action.equals("attack") && myCharacter != null){ // we pressed the button, trigger the attack!
			attackTry(myCharacter);
		}
		return false;
	}

	public boolean invokeMouseEvent(String action, int x, int y) {
		if(action.equals("attack") && myCharacter != null){ // we pressed the button, trigger the attack!
			attackTry(myCharacter);
		}
		return false;
	}

	public void quit() {
		// TODO Auto-generated method stub
		
	}
	
	public void run(){
		
	}


//	public void send(ActionClient action, Socket socket) {
//		networkLayer.ClientNetworkLayer.instance().sendMessage((ActionClient)action);	
//	}

	public void handleNetworkMessage(AbstractNetworkMessage message) {
//		ActionClient action = (ActionClient)message;
//		if(action.getCode().compareTo("sound") == 0){
//			main.getSoundEngine().play(action.getCreator(), false);
//		}		
	}

}
