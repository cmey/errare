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

import graphicsEngine.Skybox;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import main.ClientMain;
import xmlEngine.XmlEngine;

public class GuiEngineLaunch extends JFrame {
	
	private ClientMain main;
	private Container container;
	private final Image cursor = (new ImageIcon("medias/cursor.png")).getImage();;
	private XmlEngine dataBase;
	
	public GuiEngineLaunch (GuiSplashScreen gsc, ClientMain main) {
		
		try {
			dataBase = new XmlEngine();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		//this.main=main;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
        gsc.setLocation((dimScreen.width - gsc.getSize().width)/2,(dimScreen.height - gsc.getSize().height)/2); 
        gsc.setVisible(true); 
		this.setLayout(null);
		this.setBackground(Color.black);
		
		container = this.getContentPane();

		GuiTop top = new GuiTop(this);
		top.setLocation(0,0);
		container.add(top);
		
	Skybox sb=new Skybox(true);
	
	GLCapabilities capabilities = new GLCapabilities();
	capabilities.setHardwareAccelerated(true);      //We want hardware acceleration
	capabilities.setDoubleBuffered(true);           //And double buffering
	GLCanvas canvas = new GLCanvas(capabilities);
	canvas.addGLEventListener(sb);
	
	canvas.setSize(1024,150);//We want the JPanel and the GLCanvas to have the same size
	sb.setGLCanvas(canvas);
	canvas.setVisible(true);//This is somehow necessary
		container.add(canvas);
		canvas.setLocation(0,150);

		GuiLeft left = new GuiLeft(this);
		left.setLocation(0,300);
		container.add(left);
		
		GuiCenterUp centerup = new GuiCenterUp(this);
		centerup.setLocation(250,300);
		container.add(centerup);
		
		GuiCenter center = new GuiCenter(this);
		center.setLocation(250,324);
		container.add(center);
			
		GuiRight right = new GuiRight(this);
		right.setLocation(774,300);
		container.add(right);
		
		GuiLow low = new GuiLow(this,this);
		low.setLocation(0,568);
		container.add(low);
		
		
		gsc.setVisible(false); 

		
		
		
		try {
		    /*Look and feel Windows : "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
			 Look and feel CDE/Motif : "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
			 Look and feel GTK+ : "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
			 Look and feel m�tal : "javax.swing.plaf.metal.MetalLookAndFeel");
			 Look and feel Macintosh : "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"
			 look and feel Kunststoff : "com.incors.plaf.kunststoff.KunststoffLookAndFeel ")*/
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		

	
		GuiScreen sm = new GuiScreen();
		DisplayMode dm = new DisplayMode(1024,768,32,DisplayMode.REFRESH_RATE_UNKNOWN);
		sm.setFullScreen(this,dm);

	this.setVisible(true);

	Thread sbt = new Thread(sb);
	sbt.start();
//this.setCursor(getCursor(cursor,new Point(0,0)));

	
	}

	public void deleteCenter() {
		remove(getContentPane().getComponentAt(new Point(300,350)));

	}
	
    public Cursor getCursor( Image im, Point spot ) {
        return Toolkit.getDefaultToolkit().createCustomCursor( im, spot, "magicCursor" );
    }
 
    public void getImages() {
        try {
            MediaTracker mTrack = new MediaTracker( this ); // load images before display it
            mTrack.addImage(cursor, 0 );
            mTrack.waitForAll();
         } catch (Exception e) { 
         }
    }
	
	


		
	
	
	public static void main (String args[]) {
		/*try {
			XmlEngine t = new XmlEngine();
			t.set("options.difficulty", 2);
			 t.set("options.gamespeed", 2);
			 t.set("options.mousespeed", 50);
			 t.set("options.loudness", 50);
			 t.set("options.screenwidth", 1024);
			 t.set("options.screenheight", 768);
			 t.set("options.bitcolor", 1);
			 t.set("options.frequency", 70);
			 t.set("options.gamma", 3);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}*/
		new GuiEngineLaunch(new GuiSplashScreen(), null);

	}

	public XmlEngine getDataBase() {
		return dataBase;
	}
}
