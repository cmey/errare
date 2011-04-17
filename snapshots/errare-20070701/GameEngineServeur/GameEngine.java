import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.util.LinkedList;

/**
 * this class control interaction between world and players
 * @author Yannick
 */
public class GameEngine implements Runnable{
	/**
	 * world's Area
	 */
	private LinkedList<Area> worldArea;
	/**
	 * Player to connect
	 */
	private LinkedList<Player> playersToConnect;
	/**
	 * Player to deconnect
	 */
	private LinkedList<Player> playersToDisconnect;
	/**
	 * Player connected
	 */
 	private LinkedList<Player> connectedPlayer;
	/**
	 * Cycle to synchronize gameEngine
	 */
	private static boolean CYCLE=true;
	/**
	 * Specific gameEngine with data when you start Simulation.
	 */
	public GameEngine(LinkedList<Area> linkedList){
		this();
		this.worldArea=linkedList;
	}
	/**
	 * Create gameEngine with data when you start the server.
	 */
	public GameEngine(){
		this.worldArea=new LinkedList<Area>();
		playersToConnect=new LinkedList<Player>();
		playersToDisconnect=new LinkedList<Player>();
		connectedPlayer=new LinkedList<Player>();
		/*
		 *  vide pour l'instant car je sais pas encore comment on obtient le monde 
		 *  par un ficheir XML, un base de donnee ?
		 */
	}
	/**
	 * Action's who has to execute
	 */
	public void run(){
		// ajout/ suppression de joueur au jeu
		if(!playersToConnect.isEmpty()){
			while(!playersToConnect.isEmpty()){
				Player player=playersToConnect.poll();
				player.connect();
				connectedPlayer.add(player);
			}
		}
		if(!playersToDisconnect.isEmpty()){
			while(!playersToDisconnect.isEmpty()){
				Player player=playersToDisconnect.poll();
				player.disconnect();
				connectedPlayer.remove(player);
			}
		}
		// Changement de cycle
		CYCLE=!CYCLE;
		// Copy de la liste pour travailler avec 
		LinkedList<Area> linkedList=(LinkedList<Area>) worldArea.clone();
		// traitement des zones active
		for(Area a:linkedList){
			if(a.isActive()){
				a.setCycle(CYCLE);
				a.run();
			}
		}
	}
	/**
	 * This methode insert player in queue to enter in the world
	 * @param player
	 */
	public void connectPlayer(Player player){
		this.playersToConnect.add(player);
	}
	/**
	 * This methode insert player in queue to quit in the world
	 * @param player
	 */
	public void disconnectPlayer(Player player){
		this.playersToDisconnect.add(player);
	}
//	/**
//	 * This methode get active area (with player)
//	 */
//	public LinkedList<Area> getActiveArea(){
//		return null;
//	}
	/**
	 * This methode get All World Areas linkedlist of area
	 * @return linkedlist of area
	 */
	public LinkedList<Area> getWorldAreas(){
		return worldArea;
	}
	/**
	 * This methode get player connected 
	 * @return linkedlist of player
	 */
	public LinkedList<Player> getConnectedPlayer(){
		return connectedPlayer;
	}
}
