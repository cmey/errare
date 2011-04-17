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

import gameEngine.ItemCharacteristics;
import graphicsEngine.GraphicalRep;
import graphicsEngine.MD2;
import guiEngine.GuiEngine;
import guiEngine.GuiInventory;
import guiEngine.GuiRep;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import main.ItemRep;
import main.Main;
import main.Rep;


/**
 * @author trueblue
 * 
 * Représentation d'une carte sur 4 strates : 1)les sols, 2)les objets que l'on
 * peut ramasser ou marcher dessus, 3)les gros objets et les personnages, 4) les
 * toits. Note : pour l'instant les batiments ne peuvent avoir qu'un seul étage.
 */
public class PhysicsEngine implements Runnable {
	
	private PhysicalRep[][][] map;
	
	private Hashtable<CharacterPRep, AStarMovement> charMoves;
	private Hashtable<CharacterPRep, CharacterPRep> combats;
	private ArrayList<CharacterPRep> combatsRemove;
	
	public static final int CELLDIM = 50;
	
	private int width;
	
	private int height;
	
	public static final int NLVL = 2;
	public static final int ITEM=0;
	public static final int CHARNSET=1;

	private static final int CUBESNB = 40;
	
	private CharacterPRep mainChar;
	
	private Main main;
	
	private Point lastClickPoint;

	private TriangleCouple[][] triangles;

	private boolean[][] ground;

	private int combatCount;

	private CubeTree cubeTree;

	private int triangleWidth;

	
	
	
	/**
	 * Cree une carte a partir d'un fichier
	 */
	public PhysicsEngine(Main m) {
		main = m;
		
		charMoves = new Hashtable<CharacterPRep, AStarMovement>(10);
		combats = new Hashtable<CharacterPRep, CharacterPRep>(10);
		lastClickPoint=new Point();
		combatsRemove = new ArrayList<CharacterPRep>();
	}
	
	public boolean add(PhysicalRep e, int lvl) {
		
		
		for(int i=e.cx(); i<e.cx()+e.cw(); i++) {
			for(int j=e.cy(); j<e.cy()+e.ch(); j++) {
				if(!ground[i][j] || map[lvl][i][j]!=null) {
					return false;
				}
			}
		}
		
		
		for(int i=e.cx(); i<e.cx()+e.cw(); i++) {
			for(int j=e.cy(); j<e.cy()+e.ch(); j++) {
				map[lvl][i][j]=e;
			}
		}
		
		//if(e.getCube().getTree()==null)
			cubeTree.add(e.getCube());
		
		return true;
	}
	
	/**
	 * @param element
	 */
	public boolean addCharacter(CharacterPRep e) {
		return add(e, CHARNSET);
		
	}
	
	/**
	 * @param element
	 */
	public boolean addItem(ItemPRep e) {
		return add(e, ITEM);
		
	}
	
	/**
	 * @param element
	 */
	public boolean addSetting(SettingPRep e) {
		return add(e, CHARNSET);
		
	}
	
	public void remove(PhysicalRep e, int lvl) {

		for(int i=e.cx(); i<e.cx()+e.cw(); i++) {
			for(int j=e.cy(); j<e.cy()+e.ch(); j++) {
				map[lvl][i][j]=null;
			}
		}
		
		e.getCube().removeFromTree();
	}
	
	/**
	 * @param object
	 */
	public void removeCharacter(CharacterPRep e) {
		remove(e, CHARNSET);
	}
	
	public void destroyCharacter(CharacterPRep e) {
		remove(e, CHARNSET);
		combatsRemove.add(e);
		for(CharacterPRep c : combats.keySet()) {
			if(combats.get(c)==e) {
				combatsRemove.add(c);
			}
		}
		charMoves.remove(e);
		
		
	}
	
