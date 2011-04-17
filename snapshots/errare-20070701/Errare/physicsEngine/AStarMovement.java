/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package physicsEngine;


import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author trueblue
 * 
 * Class that calculates and executes the path of a character. It uses the A* pathfinding algotithm to solve the path.
 */
public class AStarMovement {
	/**Character to move*/
	private CharacterPRep character;
	
	/**Destination*/
	private Point dest;
	
	/**Reference to the physicsEngine where is executed the movement*/
	private PhysicsEngine physicsEngine;

	/**DirectMovement that will executed when the execute() methode is called*/
	private DirectMovement currentMovement;

	/**AStarMovement that will be executed at the end of the currentMovement*/
	private AStarMovement next;
	
	/**Area of the map represented as an entanglement of nodes where the movement is executed*/
	private Node map[][];
	
	/**Node from which the movement starts*/
	private Node start;
	
	/**Node where the movement ends*/
	private Node end;
	
	/**Path composed of a succession of points the character will follow*/
	private LinkedList<Point> path;
	
	
	
	
	/**
	 * Creates a movement. This constructor only sets the parameters of the movement.
	 * The methode start MUST then be called to calculate the movement BEFORE executing it.
	 * @param character charcter that does the movement
	 * @param destination point of destination of the movement
	 * @param physicsEngine physicsEngine on which the movement will be executed
	 * @param itemToPick item that will normally be picked at the end of the movement(if it is still there)
	 */
	public AStarMovement(CharacterPRep character, int x, int y, PhysicsEngine physicsEngine) {
		this.character = character;
		this.dest = new Point(x, y);
		this.physicsEngine = physicsEngine;
		
		
		
		CharacterPRep ennemy=null;
		for(PhysicalRep rep : physicsEngine.whatIsHere(x-1, y-1, 3, 3, PhysicsEngine.CHARNSET)) {
			if(rep instanceof CharacterPRep && rep != character) {
				ennemy = (CharacterPRep)rep;
				break;
			}
		}
		
		if(ennemy!=null) {
			if(physicsEngine.getEnnemy(character)!=ennemy) {
			physicsEngine.addCombat(character, ennemy);
			}
		}
		else {
			physicsEngine.removeCombat(character);
		}
	}
	
	/**
	 * Returns the destination point of the movement.
	 * @return the destination point of the movement
	 */
	public Point getDest() {
		return dest;
	}
	
