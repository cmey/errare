package guiEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Infos extends JPanel {
	
	private Rectangle infoSite, nameSite;
	
	private Ellipse2D.Double portraitSite,manaSite,lifeSite;
	private double manaPourcent, lifePourcent;
	
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
