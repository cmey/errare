package physicsEngine;

import graphicsEngine.GraphicsEngine;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import main.Main;
import main.Rep;


/**
 * @author trueblue
 * 
 * Représentation d'une carte sur 4 strates : 1)les sols, 2)les objets que l'on
 * peut ramasser ou marcher dessus, 3)les gros objets et les personnages, 4) les
 * toits. Note : pour l'instant les batiments ne peuvent avoir qu'un seul étage.
 */
@SuppressWarnings("unchecked")
public class PhysicsEngine implements Runnable {

	private List<PhysicalRep>[][][] map;

	private List<DirectMovement> charMoves;

	public static final int TAILLECELL = 10;

	public static final int XMAP = 800;

	public static final int YMAP = 600;
	
	public static final int NLVL = 3;
	
	private Thread thread;
	
	private MainCharacter mainChar;
	
	private Main main;

	/**
	 * Cree une carte a partir d'un fichier
	 */
	public PhysicsEngine(Main m) {
		main = m;
		
		map = new List[NLVL][XMAP/10][YMAP/10];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j ++) {
				for (int k = 0; k < map[0][0].length; k ++)
					map[i][j][k] = Collections.synchronizedList(new LinkedList());
			}
		}

		charMoves = Collections.synchronizedList(new LinkedList());
		
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Determine les elements d'une strate a dessiner.
	 *  
	 * @param lvl
	 *            niveau a dessiner
	 * @return liste des elements a dessiner
	 */
	public List<PhysicalRep> getDrawLevel(int lvl) {
		List list = new LinkedList();
		
		for(int i=0; i<map[lvl].length; i++) {
			for(int j=0; j<map[lvl][i].length; j++) {
				list.addAll(map[lvl][i][j]);
			}
		}

		return list;
	}


	/**
	 * @param element
	 */
	public void addElement(PhysicalRep e) {
		
		int lvl = e.getLevel();
		int area[] = new int[4];
		
		int x1 = e.x/TAILLECELL;
		int y1 = e.y/TAILLECELL;
		int x2 = (e.x+e.width)/TAILLECELL;
		int y2 = (e.y+e.height)/TAILLECELL;
		if((e.x+e.width)%TAILLECELL!=0)
			x2++;
		if((e.y+e.height)%TAILLECELL!=0)
			y2++;
		
		try {
			for(int i=x1; i<x2; i++) {
				for(int j=y1; j<y2; j++) {
					map[lvl][i][j].add(e);
				}
			}
		} catch(ArrayIndexOutOfBoundsException aioobe) {}
		
		area[0] = x1;
		area[1] = y1;
		area[2] = x2;
		area[3] = y2;
		
		e.setArea(area);
		
		
	}

	/**
	 * @param object
	 */
	public void removeElement(PhysicalRep e) {
		
		int lvl = e.getLevel();
		
		int x1 = e.x/TAILLECELL;
		int y1 = e.y/TAILLECELL;
		int x2 = (e.x+e.width)/TAILLECELL;
		int y2 = (e.y+e.height)/TAILLECELL;
		if((e.x+e.width)%TAILLECELL!=0)
			x2++;
		if((e.y+e.height)%TAILLECELL!=0)
			y2++;
		
		for(int i=x1; i<x2; i++) {
			for(int j=y1; j<y2; j++) {
				map[lvl][i][j].remove(e);
			}
		}
		
	}

	
	public boolean isMovementOK(Character e, int lvl) {
		
		int x1 = (e.x)/TAILLECELL;
		int y1 = (e.y)/TAILLECELL;
		int x2 = (e.x+e.width)/TAILLECELL;
		int y2 = (e.y+e.height)/TAILLECELL;
		if((e.x+e.width)%TAILLECELL!=0)
			x2++;
		if((e.y+e.height)%TAILLECELL!=0)
			y2++;
		
		for(int i=x1; i<x2; i++) {
			for(int j=y1; j<y2; j++) {
				if(map[lvl][i][j].size() > 1) {
						return false;
				}
			}
		}
		

		return true;
	}

	public List<PhysicalRep> whatIsHere(Rectangle r, int lvl) {
		
		List<PhysicalRep> list = new LinkedList();
		
		int x1 = r.x/TAILLECELL;
		int y1 = r.y/TAILLECELL;
		int x2 = (r.x+r.width)/TAILLECELL;
		int y2 = (r.y+r.height)/TAILLECELL;
		if((r.x+r.width)%TAILLECELL!=0)
			x2++;
		if((r.y+r.height)%TAILLECELL!=0)
			y2++;
		
		

		for(int i=x1; i<x2; i++) {
			for(int j=y1; j<y2; j++) {
				list.addAll(map[lvl][i][j]);
			}
		}
		
		return list;
	}
	
	public List<PhysicalRep> whatIsHere(Rectangle r) {
		
		List<PhysicalRep> list = new LinkedList();
		
		int x1 = r.x/TAILLECELL;
		int y1 = r.y/TAILLECELL;
		int x2 = (r.x+r.width)/TAILLECELL;
		int y2 = (r.y+r.height)/TAILLECELL;
		if((r.x+r.width)%TAILLECELL!=0)
			x2++;
		if((r.y+r.height)%TAILLECELL!=0)
			y2++;
		
		
		for(int h=0; h<map.length; h++)
			for(int i=x1; i<x2; i++) {
				for(int j=y1; j<y2; j++) {
					list.addAll(map[h][i][j]);
				}
			}
		
		return list;
	}

	
	public List<PhysicalRep> whatIsHere(Point p, int lvl) {
		
		List<PhysicalRep> list = new LinkedList(); 
		
		int x = p.x/TAILLECELL;
		int y = p.y/TAILLECELL;
		
	
		list.addAll(map[lvl][x][y]);
		
		
		return list;
		
	}
	
