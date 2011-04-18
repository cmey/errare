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
import genericEngine.Engine;
import geom.Circle;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Texture;
import graphicsEngine.TextureFactory;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import main.ClientMain;
import soundEngine.SoundEngine;
import userInputEngine.UserInputController;
import xmlEngine.XmlEngine;

import com.sun.opengl.util.GLUT;


/* push/popMatrix : position du curseur
 * push/popAttrib : option opengl / couleur
 */
import logger.Logger;

/**
 * TODO BUGS
 * 1) le texte du chat est toujours d�ssin� au dessus de tout
 * 2) qd on passe en draguant (sans selection courante) sur un item �a le rammasse
 * P-e g�rer le lastClick et comparer ?
 * 3) je suis tomb� sur un etat ou on ne pouvait plus d�placer les fenetres
 * 5) si on essaye de bouger une fenetre ferm�e en utilisant la barre des titres, �a ne marche pas (ce qui est normal) 
 * mais apr�s, une fois qu'on l'a ouverte correctement, on ne peut plus jamais la d�placer
 * d�s fois on ne peut plus bouger uniquement celle la, des fois on ne peut plus rien bouger du tout
 * 6) on peut d�placer une fenetre en cliquant sur un morceau de la barre de titre pourtant cach�
*/

/**
 * Class GuiEngine 
 * Create, display, control graphics users interfaces in games
 * @author Ak
 * @version 2.0
 */

public class GuiEngine implements Engine, Runnable {

	/* ***************************************** *
	 * *********** C O N S T A N T S *********** *
	 * ***************************************** */

	/** The number of texture (items and interfaces) */
	private static final int NBTEXTURES = 350;


	/* ***************************************** *
	 * *********** I N S T A N C E S *********** *
	 * ***************************************** */

	/** Instance of the main */
	private ClientMain main;

	/** Instance of the gameEngine to communicate with */
	private GameEngine gameEngine;

	/** Instance of the soundEngine to play sounds */
	private SoundEngine soundEngine;

	/** Instance if the xmlEngine to have access to the database */
	private XmlEngine xmlEngine;

	/** Instance of the ActionBar (the main Gui part) */
	private ActionBar actionBar;

	/** Instance of a GLCanvas to test in devellopement mode */
	private GLCanvas canvas;

	/** Instance of a Point : the cursor position on the screen */
	private Point cursorPos;

	/** Instance of a GLUT (OpenGL) */
	private GLUT glut;

	/** Instance of the Load : to simulate a loading */
	private Load load;

	/** Instance of a GuiRep */
	private GuiRep itemSelect;

	/* ***************************************** *
	 * **************** I N T ****************** *
	 * ***************************************** */

	/** Actual FPS */
	private int actualFps;

	/** FPS Counter*/
	private int fpsCount;

	/** The time (in TIC) to slide the actionBar */
	private int DISPLAY_ACTIONBAR_PX_BY_TIC;

	/** The time (in TIC) to slide the chat */
	private int DISPLAY_CHAT_PX_BY_TIC;

	/** The time (in TIC) to slide the equipement */
	private int DISPLAY_EQUIPEMENT_PX_BY_TIC;

	/** The time (in TIC) to slide the inventory */
	private int DISPLAY_INVENTORY_PX_BY_TIC;

	/** The time (in TIC) of the cursor flicked */
	private int CURSOR_FLICK_TIME;

	/** The screen width of the player */
	private int screenWidth;

	/** The screen height of the player */
	private int screenHeight;

	/** Previous button count */
	private int previousCpt;

	/** Next button count */
	private int nextCpt;

	/** Zone button count */
	private int zoneCpt;

	/** World button count */
	private int worldCpt;

	/** Quest button count */
	private int questCpt;

	/** Option button count */
	private int optionCpt;

	/** ActionBar slide count */
	private int displayActionBarCpt = 0;

	/** Chat slide count */
	private int displayChatCpt = 70;

	/** Equipement slide count */
	private int displayEquipementCpt = 70;

	/** Inventory slide count */
	private int displayInventoryCpt = 70;

	/** Chat Cursor count */
	private int chatCursorFlickCpt = 0;

	/** Chat long text Advance against char test */
	private int chatMsgNbCharAdvance;

	/** Debug action count */
	private int cptDebugAction = 0;

	/** The id of the action selected */
	private int actionSelect;

	/** The font that we use with GLUT */
	private int font;

	/**	Chat cursor display list ID */
	private int chatCursor;

	/** Fire spells display list ID (In order) */
	private int aggravees;
	private int bdf;
	private int colonne;
	private int concentration;
	private int connaissance;
	private int deflagration;
	private int expansion;
	private int explosion;
	private int implosion;
	private int insensibilite;
	private int meteore;
	private int mur;
	private int nuee;
	private int persistantes;
	private int pyromanie;
	private int vague;

	/** Ice spells display list ID (In order) */
	private int armureFroid;
	private int armureGel;
	private int armureGlace;
	private int blizzard;
	private int cone;
	private int coneAmeliore;
	private int fils;
	private int gel;
	private int nova;
	private int pere;
	private int pic;
	private int picAmeliore;
	private int serment;
	private int trait;
	private int traitAmeliore;
	private int vagueFroid;
	private int vagueFroidAmeliore;

	/** Hero'equipement display list ID (In order) */

	// Potion
	private int smallTraditionalPotion;
	private int mediumTraditionalPotion;
	private int largeTraditionalPotion;
	private int smallMagicPotion;
	private int mediumMagicPotion;
	private int largeMagicPotion;
	private int questPotion;
	private int rarePotion;
	private int singlePotion;

	// Amulet
	private int smallTraditionalAmulet;
	private int mediumTraditionalAmulet;
	private int largeTraditionalAmulet;
	private int smallMagicAmulet;
	private int mediumMagicAmulet;
	private int largeMagicAmulet;
	private int questAmulet;
	private int rareAmulet;
	private int singleAmulet;

	// Bracelet
	private int smallTraditionalBracelet;
	private int mediumTraditionalBracelet;
	private int largeTraditionalBracelet;
	private int smallMagicBracelet;
	private int mediumMagicBracelet;
	private int largeMagicBracelet;
	private int questBracelet;
	private int rareBracelet;
	private int singleBracelet;

	// Ring
	private int smallTraditionalRing;
	private int mediumTraditionalRing;
	private int largeTraditionalRing;
	private int smallMagicRing;
	private int mediumMagicRing;
	private int largeMagicRing;
	private int questRing;
	private int rareRing;
	private int singleRing;

	// Buckle
	private int smallTraditionalBuckle;
	private int mediumTraditionalBuckle;
	private int largeTraditionalBuckle;
	private int smallMagicBuckle;
	private int mediumMagicBuckle;
	private int largeMagicBuckle;
	private int questBuckle;
	private int rareBuckle;
	private int singleBuckle;

	// Helm
	private int smallTraditionalHelm;
	private int mediumTraditionalHelm;
	private int largeTraditionalHelm;
	private int smallMagicHelm;
	private int mediumMagicHelm;
	private int largeMagicHelm;
	private int questHelm;
	private int rareHelm;
	private int singleHelm;

	// Wrist
	private int smallTraditionalWrist;
	private int mediumTraditionalWrist;
	private int largeTraditionalWrist;
	private int smallMagicWrist;
	private int mediumMagicWrist;
	private int largeMagicWrist;
	private int questWrist;
	private int rareWrist;
	private int singleWrist;

	// Amor
	private int smallTraditionalArmor;
	private int mediumTraditionalArmor;
	private int largeTraditionalArmor;
	private int smallMagicArmor;
	private int mediumMagicArmor;
	private int largeMagicArmor;
	private int questArmor;
	private int rareArmor;
	private int singleArmor;

	// Glove
	private int smallTraditionalGlove;
	private int mediumTraditionalGlove;
	private int largeTraditionalGlove;
	private int smallMagicGlove;
	private int mediumMagicGlove;
	private int largeMagicGlove;
	private int questGlove;
	private int rareGlove;
	private int singleGlove;

	// Belt
	private int smallTraditionalBelt;
	private int mediumTraditionalBelt;
	private int largeTraditionalBelt;
	private int smallMagicBelt;
	private int mediumMagicBelt;
	private int largeMagicBelt;
	private int questBelt;
	private int rareBelt;
	private int singleBelt;

	// Trousers
	private int smallTraditionalTrousers;
	private int mediumTraditionalTrousers;
	private int largeTraditionalTrousers;
	private int smallMagicTrousers;
	private int mediumMagicTrousers;
	private int largeMagicTrousers;
	private int questTrousers;
	private int rareTrousers;
	private int singleTrousers;

	// Tibia
	private int smallTraditionalTibia;
	private int mediumTraditionalTibia;
	private int largeTraditionalTibia;
	private int smallMagicTibia;
	private int mediumMagicTibia;
	private int largeMagicTibia;
	private int questTibia;
	private int rareTibia;
	private int singleTibia;

	// Boot
	private int smallTraditionalBoot;
	private int mediumTraditionalBoot;
	private int largeTraditionalBoot;
	private int smallMagicBoot;
	private int mediumMagicBoot;
	private int largeMagicBoot;
	private int questBoot;
	private int rareBoot;
	private int singleBoot;

	// Shield
	private int smallTraditionalShield;
	private int mediumTraditionalShield;
	private int largeTraditionalShield;
	private int smallMagicShield;
	private int mediumMagicShield;
	private int largeMagicShield;
	private int questShield;
	private int rareShield;
	private int singleShield;

	// Sword
	private int smallTraditionalSword;
	private int mediumTraditionalSword;
	private int largeTraditionalSword;
	private int smallMagicSword;
	private int mediumMagicSword;
	private int largeMagicSword;
	private int questSword;
	private int rareSword;
	private int singleSword;

	// Axe
	private int smallTraditionalAxe;
	private int mediumTraditionalAxe;
	private int largeTraditionalAxe;
	private int smallMagicAxe;
	private int mediumMagicAxe;
	private int largeMagicAxe;
	private int questAxe;
	private int rareAxe;
	private int singleAxe;

	// Stick
	private int smallTraditionalStick;
	private int mediumTraditionalStick;
	private int largeTraditionalStick;
	private int smallMagicStick;
	private int mediumMagicStick;
	private int largeMagicStick;
	private int questStick;
	private int rareStick;
	private int singleStick;

	// Arc
	private int smallTraditionalArc;
	private int mediumTraditionalArc;
	private int largeTraditionalArc;
	private int smallMagicArc;
	private int mediumMagicArc;
	private int largeMagicArc;
	private int questArc;
	private int rareArc;
	private int singleArc;

	/** Windows display list ID (In order) */
	private int actionBarNum;
	private int chat;
	private int equipement;
	private int inventory;
	private int smallBar;
	private int quest;
	private int characteristic;
	private int spellBook;

	/** Buttons display list ID (2 states for each) */

	private int quest01;
	private int quest02;
	private int option01;
	private int option02;
	private int previous01;
	private int previous02;
	private int next01;
	private int next02;
	private int zone01;
	private int zone02;
	private int world01;
	private int world02;
	private int upOn;
	private int upOut;
	private int lockOn;
	private int lockOut;
	private int closeOn;
	private int closeOut;
	private int emoteOn;
	private int emoteOut;
	private int channelOut;
	private int channel1;
	private int channel2;
	private int channel3;
	private int channel4;
	private int set1On;
	private int set1Out;
	private int set2On;
	private int set2Out;
	private int bag01;
	private int bag02;	

	/* ***************************************** *
	 * ************* D O U B L E *************** *
	 * ***************************************** */


	/* ***************************************** *
	 * *************** L O N G ***************** *
	 * ***************************************** */

	/** Last tic time */
	private long lastTicTime;


	/* ***************************************** *
	 * *********** B O O L E E N *************** *
	 * ***************************************** */

	/** Boolean indicate if the loading test is activate or not */
	private boolean loadingActived;

	/** Boolean indicate if there is a item selected or not */
	private boolean itemSelected;

	/** Boolean indicate if there is the debug mode is activated or not */
	private boolean debugMode;

	/** Boolean indicate if there is a action selected or not */
	private boolean actionSelected;

	/** Boolean indicate if the player can use the chat */
	private boolean chatMsgSent;

	/** Boolean indicate if the message that the player is righting in the chat is long or not */
	private boolean chatMsgIsLong;
	/**
	 * Boolean indicate if the engine is link to the main or not (developpement)
	 * True if this engine is link to nothing, else false
	 */
	private boolean DEVELOPPEMENT;

	/* ***************************************** *
	 * ************* S T R I N G *************** *
	 * ***************************************** */

	/** The text witch the player is righting in the chat */
	private String chatMsg = "";

	/** The text witch the player is righting in the chat if it's long :
	 *  The width of the text is > of the width of the chat editable site */
	private String chatLongMsg = "";

	/** The action (description) associate when the player do an action */
	private String action;

	/* ***************************************** *
	 * ************** A R R A Y **************** *
	 * ***************************************** */

	/** The tab associate with the texture of items'display list */
	private int [] texture;

	/** The tab that contain the path of each texture */
	private String[] texturePathTab;

	/**
	 * GuiEngine Constructor 
	 * Constructor for DEVELOPPEMENT Mode 
	 * Work alone (not with the main) 
	 * Create : 
	 * a Listener onto a new GLCanvas 
	 * a new soundEngine
	 * a new xmlEngine with a userInputController
	 * a new gameEngine
	 */

