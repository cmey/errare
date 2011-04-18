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

package guiEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Infos extends JPanel {
	
	private Rectangle infoSite;
	private Rectangle nameSite;
	
	private Ellipse2D.Double portraitSite;
	private Ellipse2D.Double manaSite;
	private Ellipse2D.Double lifeSite;
	private double manaPourcent;
	private double lifePourcent;
	
	public boolean visible;
	
	public Infos() {
		
		infoSite = new Rectangle(0,0,0,0);
		portraitSite = new Ellipse2D.Double(0, 0, 10, 10);
		manaSite = new Ellipse2D.Double(0, 0, 10, 10);
		lifeSite = new Ellipse2D.Double(100, 100, 90, 90);
		visible=false;
	
	}
	
	public void updateMana(double manaValue, double manaMax) {
		
		manaPourcent=(manaValue/manaMax)*100;
	}
	
	public void updateLife(double lifeValue, double lifeMax) {
		
		lifePourcent=(lifeValue/lifeMax)*100;
	}

	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		g.drawOval((int)lifeSite.x,(int)lifeSite.y,(int)lifeSite.width,(int)lifeSite.height);
	
	}
	
	public static void main (String args[]) {
		
		JFrame fen = new JFrame("test");
		fen.setContentPane(new Infos());
		fen.setBounds(0,0,500,300);
		fen.setVisible(true);
		fen.repaint();
	}
}