public List<PhysicalRep> whatIsHere(Point p) {
		
		List<PhysicalRep> list = new LinkedList(); 
		
		int x = p.x/TAILLECELL;
		int y = p.y/TAILLECELL;
		
		for(int lvl=0; lvl<map.length; lvl++)
			list.addAll(map[lvl][x][y]);
		
		System.out.println(x+":"+y);
		for(PhysicalRep e : list)
			System.out.println(e);
		
		
		return list;
		
	}

	
	public void updateAreas(Character e) {
		
		int lvl = e.getLevel();
		int area[] = e.getArea();
		
		int x1 = area[0];
		int y1 = area[1];
		int x2 = area[2];
		int y2 = area[3];
		
		
		for(int i=x1; i<x2; i++) {
			for(int j=y1; j<y2; j++) {
				map[lvl][i][j].remove(e);
			}
		}
		
		
		x1 = e.x/TAILLECELL;
		y1 = e.y/TAILLECELL;
		x2 = (e.x+e.width)/TAILLECELL;
		y2 = (e.y+e.height)/TAILLECELL;
		if((e.x+e.width)%TAILLECELL!=0)
			x2++;
		if((e.y+e.height)%TAILLECELL!=0)
			y2++;
		
		for(int i=x1; i<x2; i++) {
			for(int j=y1; j<y2; j++) {
				map[lvl][i][j].add(e);
			}
		}
		
		area[0] = x1;
		area[1] = y1;
		area[2] = x2;
		area[3] = y2;
		
		e.setArea(area);
		
	}
	

	@SuppressWarnings("unchecked")
	public void addMove(DirectMovement mov) {
		
		synchronized(charMoves) {
			Iterator<DirectMovement> it = charMoves.iterator();
			while(it.hasNext()) {
				DirectMovement m = it.next();
				if (m.getChar() == mov.getChar()) {
					it.remove();
				}
			}
			
		
		charMoves.add(mov);
		}
		
	}
	

	/**
	 * Fait bouger les personnages et autres elements du jeu regulierement
	 */
	public void run() {
		long time, lastime = System.nanoTime();
		LinkedList<DirectMovement> remove = new LinkedList();
		
		while (true) {
			time = System.nanoTime();
			if ((time - lastime) > 20E6) {
				
				remove.clear();
				
				synchronized(charMoves) {
					for(Iterator<DirectMovement> it = charMoves.iterator(); it.hasNext();) {
						DirectMovement mov = it.next();
						try {
							mov.execute();
						} catch (MovementFinishedException e) {
							remove.add(mov);
						}
					}
					
					charMoves.removeAll(remove);
				}
				
				
				
				if(mainChar != null) {
					
					
					List<Rep> list = new LinkedList();
					
					Rectangle r = ((Rectangle)mainChar.clone());
					r.grow(100, 100);
					List<PhysicalRep> l = this.whatIsHere(r);
					for(PhysicalRep pr : l) {
						list.add(pr.getRep());
					}
				
					main.getGraphicsEngine().setDisplayList(list);
				}
				
				lastime = time;
			}
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
			}
		}

	}
	
	public void setMainChar(Rep r) {
		PhysicalRep pr = r.getPhysicsRep();
		if(pr instanceof MainCharacter)
			mainChar = (MainCharacter)pr;
		else
			System.err.println("Le personnage n'est pas un héros !");
	}

	/**
	 * @return
	 */
	public int getLevelsNumber() {

		return map.length;
	}

	public boolean[][] getBooleanRepresentation(int x, int y, int wtab, int htab, Character c) {
		int lvl = c.getLevel();
		
		
		boolean tab[][] = new boolean[wtab][htab];
		for(int i=0; i<wtab; i++)
			for(int j=0; j<htab; j++) {
				if(i+x>=map[0].length || j+y>=map[0][0].length || i+x<0 || j+y<0)
					tab[i][j] = false;
				else
					tab[i][j] = map[lvl][i+x][j+y].isEmpty() || map[lvl][i+x][j+y].contains(c);
			}
		
		return tab;
	}	
	
	/**
	 * Methode appelee par le moteur graphique lors dun clic de souris.
	 * @param e MouseEvent contenant les coordonnées du clic.
	 * @return true si il faut afficher le clic, false sinon.
	 */
	public boolean setClick(MouseEvent e, Point p) {
		//TODO: pour le picking
		boolean res = true;
		
		
		if (SwingUtilities.isLeftMouseButton(e)) {
			p.translate(-mainChar.width / 2, -mainChar.height / 2);
			AstarMovement asm = new AstarMovement(mainChar, p, this, null);
			res = asm.start();
		}
		else {

			List<PhysicalRep> l = this.whatIsHere(p);
			int size = l.size();
			if(l.contains(mainChar))
				size--;
			if(size > 0) {
				p.translate(-mainChar.width / 2, -mainChar.height / 2);
				AstarMovement asm = new AstarMovement(mainChar, p, this, l.get(0));
				res = asm.start();
			}
			else {
				res = false;
			}
				
		}
		
		return res;
	}

	public PhysicalRep load(PhysicalRep.type type, int x, int y, int width, int height, Rep rep) {
		
		if(type == PhysicalRep.type.MOBJECT)
			return new MObject(x, y, width, height, this, rep);
		else if(type == PhysicalRep.type.NMOBJECT)
			return new NMObject(x, y, width, height, this, rep);
		else if(type == PhysicalRep.type.CHARACTER)
			return new Character(x, y, width, height, this, rep);
		else
			return new MainCharacter(x, y, width, height, this, rep);
			
	}
	

}
