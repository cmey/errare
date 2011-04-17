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

package main;

import gameEngine.Characters;
import gameEngine.GameEngine;
import gameEngine.Hero;
import gameEngine.ItemCharacteristics;
import gameEngine.Monster;
import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.MD2;
import guiEngine.GuiRep;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import networkEngine.NetworkClient;

import physicsEngine.CharacterPRep;
import physicsEngine.ItemPRep;
import physicsEngine.PhysicsEngine;
import physicsEngine.SettingPRep;

public class Generator {
	private Main main;
	private Random rand;
	private PhysicsEngine physicsEngine;
	private GraphicsEngine graphicsEngine;
	private NetworkClient networkClient;
	private GameEngine gameEngine;
	
	public static final int MAXSIZE=64;
	
	public Generator(Main main) {
		this.main = main;
		rand = main.getRandom();
		this.physicsEngine=main.getPhysicsEngine();
		this.graphicsEngine=main.getGraphicsEngine();
		this.networkClient=main.getNetworkClient();
		this.gameEngine=main.getGameEngine();
	}
	
	public void generate(int mapsize) {
		 
		generateTerrain(mapsize);
		placeVegetation((int) (mapsize*mapsize*0.0001), "data/objects/tree2.md2");
		placeVegetation((int) (mapsize*mapsize*0.0002), "data/objects/tree3.md2");
		placeVegetation((int) (mapsize*mapsize*0.0001), "data/objects/tree4.md2");
		placeVegetation((int) (mapsize*mapsize*0.0001), "data/objects/tree5.md2");
		placeVegetation((int) (mapsize*mapsize*0.0002), "data/objects/plant1.md2");
		placeVegetation((int) (mapsize*mapsize*0.0002), "data/objects/plant2.md2");
		placeVegetation((int) (mapsize*mapsize*0.0001), "data/objects/plant3.md2");
		placeVegetation((int) (mapsize*mapsize*0.0002), "data/objects/plant4.md2");
		placeVegetation((int) (mapsize*mapsize*0.0001), "data/objects/plant5.md2");

		generateMainChars(networkClient.getNbplayers(), networkClient.getId());
		generateSpawnPoints(networkClient.getNbai());
		
	}

	private void generateMainChars(int n, int id) {
		
		Hashtable<Integer, CharacterPRep> set = new Hashtable<Integer, CharacterPRep>(n);
		for(int i=0; i<n; i++) {
			try {
				if(i==id) {
					GraphicalRep grep = null;
					switch (n) {		
					case 1:grep = new MD2("data/md2/pknight/tris.md2");break;
					case 2:grep = new MD2("data/md2/sorcerer/tris.md2");break;
					case 3:grep = new MD2("data/md2/wintersfaerie/tris.md2");break;
					case 4:grep = new MD2("data/md2/yohko/tris.md2");break;
					default:grep = new MD2("data/md2/pknight/tris.md2");break;
					}
					CharacterPRep prep = new CharacterPRep(rand.nextInt(physicsEngine.getWidth()), rand.nextInt(physicsEngine.getHeight()), physicsEngine);
					Hero arep = new Hero("Errare", gameEngine);
					HeroRep rep = new HeroRep(arep, prep, grep);
					arep.setRep(rep);
					prep.setRep(rep);
					grep.setRep(rep);
				
					main.setMainChar(rep);
					
					set.put(i, prep);
				}
				else {
					GraphicalRep grep = null;
					switch (n) {		
					case 1:grep = new MD2("data/md2/pknight/tris.md2");break;
					case 2:grep = new MD2("data/md2/sorcerer/tris.md2");break;
					case 3:grep = new MD2("data/md2/wintersfaerie/tris.md2");break;
					case 4:grep = new MD2("data/md2/yohko/tris.md2");break;
					default:grep = new MD2("data/md2/pknight/tris.md2");break;
					}
					CharacterPRep prep = new CharacterPRep(rand.nextInt(physicsEngine.getWidth()), rand.nextInt(physicsEngine.getHeight()), physicsEngine);
					Hero arep = new Hero("Errare", gameEngine);
					CharacterRep rep = new CharacterRep(arep, prep, grep);
					arep.setRep(rep);
					prep.setRep(rep);
					grep.setRep(rep);
					
					set.put(i, prep);
				}
				
				
			}catch(IllegalArgumentException e) {
				i--;
			};
		}
		
		networkClient.setMainChars(set);
	}
	