	public GuiEngine() {


		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimScreen = toolkit.getScreenSize();
		this.screenWidth = dimScreen.width;
		this.screenHeight = dimScreen.height;

		// Create array associate with the texture
		texture = new int[NBTEXTURES];

		// Set DEVELOPPEMENT to true
		DEVELOPPEMENT = true;

		// Set debugMode to true
		debugMode = true;

		// Initialisation of the action' message
		action="";

		// Creat the GLUT instance
		glut = new GLUT();

		DISPLAY_ACTIONBAR_PX_BY_TIC = 5;
		DISPLAY_CHAT_PX_BY_TIC  = 5;
		DISPLAY_EQUIPEMENT_PX_BY_TIC = 5;;
		DISPLAY_INVENTORY_PX_BY_TIC =  5;
		CURSOR_FLICK_TIME = 100;

		// We choose this font to write (We can modify it later)
		font = GLUT.BITMAP_HELVETICA_12;

		// Create a canvas to test alone
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		capabilities.setStencilBits(3);
		canvas = new GLCanvas(capabilities);

		// Anonymous class (we must do that to test alone)
		// We called the others fonctions
		GLEventListener glevent = new GLEventListener() {

			public void init(GLAutoDrawable gLDrawable) {
				initDEVELOPPEMENT(gLDrawable);
			}
			public void display(GLAutoDrawable gLDrawable) {
				displayGUI(gLDrawable);
			}
			public void reshape(GLAutoDrawable gLdrawable, int xstart,
					int ystart, int width, int height) {
				reshapeGUI(gLdrawable, xstart, ystart, width, height);
			}			
			// Not implemented
			public void displayChanged(GLAutoDrawable gLDrawable,
					boolean modeChanged, boolean DEVELOPPEMENTiceChanged) {
			}
		};

		// We create a instance of the XmlEngine to use the UserInputController
		try {
			xmlEngine = new XmlEngine();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		UserInputController userInputController = new UserInputController("test", xmlEngine);
		register(userInputController);

		// We add the listener
		canvas.addGLEventListener(glevent);
		canvas.addKeyListener(userInputController);
		canvas.addMouseListener(userInputController);
		canvas.addMouseMotionListener(userInputController);
		canvas.setSize(screenWidth, screenHeight);

		// Instance of engines to test
		load = new Load(this);
		soundEngine = new SoundEngine();
		actionBar = new ActionBar(this);
		//gameEngine = new GameEngine();


	}

	/**
	 * GuiEngine Constructor	 
	 * @param main the pointer of the Main Constructor
	 * Get all engines instances with the main
	 */

	public GuiEngine(ClientMain main) {

		// Create Instances
		texture = new int[NBTEXTURES];
		this.main = main;

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimScreen = toolkit.getScreenSize();
		this.screenWidth = dimScreen.width;
		this.screenHeight = dimScreen.height;
		
		soundEngine = main.getSoundEngine();
		actionBar = new ActionBar(this);

		// Set DEVELOPPEMENTeloppement to false
		DEVELOPPEMENT = false;

		debugMode = false;

		action = "";

		glut = new GLUT();
		font = GLUT.BITMAP_HELVETICA_12;

		// Instance of engines (from the main)
		// gameEngine = main.getGameEngine();
		xmlEngine = main.getXmlEngine();
		
		register(main.getUserInputController());

		// We suppose that : the graphicsEngine instanciates the mouse listener
	}

	public void register(UserInputController userInputController) {

		// We register somes keys
		userInputController.register(this, "chat_open");
		userInputController.register(this, "equipement_open");
		userInputController.register(this, "inventory_open");
		userInputController.register(this, "loading_activated");
		userInputController.register(this, "gui_debug");
		userInputController.register(this, "gui_pick");
		userInputController.register(this, "char_a");
		userInputController.register(this, "char_b");
		userInputController.register(this, "char_c");
		userInputController.register(this, "char_d");
		userInputController.register(this, "char_e");
		userInputController.register(this, "char_f");
		userInputController.register(this, "char_g");
		userInputController.register(this, "char_h");
		userInputController.register(this, "char_i");
		userInputController.register(this, "char_j");
		userInputController.register(this, "char_k");
		userInputController.register(this, "char_l");
		userInputController.register(this, "char_m");
		userInputController.register(this, "char_n");
		userInputController.register(this, "char_o");
		userInputController.register(this, "char_p");
		userInputController.register(this, "char_q");
		userInputController.register(this, "char_r");
		userInputController.register(this, "char_s");
		userInputController.register(this, "char_t");
		userInputController.register(this, "char_u");
		userInputController.register(this, "char_v");
		userInputController.register(this, "char_w");
		userInputController.register(this, "char_x");
		userInputController.register(this, "char_y");
		userInputController.register(this, "char_z");	
		userInputController.register(this, "char_A");
		userInputController.register(this, "char_B");
		userInputController.register(this, "char_C");
		userInputController.register(this, "char_D");
		userInputController.register(this, "char_E");
		userInputController.register(this, "char_F");
		userInputController.register(this, "char_G");
		userInputController.register(this, "char_H");
		userInputController.register(this, "char_I");
		userInputController.register(this, "char_J");
		userInputController.register(this, "char_K");
		userInputController.register(this, "char_L");
		userInputController.register(this, "char_M");
		userInputController.register(this, "char_N");
		userInputController.register(this, "char_O");
		userInputController.register(this, "char_P");
		userInputController.register(this, "char_Q");
		userInputController.register(this, "char_R");
		userInputController.register(this, "char_S");
		userInputController.register(this, "char_T");
		userInputController.register(this, "char_U");
		userInputController.register(this, "char_V");
		userInputController.register(this, "char_W");
		userInputController.register(this, "char_X");
		userInputController.register(this, "char_Y");
		userInputController.register(this, "char_Z");
		userInputController.register(this, "enter");
		userInputController.register(this, "return");
		userInputController.register(this, "exit");

		// We register somes mouse action
		userInputController.register(this, "mouse_move");
		userInputController.register(this, "mouse_dragg");
		userInputController.register(this, "mouse_click");
		userInputController.register(this, "mouse_release");
	}

	/**
	 * initDEVELOPPEMENT fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable Initialize all for the DEVELOPPEMENTeloppement
	 */

	public void initDEVELOPPEMENT(GLAutoDrawable gLDrawable) {

		// Load textures in the memory of the graphic card
		loadGLTexture(gLDrawable);

		// Create display lists
		buildLists(gLDrawable);

		// load.loadGLTexture(gLDrawable);
		// load.buildLists(gLDrawable);

		GL gl = gLDrawable.getGL();

		// OpenGL Initialisation
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glAlphaFunc(GL.GL_GREATER, 0.1f);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glClearDepth(1.0f); // Depth Buffer Setup
		gl.glClearStencil(0); // Clear The Stencil Buffer To 0
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Perspective
		// Calculations

	}

	/**
	 * initGUI fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable Initialize all for the Gui game
	 */

	public void initGUI(GLAutoDrawable gLDrawable) {

		/*
		 * We create and add the event (there is now two lisener on the
		 * glCanvas) the GUI listener and the Graphic listener
		 */

		// Load the texture in the memory of the graphic card
		loadGLTexture(gLDrawable);

		// Create display lists
		buildLists(gLDrawable);

	}

	/**
	 * glBeginOrtho fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable object Initialise the environnement Set the
	 *            point 0,0 to the top left (like java and not like openGL) Set
	 *            objects in the right position
	 */

	public void glBeginOrtho(GLAutoDrawable gLDrawable) {

		GL gl = gLDrawable.getGL();
		GLU glu = new GLU();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		// The left lower corner (0,0) will be attached with that one of the
		// window
		glu.gluOrtho2D(0, (int) gLDrawable.getWidth(), 0, (int) gLDrawable
				.getHeight());

		// Set the point of creation of the image in the left higher corner
		gl.glScaled(1, -1, 1);

		// met le rep�re en haut � gauche
		gl.glTranslatef(0, -(int) gLDrawable.getHeight(), 0);

	}

	/**
	 * displayGUI fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable object Display all graphics users interfaces in
	 *            the right places
	 */

	public void displayGUI(GLAutoDrawable gLDrawable) {


		// (long millis, int nanos)
		//Thread.yield();

		GL gl = gLDrawable.getGL();

		// If we test we must clear the buffer
		if (DEVELOPPEMENT) {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
					| GL.GL_STENCIL_BUFFER_BIT);
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
		if (!DEVELOPPEMENT) {
			gl.glEnable(GL.GL_BLEND);
		}

		if (actionBar.getPriority(Focus.CHAT_FOCUS)==0) {
			if (actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==1) {
				displayInventory(gl);
				displayEquipement(gl);
			}else {
				displayEquipement(gl);
				displayInventory(gl);
			}
			displayChat(gl);
		}else if (actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==0) {

			if (actionBar.getPriority(Focus.INVENTORY_FOCUS)==1) {
				displayChat(gl);
				displayInventory(gl);
			}else {
				displayInventory(gl);
				displayChat(gl);
			}
			displayEquipement(gl);
		}else {		
			if (actionBar.getPriority(Focus.CHAT_FOCUS)==1) {
				displayEquipement(gl);
				displayChat(gl);
			}else {
				displayChat(gl);
				displayEquipement(gl);
			}
			displayInventory(gl);
		}

		displayActionBar(gl);

		displayInfos(gl);

		displayDebug(gl);

		// If a item is select, this item will follow the cursor
		if (actionSelected) {
			// TODO un ptit bug des couleurs
			// gl.glColor3f(1.0f,0.0f,0.0f);
			switch (actionSelect) {
			case 10001:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(aggravees);break;
			case 10002:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(bdf);break;
			case 10003:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(colonne);break;
			case 10004:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(concentration);
				break;
			case 10005:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(connaissance);
				break;
			case 10006:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(deflagration);
				break;
			case 10007:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(expansion);
				break;
			case 10008:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(explosion);
				break;
			case 10009:
				gl.glTranslated(cursorPos.getX() + 10,cursorPos.getY() + 10, 0);
				gl.glCallList(implosion);
				break;
			case 10010:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(insensibilite);
				break;
			case 10011:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(meteore);
				break;
			case 10012:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(mur);
				break;
			case 10013:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(nuee);
				break;
			case 10014:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(persistantes);
				break;
			case 10015:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(pyromanie);
				break;
			case 10016:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(vague);
				break;
			case 10101:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(armureFroid);
				break;
			case 10102:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(armureGel);
				break;
			case 10103:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(armureGlace);
				break;
			case 10104:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(blizzard);
				break;
			case 10105:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(cone);
				break;
			case 10106:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(coneAmeliore);
				break;
			case 10107:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(fils);
				break;
			case 10108:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(gel);
				break;
			case 10109:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(nova);
				break;
			case 10110:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(pere);
				break;
			case 10111:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(pic);
				break;
			case 10112:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(picAmeliore);
				break;
			case 10113:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(serment);
				break;
			case 10114:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(trait);
				break;
			case 10115:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(traitAmeliore);
				break;
			case 10116:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(vagueFroid);
				break;
			case 10117:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
				gl.glCallList(vagueFroidAmeliore);
				break;
				// TODO ...
			default:
				break;
			}
		} else if (itemSelected && itemSelect != null) {
			switch (itemSelect.getType()) {
			default:
				gl
				.glTranslated(cursorPos.getX() + 10,
						cursorPos.getY() + 10, 0);
			gl.glCallList(mediumTraditionalSword);
			break;
			}
		}

		debugAction(gl, action);

		gl.glPopAttrib();
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);

		if (DEVELOPPEMENT)
			canvas.repaint();

		if (loadingActived){ load.x=load.x+1; load.displayGUI(gLDrawable); }


		fpsCount++;

	}

	private void displayInfos(GL gl) {

		gl.glPushMatrix();
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);

		// Infos messages 
		gl.glRasterPos2i(10, 20);
		glut.glutBitmapString(font, "'Echap' pour quitter et sauvegarder les parametres");
		gl.glRasterPos2i(10, 40);
		glut.glutBitmapString(font, "'Ctrl + L' pour simuler un loading");
		gl.glRasterPos2i(10, 60);
		glut.glutBitmapString(font, "'Ctrl + F1' pour voir les differentes zones d'action");
		gl.glRasterPos2i(10, 80);
		glut.glutBitmapString(font, "'Ctrl + P' pour ajouter un objet a l'inventaire");
		gl.glRasterPos2i(10, 100);
		glut.glutBitmapString(font, "'Ctrl + C' pour ouvrir/fermer le chat");
		gl.glRasterPos2i(10, 120);
		glut.glutBitmapString(font, "'Ctrl + E' pour ouvrir/fermer l'equipement");
		gl.glRasterPos2i(10, 140);
		glut.glutBitmapString(font, "'Ctrl + I' pour ouvrir/fermer l'inventaire");

