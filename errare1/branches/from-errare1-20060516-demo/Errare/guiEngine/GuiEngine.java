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

import gameEngine.GameEngine;
import gameEngine.Hero;

import java.awt.Point;
import java.awt.Rectangle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import main.ItemRep;
import main.Main;
import net.java.games.jogl.GL;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.GLDrawableFactory;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.GLUT;
import physicsEngine.CharacterPRep;
import soundEngine.SoundEngine;
import databaseEngine.DatabaseEngine;

/**
 * Class GuiEngine
 * Create, display, control graphics users interfaces
 * @author Ak
 */

public class GuiEngine {
	
	// Instance of the main
	private Main main;
	
	// Instance of the ChracterPRep
	private CharacterPRep charPRep;	
	
	// Instance of the gameEngine to communicate with
	private GameEngine gameEngine;
	
	// Instance of the soundEngine to play sounds
	private SoundEngine soundEngine;
	
	// Instance if the databaseEngine to have access to the database
	private DatabaseEngine databaseEngine;
	
	// Instance of the gui event class
	private GuiEvent event;
	
	// Instance of the Hero (client'Hero)
	private Hero hero;
	
	// Boolean indicate if the engine is link to the main or not (developpement)
	private boolean DEV=false;
	
	// A new GLCanvas to test in developpement
	private GLCanvas canvas;

	// Instance of the ActionBar
	private ActionBar actionBar;
	
	// The number of texture (items and interfaces)
	private final int NBTEXTURES = 55;
	
	// The screen width and height
	private int screenWidth,screenHeight;
	
	// The cursor postion on the screen
	private Point cursorPos;
	
	/* ***************************************************************
	 * The category of the item                                      *
	 * ***************************************************************
	 * 01 : potion          07 : wrist            13 : boot          *
	 * 02 : amulet          08 : armor            14 : shield        *
	 * 03 : bracelet        09 : glove            15 : sword         *
	 * 04 : ring            10 : belt             16 : axe           *
	 * 05 : buckle          11 : trousers         17 : stick         *
	 * 06 : helm            12 : tibia            18 : arc           *
	 * ***************************************************************/
	// The number associate with the display list
	private int	actionBarNum,infos, characteristic, inventory, chat,
	smallTraditionalPotion, mediumTraditionalPotion, largeTraditionalPotion, smallMagicPotion, mediumMagicPotion, largeMagicPotion, questPotion, rarePotion, singlePotion,
	smallTraditionalAmulet, mediumTraditionalAmulet, largeTraditionalAmulet, smallMagicAmulet, mediumMagicAmulet, largeMagicAmulet, questAmulet, rareAmulet, singleAmulet,
	smallTraditionalBracelet, mediumTraditionalBracelet, largeTraditionalBracelet, smallMagicBracelet, mediumMagicBracelet, largeMagicBracelet, questBracelet, rareBracelet, singleBracelet,
	smallTraditionalRing, mediumTraditionalRing, largeTraditionalRing, smallMagicRing, mediumMagicRing, largeMagicRing, questRing, rareRing, singleRing,
	smallTraditionalBuckle, mediumTraditionalBuckle, largeTraditionalBuckle, smallMagicBuckle, mediumMagicBuckle, largeMagicBuckle, questBuckle, rareBuckle, singleBuckle,
	smallTraditionalHelm, mediumTraditionalHelm, largeTraditionalHelm, smallMagicHelm, mediumMagicHelm, largeMagicHelm, questHelm, rareHelm, singleHelm,
	smallTraditionalWrist, mediumTraditionalWrist, largeTraditionalWrist, smallMagicWrist, mediumMagicWrist, largeMagicWrist, questWrist, rareWrist, singleWrist,
	smallTraditionalArmor, mediumTraditionalArmor, largeTraditionalArmor, smallMagicArmor, mediumMagicArmor, largeMagicArmor, questArmor, rareArmor, singleArmor,
	smallTraditionalGlove, mediumTraditionalGlove, largeTraditionalGlove, smallMagicGlove, mediumMagicGlove, largeMagicGlove, questGloVce, rareGlove, singleGlove,
	smallTraditionalBelt, mediumTraditionalBelt, largeTraditionalBelt, smallMagicBelt, mediumMagicBelt, largeMagicBelt, questBelt, rareBelt, singleBelt,
	smallTraditionalTrousers, mediumTraditionalTrousers, largeTraditionalTrousers, smallMagicTrousers, mediumMagicTrousers, largeMagicTrousers, questTrousers, rareTrousers, singleTrousers,
	smallTraditionalTibia, mediumTraditionalTibia, largeTraditionalTibia, smallMagicTibia, mediumMagicTibia, largeMagicTibia, questTibia, rareTibia, singleTibia,
	smallTraditionalBoot, mediumTraditionalBoot, largeTraditionalBoot, smallMagicBoot, mediumMagicBoot, largeMagicBoot, questBoot, rareBoot, singleBoot,
	smallTraditionalShield, mediumTraditionalShield, largeTraditionalShield, smallMagicShield, mediumMagicShield, largeMagicShield, questShield, rareShield, singleShield,
	smallTraditionalSword, mediumTraditionalSword, largeTraditionalSword, smallMagicSword, mediumMagicSword, largeMagicSword, questSword, rareSword, singleSword,
	smallTraditionalAxe, mediumTraditionalAxe, largeTraditionalAxe, smallMagicAxe, mediumMagicAxe, largeMagicAxe, questAxe, rareAxe, singleAxe,
	smallTraditionalStick, mediumTraditionalStick, largeTraditionalStick, smallMagicStick, mediumMagicStick, largeMagicStick, questStick, rareStick, singleStick,
	smallTraditionalArc, mediumTraditionalArc, largeTraditionalArc, smallMagicArc, mediumMagicArc, largeMagicArc, questArc, rareArc, singleArc;
	
	// The tab associate with the texture of items'display list
	private int[] texture;
	
	/**
	 * GuiEngine Constructor
	 * Constructor for developpement
	 * Create a Listener onto the GLCanvas,a new soundEngine,
	 * a new databaseEngine and a new gameEngine
	 */
	
