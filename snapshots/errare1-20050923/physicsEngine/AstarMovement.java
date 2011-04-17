package physicsEngine;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author trueblue
 * 
 * Définit un mouvement de personnage
 */
public class AstarMovement {
	/** Personnage a deplacer */
	private Character character;

	/** Destination */
	private Point dest;

	private PhysicsEngine map;
	
	private Node start, end;
	
	private Node tab[][];
	
	private LinkedList<Point> path;

	private PhysicalRep element;


	/**
	 * Cree un mouvement.
	 * 
	 * @param c
	 *            personnage a deplacer
	 * @param p
	 *            destination
	 * @param element 
	 */
	public AstarMovement(Character c, Point p, PhysicsEngine m, PhysicalRep element) {
		character = c;
		this.element = element;
		
		if(p.x<0)
			p.x=0;
		else if(p.x>PhysicsEngine.XMAP)
			p.x=PhysicsEngine.XMAP;
		
		if(p.y<0)
			p.y=0;
		else if(p.y>PhysicsEngine.YMAP)
				p.y=PhysicsEngine.YMAP;
			
		dest = p;
		map = m;
	}

	/**
	 * Retourne la destination du personnage.
	 * 
	 * @return destination
	 */
	public Point getDest() {
		return dest;
	}

	/**
	 * Retourne le personnage a deplacer.
	 * 
	 * @return personnage a deplacer
	 */
	public Character getChar() {
		return character;
	}


	/**
	 * Methode appelée au début du déplacement du personnage.
	 *
	 */
	@SuppressWarnings("unchecked")
	public boolean start() {
		int wtab = Math.abs((dest.x - character.x)/PhysicsEngine.TAILLECELL)+20;
		int htab = Math.abs((dest.y - character.y)/PhysicsEngine.TAILLECELL)+20;
		
		
		
		int x=0, y=0;
		
		if(character.x <= dest.x && character.y <= dest.y) {
			x = character.x/PhysicsEngine.TAILLECELL;
			y = character.y/PhysicsEngine.TAILLECELL;
		}
		else if(character.x >= dest.x && character.y >= dest.y) {
			x = dest.x/PhysicsEngine.TAILLECELL+1;
			y = dest.y/PhysicsEngine.TAILLECELL+1;
		}
		else if(character.x <= dest.x && character.y >= dest.y) {
			x = character.x/PhysicsEngine.TAILLECELL;
			y = dest.y/PhysicsEngine.TAILLECELL+1;
		}
		else if(character.x >= dest.x && character.y <= dest.y) {
			x = dest.x/PhysicsEngine.TAILLECELL+1;
			y = character.y/PhysicsEngine.TAILLECELL;
		}
		
		x-=10;
		y-=10;
		
		boolean[][] tabBool = map.getBooleanRepresentation(x, y, wtab, htab, character);
		
		Node tab[][] = new Node[tabBool.length][tabBool[0].length];
		for(int i=0; i<tab.length; i++)
			for(int j=0; j<tab[0].length; j++)
				if(tabBool[i][j])
					tab[i][j] = new Node(i, j, new Point((x+i)*PhysicsEngine.TAILLECELL, (y+j)*PhysicsEngine.TAILLECELL), tabBool[i][j]);
				else
					tab[i][j] = new Node(i, j, null, tabBool[i][j]);
		
		
		
			
		if(character.x <= dest.x && character.y <= dest.y) {
			start=tab[10][10];
			end=tab[wtab-10][htab-10];
		}
		else if(character.x >= dest.x && character.y >= dest.y) {
			start=tab[wtab-10][htab-10];
			end=tab[10][10];
		}
		else if(character.x <= dest.x && character.y >= dest.y) {
			start=tab[10][htab-10];
			end=tab[wtab-10][10];
		}
		else if(character.x >= dest.x && character.y <= dest.y) {
			start=tab[wtab-10][10];
			end=tab[10][htab-10];
		}
		
		
		this.tab = tab;
		
		Node current;
		
		
		LinkedList<Node> openlist = new LinkedList();
		LinkedList<Node> closedlist = new LinkedList();
		
		
		openlist.add(start);
		
		while(!closedlist.contains(end) && !openlist.isEmpty() && end.point != null) {
			
			Collections.sort(openlist);
			current = openlist.getFirst();
			closedlist.add(openlist.removeFirst());
			
			for(int i=-1; i<=1; i++)
				for(int j=-1; j<=1; j++) {
					try {
						if(tab[current.x+i][current.y+j].walkable && current.isDiagonalOkWith(tab[current.x+i][current.y+j]) 
								&& !closedlist.contains(tab[current.x+i][current.y+j])
								) {
							if(!openlist.contains(tab[current.x+i][current.y+j])) {
								openlist.add(tab[current.x+i][current.y+j]);
								tab[current.x+i][current.y+j].setParent(current);
							}
							else {
								if(tab[current.x+i][current.y+j].willGBeBetterThanCurrentGWithThisParent(current)) {
									tab[current.x+i][current.y+j].setParent(current);
								}
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {}
				}
			
			
			

		}
	
		if(closedlist.contains(end)) {
			LinkedList<Point> path =  new LinkedList();
			
			Node n = end;
			while(n.parent != null) {
				path.add(n.point);
				
				n = n.parent;
			}
			
			DirectMovement dm = new DirectMovement(character, dest, map,  null, element);
			for(Point p : path) {
				dm = new DirectMovement(character, p, map, dm, null);
			}
		
			dm.start();
			this.path = path;
			
			return true;
		}
		else
			return false;
		
		
		
		
	}
	
	public void draw(Graphics g) {
		
		g.setColor(Color.GREEN);
		if(start.point!=null)
			g.fillRect(start.point.x, start.point.y, 10, 10);
		g.setColor(Color.RED);
		if(end.point!=null)
			g.fillRect(end.point.x, end.point.y, 10, 10);
		
		g.setColor(Color.BLUE);
		
		int i=0;
		for(Point p : path) {
			g.fillRect(p.x, p.y, 2, 2);
		}
	}
	
	
	
	
	
	private class Node implements Comparable {
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
				return (tab[x-1][y].walkable && tab[x][y+1].walkable);
			
			else if(node.x==x+1 && node.y==y+1)
				return (tab[x][y+1].walkable && tab[x+1][y].walkable);
			
			else if(node.x==x+1 && node.y==y-1)
				return (tab[x+1][y].walkable && tab[x][y-1].walkable);
			
			else if(node.x==x-1 && node.y==y-1)
				return (tab[x-1][y].walkable && tab[x][y-1].walkable);
			
			
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
		
		private boolean willGBeBetterThanCurrentGWithThisParent(Node n) {
			if(n.x == x || n.y == y) 
				return 10<G;
			else
				return false;
		}
		
		public int compareTo(Object o) {
			return (F-((Node)o).F);
		}
		
	}
	
}