		gl.glPopAttrib();
		gl.glPopMatrix();
	}

	private void displayActionBar(GL gl) {
		// Display the action bar with a scroll effect
		displayActionBarCpt = (displayActionBarCpt < ActionBar.ACTIONBARMAP_HEIGHT) ? displayActionBarCpt + DISPLAY_ACTIONBAR_PX_BY_TIC : displayActionBarCpt;
		displayActionBarCpt = (displayActionBarCpt > ActionBar.ACTIONBARMAP_HEIGHT) ? displayActionBarCpt =  ActionBar.ACTIONBARMAP_HEIGHT : displayActionBarCpt;
		gl.glTranslated((screenWidth - 1024) / 2, screenHeight - displayActionBarCpt, 0);
		gl.glCallList(actionBarNum);

		// Display icons in the actionBar
		if (displayActionBarCpt >= ActionBar.ACTIONBARMAP_HEIGHT) {

			gl.glTranslated((screenWidth - 1024) / 2 + 8, screenHeight - 41, 0);
			if (actionBar.isClickOnQuest()) {
				if (questCpt >= 0 && questCpt < 20) {
					gl.glCallList(quest02);
					questCpt++;
				} else {
					actionBar.setClickOnQuest(false);
					questCpt = 0;
					gl.glCallList(quest01);
				}
			} else {
				gl.glCallList(quest01);
			}

			gl.glTranslated((screenWidth - 1024) / 2 + 45,screenHeight - 41, 0);
			if (actionBar.isClickOnOption()) {
				if (optionCpt >= 0 && optionCpt < 20) {
					gl.glCallList(option02);
					optionCpt++;
				} else {
					actionBar.setClickOnOption(false);
					optionCpt = 0;
					gl.glCallList(option01);
				}
			} else {
				gl.glCallList(option01);
			}

			gl.glTranslated((screenWidth - 1024) / 2 + 833, screenHeight - 43,
					0);
			if (actionBar.isClickOnPrevious()) {
				if (previousCpt >= 0 && previousCpt < 20) {
					gl.glCallList(previous02);
					previousCpt++;
				} else {
					actionBar.setClickOnPrevious(false);
					previousCpt = 0;
					gl.glCallList(previous01);
				}
			} else {
				gl.glCallList(previous01);
			}
			gl.glTranslated((screenWidth - 1024) / 2 + 851, screenHeight - 43,
					0);
			if (actionBar.isClickOnNext()) {
				if (nextCpt >= 0 && nextCpt < 20) {
					gl.glCallList(next02);
					nextCpt++;
				} else {
					actionBar.setClickOnNext(false);
					nextCpt = 0;
					gl.glCallList(next01);
				}
			} else {
				gl.glCallList(next01);
			}
			gl.glTranslated((screenWidth - 1024) / 2 + 887, screenHeight - 35,
					0);
			if (actionBar.isClickOnZone()) {
				if (zoneCpt >= 0 && zoneCpt < 20) {
					gl.glCallList(zone02);
					zoneCpt++;
				} else {
					actionBar.setClickOnZone(false);
					zoneCpt = 0;
					gl.glCallList(zone01);
				}
			} else {
				gl.glCallList(zone01);
			}
			gl.glTranslated((screenWidth - 1024) / 2 + 971, screenHeight - 153,	0);
			if (actionBar.isClickOnWorld()) {
				if (worldCpt >= 0 && worldCpt < 20) {
					gl.glCallList(world02);
					worldCpt++;
				} else {
					actionBar.setClickOnWorld(false);
					worldCpt = 0;
					gl.glCallList(world01);
				}
			} else {
				gl.glCallList(world01);
			}

			int[] tabAction = actionBar.getTabAction();
			for (int i = 0; i < tabAction.length; i++) {
				if (tabAction[i] != 0) {
					// if
					// (i<2)gl.glTranslated(((screenWidth-1024)/2)+i*38+86,screenHeight-40,0);
					if (i < 10)
						gl.glTranslated(((screenWidth - 1024) / 2) + (i - 2)
								* 37 + 162, screenHeight - 41, 0);
					else if (i > 9 && i < 20)
						gl.glTranslated(((screenWidth - 1024) / 2) + (i - 10)
								* 37 + 464, screenHeight - 41, 0);
					switch (tabAction[i]) {
					case 10001:
						gl.glCallList(aggravees);break;
					case 10002:
						gl.glCallList(bdf);break;
					case 10003:
						gl.glCallList(colonne);
						break;
					case 10004:
						gl.glCallList(concentration);
						break;
					case 10005:
						gl.glCallList(connaissance);
						break;
					case 10006:
						gl.glCallList(deflagration);
						break;
					case 10007:
						gl.glCallList(expansion);
						break;
					case 10008:
						gl.glCallList(explosion);
						break;
					case 10009:
						gl.glCallList(implosion);
						break;
					case 10010:
						gl.glCallList(insensibilite);
						break;
					case 10011:
						gl.glCallList(meteore);
						break;
					case 10012:
						gl.glCallList(mur);
						break;
					case 10013:
						gl.glCallList(nuee);
						break;
					case 10014:
						gl.glCallList(persistantes);
						break;
					case 10015:
						gl.glCallList(pyromanie);
						break;
					case 10016:
						gl.glCallList(vague);
						break;
					case 10101:
						gl.glCallList(armureFroid);
						break;
					case 10102:
						gl.glCallList(armureGel);
						break;
					case 10103:
						gl.glCallList(armureGlace);
						break;
					case 10104:
						gl.glCallList(blizzard);
						break;
					case 10105:
						gl.glCallList(cone);
						break;
					case 10106:
						gl.glCallList(coneAmeliore);
						break;
					case 10107:
						gl.glCallList(fils);
						break;
					case 10108:
						gl.glCallList(gel);
						break;
					case 10109:
						gl.glCallList(nova);
						break;
					case 10110:
						gl.glCallList(pere);
						break;
					case 10111:
						gl.glCallList(pic);
						break;
					case 10112:
						gl.glCallList(picAmeliore);
						break;
					case 10113:
						gl.glCallList(serment);
						break;
					case 10114:
						gl.glCallList(trait);
						break;
					case 10115:
						gl.glCallList(traitAmeliore);
						break;
					case 10116:
						gl.glCallList(vagueFroid);
						break;
					case 10117:
						gl.glCallList(vagueFroidAmeliore);
						break;
						// TODO ...
					default:
						break;
					}
				}
			}
		}

	}

	private void displayChat(GL gl) {

		// If it's not dragged
		if (!actionBar.getChat().isDragged()) {
			// If it's open
			if (actionBar.getChat().isVisible()) {
				// If the chat isn't set to the default position we display this window directly
				if (actionBar.getChat().getPosition().getX() != actionBar.getChat().getDefaultPosition().getX()
						|| actionBar.getChat().getPosition().getY() != actionBar.getChat().getDefaultPosition().getY()) {
					if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT) {
						displayChatCpt = Chat.CHAT_HEIGHT;
					}
				}
				// Else we display it we a scroll effect
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT) {
					displayChatCpt = (displayChatCpt <  Chat.CHAT_HEIGHT) ? displayChatCpt + DISPLAY_CHAT_PX_BY_TIC : displayChatCpt;
					displayChatCpt = (displayChatCpt >  Chat.CHAT_HEIGHT) ? Chat.CHAT_HEIGHT : displayChatCpt;
					gl.glTranslated(actionBar.getChat().getPosition().getX(), actionBar.getChat().getPosition().getY() - displayChatCpt, 0);
					gl.glCallList(chat);
				}
				// When is finish, we display the chat's tools
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT && displayChatCpt ==  Chat.CHAT_HEIGHT) {
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 9, actionBar.getChat().getPosition().getY() - 45, 0);
					gl.glCallList(emoteOn);

					// We get witch channel is on
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 92, actionBar.getChat().getPosition().getY() - 225, 0);
					gl.glCallList(channel1);
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 153,	actionBar.getChat().getPosition().getY() - 225, 0);
					gl.glCallList(channel2);
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 213, actionBar.getChat().getPosition().getY() - 225, 0);
					gl.glCallList(channel3);
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 271,	actionBar.getChat().getPosition().getY() - 225, 0);
					gl.glCallList(channel4);
					switch (actionBar.getChat().getActiveChannel()) {
					case Chat.GENERAL_CHANNEL :
						gl.glTranslated(actionBar.getChat().getPosition().getX() + 92, actionBar.getChat().getPosition().getY() - 225, 0);
						gl.glCallList(channelOut);break;
					case Chat.GUILD_CHANNEL :
						gl.glTranslated(actionBar.getChat().getPosition().getX() + 153,	actionBar.getChat().getPosition().getY() - 225, 0);
						gl.glCallList(channelOut);break;
					case Chat.GROUP_CHANNEL :
						gl.glTranslated(actionBar.getChat().getPosition().getX() + 213, actionBar.getChat().getPosition().getY() - 225, 0);
						gl.glCallList(channelOut);break;	
					case Chat.PRIVATE_CHANNEL :
						gl.glTranslated(actionBar.getChat().getPosition().getX() + 271,	actionBar.getChat().getPosition().getY() - 225, 0);
						gl.glCallList(channelOut);break;	
					default :
						gl.glCallList(channelOut);
					}

					// All chat's buttons
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 316,	actionBar.getChat().getPosition().getY() - 227, 0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 334,	actionBar.getChat().getPosition().getY() - 227, 0);
					gl.glCallList(lockOut);
					gl.glTranslated(actionBar.getChat().getPosition().getX() + 352,	actionBar.getChat().getPosition().getY() - 227, 0);
					gl.glCallList(closeOut);

					// The message wicht the player write
					// gl.glColor3f(1.0f,1.0f,1.0f);
					if (!chatMsgIsLong) {
						if (glut.glutBitmapLength(font, chatMsg)<actionBar.getChat().getEditableOpenSite().getWidth()-5) {
							gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+44, (int)(actionBar.getChat().getPosition().getY())-30);
							glut.glutBitmapString(font,chatMsg);
						}else {
							chatLongMsg=chatMsg;
							chatMsgIsLong=true;
							gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+44, (int)(actionBar.getChat().getPosition().getY())-30);
							glut.glutBitmapString(font, chatLongMsg=chatLongMsg.substring(1));
							chatMsgNbCharAdvance++;
						}
					}else {
						chatLongMsg=chatMsg.substring(chatMsgNbCharAdvance);
						if (glut.glutBitmapLength(font, chatLongMsg)<actionBar.getChat().getEditableOpenSite().getWidth()-5) {
							gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+44, (int)(actionBar.getChat().getPosition().getY())-30);
							glut.glutBitmapString(font,chatLongMsg);
						}else {
							gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+44, (int)(actionBar.getChat().getPosition().getY())-30);
							glut.glutBitmapString(font, chatLongMsg=chatLongMsg.substring(1));
							chatMsgNbCharAdvance++;
						}
					}

					
					
					// The chat cursor flicked if the chat is open
					if (actionBar.getChat().isActivated()) {
						gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
						
					chatCursorFlickCpt++;
					if (chatCursorFlickCpt >= 0 && chatCursorFlickCpt < CURSOR_FLICK_TIME) {
						if (!chatMsgIsLong)
							gl.glTranslated(actionBar.getChat().getPosition().getX()+44+glut.glutBitmapLength(font, chatMsg),(int) actionBar.getChat().getPosition().getY() - 43,0);
						else
							gl.glTranslated(actionBar.getChat().getPosition().getX()+44+glut.glutBitmapLength(font, chatLongMsg),(int) actionBar.getChat().getPosition().getY() - 43,0);
						gl.glCallList(chatCursor);

					} else if (chatCursorFlickCpt >= CURSOR_FLICK_TIME*2) {
						chatCursorFlickCpt = 0;
					}
					gl.glPopAttrib();
					}
					

					if (chatMsgSent) {

						gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
						String login = "Ak"; // getGameEngine().getHero().getName();
						// TODO HERO
						// We display only the last senventh phrases

						switch (actionBar.getChat().getActiveChannel()) {

						case Chat.GENERAL_CHANNEL:

							String[] tabGenMsg = actionBar.getChat().getMessage(Chat.GENERAL_CHANNEL);
							String GenMsg1 = "", GenMsg2 = "", GenMsg3 = "", GenMsg4 = "", GenMsg5 = "", GenMsg6 = "", GenMsg7 = "";

							for (int i = 0; i < tabGenMsg.length; i++) {
								if (tabGenMsg[i].compareTo("") == 0)break;
								if (i >= 6)	GenMsg1 = login + " : " + tabGenMsg[i - 6];
								if (i >= 5)	GenMsg2 = login + " : " + tabGenMsg[i - 5];
								if (i >= 4)	GenMsg3 = login + " : " + tabGenMsg[i - 4];
								if (i >= 3)	GenMsg4 = login + " : " + tabGenMsg[i - 3];
								if (i >= 2)	GenMsg5 = login + " : " + tabGenMsg[i - 2];
								if (i >= 1)	GenMsg6 = login + " : " + tabGenMsg[i - 1];
								GenMsg7 = login + " : " + tabGenMsg[i];
							}
							if (glut.glutBitmapLength(font, GenMsg7)>actionBar.getChat().getEditableOpenSite().getWidth()-5) {
								while(glut.glutBitmapLength(font, GenMsg7)>actionBar.getChat().getEditableOpenSite().getWidth()-5) {
									GenMsg6+=GenMsg7.substring(GenMsg7.length()-1);
									GenMsg7=GenMsg7.substring(0,GenMsg7.length()-1);
								}
							}
							gl.glColor3f(1.0f, 1.0f, 0.2f);
							if (GenMsg1.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-185);
								glut.glutBitmapString(font, GenMsg1);
							}
							if (GenMsg2.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-165);
								glut.glutBitmapString(font, GenMsg2);
							}
							if (GenMsg3.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-145);
								glut.glutBitmapString(font, GenMsg3);
							}
							if (GenMsg4.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-125);
								glut.glutBitmapString(font, GenMsg4);
							}
							if (GenMsg5.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-105);
								glut.glutBitmapString(font, GenMsg5);
							}
							if (GenMsg6.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-85);
								glut.glutBitmapString(font, GenMsg6);
							}
							if (GenMsg7.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-65);
								glut.glutBitmapString(font, GenMsg7);
							}
							break;

						case Chat.GUILD_CHANNEL:

							String[] tabGldMsg = actionBar.getChat().getMessage(Chat.GUILD_CHANNEL);
							String GldMsg1 = "", GldMsg2 = "", GldMsg3 = "", GldMsg4 = "", GldMsg5 = "", GldMsg6 = "", GldMsg7 = "";

							for (int i = 0; i < tabGldMsg.length; i++) {
								if (tabGldMsg[i].compareTo("") == 0)break;
								if (i >= 6)	GldMsg1 = login + " : " + tabGldMsg[i - 6];
								if (i >= 5)	GldMsg2 = login + " : " + tabGldMsg[i - 5];
								if (i >= 4)	GldMsg3 = login + " : " + tabGldMsg[i - 4];
								if (i >= 3)	GldMsg4 = login + " : " + tabGldMsg[i - 3];
								if (i >= 2)	GldMsg5 = login + " : " + tabGldMsg[i - 2];
								if (i >= 1)	GldMsg6 = login + " : " + tabGldMsg[i - 1];
								GldMsg7 = login + " : " + tabGldMsg[i];
							}
							gl.glColor3f(0.05f, 0.9f, 1.0f);
							if (GldMsg1.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-185);
								glut.glutBitmapString(font, GldMsg1);
							}
							if (GldMsg2.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-165);
								glut.glutBitmapString(font, GldMsg2);
							}
							if (GldMsg3.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-145);
								glut.glutBitmapString(font, GldMsg3);
							}
							if (GldMsg4.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-125);
								glut.glutBitmapString(font, GldMsg4);
							}
							if (GldMsg5.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-105);
								glut.glutBitmapString(font, GldMsg5);
							}
							if (GldMsg6.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-85);
								glut.glutBitmapString(font, GldMsg6);
							}
							if (GldMsg7.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-65);
								glut.glutBitmapString(font, GldMsg7);
							}
							break;

						case Chat.GROUP_CHANNEL:

							String[] tabGrpMsg = actionBar.getChat().getMessage(Chat.GROUP_CHANNEL);
							String GrpMsg1 = "", GrpMsg2 = "", GrpMsg3 = "", GrpMsg4 = "", GrpMsg5 = "", GrpMsg6 = "", GrpMsg7 = "";

							for (int i = 0; i < tabGrpMsg.length; i++) {
								if (tabGrpMsg[i].compareTo("") == 0)break;
								if (i >= 6)	GrpMsg1 = login + " : " + tabGrpMsg[i - 6];
								if (i >= 5)	GrpMsg2 = login + " : " + tabGrpMsg[i - 5];
								if (i >= 4)	GrpMsg3 = login + " : " + tabGrpMsg[i - 4];
								if (i >= 3)	GrpMsg4 = login + " : " + tabGrpMsg[i - 3];
								if (i >= 2)	GrpMsg5 = login + " : " + tabGrpMsg[i - 2];
								if (i >= 1)	GrpMsg6 = login + " : " + tabGrpMsg[i - 1];
								GrpMsg7 = login + " : " + tabGrpMsg[i];
							}
							gl.glColor3f(0.5f, 1.0f, 0.05f);
							if (GrpMsg1.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-185);
								glut.glutBitmapString(font, GrpMsg1);
							}
							if (GrpMsg2.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-165);
								glut.glutBitmapString(font, GrpMsg2);
							}
							if (GrpMsg3.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-145);
								glut.glutBitmapString(font, GrpMsg3);
							}
							if (GrpMsg4.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-125);
								glut.glutBitmapString(font, GrpMsg4);
							}
							if (GrpMsg5.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-105);
								glut.glutBitmapString(font, GrpMsg5);
							}
							if (GrpMsg6.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-85);
								glut.glutBitmapString(font, GrpMsg6);
							}
							if (GrpMsg7.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-65);
								glut.glutBitmapString(font, GrpMsg7);
							}
							break;

						case Chat.PRIVATE_CHANNEL:

							String[] tabPvMsg = actionBar.getChat().getMessage(Chat.PRIVATE_CHANNEL);
							String PvMsg1 = "", PvMsg2 = "", PvMsg3 = "", PvMsg4 = "", PvMsg5 = "", PvMsg6 = "", PvMsg7 = "";

							for (int i = 0; i < tabPvMsg.length; i++) {
								if (tabPvMsg[i].compareTo("") == 0)break;
								if (i >= 6)	PvMsg1 = login + " : " + tabPvMsg[i - 6];
								if (i >= 5)	PvMsg2 = login + " : " + tabPvMsg[i - 5];
								if (i >= 4)	PvMsg3 = login + " : " + tabPvMsg[i - 4];
								if (i >= 3)	PvMsg4 = login + " : " + tabPvMsg[i - 3];
								if (i >= 2)	PvMsg5 = login + " : " + tabPvMsg[i - 2];
								if (i >= 1)	PvMsg6 = login + " : " + tabPvMsg[i - 1];
								PvMsg7 = login + " : " + tabPvMsg[i];
							}
							gl.glColor3f(1.0f, 0.6f, 0.05f);
							if (PvMsg1.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-185);
								glut.glutBitmapString(font, PvMsg1);
							}
							if (PvMsg2.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-165);
								glut.glutBitmapString(font, PvMsg2);
							}
							if (PvMsg3.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-145);
								glut.glutBitmapString(font, PvMsg3);
							}
							if (PvMsg4.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-125);
								glut.glutBitmapString(font, PvMsg4);
							}
							if (PvMsg5.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-105);
								glut.glutBitmapString(font, PvMsg5);
							}
							if (PvMsg6.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-85);
								glut.glutBitmapString(font, PvMsg6);
							}
							if (PvMsg7.compareTo("")!=0) {
								gl.glRasterPos2i((int)actionBar.getChat().getPosition().getX()+20, (int)(actionBar.getChat().getPosition().getY())-65);
								glut.glutBitmapString(font, PvMsg7);
							}
							break;

						}
						gl.glPopAttrib();
					}
				}

				// If it is not opened
			} else {
				// It's must be at the default position to set invisible the
				// window
				// So we do a scroll effect
				// 70 = ActionBar.HEIGHT(40) + 30 (CHAT.HEIGHT (small))
				if (displayActionBarCpt >= ActionBar.ACTIONBARMAP_HEIGHT) {
					displayChatCpt = (displayChatCpt > 70) ? displayChatCpt - DISPLAY_CHAT_PX_BY_TIC : displayChatCpt;
					displayChatCpt = (displayChatCpt <  70) ? 70 : displayChatCpt;
					gl.glTranslated((screenWidth - 1024) / 2 + 20, screenHeight
							- displayChatCpt, 0);
					gl.glCallList(chat);
					// When is finish we set the buttons
					if (displayChatCpt == 70) {
						gl.glTranslated((screenWidth - 1024) / 2 + 112,
								screenHeight - 65, 0);
						gl.glCallList(channel1);
						gl.glTranslated((screenWidth - 1024) / 2 + 173,
								screenHeight - 65, 0);
						gl.glCallList(channel2);
						gl.glTranslated((screenWidth - 1024) / 2 + 233,
								screenHeight - 65, 0);
						gl.glCallList(channel3);
						gl.glTranslated((screenWidth - 1024) / 2 + 291,
								screenHeight - 65, 0);
						gl.glCallList(channel4);
						gl.glTranslated((screenWidth - 1024) / 2 + 336,
								screenHeight - 67, 0);
						gl.glCallList(upOut);
						gl.glTranslated((screenWidth - 1024) / 2 + 354,
								screenHeight - 67, 0);
						gl.glCallList(lockOut);
						gl.glTranslated((screenWidth - 1024) / 2 + 372,
								screenHeight - 67, 0);
						gl.glCallList(closeOut);
					}
				}
			}
		} else {// We test if the window is always in the screen
			int x = (int) (cursorPos.getX()
					- actionBar.getChat().getDragPoint().getX() + actionBar
					.getChat().getPosition().getX());
			int y = (int) (cursorPos.getY()
					- actionBar.getChat().getDragPoint().getY() + (actionBar
							.getChat().getPosition().getY() - Chat.CHAT_HEIGHT));
			if (x <= 0
					|| y <= 0
					|| x + Chat.CHAT_WIDTH >= screenWidth
					|| y + Chat.CHAT_HEIGHT + ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
				// if it's exited on the left
				if (x <= 0) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(0, 0, 0);
					// Bottom corner
					else if (y + Chat.CHAT_HEIGHT + ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(0, screenHeight - Chat.CHAT_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl.glTranslated(0,
								cursorPos.getY()
								- actionBar.getChat().getDragPoint()
								.getY()
								+ (actionBar.getChat().getPosition()
										.getY() - Chat.CHAT_HEIGHT), 0);
					gl.glCallList(chat);
					// if it's exited on the right
				} else if (x + Chat.CHAT_WIDTH >= screenWidth) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(screenWidth - Chat.CHAT_WIDTH, 0, 0);
					// Bottom corner
					else if (y + Chat.CHAT_HEIGHT + ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(screenWidth - Chat.CHAT_WIDTH,
								screenHeight - Chat.CHAT_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl.glTranslated(screenWidth - Chat.CHAT_WIDTH,
								cursorPos.getY()
								- actionBar.getChat().getDragPoint()
								.getY()
								+ (actionBar.getChat().getPosition()
										.getY() - Chat.CHAT_HEIGHT), 0);
					gl.glCallList(chat);
					// if it's exited on the top
				} else if (y <= 0) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getChat().getDragPoint().getX()
							+ actionBar.getChat().getPosition().getX(), 0, 0);
					gl.glCallList(chat);
					// if it's exited on the bottom
				} else if (y + Chat.CHAT_HEIGHT + ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getChat().getDragPoint().getX()
							+ actionBar.getChat().getPosition().getX(),
							screenHeight - Chat.CHAT_HEIGHT
							- ActionBar.ACTIONBAR_HEIGHT, 0);
					gl.glCallList(chat);
				}
			} else {
				gl.glTranslated(cursorPos.getX()
						- actionBar.getChat().getDragPoint().getX()
						+ actionBar.getChat().getPosition().getX(), cursorPos
						.getY()
						- actionBar.getChat().getDragPoint().getY()
						+ actionBar.getChat().getPosition().getY()
						- Chat.CHAT_HEIGHT, 0);
				gl.glCallList(chat);
			}
		}
	}

	private void displayEquipement(GL gl) {
		// Equipement

		// If it's not dragged
		if (!actionBar.getEquipement().isDragged()) {
			// If it's open
			if (actionBar.getEquipement().isVisible()) {
				// If the equipement isn't set to the default position we
				// display this window directly
				if (actionBar.getEquipement().getPosition().getX() != actionBar
						.getEquipement().getDefaultPosition().getX()
						|| actionBar.getEquipement().getPosition().getY() != actionBar
						.getEquipement().getDefaultPosition().getY()) {
					if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT)
						displayEquipementCpt = Equipement.EQUIPEMENT_HEIGHT;
				}
				// Else we display it we a scroll effect
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT) {
					displayEquipementCpt = (displayEquipementCpt < Equipement.EQUIPEMENT_HEIGHT) ? displayEquipementCpt + DISPLAY_EQUIPEMENT_PX_BY_TIC : displayEquipementCpt;
					displayEquipementCpt = (displayEquipementCpt > Equipement.EQUIPEMENT_HEIGHT) ? Equipement.EQUIPEMENT_HEIGHT : displayEquipementCpt;
					gl.glTranslated(actionBar.getEquipement().getPosition()
							.getX(), actionBar.getEquipement().getPosition()
							.getY()
							- displayEquipementCpt, 0);
					gl.glCallList(equipement);
				}

				// When is finish, we display the equipement's tools
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT
						&& displayEquipementCpt == Equipement.EQUIPEMENT_HEIGHT) {
					if (actionBar.getEquipement().getActiveSet() == Equipement.SET1) {
						gl.glTranslated(actionBar.getEquipement().getPosition()
								.getX() + 25, actionBar.getEquipement()
								.getPosition().getY() - 437, 0);
						gl.glCallList(set1Out);
						gl.glTranslated(actionBar.getEquipement().getPosition()
								.getX() + 130, actionBar.getEquipement()
								.getPosition().getY() - 437, 0);
						gl.glCallList(set2On);
						GuiRep[] tabSet = actionBar.getEquipement()
						.getTabSet1();
						for (int i = 0; i < tabSet.length; i++) {
							if (tabSet[i] != null) {
								if (i < 8)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX() + 14,
											actionBar.getEquipement()
											.getPosition().getY()
											- 405 + i * 38, 0);
								else if (i > 7 && i < 12)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX()
											+ 47 + (i - 8) * 41, actionBar
											.getEquipement().getPosition()
											.getY() - 90, 0);
								else if (i > 11)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX() + 203,
											actionBar.getEquipement()
											.getPosition().getY()
											- 405 + (i - 12) * 38, 0);
								switch (tabSet[i].getType()) {
								case 0:
									gl.glCallList(mediumTraditionalSword);
									break;
								default:
									gl.glCallList(mediumTraditionalSword);
								break;
								// TODO ...
								}
							}
						}
					} else {
						gl.glTranslated(actionBar.getEquipement().getPosition()
								.getX() + 25, actionBar.getEquipement()
								.getPosition().getY() - 437, 0);
						gl.glCallList(set1On);
						gl.glTranslated(actionBar.getEquipement().getPosition()
								.getX() + 130, actionBar.getEquipement()
								.getPosition().getY() - 437, 0);
						gl.glCallList(set2Out);
						GuiRep[] tabSet = actionBar.getEquipement()
						.getTabSet2();
						for (int i = 0; i < tabSet.length; i++) {
							if (tabSet[i] != null) {
								if (i < 8)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX() + 14,
											actionBar.getEquipement()
											.getPosition().getY()
											- 405 + i * 38, 0);
								else if (i > 7 && i < 12)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX()
											+ 47 + (i - 8) * 41, actionBar
											.getEquipement().getPosition()
											.getY() - 90, 0);
								else if (i > 11)
									gl.glTranslated(actionBar.getEquipement()
											.getPosition().getX() + 203,
											actionBar.getEquipement()
											.getPosition().getY()
											- 405 + (i - 12) * 38, 0);
								switch (tabSet[i].getType()) {
								case 0:
									gl.glCallList(mediumTraditionalSword);
									break;
								default:
									gl.glCallList(mediumTraditionalSword);
								break;
								// TODO ...
								}
							}
						}
					}
					gl.glTranslated(actionBar.getEquipement().getPosition()
							.getX() + 186, actionBar.getEquipement()
							.getPosition().getY() - 474, 0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getEquipement().getPosition()
							.getX() + 204, actionBar.getEquipement()
							.getPosition().getY() - 474, 0);
					gl.glCallList(lockOn);
					gl.glTranslated(actionBar.getEquipement().getPosition()
							.getX() + 222, actionBar.getEquipement()
							.getPosition().getY() - 474, 0);
					gl.glCallList(closeOn);
				}
				// If it's not opened
			} else {
				if (displayActionBarCpt >= ActionBar.ACTIONBARMAP_HEIGHT) {
					displayEquipementCpt = (displayEquipementCpt > 70) ? displayEquipementCpt - DISPLAY_EQUIPEMENT_PX_BY_TIC : displayEquipementCpt;
					displayEquipementCpt = (displayEquipementCpt < 70) ? 70 : displayEquipementCpt;
					gl.glTranslated((screenWidth - 1024) / 2 + 406,
							screenHeight - displayEquipementCpt, 0);
					gl.glCallList(equipement);
					if (displayEquipementCpt == 70) {
						gl.glTranslated((screenWidth - 1024) / 2 + 592,
								screenHeight - 67, 0);
						gl.glCallList(upOn);
						gl.glTranslated((screenWidth - 1024) / 2 + 610,
								screenHeight - 67, 0);
						gl.glCallList(lockOn);
						gl.glTranslated((screenWidth - 1024) / 2 + 628,
								screenHeight - 67, 0);
						gl.glCallList(closeOn);
					}
				}
			}
		} else {

			int x = (int) (cursorPos.getX()
					- actionBar.getEquipement().getDragPoint().getX() + actionBar
					.getEquipement().getPosition().getX());
			int y = (int) (cursorPos.getY()
					- actionBar.getEquipement().getDragPoint().getY() + (actionBar
							.getEquipement().getPosition().getY() - Equipement.EQUIPEMENT_HEIGHT));

			if (x <= 0
					|| y <= 0
					|| x + Equipement.EQUIPEMENT_WIDTH >= screenWidth
					|| y + Equipement.EQUIPEMENT_HEIGHT
					+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
				// if it's exited on the left
				if (x <= 0) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(0, 0, 0);
					// Bottom corner
					else if (y + Equipement.EQUIPEMENT_HEIGHT
							+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(0, screenHeight
								- Equipement.EQUIPEMENT_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl
						.glTranslated(
								0,
								cursorPos.getY()
								- actionBar.getEquipement()
								.getDragPoint().getY()
								+ (actionBar.getEquipement()
										.getPosition().getY() - Equipement.EQUIPEMENT_HEIGHT),
										0);
					gl.glCallList(equipement);
					// if it's exited on the right
				} else if (x + Equipement.EQUIPEMENT_WIDTH >= screenWidth) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(screenWidth
								- Equipement.EQUIPEMENT_WIDTH, 0, 0);
					// Bottom corner
					else if (y + Equipement.EQUIPEMENT_HEIGHT
							+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(screenWidth
								- Equipement.EQUIPEMENT_WIDTH, screenHeight
								- Equipement.EQUIPEMENT_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl
						.glTranslated(
								screenWidth
								- Equipement.EQUIPEMENT_WIDTH,
								cursorPos.getY()
								- actionBar.getEquipement()
								.getDragPoint().getY()
								+ (actionBar.getEquipement()
										.getPosition().getY() - Equipement.EQUIPEMENT_HEIGHT),
										0);
					gl.glCallList(equipement);
					// if it's exited on the top
				} else if (y <= 0) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getEquipement().getDragPoint().getX()
							+ actionBar.getEquipement().getPosition().getX(),
							0, 0);
					gl.glCallList(equipement);
					// if it's exited on the bottom
				} else if (y + Equipement.EQUIPEMENT_HEIGHT
						+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getEquipement().getDragPoint().getX()
							+ actionBar.getEquipement().getPosition().getX(),
							screenHeight - Equipement.EQUIPEMENT_HEIGHT
							- ActionBar.ACTIONBAR_HEIGHT, 0);
					gl.glCallList(equipement);
				}
			} else {
				gl.glTranslated(cursorPos.getX() - actionBar.getEquipement().getDragPoint().getX() + actionBar.getEquipement().getPosition().getX(),
						cursorPos.getY() - actionBar.getEquipement().getDragPoint().getY() + (actionBar.getEquipement().getPosition().getY() - Equipement.EQUIPEMENT_HEIGHT),0);
				gl.glCallList(equipement);
			}
		}

	}

	private void displayInventory(GL gl) {
		// Inventory

		// If it's not dragged
		if (!actionBar.getInventory().isDragged()) {
			// If it's open
			if (actionBar.getInventory().isVisible()) {
				// If the Inventory isn't set to the default position we display
				// this window directly
				if (actionBar.getInventory().getPosition().getX() != actionBar
						.getInventory().getDefaultPosition().getX()
						|| actionBar.getInventory().getPosition().getY() != actionBar
						.getInventory().getDefaultPosition().getY()) {
					if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT)
						displayInventoryCpt = Inventory.INVENTORY_HEIGHT;
				}
				// Else we display it we a scroll effect
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT) {
					displayInventoryCpt = (displayInventoryCpt < Inventory.INVENTORY_HEIGHT) ? displayInventoryCpt + DISPLAY_INVENTORY_PX_BY_TIC : displayInventoryCpt;
					displayInventoryCpt = (displayInventoryCpt > Inventory.INVENTORY_HEIGHT) ? Inventory.INVENTORY_HEIGHT : displayInventoryCpt;
					gl.glTranslated(actionBar.getInventory().getPosition()
							.getX(), actionBar.getInventory().getPosition()
							.getY()
							- displayInventoryCpt, 0);
					gl.glCallList(inventory);
				}

				// When is finish, we display the Inventory's tools
				if (displayActionBarCpt == ActionBar.ACTIONBARMAP_HEIGHT
						&& displayInventoryCpt == Inventory.INVENTORY_HEIGHT) {
					GuiRep[][] tabBag = actionBar.getInventory().getBagGui();
					for (int i = 0; i < Inventory.NB_BAGS; i++) {
						if (i < 3) {
							gl.glTranslated(actionBar.getInventory()
									.getPosition().getX()
									+ (i * 41) + 22, actionBar.getInventory()
									.getPosition().getY() - 413, 0);
							gl.glCallList(bag01);
							// txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+(i*41)+35,(int)(screenHeight-actionBar.getInventory().getPosition().getY()-ActionBar.ACTIONBAR_HEIGHT+445),""+(i+1),0.0f,0.5f,0.5f);
						} else if (i > 2 && i < 6) {
							gl.glTranslated(actionBar.getInventory()
									.getPosition().getX()
									+ ((i - 3) * 41) + 22, actionBar
									.getInventory().getPosition().getY() - 373,
									0);
							gl.glCallList(bag01);
							// txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+((i-3)*41)+35,(int)(screenHeight-actionBar.getInventory().getPosition().getY()-ActionBar.ACTIONBAR_HEIGHT+405),""+(i+1),0.0f,0.5f,0.5f);
						} else if (i > 5) {
							gl.glTranslated(actionBar.getInventory()
									.getPosition().getX()
									+ ((i - 6) * 41) + 22, actionBar
									.getInventory().getPosition().getY() - 332,
									0);
							gl.glCallList(bag02);
							// txt.glPrintc(gl,(int)actionBar.getInventory().getPosition().getX()+((i-6)*41)+35,(int)(screenHeight-actionBar.getInventory().getPosition().getY()-ActionBar.ACTIONBAR_HEIGHT+365),""+(i+1),0.0f,0.5f,0.5f);
						}

					}

					for (int i = 0; i < Inventory.BAG_CONTENT; i++) {
						if (tabBag[actionBar.getInventory().getActiveBag()][i] != null) {
							if (i < 3)
								gl.glTranslated(actionBar.getInventory()
										.getPosition().getX()
										+ 22 + (i * 41),
										actionBar.getInventory().getPosition()
										.getY() - 249, 0);
							else if (i > 2 && i < 6)
								gl.glTranslated(actionBar.getInventory()
										.getPosition().getX()
										+ 22 + ((i - 3) * 41),
										actionBar.getInventory().getPosition()
										.getY() - 209, 0);
							else if (i > 5 && i < 9)
								gl.glTranslated(actionBar.getInventory()
										.getPosition().getX()
										+ 22 + ((i - 6) * 41),
										actionBar.getInventory().getPosition()
										.getY() - 169, 0);
							else if (i > 8 && i < 12)
								gl.glTranslated(actionBar.getInventory()
										.getPosition().getX()
										+ 22 + ((i - 9) * 41),
										actionBar.getInventory().getPosition()
										.getY() - 129, 0);
							else if (i > 11)
								gl.glTranslated(actionBar.getInventory()
										.getPosition().getX()
										+ 22 + ((i - 12) * 41),
										actionBar.getInventory().getPosition()
										.getY() - 89, 0);
							switch (tabBag[actionBar.getInventory()
							               .getActiveBag()][i].getType()) {
							               case 0:
							            	   gl.glCallList(mediumTraditionalSword);
							            	   break;
							               default:
							            	   gl.glCallList(mediumTraditionalSword);
							               break;
							               // TODO ...

							}
						}
					}

					gl.glTranslated(actionBar.getInventory().getPosition()
							.getX() + 101, actionBar.getInventory()
							.getPosition().getY() - 474, 0);
					gl.glCallList(upOut);
					gl.glTranslated(actionBar.getInventory().getPosition()
							.getX() + 119, actionBar.getInventory()
							.getPosition().getY() - 474, 0);
					gl.glCallList(lockOn);
					gl.glTranslated(actionBar.getInventory().getPosition()
							.getX() + 137, actionBar.getInventory()
							.getPosition().getY() - 474, 0);
					gl.glCallList(closeOn);
				}
				// If it's not opened
			} else {
				if (displayActionBarCpt >= ActionBar.ACTIONBARMAP_HEIGHT) {
					displayInventoryCpt = (displayInventoryCpt > 70) ? displayInventoryCpt - DISPLAY_INVENTORY_PX_BY_TIC : displayInventoryCpt;
					displayInventoryCpt = (displayInventoryCpt < 70) ? 70 : displayInventoryCpt;
					gl.glTranslated((screenWidth - 1024) / 2 + 661,
							screenHeight - displayInventoryCpt, 0);
					gl.glCallList(inventory);
					if (displayInventoryCpt == 70) {
						gl.glTranslated((screenWidth - 1024) / 2 + 762,
								screenHeight - 67, 0);
						gl.glCallList(upOn);
						gl.glTranslated((screenWidth - 1024) / 2 + 780,
								screenHeight - 67, 0);
						gl.glCallList(lockOn);
						gl.glTranslated((screenWidth - 1024) / 2 + 798,
								screenHeight - 67, 0);
						gl.glCallList(closeOn);
					}
				}
			}
		} else {

			int x = (int) (cursorPos.getX()
					- actionBar.getInventory().getDragPoint().getX() + actionBar
					.getInventory().getPosition().getX());
			int y = (int) (cursorPos.getY()
					- actionBar.getInventory().getDragPoint().getY() + (actionBar
							.getInventory().getPosition().getY() - Inventory.INVENTORY_HEIGHT));

			if (x <= 0
					|| y <= 0
					|| x + Inventory.INVENTORY_WIDTH >= screenWidth
					|| y + Inventory.INVENTORY_HEIGHT
					+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
				// if it's exited on the left
				if (x <= 0) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(0, 0, 0);
					// Bottom corner
					else if (y + Inventory.INVENTORY_HEIGHT
							+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(0, screenHeight
								- Inventory.INVENTORY_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl.glTranslated(0, cursorPos.getY()
								- actionBar.getInventory().getDragPoint()
								.getY()
								+ (actionBar.getInventory().getPosition()
										.getY() - Inventory.INVENTORY_HEIGHT),
										0);
					gl.glCallList(inventory);
					// if it's exited on the right
				} else if (x + Inventory.INVENTORY_WIDTH >= screenWidth) {
					// Top corner
					if (y <= 0)
						gl.glTranslated(
								screenWidth - Inventory.INVENTORY_WIDTH, 0, 0);
					// Bottom corner
					else if (y + Inventory.INVENTORY_HEIGHT
							+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight)
						gl.glTranslated(
								screenWidth - Inventory.INVENTORY_WIDTH,
								screenHeight - Inventory.INVENTORY_HEIGHT
								- ActionBar.ACTIONBAR_HEIGHT, 0);
					// No corner
					else
						gl
						.glTranslated(
								screenWidth - Inventory.INVENTORY_WIDTH,
								cursorPos.getY()
								- actionBar.getInventory()
								.getDragPoint().getY()
								+ (actionBar.getInventory()
										.getPosition().getY() - Inventory.INVENTORY_HEIGHT),
										0);
					gl.glCallList(inventory);
					// if it's exited on the top
				} else if (y <= 0) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getInventory().getDragPoint().getX()
							+ actionBar.getInventory().getPosition().getX(), 0,
							0);
					gl.glCallList(inventory);
					// if it's exited on the bottom
				} else if (y + Inventory.INVENTORY_HEIGHT
						+ ActionBar.ACTIONBAR_HEIGHT >= screenHeight) {
					gl.glTranslated(cursorPos.getX()
							- actionBar.getInventory().getDragPoint().getX()
							+ actionBar.getInventory().getPosition().getX(),
							screenHeight - Inventory.INVENTORY_HEIGHT
							- ActionBar.ACTIONBAR_HEIGHT, 0);
					gl.glCallList(inventory);
				}
			} else {
				gl.glTranslated(cursorPos.getX()
						- actionBar.getInventory().getDragPoint().getX()
						+ actionBar.getInventory().getPosition().getX(),
						cursorPos.getY()
						- actionBar.getInventory().getDragPoint()
						.getY()
						+ (actionBar.getInventory().getPosition()
								.getY() - Inventory.INVENTORY_HEIGHT),
								0);
				gl.glCallList(inventory);
			}
		}

	}

	private void displayDebug(GL gl) {

		// ****************************************************************\\
		// ************************* D E B U G ****************************\\
		// ************************* S T A R T ****************************\\
		// ****************************************************************\\


		if (debugMode) {

			gl.glDisable(GL.GL_TEXTURE_2D);

			debugRectangle(gl, actionBar.getActionBarSite(), 0.0f, 0.0f, 1.0f);
			debugCircle(gl, actionBar.getActionMapSite(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getQuestSite(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getOptionSite(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction1Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction2Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction3Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction4Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction5Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction6Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction7Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction8Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction9Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction10Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction11Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction12Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction13Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction14Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction15Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction16Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction17Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction18Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction19Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getAction20Site(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getPreviousSite(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getNextSite(), 0.0f, 0.0f, 1.0f);
			debugRectangle(gl, actionBar.getZoneSite(), 0.0f, 0.0f, 1.0f);
			debugCircle(gl, actionBar.getWorldSite(), 0.0f, 0.0f, 1.0f);

			if (actionBar.getChat().isVisible()) {
				debugRectangle(gl, actionBar.getChat().getChatOpenSite(), 1.0f,
						0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getChatDragBarSite(),
						1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGeneralOpenChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGuildOpenChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGroupOpenChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getPrivateOpenChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getUpOpenSite(), 1.0f,
						0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getLockOpenSite(), 1.0f,
						0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getCloseOpenSite(),
						1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getEditableOpenSite(),
						1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getEmotesOpenSite(),
						1.0f, 0.0f, 0.0f);
			} else {
				debugRectangle(gl, actionBar.getChat().getChatCloseSite(),
						1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGeneralCloseChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGuildCloseChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getGroupCloseChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat()
						.getPrivateCloseChannelSite(), 1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getUpCloseSite(), 1.0f,
						0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getLockCloseSite(),
						1.0f, 0.0f, 0.0f);
				debugRectangle(gl, actionBar.getChat().getCloseCloseSite(),
						1.0f, 0.0f, 0.0f);
			}
			if (actionBar.getEquipement().isVisible()) {
				debugRectangle(gl, actionBar.getEquipement()
						.getEquipementOpenSite(), 0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement()
						.getEquipementDragBarSite(), 0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement().getSet1OpenSite(),
						0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement().getSet2OpenSite(),
						0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement().getUpOpenSite(),
						0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement().getLockOpenSite(),
						0.0f, 1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getCloseOpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet01OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet02OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet03OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet04OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet05OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet06OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet07OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet08OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet09OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet10OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet11OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet12OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet13OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet14OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet15OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet16OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet17OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet18OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet19OpenSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getSet20OpenSite(), 0.0f,
						1.0f, 0.0f);
			} else {
				debugRectangle(gl, actionBar.getEquipement()
						.getEquipementCloseSite(), 0.0f, 1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement().getUpCloseSite(),
						0.0f, 1.0f, 0.0f);
				debugRectangle(gl,
						actionBar.getEquipement().getLockCloseSite(), 0.0f,
						1.0f, 0.0f);
				debugRectangle(gl, actionBar.getEquipement()
						.getCloseCloseSite(), 0.0f, 1.0f, 0.0f);
			}
			if (actionBar.getInventory().isVisible()) {
				debugRectangle(gl, actionBar.getInventory()
						.getInventoryOpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getInventoryDragBarSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getUpOpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getLockOpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getCloseOpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag01OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag02OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag03OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag04OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag05OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag06OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag07OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag08OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getBag09OpenSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent01OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent02OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent03OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent04OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent05OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent06OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent07OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent08OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent09OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent10OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent11OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent12OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent13OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent14OpenSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory()
						.getContent15OpenSite(), 1.0f, 0.0f, 1.0f);
			} else {
				debugRectangle(gl, actionBar.getInventory()
						.getInventoryCloseSite(), 1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getUpCloseSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl, actionBar.getInventory().getLockCloseSite(),
						1.0f, 0.0f, 1.0f);
				debugRectangle(gl,
						actionBar.getInventory().getCloseCloseSite(), 1.0f,
						0.0f, 1.0f);

			}

			gl.glEnable(GL.GL_TEXTURE_2D);
		}
	}

	public void setActionSelected(int id) {

		if (!actionSelected) {
			actionSelected = true;
			int[] tabAction = actionBar.getTabAction();
			actionSelect = tabAction[id];
			actionBar.deleteAction(id);
		}
	}

	public void setActionNotSelected(int id) {

		if (actionSelected) {
			actionSelected = false;
			int[] tabAction = actionBar.getTabAction();
			if (tabAction[id] == 0) {
				actionBar.addAction(actionSelect, id);
			} else {
				int actionSelectTmp = actionSelect;
				actionSelect = tabAction[id];
				actionSelected = true;
				actionBar.deleteAction(id);
				actionBar.addAction(actionSelectTmp, id);

			}
		}
	}

	// TODO CHANGER LES DROPGUI ET PICKGUI PAR DES ITEMREP
	public void setItemSelectedInInventory(int bag, int content) {
		
		if (!itemSelected) {
			soundEngine.play("data/sounds/gui/select.ogg",false);
			itemSelected = true;
			GuiRep[][] tabItem = actionBar.getInventory().getBagGui();
			itemSelect = tabItem[bag][content];
			actionBar.getInventory().pickGui(itemSelect, bag, content);
		}
	}

	public void setItemDropInInventory(int bag, int content) {

		soundEngine.play("data/sounds/gui/drop.ogg",false);
		
		if (itemSelected) {
			itemSelected = false;
			GuiRep[][] tabItem = actionBar.getInventory().getBagGui();
			if (tabItem[bag][content] == null) {
				actionBar.getInventory().dropGui(itemSelect, bag, content);
			} else {
				GuiRep itemSelectTmp = itemSelect;
				itemSelect = tabItem[bag][content];
				itemSelected = true;
				actionBar.getInventory().pickGui(itemSelect, bag, content);
				actionBar.getInventory().dropGui(itemSelectTmp, bag, content);

			}
		}
	}

	public void setItemDropInInventory(int bag) {

		soundEngine.play("data/sounds/gui/drop.ogg",false);
		
		if (itemSelected) {
			itemSelected = false;
			GuiRep[][] tabItem = actionBar.getInventory().getBagGui();
			if (!actionBar.getInventory().bagFull(tabItem[bag])) {
				actionBar.getInventory().dropGui(itemSelect, bag);
			}
		}
	}

	public void setItemSelectedInEquipement(int id) {
	
		if (!itemSelected) {
			soundEngine.play("data/sounds/gui/select.ogg",false);
			itemSelected = true;
			GuiRep[] tabItem;
			if (actionBar.getEquipement().getActiveSet() == Equipement.SET1) {
				tabItem = actionBar.getEquipement().getTabSet1();
			} else {
				tabItem = actionBar.getEquipement().getTabSet2();
			}
			itemSelect = tabItem[id];
			actionBar.getEquipement().removeEquipement(id,
					actionBar.getEquipement().getActiveSet());
		}
	}

	public void setItemDropInEquipement(int id) {

		soundEngine.play("data/sounds/gui/drop.ogg",false);
		
		if (itemSelected) {
			itemSelected = false;
			GuiRep[] tabItem;
			if (actionBar.getEquipement().getActiveSet() == Equipement.SET1) {
				tabItem = actionBar.getEquipement().getTabSet1();
			} else {
				tabItem = actionBar.getEquipement().getTabSet2();
			}
			if (tabItem[id] == null) {
				actionBar.getEquipement().addEquipement(itemSelect, id,
						actionBar.getEquipement().getActiveSet());
			} else {
				GuiRep itemSelectTmp = itemSelect;
				itemSelect = tabItem[id];
				itemSelected = true;
				actionBar.getEquipement().removeEquipement(id,
						actionBar.getEquipement().getActiveSet());
				actionBar.getEquipement().addEquipement(itemSelectTmp, id,
						actionBar.getEquipement().getActiveSet());
			}
		}
	}

	/**
	 * reshapeGui fonction
	 * 
	 * @param gLdrawable
	 *            the GLDrawable fonction
	 * @param xstart
	 *            the x start
	 * @param ystart
	 *            the y start
	 * @param width
	 *            the width
	 * @param height
	 *            the hieght
	 */

	public void reshapeGUI(GLAutoDrawable gLdrawable, int xstart, int ystart,
			int width, int height) {

		GL gl = gLdrawable.getGL();
		GLU glu = new GLU();

		height = (height == 0) ? 1 : height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45, (float) width / height, 1, 1000);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	/**
	 * displayChangedGUI fonction
	 * 
	 * @param gLDrawable
	 * @param modeChanged
	 *            boolean
	 * @param DEVELOPPEMENTiceChanged
	 *            boolean NOT WORK WITH JAVA
	 */

	public void displayChangedGUI(GLDrawable gLDrawable, boolean modeChanged,
			boolean DEVELOPPEMENTiceChanged) {
	}

	/**
	 * 
	 * @param rec
	 */
	public void debugRectangle(GL gl, Rectangle rec, float c1, float c2,
			float c3) {


		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glColor3f(c1, c2, c3);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(rec.getX(), rec.getY());
		gl.glVertex2d(rec.getX(), rec.getY() + rec.getHeight());
		gl
		.glVertex2d(rec.getX() + rec.getWidth(), rec.getY()
				+ rec.getHeight());
		gl.glVertex2d(rec.getX() + rec.getWidth(), rec.getY());
		gl.glEnd();
		gl.glPopMatrix();

	}

	public void debugCircle(GL gl, Circle c, float c1, float c2, float c3) {

		double DEG2RAD = 3.14159 / 180;

		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glColor3f(c1, c2, c3);

		gl.glBegin(GL.GL_LINE_LOOP);
		for (int i = 0; i < 360; i++) {
			gl.glVertex2d(c.getX() + Math.cos(i * DEG2RAD) * c.getRadius(), c
					.getY()
					+ Math.sin(i * DEG2RAD) * c.getRadius());
		}
		gl.glEnd();
		gl.glPopMatrix();

	}

	public void debugAction(GL gl, String action) {

		gl.glLoadIdentity();
		gl.glPushMatrix();

		// txt.setFontSize(30,gl);

		cptDebugAction++;

		if (cptDebugAction <= 400) {
			// The message wicht the player write
			// txt.glPrintc(gl,400,400,action,1.0f,0.0f,0.0f);
		} else {
			cptDebugAction = 0;
			this.action = "";
		}


		gl.glPopMatrix();

	}

	public void run() {

		if(System.currentTimeMillis() - lastTicTime >= 1000){
			actualFps = fpsCount;
			fpsCount = 0;
			lastTicTime = System.currentTimeMillis();
		}

		DISPLAY_ACTIONBAR_PX_BY_TIC = ActionBar.ACTIONBARMAP_HEIGHT/(5+this.actualFps);
		DISPLAY_CHAT_PX_BY_TIC  = Chat.CHAT_HEIGHT/(5+this.actualFps);
		DISPLAY_EQUIPEMENT_PX_BY_TIC = Equipement.EQUIPEMENT_HEIGHT/(5+this.actualFps);
		DISPLAY_INVENTORY_PX_BY_TIC =  Inventory.INVENTORY_HEIGHT/(5+this.actualFps);
		CURSOR_FLICK_TIME = this.actualFps*3;

	}

	/**
	 * buildLists fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable object Build the display list (faster)
	 */

	public void buildLists(GLAutoDrawable gLDrawable) {

		Logger.printINFO("Building display lists...");

		int cpt = 0;

		GL gl = gLDrawable.getGL();

		actionBarNum = gl.glGenLists(NBTEXTURES);
		gl.glNewList(actionBarNum, GL.GL_COMPILE);
		draw(gLDrawable, 1024, 256, cpt++);
		gl.glEndList();

		chat = actionBarNum + 1;
		gl.glNewList(chat, GL.GL_COMPILE);
		draw(gLDrawable, 512, 256, cpt++);
		gl.glEndList();

		equipement = chat + 1;
		gl.glNewList(equipement, GL.GL_COMPILE);
		draw(gLDrawable, 256, 512, cpt++);
		gl.glEndList();

		inventory = equipement + 1;
		gl.glNewList(inventory, GL.GL_COMPILE);
		draw(gLDrawable, 256, 512, cpt++);
		gl.glEndList();

		smallBar = inventory + 1;
		gl.glNewList(smallBar, GL.GL_COMPILE);
		draw(gLDrawable, 512, 256, cpt++); // 5
		gl.glEndList();

		quest = smallBar + 1;
		gl.glNewList(quest, GL.GL_COMPILE);
		draw(gLDrawable, 512, 256, cpt++);
		gl.glEndList();

		previous01 = quest + 1;
		gl.glNewList(previous01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 64, cpt++);
		gl.glEndList();

		previous02 = previous01 + 1;
		gl.glNewList(previous02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 64, cpt++);
		gl.glEndList();

		next01 = previous02 + 1;
		gl.glNewList(next01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 64, cpt++);
		gl.glEndList();

		next02 = next01 + 1;
		gl.glNewList(next02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 64, cpt++);
		gl.glEndList();

		zone01 = next02 + 1;
		gl.glNewList(zone01, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		zone02 = zone01 + 1;
		gl.glNewList(zone02, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		world01 = zone02 + 1;
		gl.glNewList(world01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		world02 = world01 + 1;
		gl.glNewList(world02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		upOn = world02 + 1;
		gl.glNewList(upOn, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		upOut = upOn + 1;
		gl.glNewList(upOut, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		lockOn = upOut + 1;
		gl.glNewList(lockOn, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		lockOut = lockOn + 1;
		gl.glNewList(lockOut, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		closeOn = lockOut + 1;
		gl.glNewList(closeOn, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		closeOut = closeOn + 1;
		gl.glNewList(closeOut, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		emoteOn = closeOut + 1;
		gl.glNewList(emoteOn, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		emoteOut = emoteOn + 1;
		gl.glNewList(emoteOut, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		channel1 = emoteOut + 1;
		gl.glNewList(channel1, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		channel2 = channel1 + 1;
		gl.glNewList(channel2, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		channel3 = channel2 + 1;
		gl.glNewList(channel3, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		channel4 = channel3 + 1;
		gl.glNewList(channel4, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		channelOut = channel4 + 1;
		gl.glNewList(channelOut, GL.GL_COMPILE);
		draw(gLDrawable, 16, 16, cpt++);
		gl.glEndList();

		set1On = channelOut + 1;
		gl.glNewList(set1On, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		set1Out = set1On + 1;
		gl.glNewList(set1Out, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		set2On = set1Out + 1;
		gl.glNewList(set2On, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		set2Out = set2On + 1;
		gl.glNewList(set2Out, GL.GL_COMPILE);
		draw(gLDrawable, 128, 32, cpt++);
		gl.glEndList();

		quest01 = set2Out + 1;
		gl.glNewList(quest01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		quest02 = quest01 + 1;
		gl.glNewList(quest02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		option01 = quest02 + 1;
		gl.glNewList(option01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		option02 = option01 + 1;
		gl.glNewList(option02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		chatCursor = option02 + 1;
		gl.glNewList(chatCursor, GL.GL_COMPILE);
		draw(gLDrawable, 16, 32, cpt++);
		gl.glEndList();

		bag01 = chatCursor + 1;
		gl.glNewList(bag01, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		bag02 = bag01 + 1;
		gl.glNewList(bag02, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		aggravees = bag02 + 1;
		gl.glNewList(aggravees, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		bdf = aggravees + 1;
		gl.glNewList(bdf, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 190
		gl.glEndList();

		colonne = bdf + 1;
		gl.glNewList(colonne, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		concentration = colonne + 1;
		gl.glNewList(concentration, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		connaissance = concentration + 1;
		gl.glNewList(connaissance, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		deflagration = connaissance + 1;
		gl.glNewList(deflagration, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		expansion = deflagration + 1;
		gl.glNewList(expansion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 195
		gl.glEndList();

		explosion = expansion + 1;
		gl.glNewList(explosion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		implosion = explosion + 1;
		gl.glNewList(implosion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		insensibilite = implosion + 1;
		gl.glNewList(insensibilite, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		meteore = insensibilite + 1;
		gl.glNewList(meteore, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mur = meteore + 1;
		gl.glNewList(mur, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 200
		gl.glEndList();

		nuee = mur + 1;
		gl.glNewList(nuee, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		persistantes = nuee + 1;
		gl.glNewList(persistantes, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		pyromanie = persistantes + 1;
		gl.glNewList(pyromanie, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		vague = pyromanie + 1;
		gl.glNewList(vague, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		armureFroid = vague + 1;
		gl.glNewList(armureFroid, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		armureGel = armureFroid + 1;
		gl.glNewList(armureGel, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		armureGlace = armureGel + 1;
		gl.glNewList(armureGlace, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		blizzard = armureGlace + 1;
		gl.glNewList(blizzard, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		cone = blizzard + 1;
		gl.glNewList(cone, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		coneAmeliore = cone + 1;
		gl.glNewList(coneAmeliore, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 210
		gl.glEndList();

		fils = coneAmeliore + 1;
		gl.glNewList(fils, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		gel = fils + 1;
		gl.glNewList(gel, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		nova = gel + 1;
		gl.glNewList(nova, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		pere = nova + 1;
		gl.glNewList(pere, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		pic = pere + 1;
		gl.glNewList(pic, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		picAmeliore = pic + 1;
		gl.glNewList(picAmeliore, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		serment = picAmeliore + 1;
		gl.glNewList(serment, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		trait = serment + 1;
		gl.glNewList(trait, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		traitAmeliore = trait + 1;
		gl.glNewList(traitAmeliore, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		vagueFroid = traitAmeliore + 1;
		gl.glNewList(vagueFroid, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		vagueFroidAmeliore = vagueFroid + 1;
		gl.glNewList(vagueFroidAmeliore, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalSword = vagueFroidAmeliore + 1;
		gl.glNewList(mediumTraditionalSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();
		/*
		smallTraditionalPotion = mediumTraditionalSword + 1;
		gl.glNewList(smallTraditionalPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalPotion = smallTraditionalPotion + 1;
		gl.glNewList(mediumTraditionalPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalPotion = mediumTraditionalPotion + 1;
		gl.glNewList(largeTraditionalPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicPotion = largeTraditionalPotion + 1;
		gl.glNewList(smallMagicPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicPotion = smallMagicPotion + 1;
		gl.glNewList(mediumMagicPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 10
		gl.glEndList();

		largeMagicPotion = mediumMagicPotion + 1;
		gl.glNewList(largeMagicPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questPotion = largeMagicPotion + 1;
		gl.glNewList(questPotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rarePotion = questPotion + 1;
		gl.glNewList(rarePotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singlePotion = rarePotion + 1;
		gl.glNewList(singlePotion, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalAmulet = singlePotion + 1;
		gl.glNewList(smallTraditionalAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 15
		gl.glEndList();

		mediumTraditionalAmulet = smallTraditionalAmulet + 1;
		gl.glNewList(mediumTraditionalAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalAmulet = mediumTraditionalAmulet + 1;
		gl.glNewList(largeTraditionalAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicAmulet = largeTraditionalAmulet + 1;
		gl.glNewList(smallMagicAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicAmulet = smallMagicAmulet + 1;
		gl.glNewList(mediumMagicAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicAmulet = mediumMagicAmulet + 1;
		gl.glNewList(largeMagicAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 20
		gl.glEndList();

		questAmulet = largeMagicAmulet + 1;
		gl.glNewList(questAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareAmulet = questAmulet + 1;
		gl.glNewList(rareAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleAmulet = rareAmulet + 1;
		gl.glNewList(singleAmulet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalBracelet = singleAmulet + 1;
		gl.glNewList(smallTraditionalBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalBracelet = smallTraditionalBracelet + 1;
		gl.glNewList(mediumTraditionalBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 25
		gl.glEndList();

		largeTraditionalBracelet = mediumTraditionalBracelet + 1;
		gl.glNewList(largeTraditionalBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicBracelet = largeTraditionalBracelet + 1;
		gl.glNewList(smallMagicBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicBracelet = smallMagicBracelet + 1;
		gl.glNewList(mediumMagicBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicBracelet = mediumMagicBracelet + 1;
		gl.glNewList(largeMagicBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questBracelet = largeMagicBracelet + 1;
		gl.glNewList(questBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 30
		gl.glEndList();

		rareBracelet = questBracelet + 1;
		gl.glNewList(rareBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleBracelet = rareBracelet + 1;
		gl.glNewList(singleBracelet, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalRing = singleBracelet + 1;
		gl.glNewList(smallTraditionalRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalRing = smallTraditionalRing + 1;
		gl.glNewList(mediumTraditionalRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalRing = mediumTraditionalRing + 1;
		gl.glNewList(largeTraditionalRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 35
		gl.glEndList();

		smallMagicRing = largeTraditionalRing + 1;
		gl.glNewList(smallMagicRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicRing = smallMagicRing + 1;
		gl.glNewList(mediumMagicRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicRing = mediumMagicRing + 1;
		gl.glNewList(largeMagicRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questRing = largeMagicRing + 1;
		gl.glNewList(questRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareRing = questRing + 1;
		gl.glNewList(rareRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 40
		gl.glEndList();

		singleRing = rareRing + 1;
		gl.glNewList(singleRing, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalBuckle = singleRing + 1;
		gl.glNewList(smallTraditionalBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalBuckle = smallTraditionalBuckle + 1;
		gl.glNewList(mediumTraditionalBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalBuckle = mediumTraditionalBuckle + 1;
		gl.glNewList(largeTraditionalBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicBuckle = largeTraditionalBuckle + 1;
		gl.glNewList(smallMagicBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 45
		gl.glEndList();

		mediumMagicBuckle = smallMagicBuckle + 1;
		gl.glNewList(mediumMagicBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicBuckle = mediumMagicBuckle + 1;
		gl.glNewList(largeMagicBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questBuckle = largeMagicBuckle + 1;
		gl.glNewList(questBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareBuckle = questBuckle + 1;
		gl.glNewList(rareBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleBuckle = rareBuckle + 1;
		gl.glNewList(singleBuckle, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 50
		gl.glEndList();

		smallTraditionalHelm = singleBuckle + 1;
		gl.glNewList(smallTraditionalHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalHelm = smallTraditionalHelm + 1;
		gl.glNewList(mediumTraditionalHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalHelm = mediumTraditionalHelm + 1;
		gl.glNewList(largeTraditionalHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicHelm = largeTraditionalHelm + 1;
		gl.glNewList(smallMagicHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicHelm = smallMagicHelm + 1;
		gl.glNewList(mediumMagicHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 55
		gl.glEndList();

		largeMagicHelm = mediumMagicHelm + 1;
		gl.glNewList(largeMagicHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questHelm = largeMagicHelm + 1;
		gl.glNewList(questHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareHelm = questHelm + 1;
		gl.glNewList(rareHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleHelm = rareHelm + 1;
		gl.glNewList(singleHelm, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalWrist = singleHelm + 1;
		gl.glNewList(smallTraditionalWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 60
		gl.glEndList();

		mediumTraditionalWrist = smallTraditionalWrist + 1;
		gl.glNewList(mediumTraditionalWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalWrist = mediumTraditionalWrist + 1;
		gl.glNewList(largeTraditionalWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicWrist = largeTraditionalWrist + 1;
		gl.glNewList(smallMagicWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicWrist = smallMagicWrist + 1;
		gl.glNewList(mediumMagicWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicWrist = mediumMagicWrist + 1;
		gl.glNewList(largeMagicWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 65
		gl.glEndList();

		questWrist = largeMagicWrist + 1;
		gl.glNewList(questWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareWrist = questWrist + 1;
		gl.glNewList(rareWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleWrist = rareWrist + 1;
		gl.glNewList(singleWrist, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalArmor = singleWrist + 1;
		gl.glNewList(smallTraditionalArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalArmor = smallTraditionalArmor + 1;
		gl.glNewList(mediumTraditionalArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalArmor = mediumTraditionalArmor + 1;
		gl.glNewList(largeTraditionalArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicArmor = largeTraditionalArmor + 1;
		gl.glNewList(smallMagicArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicArmor = smallMagicArmor + 1;
		gl.glNewList(mediumMagicArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicArmor = mediumMagicArmor + 1;
		gl.glNewList(largeMagicArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questArmor = largeMagicArmor + 1;
		gl.glNewList(questArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 75
		gl.glEndList();

		rareArmor = questArmor + 1;
		gl.glNewList(rareArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleArmor = rareArmor + 1;
		gl.glNewList(singleArmor, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalGlove = singleArmor + 1;
		gl.glNewList(smallTraditionalGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalGlove = smallTraditionalGlove + 1;
		gl.glNewList(mediumTraditionalGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalGlove = mediumTraditionalGlove + 1;
		gl.glNewList(largeTraditionalGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 80
		gl.glEndList();

		smallMagicGlove = largeTraditionalGlove + 1;
		gl.glNewList(smallMagicGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicGlove = smallMagicGlove + 1;
		gl.glNewList(mediumMagicGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicGlove = mediumMagicGlove + 1;
		gl.glNewList(largeMagicGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questGlove = largeMagicGlove + 1;
		gl.glNewList(questGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareGlove = questGlove + 1;
		gl.glNewList(rareGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 85
		gl.glEndList();

		singleGlove = rareGlove + 1;
		gl.glNewList(singleGlove, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalBelt = singleGlove + 1;
		gl.glNewList(smallTraditionalBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalBelt = smallTraditionalBelt + 1;
		gl.glNewList(mediumTraditionalBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalBelt = mediumTraditionalBelt + 1;
		gl.glNewList(largeTraditionalBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicBelt = largeTraditionalBelt + 1;
		gl.glNewList(smallMagicBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 90
		gl.glEndList();

		mediumMagicBelt = smallMagicBelt + 1;
		gl.glNewList(mediumMagicBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicBelt = mediumMagicBelt + 1;
		gl.glNewList(largeMagicBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questBelt = largeMagicBelt + 1;
		gl.glNewList(questBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareBelt = questBelt + 1;
		gl.glNewList(rareBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleBelt = rareBelt + 1;
		gl.glNewList(singleBelt, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 95
		gl.glEndList();

		smallTraditionalTrousers = singleBelt + 1;
		gl.glNewList(smallTraditionalTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalTrousers = smallTraditionalTrousers + 1;
		gl.glNewList(mediumTraditionalTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalTrousers = mediumTraditionalTrousers + 1;
		gl.glNewList(largeTraditionalTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicTrousers = largeTraditionalTrousers + 1;
		gl.glNewList(smallMagicTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicTrousers = smallMagicTrousers + 1;
		gl.glNewList(mediumMagicTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 100
		gl.glEndList();

		largeMagicTrousers = mediumMagicTrousers + 1;
		gl.glNewList(largeMagicTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questTrousers = largeMagicTrousers + 1;
		gl.glNewList(questTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareTrousers = questTrousers + 1;
		gl.glNewList(rareTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleTrousers = rareTrousers + 1;
		gl.glNewList(singleTrousers, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalTibia = singleTrousers + 1;
		gl.glNewList(smallTraditionalTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 105
		gl.glEndList();

		mediumTraditionalTibia = smallTraditionalTibia + 1;
		gl.glNewList(mediumTraditionalTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalTibia = mediumTraditionalTibia + 1;
		gl.glNewList(largeTraditionalTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicTibia = largeTraditionalTibia + 1;
		gl.glNewList(smallMagicTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicTibia = smallMagicTibia + 1;
		gl.glNewList(mediumMagicTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicTibia = mediumMagicTibia + 1;
		gl.glNewList(largeMagicTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questTibia = largeMagicTibia + 1;
		gl.glNewList(questTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareTibia = questTibia + 1;
		gl.glNewList(rareTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleTibia = rareTibia + 1;
		gl.glNewList(singleTibia, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalBoot = singleTibia + 1;
		gl.glNewList(smallTraditionalBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalBoot = smallTraditionalBoot + 1;
		gl.glNewList(mediumTraditionalBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalBoot = mediumTraditionalBoot + 1;
		gl.glNewList(largeTraditionalBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicBoot = largeTraditionalBoot + 1;
		gl.glNewList(smallMagicBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicBoot = smallMagicBoot + 1;
		gl.glNewList(mediumMagicBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicBoot = mediumMagicBoot + 1;
		gl.glNewList(largeMagicBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questBoot = largeMagicBoot + 1;
		gl.glNewList(questBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareBoot = questBoot + 1;
		gl.glNewList(rareBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleBoot = rareBoot + 1;
		gl.glNewList(singleBoot, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalShield = gl.glGenLists(NBTEXTURES);
		gl.glNewList(smallTraditionalShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalShield = smallTraditionalShield + 1;
		gl.glNewList(mediumTraditionalShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalShield = mediumTraditionalShield + 1;
		gl.glNewList(largeTraditionalShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicShield = largeTraditionalShield + 1;
		gl.glNewList(smallMagicShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicShield = smallMagicShield + 1;
		gl.glNewList(mediumMagicShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicShield = mediumMagicShield + 1;
		gl.glNewList(largeMagicShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questShield = largeMagicShield + 1;
		gl.glNewList(questShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareShield = questShield + 1;
		gl.glNewList(rareShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 130
		gl.glEndList();

		singleShield = rareShield + 1;
		gl.glNewList(singleShield, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalSword = singleShield + 1;
		gl.glNewList(smallTraditionalSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalSword = smallTraditionalSword + 1;
		gl.glNewList(mediumTraditionalSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalSword = mediumTraditionalSword + 1;
		gl.glNewList(largeTraditionalSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicSword = largeTraditionalSword + 1;
		gl.glNewList(smallMagicSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 135
		gl.glEndList();

		mediumMagicSword = smallMagicSword + 1;
		gl.glNewList(mediumMagicSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicSword = mediumMagicSword + 1;
		gl.glNewList(largeMagicSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questSword = largeMagicSword + 1;
		gl.glNewList(questSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareSword = questSword + 1;
		gl.glNewList(rareSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 140
		gl.glEndList();

		singleSword = rareSword + 1;
		gl.glNewList(singleSword, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalAxe = singleSword + 1;
		gl.glNewList(smallTraditionalAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalAxe = smallTraditionalAxe + 1;
		gl.glNewList(mediumTraditionalAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalAxe = mediumTraditionalAxe + 1;
		gl.glNewList(largeTraditionalAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicAxe = largeTraditionalAxe + 1;
		gl.glNewList(smallMagicAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 145
		gl.glEndList();

		mediumMagicAxe = smallMagicAxe + 1;
		gl.glNewList(mediumMagicAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicAxe = mediumMagicAxe + 1;
		gl.glNewList(largeMagicAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questAxe = largeMagicAxe + 1;
		gl.glNewList(questAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareAxe = questAxe + 1;
		gl.glNewList(rareAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleAxe = rareAxe + 1;
		gl.glNewList(singleAxe, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 150
		gl.glEndList();

		smallTraditionalStick = singleAxe + 1;
		gl.glNewList(smallTraditionalStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumTraditionalStick = smallTraditionalStick + 1;
		gl.glNewList(mediumTraditionalStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalStick = mediumTraditionalStick + 1;
		gl.glNewList(largeTraditionalStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicStick = largeTraditionalStick + 1;
		gl.glNewList(smallMagicStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicStick = smallMagicStick + 1;
		gl.glNewList(mediumMagicStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 155
		gl.glEndList();

		largeMagicStick = mediumMagicStick + 1;
		gl.glNewList(largeMagicStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		questStick = largeMagicStick + 1;
		gl.glNewList(questStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareStick = questStick + 1;
		gl.glNewList(rareStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleStick = rareStick + 1;
		gl.glNewList(singleStick, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallTraditionalArc = singleStick + 1;
		gl.glNewList(smallTraditionalArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 160
		gl.glEndList();

		mediumTraditionalArc = smallTraditionalArc + 1;
		gl.glNewList(mediumTraditionalArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeTraditionalArc = mediumTraditionalArc + 1;
		gl.glNewList(largeTraditionalArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		smallMagicArc = largeTraditionalArc + 1;
		gl.glNewList(smallMagicArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		mediumMagicArc = smallMagicArc + 1;
		gl.glNewList(mediumMagicArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		largeMagicArc = mediumMagicArc + 1;
		gl.glNewList(largeMagicArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++); // 165
		gl.glEndList();

		questArc = largeMagicArc + 1;
		gl.glNewList(questArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		rareArc = questArc + 1;
		gl.glNewList(rareArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();

		singleArc = rareArc + 1;
		gl.glNewList(singleArc, GL.GL_COMPILE);
		draw(gLDrawable, 32, 32, cpt++);
		gl.glEndList();
		 */
		Logger.printINFO("...Build finished");

	}

	/**
	 * draw fonction	 * 
	 * @param gLDrawable
	 *            the GLDrawable object
	 * @param w
	 *            the width of the texture
	 * @param h
	 *            the height of th texture
	 * @param t
	 *            the type of the texture Draw a square with a texture (type)
	 */

	public void draw(GLAutoDrawable gLDrawable, int w, int h, int t) {

		GL gl = gLDrawable.getGL();

		gl.glPushMatrix();
		// Set the image in the right postion
		gl.glTranslated(0, h, 0);
		gl.glScaled(1, -1d, 0);

		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[t]);

		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0, 0);
		gl.glVertex2i(0, 0);
		gl.glTexCoord2i(1, 0);
		gl.glVertex2i(w, 0);
		gl.glTexCoord2i(1, 1);
		gl.glVertex2i(w, h);
		gl.glTexCoord2i(0, 1);
		gl.glVertex2i(0, h);
		gl.glEnd();

		gl.glPopMatrix();

		gl.glLoadIdentity();

	}

	/**
	 * draw fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable object
	 * @param x
	 *            X-coordinate
	 * @param y
	 *            Y-coordinate
	 * @param w
	 *            the width of the texture
	 * @param h
	 *            the height of th texture
	 * @param t
	 *            the type of the texture Draw a square with a texture (type)
	 */

	public void draw(GLAutoDrawable gLDrawable, int x, int y, int w, int h,	int t) {

		GL gl = gLDrawable.getGL();

		gl.glLoadIdentity();

		gl.glPushMatrix();
		// Set the image in the right postion
		gl.glTranslated(x, y, 0);
		gl.glTranslated(0, h, 0);
		gl.glScaled(1, -1d, 0);

		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[t]);

		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0, 0);
		gl.glVertex2i(0, 0);
		gl.glTexCoord2i(1, 0);
		gl.glVertex2i(w, 0);
		gl.glTexCoord2i(1, 1);
		gl.glVertex2i(w, h);
		gl.glTexCoord2i(0, 1);
		gl.glVertex2i(0, h);
		gl.glEnd();

		gl.glPopMatrix();
	}

	/**
	 * loadGLTexture fonction
	 * 
	 * @param gLDrawable
	 *            the GLDrawable object
	 */

	public void loadGLTexture(GLAutoDrawable gLDrawable) {

		GL gl = gLDrawable.getGL();

		gl.glGenTextures(72/* NBTEXTURES */, texture, 0);

		Logger.printINFO("Loading textures...");
		Texture textureLoaded = null;
		int cpt = 0;

		/* --- items textures --- */
		TextureFactory textureLoader = new TextureFactory();
		texturePathTab = new String[72/* NBTEXTURES */];
		texturePathTab[cpt++] = "data/images/gui/actionBar/actionBar.png";
		texturePathTab[cpt++] = "data/images/gui/chat/chat.png";
		texturePathTab[cpt++] = "data/images/gui/equipement/equipement.png";
		texturePathTab[cpt++] = "data/images/gui/inventory/inventory.png";
		texturePathTab[cpt++] = "data/images/gui/smallBar/smallBar.png";
		texturePathTab[cpt++] = "data/images/gui/quest/quest.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/previous01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/previous02.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/next01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/next02.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/zone01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/zone02.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/world01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/world02.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt1_01.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt1_02.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt2_01.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt2_02.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt3_01.png";
		texturePathTab[cpt++] = "data/images/gui/others/bt3_02.png";
		texturePathTab[cpt++] = "data/images/gui/chat/emotes01.png";
		texturePathTab[cpt++] = "data/images/gui/chat/emotes02.png";
		texturePathTab[cpt++] = "data/images/gui/chat/channel_on_01.png";
		texturePathTab[cpt++] = "data/images/gui/chat/channel_on_02.png";
		texturePathTab[cpt++] = "data/images/gui/chat/channel_on_03.png";
		texturePathTab[cpt++] = "data/images/gui/chat/channel_on_04.png";
		texturePathTab[cpt++] = "data/images/gui/chat/channel_off.png";
		texturePathTab[cpt++] = "data/images/gui/equipement/set01_01.png";
		texturePathTab[cpt++] = "data/images/gui/equipement/set01_02.png";
		texturePathTab[cpt++] = "data/images/gui/equipement/set02_01.png";
		texturePathTab[cpt++] = "data/images/gui/equipement/set02_02.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/quest_01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/quest_02.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/option_01.png";
		texturePathTab[cpt++] = "data/images/gui/actionBar/option_02.png";
		texturePathTab[cpt++] = "data/images/gui/others/cursor.png";
		texturePathTab[cpt++] = "data/images/gui/icons/bags/bag_01.png";
		texturePathTab[cpt++] = "data/images/gui/icons/bags/bag_02.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/aggravees.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/bdf.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/colonne.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/concentration.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/connaissance.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/deflagration.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/expansion.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/explosion.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/implosion.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/insensibilite.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/meteore.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/mur.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/nuee.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/persistantes.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/pyromanie.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_fire/vague.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/armureFroid.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/armureGel.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/armureGlace.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/blizzard.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/cone.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/coneAmeliore.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/fils.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/gel.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/nova.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/pere.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/pic.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/picAmeliore.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/serment.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/trait.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/traitAmeliore.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/vagueFroid.png";
		texturePathTab[cpt++] = "data/images/gui/icons/spell_ice/vagueFroidAmeliore.png";
		texturePathTab[cpt++] = "data/images/gui/icons/weapons/longue.png";
		cpt = 0;
		for (String texturePath : texturePathTab) {
			try {
				textureLoaded = textureLoader.loadTexture(texturePath);
				textureLoaded.doExpandTexture(gl);
				texture[cpt] = textureLoaded.internal_index[0];
				gl.glBindTexture(GL.GL_TEXTURE_2D, texture[cpt++]);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		/*
		 * textureLoader.load_texture("data/images/gui/0205.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[19]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0206.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[20]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0207.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[21]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0208.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[22]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0209.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[23]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0301.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[24]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0302.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[25]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0303.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[26]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0304.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[27]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0305.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[28]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0306.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[29]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0307.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[30]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0308.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[31]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0309.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[32]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0401.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[33]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0402.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[34]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0403.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[35]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0404.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[36]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0405.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[37]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0406.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[38]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0407.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[39]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0408.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[40]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0409.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[41]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0501.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[42]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0502.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[43]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0503.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[44]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0504.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[45]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0505.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[46]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0506.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[47]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0507.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[48]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0508.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[49]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0509.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[50]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0601.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[51]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0602.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[52]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0603.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[53]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0604.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[54]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0605.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[55]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0606.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[56]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0607.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[57]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0608.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[58]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0609.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[59]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0701.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[60]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0702.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[61]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0703.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[62]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0704.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[63]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0705.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[64]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0706.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[65]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0707.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[66]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0708.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[67]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0709.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[68]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0801.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[69]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0802.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[70]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0803.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[71]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0804.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[72]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0805.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[73]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0806.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[74]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0807.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[75]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0808.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[76]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0809.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[77]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0901.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[78]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0902.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[79]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0903.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[80]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0904.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[81]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0905.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[82]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0906.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[83]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0907.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[84]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0908.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[85]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/0909.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[86]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1001.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[87]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1002.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[88]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1003.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[89]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1004.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[90]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1005.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[91]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1006.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[92]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1007.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[93]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1008.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[94]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1009.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[95]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1101.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[96]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1102.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[97]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1103.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[98]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1104.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[99]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1105.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[100]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1106.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[101]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1107.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[102]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1108.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[103]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1109.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[104]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1201.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[105]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1202.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[106]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1203.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[107]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1204.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[108]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1205.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[109]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1206.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[110]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1207.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[111]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1208.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[112]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1209.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[113]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1301.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[114]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1302.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[115]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1303.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[116]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1304.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[117]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1305.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[118]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1306.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[119]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1307.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[120]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1308.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[121]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1309.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[122]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1401.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[123]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1402.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[124]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1403.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[125]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1404.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[126]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1405.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[127]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1406.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[128]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1407.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[129]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1408.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[130]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1409.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[131]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1501.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[132]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1502.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[133]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1503.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[134]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1504.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[135]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1504.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[136]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1505.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[137]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1506.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[138]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1507.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[139]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1508.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[140]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1509.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[141]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1601.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[142]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1602.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[143]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1603.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[144]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1604.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[145]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1605.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[146]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1606.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[147]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1607.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[148]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1608.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[149]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1609.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[150]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1701.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[151]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1702.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[152]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1703.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[153]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1704.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[154]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1705.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[155]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1706.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[156]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1707.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[157]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1708.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[158]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1709.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[159]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1801.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[160]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1802.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[161]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1803.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[162]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1804.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[163]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1805.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[164]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1806.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[165]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1807.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[166]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1808.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[167]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 * 
		 * textureLoader.load_texture("data/images/gui/1809.png");
		 * gl.glBindTexture(GL.GL_TEXTURE_2D, texture[168]);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		 * gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		 * gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
		 * textureLoader.getTWidth(), textureLoader.getTHeight(),
		 * 0,GL.GL_RGBA,GL.GL_UNSIGNED_BYTE, textureLoader.getTexture());
		 */

		Logger.printINFO("... loading finished !");

	}

	public boolean invokeKeyEvent(String action) {

		boolean res=false;
		
		if (actionBar.getChat().isActivated()) {
			if (action.equals("enter")) {
				actionBar.getChat().addMessage(chatMsg,actionBar.getChat().getActiveChannel());	
				chatMsg="";
				chatLongMsg="";
				chatMsgIsLong=false;
				chatMsgSent=true;
				chatMsgNbCharAdvance=0;
			}else if (action.equals("char_a")) chatMsg+="a";
			else if (action.equals("char_b")) chatMsg+="b";
			else if (action.equals("char_c")) chatMsg+="c";
			else if (action.equals("char_d")) chatMsg+="d";
			else if (action.equals("char_e")) chatMsg+="e";
			else if (action.equals("char_f")) chatMsg+="f";
			else if (action.equals("char_g")) chatMsg+="g";
			else if (action.equals("char_h")) chatMsg+="h";
			else if (action.equals("char_i")) chatMsg+="i";
			else if (action.equals("char_j")) chatMsg+="j";
			else if (action.equals("char_k")) chatMsg+="k";
			else if (action.equals("char_l")) chatMsg+="l";
			else if (action.equals("char_m")) chatMsg+="m";
			else if (action.equals("char_n")) chatMsg+="n";
			else if (action.equals("char_o")) chatMsg+="o";
			else if (action.equals("char_p")) chatMsg+="p";
			else if (action.equals("char_q")) chatMsg+="q";
			else if (action.equals("char_r")) chatMsg+="r";
			else if (action.equals("char_s")) chatMsg+="s";
			else if (action.equals("char_t")) chatMsg+="t";
			else if (action.equals("char_u")) chatMsg+="u";
			else if (action.equals("char_v")) chatMsg+="v";
			else if (action.equals("char_w")) chatMsg+="w";
			else if (action.equals("char_x")) chatMsg+="x";
			else if (action.equals("char_y")) chatMsg+="y";
			else if (action.equals("char_z")) chatMsg+="z";
			else if (action.equals("char_A")) chatMsg+="A";
			else if (action.equals("char_B")) chatMsg+="B";
			else if (action.equals("char_C")) chatMsg+="C";
			else if (action.equals("char_D")) chatMsg+="D";
			else if (action.equals("char_E")) chatMsg+="E";
			else if (action.equals("char_F")) chatMsg+="F";
			else if (action.equals("char_G")) chatMsg+="G";
			else if (action.equals("char_H")) chatMsg+="H";
			else if (action.equals("char_I")) chatMsg+="I";
			else if (action.equals("char_J")) chatMsg+="J";
			else if (action.equals("char_K")) chatMsg+="K";
			else if (action.equals("char_L")) chatMsg+="L";
			else if (action.equals("char_M")) chatMsg+="M";
			else if (action.equals("char_N")) chatMsg+="N";
			else if (action.equals("char_O")) chatMsg+="O";
			else if (action.equals("char_P")) chatMsg+="P";
			else if (action.equals("char_Q")) chatMsg+="Q";
			else if (action.equals("char_R")) chatMsg+="R";
			else if (action.equals("char_S")) chatMsg+="S";
			else if (action.equals("char_T")) chatMsg+="T";
			else if (action.equals("char_U")) chatMsg+="U";
			else if (action.equals("char_V")) chatMsg+="V";
			else if (action.equals("char_W")) chatMsg+="W";
			else if (action.equals("char_X")) chatMsg+="X";
			else if (action.equals("char_Y")) chatMsg+="Y";
			else if (action.equals("char_Z")) chatMsg+="Z";
			else if (action.equals("return")) chatMsg=chatMsg.substring(0, chatMsg.length()-1);
		}else if(action.equals("chat_open")) {
			if (!actionBar.getChat().isVisible())actionBar.openChat(actionBar.getChat().getActiveChannel());
			else actionBar.closeChat();
		}else if(action.equals("equipement_open")) {
			if (!actionBar.getEquipement().isVisible())actionBar.openEquipement();
			else actionBar.closeEquipement();
		}else if(action.equals("inventory_open")) {
			if (!actionBar.getInventory().isVisible())actionBar.openInventory();
			else actionBar.closeInventory();
		}else if(action.equals("loading_activated")) {
			System.out.println("loadind activated !");
			if (isLoadingActived()) setLoadingActived(false);
			else setLoadingActived(true);
		}else if(action.equals("gui_debug")) {
			if (isDebugMode()) setDebugMode(false);
			else setDebugMode(true);
		}else if(action.equals("gui_pick")) {
			actionBar.getInventory().pickGui(new GuiRep(1806,18));
		}else if(action.equals("exit")) {
			quit();
		}
		return res;
	}

	public boolean invokeMouseEvent(String action, int xPos, int yPos) {

		Point p = new Point(xPos,yPos);
		Chat chat = actionBar.getChat();
		Equipement equipement = actionBar.getEquipement();
		Inventory inventory = actionBar.getInventory();
		
		if (action.equals("mouse_move")) {

			setCursorPos(new Point(xPos,yPos));

		}else if (action.equals("mouse_click")) {
			
			actionBar.getChat().setActivated(false);

			if (actionBar.getActionBarSite().contains(p)) {

				if (actionBar.getAction1Site().contains(p))  {
					getActionBar().doAction(0);
				}else if (actionBar.getAction2Site().contains(p))  {
					getActionBar().doAction(1);
				}else if (actionBar.getAction3Site().contains(p))  {
					getActionBar().doAction(2);
				}else if (actionBar.getAction4Site().contains(p))  {
					getActionBar().doAction(3);
				}else if (actionBar.getAction5Site().contains(p))  {
					getActionBar().doAction(4);
				}else if (actionBar.getAction6Site().contains(p))  {
					getActionBar().doAction(5);
				}else if (actionBar.getAction7Site().contains(p))  {
					getActionBar().doAction(6);
				}else if (actionBar.getAction8Site().contains(p))  {
					getActionBar().doAction(7);
				}else if (actionBar.getAction9Site().contains(p))  {
					getActionBar().doAction(8);
				}else if (actionBar.getAction10Site().contains(p))  {
					getActionBar().doAction(9);
				}else if (actionBar.getAction11Site().contains(p))  {
					getActionBar().doAction(10);
				}else if (actionBar.getAction12Site().contains(p))  {
					getActionBar().doAction(11);
				}else if (actionBar.getAction13Site().contains(p))  {
					getActionBar().doAction(12);
				}else if (actionBar.getAction14Site().contains(p))  {
					getActionBar().doAction(13);
				}else if (actionBar.getAction15Site().contains(p))  {
					getActionBar().doAction(14);
				}else if (actionBar.getAction16Site().contains(p))  {
					getActionBar().doAction(15);
				}else if (actionBar.getAction17Site().contains(p))  {
					getActionBar().doAction(16);
				}else if (actionBar.getAction18Site().contains(p))  {
					getActionBar().doAction(17);
				}else if (actionBar.getAction19Site().contains(p))  {
					getActionBar().doAction(18);
				}else if (actionBar.getAction20Site().contains(p))  {
					getActionBar().doAction(19);
				}else if (actionBar.getQuestSite().contains(p))  {
					getActionBar().setClickOnQuest(true);
				}else if (actionBar.getOptionSite().contains(p))  {
					getActionBar().setClickOnOption(true);
				}else if (actionBar.getPreviousSite().contains(p))  {
					getActionBar().setClickOnPrevious(true);
				}else if (actionBar.getNextSite().contains(p))  {
					getActionBar().setClickOnNext(true);
				}else if (actionBar.getZoneSite().contains(p))  {
					getActionBar().setClickOnZone(true);
				}
			}else if (actionBar.getWorldSite().distance(p)<=actionBar.getWorldSite().getRadius())  {
				getActionBar().setClickOnWorld(true);
			}else if (actionBar.getActionMapSite().distance(p)<=actionBar.getActionMapSite().getRadius()) {

			}else if(inventory.isVisible() && inventory.getInventoryOpenSite().contains(p)) { // If the click is in the inventory
				
				if ((!chat.getChatOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.INVENTORY_FOCUS)==0)) {					
					
				if (inventory.getCloseOpenSite().contains(p)) {		
					getActionBar().closeInventory();
				}else if (inventory.getBag01OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG01);
				}else if (inventory.getBag02OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG02);
				}else if (inventory.getBag03OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG03);
				}else if (inventory.getBag04OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG04);
				}else if (inventory.getBag05OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG05);
				}else if (inventory.getBag06OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG06);
				}else if (inventory.getBag07OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG07);
				}else if (inventory.getBag08OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG08);
				}else if (inventory.getBag09OpenSite().contains(p)) {
					getActionBar().getInventory().changeBag(Inventory.INVENTORY_BAG09);
				}else if (inventory.getLockOpenSite().contains(p)) {
					getActionBar().getInventory().setDefaultPosition();
				}else if (inventory.getCloseOpenSite().contains(p)) {
					getActionBar().closeInventory();
				}
				
				actionBar.setPriority(Focus.INVENTORY_FOCUS);
				
			}
			}else if(!inventory.isVisible() && inventory.getInventoryCloseSite().contains(p)) {

				if (inventory.getUpCloseSite().contains(p)) {	
					getActionBar().openInventory();
				}

			}else if(equipement.isVisible() && equipement.getEquipementOpenSite().contains(p)) { // If the click is in the equipement
				
				if ((!chat.getChatOpenSite().contains(p) &&	!inventory.getInventoryOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || inventory.getInventoryOpenSite().contains(p)) && actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==0)) {					
			
					if (equipement.getCloseOpenSite().contains(p)) {		
					getActionBar().closeEquipement();
				}else if (equipement.getSet1OpenSite().contains(p)) {
					getActionBar().getEquipement().changeSet(Equipement.SET1);
				}else if (equipement.getSet2OpenSite().contains(p)) {
					getActionBar().getEquipement().changeSet(Equipement.SET2);
				}else if (equipement.getLockOpenSite().contains(p)) {
					getActionBar().getEquipement().setDefaultPosition();
				}else if (equipement.getCloseOpenSite().contains(p)) {
					getActionBar().closeEquipement();
				}
					actionBar.setPriority(Focus.EQUIPEMENT_FOCUS);
				}
				

				

			}else if(!equipement.isVisible() && equipement.getEquipementCloseSite().contains(p)) {

				if (equipement.getUpCloseSite().contains(p)) {	
					getActionBar().openEquipement();
				}

			}else if (chat.isVisible() && chat.getChatOpenSite().contains(p)) { // If the click is in the chat
				
				if ((!equipement.getEquipementOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(equipement.getEquipementOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.CHAT_FOCUS)==0)) {		
			
					if (chat.getGeneralOpenChannelSite().contains(p)) {
					getActionBar().getChat().changeChannel(Chat.GENERAL_CHANNEL);
				}else if (chat.getGuildOpenChannelSite().contains(p)) {
					getActionBar().getChat().changeChannel(Chat.GUILD_CHANNEL);
				}else if (chat.getGroupOpenChannelSite().contains(p)) {
					getActionBar().getChat().changeChannel(Chat.GROUP_CHANNEL);
				}else if (chat.getPrivateOpenChannelSite().contains(p)) {
					getActionBar().getChat().changeChannel(Chat.PRIVATE_CHANNEL);
				}else if (chat.getEmotesOpenSite().contains(p)) {
					// EMOTES
				}else if (chat.getEditableOpenSite().contains(p)) {
					// ?
				}else if (chat.getLockOpenSite().contains(p)) {
					getActionBar().getChat().setDefaultPosition();
				}else if (chat.getCloseOpenSite().contains(p) ) {
					getActionBar().closeChat();
				}
				actionBar.setPriority(Focus.CHAT_FOCUS);
				actionBar.getChat().setActivated(true);
				}

			}else if (!chat.isVisible() && chat.getChatCloseSite().contains(p)) {

				if (chat.getGeneralCloseChannelSite().contains(p)) {
					getActionBar().getChat().openChannel(Chat.GENERAL_CHANNEL);
				}else if (chat.getGuildCloseChannelSite().contains(p)) {
					getActionBar().getChat().openChannel(Chat.GUILD_CHANNEL);
				}else if (chat.getGroupCloseChannelSite().contains(p)) {
					getActionBar().getChat().openChannel(Chat.GROUP_CHANNEL);
				}else if (chat.getPrivateCloseChannelSite().contains(p)) {
					getActionBar().getChat().openChannel(Chat.PRIVATE_CHANNEL);
				}else if (chat.getUpCloseSite().contains(p)) {
					getActionBar().openChat(Chat.GENERAL_CHANNEL);
				}

			}

		}else if (action.equals("mouse_dragg")) {


			int d = ActionBar.MagnetismPixelLenght;

			setCursorPos(p);

			if (actionBar.getActionBarSite().contains(p)) {

				if (actionBar.getAction1Site().contains(p))  {
					setActionSelected(0);
				}else if (actionBar.getAction2Site().contains(p))  {
					setActionSelected(1);
				}else if (actionBar.getAction3Site().contains(p))  {
					setActionSelected(2);
				}else if (actionBar.getAction4Site().contains(p))  {
					setActionSelected(3);
				}else if (actionBar.getAction5Site().contains(p))  {
					setActionSelected(4);
				}else if (actionBar.getAction6Site().contains(p))  {
					setActionSelected(5);
				}else if (actionBar.getAction7Site().contains(p))  {
					setActionSelected(6);
				}else if (actionBar.getAction8Site().contains(p))  {
					setActionSelected(7);
				}else if (actionBar.getAction9Site().contains(p))  {
					setActionSelected(8);
				}else if (actionBar.getAction10Site().contains(p))  {
					setActionSelected(9);
				}else if (actionBar.getAction11Site().contains(p))  {
					setActionSelected(10);
				}else if (actionBar.getAction12Site().contains(p))  {
					setActionSelected(11);
				}else if (actionBar.getAction13Site().contains(p))  {
					setActionSelected(12);
				}else if (actionBar.getAction14Site().contains(p))  {
				setActionSelected(13);
				}else if (actionBar.getAction15Site().contains(p))  {
					setActionSelected(14);
				}else if (actionBar.getAction16Site().contains(p))  {
					setActionSelected(15);
				}else if (actionBar.getAction17Site().contains(p))  {
					setActionSelected(16);
				}else if (actionBar.getAction18Site().contains(p))  {
					setActionSelected(17);
				}else if (actionBar.getAction19Site().contains(p))  {
					setActionSelected(18);
				}else if (actionBar.getAction20Site().contains(p))  {
					setActionSelected(19);
				}

			}else if ((inventory.isVisible() && inventory.getInventoryDragBarSite().contains(p))) { 			
				if ((!chat.getChatOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.INVENTORY_FOCUS)==0)) {					
					if (!getActionBar().getInventory().isDragged() && !getActionBar().getChat().isDragged() && !getActionBar().getEquipement().isDragged() &&!this.itemSelected) {
						actionBar.setPriority(Focus.INVENTORY_FOCUS);
						getActionBar().getInventory().dragIt(p);
					}
				}
			}else if ((equipement.isVisible() && equipement.getEquipementDragBarSite().contains(p))) { 			
				if ((!chat.getChatOpenSite().contains(p) &&	!inventory.getInventoryOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || inventory.getInventoryOpenSite().contains(p)) && actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==0)) {					
					if (!getActionBar().getEquipement().isDragged() && !getActionBar().getChat().isDragged() && !getActionBar().getInventory().isDragged() &&!this.itemSelected) {
						actionBar.setPriority(Focus.EQUIPEMENT_FOCUS);
						getActionBar().getEquipement().dragIt(p);
					}
				}
			}else if ((chat.isVisible() && chat.getChatDragBarSite().contains(p))) { 			
				if ((!equipement.getEquipementOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(equipement.getEquipementOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.CHAT_FOCUS)==0)) {					
					if (!getActionBar().getChat().isDragged() && !getActionBar().getEquipement().isDragged() && !getActionBar().getInventory().isDragged() &&!this.itemSelected) {
						actionBar.setPriority(Focus.CHAT_FOCUS);
						getActionBar().getChat().dragIt(p);
					}
				}
			}

			// Magnetism effect


			if (getActionBar().getChat().isDragged()) {

				int x= (int)(p.getX()-actionBar.getChat().getDragPoint().getX()+actionBar.getChat().getPosition().getX());
				int y= (int)(p.getY()-actionBar.getChat().getDragPoint().getY()+actionBar.getChat().getPosition().getY()-Chat.CHAT_HEIGHT);

				//Left
				if ((x>=0 && x<=d) && (y>=d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
					// Top
				}else if ((y>=0 && y<=d) && (x>=d && x+Chat.CHAT_WIDTH <=getScreenWidth()-d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
					// Right
				}else if ((x+Chat.CHAT_WIDTH<=getScreenWidth() && x+Chat.CHAT_WIDTH>=getScreenWidth()-d) && (y>=d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()));	
					// Down
				}else if ((y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x>=d && x+Chat.CHAT_WIDTH<=getScreenWidth()-d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
				}
				// Top left corner
				if ((x>=0 && x<=d) && (y>=0 && y<=d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
				}
				// Top right corner
				if ((x+Chat.CHAT_WIDTH<=getScreenWidth() && x+Chat.CHAT_WIDTH>=getScreenWidth()-d) && (y>=0 && y<=d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));	

				}
				// Down right corner
				if ((y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x+Chat.CHAT_WIDTH<=getScreenWidth() && x+Chat.CHAT_WIDTH>=getScreenWidth()-d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()+(getScreenHeight()-y)));
				}
				// Down left corner
				if ((x>=0 && x<=d) && (y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Chat.CHAT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d)){
					getActionBar().getChat().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Chat.CHAT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(getScreenHeight()-y)));	
				}
			}else if (getActionBar().getEquipement().isDragged()) {

				int x= (int)(p.getX()-actionBar.getEquipement().getDragPoint().getX()+actionBar.getEquipement().getPosition().getX());
				int y= (int)(p.getY()-actionBar.getEquipement().getDragPoint().getY()+(actionBar.getEquipement().getPosition().getY()-Equipement.EQUIPEMENT_HEIGHT));

				//Left
				if ((x>=0 && x<=d) && (y>=d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
					// Top
				}else if ((y>=0 && y<=d) && (x>=d && x+Equipement.EQUIPEMENT_WIDTH<=getScreenWidth()-d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
					// Right
				}else if ((x+Equipement.EQUIPEMENT_WIDTH<=getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=getScreenWidth()-d) && (y>=d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()));	
					// Down
				}else if ((y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x>=d && x+Equipement.EQUIPEMENT_WIDTH<=getScreenWidth()-d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
				}
				//	Top left corner

				if ((x>=0 && x<=d) && (y>=0 && y<=d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
				}
				// Top right corner
				if ((x+Equipement.EQUIPEMENT_WIDTH<=getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=getScreenWidth()-d) && (y>=0 && y<=d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));	

				}
				// Down right corner
				if ((y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x+Equipement.EQUIPEMENT_WIDTH<=getScreenWidth() && x+Equipement.EQUIPEMENT_WIDTH>=getScreenWidth()-d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()+(getScreenHeight()-y)));
				}
				// Down left corner
				if ((x>=0 && x<=d) && (y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Equipement.EQUIPEMENT_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d)){
					getActionBar().getEquipement().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Equipement.EQUIPEMENT_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(getScreenHeight()-y)));	
				}
			}else if (getActionBar().getInventory().isDragged()) {

				int x= (int)(p.getX()-actionBar.getInventory().getDragPoint().getX()+actionBar.getInventory().getPosition().getX());
				int y= (int)(p.getY()-actionBar.getInventory().getDragPoint().getY()+(actionBar.getInventory().getPosition().getY()-Inventory.INVENTORY_HEIGHT));

				//Left
				if ((x>=0 && x<=d) && (y>=d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()-x,(int)p.getY()));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()));
					// Top
				}else if ((y>=0 && y<=d) && (x>=d && x+Inventory.INVENTORY_WIDTH<=getScreenWidth()-d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX(),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()-y));	
					// Right
				}else if ((x+Inventory.INVENTORY_WIDTH<=getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=getScreenWidth()-d) && (y>=d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()));	
					// Down
				}else if ((y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x>=d && x+Inventory.INVENTORY_WIDTH<=getScreenWidth()-d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
					setCursorPos(new Point((int)p.getX(),(int)p.getY()+(getScreenHeight()-y)));
				}
				//	Top left corner

				if ((x>=0 && x<=d) && (y>=0 && y<=d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()-x,(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()-y));	
				}
				// Top right corner
				if ((x+Inventory.INVENTORY_WIDTH<=getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=getScreenWidth()-d) && (y>=0 && y<=d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));	

				}
				// Down right corner
				if ((y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d) && (x+Inventory.INVENTORY_WIDTH<=getScreenWidth() && x+Inventory.INVENTORY_WIDTH>=getScreenWidth()-d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()+(getScreenHeight()-y)));
				}
				// Down left corner
				if ((x>=0 && x<=d) && (y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT<=getScreenHeight() && y+Inventory.INVENTORY_HEIGHT+ActionBar.ACTIONBAR_HEIGHT>=getScreenHeight()-d)){
					getActionBar().getInventory().dragIt(new Point((int)p.getX()+(getScreenWidth()-x-Inventory.INVENTORY_WIDTH),(int)p.getY()-y));
					setCursorPos(new Point((int)p.getX()-x,(int)p.getY()+(getScreenHeight()-y)));	
				}
			}


//			TODO P-e changer le content et l'equipement en variable global

			if (!getActionBar().getChat().isDragged() && !getActionBar().getEquipement().isDragged() && !getActionBar().getInventory().isDragged()) {

				if ((!chat.getChatOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.INVENTORY_FOCUS)==0)) {					
					
				if (getActionBar().getInventory().getContent01OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),0);
				}else if (getActionBar().getInventory().getContent02OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),1);
				}else if (getActionBar().getInventory().getContent03OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),2);
				}else if (getActionBar().getInventory().getContent04OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),3);
				}else if (getActionBar().getInventory().getContent05OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),4);
				}else if (getActionBar().getInventory().getContent06OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),5);
				}else if (getActionBar().getInventory().getContent07OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),6);
				}else if (getActionBar().getInventory().getContent08OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),7);
				}else if (getActionBar().getInventory().getContent09OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),8);
				}else if (getActionBar().getInventory().getContent10OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),9);
				}else if (getActionBar().getInventory().getContent11OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),10);
				}else if (getActionBar().getInventory().getContent12OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),11);
				}else if (getActionBar().getInventory().getContent13OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),12);
				}else if (getActionBar().getInventory().getContent14OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),13);
				}else if (getActionBar().getInventory().getContent15OpenSite().contains(p)) {
					setItemSelectedInInventory(getActionBar().getInventory().getActiveBag(),14);
				
				}
				}
					if ((!chat.getChatOpenSite().contains(p) &&	!inventory.getInventoryOpenSite().contains(p)
							||(chat.getChatOpenSite().contains(p) || inventory.getInventoryOpenSite().contains(p)) && actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==0)) {					
				
				if (getActionBar().getEquipement().getSet01OpenSite().contains(p)) {
					setItemSelectedInEquipement(0);
				}else if (getActionBar().getEquipement().getSet02OpenSite().contains(p)) {
					setItemSelectedInEquipement(1);
				}else if (getActionBar().getEquipement().getSet03OpenSite().contains(p)) {
					setItemSelectedInEquipement(2);
				}else if (getActionBar().getEquipement().getSet04OpenSite().contains(p)) {
					setItemSelectedInEquipement(3);
				}else if (getActionBar().getEquipement().getSet05OpenSite().contains(p)) {
					setItemSelectedInEquipement(4);
				}else if (getActionBar().getEquipement().getSet06OpenSite().contains(p)) {
					setItemSelectedInEquipement(5);
				}else if (getActionBar().getEquipement().getSet07OpenSite().contains(p)) {
					setItemSelectedInEquipement(6);
				}else if (getActionBar().getEquipement().getSet08OpenSite().contains(p)) {
					setItemSelectedInEquipement(7);
				}else if (getActionBar().getEquipement().getSet09OpenSite().contains(p)) {
					setItemSelectedInEquipement(8);
				}else if (getActionBar().getEquipement().getSet10OpenSite().contains(p)) {
					setItemSelectedInEquipement(9);
				}else if (getActionBar().getEquipement().getSet11OpenSite().contains(p)) {
					setItemSelectedInEquipement(10);
				}else if (getActionBar().getEquipement().getSet12OpenSite().contains(p)) {
					setItemSelectedInEquipement(11);
				}else if (getActionBar().getEquipement().getSet13OpenSite().contains(p)) {
					setItemSelectedInEquipement(12);
				}else if (getActionBar().getEquipement().getSet14OpenSite().contains(p)) {
					setItemSelectedInEquipement(13);
				}else if (getActionBar().getEquipement().getSet15OpenSite().contains(p)) {
					setItemSelectedInEquipement(14);
				}else if (getActionBar().getEquipement().getSet16OpenSite().contains(p)) {
					setItemSelectedInEquipement(15);
				}else if (getActionBar().getEquipement().getSet17OpenSite().contains(p)) {
					setItemSelectedInEquipement(16);
				}else if (getActionBar().getEquipement().getSet18OpenSite().contains(p)) {
					setItemSelectedInEquipement(17);
				}else if (getActionBar().getEquipement().getSet19OpenSite().contains(p)) {
					setItemSelectedInEquipement(18);
				}else if (getActionBar().getEquipement().getSet20OpenSite().contains(p)) {
					setItemSelectedInEquipement(19);
				}
				}
			}
		}else if (action.equals("mouse_release")) {

			if (getActionBar().getChat().isDragged()) {
				getActionBar().getChat().dropIt(getCursorPos());
			}else if (getActionBar().getEquipement().isDragged()) {
				getActionBar().getEquipement().dropIt(getCursorPos());
			}else if (getActionBar().getInventory().isDragged()) {
				getActionBar().getInventory().dropIt(getCursorPos());
			}

			if (actionBar.getActionBarSite().contains(p)) {

				if (actionBar.getAction1Site().contains(p))  {
					setActionNotSelected(0);
				}else if (actionBar.getAction2Site().contains(p))  {
					setActionNotSelected(1);
				}else if (actionBar.getAction3Site().contains(p))  {
					setActionNotSelected(2);
				}else if (actionBar.getAction4Site().contains(p))  {
					setActionNotSelected(3);
				}else if (actionBar.getAction5Site().contains(p))  {
					setActionNotSelected(4);
				}else if (actionBar.getAction6Site().contains(p))  {
					setActionNotSelected(5);
				}else if (actionBar.getAction7Site().contains(p))  {
					setActionNotSelected(6);
				}else if (actionBar.getAction8Site().contains(p))  {
					setActionNotSelected(7);
				}else if (actionBar.getAction9Site().contains(p))  {
					setActionNotSelected(8);
				}else if (actionBar.getAction10Site().contains(p))  {
					setActionNotSelected(9);
				}else if (actionBar.getAction11Site().contains(p))  {
					setActionNotSelected(10);
				}else if (actionBar.getAction12Site().contains(p))  {
					setActionNotSelected(11);
				}else if (actionBar.getAction13Site().contains(p))  {
					setActionNotSelected(12);
				}else if (actionBar.getAction14Site().contains(p))  {;
				setActionNotSelected(13);
				}else if (actionBar.getAction15Site().contains(p))  {
					setActionNotSelected(14);
				}else if (actionBar.getAction16Site().contains(p))  {
					setActionNotSelected(15);
				}else if (actionBar.getAction17Site().contains(p))  {
					setActionNotSelected(16);
				}else if (actionBar.getAction18Site().contains(p))  {
					setActionNotSelected(17);
				}else if (actionBar.getAction19Site().contains(p))  {
					setActionNotSelected(18);
				}else if (actionBar.getAction20Site().contains(p))  {
					setActionNotSelected(19);
				}

			}else if (actionBar.getEquipement().isVisible() && actionBar.getEquipement().getEquipementOpenSite().contains(p)) {

				if ((!chat.getChatOpenSite().contains(p) &&	!inventory.getInventoryOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || inventory.getInventoryOpenSite().contains(p)) && actionBar.getPriority(Focus.EQUIPEMENT_FOCUS)==0)) {					
					
				if (getActionBar().getEquipement().getSet01OpenSite().contains(p)) {
					setItemDropInEquipement(0);
				}else if (getActionBar().getEquipement().getSet02OpenSite().contains(p)) {
					setItemDropInEquipement(1);
				}else if (getActionBar().getEquipement().getSet03OpenSite().contains(p)) {
					setItemDropInEquipement(2);
				}else if (getActionBar().getEquipement().getSet04OpenSite().contains(p)) {
					setItemDropInEquipement(3);
				}else if (getActionBar().getEquipement().getSet05OpenSite().contains(p)) {
					setItemDropInEquipement(4);
				}else if (getActionBar().getEquipement().getSet06OpenSite().contains(p)) {
					setItemDropInEquipement(5);
				}else if (getActionBar().getEquipement().getSet07OpenSite().contains(p)) {
					setItemDropInEquipement(6);
				}else if (getActionBar().getEquipement().getSet08OpenSite().contains(p)) {
					setItemDropInEquipement(7);
				}else if (getActionBar().getEquipement().getSet09OpenSite().contains(p)) {
					setItemDropInEquipement(8);
				}else if (getActionBar().getEquipement().getSet10OpenSite().contains(p)) {
					setItemDropInEquipement(9);
				}else if (getActionBar().getEquipement().getSet11OpenSite().contains(p)) {
					setItemDropInEquipement(10);
				}else if (getActionBar().getEquipement().getSet12OpenSite().contains(p)) {
					setItemDropInEquipement(11);
				}else if (getActionBar().getEquipement().getSet13OpenSite().contains(p)) {
					setItemDropInEquipement(12);
				}else if (getActionBar().getEquipement().getSet14OpenSite().contains(p)) {
					setItemDropInEquipement(13);
				}else if (getActionBar().getEquipement().getSet15OpenSite().contains(p)) {
					setItemDropInEquipement(14);
				}else if (getActionBar().getEquipement().getSet16OpenSite().contains(p)) {
					setItemDropInEquipement(15);
				}else if (getActionBar().getEquipement().getSet17OpenSite().contains(p)) {
					setItemDropInEquipement(16);
				}else if (getActionBar().getEquipement().getSet18OpenSite().contains(p)) {
					setItemDropInEquipement(17);
				}else if (getActionBar().getEquipement().getSet19OpenSite().contains(p)) {
					setItemDropInEquipement(18);
				}else if (getActionBar().getEquipement().getSet20OpenSite().contains(p)) {
					setItemDropInEquipement(19);
				}
				}

			}else if (actionBar.getInventory().isVisible() && actionBar.getInventory().getInventoryOpenSite().contains(p)) {

				if ((!chat.getChatOpenSite().contains(p) &&	!equipement.getEquipementOpenSite().contains(p)
						||(chat.getChatOpenSite().contains(p) || equipement.getEquipementOpenSite().contains(p)) && actionBar.getPriority(Focus.INVENTORY_FOCUS)==0)) {					
				
					if (getActionBar().getInventory().getBag01OpenSite().contains(p)) {
					setItemDropInInventory(0);
				}else if (getActionBar().getInventory().getBag02OpenSite().contains(p)) {
					setItemDropInInventory(1);
				}else if (getActionBar().getInventory().getBag03OpenSite().contains(p)) {
					setItemDropInInventory(2);
				}else if (getActionBar().getInventory().getBag04OpenSite().contains(p)) {
					setItemDropInInventory(3);
				}else if (getActionBar().getInventory().getBag05OpenSite().contains(p)) {
					setItemDropInInventory(4);
				}else if (getActionBar().getInventory().getBag06OpenSite().contains(p)) {
					setItemDropInInventory(5);
				}else if (getActionBar().getInventory().getBag07OpenSite().contains(p)) {
					setItemDropInInventory(6);
				}else if (getActionBar().getInventory().getBag08OpenSite().contains(p)) {
					setItemDropInInventory(7);
				}else if (getActionBar().getInventory().getBag09OpenSite().contains(p)) {
					setItemDropInInventory(8);
				}else if (getActionBar().getInventory().getContent01OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),0);
				}else if (getActionBar().getInventory().getContent02OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),1);
				}else if (getActionBar().getInventory().getContent03OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),2);
				}else if (getActionBar().getInventory().getContent04OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),3);
				}else if (getActionBar().getInventory().getContent05OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),4);
				}else if (getActionBar().getInventory().getContent06OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),5);
				}else if (getActionBar().getInventory().getContent07OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),6);
				}else if (getActionBar().getInventory().getContent08OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),7);
				}else if (getActionBar().getInventory().getContent09OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),8);
				}else if (getActionBar().getInventory().getContent10OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),9);
				}else if (getActionBar().getInventory().getContent11OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),10);
				}else if (getActionBar().getInventory().getContent12OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),11);
				}else if (getActionBar().getInventory().getContent13OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),12);
				}else if (getActionBar().getInventory().getContent14OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),13);
				}else if (getActionBar().getInventory().getContent15OpenSite().contains(p)) {
					setItemDropInInventory(getActionBar().getInventory().getActiveBag(),14);
				}
				}
			}
		}

		return true;
	}

	public void quit() {

		try {
			xmlEngine.set("chat.position_x", (int)actionBar.getChat().getPosition().getX());
			xmlEngine.set("chat.position_y", (int)actionBar.getChat().getPosition().getY());
			xmlEngine.set("chat.open", actionBar.getChat().isVisible());

			xmlEngine.set("equipement.position_x", (int)actionBar.getEquipement().getPosition().getX());
			xmlEngine.set("equipement.position_y", (int)actionBar.getEquipement().getPosition().getY());
			xmlEngine.set("equipement.open", actionBar.getEquipement().isVisible());

			xmlEngine.set("inventory.position_x", (int)actionBar.getInventory().getPosition().getX());
			xmlEngine.set("inventory.position_y", (int)actionBar.getInventory().getPosition().getY());
			xmlEngine.set("inventory.open", actionBar.getInventory().isVisible());

		} catch (TransformerFactoryConfigurationError err) {
			System.err.println("DataBase Error (Save 1) : "+err.getMessage());
		} catch (TransformerException err) {
			System.err.println("DataBase Error (Save 2) : "+err.getMessage());
		}

		System.exit(0);
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

	public XmlEngine getXmlEngine() {
		return xmlEngine;
	}

	public Point getCursorPos() {
		return cursorPos;
	}

	public GLCanvas getCanvas() {
		return canvas;
	}

	/* --------------- SET --------------- */

	public void setCursorPos(Point point) {
		if (point.getX() >= 0 && point.getY() >= 0)
			cursorPos = point;
	}


	/* --------------- IS --------------- */

	public boolean isItemSelected() {
		return itemSelected;
	}

	public boolean isDEVELOPPEMENT() {
		return DEVELOPPEMENT;
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

	public boolean isLoadingActived() {
		return loadingActived;
	}

	public void setLoadingActived(boolean loadingActived) {
		this.loadingActived = loadingActived;
	}

	public ClientMain getMain() {
		return main;
	}

}