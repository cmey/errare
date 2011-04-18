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
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import main.ResourceLocator;

public class Player extends Thread{
	
	private AudioInputStream in;
	
	private AudioFormat decodedFormat;
	
	private AudioInputStream din;
	
	private SourceDataLine line;
	
	private FloatControl gainControl;
	
	private BooleanControl muteControl;
	
	private SoundEngine s;
	
	private boolean loop; 
	
	public Player(String filename, SoundEngine s, boolean loop)
	{
		super(filename);
		this.loop=loop;
		this.s=s;
		getFormat();
	}
	
	private void getFormat(){
		try
		{
			// Get AudioInputStream from given file.
			in = AudioSystem.getAudioInputStream(ResourceLocator.getRessourceAsStream(getName()));
			din = null;
			if (in != null)
			{
				AudioFormat baseFormat = in.getFormat();
				if(getName().endsWith(".wav")){
					decodedFormat= new AudioFormat(
							AudioFormat.Encoding.PCM_SIGNED,
							baseFormat.getSampleRate(),
							baseFormat.getSampleSizeInBits(),
							baseFormat.getChannels(),
							baseFormat.getFrameSize(),
							baseFormat.getSampleRate(),
							false);	
				}else{
					decodedFormat= new AudioFormat(
							AudioFormat.Encoding.PCM_SIGNED,
							baseFormat.getSampleRate(),
							16,
							baseFormat.getChannels(),
							baseFormat.getChannels() * 2,
							baseFormat.getSampleRate(),
							false);
				}
				// Get AudioInputStream that will be decoded by underlying VorbisSPI
				din = AudioSystem.getAudioInputStream(decodedFormat, in); 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
    @Override
	public void run(){
		try {
			if(!loop) {
                            rawplay(decodedFormat, din);
                        } else {
                            while(true) {
                            	getFormat();
                                rawplay(decodedFormat, din);
                            }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void rawplay(AudioFormat targetFormat, 
			AudioInputStream din) throws IOException, LineUnavailableException
			{
		byte[] data = new byte[4096];
		line = getLine(targetFormat);		
		if (line != null)
		{
			gainControl= (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
			muteControl = (BooleanControl)line.getControl(BooleanControl.Type.MUTE);
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1)
			{
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
			}
			line.drain();
			line.stop();
			if(!loop){
				line.close();
				din.close();
				in.close();
				s.removePlayer(this);
			}
		}		
	}
	
	public synchronized void stopPlay() {
		try {
			line.drain();
			line.stop();
			line.close();
			din.close();
			in.close();
			s.removePlayer(this);
		} catch (IOException e) {
		}
	}
	
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
		SourceDataLine res = null;
		javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}
	
//	Set Volume
	public void setVolume(double gain){// number between 0 and 1 (loudest)
		float dB = (float)(Math.log(gain)/Math.log(10.0)*20.0);
		gainControl.setValue(dB);
	}
	
	// Mute On
	public void mute(){
		muteControl.setValue(true);
	}
	
	public void unMute(){
		// Mute Off
		muteControl.setValue(false);
	}
	
	
	
}
