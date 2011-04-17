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

public class GuiInventoryPanel extends JPanel {
	
	/* Declaration of all images */
	
	private Image inventoryBackground,itemCaract,
	allPotion, healthPotion,manaPotion,
	smallArc,largeArc,
	smallAxe,largeAxe,
	smallStick,largeStick,
	smallSword,largeSword,
	smallGlove,mediumGlove,largeGlove,
	smallBoot,mediumBoot,largeBoot,
	dress,smallArmor,mediumArmor,largeArmor,
	smallHelm,mediumHelm,largeHelm,
	smallShield,mediumShield,largeShield,
	smallBelt,mediumBelt,largeBelt,
	ring1,ring2,ring3,
	amulet1,amulet2,amulet3;
	
	/* The width and height of the main JInternalframe */
	
	private int width,height;
	
	/* The main Frame */
	
	private JInternalFrame mainWindow;
	
	/** 
	 * GuiInventoryPanel Constructor
	 * Display character option frame
	 */
	
	public GuiInventoryPanel(int width, int height, JInternalFrame mainWindow) {
		
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
		Rectangle inventory = new Rectangle(7,254,241,101);
		//g.drawRect(7,254,241,101);
		GuiRep[][] tab  = GuiInventory.getInventory();
			
		if (inventory.contains(new Point(x,y))) {
			for (int i=0;i<tab.length;i++) {
				for (int j=0;j<tab[0].length;j++) {
					if ((tab[i][j] != null)&&(tab[i][j].getType()!=0)) {
						Rectangle r= new Rectangle(7+(j*20),254+(i*20),tab[i][j].getWidthPixel(),tab[i][j].getHeightPixel());	
						if (r.contains(new Point(x,y))) {
							System.out.println(tab[i][j].getType());
			
						}
					}
				}
			}
		}
	}


	/**
	 * Private class Delay extends Thread
	 * @author Ak
	 *
	 */
	private class Delay extends Thread {
        long delay;
        Graphics g;
        int x,y;
        Delay(long delay,Graphics g,int x,int y) {
            this.delay = delay;
            this.g=g;
            this.x=x;
            this.y=y;
        }

