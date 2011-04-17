package guiEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class GuiTop extends JPanel {
	
	private Image top;
	
	public GuiTop() {
	
	top = (new ImageIcon("guiEngine/top.png")).getImage();
	this.setSize(1024,150);
	this.setBackground(Color.black);
	
	}
	


	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(top,0,0,null);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
