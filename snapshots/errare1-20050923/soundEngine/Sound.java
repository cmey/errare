package soundEngine;


import javax.sound.sampled.spi.AudioFileWriter;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

/**
 * 
 * @author Troubleshooting
 *
 */
public class Sound {
    private AudioFormat moAudioFormat = null;
    private Mixer.Info moMixerInfo = null;
    private static int BUFFER_SIZE = 40960;
    private AudioFileFormat moAudioFileFormat = null;
    private DataLine.Info moInfo = null;
    private File moFile = null;
    
   
    public Sound(String soundFile) {
        this(new File(soundFile));
    }
    public Sound(File poFile) {
        moFile = poFile;
        try {
            moAudioFileFormat = AudioSystem.getAudioFileFormat(moFile);
            moAudioFormat = moAudioFileFormat.getFormat();
            Mixer oMixer = AudioSystem.getMixer(moMixerInfo);
            moInfo = new DataLine.Info(SourceDataLine.class, moAudioFormat, BUFFER_SIZE);
            if (!AudioSystem.isLineSupported(moInfo)) {
                throw new SoundException("* A line for file "+moFile.getAbsoluteFile()+" could not be found");
            }
        } catch(IllegalArgumentException iae) {
            throw new SoundException("* Illegal Argument");
        } catch(FileNotFoundException fe) {
            throw new SoundException("* File not found");
        } catch(UnsupportedAudioFileException e) {
            throw new SoundException("* Audio file "+moFile.getAbsoluteFile()+" has not a supported AudioFormat");
        } catch(Throwable e) {
            throw new SoundException(e);
        }
    }
    public void playWait() {
        doPlay(false);
    }
    public void play() {
        new Thread(new Runnable() {
            public void run() {
                doPlay(false);
            }
        }).start();
    }
    public void playOnce() {
        new Thread(new Runnable() {
            public void run() {
                doPlay(true);
            }
        }).start();
    }
    private void doPlay(boolean pbClose) {
        try {
            SourceDataLine oSourceDataLine = (SourceDataLine)AudioSystem.getLine(moInfo);
            AudioInputStream oAudioInputStream = AudioSystem.getAudioInputStream(moFile);
            oSourceDataLine.open(moAudioFormat, BUFFER_SIZE);
            oSourceDataLine.start();
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while((read = oAudioInputStream.read(buffer)) != -1) {
                oSourceDataLine.write(buffer, 0, read);
            }
            oSourceDataLine.drain();
            if (pbClose) {
                oSourceDataLine.stop();
                oSourceDataLine.close();
            }
        } catch(IllegalArgumentException iae) {
            throw new SoundException("* Illegal Argument");
        } catch(Throwable e) {
            throw new SoundException(e);
        }
    }
    
 
    public static void main(String[] args) {
        Sound sound = new Sound("SoundEngine/test.wav");
        sound.play();
    }
    
    
}
