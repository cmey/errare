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

import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * class GuiScreen
 * @author Ak
 * @version 0.1
 */

public class GuiScreen {
	
//	Graphics Card
	private GraphicsDevice gd;
	
	/**
	 * GuiScreen Constructor 
	 * Initialise GraphicsDevice
	 */
	
	public GuiScreen() {
		
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
	}
	
	/**
	 * setFullScreen fonction
	 * @param window the window that you want change the resolution
	 * @param displayMode the graphic mode
	 */
	
	public void setFullScreen(JFrame window, DisplayMode displayMode) {
		if (window == null) return;
		window.setUndecorated(true);
		window.setResizable(false);
		// FullScreen
		if (gd.isFullScreenSupported()) gd.setFullScreenWindow(window);
		// Display Mode
		if ((displayMode != null) && (gd.isDisplayChangeSupported())) {
			try {
				gd.setDisplayMode(displayMode);
			} catch (Exception e) {
				gd.setDisplayMode(new DisplayMode(1024,768,DisplayMode.BIT_DEPTH_MULTI,DisplayMode.REFRESH_RATE_UNKNOWN));
			}
		}else {
			
				System.out.println("You have linux so you can't play :p\nLinux own you !");
			
			
		}
	}
	
	/**
	 * getFullScreen fonction
	 * @return the window (JFrame)
	 */
	
	public JFrame getFullScreen() {
		return (JFrame) gd.getFullScreenWindow();
	}
	
	/**
	 * getGraphicsDevice
	 * @return the graphic card
	 */
	
	public GraphicsDevice getGraphicsDevice(){
		return gd;
	}
	
	/**
	 * restoreFullScreen fonction
	 * Stop the fullscreen mode
	 */
	
	public void restoreFullScreen() {
		JFrame window = getFullScreen();
		if (window != null) window.dispose();
		gd.setFullScreenWindow(null);
	}
	
	public static void main(String[] args) {
		
		JFrame fen = new JFrame();
		fen.setSize(800,400);
		fen.setLayout(new FlowLayout());
		fen.add(new JButton("tt"));
		fen.add(new JButton("aaaa"));
		
		GuiScreen sm = new GuiScreen();
		DisplayMode dm = new DisplayMode(1024,768,16,DisplayMode.REFRESH_RATE_UNKNOWN);
		sm.setFullScreen(fen,dm);
		
	}
}