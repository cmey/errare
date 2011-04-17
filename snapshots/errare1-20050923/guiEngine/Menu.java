package guiEngine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Ak
 * @version 0.1
 * 
 * Class Menu 
 * Create and display the game's menu
 */

@SuppressWarnings("serial")

public class Menu extends JPanel {
	
	/* Declaration of all images */
	
	private Image imageNewCampaign1, imageLoad1, imageMultiplayer1, imageOptions1, imageBonus1, imageExit1,
	imageNewCampaign2, imageLoad2, imageMultiplayer2, imageOptions2, imageBonus2, imageExit2,
	imageMenu800x600, imageMenu1024x768, imageMenu1152x864, imageMenu1280x768, imageMenu1280x1024;
	
	/* The width and height of the frame */
	
	private int width,height;
	
	private int x,y,ecart;
	
	/* To know witch  screen size display */
	
	private boolean b800x600,b1024x768, b1152x864, b1280x768, b1280x1024, binconnu;
	
	private JFrame mainWindow;
	
	/** Constructor
	 * Create the menu
	 */
	
	public Menu(int width, int height, JFrame mainWindow) {
		
		
		this.mainWindow=mainWindow;
		this.width=width;
		this.height=height;

		
		//this.width=1152;this.height=864;
		switch (this.width) {
		case 800: if (this.height==600) {
			b800x600=true; 
			this.x=330; 
			this.y=240; 
			this.ecart=35;
		}
		break;
		case 1024: if (this.height==768) {
			b1024x768=true;
			this.x=450; 
			this.y=320; 
			this.ecart=45;
		}
		break;
		case 1152: if (this.height==864){
			b1152x864=true;
			this.x=500; 
			this.y=350; 
			this.ecart=50;
		}
		break;
		case 1280: if (this.height==768) {
			b1280x768=true;
			this.x=550; 
			this.y=320; 
			this.ecart=45;
		}else if (this.height==1024) {
			b1280x1024=true; 
			this.x=550; 
			this.y=450; 
			this.ecart=50;
		}
		break;
		}
		drawInitialisation();
		this.setPreferredSize(new Dimension(this.width,this.height));

	/*	b800x600=false;
		b1024x768=false;
	    b1152x864=false;
	    b1280x768=true;
	    b1280x1024=false;*/
		
		/** Action MouseListener */
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				/** Appelle la methode avec les coordonnees du curseur */
				mouseClick(e.getX(), e.getY());
				
			}
		});
		
		/** Action MotionListener */
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				/** Appelle la methode avec les coordonnee du curseur */
				mouseMove(e.getX(), e.getY());
			}
		});
	}
	
	private void mouseClick(int x, int y)  {
		Graphics g=this.getGraphics();
		int [] tabXAreaNewCampaign = {this.x,this.x,this.x+181,this.x+181};
		int [] tabYAreaNewCampaign = {this.y,this.y+76,this.y+76,this.y};
		Polygon areaNewCampaign = new Polygon(tabXAreaNewCampaign,tabYAreaNewCampaign,4);
		g.drawPolygon(areaNewCampaign);
		int [] tabXAreaLoad = {this.x+20,this.x+20,this.x+20+147,this.x+20+147};
		int [] tabYAreaLoad = {this.y+ecart+30,this.y+ecart+44+30,this.y+ecart+44+30,this.y+ecart+30};
		Polygon areaLoad = new Polygon(tabXAreaLoad,tabYAreaLoad,4);
		g.drawPolygon(areaLoad);
		int [] tabXAreaMultiplayer = {this.x-15,this.x-15,this.x-15+217,this.x-15+217};
		int [] tabYAreaMultiplayer = {this.y+(2*ecart)+30,this.y+(2*ecart)+30+37,this.y+(2*ecart)+30+37,this.y+(2*ecart)+30};
		Polygon areaMultiplayer = new Polygon(tabXAreaMultiplayer,tabYAreaMultiplayer,4);
		g.drawPolygon(areaMultiplayer);
		int [] tabXAreaOptions = {this.x+20,this.x+20,this.x+20+141,this.x+20+141};
		int [] tabYAreaOptions = {this.y+(3*ecart)+30,this.y+(3*ecart)+30+42,this.y+(3*ecart)+30+42,this.y+(3*ecart)+30};
		Polygon areaOptions = new Polygon(tabXAreaOptions,tabYAreaOptions,4);
		g.drawPolygon(areaOptions);
		int [] tabXAreaBonus = {this.x+30,this.x+30,this.x+30+117,this.x+30+117};
		int [] tabYAreaBonus = {this.y+(4*ecart)+30,this.y+(4*ecart)+30+38,this.y+(4*ecart)+30+38,this.y+(4*ecart)+30};
		Polygon areaBonus = new Polygon(tabXAreaBonus,tabYAreaBonus,4);
		g.drawPolygon(areaBonus);
		int [] tabXAreaExit = {this.x+20,this.x+20,this.x+20+140,this.x+20+140};
		int [] tabYAreaExit = {this.y+(5*ecart)+30,this.y+(5*ecart)+30+37,this.y+(5*ecart)+30+37,this.y+(5*ecart)+30};
		Polygon areaExit = new Polygon(tabXAreaExit,tabYAreaExit,4);
		g.drawPolygon(areaExit);
		
		if (areaNewCampaign.contains(new Point(x,y))) {
			System.out.println("New Campaign");
		}else if (areaLoad.contains(new Point(x,y))) {
			System.out.println("Load");
		}else if (areaMultiplayer.contains(new Point(x,y))) {
			System.out.println("Multiplayer");
		}else if (areaOptions.contains(new Point(x,y))) {
			g.drawImage(imageLoad2, this.x+20, this.y+ecart+30, null);
			Options options = new Options(this.width,this.height,this.mainWindow);
			//options.setPreferredSize(new Dimension(1024, 768));
			this.mainWindow.setContentPane(options);
			this.mainWindow.pack();
			this.mainWindow.setVisible(true);
		}else if (areaBonus.contains(new Point(x,y))) {
			System.out.println("Bonus");
		}else if (areaExit.contains(new Point(x,y))) {
			System.out.println("Exit");
			System.exit(0);
		}else {
			System.out.println("Out");

		}
		
		
	}
	
	private void mouseMove(int x, int y)  {
		Graphics g=this.getGraphics();
		int [] tabXAreaNewCampaign = {this.x,this.x,this.x+181,this.x+181};
		int [] tabYAreaNewCampaign = {this.y,this.y+76,this.y+76,this.y};
		Polygon areaNewCampaign = new Polygon(tabXAreaNewCampaign,tabYAreaNewCampaign,4);
		g.drawPolygon(areaNewCampaign);
		int [] tabXAreaLoad = {this.x+20,this.x+20,this.x+20+147,this.x+20+147};
		int [] tabYAreaLoad = {this.y+ecart+30,this.y+ecart+44+30,this.y+ecart+44+30,this.y+ecart+30};
		Polygon areaLoad = new Polygon(tabXAreaLoad,tabYAreaLoad,4);
		g.drawPolygon(areaLoad);
		int [] tabXAreaMultiplayer = {this.x-15,this.x-15,this.x-15+217,this.x-15+217};
		int [] tabYAreaMultiplayer = {this.y+(2*ecart)+30,this.y+(2*ecart)+30+37,this.y+(2*ecart)+30+37,this.y+(2*ecart)+30};
		Polygon areaMultiplayer = new Polygon(tabXAreaMultiplayer,tabYAreaMultiplayer,4);
		g.drawPolygon(areaMultiplayer);
		int [] tabXAreaOptions = {this.x+20,this.x+20,this.x+20+141,this.x+20+141};
		int [] tabYAreaOptions = {this.y+(3*ecart)+30,this.y+(3*ecart)+30+42,this.y+(3*ecart)+30+42,this.y+(3*ecart)+30};
		Polygon areaOptions = new Polygon(tabXAreaOptions,tabYAreaOptions,4);
		g.drawPolygon(areaOptions);
		int [] tabXAreaBonus = {this.x+30,this.x+30,this.x+30+117,this.x+30+117};
		int [] tabYAreaBonus = {this.y+(4*ecart)+30,this.y+(4*ecart)+30+38,this.y+(4*ecart)+30+38,this.y+(4*ecart)+30};
		Polygon areaBonus = new Polygon(tabXAreaBonus,tabYAreaBonus,4);
		g.drawPolygon(areaBonus);
		int [] tabXAreaExit = {this.x+20,this.x+20,this.x+20+140,this.x+20+140};
		int [] tabYAreaExit = {this.y+(5*ecart)+30,this.y+(5*ecart)+30+37,this.y+(5*ecart)+30+37,this.y+(5*ecart)+30};
		Polygon areaExit = new Polygon(tabXAreaExit,tabYAreaExit,4);
		g.drawPolygon(areaExit);
		if (areaNewCampaign.contains(new Point(x,y))) {

		}else if (areaLoad.contains(new Point(x,y))) {
			System.out.println("On Load"); 
		}else if (areaMultiplayer.contains(new Point(x,y))) {
			System.out.println("On Multiplayer");
		}else if (areaOptions.contains(new Point(x,y))) {
			System.out.println("On  Options");
		}else if (areaBonus.contains(new Point(x,y))) {
			System.out.println("On Bonus");
		}else if (areaExit.contains(new Point(x,y))) {
			System.out.println("On Exit");
		}else {
			System.out.println("Out");

		}
	}
	
	/**
	 * Methode drawInitialisation
	 * Load draws for the menu
	 */
	
	private void drawInitialisation() {
		
		imageMenu800x600 = (new ImageIcon("Menu/draw/menu800x600.png")).getImage();
		imageMenu1024x768 = (new ImageIcon("Menu/draw/menu1024x768.png")).getImage();
		imageMenu1152x864 = (new ImageIcon("Menu/draw/menu1152x864.png")).getImage();
		imageMenu1280x768 = (new ImageIcon("Menu/draw/menu1280x768.png")).getImage();
		imageMenu1280x1024 = (new ImageIcon("Menu/draw/menu1280x1024.png")).getImage();
		imageNewCampaign1 = (new ImageIcon("Menu/draw/nouvelle_campagne1.png")).getImage();
		imageLoad1 = (new ImageIcon("Menu/draw/charger1.png")).getImage();
		imageMultiplayer1 = (new ImageIcon("Menu/draw/multijoueur1.png")).getImage();
		imageOptions1 = (new ImageIcon("Menu/draw/options1.png")).getImage();
		imageBonus1 = (new ImageIcon("Menu/draw/bonus1.png")).getImage();
		imageExit1 = (new ImageIcon("Menu/draw/quitter1.png")).getImage();
		imageNewCampaign2 = (new ImageIcon("Menu/draw/nouvelle_campagne2.png")).getImage();
		imageLoad2 = (new ImageIcon("Menu/draw/charger2.png")).getImage();
		imageMultiplayer2 = (new ImageIcon("Menu/draw/multijoueur2.png")).getImage();
		imageOptions2 = (new ImageIcon("Menu/draw/options2.png")).getImage();
		imageBonus2 = (new ImageIcon("Menu/draw/bonus2.png")).getImage();
		imageExit2 = (new ImageIcon("Menu/draw/quitter2.png")).getImage();
	}
	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (b800x600) {
			g.drawImage(imageMenu800x600, 0, 0, null);
			g.translate(x,y);
			g.drawImage(imageNewCampaign1, 0, 0, null);
			g.translate(0,ecart+30);
			g.drawImage(imageLoad1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageMultiplayer1, -15, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageOptions1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageBonus1, 30, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageExit1, 20, 0, null);
		}else if (b1024x768) {
			g.drawImage(imageMenu1024x768, 0, 0, null);
			g.translate(x,y);
			g.drawImage(imageNewCampaign1, 0, 0, null);
			g.translate(0,ecart+30);
			g.drawImage(imageLoad1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageMultiplayer1, -15, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageOptions1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageBonus1, 30, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageExit1, 20, 0, null);
		}else if (b1152x864) {
			g.drawImage(imageMenu1152x864, 0, 0, null);
			g.translate(x,y);
			g.drawImage(imageNewCampaign1, 0, 0, null);
			g.translate(0,ecart+30);
			g.drawImage(imageLoad1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageMultiplayer1, -15, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageOptions1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageBonus1, 30, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageExit1, 20, 0, null);
		}else if (b1280x768) {
			g.drawImage(imageMenu1280x768, 0, 0, null);
			g.translate(x,y);
			g.drawImage(imageNewCampaign1, 0, 0, null);
			g.translate(0,ecart+30);
			g.drawImage(imageLoad1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageMultiplayer1, -15, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageOptions1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageBonus1, 30, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageExit1, 20, 0, null);
		}else if (b1280x1024) {
			g.drawImage(imageMenu1280x1024, 0, 0, null);
			g.translate(x,y);
			g.drawImage(imageNewCampaign1, 0, 0, null);
			g.translate(0,ecart+30);
			g.drawImage(imageLoad1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageMultiplayer1, -15, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageOptions1, 20, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageBonus1, 30, 0, null);
			g.translate(0,ecart);
			g.drawImage(imageExit1, 20, 0, null);
		}else {
			
		}
		
	}
	
	/**
	 * Main
	 * @param args
	 * Create a JFrame for the menu
	 */
	
	public static void main (String args[]) {


		
	}
	
}
