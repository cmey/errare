package guiEngine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.Spring;

/**
 * class GuiInventoryPanel
 * @author Ak
 * @version 0.1
 */

@SuppressWarnings("serial")

public class GuiBarPanel extends JPanel {
	
	/* Declaration of all images */
	
	private Image barBackground;
	
	/* The width and height of the main JInternalframe */
	
	private int width,height;
	
	/* The main Frame */
	
	private JInternalFrame mainWindow;
	
	/** 
	 * GuiInventoryPanel Constructor
	 * Display character option frame
	 */
	
	public GuiBarPanel(int width, int height, JInternalFrame mainWindow) {
		
		this.mainWindow=mainWindow;
		this.width=width;
		this.height=height;
		drawInitialisation();
		this.setPreferredSize(new Dimension(this.width,this.height));
		
		/**
		 * Aonymous class
		 * Action MouseListener 
		 */
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				mouseClick(e.getX(), e.getY());	
			}
		});
		
		/** 
		 * Anonymous class
		 * Action MotionListener 
		 * */
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseMove(e.getX(), e.getY());
			}
		});
	}
	
	/**
	 * mouseClick Fonction
	 * @param x x cursor coordonate (int)
	 * @param y y cursor coordonate (int)
	 */
	
	private void mouseClick(int x, int y)  {
		
		Graphics g=this.getGraphics();

	}


	
	/**
	 * mouseMoveFonction
	 * @param x x cursor coordonate (int)
	 * @param y y cursor coordonate (int)
	 */
	

	private void mouseMove(int x, int y) {
	
		Graphics g=this.getGraphics();
		
		
	}
	
	 
	
	/**
	 * Methode drawInitialisation
	 * Load pictures
	 */
	
	private void drawInitialisation() {
		
		barBackground = (new ImageIcon("characterOptions/draw/barBackground.png")).getImage();
		
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		g.drawImage(barBackground, 0, 0, null);
	
		
	}
}


