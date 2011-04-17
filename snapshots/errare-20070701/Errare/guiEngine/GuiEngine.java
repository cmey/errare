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
import geom.Circle;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Text2D;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
	private Event event;
	
	// Instance of the Hero (client'Hero)
	private Hero hero;
	
	// Boolean indicate if the engine is link to the main or not (developpement)
	private boolean DEV=false;
	
	// A new GLCanvas to test in developpement
	private GLCanvas canvas;
	
	// Instance of the ActionBar
	private ActionBar actionBar;
	
	// The number of texture (items and interfaces)
	private final int NBTEXTURES = 250;
	
	// The screen width and height
	private int screenWidth,screenHeight;
	
	// The cursor postion on the screen
	private Point cursorPos;
	
	// The number associate with the display list
	private int	actionBarNum, equipement, characteristic, inventory, chat,
	smallTraditionalPotion, mediumTraditionalPotion, largeTraditionalPotion, smallMagicPotion, mediumMagicPotion, largeMagicPotion, questPotion, rarePotion, singlePotion,
	smallTraditionalAmulet, mediumTraditionalAmulet, largeTraditionalAmulet, smallMagicAmulet, mediumMagicAmulet, largeMagicAmulet, questAmulet, rareAmulet, singleAmulet,
	smallTraditionalBracelet, mediumTraditionalBracelet, largeTraditionalBracelet, smallMagicBracelet, mediumMagicBracelet, largeMagicBracelet, questBracelet, rareBracelet, singleBracelet,
	smallTraditionalRing, mediumTraditionalRing, largeTraditionalRing, smallMagicRing, mediumMagicRing, largeMagicRing, questRing, rareRing, singleRing,
	smallTraditionalBuckle, mediumTraditionalBuckle, largeTraditionalBuckle, smallMagicBuckle, mediumMagicBuckle, largeMagicBuckle, questBuckle, rareBuckle, singleBuckle,
	smallTraditionalHelm, mediumTraditionalHelm, largeTraditionalHelm, smallMagicHelm, mediumMagicHelm, largeMagicHelm, questHelm, rareHelm, singleHelm,
	smallTraditionalWrist, mediumTraditionalWrist, largeTraditionalWrist, smallMagicWrist, mediumMagicWrist, largeMagicWrist, questWrist, rareWrist, singleWrist,
	smallTraditionalArmor, mediumTraditionalArmor, largeTraditionalArmor, smallMagicArmor, mediumMagicArmor, largeMagicArmor, questArmor, rareArmor, singleArmor,
	smallTraditionalGlove, mediumTraditionalGlove, largeTraditionalGlove, smallMagicGlove, mediumMagicGlove, largeMagicGlove, questGlove, rareGlove, singleGlove,
	smallTraditionalBelt, mediumTraditionalBelt, largeTraditionalBelt, smallMagicBelt, mediumMagicBelt, largeMagicBelt, questBelt, rareBelt, singleBelt,
	smallTraditionalTrousers, mediumTraditionalTrousers, largeTraditionalTrousers, smallMagicTrousers, mediumMagicTrousers, largeMagicTrousers, questTrousers, rareTrousers, singleTrousers,
	smallTraditionalTibia, mediumTraditionalTibia, largeTraditionalTibia, smallMagicTibia, mediumMagicTibia, largeMagicTibia, questTibia, rareTibia, singleTibia,
	smallTraditionalBoot, mediumTraditionalBoot, largeTraditionalBoot, smallMagicBoot, mediumMagicBoot, largeMagicBoot, questBoot, rareBoot, singleBoot,
	smallTraditionalShield, mediumTraditionalShield, largeTraditionalShield, smallMagicShield, mediumMagicShield, largeMagicShield, questShield, rareShield, singleShield,
	smallTraditionalSword, mediumTraditionalSword, largeTraditionalSword, smallMagicSword, mediumMagicSword, largeMagicSword, questSword, rareSword, singleSword,
	smallTraditionalAxe, mediumTraditionalAxe, largeTraditionalAxe, smallMagicAxe, mediumMagicAxe, largeMagicAxe, questAxe, rareAxe, singleAxe,
	smallTraditionalStick, mediumTraditionalStick, largeTraditionalStick, smallMagicStick, mediumMagicStick, largeMagicStick, questStick, rareStick, singleStick,
	smallTraditionalArc, mediumTraditionalArc, largeTraditionalArc, smallMagicArc, mediumMagicArc, largeMagicArc, questArc, rareArc, singleArc,
	spellThunder, spellThunder1, spellThunder2, spellThunder3,
	closeOn, closeOut, lockOn, lockOut, upOn, upOut, downOn, downOut, emoteOn, emoteOut,	
	channelOn, channelOut, channel1, channel2, channel3, channel4, set1On, set1Out, set2On, set2Out,
	spellIce, spellIce1, spellIce2, spellIce3, spellIce4,
	spellFire, spellFire1, spellFire2, spellFire3, spellFire4,
	spellEarth, spellEarth1, spellEarth2, spellEarth3, spellEarth4, spellEarth5,
	spellWind, spellWind1, spellWind2, spellWind3, spellWind4, spellWind5, spellWind6,
	selectCursor;
	
	// The tab associate with the texture of items'display list
	private int[] texture;
	
	private int actionSelect;
	private boolean actionSelected;
	
	private int displayActionBar = 0, displayChat = 70, displayEquipement = 70, displayInventory = 70;
	
	private String textChat="";
	
	private boolean chatMsgSent = false;
	
	private String chatMsgTyped="";
	
	private int selectCursorFlick=0;
	private int cptDebugAction=0;
	
	private boolean debugMode;
	
	private String action;
	
	private Text2D txt;
	
	// The item witch is select and other temporary item to do the change between them
	private int itemSelect,itemSelectTmp;
	
	// Boolean indicate if there is a item select
	private boolean itemSelected = false;
	
	
	/**
	 * GuiEngine Constructor
	 * Constructor for developpement
	 * Create a Listener onto the GLCanvas,a new soundEngine,
	 * a new databaseEngine and a new gameEngine
	 */
	
	public GuiEngine(){
		
		//joueur.getGraphics().castSpell(Spell.FIRE_RING_SPELL);
		
		// Create Instances
		texture = new int[NBTEXTURES];
		
		// Set developpement to true
		DEV = true ;
		
		debugMode=true;
		
		action="";
		
		txt = new Text2D("data/images/other/font.png");
		
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
		event = new Event(this);
		
		// We add the listener
		canvas.addGLEventListener(glevent);
		canvas.addKeyListener(event);
		canvas.addMouseListener(event);
		canvas.addMouseMotionListener(event); 
		canvas.setSize(1024,768); // Default Size
		
		// Instance of engines to test 
		actionBar = new ActionBar(this);
		gameEngine = new GameEngine(null);
		hero = new Hero("Ak", gameEngine);
		gameEngine.setMainChar(hero);
		soundEngine =new SoundEngine();
		
		try {
			databaseEngine = new DatabaseEngine();
		} catch (ParserConfigurationException e) {
			System.err.println("dataBase Error : "+e.getMessage());
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
		
		debugMode=true;
		
		action="";
		
		txt = new Text2D("data/images/other/font.png");
			
		/*
		actionBar.addAction(5001,0);
		actionBar.addAction(5101,1);
		actionBar.addAction(5201,2);
		actionBar.addAction(5202,3);
		actionBar.addAction(5305,4);
		actionBar.addAction(5401,5);*/
		
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
		event = new Event(this);
		main.getGraphicsEngine().getGLCanvas().addKeyListener(event);
		main.getGraphicsEngine().getGLCanvas().addMouseListener(event);
		main.getGraphicsEngine().getGLCanvas().addMouseMotionListener(event);
		
		// Load the texture in the memory of the graphic card
		loadGLTexture(gLDrawable); 
		
		// Create display lists
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
	 * displayGUI fonction
	 * @param gLDrawable the GLDrawable object
	 * Display all graphics users interfaces in the right places
	 */
	
	public void displayGUI(GLDrawable gLDrawable){
		
		try {
			Thread.sleep(0,1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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
		
		txt.BuildFont(gl);
		
		/* ----- ACTION BAR ----- */
		
		if (DEV) {
			GraphicsEngine.window_width=1024;
			GraphicsEngine.window_height=768;
			}

		txt.setFontSize(12,gl);
		
		// Infos messages
		txt.glPrintc(gl,10,screenHeight-115,"'Echap' pour quitter et sauvegarder les parametres",1.0f,1.0f,1.0f);
		txt.glPrintc(gl,10,screenHeight-95,"........",1.0f,1.0f,1.0f);
		txt.glPrintc(gl,10,screenHeight-75,"'F1' pour voir les differentes zones d'action",1.0f,1.0f,1.0f);
		txt.glPrintc(gl,10,screenHeight-55,"'C' pour ouvrir/fermer le chat",1.0f,1.0f,1.0f);
		txt.glPrintc(gl,10,screenHeight-35,"'E' pour ouvrir/fermer la fenetre d'equipement",1.0f,1.0f,1.0f);
		txt.glPrintc(gl,10,screenHeight-15,"'I' pour ouvrir/fermer l'inventaire",1.0f,1.0f,1.0f);
		
		//Chat
		
		// If it's not dragged
		if (!actionBar.getChat().isDragged()) {
			// If it's open
			if (actionBar.getChat().isVisible()) {
				// If the chat isn't set to the default position we display this window directly
				if (actionBar.getChat().getPosition().getX()!=actionBar.getChat().getDefaultPosition().getX() ||
						actionBar.getChat().getPosition().getY()!=actionBar.getChat().getDefaultPosition().getY()) {
					if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) displayChat=Chat.CHAT_HEIGHT;
				}
				// Else we display it we a scroll effect
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) {
					displayChat = (displayChat<Chat.CHAT_HEIGHT) ? displayChat+1 : displayChat ;
					gl.glTranslated(actionBar.getChat().getPosition().getX(),actionBar.getChat().getPosition().getY()-displayChat,0);
					gl.glCallList(chat);		
				}
				// When is finish, we display the chat's tools
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT && displayChat==Chat.CHAT_HEIGHT) {
					gl.glTranslated(actionBar.getChat().getPosition().getX()+9,actionBar.getChat().getPosition().getY()-50,0);
					gl.glCallList(emoteOn);
					gl.glTranslated(actionBar.getChat().getPosition().getX()+92,actionBar.getChat().getPosition().getY()-229,0);
					// We get witch channel is on
					if (actionBar.getChat().getActiveChannel()==Chat.GENERAL_CHANNEL) {
						gl.glCallList(channel1);
						textChat = "General Channel";
					}else {
						gl.glCallList(channelOn);
					}
					gl.glTranslated(actionBar.getChat().getPosition().getX()+153,actionBar.getChat().getPosition().getY()-229,0);
					if (actionBar.getChat().getActiveChannel()==Chat.GUILD_CHANNEL) {
						gl.glCallList(channel2);
						textChat = "Guild Channel";
					}else {
						gl.glCallList(channelOn);
					}
					gl.glTranslated(actionBar.getChat().getPosition().getX()+213,actionBar.getChat().getPosition().getY()-229,0);
					if (actionBar.getChat().getActiveChannel()==Chat.GROUP_CHANNEL) {
						gl.glCallList(channel3);
						textChat = "Group Channel";
					}else {
						gl.glCallList(channelOn);
					}
					gl.glTranslated(actionBar.getChat().getPosition().getX()+271,actionBar.getChat().getPosition().getY()-229,0);
					if (actionBar.getChat().getActiveChannel()==Chat.PRIVATE_CHANNEL) {
						gl.glCallList(channel4);
						textChat = "Private Channel";
					}else {
						gl.glCallList(channelOn);
					}
					// All chat's buttons
					gl.glTranslated(actionBar.getChat().getPosition().getX()+316,actionBar.getChat().getPosition().getY()-229,0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getChat().getPosition().getX()+334,actionBar.getChat().getPosition().getY()-229,0);
					gl.glCallList(lockOn);
					gl.glTranslated(actionBar.getChat().getPosition().getX()+352,actionBar.getChat().getPosition().getY()-229,0);
					gl.glCallList(closeOn);
				}
				
				// If it is not opened
			}else {		
				// It's must be at the default position to set invisible the window 
				// So we do a scroll effect
				// 70 = ActionBar.HEIGHT(40) + 30 (CHAT.HEIGHT (small))
				if (displayActionBar>=ActionBar.ACTIONBARMAP_HEIGHT) {
					displayChat = (displayChat>70) ? displayChat -1 : displayChat ;
					gl.glTranslated((screenWidth-1024)/2+20,screenHeight-displayChat,0);
					gl.glCallList(chat);
					// When is finish we set the buttons
					if (displayChat==70) {
						gl.glTranslated((screenWidth-1024)/2+112,screenHeight-65,0);
						gl.glCallList(channelOn);
						gl.glTranslated((screenWidth-1024)/2+173,screenHeight-65,0);
						gl.glCallList(channelOn);
						gl.glTranslated((screenWidth-1024)/2+233,screenHeight-65,0);
						gl.glCallList(channelOn);
						gl.glTranslated((screenWidth-1024)/2+291,screenHeight-65,0);
						gl.glCallList(channelOn);
						gl.glTranslated((screenWidth-1024)/2+336,screenHeight-65,0);
						gl.glCallList(upOn);
						gl.glTranslated((screenWidth-1024)/2+354,screenHeight-65,0);
						gl.glCallList(lockOn);
						gl.glTranslated((screenWidth-1024)/2+372,screenHeight-65,0);
						gl.glCallList(closeOn);
					}
				}
			}
		}
		
		// Equipement
		
		// If it's not dragged
		if (!actionBar.getEquipement().isDragged()) {
			// If it's open
			if (actionBar.getEquipement().isVisible()) {				
//				If the equipement isn't set to the default position we display this window directly
				if (actionBar.getEquipement().getPosition().getX()!=actionBar.getEquipement().getDefaultPosition().getX() ||
						actionBar.getEquipement().getPosition().getY()!=actionBar.getEquipement().getDefaultPosition().getY()) {
					if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) displayEquipement=Equipement.EQUIPEMENT_HEIGHT;
				}
				// Else we display it we a scroll effect
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) {
					displayEquipement = (displayEquipement<Equipement.EQUIPEMENT_HEIGHT) ? displayEquipement+1 : displayEquipement ;
					gl.glTranslated(actionBar.getEquipement().getPosition().getX(),actionBar.getEquipement().getPosition().getY()-displayEquipement,0);
					gl.glCallList(equipement);		
				}
				
				// When is finish, we display the equipement's tools
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT && displayEquipement==Equipement.EQUIPEMENT_HEIGHT) {
					if (actionBar.getEquipement().getActiveSet()==Equipement.SET1) {
						gl.glTranslated(actionBar.getEquipement().getPosition().getX()+34,actionBar.getEquipement().getPosition().getY()-437,0);
						gl.glCallList(set1Out);
						gl.glTranslated(actionBar.getEquipement().getPosition().getX()+137,actionBar.getEquipement().getPosition().getY()-437,0);
						gl.glCallList(set2On);
						int [] tabSet = actionBar.getEquipement().getTabSet1();
						for (int i=0;i<tabSet.length;i++) {
							if(tabSet[i]!=0) {
								if (i<8)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+22,actionBar.getEquipement().getPosition().getY()-413+i*39,0);
								else if (i>7 && i<11)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+71+(i-8)*40,actionBar.getEquipement().getPosition().getY()-120,0);
								else if (i>10)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+201,actionBar.getEquipement().getPosition().getY()-413+(i-11)*39,0);					
								switch (tabSet[i]) {
								case 0301: gl.glCallList(smallTraditionalBracelet);break;
								case 1507: gl.glCallList(questSword);break;
								case 1509: gl.glCallList(singleSword);break;
								case 1602: gl.glCallList(smallTraditionalAxe);break;
								case 1703: gl.glCallList(largeTraditionalStick);break;
								case 1801: gl.glCallList(smallTraditionalArc);break;
								case 1804: gl.glCallList(smallMagicArc);break;
								case 1806: gl.glCallList(largeMagicArc);break;
								default : gl.glCallList(smallMagicArc);break;
								// TODO ...
								}
							}
						}
					}else {
						gl.glTranslated(actionBar.getEquipement().getPosition().getX()+34,actionBar.getEquipement().getPosition().getY()-437,0);
						gl.glCallList(set1On);
						gl.glTranslated(actionBar.getEquipement().getPosition().getX()+137,actionBar.getEquipement().getPosition().getY()-437,0);
						gl.glCallList(set2Out);
						int [] tabSet = actionBar.getEquipement().getTabSet2();
						for (int i=0;i<tabSet.length;i++) {
							if(tabSet[i]!=0) {
								if (i<8)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+22,actionBar.getEquipement().getPosition().getY()-413+i*39,0);
								else if (i>=8 && i<=10)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+71+(i-8)*40,actionBar.getEquipement().getPosition().getY()-120,0);
								else if (i>10)gl.glTranslated(actionBar.getEquipement().getPosition().getX()+201,actionBar.getEquipement().getPosition().getY()-413+(i-11)*39,0);					
								switch (tabSet[i]) {
								case 0301: gl.glCallList(smallTraditionalBracelet);break;
								case 1507: gl.glCallList(questSword);break;
								case 1509: gl.glCallList(singleSword);break;
								case 1602: gl.glCallList(smallTraditionalAxe);break;
								case 1703: gl.glCallList(largeTraditionalStick);break;
								case 1801: gl.glCallList(smallTraditionalArc);break;
								case 1804: gl.glCallList(smallMagicArc);break;
								case 1806: gl.glCallList(largeMagicArc);break;
								default : gl.glCallList(smallMagicArc);break;
								// TODO ...
								}
							}
						}
					}
					gl.glTranslated(actionBar.getEquipement().getPosition().getX()+186,actionBar.getEquipement().getPosition().getY()-477,0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getEquipement().getPosition().getX()+204,actionBar.getEquipement().getPosition().getY()-477,0);
					gl.glCallList(lockOn);
					gl.glTranslated(actionBar.getEquipement().getPosition().getX()+224,actionBar.getEquipement().getPosition().getY()-477,0);
					gl.glCallList(closeOn);	
				}
				// If it's not opened
			}else {
				if (displayActionBar>=ActionBar.ACTIONBARMAP_HEIGHT) {
					displayEquipement = (displayEquipement>70) ? displayEquipement-1 : displayEquipement;
					gl.glTranslated((screenWidth-1024)/2+406,screenHeight-displayEquipement,0);
					gl.glCallList(equipement);
					if (displayEquipement==70) {
						gl.glTranslated((screenWidth-1024)/2+592,screenHeight-65,0);
						gl.glCallList(upOn);
						gl.glTranslated((screenWidth-1024)/2+610,screenHeight-65,0);
						gl.glCallList(lockOn);
						gl.glTranslated((screenWidth-1024)/2+628,screenHeight-65,0);
						gl.glCallList(closeOn);	
					}
				}
			}
		}
		
		// Inventory
		
