package gameEngine;

import java.net.Socket;
import java.util.ArrayList;

import networkEngine.NetworkMessageListener;
import networkEngine.messages.AbstractNetworkMessage;
import main.ServerMain;

public class GameEngineServer extends GameEngine implements NetworkMessageListener {
	
	ServerMain main;
	
	private ArrayList<Action> current_actions;
	
	public GameEngineServer(ServerMain main) {
		super();
		this.main = main;
//		ServerNetworkLayer.instance().register(new ActionClient("", "", ""), this);
		current_actions = new ArrayList<Action>();
	}
	
	public void gestionActions(){
		for(Action a : current_actions){
			if(a.perform()) current_actions.remove(a);
		}
	}
	
	public void run(){
			gestionActions();
	}

//	public void addAction(ActionClient o) {
//		if(o.getCode().compareTo("attack") == 0){
//			current_actions.add(new Attack(GameEngine.game.getGameCharacter(o.getCreator()), GameEngine.game.getGameCharacter(o.getTarget())));
//		}
//	}

	public void handleNetworkMessage(AbstractNetworkMessage message) {	
//		addAction((ActionClient)message);
//		gameCharacter.get( ((ActionClient)message).getCreator() ).setSocket((message.getSocket()));
	}

	
	/*
	void attack(GameCharacter attacker, String attackerID){
		float dist2 = attacker.getRep().getPhysicalRep().distance(attacker.target.getRep().getPhysicalRep());
		if(dist2 > attacker.range){
			//TODO: message the client : "target too far to attack"
			return;
		}else{
			GameCharacter target = attacker.target;
			target.pv -= attacker.dmg;
			ChangeHPEvent hpmodif = new ChangeHPEvent(main.getNetworkEngine().getMyID(),attackerID);
			main.getNetworkEngine().bufferOutEvent(hpmodif); // send the information "attack" to the server
		}
	}*/
	public boolean invokeKeyEvent(String action) {
		// never pass here (this is the server)
		return false;
	}
	public boolean invokeMouseEvent(String action, int x, int y) {
		// never pass here (this is the server)
		return false;
	}

//	public void send(ActionClient action, Socket socket) {
//		networkLayer.ServerNetworkLayer.instance().sendMessage(socket, (ActionClient)action);	
//	}

}
