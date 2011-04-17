/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Guillaume PERSONNETTAZ

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package gameEngine;


import physicsEngine.*;
import aiEngine.*;

import graphicsEngine.*;
import guiEngine.*;

import java.awt.*;
import java.util.*;

import main.*;

/*
 * Created on 14 sept. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author DjSteaK
 *
 * this class manage the monsters (creation and behaviour)
 */

//!!! multiplayers !!!
public class Monster extends Characters {
	public static final int ENTRY = 4;
	public static final int ENTRY_BY_NEURON = 4;
	public static final int NB_OUT = 2;
	public static final int NB_HIDDEN_LAYER = 3;
	public static final int NEURON_BY_LAYER = 4;
	public static final int ENTRY_FIRST_LAYER = 8;
	
	/** the percentage of chances a monster is a boss*/
	public static final double CHANCES_TO_BE_A_BOSS = 10;
	
	/** the base experience given by a monster (without penality and bonus)*/
	public static final int BASE_EXPERIENCE_GIVEN_BY_A_MONSTER = 100;
	
	/** the experience bonus given by a boss it's a multiplicator
	 * boss experience = base experience * this multiplier*/
	public static final double EXPERIENCE_MULTIPLIER_BOSS = 5;
	
	/** when a boss has less than this percentage of life, he calls help*/
	public static final double CALL_HELP_LIFE_LIMIT = 75;
	
	/** when a monster has less than this percentage of life, he can flee*/
	public static final double LIFE_LIMIT_TO_FLEE = 50;
	
	/** the range of a boss' call*/
	public static final int RANGE_CALL_HELP = 100;
	
	/** the range of a monster's call to help
	 *///here, 8 cases of range
	public static int range_monster_need_help = 8;
	
	/** indiactes simply if the monster is a boss or not*/
	private boolean boss;
	
	/** the monster's sight (in number of cases)*/
	private int sight;
	
	/** the monster's chances to flee a combat*/
	private double chance_to_flee;
	
	/** the number of iteration a monster flee (default : 0)*/
	private int flee;
	
	private ReseauNeural brain;
	
	private GameEngine game_engine;
	
	private HeroRep target;
	
	private int[] attack;
	
	/**
	 * this constructor creates a monster
	 * @param name name of the monster
	 * @param level level of a monster
	 */
	/*
	 * all the competences of a monster has the same level 
	 * it means melee level = ranged level = magical level
	 */
	public Monster(String name, int level, GameEngine game, boolean is_boss) {
		super(name, game);
		attack = new int[1];
		attack[0] = 0;
		target = null;
		boss = is_boss;
		flee = 0;
		this.sight = 30;
		game.addMonster(this);
		brain = new ReseauNeural(ENTRY, ENTRY_FIRST_LAYER, ENTRY_BY_NEURON, NB_OUT, NB_HIDDEN_LAYER, NEURON_BY_LAYER);
		game_engine = game;
		brainInitializer();
		
		damage_physical_min = 1;
		damage_physical_max = 2;
		//objets sur lui
		//aptitudes des obj...
	}
	
	public Monster(String name, int level, GameEngine game, boolean is_boss, int[] attack_type) {
		this(name, level, game, is_boss);
		attack = attack_type;
	}
	
	//no use for a monster
	public void experienceDistributer(double experience_won, int level_ennemy) {}
	
	protected double isAttacked(int melee, int ranged, int fire, int ice, int thunder, int level_attacker){
		int damage = damageTaken(melee, ranged, fire, ice, thunder);
		double experience;
		if (life < damage) damage = life;
		life -= damage;
		experience = (((double)damage)/life_max * BASE_EXPERIENCE_GIVEN_BY_A_MONSTER);
		if (boss) experience *= EXPERIENCE_MULTIPLIER_BOSS;
		if(life <= 0){
			drop();
			getRep().getPhysics().destroy();
			//getRep().getGraphics().startAnimation("death");
		}else{
			//if(life != life_max) System.out.println("life:"+life+"max:"+life_max+" limite :"+(life_max * LIFE_LIMIT_TO_FLEE/100)+"fuite ?"+(life < life_max * LIFE_LIMIT_TO_FLEE/100));
			if(life < life_max * LIFE_LIMIT_TO_FLEE/100 && game_engine.getMainLink().getRandom().nextInt(100) <= chance_to_flee){
				flee = game_engine.getMainLink().getRandom().nextInt(20);
			}
		}
		return experience;
	}
	
	/**
	 * @return Returns the boss.
	 */
	public boolean isBoss() {
		return boss;
	}
	
	/**
	 * @param boss The boss to set.
	 */
	public void setBoss(boolean boss) {
		this.boss = boss;
	}
	