	public GuiEngine(){
		
		// Create Instances
		texture = new int[NBTEXTURES];
		actionBar = new ActionBar(this);

		// Set developpement to true
		DEV = true ;

		// Create a canvas to test alone
		canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
		
		// Anonymous class (we must do that to test alone)
		GLEventListener glevent = new GLEventListener() {
			
			public void init(GLDrawable gLDrawable) {
				initDev(gLDrawable);
			}
			
			public void display(GLDrawable gLDrawable) {
				displayGUI(gLDrawable);
			}
			
			public void reshape(GLDrawable gLdrawable,int xstart,int ystart,int width, int height){
				reshapeGUI(gLdrawable,xstart,ystart,width,height);
			}
			
			public void displayChanged(GLDrawable gLDrawable,boolean modeChanged,boolean deviceChanged){}
			
		};
		
		// The Listener
		event = new GuiEvent(this);
		
		// We add the listener
		canvas.addGLEventListener(glevent);
		canvas.addKeyListener(event);
		canvas.addMouseListener(event);
		canvas.addMouseMotionListener(event); 
		canvas.setSize(1024,768); // Default Size
		
		// Instance of engines to test 
		gameEngine = new GameEngine(null);
		hero = new Hero("Aragorn", gameEngine);
		gameEngine.setMainChar(hero);
		soundEngine =new SoundEngine();
		
		try {
			databaseEngine = new DatabaseEngine();
			/*t.set("keys.gui.potion1", KeyEvent.VK_1);
			 t.set("keys.gui.potion2", KeyEvent.VK_2);
			 t.set("keys.gui.potion3", KeyEvent.VK_3);
			 t.set("keys.gui.potion4", KeyEvent.VK_4);
			 t.set("keys.gui.inventory", KeyEvent.VK_I);
			 t.set("keys.gui.characteristics", KeyEvent.VK_A);
			 t.set("keys.gui.pause", KeyEvent.VK_P);*/
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * GuiEngine Constructor
	 * @param main the pointer of the Main
	 * Constructor for the game
	 * Get all engines instances with the main
	 */
	
	public GuiEngine(Main main){
		
		// Create Instances
		texture = new int[NBTEXTURES];
		this.main = main;
		actionBar = new ActionBar(this);

		// Set developpement to false
		DEV = false;
		
		// Instance of engines (from the main)
		gameEngine = main.getGameEngine();
		databaseEngine = main.getDatabaseEngine();
		soundEngine = main.getSoundEngine();
	}
	
	/**
	 * initDev fonction
	 * @param gLDrawable the GLDrawable
	 * Initialize all for the developpement
	 */
	
	public void initDev(GLDrawable gLDrawable) {
		
		// For the DEV mode
		this.screenWidth = 1024;
		this.screenHeight = 768;
		
		// Load textures in the memory of the graphic card
		loadGLTexture(gLDrawable); 
		
		// Create display lists
		buildLists(gLDrawable);
		
		GL gl = gLDrawable.getGL();
		
		// OpenGL Initialisation
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glAlphaFunc(GL.GL_GREATER,0.1f);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glClearDepth(1.0f);	// Depth Buffer Setup
		gl.glClearStencil(0);	// Clear The Stencil Buffer To 0
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);	// Perspective Calculations
		
	}
	
	/**
	 * initGUI fonction
	 * @param gLDrawable the GLDrawable
	 * Initialize all for the Gui game
	 */
	
	public void initGUI(GLDrawable gLDrawable){
		
		// For the game mode	
		this.screenWidth = main.getJFrame().getWidth();
		this.screenHeight = main.getJFrame().getHeight();
		
		/* We create and add the event (there is now two lisener on the glCanvas)
		 the GUI listener and the Graphic listener*/
		event = new GuiEvent(this);
		main.getGraphicsEngine().getGLCanvas().addKeyListener(event);
		main.getGraphicsEngine().getGLCanvas().addMouseListener(event);
		main.getGraphicsEngine().getGLCanvas().addMouseMotionListener(event);
		
		// Load the texture in the memory of the graphic card
		loadGLTexture(gLDrawable); 
		
		// Create dysplay lists
		buildLists(gLDrawable); 
		
	}
	
	/**
	 * glBeginOrtho fonction
	 * @param gLDrawable the GLDrawable object
	 * Initialise the environnement
	 * Set the point 0,0 to the top left (like java and not like openGL)
	 * Set objects in the right position
	 **/
	
	public void glBeginOrtho(GLDrawable gLDrawable) {
		
		GL gl = gLDrawable.getGL();
		GLU glu = gLDrawable.getGLU();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		// The left lower corner (0,0) will be attached with that one of the window 
		glu.gluOrtho2D(
				0, (int) gLDrawable.getSize().getWidth(),
				0, (int) gLDrawable.getSize().getHeight());
		
		// Set the point of creation of the image in the left higher corner
		gl.glScaled(1,-1,1);
		
		// met le repère en haut à gauche
		gl.glTranslatef(0,-(int) gLDrawable.getSize().getHeight(),0); 
		
	}
	
	/**
	 * glText fonction
	 * @param gLDrawable the GLDrawable object
	 * @param str the string to display
	 * @param x X-coordinate 
	 * @param y Y-coordinate 
	 * Is used to post text on the screen
	 **/
	
	void glText(GLDrawable gLDrawable, String str, int x, int y) {
		
		final GL gl = gLDrawable.getGL();
		
		/* Specify the point running of drawing for the operations on the pixels 
		 +18 = the height of the policy */
		gl.glRasterPos2i(x, y+18); 
		
		new GLUT().glutBitmapString(gl, GLUT.BITMAP_HELVETICA_18, str);
		
	}
	
	/**
	 * displayGUI fonction
	 * @param gLDrawable the GLDrawable object
	 * Display all graphics users interfaces in the right places
	 */
	
	public void displayGUI(GLDrawable gLDrawable){
		
		GL gl = gLDrawable.getGL();
		
		// If we test we must clear the buffer
		if (DEV){
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
		}
		
		// Reset The View
		gl.glLoadIdentity();         
		
		// Initialise the environnement (position)
		glBeginOrtho(gLDrawable);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		// If we test we enable the blend else the graphicsEngine do it
		if (!DEV) {
			gl.glEnable(GL.GL_BLEND);
		}
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		
		/* ----- STATISTICS ----- */
		
		// Update Statistics
		updateStatistics();
		
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		
		// Display the interface
		gl.glCallList(statisticsNum); 
		
		// Set the life
		gl.glPushMatrix();
		gl.glColor3f(1.0f,0.0f,0.0f);
		gl.glTranslated(screenWidth-148,55,0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2i(0,0);
		gl.glVertex2d(pourcentageLife*1.32,0);
		gl.glVertex2d(pourcentageLife*1.32,14);
		gl.glVertex2i(0,14);  
		gl.glEnd();
		gl.glPopMatrix();
		
		// Set the mana
		gl.glPushMatrix();
		gl.glColor3f(0.0f,0.0f,1.0f);
		gl.glTranslated(screenWidth-148,74,0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2i(0,0);
		gl.glVertex2d(pourcentageMana*1.32,0);
		gl.glVertex2d(pourcentageMana*1.32,15);
		gl.glVertex2i(0,15);  
		gl.glEnd();
		gl.glPopMatrix();
		
		// Set the experiences
		gl.glPushMatrix();
		gl.glColor3f(0.0f,1.0f,0.0f);
		gl.glTranslated(screenWidth-250,75,0);
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2i(0,0);
		gl.glVertex2d(pourcentageLevel*0.68,0);
		gl.glVertex2d(pourcentageLevel*0.68,12);
		gl.glVertex2i(0,12);  
		gl.glEnd();
		gl.glPopMatrix();
		
		gl.glPopAttrib();
		
		
		/* ----- BELT ------ */
		
		// Display the interface
		gl.glCallList(beltNum);
		
		
		/* -- POTIONS -- */
		
		// Display the potions in the belt
		GuiRep[] beltTab  = belt.getGuiBelt();
		int[] weaponsTab  = belt.getWeaponsStatesTab();
		for (int i=0;i<beltTab.length;i++) {
			if (beltTab[i]!=null) {
				switch (beltTab[i].getType()) {
				case 1:gl.glTranslated(screenWidth/2-59+(i*38)-(i*2),screenHeight-43,0);gl.glCallList(allPotionNum);break;
				case 2:gl.glTranslated(screenWidth/2-59+(i*38)-(i*2),screenHeight-43,0);gl.glCallList(healthPotionNum);break;
				case 3:gl.glTranslated(screenWidth/2-59+(i*38)-(i*2),screenHeight-43,0);gl.glCallList(manaPotionNum);break;
				}
			}
		}
		
		/* -- WEAPONS STATES -- */
		
		// Display the weapons (fisrt and second) states
		switch (weaponsTab[0]) {
		case 1:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon1Num);break;
		case 2:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon2Num);break;
		case 3:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon3Num);break;
		case 4:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon4Num);break;
		case 5:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon5Num);break;
		case 6:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon6Num);break;
		case 7:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon7Num);break;
		case 8:gl.glTranslated(screenWidth/2-117,screenHeight-76,0);gl.glCallList(_1weapon8Num);break;
		default : break;
		}
		
		switch (weaponsTab[1]) {		
		case 1:gl.glTranslated(screenWidth/2+82,screenHeight-76,0);gl.glCallList(_2weapon1Num);break;
		case 2:gl.glTranslated(screenWidth/2+82,screenHeight-76,0);gl.glCallList(_2weapon2Num);break;
		case 3:gl.glTranslated(screenWidth/2+82,screenHeight-76,0);gl.glCallList(_2weapon3Num);break;
		default : break;
		}
		
		
		/* ----- INVENTORY ----- */
		
		if (inventoryOn) {
			
			// Display the interface
			gl.glCallList(inventoryNum);
			
			// Get some information about the hero to display it
			String name = getGameEngine().getHero().getName();
			String gold = ""+getGameEngine().getHero().getGold();
			
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			gl.glColor3f(1.0f,1.0f,0.0f);
			glText(gLDrawable,name,screenWidth-230,128);
			glText(gLDrawable, "12",screenWidth-42,120);
			glText(gLDrawable,gold,screenWidth-135,324);
			gl.glPopAttrib();
			
			GuiRep[][] inveTab  = inventory.getGuiInventory();
			GuiRep[] charTab = inventory.getGuiOnCharacter();
			
			// Display items 
			
			// Items on the hero 
			for (int i=0;i<charTab.length;i++) {
				if (charTab[i] != null) {
					switch(i){
					case 0: // Helm
						switch (charTab[i].getType()) {
						case 13:gl.glTranslated(screenWidth-148,117,0);gl.glCallList(smallHelmNum);break;
						case 14:gl.glTranslated(screenWidth-148,117,0);gl.glCallList(mediumHelmNum);break;
						case 15:gl.glTranslated(screenWidth-148,117,0);gl.glCallList(largeHelmNum);break;
						}
						break;
					case 1: // Amulette
						switch (charTab[i].getType()) {
						case 4:gl.glTranslated(screenWidth-85,140,0);gl.glCallList(amulet1Num);break;
						case 5:gl.glTranslated(screenWidth-85,140,0);gl.glCallList(amulet2Num);break;
						case 6:gl.glTranslated(screenWidth-85,140,0);gl.glCallList(amulet3Num);break;
						}
						break;
					case 2: // First Weapon
						switch (charTab[i].getType()) {
						case 29:gl.glTranslated(screenWidth-221,188,0);gl.glCallList(smallStickNum);break;
						case 30:gl.glTranslated(screenWidth-232,190,0);gl.glCallList(largeStickNum);break;
						case 31:gl.glTranslated(screenWidth-221,188,0);gl.glCallList(smallAxeNum);break;
						case 32:gl.glTranslated(screenWidth-232,190,0);gl.glCallList(largeAxeNum);break;
						case 33:gl.glTranslated(screenWidth-221,188,0);gl.glCallList(smallSwordNum);break;
						case 34:gl.glTranslated(screenWidth-221,180,0);gl.glCallList(largeSwordNum);break;
						case 35:gl.glTranslated(screenWidth-232,190,0);gl.glCallList(smallArcNum);break;
						case 36:gl.glTranslated(screenWidth-232,190,0);gl.glCallList(largeArcNum);break;
						}
						break;			 
					case 3: // Armor
						switch (charTab[i].getType()) {
						case 22:gl.glTranslated(screenWidth-159,192,0);gl.glCallList(smallArmorNum);break;
						case 23:gl.glTranslated(screenWidth-159,192,0);gl.glCallList(mediumArmorNum);break;
						case 24:gl.glTranslated(screenWidth-158,180,0);gl.glCallList(largeArmorNum);break;
						case 25:gl.glTranslated(screenWidth-148,183,0);gl.glCallList(dressNum);break;
						}
						break;
					case 4: // Seconde Weapon (Shield)
						switch (charTab[i].getType()) {
						case 26:gl.glTranslated(screenWidth-65,190,0);gl.glCallList(smallShieldNum);break;
						case 27:gl.glTranslated(screenWidth-65,190,0);gl.glCallList(mediumShieldNum);break;
						case 28:gl.glTranslated(screenWidth-65,185,0);gl.glCallList(largeShieldNum);break;
						}
						break;
					case 5: // Gloves
						switch (charTab[i].getType()) {
						case 19:gl.glTranslated(screenWidth-240,293,0);gl.glCallList(smallGloveNum);break;
						case 20:gl.glTranslated(screenWidth-240,293,0);gl.glCallList(mediumGloveNum);break;
						case 21:gl.glTranslated(screenWidth-240,293,0);gl.glCallList(largeGloveNum);break;
						}
						break;
					case 6: // Ring 1
						switch (charTab[i].getType()) {
						case 7:gl.glTranslated(screenWidth-181,287,0);gl.glCallList(ring1Num);break;
						case 8:gl.glTranslated(screenWidth-181,287,0);gl.glCallList(ring2Num);break;
						case 9:gl.glTranslated(screenWidth-181,287,0);gl.glCallList(ring3Num);break;
						}
						break;
					case 7: // Belt
						switch (charTab[i].getType()) {
						case 10:gl.glTranslated(screenWidth-148,287,0);gl.glCallList(smallBeltNum);break;
						case 11:gl.glTranslated(screenWidth-148,287,0);gl.glCallList(mediumBeltNum);break;
						case 12:gl.glTranslated(screenWidth-148,287,0);gl.glCallList(largeBeltNum);break;
						}
						break;
					case 8: // Ring 2
						switch (charTab[i].getType()) {
						case 7:gl.glTranslated(screenWidth-96,287,0);gl.glCallList(ring1Num);break;
						case 8:gl.glTranslated(screenWidth-96,287,0);gl.glCallList(ring2Num);break;
						case 9:gl.glTranslated(screenWidth-96,287,0);gl.glCallList(ring3Num);break;
						}
						break;
					case 9: // Boots
						switch (charTab[i].getType()) {
						case 16:gl.glTranslated(screenWidth-57,293,0);gl.glCallList(smallBootNum);break;
						case 17:gl.glTranslated(screenWidth-57,293,0);gl.glCallList(mediumBootNum);break;
						case 18:gl.glTranslated(screenWidth-57,293,0);gl.glCallList(largeBootNum);break;
						}
						break;
					}
				}
			}
			
			// Items in the Inventory of the hero
			for(int i=0; i<inveTab.length; i++) {
				for(int j=0; j<inveTab[0].length; j++) {
					if(inveTab[i][j] != null) {
						switch (inveTab[i][j].getType()) {					
						case 1:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(allPotionNum);break;
						case 2:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(healthPotionNum);break;
						case 3:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(manaPotionNum);break;
						case 4:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(amulet1Num);break;
						case 5:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(amulet2Num);break;
						case 6:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(amulet3Num);break;
						case 7:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(ring1Num);break;
						case 8:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(ring2Num);break;
						case 9:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(ring3Num);break;
						case 10:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallBeltNum);break;
						case 11:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumBeltNum);break;
						case 12:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeBeltNum);break;
						case 13:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallHelmNum);break;
						case 14:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumHelmNum);break;
						case 15:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeHelmNum);break;
						case 16:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallBootNum);break;
						case 17:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumBootNum);break;
						case 18:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeBootNum);break;
						case 19:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallGloveNum);break;
						case 20:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumGloveNum);break;
						case 21:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeGloveNum);break;
						case 22:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallArmorNum);break;
						case 23:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumArmorNum);break;
						case 24:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeArmorNum);break;
						case 25:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(dressNum);break;
						case 26:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallShieldNum);break;
						case 27:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(mediumShieldNum);break;
						case 28:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeShieldNum);break;
						case 29:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallStickNum);break;
						case 30:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeStickNum);break;
						case 31:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallAxeNum);break;
						case 32:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeAxeNum);break;
						case 33:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallSwordNum);break;
						case 34:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeSwordNum);break;
						case 35:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(smallArcNum);break;
						case 36:gl.glTranslated(screenWidth-256+8+(j*20),102+254+(i*20),0);gl.glCallList(largeArcNum);break;
						default :break;
						}			
					} 
				}
			}		
		}
		
		
		/* ----- CHARACTERISTICS ----- */
		
		if (characteristicsOn) {
			
			// Display the interface
			gl.glCallList(characteristicsNum);
			
			// Update characteristics
			updateCharacteristics();
			
			// Get and display information about the hero with different colors
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			gl.glColor3f(1.0f,1.0f,0.0f);
			glText(gLDrawable, name,170,12);
			glText(gLDrawable, "12",190,52);
			gl.glColor3f(1.0f,1.0f,0.5f);
			glText(gLDrawable, strength,190,104);
			glText(gLDrawable, dexterity,190,134);
			glText(gLDrawable, mind,190,164);
			gl.glColor3f(0.0f,1.0f,1.0f);
			glText(gLDrawable, damageMinTotal,180,194);
			gl.glColor3f(1.0f,1.0f,1.0f);
			glText(gLDrawable, "-",200,194);
			gl.glColor3f(1.0f,0.0f,0.0f);
			glText(gLDrawable, damageMaxTotal,208,194);
			gl.glColor3f(1.0f,1.0f,1.0f);
			glText(gLDrawable, armor,190,224);
			gl.glColor3f(0.5f,1.0f,0.5f);
			glText(gLDrawable, physicalResist,190,254);
			glText(gLDrawable, fireResist,190,284);
			glText(gLDrawable, iceResist,190,314);
			glText(gLDrawable, thunderResist,190,344);
			gl.glPopAttrib();
		}
		
		/* ----- PAUSE ----- */
		
		if (pauseOn) {
			
			// Display the interface
			gl.glCallList(pauseNum);
		}
		
		// If a item is select, this item will follow the cursor
		if (itemSelected) {		
			int w = itemSelect.getGui().getWidthPixel();
			int h = itemSelect.getGui().getHeightPixel();
			switch (itemSelect.getGui().getType()) {		
			case 1:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(allPotionNum);break;
			case 2:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(healthPotionNum);break;
			case 3:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(manaPotionNum);break;
			case 4:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(amulet1Num);break;
			case 5:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(amulet2Num);break;
			case 6:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(amulet3Num);break;
			case 7:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(ring1Num);break;
			case 8:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(ring2Num);break;
			case 9:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(ring3Num);break;
			case 10:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallBeltNum);break;
			case 11:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumBeltNum);break;
			case 12:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeBeltNum);break;
			case 13:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallHelmNum);break;
			case 14:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumHelmNum);break;
			case 15:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeHelmNum);break;
			case 16:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallBootNum);break;
			case 17:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumBootNum);break;
			case 18:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeBootNum);break;
			case 19:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallGloveNum);break;
			case 20:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumGloveNum);break;
			case 21:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeGloveNum);break;
			case 22:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallArmorNum);break;
			case 23:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumArmorNum);break;
			case 24:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeArmorNum);break;
			case 25:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(dressNum);break;
			case 26:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallShieldNum);break;
			case 27:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(mediumShieldNum);break;
			case 28:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeShieldNum);break;
			case 29:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallStickNum);break;
			case 30:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeStickNum);break;
			case 31:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallAxeNum);break;
			case 32:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeAxeNum);break;
			case 33:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallSwordNum);break;
			case 34:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeSwordNum);break;
			case 35:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(smallArcNum);break;
			case 36:gl.glTranslated(cursorPos.getX()-w+10,cursorPos.getY()-h+10,0);gl.glCallList(largeArcNum);break;
			default :break;
			}
		}	
		gl.glPopAttrib();
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
	}
	
	/**
	 * updateInventory fonction
	 * @param inveTab the new Inventory
	 */
	
	public void updateInventory(ItemRep[][] inveTab) {
		
		inventory.update(inveTab);
	}
	
	/**
	 * updateBelt fonction
	 * @param beltTab the new belt
	 */
	
	public void updateBelt(ItemRep[] beltTab) {
		
		belt.updateBelt(beltTab);
	}
	
	/**
	 * updateWeaponsStates fonction
	 * @param tabWeapons the new weapons states
	 */
	
	public void updateWeaponsStates(int [] weaponsStatesTab) {
		
		belt.updateWeapons(weaponsStatesTab);
	}
	
	/**
	 * updateCharacter fonction
	 * @param characterTab the new character items
	 */
	
	public void updateCharacter(ItemRep[] characterTab) {
		
		inventory.updateCharacter(characterTab);
	}
	
	// The pourcentage of life,mana and experience
	private double pourcentageLife,pourcentageMana,pourcentageLevel;
	
	// The time before the regeneration of the life or the mana
	private int lifeRegeneration=0,manaRegeneration=0;
	
	/**
	 * updateStatistics fonction
	 * Get the right pourcentage of life,mana and experience
	 */
	
	public void updateStatistics() {
		
		lifeRegeneration++;
		manaRegeneration++;
		statistics.setLevel(getGameEngine().getHero().getExperience_ranged());
		
		
		if (lifeRegeneration % 354 == 0) {
			updateLife(1);
		}else {
			updateLife(0);
		}
		if (manaRegeneration % 456 == 0) {
			updateMana(1);
		}else {
			updateMana(0);
		}
		pourcentageLife = statistics.getPourcentLife();
		pourcentageMana =  statistics.getPourcentMana();
		pourcentageLevel = statistics.getLevel()/100;
	}
	
	// Information about the hero'characteristics
	private String name,strength,dexterity,mind,damageMinTotal,damageMaxTotal,armor,
	physicalResist,fireResist,iceResist,thunderResist;
	
	/**
	 * updateCharacteristics fonction
	 * Get the right characteristics of the hero
	 * Use the gameEngine
	 */
	
	public void updateCharacteristics() {
		
		name = getGameEngine().getHero().getName();
		strength = ""+getGameEngine().getHero().getStrength();
		dexterity = ""+getGameEngine().getHero().getDexterity();
		mind = ""+getGameEngine().getHero().getMind();
		damageMinTotal = ""+getGameEngine().getHero().getDamageMinTotal();
		damageMaxTotal = ""+getGameEngine().getHero().getDamageMaxTotal();
		armor = ""+getGameEngine().getHero().getArmor();
		physicalResist = ""+getGameEngine().getHero().getPhysicalResist();
		fireResist = ""+getGameEngine().getHero().getFireResist();
		iceResist = ""+getGameEngine().getHero().getIceResist();
		thunderResist = ""+getGameEngine().getHero().getThunderResist();
		
	}
	
	/**
	 * updateLife fonction
	 * @param life the new life state
	 * Change the Life bar
	 */
	
	public void updateLife(int life) {
		getGameEngine().getHero().addLife(life);	
		double l = (double)(getGameEngine().getHero().getLife())/getGameEngine().getHero().getLifeMax();
		if (l>0 && l<=1 ){			
			statistics.setPourcentLife(l);
		}else if (l<=0) {
			statistics.setPourcentLife(0);
		}else if (l>1) {
			statistics.setPourcentLife(1);
		}
	}
	
	/**
	 * updateMana fonction
	 * @param mana the new mana state
	 * Change the Mana bar
	 */
	
	public void updateMana(int mana) {
		getGameEngine().getHero().addMana(mana);
		double m = (double)(mana+getGameEngine().getHero().getMana())/getGameEngine().getHero().getManaMax();
		if (m>0 && m<=1){
			statistics.setPourcentMana(m);
		}else if (m<=0) {
			statistics.setPourcentMana(0);
		}else if (m>1) {
			statistics.setPourcentMana(1);
		}
	}
	
	/**
	 * updateLevel fonction
	 * @param level the new level state
	 * Change the Level bar
	 */
	
	public void updateLevel(double lvl) {
		statistics.setLevel(lvl);
	}
	
	
	// The item witch is select and other temporary item to do the change between them
	private ItemRep itemSelect,itemSelectTmp;
	
	// Boolean indicate if there is a item select
	private boolean itemSelected = false;
	
	/**
	 * deleteItemSelect
	 * Delete the item Select (drop)
	 */
	
	public void deleteItemSelect() {
		itemSelect = null;
		itemSelectTmp = null;
		itemSelected = false ;
	}
	
	/**
	 * setItemSelectInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Select an item in the Inventory
	 */
	
	public void setItemSelectInventory(int i, int j) {
		
		if (!itemSelected) {
			itemSelected = true ;
			ItemRep[][] inveTab  = inventory.getItemInventory();
			
			itemSelect = inveTab[i][j];
			
			int h = itemSelect.getGui().getHeightCase();
			int w = itemSelect.getGui().getWidthCase();
			for (int k=0;k<h;k++) {
				for (int l=0;l<w;l++) {
					// Set null in the Inventory because the item is picked
					inveTab[k+i][l+j]=null;		
				}
			}
			// Update Inventory
			updateInventory(inveTab);	
		}	
	}
	
	/**
	 * setItemSelectBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Select a potion in the belt
	 */
	
	public void setItemSelectBelt(int i) {
		
		if (!itemSelected) {
			itemSelected = true ;
			ItemRep[] beltTab  = belt.getItemBelt();
			
			itemSelect = beltTab[i];
			
			// Set null because we picked the potion
			beltTab[i]=null;
			
			// Update Belt
			updateBelt(beltTab);
		}
	}
	
	/**
	 * setItemSelectCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Select an item on the character
	 */
	
	public void setItemSelectCharacter(int i) {
		
		if (!itemSelected) {
			itemSelected = true ;
			ItemRep[] characterTab  = inventory.getItemOnCharacter();
			
			itemSelect = characterTab[i];
			
			// Set null because we picked the item
			characterTab[i]=null;
			
			// Update Character
			updateCharacter(characterTab);
		}
	}
	
	/**
	 * setItemSelectChangeInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Switch between to items in the Inventory
	 */
	
	public void setItemSelectChangeInventory(int i, int j) {
		 
		if (itemSelected) {
		
		// We get the content of the Inventory
		ItemRep[][] inveTab  = inventory.getItemInventory();
		
		// We set the item select to the item select temp
		itemSelectTmp = itemSelect;
		
		// We set the new item Select
		itemSelect = inveTab[i][j];
		
		// Set null because the item will be leave is place to the other
		int h = itemSelect.getGui().getHeightCase();
		int w = itemSelect.getGui().getWidthCase();
		for (int k=0;k<h;k++) {
			for (int l=0;l<w;l++) {
				inveTab[k+i][l+j]=null;		
			}
		}
		
		// Set the item to the Inventory
		int h2= itemSelectTmp.getGui().getHeightCase();
		int w2= itemSelectTmp.getGui().getWidthCase();
		for (int m=0;(m<h);m++) {
			for (int n=0;(n<w);n++) {
				inveTab[i+m][j+n] = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),0,null));
			}
		}
		inveTab[i][j] = itemSelectTmp;
		
		// Update the Inventory
		updateInventory(inveTab);
		}
	}
	
	/**
	 * setItemSelectChangeCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Switch between to items on the character
	 */
	
	public void setItemSelectChangeCharacter(int i) {
		 
		if (itemSelected) {
		
		// We get the content of the belt
		ItemRep[] characterTab  = inventory.getItemOnCharacter();
		
		// We set the item select to the item select temp
		itemSelectTmp = itemSelect;
		
		itemSelect = characterTab[i];
		
		// Set null because we picked the potion
		characterTab[i]=null;
		
		// Set the new potion to the belt
		characterTab[i]=itemSelectTmp;
		
		// Update Character
		updateCharacter(characterTab);
		}
	}
	
	/**
	 * setItemSelectChangeBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Switch between to items in the Inventory
	 */
	
	public void setItemSelectChangeBelt(int i) {
		 
		if (itemSelected) {
		
		// We get the content of the belt
		ItemRep[] beltTab  = belt.getItemBelt();
		
		// We set the item select to the item select temp
		itemSelectTmp = itemSelect;
		
		itemSelect = beltTab[i];
		
		// Set null because we picked the potion
		beltTab[i]=null;
		
		// Set the new potion to the belt
		beltTab[i]=itemSelectTmp;
		
		// Update Belt
		updateBelt(beltTab);
		}
	}
	
	/**
	 * toInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Item back to Inventory
	 */
	
	public void toInventory(int i, int j) {
		
		if (itemSelected) {
			itemSelected = false;
			
			int h= itemSelect.getGui().getHeightCase();
			int w= itemSelect.getGui().getWidthCase();
			
			ItemRep[][] inveTab  = inventory.getItemInventory();
			
			for (int m=0;(m<h);m++) {
				for (int n=0;(n<w);n++) {
					inveTab[i+m][j+n] = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),0,null));
				}
			}
			inveTab[i][j] = itemSelect;
			
			// Update Inventory
			updateInventory(inveTab);
		}	
	}
	
	/**
	 * toBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Item back to belt
	 */
	
	public void toBelt(int i) {
		
		if (itemSelected) {
			itemSelected = false;
			
			ItemRep[] beltTab = belt.getItemBelt();
			beltTab[i] = itemSelect;
			
			// Update Belt
			updateBelt(beltTab);
		}
		
	}
	
	/**
	 * toCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Item back to Character
	 */
	
	public void toCharacter(int i) {
		
		if (itemSelected) {
			itemSelected = false;
			
			ItemRep[] characterTab = inventory.getItemOnCharacter();
			characterTab[i] = itemSelect;
			
			// Update Character
			updateCharacter(characterTab);
		}
		
	}
	
	/**
	 * weaponsStatesChange fonction
	 * @param type the type of the fisrt weapons
	 * Change icon associated with the weapon of the character
	 */
	
	public void weaponsStatesChange(int type) {
		
		int[] weaponsStatesTab  = belt.getWeaponsStatesTab();
		switch (type) {
		case 29: weaponsStatesTab[0]=1; break;
		case 30: weaponsStatesTab[0]=2; break;
		case 31: weaponsStatesTab[0]=5; break;
		case 32: weaponsStatesTab[0]=6; break;
		case 33: weaponsStatesTab[0]=3; break;
		case 34: weaponsStatesTab[0]=4; break;
		case 35: weaponsStatesTab[0]=7; break;
		case 36: weaponsStatesTab[0]=8; break;
		default: weaponsStatesTab[0]=0; break;
		}
		
		// Update WeaponsStates
		updateWeaponsStates(weaponsStatesTab);		
	}
	
	/**
	 * reshapeGui fonction
	 * @param gLdrawable the GLDrawable fonction
	 * @param xstart the x start
	 * @param ystart the y start
	 * @param width the width
	 * @param height the hieght
	 */
	
	public void reshapeGUI(GLDrawable gLdrawable,
			int xstart,int ystart,
			int width, int height){
		
		GL gl = gLdrawable.getGL();
		GLU glu = gLdrawable.getGLU();
		
		height = (height == 0) ? 1 : height;
		
		gl.glViewport(0,0,width,height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45,(float)width/height,1,1000);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * displayChangedGUI fonction
	 * @param gLDrawable
	 * @param modeChanged boolean
	 * @param deviceChanged boolean
	 * NOT WORK WITH JAVA
	 */
	
	public void displayChangedGUI(GLDrawable gLDrawable,
			boolean modeChanged,
			boolean deviceChanged){}
	
	
	/**
	 * buildLists fonction
	 * @param gLDrawable the GLDrawable object
	 * Build the display list (faster)
	 */
	
	public void buildLists(GLDrawable gLDrawable){
		
		System.out.println("Build display lists...");
		
		GL gl = gLDrawable.getGL();
		
actionBarNum = gl.glGenLists(NBTEXTURES);
gl.glNewList(actionBarNum,GL.GL_COMPILE); 
draw(gLDrawable,32,32,1);
gl.glEndList();

infos = actionBar+1;
gl.glNewList(infos,GL.GL_COMPILE); 
draw(gLDrawable,32,32,1);
gl.glEndList();

characteristic = infos+1;
gl.glNewList(characteristic,GL.GL_COMPILE); 
draw(gLDrawable,32,32,1);
gl.glEndList();

inventory = characteristic+1;
gl.glNewList(inventory,GL.GL_COMPILE); 
draw(gLDrawable,32,32,1);
gl.glEndList();

chat = inventory+1;
gl.glNewList(chat,GL.GL_COMPILE); 
draw(gLDrawable,32,32,1);
gl.glEndList();

		// Generate 54 Different Lists
		smallTraditionalPotion = gl.glGenLists(NBTEXTURES);           
		
		gl.glNewList(smallTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalPotion = smallTraditionalPotion+1;
		gl.glNewList(mediumTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalPotion = mediumTraditionalPotion+1;
		gl.glNewList(largeTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicPotion = largeTraditionalPotion +1;
		gl.glNewList(smallMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicPotion = smallMagicPotion+1;
		gl.glNewList(mediumMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicPotion  = mediumMagicPotion +1;
		gl.glNewList(largeMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questPotion  = largeMagicPotion +1;
		gl.glNewList(questPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rarePotion = questPotion+1;
		gl.glNewList(rarePotion ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singlePotion= rarePotion+1;
		gl.glNewList(singlePotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		
		smallTraditionalAmulet = singlePotion+1;           
		gl.glNewList(smallTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalAmulet = smallTraditionalAmulet+1;
		gl.glNewList(mediumTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalAmulet = mediumTraditionalAmulet+1;
		gl.glNewList(largeTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicAmulet = largeTraditionalAmulet +1;
		gl.glNewList(smallMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicAmulet = smallMagicAmulet+1;
		gl.glNewList(mediumMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicAmulet  = mediumMagicAmulet +1;
		gl.glNewList(largeMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questAmulet  = largeMagicAmulet +1;
		gl.glNewList(questAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareAmulet = questAmulet+1;
		gl.glNewList(rareAmulet ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleAmulet= rareAmulet+1;
		gl.glNewList(singleAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalBracelet = singleAmulet+1;        
		gl.glNewList(smallTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalBracelet = smallTraditionalBracelet+1;
		gl.glNewList(mediumTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalBracelet = mediumTraditionalBracelet+1;
		gl.glNewList(largeTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicBracelet = largeTraditionalBracelet +1;
		gl.glNewList(smallMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicBracelet = smallMagicBracelet+1;
		gl.glNewList(mediumMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicBracelet  = mediumMagicBracelet +1;
		gl.glNewList(largeMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questBracelet  = largeMagicBracelet +1;
		gl.glNewList(questBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareBracelet = questBracelet+1;
		gl.glNewList(rareBracelet ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleBracelet= rareBracelet+1;
		gl.glNewList(singlebracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalRing = singlebracelet+1;           
		gl.glNewList(smallTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalRing = smallTraditionalRing+1;
		gl.glNewList(mediumTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalRing = mediumTraditionalRing+1;
		gl.glNewList(largeTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicRing = largeTraditionalRing +1;
		gl.glNewList(smallMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicRing = smallMagicRing+1;
		gl.glNewList(mediumMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicRing  = mediumMagicRing +1;
		gl.glNewList(largeMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questRing  = largeMagicRing +1;
		gl.glNewList(questRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareRing = questRing+1;
		gl.glNewList(rareRing ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleRing= rareRing+1;
		gl.glNewList(singleRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalBuckle = singleRing+1;           	
		gl.glNewList(smallTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalBuckle = smallTraditionalBuckle+1;
		gl.glNewList(mediumTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalBuckle = mediumTraditionalBuckle+1;
		gl.glNewList(largeTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicBuckle = largeTraditionalBuckle +1;
		gl.glNewList(smallMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicBuckle = smallMagicBuckle+1;
		gl.glNewList(mediumMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicBuckle  = mediumMagicBuckle +1;
		gl.glNewList(largeMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questBuckle  = largeMagicBuckle +1;
		gl.glNewList(questBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareBuckle = questBuckle+1;
		gl.glNewList(rareBuckle ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleBuckle= rareBuckle+1;
		gl.glNewList(singleBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalHelm = singleBuckle+1;           
		gl.glNewList(smallTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalHelm = smallTraditionalHelm+1;
		gl.glNewList(mediumTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalHelm = mediumTraditionalHelm+1;
		gl.glNewList(largeTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicHelm = largeTraditionalHelm +1;
		gl.glNewList(smallMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicHelm = smallMagicHelm+1;
		gl.glNewList(mediumMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicHelm  = mediumMagicHelm +1;
		gl.glNewList(largeMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questHelm  = largeMagicHelm +1;
		gl.glNewList(questHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareHelm = questHelm+1;
		gl.glNewList(rareHelm ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleHelm= rareHelm+1;
		gl.glNewList(singleHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalWrist = singleHelm+1;           	
		gl.glNewList(smallTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalWrist = smallTraditionalWrist+1;
		gl.glNewList(mediumTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalWrist = mediumTraditionalWrist+1;
		gl.glNewList(largeTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicWrist = largeTraditionalWrist +1;
		gl.glNewList(smallMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicWrist = smallMagicWrist+1;
		gl.glNewList(mediumMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicWrist  = mediumMagicWrist +1;
		gl.glNewList(largeMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questWrist  = largeMagicWrist +1;
		gl.glNewList(questWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareWrist = questWrist+1;
		gl.glNewList(rareWrist ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleWrist= rareWrist+1;
		gl.glNewList(singleWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalArmor = singleWrist+1;           
		
		gl.glNewList(smallTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalArmor = smallTraditionalArmor+1;
		gl.glNewList(mediumTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalArmor = mediumTraditionalArmor+1;
		gl.glNewList(largeTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicArmor = largeTraditionalArmor +1;
		gl.glNewList(smallMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicArmor = smallMagicArmor+1;
		gl.glNewList(mediumMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicArmor  = mediumMagicArmor +1;
		gl.glNewList(largeMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questArmor  = largeMagicArmor +1;
		gl.glNewList(questArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareArmor = questArmor+1;
		gl.glNewList(rareArmor ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleArmor= rareArmor+1;
		gl.glNewList(singleArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalGlove = singleArmor+1;           
		
		gl.glNewList(smallTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalGlove = smallTraditionalGlove+1;
		gl.glNewList(mediumTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalGlove = mediumTraditionalGlove+1;
		gl.glNewList(largeTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicGlove = largeTraditionalGlove +1;
		gl.glNewList(smallMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicGlove = smallMagicGlove+1;
		gl.glNewList(mediumMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicGlove  = mediumMagicGlove +1;
		gl.glNewList(largeMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questGlove  = largeMagicGlove +1;
		gl.glNewList(questGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareGlove = questGlove+1;
		gl.glNewList(rareGlove ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleGlove= rareGlove+1;
		gl.glNewList(singleGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalBelt = singleGlove+1;           
		
		gl.glNewList(smallTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalBelt = smallTraditionalBelt+1;
		gl.glNewList(mediumTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalBelt = mediumTraditionalBelt+1;
		gl.glNewList(largeTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicBelt = largeTraditionalBelt +1;
		gl.glNewList(smallMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicBelt = smallMagicBelt+1;
		gl.glNewList(mediumMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicBelt  = mediumMagicBelt +1;
		gl.glNewList(largeMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questBelt  = largeMagicBelt +1;
		gl.glNewList(questBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareBelt = questBelt+1;
		gl.glNewList(rareBelt ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleBelt= rareBelt+1;
		gl.glNewList(singleBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalTrousers = singleBelt +1;           
		gl.glNewList(smallTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalTrousers = smallTraditionalTrousers+1;
		gl.glNewList(mediumTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalTrousers = mediumTraditionalTrousers+1;
		gl.glNewList(largeTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicTrousers = largeTraditionalTrousers +1;
		gl.glNewList(smallMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicTrousers = smallMagicTrousers+1;
		gl.glNewList(mediumMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicTrousers  = mediumMagicTrousers +1;
		gl.glNewList(largeMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questTrousers  = largeMagicTrousers +1;
		gl.glNewList(questTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareTrousers = questTrousers+1;
		gl.glNewList(rareTrousers ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleTrousers= rareTrousers+1;
		gl.glNewList(singleTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalTibia = singleTrousers+1;           
		gl.glNewList(smallTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalTibia = smallTraditionalTibia+1;
		gl.glNewList(mediumTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalTibia = mediumTraditionalTibia+1;
		gl.glNewList(largeTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicTibia = largeTraditionalTibia +1;
		gl.glNewList(smallMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicTibia = smallMagicTibia+1;
		gl.glNewList(mediumMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicTibia  = mediumMagicTibia +1;
		gl.glNewList(largeMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questTibia  = largeMagicTibia +1;
		gl.glNewList(questTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareTibia = questTibia+1;
		gl.glNewList(rareTibia ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleTibia= rareTibia+1;
		gl.glNewList(singleTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalBoot = singleTibia+1;           
		
		gl.glNewList(smallTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalBoot = smallTraditionalBoot+1;
		gl.glNewList(mediumTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalBoot = mediumTraditionalBoot+1;
		gl.glNewList(largeTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicBoot = largeTraditionalBoot +1;
		gl.glNewList(smallMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicBoot = smallMagicBoot+1;
		gl.glNewList(mediumMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicBoot  = mediumMagicBoot +1;
		gl.glNewList(largeMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questBoot  = largeMagicBoot +1;
		gl.glNewList(questBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareBoot = questBoot+1;
		gl.glNewList(rareBoot ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleBoot= rareBoot+1;
		gl.glNewList(singleBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalShield = gl.glGenLists(NBTEXTURES);           
		
		gl.glNewList(smallTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalShield = smallTraditionalShield+1;
		gl.glNewList(mediumTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalShield = mediumTraditionalShield+1;
		gl.glNewList(largeTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicShield = largeTraditionalShield +1;
		gl.glNewList(smallMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicShield = smallMagicShield+1;
		gl.glNewList(mediumMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicShield  = mediumMagicShield +1;
		gl.glNewList(largeMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questShield  = largeMagicShield +1;
		gl.glNewList(questShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareShield = questShield+1;
		gl.glNewList(rareShield ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleShield= rareShield+1;
		gl.glNewList(singleShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalSword = singleShield+1;           
		gl.glNewList(smallTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalSword = smallTraditionalSword+1;
		gl.glNewList(mediumTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalSword = mediumTraditionalSword+1;
		gl.glNewList(largeTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicSword=largeTraditionalSword +1;
		gl.glNewList(smallMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicSword = smallMagicSword+1;
		gl.glNewList(mediumMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicSword  = mediumMagicSword +1;
		gl.glNewList(largeMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questSword  = largeMagicSword +1;
		gl.glNewList(questSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareSword = questSword+1;
		gl.glNewList(rareSword ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleSword= rareSword+1;
		gl.glNewList(singleSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalAxe = singleSword+1;           
		gl.glNewList(smallTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalAxe = smallTraditionalAxe+1;
		gl.glNewList(mediumTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalAxe = mediumTraditionalAxe+1;
		gl.glNewList(largeTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicPotiAxe = largeTraditionalAxe +1;
		gl.glNewList(smallMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicAxe = smallMagicAxe+1;
		gl.glNewList(mediumMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicAxe  = mediumMagicAxe +1;
		gl.glNewList(largeMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questAxe  = largeMagicAxe +1;
		gl.glNewList(questAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareAxe = questAxe+1;
		gl.glNewList(rareAxe ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleAxe= rareAxe+1;
		gl.glNewList(singleAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalStick = singleAxe+1;           
		gl.glNewList(smallTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalStick = smallTraditionalStick+1;
		gl.glNewList(mediumTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalStick = mediumTraditionalStick+1;
		gl.glNewList(largeTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicStick = largeTraditionalStick +1;
		gl.glNewList(smallMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicStick = smallMagicStick+1;
		gl.glNewList(mediumMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicStick  = mediumMagicStick +1;
		gl.glNewList(largeMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questStick  = largeMagicStick +1;
		gl.glNewList(questStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareStick = questStick+1;
		gl.glNewList(rareStick ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleStick= rareStick+1;
		gl.glNewList(singleStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		
		smallTraditionalArc = singleStick+1;           
		gl.glNewList(smallTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,1);
		gl.glEndList();
		
		mediumTraditionalArc = smallTraditionalArc+1;
		gl.glNewList(mediumTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,2);
		gl.glEndList();
		
		largeTraditionalArc = mediumTraditionalArc+1;
		gl.glNewList(largeTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,3);
		gl.glEndList();
		
		smallMagicArc = largeTraditionalArc +1;
		gl.glNewList(smallMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,4);
		gl.glEndList();
		
		mediumMagicArc = smallMagicArc+1;
		gl.glNewList(mediumMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,5);
		gl.glEndList();
		
		largeMagicArc  = mediumMagicArc +1;
		gl.glNewList(largeMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,6);
		gl.glEndList();
		
		questArc  = largeMagicArc +1;
		gl.glNewList(questArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,7);
		gl.glEndList();
		
		rareArc = questArc+1;
		gl.glNewList(rareArc ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,8);
		gl.glEndList();
		
		singleArc= rareArc+1;
		gl.glNewList(singleArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,9);
		gl.glEndList();
		System.out.println("...Build finished");
		
	}
	
	/**
	 * draw fonction
	 * @param gLDrawable the GLDrawable object
	 * @param w the width of the texture
	 * @param h the height of th texture
	 * @param t the type of the texture
	 * Draw a square with a texture (type)
	 */
	
	public void draw(GLDrawable gLDrawable, int w, int h, int t){
		
		GL gl = gLDrawable.getGL();
		
		gl.glPushMatrix();	
		// Set the image in the right postion
		gl.glTranslated(0,h,0);
		gl.glScaled(1,-1d,0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[t]);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,0);gl.glVertex2i(0,0);
		gl.glTexCoord2i(1,0);gl.glVertex2i(w,0);
		gl.glTexCoord2i(1,1);gl.glVertex2i(w,h);
		gl.glTexCoord2i (0,1);gl.glVertex2i(0,h);  
		gl.glEnd();
		
		gl.glPopMatrix();
		
		gl.glLoadIdentity();
		
	}
	
	/**
	 * draw fonction
	 * @param gLDrawable the GLDrawable object
	 * @param x X-coordinate 
	 * @param y Y-coordinate 
	 * @param w the width of the texture
	 * @param h the height of th texture
	 * @param t the type of the texture
	 * Draw a square with a texture (type)
	 */
	
	public void draw(GLDrawable gLDrawable, int x, int y, int w, int h, int t){
		
		GL gl = gLDrawable.getGL();
		
		gl.glLoadIdentity();
		
		gl.glPushMatrix();
		// Set the image in the right postion
		gl.glTranslated(x,y,0);
		gl.glTranslated(0,h,0);
		gl.glScaled(1,-1d,0);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[t]);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0,0);gl.glVertex2i(0,0);
		gl.glTexCoord2i(1,0);gl.glVertex2i(w,0);
		gl.glTexCoord2i(1,1);gl.glVertex2i(w,h);
		gl.glTexCoord2i (0,1);gl.glVertex2i(0,h);  
		gl.glEnd();
		
		gl.glPopMatrix();
	}
	
	/**
	 * loadGLTexture fonction
	 * @param gLDrawable the GLDrawable object
	 */
	
	public void loadGLTexture(GLDrawable gLDrawable){
		
		GL gl = gLDrawable.getGL();
		
		LoadTexture textureLoader = new LoadTexture();
		gl.glGenTextures(NBTEXTURES, texture);  // 56 textures
		
		System.out.println("Loading textures...");
		
		/* --- items textures --- */
		
		textureLoader.load_texture("data/images/gui/actionBar.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/infos.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/quest.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[2]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/characteristic.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[3]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/inventory.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[4]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/chat.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[5]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0101.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[6]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0102.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[7]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0103.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[8]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0104.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[9]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0105.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[10]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0106.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[11]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0107.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[12]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0108.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[13]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0109.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[14]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0201.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[15]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0202.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[16]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0203.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[17]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0204.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[18]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0205.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[19]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0206.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[20]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0207.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[21]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0208.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[22]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0209.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[23]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0301.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[24]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0302.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[25]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0303.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[26]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0304.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[27]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0305.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[28]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0306.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[29]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0307.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[30]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0308.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[31]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0309.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[32]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0401.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[33]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0402.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[34]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0403.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[35]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0404.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[36]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());

		textureLoader.load_texture("data/images/gui/0405.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[37]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0406.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[38]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0407.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[39]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0408.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[40]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0409.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[41]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0501.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[42]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0502.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[43]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0503.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[44]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0504.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[45]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0505.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[46]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0506.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[47]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0507.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[48]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0508.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[49]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0509.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[50]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0601.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[51]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0602.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[52]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0603.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[53]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0604.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[54]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0605.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[55]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0606.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[56]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0607.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[57]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0608.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[58]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0609.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[59]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0701.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[60]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0702.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[61]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0703.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[62]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0704.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[63]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0705.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[64]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0706.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[65]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0707.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[66]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0708.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[67]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0709.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[68]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0801.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[69]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0802.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[70]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0803.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[71]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0804.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[72]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0805.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[73]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0806.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[74]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0807.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[75]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0808.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[76]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0809.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[77]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0901.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[78]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0902.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[79]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0903.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[80]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0904.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[81]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0905.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[82]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0906.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[83]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0907.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[84]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0908.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[85]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/0909.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[86]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1001.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[87]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1002.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[88]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1003.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[89]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1004.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[90]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1005.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[91]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1006.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[92]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1007.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[93]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1008.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[94]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1009.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[95]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1101.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[22]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1102.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[23]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1103.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[24]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1104.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[25]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1105.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[26]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1106.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[27]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1107.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[28]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1108.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[29]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1109.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[30]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1201.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[31]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1202.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[32]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1203.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[33]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1204.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[34]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1205.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[35]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1206.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[36]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());

		textureLoader.load_texture("data/images/gui/1207.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[37]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1208.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[38]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1209.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[39]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1301.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[40]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1302.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[41]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1303.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[42]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1304.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[43]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1305.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[44]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1306.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[45]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1307.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[46]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1308.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[47]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1309.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[48]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1401.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[49]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1402.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[50]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1403.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[51]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1403.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[52]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1404.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[53]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1405.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[54]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1406.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[55]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1407.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[56]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1408.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[57]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1409.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[58]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1501.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[59]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1502.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[60]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1503.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[61]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1504.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[62]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1504.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[63]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1505.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[64]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1506.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[65]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1507.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[66]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1508.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[67]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1509.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[68]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1601.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[69]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1602.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[70]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1603.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[71]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1604.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[72]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1605.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[73]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1606.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[74]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1607.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[75]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1608.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[76]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1609.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[77]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1701.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[78]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1702.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[79]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1603.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[71]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1704.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[72]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1705.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[73]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1706.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[74]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1707.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[75]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1708.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[76]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1709.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[77]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		System.out.println("... loading finished !");
		
	}
	
	/* --------------- GET --------------- */
	

	public ItemRep getItemSelect() {
		return itemSelect;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public GameEngine getGameEngine() {
		return gameEngine;
	}
	
	public SoundEngine getSoundEngine() {
		return soundEngine;
	}
	
	public DatabaseEngine getDatabaseEngine() {
		return databaseEngine;
	}
	
	public GLCanvas getCanvas() {
		return canvas;
	}
	
	public Hero getHero() {
		if (DEV) {
			return hero;
		}else {
			return getGameEngine().getHero();
		}
	}
	
	/* --------------- SET --------------- */
	

	public void setCursorPos(Point point) {
		cursorPos = point;
	}
	
	public void setMainChar(CharacterPRep physics) {
		charPRep = physics;
		getInventory().setCharPRep(charPRep);
	}
	
	/* --------------- IS ---------------*/
	
	
	public boolean isItemSelected() {
		return itemSelected;
	}
	
	public boolean isDEV() {
		return DEV;
	}

	
}