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

public class GuiStatisticsPanel extends JPanel {
	
	/* Declaration of all images */
	
	private Image statisticsBackground;
	
	/* The width and height of the main JInternalframe */
	
	private int width,height;
	
	/* The main Frame */
	
	private JInternalFrame mainWindow;
	
	/** 
	 * GuiInventoryPanel Constructor
	 * Display character option frame
	 */
	
	public GuiStatisticsPanel(int width, int height, JInternalFrame mainWindow) {
		
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
	int inventoryCount=0,infosCount=0,spellsCount=0,pauseCount=0;
	
	private void mouseClick(int x, int y)  {
		
		Graphics g=this.getGraphics();
		Rectangle inventory = new Rectangle(74,9,32,32);
		Rectangle infos = new Rectangle(119,9,32,32);
		Rectangle spells = new Rectangle(164,9,32,32);
		Rectangle pause = new Rectangle(209,9,32,32);
		JInternalFrame tabJInternalFrame[]=this.mainWindow.getDesktopPane().getAllFrames();
		boolean show = true;
		
		if (inventory.contains(new Point(x,y))) {
			for (int i=0;i<tabJInternalFrame.length;i++) {
				if (tabJInternalFrame[i] instanceof GuiInventory) {
					inventoryCount++;
					if ((inventoryCount % 2)== 0) show=false;
					tabJInternalFrame[i].setVisible(show);
					tabJInternalFrame[i].toFront();
				}
			}			
		}else if (infos.contains(new Point(x,y))) {
			for (int i=0;i<tabJInternalFrame.length;i++) {
				if (tabJInternalFrame[i] instanceof GuiInfos) {
					infosCount++;
					if ((infosCount % 2)== 0) show=false;
					tabJInternalFrame[i].setVisible(show);
					tabJInternalFrame[i].toFront();
				}
			}		
		}else if (spells.contains(new Point(x,y))) {
			/*for (int i=0;i<tabJInternalFrame.length;i++) {
				if (tabJInternalFrame[i] instanceof GuiSpells) {
					spellsCount++;
					if ((spellsCount % 2)== 0) show=false;
					tabJInternalFrame[i].setVisible(show);
					tabJInternalFrame[i].toFront();
				}
			}	*/
		}else if (pause.contains(new Point(x,y))) {

		
		}
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
		
		statisticsBackground = (new ImageIcon("characterOptions/draw/statisticsBackground.png")).getImage();
		
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		g.drawImage(statisticsBackground, 0, 0, null);
	
		
	}
}


