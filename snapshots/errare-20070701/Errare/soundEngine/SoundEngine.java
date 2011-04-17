/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Antoine PIERRONNET

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package soundEngine;

import java.util.ArrayList;

public class SoundEngine{
   
	private boolean isRunning;
	
	private ArrayList<Player> players;
	
	public SoundEngine(){
		players=new ArrayList<Player>();
	}
	
	public void play(String filename,boolean loop){
		isRunning=true;
		Player p=new Player(filename,this,loop);
		p.start();
		players.add(p);
	}
    
	public void stop(String filename){
		for(Player p : players){
			if(p.getName().compareTo(filename)==0) p.stopPlay();
		}
	}
	
	public void setVolume(double volume){
		if(isRunning){
		for (Player p : players){
			p.setVolume(volume);
		}
		}
	}
	
	public void mute(){
		if(isRunning){
		for (Player p : players){
			p.mute();
		}
		}
	}
	
	public void unMute(){
		if(isRunning){
		for (Player p : players){
			p.unMute();
		}
		}
	}

	public void stopAll(){
		if(isRunning){
		synchronized(players){
		for(int i=0;i<players.size();i++){
			players.get(i).stopPlay();
		}
		players.get(0).stopPlay();
		}
		}
	}
	
	protected void removePlayer(Player player) {
		players.remove(player);
	}
	
	public static void main(String[] args) throws InterruptedException{
		SoundEngine so=new SoundEngine();
		so.play("medias/Sounds/shortSounds/deaths.ogg",true);
		//so.play("medias/Sounds/Musique.ogg",true);
		//so.play("medias/Sounds/gui.ogg");
		//so.play("medias/Sounds/Musique2.ogg");
		/*Thread.sleep(2000);
		so.setVolume(.9D);
		Thread.sleep(2000);
		so.setVolume(.1D);
		Thread.sleep(2000);
		so.mute();
		Thread.sleep(2000);
		so.setVolume(.9D);
		so.unMute();
		Thread.sleep(2000);
		so.stopAll();*/
	}
	
}