	/**
	 * @return Returns the chance_to_flee.
	 */
	public double getChance_to_flee() {
		return chance_to_flee;
	}
	
	/**
	 * @param chance_to_flee The chance_to_flee to set.
	 */
	public void setChance_to_flee(double chance_to_flee) {
		this.chance_to_flee = chance_to_flee;
	}
	
	/**
	 * @return Returns the sight.
	 */
	public int getSight() {
		return sight;
	}
	
	/**
	 * @param sight The sight to set.
	 */
	public void setSight(int sight) {
		this.sight = sight;
	}
	
	private void brainInitializer(){
		brain.remplacerPoids(game_engine.getWeight());
	}
	
	public void think(Main main){
		
		
		if(!busy){ //if he is busy, no use to think
			if(boss && life < life_max * CALL_HELP_LIFE_LIMIT / 100){
				callHelp(main);
			}
			
			PhysicsEngine physic = main.getPhysicsEngine();
			//call the setRep before!!
			int x = this.getRep().getPhysics().cx();
			int y = this.getRep().getPhysics().cy();
			
			Set<PhysicalRep> saw = physic.whatIsHere(x - sight/2, y - sight/2, sight, sight);
			
			//---select the target
			HeroRep ennemy = null;
			if(target == null){
				for(PhysicalRep p : saw){
					if(p.getRep() instanceof HeroRep){
						ennemy = (HeroRep) p.getRep();
						break;
					}
				}
			}else{
				ennemy = target;
			}
			//----
			
			if(ennemy != null){//player is spotted
				//ask the neural network
				if(rangedAttackAllowed()
						&& !(target.getPhysics().cx() <= this.rep.getPhysics().cx() + 1
						&& target.getPhysics().cx() >= this.rep.getPhysics().cx() - 1
						&& target.getPhysics().cy() <= this.rep.getPhysics().cy() + 1
						&& target.getPhysics().cy() >= this.rep.getPhysics().cy() - 1)){
					if(Point.distance(target.getPhysics().cx(), target.getPhysics().cy(), this.rep.getPhysics().cx(), this.rep.getPhysics().cy()) <= sight){
						//TODO attack ranged
					}
							
				}
				ArrayList<Double> entry = new ArrayList<Double>();
				entry.add((double) ennemy.getPhysics().cx() - x);
				entry.add((double) ennemy.getPhysics().cy() - y);
				//trouver les 2 obstacles les = proches
				findNearestObstacle(saw, entry);
				ArrayList<Double> answer = brain.queFaire(entry);
				
				int move_x = 0, move_y = 0;
				if(answer.get(0) > 0.25) move_x = 1;
				else if(answer.get(0) < -0.25) move_x = -1;
				if(answer.get(1) > 0.25) move_y = 1;
				else if(answer.get(1) < -0.25) move_y = -1;
				//System.out.println("flee:"+flee);
				if(flee == 0){
					x += move_x;
					y += move_y;
					if(target != null && target.getPhysics().cx() <= this.rep.getPhysics().cx() + 1
							&& target.getPhysics().cx() >= this.rep.getPhysics().cx() - 1
							&& target.getPhysics().cy() <= this.rep.getPhysics().cy() + 1
							&& target.getPhysics().cy() >= this.rep.getPhysics().cy() - 1)
						target = null;
				}else{
					x -= move_x;
					y -= move_y;
					flee--;
				}
				
				main.getPhysicsEngine().addMove(this.rep.getPhysics(), x, y);
				
			}/*else{
				
			}*/
			
		}else{
			busy = false;
		}
	}
	
