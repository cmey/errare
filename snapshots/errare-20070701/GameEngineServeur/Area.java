import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * This Class is Area fonction for the game engine.
 * @author ITCsoft54
 */
public class Area implements Runnable{
	/** 
	 * En : List of character who'sn't player but has interaction with world 
	 * For Exemple wandering monster, patrols or messager
	 */
	/*
	 * List de charater qui ne sont pas des joueurs mais qui ont une interaction avec le monde
	 * Par exemple un monstre errant, une patrouille ou un messager
	 */
	private LinkedList<NoPlayer> activeCharacter;
	/** 
	 * En : List of character who'sn't player and who hasn't interaction with world 
	 */
	/*
	 * List de charater qui ne sont pas des joueurs et qui ont une interaction avec le monde
	 */
	private LinkedList<Character> inactiveCharacter;
	/**
	 * list of player's character.
	 */
	/*
	 * List des joueurs.
	 */
	private LinkedList<Player> players;
	/**
	 * Area around this Area
	 */
	private LinkedList<Area> aroundArea;
	private int[] xpoints;
	private int[] ypoints;
	private int nbpoints;
	private boolean cycle;
	private boolean active;
	/**
	 * Create a area area can be null juste if player cant be going to !
	 * @param northWeast
	 * @param north
	 * @param northEast
	 * @param weast
	 * @param east
	 * @param southWeast
	 * @param south
	 * @param southEast
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public Area(Point ... points){
		this.players=new LinkedList<Player>();
		this.aroundArea=new LinkedList<Area>();
		this.activeCharacter= new LinkedList<NoPlayer>();
		this.inactiveCharacter= new LinkedList<Character>();
		this.nbpoints=points.length;
		this.xpoints=new int[nbpoints];
		this.ypoints=new int[nbpoints];
		for(int i=0;i<points.length ;i++){
			xpoints[i]=points[i].x;
			ypoints[i]=points[i].y;
		}
		this.active=false;
	}
	/**
	 * Character Action's who has to execute
	 */
	/*
	 * Action des personnages a executer
	 */
	public void run(){
		/* 
		 * on traite dabors les PNJ puis les Joueurs si on traite tous les zones
		 * dans un meme cycle l'ordre n'a pas d'importance
		 */
		for(NoPlayer pnj:activeCharacter){
			if(pnj.getCycle()!=cycle){
				pnj.interact();
				pnj.setCycle(cycle);
			}
		}
		/*
		 * copy de la liste pour modification
		 */
		LinkedList<Player> tmp=(LinkedList<Player>) players.clone();
		for(Player player:tmp){
			player.interact();
			if(!this.contain(player)){
				System.out.print("quit ["+this+"]");
				// on supose que les zone sont ajouté dans l'ordre pour optimiser le changement de zone
				for(Area area:aroundArea){
					if(area.contain(player)){
						System.out.println(" for ["+ area +"]");
						player.moveBetween(this,area);
						break;
					}
				}
			}
		}
		/*
		 * on remet la copy en place 
		 */
		// si il n'y a plus de personnages dans la zone on la desactive.
		this.setActive(!this.players.isEmpty());
	}
	/**
	 * This methode permit to know if area contain character or not
	 * @param pnj character to test
	 * @return true if area contain the pnj
	 */
	public boolean contain(Character pnj) {
		Polygon p=new Polygon(xpoints,ypoints,nbpoints);
		return p.contains(new Point(pnj.getX(),pnj.getY()));
	}
	/**
	 * 
	 */
	/*
	 * ajout d'une zone dans laquel on peut ce retrouve si on sort de la zone
	 * on supose que les zones sont ajoutee de facon a optimiser le test de changement de zone
	 * les zone dont la sortie de contact sont les plus important en premier 
	 */
	public void addAroundArea(Area area){
		this.aroundArea.add(area);
	}
	/**
	 * This methode permit to recover list of Characters not players active
	 * @return list of active characters
	 */
	/*
	 * Cette methode permet de recuperer les listes des Personnages non joueurs actif 
	 * @return la liste des characters actif
	 */
	public LinkedList<? extends NoPlayer> getActiveCharacter() {
		return activeCharacter;
	}
	/**
	 * This methode permit to recover list of Characters players connected
	 * @return
	 */
	/*
	 * Cette methode permet de recuperer les listes des Personnages joueurs connect�
	 * @return
	 */
	public LinkedList<Player> getPlayers() {
		return players;
	}
	/**
	 * this methode permit to active the area
	 * @param b true to active false to disactivate
	 */
	public void setActive(boolean b){
		active=b;
	}
	/**
	 * this methode set the cycle of the area
	 * @param b the cycle
	 */
	public void setCycle(boolean b){
		this.cycle=b;
	}
	/**
	 * this methode get inactive character for exemple : for the aggro.
	 */
	public LinkedList<Character> getInactiveCharacter(){
		return inactiveCharacter;
	}
	public Polygon getPolygone(){
		return new Polygon(xpoints,ypoints,nbpoints);
	}
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}
	public boolean containPosition(int x, int y, int z) {
		// TODO Auto-generated method stub
		return this.getPolygone().contains(x, y);
	}
}