//		 If it's not dragged
		if (!actionBar.getInventory().isDragged()) {
			// If it's open
			if (actionBar.getInventory().isVisible()) {				
//				If the Inventory isn't set to the default position we display this window directly
				if (actionBar.getInventory().getPosition().getX()!=actionBar.getInventory().getDefaultPosition().getX() ||
						actionBar.getInventory().getPosition().getY()!=actionBar.getInventory().getDefaultPosition().getY()) {
					if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) displayInventory=Inventory.INVENTORY_HEIGHT;
				}
				// Else we display it we a scroll effect
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT) {
					displayInventory = (displayInventory<Inventory.INVENTORY_HEIGHT) ? displayInventory+1 : displayInventory ;
					gl.glTranslated(actionBar.getInventory().getPosition().getX(),actionBar.getInventory().getPosition().getY()-displayInventory,0);
					gl.glCallList(inventory);
				}
				
				// When is finish, we display the Inventory's tools
				if (displayActionBar==ActionBar.ACTIONBARMAP_HEIGHT && displayInventory==Inventory.INVENTORY_HEIGHT) {
					if (actionBar.getInventory().getActiveBag()==Inventory.INVENTORY_BAG01) {
						ItemRep [][] tabBag = actionBar.getInventory().getBag();
						for (int i=0;i<Inventory.NB_BAGS;i++) {
						if (i<3) {
							gl.glTranslated(actionBar.getInventory().getPosition().getX()+(i*41)+25,actionBar.getInventory().getPosition().getY()-423,0);
							gl.glCallList(largeMagicAxe);
							txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+(i*41)+35,(int)actionBar.getInventory().getPosition().getY()-290,""+(i+1),0.0f,0.5f,0.5f);
						}
						else if (i>2 && i<6) {
							gl.glTranslated(actionBar.getInventory().getPosition().getX()+((i-3)*41)+25,actionBar.getInventory().getPosition().getY()-383,0);
							gl.glCallList(largeMagicAxe);
							txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+((i-3)*41)+35,(int)actionBar.getInventory().getPosition().getY()-330,""+(i+1),0.0f,0.5f,0.5f);			
						}
						else if (i>5) {
							gl.glTranslated(actionBar.getInventory().getPosition().getX()+((i-6)*41)+25,actionBar.getInventory().getPosition().getY()-343,0);
							gl.glCallList(largeMagicAxe);
							txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+((i-6)*41)+35,(int)actionBar.getInventory().getPosition().getY()-370,""+(i+1),0.0f,0.5f,0.5f);
						}
						
						}
						for (int i=0;i<Inventory.BAG_CONTENT;i++) {
							if(tabBag[actionBar.getInventory().getActiveBag()][i]!=null) {
								if (i<3)gl.glTranslated(actionBar.getInventory().getPosition().getX()+22,actionBar.getInventory().getPosition().getY()-413+i*39,0);
								else if (i>2 && i<6)gl.glTranslated(actionBar.getInventory().getPosition().getX()+71+(i-8)*40,actionBar.getInventory().getPosition().getY()-120,0);
								else if (i>5 && i<9)gl.glTranslated(actionBar.getInventory().getPosition().getX()+71+(i-8)*40,actionBar.getInventory().getPosition().getY()-120,0);
								else if (i>8 && i<12)gl.glTranslated(actionBar.getInventory().getPosition().getX()+71+(i-8)*40,actionBar.getInventory().getPosition().getY()-120,0);
								else if (i>11)gl.glTranslated(actionBar.getInventory().getPosition().getX()+201,actionBar.getInventory().getPosition().getY()-413+(i-11)*39,0);					
								switch (tabBag[actionBar.getInventory().getActiveBag()][i].getGui().getType()) {
									// TODO ...
								}
							}
						}
					
					}
					gl.glTranslated(actionBar.getInventory().getPosition().getX()+101,actionBar.getInventory().getPosition().getY()-477,0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getInventory().getPosition().getX()+119,actionBar.getInventory().getPosition().getY()-477,0);
					gl.glCallList(lockOn);
					gl.glTranslated(actionBar.getInventory().getPosition().getX()+137,actionBar.getInventory().getPosition().getY()-477,0);
					gl.glCallList(closeOn);	
				}
				// If it's not opened
			}else {
				if (displayActionBar>=ActionBar.ACTIONBARMAP_HEIGHT) {
					displayInventory = (displayInventory>70) ? displayInventory-1 : displayInventory;
					gl.glTranslated((screenWidth-1024)/2+661,screenHeight-displayInventory,0);
					gl.glCallList(inventory);
					if (displayInventory==70) {
						gl.glTranslated((screenWidth-1024)/2+592,screenHeight-65,0);
						gl.glCallList(upOn);
						gl.glTranslated((screenWidth-1024)/2+610,screenHeight-65,0);
						gl.glCallList(lockOn);
						gl.glTranslated((screenWidth-1024)/2+628,screenHeight-65,0);
						gl.glCallList(closeOn);	
					}
				}
			}
		}
		
		// Display the action bar with a scroll effect
		displayActionBar = (displayActionBar<ActionBar.ACTIONBARMAP_HEIGHT) ? displayActionBar+1 : displayActionBar;
		gl.glTranslated((screenWidth-1024)/2,screenHeight-displayActionBar,0);
		gl.glCallList(actionBarNum);
		
		// Display icons in the actionBar
		if (displayActionBar>=ActionBar.ACTIONBARMAP_HEIGHT) {
			int [] tabAction = actionBar.getTabAction();
			for (int i=0;i<tabAction.length;i++) {
				if(tabAction[i]!=0) {
					if (i<8)gl.glTranslated(((screenWidth-1024)/2)+i*33+86,screenHeight-35,0);
					else gl.glTranslated(((screenWidth-1024)/2)+i*33+97,screenHeight-35,0);
					switch (tabAction[i]) {
					case 5000: gl.glCallList(spellThunder);break;
					case 5001: gl.glCallList(spellThunder1);break;
					case 5002: gl.glCallList(spellThunder2);break;
					case 5003: gl.glCallList(spellThunder3);break;
					case 5100: gl.glCallList(spellIce);break;
					case 5101: gl.glCallList(spellIce1);break;
					case 5102: gl.glCallList(spellIce2);break;
					case 5103: gl.glCallList(spellIce3);break;
					case 5104: gl.glCallList(spellIce4);break;
					case 5200: gl.glCallList(spellFire);break;
					case 5201: gl.glCallList(spellFire1);break;
					case 5202: gl.glCallList(spellFire2);break;
					case 5203: gl.glCallList(spellFire3);break;
					case 5204: gl.glCallList(spellFire4);break;
					case 5300: gl.glCallList(spellEarth);break;
					case 5301: gl.glCallList(spellEarth1);break;
					case 5302: gl.glCallList(spellEarth2);break;
					case 5303: gl.glCallList(spellEarth3);break;
					case 5304: gl.glCallList(spellEarth4);break;
					case 5305: gl.glCallList(spellEarth5);break;
					case 5400: gl.glCallList(spellWind);break;
					case 5401: gl.glCallList(spellWind1);break;
					case 5402: gl.glCallList(spellWind2);break;
					case 5403: gl.glCallList(spellWind3);break;
					case 5404: gl.glCallList(spellWind4);break;
					case 5405: gl.glCallList(spellWind5);break;
					case 5406: gl.glCallList(spellWind6);break;
					// TODO ...
					default: break;
					}
				}
			}
		}
		
		if (actionBar.getChat().isVisible() && displayChat==Chat.CHAT_HEIGHT && !actionBar.getChat().isDragged()) {
			
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			
			if (DEV) {
			GraphicsEngine.window_width=1024;
			GraphicsEngine.window_height=768;
			}
					
			// The message wicht the player write
			txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+40,(int)(screenHeight-actionBar.getChat().getPosition().getY())+34,chatMsgTyped,1.0f,1.0f,1.0f);
			// The select cursor
			gl.glTranslated((int)actionBar.getChat().getPosition().getX()+45+(7*chatMsgTyped.length()),(int)actionBar.getChat().getPosition().getY()-49,0);
				
			if (actionBar.getChat().isTyping()) {
				
				// The select cursor flicked if we select the chat
				selectCursorFlick++;
				if (selectCursorFlick >=0 && selectCursorFlick < 20)	{
					gl.glCallList(selectCursor);
				}else if (selectCursorFlick >= 40){
					selectCursorFlick=0;
				}
			}			
			
			if (chatMsgSent) {				
				
				String login = getGameEngine().getHero().getName();
				
//				We display only the last senventh phrases
				switch (actionBar.getChat().getActiveChannel()) {
				
				case Chat.GENERAL_CHANNEL : 
					
					String [] tabGenMsg = actionBar.getChat().getMessage(Chat.GENERAL_CHANNEL);
					String GenMsg1="",GenMsg2="",GenMsg3="",GenMsg4="",GenMsg5="",GenMsg6="",GenMsg7="";
				    
					for (int i=0;i<tabGenMsg.length;i++) {
						if (tabGenMsg[i].compareTo("")==0) break;
						if (i>=6) GenMsg1 = login+" : "+tabGenMsg[i-6];
						if (i>=5) GenMsg2 = login+" : "+tabGenMsg[i-5];
						if (i>=4) GenMsg3 = login+" : "+tabGenMsg[i-4];
						if (i>=3) GenMsg4 = login+" : "+tabGenMsg[i-3];
						if (i>=2) GenMsg5 = login+" : "+tabGenMsg[i-2];
						if (i>=1) GenMsg6 = login+" : "+tabGenMsg[i-1];
						GenMsg7 = login+" : "+tabGenMsg[i];
					}
					if (GenMsg1.compareTo("")!=0) txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+194,GenMsg1,1.0f,0.0f,0.0f);
					if (GenMsg2.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+174,GenMsg2,1.0f,0.0f,0.0f);
					if (GenMsg3.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+154,GenMsg3,1.0f,0.0f,0.0f);
					if (GenMsg4.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+134,GenMsg4,1.0f,0.0f,0.0f);
					if (GenMsg5.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+114,GenMsg5,1.0f,0.0f,0.0f);
					if (GenMsg6.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+94,GenMsg6,1.0f,0.0f,0.0f);
					if (GenMsg7.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+74,GenMsg7,1.0f,0.0f,0.0f);
					break;
					
				case Chat.GUILD_CHANNEL :
					
					String [] tabGldMsg = actionBar.getChat().getMessage(Chat.GUILD_CHANNEL);
					String GldMsg1="",GldMsg2="",GldMsg3="",GldMsg4="",GldMsg5="",GldMsg6="",GldMsg7="";
					
					for (int i=0;i<tabGldMsg.length;i++) {
						if (tabGldMsg[i].compareTo("")==0) break;
						if (i>=6) GldMsg1 = login+" : "+tabGldMsg[i-6];
						if (i>=5) GldMsg2 = login+" : "+tabGldMsg[i-5];
						if (i>=4) GldMsg3 = login+" : "+tabGldMsg[i-4];
						if (i>=3) GldMsg4 = login+" : "+tabGldMsg[i-3];
						if (i>=2) GldMsg5 = login+" : "+tabGldMsg[i-2];
						if (i>=1) GldMsg6 = login+" : "+tabGldMsg[i-1];
						GldMsg7 = login+" : "+tabGldMsg[i];
					}
					if (GldMsg1.compareTo("")!=0) txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+194,GldMsg1,1.0f,1.0f,0.0f);
					if (GldMsg2.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+174,GldMsg2,1.0f,1.0f,0.0f);
					if (GldMsg3.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+154,GldMsg3,1.0f,1.0f,0.0f);
					if (GldMsg4.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+134,GldMsg4,1.0f,1.0f,0.0f);
					if (GldMsg5.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+114,GldMsg5,1.0f,1.0f,0.0f);
					if (GldMsg6.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+94,GldMsg6,1.0f,1.0f,0.0f);
					if (GldMsg7.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+74,GldMsg7,1.0f,1.0f,0.0f);
					break;
					
				case Chat.GROUP_CHANNEL :
					
					String [] tabGrpMsg = actionBar.getChat().getMessage(Chat.GROUP_CHANNEL);
					String GrpMsg1="",GrpMsg2="",GrpMsg3="",GrpMsg4="",GrpMsg5="",GrpMsg6="",GrpMsg7="";
					
					for (int i=0;i<tabGrpMsg.length;i++) {
						if (tabGrpMsg[i].compareTo("")==0) break;
						if (i>=6) GrpMsg1 = login+" : "+tabGrpMsg[i-6];
						if (i>=5) GrpMsg2 = login+" : "+tabGrpMsg[i-5];
						if (i>=4) GrpMsg3 = login+" : "+tabGrpMsg[i-4];
						if (i>=3) GrpMsg4 = login+" : "+tabGrpMsg[i-3];
						if (i>=2) GrpMsg5 = login+" : "+tabGrpMsg[i-2];
						if (i>=1) GrpMsg6 = login+" : "+tabGrpMsg[i-1];
						GrpMsg7 = login+" : "+tabGrpMsg[i];
					}
					if (GrpMsg1.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+194,GrpMsg1,0.0f,0.5f,1.0f);
					if (GrpMsg2.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+174,GrpMsg2,0.0f,0.5f,1.0f);
					if (GrpMsg3.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+154,GrpMsg3,0.0f,0.5f,1.0f);
					if (GrpMsg4.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+134,GrpMsg4,0.0f,0.5f,1.0f);
					if (GrpMsg5.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+114,GrpMsg5,0.0f,0.5f,1.0f);
					if (GrpMsg6.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+94,GrpMsg6,0.0f,0.5f,1.0f);
					if (GrpMsg7.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+74,GrpMsg7,0.0f,0.5f,1.0f);
					break;
					
				case Chat.PRIVATE_CHANNEL :
					
					String [] tabpvMsg = actionBar.getChat().getMessage(Chat.PRIVATE_CHANNEL);
					String PvMsg1="",PvMsg2="",PvMsg3="",PvMsg4="",PvMsg5="",PvMsg6="",PvMsg7="";
					
					for (int i=0;i<tabpvMsg.length;i++) {
						if (tabpvMsg[i].compareTo("")==0) break;
						if (i>=6) PvMsg1 = login+" : "+tabpvMsg[i-6];
						if (i>=5) PvMsg2 = login+" : "+tabpvMsg[i-5];
						if (i>=4) PvMsg3 = login+" : "+tabpvMsg[i-4];
						if (i>=3) PvMsg4 = login+" : "+tabpvMsg[i-3];
						if (i>=2) PvMsg5 = login+" : "+tabpvMsg[i-2];
						if (i>=1) PvMsg6 = login+" : "+tabpvMsg[i-1];
						PvMsg7 = login+" : "+tabpvMsg[i];
					}
					if (PvMsg1.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+194,PvMsg1,1.0f,0.0f,1.0f);
					if (PvMsg2.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+174,PvMsg2,1.0f,0.0f,1.0f);
					if (PvMsg3.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+154,PvMsg3,1.0f,0.0f,1.0f);
					if (PvMsg4.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+134,PvMsg4,1.0f,0.0f,1.0f);
					if (PvMsg5.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+114,PvMsg5,1.0f,0.0f,1.0f);
					if (PvMsg6.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+94,PvMsg6,1.0f,0.0f,1.0f);
					if (PvMsg7.compareTo("")!=0)txt.glPrintc(gl,(int)actionBar.getChat().getPosition().getX()+20,(int)(screenHeight-actionBar.getChat().getPosition().getY())+74,PvMsg7,1.0f,0.0f,1.0f);
					break;
				}				
			}			
			gl.glPopAttrib();
		}
		
		if (actionBar.getEquipement().isVisible() && displayEquipement==Equipement.EQUIPEMENT_HEIGHT && !actionBar.getEquipement().isDragged()) {
			
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			
			if (DEV) {
				GraphicsEngine.window_width=1024;
				GraphicsEngine.window_height=768;
				}
			
			txt.glPrintc(gl,(int)actionBar.getEquipement().getPosition().getX()+20,(int)(screenHeight-actionBar.getEquipement().getPosition().getY())+60,"0 Era    12 Gold    05 Copper",1.0f,0.5f,1.0f);
			
			gl.glPopAttrib();
		}
		
