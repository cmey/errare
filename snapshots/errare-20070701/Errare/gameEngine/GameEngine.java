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

import java.io.*;
import java.util.ArrayList;
import aiEngine.*;
import main.Main;

/**
 * Game Engine manages the combats between two characters
 * @author DjSteaK
 *
 */
public class GameEngine {
	
	/** Link to the main instance*/
	private Main main_link;
	
	private ArrayList<Monster> monster_array;
	
	private Hero hero;
	
	private ArrayList<Double> genome;
	
	
	
	/**
	 * Constructor which creates a Game engine object (initializes the duel_list)
	 */
	public GameEngine(Main m){
		main_link = m;
		hero = null;
		monster_array = new ArrayList<Monster>();
		loadAI();
	}
	
	public void loadAI(){
		genome = new ArrayList<Double>();
		//TODO initialiser genome
		
		/*genome.add(3.67627558223076);
		genome.add(-4.910214332307243);
		genome.add(0.14622462762243735);
		genome.add(-0.031363739511103966);
		genome.add(2.1391091039186967);
		genome.add(3.2016239509605584);
		genome.add(2.872674618740496);
		genome.add(-1.6889889710831156);
		genome.add(3.370182248493975);
		genome.add(0.7010110829898486);
		genome.add(-7.230560141537207);
		genome.add(-6.438696822450503);
		genome.add(0.5140758777842086);
		genome.add(0.5885755465591402);
		genome.add(1.33466830762634);
		genome.add(-2.8357534034886807);
		genome.add(2.717053156618415);
		genome.add(1.589171309692442);
		genome.add(4.54987966055754);
		genome.add(6.832216150264689);
		genome.add(-1.5938258761280228);
		genome.add(-2.552506858927761);
		genome.add(-0.8683571283968652);
		genome.add(-0.6166544725402674);
		genome.add(-2.1549138556225143);
		genome.add(0.20217625780629153);
		genome.add(-6.725659435322454);
		genome.add(-9.579347334138934);
		genome.add(1.2650459450741978);
		genome.add(1.4946844990276862);
		genome.add(-0.6580378444064104);
		genome.add(0.47046900854801643);
		genome.add(-0.7184134340320798);
		genome.add(-0.4686659954479499);
		genome.add(-3.2586518534386455);
		genome.add(-7.894721022213036);
		genome.add(1.5626932953965484);
		genome.add(2.4485196503544664);
		genome.add(-3.6136935536913772);
		genome.add(-0.3293247631173938);
		genome.add(0.5615901037763678);
		genome.add(4.208720412202737);
		genome.add(-4.472292542321828);
		genome.add(1.911803806223376);
		genome.add(-4.822847357851203);
		genome.add(-2.025563736419513);
		genome.add(3.0857058430521875);
		genome.add(-2.0911505870990394);
		genome.add(-1.5572871114151086);
		genome.add(2.195057633804886);
		genome.add(-1.5456796631113714);
		genome.add(-6.499462913258821);
		genome.add(0.161104611745537);
		genome.add(1.6486713409727392);
		genome.add(-3.210751100210139);
		genome.add(1.5849542687092895);
		genome.add(3.0652227470075784);
		genome.add(-0.7100821992826541);
		genome.add(-2.1925612001564216);
		genome.add(-3.1380489962380453);
		genome.add(1.4982817176370096);
		genome.add(-4.040494389023022);
		genome.add(-4.755718051168524);
		genome.add(0.0729352985703462);
		genome.add(6.035749069344719);
		genome.add(-3.3040944963982937);
		genome.add(3.4394869130911174);
		genome.add(-6.027557410607643);
		genome.add(3.178197560524425);
		genome.add(1.2223733345171706);
		genome.add(-3.3580267356370173);
		genome.add(6.438109140106966);
		genome.add(-1.2253774995483142);
		genome.add(-2.480663123931427);
		genome.add(-4.227622219293804);
		genome.add(0.32406513629550404);
		genome.add(-2.542197967970196);
		genome.add(-3.603535541700743);
		genome.add(-10.716904561045164);
		genome.add(0.6719933063412378);
		genome.add(-2.3065623392250387);
		genome.add(-1.7973242807333378);
		genome.add(3.315517561380998);
		genome.add(-6.4686308137876);
		genome.add(-3.7517270584457894);
		genome.add(0.72567204004981);*/
		genome.add(3.8762990236266046);
		genome.add(-3.6683597412760687);
		genome.add(-0.9351278972413096);
		genome.add(0.24568443629857006);
		genome.add(0.331674667981225);
		genome.add(1.3099021187879842);
		genome.add(-0.2189006901079359);
		genome.add(-0.9446291455012483);
		genome.add(3.231978937400655);
		genome.add(1.4029919933950525);
		genome.add(-9.389169508832659);
		genome.add(-5.837826526996793);
		genome.add(-0.24305874032167235);
		genome.add(-0.5762350978761739);
		genome.add(3.0357792349116077);
		genome.add(-0.5718123326105853);
		genome.add(1.2263653299827477);
		genome.add(3.5200555540846366);
		genome.add(6.522365900767666);
		genome.add(7.774563163788345);
		genome.add(-2.210550036881033);
		genome.add(-1.4534672494502514);
		genome.add(-1.7084248167479013);
		genome.add(-0.19860120692766642);
		genome.add(1.378294038499419);
		genome.add(-0.04355428911264103);
		genome.add(-5.438696952825862);
		genome.add(-9.334041373086691);
		genome.add(0.5367462355480461);
		genome.add(2.2630472945945495);
		genome.add(0.16141999545754354);
		genome.add(-1.9899727370523825);
		genome.add(-2.3168801801142824);
		genome.add(-0.752611127638064);
		genome.add(-0.6889897467896752);
		genome.add(-9.277511938614296);
		genome.add(1.9901943418819592);
		genome.add(3.073257208889967);
		genome.add(-4.2459871142702275);
		genome.add(-1.7664044923394788);
		genome.add(1.0854070493687134);
		genome.add(5.387165321668446);
		genome.add(-5.098955227868543);
		genome.add(2.618561328497936);
		genome.add(-6.131331129736518);
		genome.add(-3.587930703663317);
		genome.add(2.5226414217034945);
		genome.add(-1.459979999034075);
		genome.add(1.3985776050429388);
		genome.add(6.023012363835834);
		genome.add(-0.7562242384017813);
		genome.add(-6.991441004838686);
		genome.add(-0.5154842693738207);
		genome.add(2.273557076062545);
		genome.add(-4.483016303570645);
		genome.add(2.360827284862588);
		genome.add(2.797898392226858);
		genome.add(-2.033909266826359);
		genome.add(-2.395035100468901);
		genome.add(-4.475666858365502);
		genome.add(2.2114175505554186);
		genome.add(-4.697693111316491);
		genome.add(-2.6112969517231877);
		genome.add(1.1013205635737393);
		genome.add(3.5184856679903804);
		genome.add(-2.0959969623216788);
		genome.add(4.7636155457971805);
		genome.add(-8.191389266524057);
		genome.add(4.80856128215294);
		genome.add(1.0668759340904248);
		genome.add(-3.7552414302804245);
		genome.add(5.906137012496192);
		genome.add(-3.942403801252117);
		genome.add(-2.2334485041947665);
		genome.add(-6.05523951876889);
		genome.add(1.8973105900573022);
		genome.add(-4.71846108271389);
		genome.add(-3.6970249320113147);
		genome.add(-11.690264611553385);
		genome.add(-1.0213361464188684);
		genome.add(-1.418059680526788);
		genome.add(-1.0762419058738535);
		genome.add(4.783439898340694);
		genome.add(-4.951529436273152);
		genome.add(-7.288081841383688);
		genome.add(0.2080127829945007);
	}
	
	public void addMonster(Monster m){
		monster_array.add(m);
	}
	
	public void setMainChar(Hero h){
		hero = h;
	}
	
	public void executeCombat(Characters attacker, Characters defender){
		//TODO a revoir : et si il y a une attaque � distance ??
		attacker.attack(defender);
		defender.attack(attacker);
		
		if(attacker.isDead() && attacker instanceof Monster)
			monster_array.remove(attacker);	
		else{
			attacker.setBusy(true);
			attacker.getRep().getGraphics().startAnimation("attack");
		}
		if(defender.isDead() && defender instanceof Monster)
			monster_array.remove(defender);
		else{
			defender.setBusy(true);
			defender.getRep().getGraphics().startAnimation("attack");
		}
		
		
	}
	
	/**
	 * Method called by the Main class to
	 * periodically run this engine
	 */
	public void run(){
		for(int i = 0 ; i < monster_array.size() ; i++){
			monster_array.get(i).think(main_link);
		}
	}
	
	public Hero getHero(){
		return hero;
	}
	
	public ArrayList<Double> getWeight(){
		return genome;
	}
	
	public Main getMainLink(){
		return main_link;
	}
	
}