	/**
	 * Returns the character that does the movement.
	 * @return the character that does the movement
	 */
	public CharacterPRep getChar() {
		return character;
	}
	
	
	/**
	 * Method that calculates the path which the character will follow.
	 * The path is composed of a series of DirectMovements that will be executed one after the other.
	 *
	 */
	public void start() {
		
		//TODO: optimiser qd il n'y a qu'une case a parcourir
		
		int wmap = Math.abs((dest.x - character.cx()))+20;
		int hmap = Math.abs((dest.y - character.cy()))+20;
		
		int x = Math.min(dest.x, character.cx());
		int y = Math.min(dest.y, character.cy());
		
		x-=10;
		y-=10;
		
		boolean[][] mapBool = physicsEngine.getBooleanRepresentation(x, y, wmap, hmap, character);
		
		map = new Node[mapBool.length][mapBool[0].length];
		for(int i=0; i<map.length; i++)
			for(int j=0; j<map[0].length; j++)
				map[i][j] = new Node(i, j, new Point((x+i), (y+j)), mapBool[i][j]);
		
		
		
		
		if(character.cx() <= dest.x && character.cy() <= dest.y) {
			start=map[10][10];
			end=map[wmap-10][hmap-10];
		}
		else if(character.cx() >= dest.x && character.cy() >= dest.y) {
			start=map[wmap-10][hmap-10];
			end=map[10][10];
		}
		else if(character.cx() <= dest.x && character.cy() >= dest.y) {
			start=map[10][hmap-10];
			end=map[wmap-10][10];
		}
		else if(character.cx() >= dest.x && character.cy() <= dest.y) {
			start=map[wmap-10][10];
			end=map[10][hmap-10];	
		}
		end.walkable=true;
		
		
		Node current;
		
		LinkedList<Node> openlist = new LinkedList<Node>();
		LinkedList<Node> closedlist = new LinkedList<Node>();
		
		
		openlist.add(start);
		
		
		while(!closedlist.contains(end) && !openlist.isEmpty()) {
			
			Iterator<Node> it = openlist.iterator();
			current = it.next();
			while(it.hasNext()) {
				Node n = it.next();
				if(n.F<current.F)
					current=n;
			}
			closedlist.add(current);
			openlist.remove(current);
			
			for(int i=-1; i<=1; i++)
				for(int j=-1; j<=1; j++) {
					try {
						if(map[current.x+i][current.y+j].walkable && current.isDiagonalOkWith(map[current.x+i][current.y+j]) 
								&& !closedlist.contains(map[current.x+i][current.y+j])
						) {
							if(!openlist.contains(map[current.x+i][current.y+j])) {
								
								openlist.add(map[current.x+i][current.y+j]);
								map[current.x+i][current.y+j].setParent(current);
								
							}
							else {
								if(map[current.x+i][current.y+j].willGBeBetterWithThisParent(current)) {
									map[current.x+i][current.y+j].setParent(current);
								}
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
			
			
			
			
		}
		
		
		path =  new LinkedList<Point>();
		
		Node n = end;
		while(n.parent != null) {
			path.add(n.point);
			
			n = n.parent;
		}
		
		currentMovement = new DirectMovement(character, start.point, physicsEngine);
		
		//TODO: if(!character.getRep().getGraphics().getCurrentAnimationName().startsWith("run"))
		if(!character.isRunning()) {
			character.getRep().getGraphics().startAnimation("run");
			character.setRunning(true);
		}
		//System.out.println(character.getRep().getGraphics().getAnimationsNames());
		//System.out.println(character.getRep().getGraphics().getCurrentAnimationName().startsWith("run"));
	}
	
	/**
	 * Executes the movement, following the path calculated by the start() method. 
	 * @throws MovementFinishedException when the path is totaly executed or the character encounters an obstacle
	 */
	public void execute() throws MovementFinishedException {
		try {
			try {
				currentMovement.execute();
			}
			catch(NullPointerException e) { //TODO: debug, for demo purposes
				throw new MovementFinishedException();
			}
		} catch (MovementFinishedException e) {
			
			if(next != null) {
				for(CharacterPRep cr : character.getFollowers()) {
					physicsEngine.addMove(new AStarMovement(cr, character.cx(), character.cy(), physicsEngine));
				}
				//character.getRep().getGraphics().startAnimation("stand");
				//character.setRunning(false);
				throw new MovementFinishedException();
			}
			if(path.size()==0) {
				/*Set<PhysicalRep> set = physicsEngine.whatIsHere(character.cx(), character.cy(), PhysicsEngine.CHARNSET);
				for(PhysicalRep r :set) {
					if(r instanceof CharacterPRep) {
						physicsEngine.addCombat(character, (CharacterPRep)r);
						break;
					}
				}*/
				
				for(PhysicalRep pr : physicsEngine.whatIsHere(dest.x-1, dest.y-1, 3, 3, PhysicsEngine.ITEM)) {
					if(pr instanceof ItemPRep) {
						//character.pick((ItemPRep)pr);
					}
				}
				for(CharacterPRep cr : character.getFollowers()) {
					physicsEngine.addMove(new AStarMovement(cr, character.cx(), character.cy(), physicsEngine));
				}
				character.getRep().getGraphics().startAnimation("stand");
				character.setRunning(false);
				throw new MovementFinishedException();
			}
			else {
				Point last = path.removeLast();
				currentMovement = new DirectMovement(character, last, physicsEngine);
				if(!path.isEmpty())
					last = path.getLast();
				for(CharacterPRep cr : character.getFollowers()) {
					physicsEngine.addMove(new AStarMovement(cr, last.x, last.y, physicsEngine));
				}
			}
		}
		
	}
	
	/**
	 * Sets the next movement that will be executed at the end when the current DirectMovement ends.
	 * @param next movement which will replace this movement
	 */
	public void setNextMovement(AStarMovement next) {
		Point d1 = next.getDest();
		Point d2 = this.getDest();
		if(d1.x!=d2.x || d1.y!=d2.y)
			this.next = next;
	}
	
	/**
	 * Gets the movement that will replace this movement
	 * @return the movement that will replace this movement
	 */
	public AStarMovement getNextMovement() {
		return next;
	}
	
	
	
	
	
	/**
	 * Implementation of a node.
	 * A node is a square on a map. It is used by the A* algorithm.
	 *
	 */
	private class Node implements Comparable<Node> {
		private Node parent;
		private int x, y;
		private int F, G, H;
		private Point point;
		private boolean walkable;
		
		private Node(int x, int y, Point p, boolean w) {
			this.x = x;
			this.y = y;
			point = p;
			walkable = w;
			
		}
		
		private boolean isDiagonalOkWith(Node node) {
			
			if(x == node.x || y == node.y)
				return true;
			
			if(node.x==x-1 && node.y==y+1)
				return (map[x-1][y].walkable && map[x][y+1].walkable);
			
			else if(node.x==x+1 && node.y==y+1)
				return (map[x][y+1].walkable && map[x+1][y].walkable);
			
			else if(node.x==x+1 && node.y==y-1)
				return (map[x+1][y].walkable && map[x][y-1].walkable);
			
			else if(node.x==x-1 && node.y==y-1)
				return (map[x-1][y].walkable && map[x][y-1].walkable);
			
			
			return false;
		}
		
		private void setParent(Node n) {
			parent = n;
			
			if(H==0) {
				int xDistance = Math.abs(x-end.x);
				int yDistance = Math.abs(y-end.y);
				if( xDistance > yDistance)
					H = 14*yDistance + 10*(xDistance-yDistance);
				else
					H = 14*xDistance + 10*(yDistance-xDistance);
			}
			
			if(parent.x == x || parent.y == y) 
				G=10;
			else
				G=14;
			
			F = G+H;
			
		}
		
		private boolean willGBeBetterWithThisParent(Node n) {
			if(n.x == x || n.y == y) 
				return 10<G;
			else
				return false;
		}
		
		public int compareTo(Node o) {
			return (this.F-o.F);
		}
		
	}
	
	
	
	
	
}