	private void findNearestObstacle(Set<PhysicalRep> set, ArrayList<Double> entry){
		PhysicalRep i_1 = null, i_2 = null, i_3 = null;
		int x = this.getRep().getPhysics().cx();
		int y = this.getRep().getPhysics().cy();
		
		double dist_1 = sight;
		double dist_2 = sight, dist_3 = sight;
		for(PhysicalRep p : set){
			double tmp =  Point.distance(x, y, p.cx(), p.cy());
			if(p.getRep() != this.rep && tmp < dist_3 && !(p.getRep() instanceof HeroRep)){
				if(p.getRep() != this.rep && tmp < dist_2){
					if(tmp < dist_1){
						dist_3 = dist_2;
						dist_2 = dist_1;
						i_3 = i_2;
						i_2 = i_1;
						dist_1 = tmp;
						i_1 = p;
					}else{
						dist_3 = dist_2;
						dist_2 = tmp;
						i_3 = i_2;
						i_2 = p;
					}
				}else{
					dist_3 = tmp;
					i_3 = p;
				}
			}
		}
		if(i_1 != null){
			entry.add((double) i_1.cx() - x);
			entry.add((double) i_1.cy() - y);
		}else{
			entry.add(0.);
			entry.add(0.);
		}
		if(i_2!= null){
			entry.add((double) i_2.cx() - x);
			entry.add((double) i_2.cy() - y);
		}else{
			entry.add(0.);
			entry.add(0.);
		}
		if(i_3 != null){
			entry.add((double) i_3.cx() - x);
			entry.add((double) i_3.cy() - y);
		}else{
			entry.add(0.);
			entry.add(0.);
		}
		/*int x = this.getRep().getPhysics().cx();
		int y = this.getRep().getPhysics().cy();
		
		double dist_1 = sight, dist_2 = sight;
		for(PhysicalRep p : set){
			double tmp =  Point.distance(x, y, p.cx(), p.cy());
			if(p.getRep() != this.rep && tmp < dist_2 && !(p.getRep() instanceof HeroRep)){
				if(tmp < dist_1){
					dist_2 = dist_1;
					i_2 = i_1;
					dist_1 = tmp;
					i_1 = p;
				}else{
					dist_2 = tmp;
					i_2 = p;
				}
			}
		}
		if(i_1 != null){
			entry.add((double) i_1.cx() - x);
			entry.add((double) i_1.cy() - y);
		}else{
			entry.add(0.);
			entry.add(0.);
		}
		if(i_2 != null){
			entry.add((double) i_2.cx() - x);
			entry.add((double) i_2.cy() - y);
		}else{
			entry.add(0.);
			entry.add(0.);
		}*/
		
	}
	
	private void callHelp(Main main){
		int x = this.getRep().getPhysics().cx();
		int y = this.getRep().getPhysics().cy();
		
		Set<PhysicalRep> saw = main.getPhysicsEngine().whatIsHere(x - RANGE_CALL_HELP, y - RANGE_CALL_HELP, RANGE_CALL_HELP*2, RANGE_CALL_HELP*2);
		
		HeroRep ennemy = null;
		for(PhysicalRep p : saw){
			if(p.getRep() instanceof HeroRep){
				ennemy = (HeroRep) p.getRep();
				break;
			}
		}
		
		for(PhysicalRep p : saw){
			if(p.getRep() instanceof Monster){
				((Monster)p.getRep()).setTarget(ennemy);
				break;
			}
		}
	}
	
	private void setTarget(HeroRep ennemy){
		target = ennemy;
	}
	
	private boolean rangedAttackAllowed(){
		boolean rangedAttackAllowed = false;
		for(int i = 0 ; i < attack.length && !rangedAttackAllowed; i++){
			rangedAttackAllowed = attack[i] > 0;
		}
		return rangedAttackAllowed;
	}
	
	private void drop(){
		try{
			int nombre = game.getMainLink().getRandom().nextInt(3);
			ItemCharacteristics i;
			ItemPRep p;
			MD2 m;
			GuiRep g;
			if(nombre == 0){
				i = new ItemCharacteristics(true, "Sword of fire", 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0,
						0, 0, 0, 10, 11,
						0, 0, 0, 0, "sword",
						0, "melee", 5, 10, 0, 0, 0, 0, 0);
				p = new ItemPRep(this.getRep().getPhysics().cx(), this.getRep().getPhysics().cy(), game.getMainLink().getPhysicsEngine());
				m = new MD2("data/objects/money.md2");
				g = new GuiRep(10,2);
			}else if (nombre == 1){
				i = new ItemCharacteristics(false, "Armor", 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0,
						0, 0, 0, 0, "armor",
						10, null, 0, 0, 0, 0, 0, 0, 0);
				p = new ItemPRep(this.getRep().getPhysics().cx(), this.getRep().getPhysics().cy(), game.getMainLink().getPhysicsEngine());
				m = new MD2("data/objects/money.md2");
				g = new GuiRep(12,1);
			}else {
				i = new ItemCharacteristics(false, "Potion", 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0,
						0, 0, 0, 0, "potion",
						10, null, 0, 0, 0, 0, 0, 0, 0);
				p = new ItemPRep(this.getRep().getPhysics().cx(), this.getRep().getPhysics().cy(), game.getMainLink().getPhysicsEngine());
				m = new MD2("data/objects/potion.md2");
				g = new GuiRep(15,2);
			}
			
			ItemRep lswo = new ItemRep(i, p, m, g);
			i.setRep(lswo);
			p.setRep(lswo);
			m.setRep(lswo);
			
			getRep().getPhysics().drop(p);
			}catch (Exception e){}
	}
}
