package guiEngine;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;

/** 
 * @author ak
 * class GuiSplashScreen
 */ 

@SuppressWarnings("serial")

public class GuiSplashScreen extends Frame 
{ 
	Image[] img; 
	
	public GuiSplashScreen() 
	{ 
		super(); 
		setSize(461,243); 
		setUndecorated( true ); 
		setFocusable( false ); 
		setEnabled( false ); 
		String fileloc = "./guiEngine/splashScreen.png"; 
		img = new Image[1]; 
		img[0] = this.getToolkit().createImage( fileloc ); 
		try { 
			MediaTracker mTrack = new MediaTracker( this ); // load pictures 
			for ( int i = 0; i < img.length; i++ ) {
				mTrack.addImage( img[ i ], i ); 
			}
			mTrack.waitForAll(); 
		} catch( Exception e ) { 
			e.printStackTrace();
			} 
	} 
	
	public void paint( Graphics g ) 
	{ 
		super.paint( g ); 
		Dimension d = this.getSize(); 
		g.drawImage( img[0], 0, 0, d.width, d.height, this );
	} 
} 

