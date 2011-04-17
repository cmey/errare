package guiEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class GuiMenu extends JPanel {
	
	private Image menu,button1;
	
	
	public GuiMenu() {
	
	menu = (new ImageIcon("guiEngine/menu.png")).getImage();
	button1 = (new ImageIcon("guiEngine/button.png")).getImage();
	this.setSize(1024,468);
			this.setBackground(Color.black);

	
	}
	


	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(menu,0,0,null);
		g.drawImage(button1,500,100,null);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
