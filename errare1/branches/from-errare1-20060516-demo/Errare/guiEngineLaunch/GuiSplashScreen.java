/*Errare is a free and crossplatform MMORPG project.
 Copyright (C) 2006  Arnaud KNOBLOCH
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.*/

package guiEngineLaunch;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

/** 
 * @author ak
 * class GuiSplashScreen
 */ 


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
		String fileloc = "medias/GuiEngineLaunch/splashScreen.png"; 
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

