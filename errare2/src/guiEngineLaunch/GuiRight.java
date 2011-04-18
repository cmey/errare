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

public class GuiRight extends JPanel {
	
	private Image right;
	private JFrame mainWindow;
	
	public GuiRight(JFrame mainWindow) {
	
		this.mainWindow=mainWindow;
	right = (new ImageIcon("medias/GuiEngineLaunch/right.png")).getImage();

	this.setSize(1024,268);
	this.setBackground(Color.black);
	
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(right,0,0,null);
		
	}
	
}
