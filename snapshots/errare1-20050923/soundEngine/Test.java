package soundEngine;

import java.net.MalformedURLException;

public class Test {

		public static void main(String[] args){
			try {
			
				Sound2 snd=new Sound2("SoundEngine/chord.wav");
				snd.playSound();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
}
