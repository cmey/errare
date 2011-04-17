package soundEngine;

/**
 * @author Troubleshooting
 */

import java.applet.Applet ;
import java.applet.AudioClip ;
import java.io.File;
import java.net.URL ;
import java.net.MalformedURLException ;

public class Sound2
    {
    private URL file ;
    private AudioClip sound ;

    public Sound2(String name) throws MalformedURLException
        {
        try{
        	File fnd=new File(name);
            file =fnd.toURL() ;
            sound = Applet.newAudioClip(file) ;
          }
          catch (MalformedURLException e)
          {
          System.err.println("Unable to select targeted file") ;
          }
        }

    public URL getFile()
        {return file ;}

    public AudioClip getSound()
        {return sound ;}
        
    public void setFile(String name)
        {
        try
          {
          file = new URL(name) ;
          }
        catch (MalformedURLException e)
          {
          System.err.println("Unable to select targeted sound") ;
          }
        }

    public void setSound(URL file)
        {sound = Applet.newAudioClip(file) ;}

    public void playSound()
        {sound.play() ;}

    public void loopSound()
        {sound.loop() ;}
        
    public void stopSound()
       {sound.stop() ;}
    }