	/**
	 * @param object
	 */
	public void removeItem(ItemPRep e) {
		remove(e, ITEM);
		e.getCube().removeFromTree();
		
	}
	
	
	public boolean isMovementOK(CharacterPRep e) {
		
		
		if(e.cx()<0 || e.cy()<0 || e.cx()+e.cw()>=width || e.cx()+e.ch()>=height) {
			return false;
		}
		
		
		for(int i=e.cx(); i<e.cx()+e.cw(); i++) {
			for(int j=e.cy(); j<e.cy()+e.ch(); j++) {
				if(!ground[i][j] || map[CHARNSET][i][j]!=null) {
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	public Set<PhysicalRep> whatIsHere(int x, int y, int w, int h, int lvl) {
		
		Set<PhysicalRep> list = new HashSet<PhysicalRep>(w*h*3); //regler taille set pour optimisation
		
		if(x<0)
			x=0;
		if(y<0)
			y=0;
		if(x+w>=width)
			w=width-x-1;
		if(y+h>=height)
			h=width-y-1;
		
			for(int i=x; i<x+w; i++) {
				for(int j=y; j<y+h; j++) {
					if(map[lvl][i][j]!=null)
						list.add(map[lvl][i][j]);
				}
			}
		
		return list;
	}
	
	public Set<PhysicalRep> whatIsHere(int x, int y, int w, int h) {
		Set<PhysicalRep> list = new HashSet<PhysicalRep>(w*h*3*NLVL);
		
		for(int i=0; i<NLVL; i++)
			list.addAll(whatIsHere(x, y, w, h, i));
		
		return list;
	}
	
	
	public Set<PhysicalRep> whatIsHere(int x, int y, int lvl) {
		
		Set<PhysicalRep> list = new HashSet<PhysicalRep>(3);
		
		if(x>=0 && y>=0 && x<width && y<height) {
			if(map[lvl][x][y]!=null)
				list.add(map[lvl][x][y]);
		}
		
		
		return list;
		
	}
	
	public Set<PhysicalRep> whatIsHere(int x, int y) {
		
		Set<PhysicalRep> list = new HashSet<PhysicalRep>(3*3); 
		
		if(x>=0 && y>=0 && x<width && y<height)
			for(int lvl=0; lvl<map.length; lvl++)
				if(map[lvl][x][y]!=null)
					list.add(map[lvl][x][y]);
		
		
		
		return list;
		
	}
	
	public void addMove(AStarMovement asm) {
		if(asm.getChar()==mainChar) {
			Point dest = asm.getDest();
			main.getNetworkClient().addMove(mainChar, dest.x, dest.y);
		}
		else {
			if(charMoves.containsKey(asm.getChar()))
				charMoves.get(asm.getChar()).setNextMovement(asm);
			else {
				asm.start();
				charMoves.put(asm.getChar(), asm);
			}
		}
	}
	
	public void addMove(CharacterPRep c, int x, int y) {
		addMove(new AStarMovement(c, x, y, this));
	}
	
	public void addNetworkMove(CharacterPRep c, int x, int y) {
		AStarMovement asm = new AStarMovement(c, x, y, this);
		if(charMoves.containsKey(asm.getChar()))
			charMoves.get(asm.getChar()).setNextMovement(asm);
		else {
			asm.start();
			charMoves.put(asm.getChar(), asm);
		}
	}
	
	
	/**
	 * Fait bouger les personnages et autres elements du jeu regulierement
	 */
	public void run() {
		
		Enumeration<CharacterPRep> characters = charMoves.keys();
		
		CharacterPRep c = null;
		while(characters.hasMoreElements()) {
			c = characters.nextElement();
			
			AStarMovement asm = charMoves.get(c);
			try {
				asm.execute();
			} catch (MovementFinishedException e) {
				if(asm.getNextMovement()==null) {
					charMoves.remove(c);
				}
				else {
					charMoves.put(c, asm.getNextMovement());
					asm.getNextMovement().start();
				}
			}
		}
		
		Enumeration<CharacterPRep> attackers = combats.keys();
		
		if(combatCount++==Main.COMBATFREQ) {
			combatCount=0;
			c = null;
			while(attackers.hasMoreElements()) {
				c = attackers.nextElement();
				CharacterPRep defender = combats.get(c);
				if(defender.isInContact(c)) {
					if(!defender.isRunning()) {
						defender.setAngle(-c.getAngle());
					}
					main.getGameEngine().executeCombat(c.getRep().getCharacteristics(), defender.getRep().getCharacteristics());
				}
			}
		}
		
		for(CharacterPRep rem : combatsRemove) {
			combats.remove(rem);
		}
		
	}
	
	public boolean[][] getBooleanRepresentation(int x, int y, int wtab, int htab, CharacterPRep c) {
		
		
		boolean tab[][] = new boolean[wtab][htab];
		for(int i=0; i<wtab; i++)
			for(int j=0; j<htab; j++) {
				
				if(i+x>=map[0].length || j+y>=map[0][0].length || i+x<0 || j+y<0)
					tab[i][j] = false;
				else if(map[CHARNSET][i+x][j+y]==c) {
					tab[i][j]=true;
				}
				else {
						tab[i][j] = map[CHARNSET][i+x][j+y]==null && ground[i+x][j+y];
				}
			}
		
		return tab;
	}	
	
	public boolean isWalkable(int x, int y) {
		if(x>=map[0].length || y>=map[0][0].length || x<0 || y<0) {
			return false;
		}
		else {
			return map[CHARNSET][x][y]==null && ground[x][y];
		}
	}
	
	/**
	 * Methode appelee par le moteur graphique lors dun clic de souris.
	 * @param e MouseEvent contenant les coordonnées du clic.
	 * @return true si il faut afficher le clic, false sinon.
	 */
	public boolean setClick(MouseEvent e, Point p) {
		boolean res = false;
		
		int x=p.x/CELLDIM;
		int y=p.y/CELLDIM;
		
		if(x>=0 && y>=0 && x<width && y<height && ground[x][y] && p.distance(lastClickPoint)>CELLDIM) {
			lastClickPoint=p.getLocation();
			
			CharacterPRep guide = mainChar.getGuide();
			if(guide!=null)
				guide.removeFollower(mainChar);
			
			for(PhysicalRep pr : whatIsHere(x-1, y-1, 3, 3)) {
				if(pr instanceof CharacterPRep) {
					((CharacterPRep)pr).addFollower(mainChar);
					mainChar.setGuide((CharacterPRep)pr);
					break;
				}
			}
			
			addMove(mainChar, x, y);
		}
		return res;
	}

	public void addCombat(CharacterPRep attacker, CharacterPRep defender) {
		if(!combats.containsKey(defender)) {
			combats.put(attacker, defender);
		}
		//System.out.println("combat added");
	}
	
	public void removeCombat(CharacterPRep attacker) {
		combats.remove(attacker);
		//System.out.println("combat removed");
	}
	
	public CharacterPRep getEnnemy(CharacterPRep attacker) {
		return combats.get(attacker);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setMapPhysics(float[][] terrain, int triangleWidth) {
		
		this.triangleWidth=triangleWidth;
		
		
		width=((terrain.length-1)*triangleWidth)/CELLDIM;
		height=width;
		
		triangles = new TriangleCouple[terrain.length-1][terrain[0].length-1];
		
		
		for(int i=0, x=0; x<terrain.length-1; i+=triangleWidth, x++) {
			for(int j=0, y=0; y<terrain[0].length-1; j+=triangleWidth, y++) {
				
				Point3D tl = new Point3D(i, j, terrain[x][y]);
				Point3D tr = new Point3D(i+triangleWidth, j, terrain[x+1][y]);
				Point3D bl = new Point3D(i, j+triangleWidth, terrain[x][y+1]);
				Point3D br = new Point3D(i+triangleWidth, j+triangleWidth, terrain[x+1][y+1]);
				
				Triangle topLeft = new Triangle(tl, tr, br);
				Triangle bottomRight = new Triangle(tl, br, bl);
				
				triangles[x][y] = new TriangleCouple(topLeft, bottomRight);
			}
		}
		
		map = new PhysicalRep[NLVL][width][height];
		ground = new boolean[width][height];
		for(int i=0; i<ground.length; i++) {
			for(int j=0; j<ground[0].length; j++) {
				ground[i][j] = getTerrainHeightAt(i*CELLDIM-CELLDIM/2, j*CELLDIM-CELLDIM/2)>=0;
			}
		}
		
		float halfcubesize = (ground.length-1)*CELLDIM/2;
		try {
			cubeTree = new CubeTree(new Point3D(0, 0, halfcubesize), new Point3D(halfcubesize*2, halfcubesize*2, -halfcubesize), (ground.length*CELLDIM)/CUBESNB, null, 0);
			main.getGraphicsEngine().setDisplayList(cubeTree);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	public int getTerrainHeightAt(int x, int y) {
		int modX=x%triangleWidth;
		int modY=y%triangleWidth;
		int tx=x/triangleWidth;
		int ty=y/triangleWidth;
		
		//System.out.println(triangles.length+" tx="+tx+" ty="+ty);
		
		try {
			TriangleCouple couple = triangles[tx][ty];
			
			Triangle triangle;
			if(modX>modY) {
				triangle = couple.getTopRight();
			}
			else {
				triangle = couple.getBottomLeft();
			}
			
			return (int)triangle.solve(x, y);
		}catch(IndexOutOfBoundsException e) {
			return 0;
		}
		
	}

	public void setMainChar(CharacterPRep mainChar) {
		this.mainChar=mainChar;
		
	}
	/*
	public GuiInventory getGuiInventory() {
		return main.getGuiEngine().getInventory();
	}*/
	
	public int random(int i) {
		return main.getRandom().nextInt(i);
	}

	public CubeTree getCubeTree() {
		return cubeTree;
	}
	
}