        public void run() {

        	try {
				sleep(delay);
				if(mX==x && mY==y) { //DEBUG : verfie que les coordonees de la souris sont toujours au meme endroit
					g.drawImage(itemCaract,x,y,null);
					g.drawString("A Recup",x+10,y+10);
					sleep(delay*5);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

        }
    }
	
	/**
	 * mouseMoveFonction
	 * @param x x cursor coordonate (int)
	 * @param y y cursor coordonate (int)
	 */
	
	private int mX; 
	private int mY;
	private void mouseMove(int x, int y) {
		mX=x; 
		mY=y;
		
		Graphics g=this.getGraphics();
		Rectangle inventory = new Rectangle(7,254,241,101);
		GuiRep[][] tab  = GuiInventory.getInventory();
		if (inventory.contains(new Point(x,y))) {
			for (int i=0;i<tab.length;i++) {
				for (int j=0;j<tab[0].length;j++) {
				if ((tab[i][j] != null)&&(tab[i][j].getType()!=0)) {
						Rectangle r= new Rectangle(7+(j*20),254+(i*20),tab[i][j].getWidthPixel(),tab[i][j].getHeightPixel());	
						repaint();	
						if (r.contains(new Point(x,y))) {
							 Delay d = new Delay(2000,g,x,y);
						     d.start();

						}
					}
				}
			}
		}
		
	}
	
	 
	
	/**
	 * Methode drawInitialisation
	 * Load pictures
	 */
	
	private void drawInitialisation() {
		
		inventoryBackground = (new ImageIcon("characterOptions/draw/inventoryBackground.png")).getImage();
		itemCaract = (new ImageIcon("characterOptions/draw/itemCaract.png")).getImage();
		healthPotion = (new ImageIcon("characterOptions/draw/healthPotion.png")).getImage();
		manaPotion = (new ImageIcon("characterOptions/draw/manaPotion.png")).getImage();
		allPotion = (new ImageIcon("characterOptions/draw/allPotion.png")).getImage();
		smallArc= (new ImageIcon("characterOptions/draw/smallArc.png")).getImage();
		largeArc = (new ImageIcon("characterOptions/draw/largeArc.png")).getImage();
		smallAxe = (new ImageIcon("characterOptions/draw/smallAxe.png")).getImage();
		largeAxe = (new ImageIcon("characterOptions/draw/largeAxe.png")).getImage();
		smallStick = (new ImageIcon("characterOptions/draw/smallStick.png")).getImage();
		largeStick = (new ImageIcon("characterOptions/draw/largeStick.png")).getImage();
		smallSword = (new ImageIcon("characterOptions/draw/smallSword.png")).getImage();
		largeSword = (new ImageIcon("characterOptions/draw/largeSword.png")).getImage();
		smallGlove = (new ImageIcon("characterOptions/draw/smallGlove.png")).getImage();
		mediumGlove = (new ImageIcon("characterOptions/draw/mediumGlove.png")).getImage();
		largeGlove = (new ImageIcon("characterOptions/draw/largeGlove.png")).getImage();
		smallBoot = (new ImageIcon("characterOptions/draw/smallBoot.png")).getImage();
		mediumBoot = (new ImageIcon("characterOptions/draw/mediumBoot.png")).getImage();
		largeBoot = (new ImageIcon("characterOptions/draw/largeBoot.png")).getImage();
		dress = (new ImageIcon("characterOptions/draw/dress.png")).getImage();
		smallArmor = (new ImageIcon("characterOptions/draw/smallArmor.png")).getImage();
		mediumArmor = (new ImageIcon("characterOptions/draw/mediumArmor.png")).getImage();
		largeArmor = (new ImageIcon("characterOptions/draw/largeArmor.png")).getImage();
		smallHelm = (new ImageIcon("characterOptions/draw/smallHelm.png")).getImage();
		mediumHelm = (new ImageIcon("characterOptions/draw/mediumHelm.png")).getImage();
		largeHelm= (new ImageIcon("characterOptions/draw/largeHelm.png")).getImage();
		smallShield = (new ImageIcon("characterOptions/draw/smallShield.png")).getImage();
		mediumShield = (new ImageIcon("characterOptions/draw/mediumShield.png")).getImage();
		largeShield = (new ImageIcon("characterOptions/draw/largeShield.png")).getImage();
		smallBelt = (new ImageIcon("characterOptions/draw/smallBelt.png")).getImage();
		mediumBelt = (new ImageIcon("characterOptions/draw/mediumBelt.png")).getImage();
		largeBelt = (new ImageIcon("characterOptions/draw/largeBelt.png")).getImage();
		ring1 = (new ImageIcon("characterOptions/draw/ring3.png")).getImage();
		ring2 = (new ImageIcon("characterOptions/draw/ring2.png")).getImage();
		ring3 = (new ImageIcon("characterOptions/draw/ring1.png")).getImage();
		amulet1 = (new ImageIcon("characterOptions/draw/amulet1.png")).getImage();
		amulet2 = (new ImageIcon("characterOptions/draw/amulet2.png")).getImage();
		amulet3 = (new ImageIcon("characterOptions/draw/amulet3.png")).getImage();
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		GuiRep[][] tab  = GuiInventory.getInventory();	
		g.drawImage(inventoryBackground, 0, 0, null);
		
		for(int i=0; i<tab.length; i++) {
			for(int j=0; j<tab[0].length; j++) {
				if(tab[i][j] != null) {
					
					switch (tab[i][j].getType()) {
					
					case 1: g.drawImage(allPotion,7+(j*20),254+(i*20),null);break;
					case 2: g.drawImage(healthPotion,7+(j*20),254+(i*20),null);break;
					case 3: g.drawImage(manaPotion,7+(j*20),254+(i*20),null);break;
					case 4: g.drawImage(amulet1,7+(j*20),254+(i*20),null);break;
					case 5: g.drawImage(amulet2,7+(j*20),254+(i*20),null);break;
					case 6: g.drawImage(amulet3,7+(j*20),254+(i*20),null);break;
					case 7: g.drawImage(ring1,7+(j*20),254+(i*20),null);break;
					case 8: g.drawImage(ring2,7+(j*20),254+(i*20),null);break;
					case 9: g.drawImage(ring3,7+(j*20),254+(i*20),null);break;
					case 10: g.drawImage(smallBelt,7+(j*20),254+(i*20),null);break;
					case 11: g.drawImage(mediumBelt,7+(j*20),254+(i*20),null);break;
					case 12: g.drawImage(largeBelt,7+(j*20),254+(i*20),null);break;
					case 13: g.drawImage(smallHelm,7+(j*20),254+(i*20),null);break;
					case 14: g.drawImage(mediumHelm,7+(j*20),254+(i*20),null);break;
					case 15: g.drawImage(largeHelm,7+(j*20),254+(i*20),null);break;
					case 16: g.drawImage(smallBoot,7+(j*20),254+(i*20),null);break;
					case 17: g.drawImage(mediumBoot,7+(j*20),254+(i*20),null);break;
					case 18: g.drawImage(largeBoot,7+(j*20),254+(i*20),null);break;
					case 19: g.drawImage(smallGlove,7+(j*20),254+(i*20),null);break;
					case 20: g.drawImage(mediumGlove,7+(j*20),254+(i*20),null);break;
					case 21: g.drawImage(largeGlove,7+(j*20),254+(i*20),null);break;
					case 22: g.drawImage(smallArmor,7+(j*20),254+(i*20),null);break;
					case 23: g.drawImage(mediumArmor,7+(j*20),254+(i*20),null);break;
					case 24: g.drawImage(largeArmor,7+(j*20),254+(i*20),null);break;
					case 25: g.drawImage(dress,7+(j*20),254+(i*20),null);break;
					case 26: g.drawImage(smallShield,7+(j*20),254+(i*20),null);break;
					case 27: g.drawImage(mediumShield,7+(j*20),254+(i*20),null);break;
					case 28: g.drawImage(largeShield,7+(j*20),254+(i*20),null);break;
					case 29: g.drawImage(smallStick,7+(j*20),254+(i*20),null);break;
					case 30: g.drawImage(largeStick,7+(j*20),254+(i*20),null);break;
					case 31: g.drawImage(smallAxe,7+(j*20),254+(i*20),null);break;
					case 32: g.drawImage(largeAxe,7+(j*20),254+(i*20),null);break;
					case 33: g.drawImage(smallSword,7+(j*20),254+(i*20),null);break;
					case 34: g.drawImage(largeSword,7+(j*20),254+(i*20),null);break;
					case 35: g.drawImage(smallArc,7+(j*20),254+(i*20),null);break;
					case 36: g.drawImage(largeArc,7+(j*20),254+(i*20),null);break;
					default :	break;
					}				
				} 
			}
			
		}
	}
}