	private void generateSpawnPoints(int n) {
		while(n>0) {
			GraphicalRep grep = null;
			switch (n) {		
			case 1:case 2:case 3:grep = new MD2("data/md2/awolf/tris.md2");break;
			case 4:case 5:case 6:grep = new MD2("data/md2/centaur/tris.md2");break;
			case 7:case 8:case 9:grep = new MD2("data/md2/cobra/tris.md2");break;
			case 10:case 11:case 12:grep = new MD2("data/md2/darkness/tris.md2");break;
			case 13:case 14:case 15:grep = new MD2("data/md2/demoness/tris.md2");break;
			case 16:case 17:case 18:grep = new MD2("data/md2/gnore/tris.md2");break;
			case 19:case 20:case 21:grep = new MD2("data/md2/goblin/tris.md2");break;
			case 22:case 23:case 24:grep = new MD2("data/md2/hobgoblin/tris.md2");break;
			case 25:case 26:case 27:grep = new MD2("data/md2/hydralisk/tris.md2");break;
			case 28:case 29:case 30:grep = new MD2("data/md2/insect/tris.md2");break;
			case 31:case 32:case 33:grep = new MD2("data/md2/jd/tris.md2");break;
			case 34:case 35:case 36:grep = new MD2("data/md2/necro/tris.md2");break;
			case 37:case 38:case 39:grep = new MD2("data/md2/ogre/tris.md2");break;
			case 40:case 41:case 42:grep = new MD2("data/md2/ogro/tris.md2");break;
			case 43:case 44:case 45:grep = new MD2("data/md2/ontra/tris.md2");break;
			case 46:case 47:case 48:grep = new MD2("data/md2/orc/tris.md2");break;
			case 49:case 50:case 51:grep = new MD2("data/md2/phantom/tris.md2");break;
			case 52:case 53:case 54:grep = new MD2("data/md2/purgator/tris.md2");break;
			case 55:case 56:case 57:grep = new MD2("data/md2/rat/tris.md2");break;
			case 58:case 59:case 60:grep = new MD2("data/md2/wraith2/tris.md2");break;
			default:grep = new MD2("data/md2/goblin/tris.md2");break;
			}
	
			try {
				CharacterPRep prep = new CharacterPRep(rand.nextInt(physicsEngine.getWidth()), rand.nextInt(physicsEngine.getHeight()), physicsEngine);
				Monster arep =null;
				if (n%16==0)arep = new Monster("Boss", 10, gameEngine,true);
				else arep = new Monster("Mob", 10, gameEngine,false);
				MonsterRep rep = new MonsterRep(arep, prep, grep);
				arep.setRep(rep);
				prep.setRep(rep);
				grep.setRep(rep);
				n--;
			}catch(IllegalArgumentException e) {};
		}
	}
	
	private void generateTerrain(int mapsize) {
		float[][] terrain = new float[MAXSIZE][MAXSIZE];
		
		
		generateMountains(terrain, mapsize*2);
			
		
		physicsEngine.setMapPhysics(terrain, (mapsize*PhysicsEngine.CELLDIM)/(terrain.length-1));
		graphicsEngine.setHeightMap(terrain, (mapsize*PhysicsEngine.CELLDIM)/(terrain.length-1));
		
	}
	
	private void placeVegetation(int density, String name) {
		for(int n=0; n<density; n++) {
			int cx=rand.nextInt(physicsEngine.getWidth());
			int cy=rand.nextInt(physicsEngine.getHeight());
			
			int rad=10+rand.nextInt(20);
			int startx = cx-rad;
			int endx=cx+rad;
			int starty=cy-rad;
			int endy=cy+rad;
			if(startx<0)
				startx=0;
			if(starty<0)
				starty=0;
			if(endx>physicsEngine.getWidth()-1)
				endx=physicsEngine.getWidth()-1;
			if(endy>physicsEngine.getHeight()-1)
				endy=physicsEngine.getHeight()-1;
			
			rad*=rad;
			
			
			for(int x=startx; x<endx; x++) {
				for(int y=starty; y<endy; y++) {
					int z = rad - ((x-cx)*(x-cx) + (y-cy)*(y-cy));
					if(z>0) {
						int r = rand.nextInt(rad*40);
						if(r<z) {
							try {
								if(rand.nextBoolean()) {
									GraphicalRep grep = new MD2(name);
									SettingPRep prep = new SettingPRep(x, y, 1, 1, physicsEngine);
									SettingRep rep = new SettingRep(prep, grep);
									prep.setRep(rep);
									grep.setRep(rep);
								}
							} catch(IllegalArgumentException e) {}
						}
					}
					
				}
			}
		}
	}
	
	private void generateMountains(float[][] terrain, int maxHeight) {

		float max=Integer.MIN_VALUE;
		float min=Integer.MAX_VALUE;
		
		
		for(int n=0; n<terrain.length*3; n++) {
		
			int rad=rand.nextInt(terrain.length/12);
			
			double theta = rand.nextDouble()*(Math.PI*2);
			int distance = terrain.length/5+rand.nextInt(terrain.length/2-rad-terrain.length/5);
			
			int cx = (int)(terrain.length/2 +Math.cos(theta)*distance);
			int cy = (int)(terrain.length/2 +Math.sin(theta)*distance);
			
			
			int startx = cx-rad;
			int endx=cx+rad;
			int starty=cy-rad;
			int endy=cy+rad;
			if(startx<0)
				startx=0;
			if(starty<0)
				starty=0;
			if(endx>terrain.length)
				endx=terrain.length;
			if(endy>terrain[0].length)
				endy=terrain[0].length;
			

			rad*=rad;
			
			for(int x=startx; x<endx; x++) {
				for(int y=starty; y<endy; y++) {
					int z = rad - ((x-cx)*(x-cx) + (y-cy)*(y-cy));
					if(z>0) {
						terrain[x][y]+=z;
						
						if(terrain[x][y]>max)
							max=terrain[x][y];
						if(terrain[x][y]<min)
							min=terrain[x][y];
					}
					
				}
			}
		}
		
		float maxmmin=max-min;
		for(int x=0; x<terrain.length; x++) {
			for(int y=0; y<terrain[0].length; y++) {
				terrain[x][y] = (terrain[x][y]-min)/(maxmmin);
				terrain[x][y]= terrain[x][y]*maxHeight-10;
			}
		}
		
	}
	
	
	
}
