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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GuiCenterUp extends JPanel {
	
	private Image centerup;
	private JFrame mainWindow;
	
	public GuiCenterUp(JFrame mainWindow) {
	
		this.mainWindow=mainWindow;
	centerup = (new ImageIcon("medias/GuiEngineLaunch/centerup.png")).getImage();

	this.setSize(524,24);
	this.setBackground(Color.black);
	
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(centerup,0,0,null);
		
	}
	
}
