package soundEngine;

import java.io.FileNotFoundException;
import java.util.Hashtable;

/**
 * 
 * @author Troubleshooting
 * This class manages sound players
 */
public class SoundEngine {
	
	/**
	 * The list of players
	 */
	private Hashtable listofplayers;
	
	/**
	 * Builds an new instance of this class
	 *
	 */
	public SoundEngine(){
		listofplayers=new Hashtable();
	}
	
	/**
	 * Plays the provided file
	 * @param path the path of the ogg file
	 */
	public void playSound(String path){
		JOrbisPlayer player=new JOrbisPlayer(); 
		player.addPath(path);
		listofplayers.put(path,player);
		player.play_sound(path);
	}
	
	/**
	 * Stop playing the provided file
	 * @param path the path of the ogg file
	 * @throws FileNotFoundException
	 */
	public void stopPlay(String path) throws FileNotFoundException{
		if(!listofplayers.contains(path)){
			throw new FileNotFoundException();
		}else{
			((JOrbisPlayer)(listofplayers.get(path))).stop_sound();
		} 
	}
	
	public static void main (String[] args){
		SoundEngine se=new SoundEngine();
		se.playSound("soundEngine/Musique.ogg");
	}
}