if (actionBar.getInventory().isVisible() && displayInventory==Inventory.INVENTORY_HEIGHT && !actionBar.getInventory().isDragged()) {
			
			gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
			
			if (DEV) {
				GraphicsEngine.window_width=1024;
				GraphicsEngine.window_height=768;
				}
			
			//txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+20,(int)(screenHeight-actionBar.getInventory().getPosition().getY())+60,"0 Era    12 Gold    05 Copper",1.0f,0.5f,1.0f);
			
			gl.glPopAttrib();
		}

		
		// We test if the window is always in the screen
		if (actionBar.getChat().isDragged()) {
			int x= (int)(cursorPos.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX());
			int y= (int)(cursorPos.getY()-actionBar.getChat().getDragPoint().getY()+(actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT));
			if (x<=0 || y<=0 || x+Chat.CHAT_WIDTH>=screenWidth || y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
				// if it's exited on the left
				if (x<=0) {
					// Top corner
					if (y<=0) gl.glTranslated(0,0,0);
					// Bottom corner
					else if (y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) gl.glTranslated(0,screenHeight-Chat.CHAT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(0,cursorPos.getY()-actionBar.getChat().getDragPoint().getY()+(actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT),0);
					gl.glCallList(chat);
					// if it's exited on the right				
				}else if (x+Chat.CHAT_WIDTH>=screenWidth) {
					// Top corner
					if (y<=0) gl.glTranslated(screenWidth-Chat.CHAT_WIDTH,0,0);
					// Bottom corner
					else if (y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight)	gl.glTranslated(screenWidth-Chat.CHAT_WIDTH,screenHeight-Chat.CHAT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(screenWidth-Chat.CHAT_WIDTH,cursorPos.getY()-actionBar.getChat().getDragPoint().getY()+(actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT),0);
					gl.glCallList(chat);
					// if it's exited on the top	
				}else if (y<=0) {
					gl.glTranslated(cursorPos.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX(),0,0);
					gl.glCallList(chat);
					// if it's exited on the bottom
				}else if (y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
					gl.glTranslated(cursorPos.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX(),screenHeight-Chat.CHAT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					gl.glCallList(chat);
				}
			}else {
				gl.glTranslated(cursorPos.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX(),cursorPos.getY()-actionBar.getChat().getDragPoint().getY()+actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT,0);
				gl.glCallList(chat);
			}
		}else if (actionBar.getEquipement().isDragged()) {
			
			int x= (int)(cursorPos.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX());
			int y= (int)(cursorPos.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT));
			
			if (x<=0 || y<=0 || x+Equipement.EQUIPEMENT_WIDTH>=screenWidth || y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
				// if it's exited on the left
				if (x<=0) {
					// Top corner
					if (y<=0) gl.glTranslated(0,0,0);
					// Bottom corner
					else if (y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight)	gl.glTranslated(0,screenHeight-Equipement.EQUIPEMENT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(0,cursorPos.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT),0);
					gl.glCallList(equipement);
					// if it's exited on the right				
				}else if (x+Equipement.EQUIPEMENT_WIDTH>=screenWidth) {
					// Top corner
					if (y<=0) gl.glTranslated(screenWidth-Equipement.EQUIPEMENT_WIDTH,0,0);
					// Bottom corner
					else if (y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight)	gl.glTranslated(screenWidth-Equipement.EQUIPEMENT_WIDTH,screenHeight-Equipement.EQUIPEMENT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(screenWidth-Equipement.EQUIPEMENT_WIDTH,cursorPos.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT),0);
					gl.glCallList(equipement);
					// if it's exited on the top	
				}else if (y<=0) {
					gl.glTranslated(cursorPos.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX(),0,0);
					gl.glCallList(equipement);
					// if it's exited on the bottom
				}else if (y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
					gl.glTranslated(cursorPos.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX(),screenHeight-Equipement.EQUIPEMENT_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					gl.glCallList(equipement);				
				}
			}else {
				gl.glTranslated(cursorPos.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX(),cursorPos.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT),0);
				gl.glCallList(equipement);
			}
		}else if (actionBar.getInventory().isDragged()) {
			
			int x= (int)(cursorPos.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX());
			int y= (int)(cursorPos.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT));
			
			if (x<=0 || y<=0 || x+Inventory.INVENTORY_WIDTH>=screenWidth || y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
				// if it's exited on the left
				if (x<=0) {
					// Top corner
					if (y<=0) gl.glTranslated(0,0,0);
					// Bottom corner
					else if (y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight)	gl.glTranslated(0,screenHeight-Inventory.INVENTORY_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(0,cursorPos.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT),0);
					gl.glCallList(inventory);
					// if it's exited on the right				
				}else if (x+Inventory.INVENTORY_WIDTH>=screenWidth) {
					// Top corner
					if (y<=0) gl.glTranslated(screenWidth-Inventory.INVENTORY_WIDTH,0,0);
					// Bottom corner
					else if (y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight)	gl.glTranslated(screenWidth-Inventory.INVENTORY_WIDTH,screenHeight-Inventory.INVENTORY_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					// No corner
					else gl.glTranslated(screenWidth-Inventory.INVENTORY_WIDTH,cursorPos.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT),0);
					gl.glCallList(inventory);
					// if it's exited on the top	
				}else if (y<=0) {
					gl.glTranslated(cursorPos.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX(),0,0);
					gl.glCallList(inventory);
					// if it's exited on the bottom
				}else if (y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=screenHeight) {
					gl.glTranslated(cursorPos.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX(),screenHeight-Inventory.INVENTORY_HEIGHT-ActionBar.ACTIONBAR_HEIGHT,0);
					gl.glCallList(inventory);				
				}
			}else {
				gl.glTranslated(cursorPos.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX(),cursorPos.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT),0);
				gl.glCallList(inventory);
			}
		}
		
			
		//****************************************************************\\
		//************************* D E B U G ****************************\\
		//************************* S T A R T ****************************\\
		//****************************************************************\\
		
		if (debugMode) {
		debugRectangle(gl,actionBar.getActionBarSite(),0.0f,0.0f,1.0f);		
		debugCircle(gl,actionBar.getActionMapSite(),0.0f,0.0f,1.0f);		
		debugRectangle(gl,actionBar.getCase1Site(),0.0f,0.0f,1.0f);		
		debugRectangle(gl,actionBar.getCase2Site(),0.0f,0.0f,1.0f);		
		debugRectangle(gl,actionBar.getAction1Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction2Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction3Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction4Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction5Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction6Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction7Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction8Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction9Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction10Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction11Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction12Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction13Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction14Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction15Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction16Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction17Site(),0.0f,0.0f,1.0f);
		debugRectangle(gl,actionBar.getAction18Site(),0.0f,0.0f,1.0f);
		
	
		if (actionBar.getChat().isVisible()) {
			debugRectangle(gl,actionBar.getChat().getChatOpenSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGeneralOpenChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGuildOpenChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGroupOpenChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getPrivateOpenChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getUpOpenSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getLockOpenSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getCloseOpenSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getEditableOpenSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getEmotesOpenSite(),1.0f,0.0f,0.0f);
		}else {
			debugRectangle(gl,actionBar.getChat().getChatCloseSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGeneralCloseChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGuildCloseChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getGroupCloseChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getPrivateCloseChannelSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getUpCloseSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getLockCloseSite(),1.0f,0.0f,0.0f);
			debugRectangle(gl,actionBar.getChat().getCloseCloseSite(),1.0f,0.0f,0.0f);
		}
		if (actionBar.getEquipement().isVisible()) {
			debugRectangle(gl,actionBar.getEquipement().getEquipementOpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet1OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet2OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getUpOpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getLockOpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getCloseOpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet01OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet02OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet03OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet04OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet05OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet06OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet07OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet08OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet09OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet10OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet11OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet12OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet13OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet14OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet15OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet16OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet17OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet18OpenSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getSet19OpenSite(),0.0f,1.0f,0.0f);
		}else {
			debugRectangle(gl,actionBar.getEquipement().getEquipementCloseSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getUpCloseSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getLockCloseSite(),0.0f,1.0f,0.0f);
			debugRectangle(gl,actionBar.getEquipement().getCloseCloseSite(),0.0f,1.0f,0.0f);
		}
		if (actionBar.getInventory().isVisible()) {
			debugRectangle(gl,actionBar.getInventory().getInventoryOpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getUpOpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getLockOpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getCloseOpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag01OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag02OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag03OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag04OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag05OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag06OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag07OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag08OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getBag09OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent01OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent02OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent03OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent04OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent05OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent06OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent07OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent08OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent09OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent10OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent11OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent12OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent13OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent14OpenSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getContent15OpenSite(),1.0f,0.0f,1.0f);
		}else {
			debugRectangle(gl,actionBar.getInventory().getInventoryCloseSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getUpCloseSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getLockCloseSite(),1.0f,0.0f,1.0f);
			debugRectangle(gl,actionBar.getInventory().getCloseCloseSite(),1.0f,0.0f,1.0f);
			
		}

		}
		//****************************************************************\\
		//************************* D E B U G ****************************\\
		//*************************** E N D ******************************\\
		//****************************************************************\\
		
		
		/* ----- INVENTORY ----- */
		
		/*if (inventoryOn) {
		 
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
		      */
		
		
		
		// If a item is select, this item will follow the cursor
		if (actionSelected) {	
			// TODO un ptit bug des couleurs
			gl.glColor3f(1.0f,0.0f,0.0f);
			switch (actionSelect) {		
			
			case 5001:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellThunder1);break;
			case 5101:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellIce1);break;
			case 5201:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellFire1);break;
			case 5202:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellFire2);break;
			case 5305:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellEarth1);break;
			case 5401:gl.glTranslated(cursorPos.getX()+10,cursorPos.getY()+10,0);gl.glCallList(spellWind1);break;
			default :break;
			}
		}	
		
		
		debugAction(gl,action);
		
		gl.glPopAttrib();
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		
		if (DEV)canvas.repaint();
		
	}
	
	/**
	 * updateInventory fonction
	 * @param inveTab the new Inventory
	 */
	
	public void updateInventory(ItemRep[][] inveTab) {
		
		//inventory.update(inveTab);
	}
	
	/**
	 * updateBelt fonction
	 * @param beltTab the new belt
	 */
	
	public void updateBelt(ItemRep[] beltTab) {
		
		//belt.updateBelt(beltTab);
	}
	
	
	public void setActionSelected(int id) {
		
		 if (!actionSelected) {
			 actionSelected = true ;
			 int [] tabAction = actionBar.getTabAction();
			 actionSelect = tabAction[id];
			 actionBar.deleteAction(id);
		 }
	}
	
	public void setActionNotSelected(int id) {
		
		 if (actionSelected) {
			 actionSelected = false ;
			 int [] tabAction = actionBar.getTabAction();
			 if (tabAction[id]==0) {
				 actionBar.addAction(actionSelect,id);
			 }else {	
				 int actionSelectTmp = actionSelect;
				 actionSelect = tabAction[id];
				 actionSelected = true ;
				 actionBar.deleteAction(id);
				 actionBar.addAction(actionSelectTmp,id);
				 
			 }
		 }
	}
	
	/**
	 * updateCharacter fonction
	 * @param characterTab the new character items
	 */
	
	public void updateCharacter(ItemRep[] characterTab) {
		
		//inventory.updateCharacter(characterTab);
	}
	
	// The pourcentage of life,mana and experience
	private double pourcentageLife,pourcentageMana,pourcentageLevel;
	
	// The time before the regeneration of the life or the mana
	private int lifeRegeneration=0,manaRegeneration=0;
	
	/**
	 * updateStatistics fonction
	 * Get the right pourcentage of life,mana and experience
	 */
	/*
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
	 }*/
	
	// Information about the hero'characteristics
	private String name,strength,dexterity,mind,damageMinTotal,damageMaxTotal,armor,
	physicalResist,fireResist,iceResist,thunderResist;
	
	/**
	 * updateCharacteristics fonction
	 * Get the right characteristics of the hero
	 * Use the gameEngine
	 */
	
	public void updateCharacteristics() {
		/*
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
		 */
	}
	
	/**
	 * updateLife fonction
	 * @param life the new life state
	 * Change the Life bar
	 */
	
	public void updateLife(int life) {/*
	getGameEngine().getHero().addLife(life);	
	double l = (double)(getGameEngine().getHero().getLife())/getGameEngine().getHero().getLifeMax();
	if (l>0 && l<=1 ){			
	statistics.setPourcentLife(l);
	}else if (l<=0) {
	statistics.setPourcentLife(0);
	}else if (l>1) {
	statistics.setPourcentLife(1);
	}*/
	}
	
	/**
	 * updateMana fonction
	 * @param mana the new mana state
	 * Change the Mana bar
	 */
	
	public void updateMana(int mana) {/*
	getGameEngine().getHero().addMana(mana);
	double m = (double)(mana+getGameEngine().getHero().getMana())/getGameEngine().getHero().getManaMax();
	if (m>0 && m<=1){
	statistics.setPourcentMana(m);
	}else if (m<=0) {
	statistics.setPourcentMana(0);
	}else if (m>1) {
	statistics.setPourcentMana(1);
	}*/
	}
	
	/**
	 * updateLevel fonction
	 * @param level the new level state
	 * Change the Level bar
	 */
	
	public void updateLevel(double lvl) {
		//statistics.setLevel(lvl);
	}
	
	

	
	
	
	/**
	 * deleteItemSelect
	 * Delete the item Select (drop)
	 */
	/*
	public void deleteItemSelect() {
		itemSelect = null;
		itemSelectTmp = null;
		itemSelected = false ;
	}*/
	
	/**
	 * setItemSelectInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Select an item in the Inventory
	 */
	
	public void setItemSelectInventory(int i, int j) {
		/*
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
		   }	*/
	}
	
	/**
	 * setItemSelectBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Select a potion in the belt
	 */
	
	public void setItemSelectActiofdgfdnBar(int i) {
		
		 if (!itemSelected) {
		 itemSelected = true ;
		 int [] tabAction = actionBar.getTabAction();
		 
		 itemSelect = tabAction[i];
		 
		  }
	}
	
	/**
	 * setItemSelectCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Select an item on the character
	 */
	
	public void setItemSelectCharacter(int i) {
		/*
		 if (!itemSelected) {
		 itemSelected = true ;
		 ItemRep[] characterTab  = inventory.getItemOnCharacter();
		 
		 itemSelect = characterTab[i];
		 
		 // Set null because we picked the item
		  characterTab[i]=null;
		  
		  // Update Character
		   updateCharacter(characterTab);
		   }*/
	}
	
	/**
	 * setItemSelectChangeInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Switch between to items in the Inventory
	 */
	
	public void setItemSelectChangeInventory(int i, int j) {
		/*
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
		       }*/
	}
	
	/**
	 * setItemSelectChangeCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Switch between to items on the character
	 */
	
	public void setItemSelectChangeCharacter(int i) {
		/*
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
		      }*/
	}
	
	/**
	 * setItemSelectChangeBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Switch between to items in the Inventory
	 */
	
	public void setItemSelectChangeBelt(int i) {
		/*
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
		      }*/
	}
	
	/**
	 * toInventory fonction
	 * @param i the X-coordonate of the top left case of the item
	 * @param j the Y-coordonate of the top left case of the item
	 * Item back to Inventory
	 */
	
	public void toInventory(int i, int j) {
		/*
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
		  }	*/
	}
	
	/**
	 * toBelt fonction
	 * @param i the case of the belt (0 to 3)
	 * Item back to belt
	 */
	
	public void toBelt(int i) {
		/*
		 if (itemSelected) {
		 itemSelected = false;
		 
		 ItemRep[] beltTab = belt.getItemBelt();
		 beltTab[i] = itemSelect;
		 
		 // Update Belt
		  updateBelt(beltTab);
		  }*/
		
	}
	
	/**
	 * toCharacter fonction
	 * @param i the case of the item Character (0 to 9)
	 * Item back to Character
	 */
	
	public void toCharacter(int i) {
		/*
		 if (itemSelected) {
		 itemSelected = false;
		 
		 ItemRep[] characterTab = inventory.getItemOnCharacter();
		 characterTab[i] = itemSelect;
		 
		 // Update Character
		  updateCharacter(characterTab);
		  }*/
		
	}
	
	/**
	 * weaponsStatesChange fonction
	 * @param type the type of the fisrt weapons
	 * Change icon associated with the weapon of the character
	 */
	
	public void weaponsStatesChange(int type) {
		/*
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
		  updateWeaponsStates(weaponsStatesTab);		*/
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
	 * 
	 * @param rec
	 */
	public void debugRectangle(GL gl, Rectangle rec, float c1, float c2, float c3) {
		
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glColor3f(c1,c2,c3);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(rec.getX(),rec.getY());
		gl.glVertex2d(rec.getX(),rec.getY()+rec.getHeight());
		gl.glVertex2d(rec.getX()+rec.getWidth(),rec.getY()+rec.getHeight());
		gl.glVertex2d(rec.getX()+rec.getWidth(),rec.getY());
		gl.glEnd();
		gl.glPopMatrix();
		
		
	}
	
	public void debugCircle(GL gl, Circle c, float c1, float c2, float c3) {
		
		double DEG2RAD = 3.14159/180;
		
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glColor3f(c1,c2,c3);
		
		gl.glBegin(GL.GL_LINE_LOOP);
		for (int i=0; i < 360; i++)   {
			gl.glVertex2d(c.getX()+Math.cos(i*DEG2RAD)*c.getRadius(),c.getY()+Math.sin(i*DEG2RAD)*c.getRadius());
		}
		gl.glEnd();
		gl.glPopMatrix();
		
	}
	
	public void debugAction(GL gl, String action) {
		
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		txt.setFontSize(30,gl);		

		cptDebugAction++;
		
		if (cptDebugAction<=400) {
		// The message wicht the player write
		txt.glPrintc(gl,400,400,action,1.0f,0.0f,0.0f);
		}else {
			cptDebugAction=0;
			this.action="";
		}
		
		gl.glEnd();
		gl.glPopMatrix();
		
	}
	/**
	 * buildLists fonction
	 * @param gLDrawable the GLDrawable object
	 * Build the display list (faster)
	 */
	
	public void buildLists(GLDrawable gLDrawable){
		
		System.out.println("Build display lists...");
		int cpt=0;
		
		GL gl = gLDrawable.getGL();
		
		actionBarNum = gl.glGenLists(NBTEXTURES+1);
		gl.glNewList(actionBarNum,GL.GL_COMPILE); 
		draw(gLDrawable,1024,256,cpt++); 
		gl.glEndList();
		
		cpt++; // 1
		
		equipement = actionBarNum+1;
		gl.glNewList(equipement,GL.GL_COMPILE); 
		draw(gLDrawable,256,512,cpt++);
		gl.glEndList();
		
		characteristic = equipement+1;
		gl.glNewList(characteristic,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		inventory = characteristic+1;
		gl.glNewList(inventory,GL.GL_COMPILE); 
		draw(gLDrawable,256,512,cpt++);
		gl.glEndList();
		
		chat = inventory+1;
		gl.glNewList(chat,GL.GL_COMPILE); 
		draw(gLDrawable,512,256,cpt++); // 5
		gl.glEndList();
		
		smallTraditionalPotion = chat+1;           
		gl.glNewList(smallTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalPotion = smallTraditionalPotion+1;
		gl.glNewList(mediumTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalPotion = mediumTraditionalPotion+1;
		gl.glNewList(largeTraditionalPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicPotion = largeTraditionalPotion +1;
		gl.glNewList(smallMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicPotion = smallMagicPotion+1;
		gl.glNewList(mediumMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 10
		gl.glEndList();
		
		largeMagicPotion  = mediumMagicPotion +1;
		gl.glNewList(largeMagicPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questPotion  = largeMagicPotion +1;
		gl.glNewList(questPotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rarePotion = questPotion+1;
		gl.glNewList(rarePotion ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singlePotion= rarePotion+1;
		gl.glNewList(singlePotion,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalAmulet = singlePotion+1;           
		gl.glNewList(smallTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 15
		gl.glEndList();
		
		mediumTraditionalAmulet = smallTraditionalAmulet+1;
		gl.glNewList(mediumTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalAmulet = mediumTraditionalAmulet+1;
		gl.glNewList(largeTraditionalAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicAmulet = largeTraditionalAmulet +1;
		gl.glNewList(smallMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicAmulet = smallMagicAmulet+1;
		gl.glNewList(mediumMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicAmulet  = mediumMagicAmulet +1;
		gl.glNewList(largeMagicAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 20
		gl.glEndList();
		
		questAmulet  = largeMagicAmulet +1;
		gl.glNewList(questAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareAmulet = questAmulet+1;
		gl.glNewList(rareAmulet ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleAmulet= rareAmulet+1;
		gl.glNewList(singleAmulet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalBracelet = singleAmulet+1;        
		gl.glNewList(smallTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalBracelet = smallTraditionalBracelet+1;
		gl.glNewList(mediumTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 25
		gl.glEndList();
		
		largeTraditionalBracelet = mediumTraditionalBracelet+1;
		gl.glNewList(largeTraditionalBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicBracelet = largeTraditionalBracelet +1;
		gl.glNewList(smallMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicBracelet = smallMagicBracelet+1;
		gl.glNewList(mediumMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicBracelet  = mediumMagicBracelet +1;
		gl.glNewList(largeMagicBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questBracelet  = largeMagicBracelet +1;
		gl.glNewList(questBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 30
		gl.glEndList();
		
		rareBracelet = questBracelet+1;
		gl.glNewList(rareBracelet ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleBracelet= rareBracelet+1;
		gl.glNewList(singleBracelet,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalRing = singleBracelet+1;           
		gl.glNewList(smallTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalRing = smallTraditionalRing+1;
		gl.glNewList(mediumTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalRing = mediumTraditionalRing+1;
		gl.glNewList(largeTraditionalRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 35
		gl.glEndList();
		
		smallMagicRing = largeTraditionalRing +1;
		gl.glNewList(smallMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicRing = smallMagicRing+1;
		gl.glNewList(mediumMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicRing  = mediumMagicRing +1;
		gl.glNewList(largeMagicRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questRing  = largeMagicRing +1;
		gl.glNewList(questRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareRing = questRing+1;
		gl.glNewList(rareRing ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 40
		gl.glEndList();
		
		singleRing= rareRing+1;
		gl.glNewList(singleRing,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalBuckle = singleRing+1;           	
		gl.glNewList(smallTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalBuckle = smallTraditionalBuckle+1;
		gl.glNewList(mediumTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalBuckle = mediumTraditionalBuckle+1;
		gl.glNewList(largeTraditionalBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicBuckle = largeTraditionalBuckle +1;
		gl.glNewList(smallMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 45
		gl.glEndList();
		
		mediumMagicBuckle = smallMagicBuckle+1;
		gl.glNewList(mediumMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicBuckle  = mediumMagicBuckle +1;
		gl.glNewList(largeMagicBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questBuckle  = largeMagicBuckle +1;
		gl.glNewList(questBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareBuckle = questBuckle+1;
		gl.glNewList(rareBuckle ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleBuckle= rareBuckle+1;
		gl.glNewList(singleBuckle,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 50
		gl.glEndList();
		
		smallTraditionalHelm = singleBuckle+1;           
		gl.glNewList(smallTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalHelm = smallTraditionalHelm+1;
		gl.glNewList(mediumTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalHelm = mediumTraditionalHelm+1;
		gl.glNewList(largeTraditionalHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicHelm = largeTraditionalHelm +1;
		gl.glNewList(smallMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicHelm = smallMagicHelm+1;
		gl.glNewList(mediumMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 55
		gl.glEndList();
		
		largeMagicHelm  = mediumMagicHelm +1;
		gl.glNewList(largeMagicHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questHelm  = largeMagicHelm +1;
		gl.glNewList(questHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareHelm = questHelm+1;
		gl.glNewList(rareHelm ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleHelm= rareHelm+1;
		gl.glNewList(singleHelm,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalWrist = singleHelm+1;           	
		gl.glNewList(smallTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 60
		gl.glEndList();
		
		mediumTraditionalWrist = smallTraditionalWrist+1;
		gl.glNewList(mediumTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalWrist = mediumTraditionalWrist+1;
		gl.glNewList(largeTraditionalWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicWrist = largeTraditionalWrist +1;
		gl.glNewList(smallMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicWrist = smallMagicWrist+1;
		gl.glNewList(mediumMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicWrist  = mediumMagicWrist +1;
		gl.glNewList(largeMagicWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 65
		gl.glEndList();
		
		questWrist  = largeMagicWrist +1;
		gl.glNewList(questWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareWrist = questWrist+1;
		gl.glNewList(rareWrist ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleWrist= rareWrist+1;
		gl.glNewList(singleWrist,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();   
		
		smallTraditionalArmor = singleWrist+1;
		gl.glNewList(smallTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalArmor = smallTraditionalArmor+1;
		gl.glNewList(mediumTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 70
		gl.glEndList();
		
		largeTraditionalArmor = mediumTraditionalArmor+1;
		gl.glNewList(largeTraditionalArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicArmor = largeTraditionalArmor +1;
		gl.glNewList(smallMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicArmor = smallMagicArmor+1;
		gl.glNewList(mediumMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicArmor  = mediumMagicArmor +1;
		gl.glNewList(largeMagicArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questArmor  = largeMagicArmor +1;
		gl.glNewList(questArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 75
		gl.glEndList();
		
		rareArmor = questArmor+1;
		gl.glNewList(rareArmor ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleArmor= rareArmor+1;
		gl.glNewList(singleArmor,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalGlove = singleArmor+1;           
		gl.glNewList(smallTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalGlove = smallTraditionalGlove+1;
		gl.glNewList(mediumTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalGlove = mediumTraditionalGlove+1;
		gl.glNewList(largeTraditionalGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 80
		gl.glEndList();
		
		smallMagicGlove = largeTraditionalGlove +1;
		gl.glNewList(smallMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicGlove = smallMagicGlove+1;
		gl.glNewList(mediumMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicGlove  = mediumMagicGlove +1;
		gl.glNewList(largeMagicGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questGlove  = largeMagicGlove +1;
		gl.glNewList(questGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareGlove = questGlove+1;
		gl.glNewList(rareGlove ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 85
		gl.glEndList();
		
		singleGlove= rareGlove+1;
		gl.glNewList(singleGlove,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalBelt = singleGlove+1;           
		gl.glNewList(smallTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalBelt = smallTraditionalBelt+1;
		gl.glNewList(mediumTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalBelt = mediumTraditionalBelt+1;
		gl.glNewList(largeTraditionalBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicBelt = largeTraditionalBelt +1;
		gl.glNewList(smallMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 90
		gl.glEndList();
		
		mediumMagicBelt = smallMagicBelt+1;
		gl.glNewList(mediumMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicBelt  = mediumMagicBelt +1;
		gl.glNewList(largeMagicBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questBelt  = largeMagicBelt +1;
		gl.glNewList(questBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareBelt = questBelt+1;
		gl.glNewList(rareBelt ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleBelt= rareBelt+1;
		gl.glNewList(singleBelt,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 95
		gl.glEndList();
		
		smallTraditionalTrousers = singleBelt +1;           
		gl.glNewList(smallTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalTrousers = smallTraditionalTrousers+1;
		gl.glNewList(mediumTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalTrousers = mediumTraditionalTrousers+1;
		gl.glNewList(largeTraditionalTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicTrousers = largeTraditionalTrousers +1;
		gl.glNewList(smallMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicTrousers = smallMagicTrousers+1;
		gl.glNewList(mediumMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 100
		gl.glEndList();
		
		largeMagicTrousers  = mediumMagicTrousers +1;
		gl.glNewList(largeMagicTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questTrousers  = largeMagicTrousers +1;
		gl.glNewList(questTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareTrousers = questTrousers+1;
		gl.glNewList(rareTrousers ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleTrousers= rareTrousers+1;
		gl.glNewList(singleTrousers,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalTibia = singleTrousers+1;           
		gl.glNewList(smallTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 105
		gl.glEndList();
		
		mediumTraditionalTibia = smallTraditionalTibia+1;
		gl.glNewList(mediumTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalTibia = mediumTraditionalTibia+1;
		gl.glNewList(largeTraditionalTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicTibia = largeTraditionalTibia +1;
		gl.glNewList(smallMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicTibia = smallMagicTibia+1;
		gl.glNewList(mediumMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicTibia  = mediumMagicTibia +1;
		gl.glNewList(largeMagicTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 110
		gl.glEndList();
		
		questTibia  = largeMagicTibia +1;
		gl.glNewList(questTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareTibia = questTibia+1;
		gl.glNewList(rareTibia ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleTibia= rareTibia+1;
		gl.glNewList(singleTibia,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalBoot = singleTibia+1;           
		gl.glNewList(smallTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalBoot = smallTraditionalBoot+1;
		gl.glNewList(mediumTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 115
		gl.glEndList();
		
		largeTraditionalBoot = mediumTraditionalBoot+1;
		gl.glNewList(largeTraditionalBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicBoot = largeTraditionalBoot +1;
		gl.glNewList(smallMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicBoot = smallMagicBoot+1;
		gl.glNewList(mediumMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicBoot = mediumMagicBoot +1;
		gl.glNewList(largeMagicBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questBoot  = largeMagicBoot +1;
		gl.glNewList(questBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 120
		gl.glEndList();
		
		rareBoot = questBoot+1;
		gl.glNewList(rareBoot ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleBoot= rareBoot+1;
		gl.glNewList(singleBoot,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalShield = gl.glGenLists(NBTEXTURES);           
		gl.glNewList(smallTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalShield = smallTraditionalShield+1;
		gl.glNewList(mediumTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalShield = mediumTraditionalShield+1;
		gl.glNewList(largeTraditionalShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 125
		gl.glEndList();
		
		smallMagicShield = largeTraditionalShield +1;
		gl.glNewList(smallMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicShield = smallMagicShield+1;
		gl.glNewList(mediumMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicShield = mediumMagicShield +1;
		gl.glNewList(largeMagicShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questShield = largeMagicShield +1;
		gl.glNewList(questShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareShield = questShield+1;
		gl.glNewList(rareShield ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 130
		gl.glEndList();
		
		singleShield= rareShield+1;
		gl.glNewList(singleShield,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalSword = singleShield+1;           
		gl.glNewList(smallTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalSword = smallTraditionalSword+1;
		gl.glNewList(mediumTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalSword = mediumTraditionalSword+1;
		gl.glNewList(largeTraditionalSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicSword=largeTraditionalSword +1;
		gl.glNewList(smallMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 135
		gl.glEndList();
		
		mediumMagicSword = smallMagicSword+1;
		gl.glNewList(mediumMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		// 137 TODO
		cpt++;
		
		largeMagicSword  = mediumMagicSword +1;
		gl.glNewList(largeMagicSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questSword  = largeMagicSword +1;
		gl.glNewList(questSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareSword = questSword+1;
		gl.glNewList(rareSword ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 140
		gl.glEndList();
		
		singleSword= rareSword+1;
		gl.glNewList(singleSword,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalAxe = singleSword+1;           
		gl.glNewList(smallTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalAxe = smallTraditionalAxe+1;
		gl.glNewList(mediumTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalAxe = mediumTraditionalAxe+1;
		gl.glNewList(largeTraditionalAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicAxe = largeTraditionalAxe +1;
		gl.glNewList(smallMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 145
		gl.glEndList();
		
		mediumMagicAxe = smallMagicAxe+1;
		gl.glNewList(mediumMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicAxe  = mediumMagicAxe +1;
		gl.glNewList(largeMagicAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questAxe = largeMagicAxe +1;
		gl.glNewList(questAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareAxe = questAxe+1;
		gl.glNewList(rareAxe ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleAxe= rareAxe+1;
		gl.glNewList(singleAxe,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 150
		gl.glEndList();
		
		smallTraditionalStick = singleAxe+1;           
		gl.glNewList(smallTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumTraditionalStick = smallTraditionalStick+1;
		gl.glNewList(mediumTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalStick = mediumTraditionalStick+1;
		gl.glNewList(largeTraditionalStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicStick = largeTraditionalStick +1;
		gl.glNewList(smallMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicStick = smallMagicStick+1;
		gl.glNewList(mediumMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 155
		gl.glEndList();
		
		largeMagicStick  = mediumMagicStick +1;
		gl.glNewList(largeMagicStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		questStick  = largeMagicStick +1;
		gl.glNewList(questStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareStick = questStick+1;
		gl.glNewList(rareStick ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleStick= rareStick+1;
		gl.glNewList(singleStick,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallTraditionalArc = singleStick+1;           
		gl.glNewList(smallTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 160
		gl.glEndList();
		
		mediumTraditionalArc = smallTraditionalArc+1;
		gl.glNewList(mediumTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeTraditionalArc = mediumTraditionalArc+1;
		gl.glNewList(largeTraditionalArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		smallMagicArc = largeTraditionalArc +1;
		gl.glNewList(smallMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		mediumMagicArc = smallMagicArc+1;
		gl.glNewList(mediumMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		largeMagicArc  = mediumMagicArc +1;
		gl.glNewList(largeMagicArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 165
		gl.glEndList();
		
		questArc  = largeMagicArc +1;
		gl.glNewList(questArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		rareArc = questArc+1;
		gl.glNewList(rareArc ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		singleArc= rareArc+1;
		gl.glNewList(singleArc,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		closeOn = singleArc+1;
		gl.glNewList(closeOn,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		closeOut = closeOn+1;
		gl.glNewList(closeOut,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++); // 170
		gl.glEndList();
		
		lockOn = closeOut+1;
		gl.glNewList(lockOn,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		lockOut = lockOn+1;
		gl.glNewList(lockOut,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		upOn = lockOut+1;
		gl.glNewList(upOn,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		upOut = upOn+1;
		gl.glNewList(upOut,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		downOn = upOut+1;
		gl.glNewList(downOn,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++); // 175
		gl.glEndList();
		
		downOut = downOn+1;
		gl.glNewList(downOut,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		emoteOn = downOut+1;
		gl.glNewList(emoteOn,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		emoteOut = emoteOn+1;
		gl.glNewList(emoteOut,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		channelOn = emoteOut+1;
		gl.glNewList(channelOn,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		channelOut = channelOn+1;
		gl.glNewList(channelOut,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++); // 180
		gl.glEndList();
		
		channel1 = channelOut+1;
		gl.glNewList(channel1,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		channel2 = channel1+1;
		gl.glNewList(channel2,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		channel3 = channel2+1;
		gl.glNewList(channel3,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		channel4 = channel3+1;
		gl.glNewList(channel4,GL.GL_COMPILE); 
		draw(gLDrawable,16,16,cpt++);
		gl.glEndList();
		
		set1On = channel4+1;
		gl.glNewList(set1On,GL.GL_COMPILE); 
		draw(gLDrawable,128,16,cpt++); // 185
		gl.glEndList();
		
		set1Out = set1On+1;
		gl.glNewList(set1Out,GL.GL_COMPILE); 
		draw(gLDrawable,128,16,cpt++);
		gl.glEndList();
		
		set2On = set1Out+1;
		gl.glNewList(set2On,GL.GL_COMPILE); 
		draw(gLDrawable,128,16,cpt++);
		gl.glEndList();
		
		set2Out = set2On+1;
		gl.glNewList(set2Out,GL.GL_COMPILE); 
		draw(gLDrawable,128,16,cpt++);
		gl.glEndList();
		
		spellThunder = set2Out+1;
		gl.glNewList(spellThunder,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellThunder1 = spellThunder+1;
		gl.glNewList(spellThunder1,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 190
		gl.glEndList();
		
		spellThunder2 = spellThunder1+1;
		gl.glNewList(spellThunder2,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellThunder3 = spellThunder2+1;
		gl.glNewList(spellThunder3,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellIce = spellThunder3+1;
		gl.glNewList(spellIce ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellIce1 = spellIce+1;
		gl.glNewList(spellIce1 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellIce2 = spellIce1+1;
		gl.glNewList(spellIce2 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 195
		gl.glEndList();
		
		spellIce3 = spellIce2+1;
		gl.glNewList(spellIce3 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellIce4 = spellIce3+1;
		gl.glNewList(spellIce4 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellFire = spellIce4+1;
		gl.glNewList(spellFire ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellFire1 = spellFire+1;
		gl.glNewList(spellFire1 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellFire2 = spellFire1+1;
		gl.glNewList(spellFire2 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 200
		gl.glEndList();
		
		spellFire3 = spellFire2+1;
		gl.glNewList(spellFire3 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellFire4 = spellFire3+1;
		gl.glNewList(spellFire4 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellEarth = spellFire4+1;
		gl.glNewList(spellEarth ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellEarth1 = spellEarth+1;
		gl.glNewList(spellEarth1 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellEarth2 = spellEarth1+1;
		gl.glNewList(spellEarth2 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 205
		gl.glEndList();
		
		spellEarth3 = spellEarth2+1;
		gl.glNewList(spellEarth3 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellEarth4 = spellEarth3+1;
		gl.glNewList(spellEarth4 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellEarth5 = spellEarth4+1;
		gl.glNewList(spellEarth5 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind = spellEarth5+1;
		gl.glNewList(spellWind ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind1 = spellWind+1;
		gl.glNewList(spellWind1 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 210
		gl.glEndList();
		
		spellWind2 = spellWind1+1;
		gl.glNewList(spellWind2 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind3 = spellWind2+1;
		gl.glNewList(spellWind3 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind4 = spellWind3+1;
		gl.glNewList(spellWind4 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind5 = spellWind4+1;
		gl.glNewList(spellWind5 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++);
		gl.glEndList();
		
		spellWind6 = spellWind5+1;
		gl.glNewList(spellWind6 ,GL.GL_COMPILE); 
		draw(gLDrawable,32,32,cpt++); // 215
		gl.glEndList();
		
		selectCursor = spellWind6;
		gl.glNewList(selectCursor ,GL.GL_COMPILE); 
		draw(gLDrawable,16,32,cpt++); 
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
		
		textureLoader.load_texture("data/images/gui/equipement.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/equipement.png");
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
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[96]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1102.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[97]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1103.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[98]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1104.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[99]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1105.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[100]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1106.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[101]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1107.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[102]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1108.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[103]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1109.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[104]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1201.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[105]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1202.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[106]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1203.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[107]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1204.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[108]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1205.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[109]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1206.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[110]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1207.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[111]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1208.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[112]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1209.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[113]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1301.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[114]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1302.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[115]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1303.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[116]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1304.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[117]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1305.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[118]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1306.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[119]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1307.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[120]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1308.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[121]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1309.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[122]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1401.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[123]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1402.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[124]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1403.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[125]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1404.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[126]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1405.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[127]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1406.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[128]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1407.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[129]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1408.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[130]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1409.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[131]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1501.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[132]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1502.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[133]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1503.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[134]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1504.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[135]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1504.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[136]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1505.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[137]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1506.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[138]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());

		textureLoader.load_texture("data/images/gui/1507.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[139]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1508.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[140]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1509.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[141]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1601.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[142]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1602.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[143]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1603.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[144]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1604.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[145]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1605.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[146]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1606.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[147]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1607.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[148]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1608.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[149]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1609.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[150]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1701.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[151]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1702.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[152]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1703.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[153]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());

		textureLoader.load_texture("data/images/gui/1704.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[154]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1705.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[155]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1706.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[156]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1707.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[157]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1708.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[158]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1709.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[159]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1801.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[160]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1802.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[161]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1803.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[162]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1804.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[163]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1805.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[164]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1806.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[165]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1807.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[166]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1808.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[167]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/1809.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[168]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/close01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[169]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/close02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[170]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/lock01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[171]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/lock02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[172]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/up01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[173]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/up02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[174]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/down01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[175]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/down02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[176]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/emote01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[177]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/emote02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[178]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel01.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[179]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel02.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[180]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel03.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[181]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel04.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[182]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel05.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[183]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/channel06.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[184]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/set0101.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[185]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/set0102.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[186]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/set0201.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[187]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/set0202.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[188]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5000.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[189]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5001.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[190]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5002.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[191]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5003.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[192]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5100.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[193]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5101.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[194]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5102.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[195]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5103.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[196]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5104.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[197]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5200.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[198]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5201.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[199]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5202.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[200]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5203.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[201]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5204.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[202]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5300.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[203]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5301.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[204]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5302.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[205]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5303.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[206]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5304.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[207]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5305.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[208]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5400.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[209]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5401.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[210]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5402.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[211]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5403.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[212]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5404.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[213]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5405.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[214]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/5406.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[215]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		textureLoader.load_texture("data/images/gui/selectCursor.png");
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[216]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, textureLoader.getTWidth(), textureLoader.getTHeight(),
				0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		
		System.out.println("... loading finished !");
		
	}
	
	/* --------------- GET --------------- */
	
	
	
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
	
	public Point getCursorPos() {
		return cursorPos;
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
		if (point.getX()>=0 && point.getY()>=0)	cursorPos = point;
	}
	
	public void setMainChar(CharacterPRep physics) {
		charPRep = physics;
		//getInventory().setCharPRep(charPRep);
	}
	
	public void sendChatMessage(boolean send) {
		chatMsgSent=send;
		
	}
	
	public void setMsgType(String message) {
		chatMsgTyped=message;
		
	}
	
	/* --------------- IS ---------------*/
	
	
	public boolean isItemSelected() {
		return itemSelected;
	}
	
	public boolean isDEV() {
		return DEV;
	}
	
	public ActionBar getActionBar() {
		return actionBar;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public void setDebugAction(String action) {
		this.action = action;
	}

	public int getActionSelect() {
		return actionSelect;
	}

	public void setActionSelect(int actionSelect) {
		this.actionSelect = actionSelect;
	}

	public boolean isActionSelected() {
		return actionSelected;
	}
	
	
	
	
	
	
	
	
}