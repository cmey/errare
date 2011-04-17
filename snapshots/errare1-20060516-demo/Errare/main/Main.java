/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Martin DELEMOTTE

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package main;

import gameEngine.GameEngine;
import graphicsEngine.GraphicsEngine;
import guiEngine.GuiEngine;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import databaseEngine.DatabaseEngine;

import networkEngine.NetworkClient;
import networkEngine.NetworkServer;

import physicsEngine.PhysicsEngine;
import soundEngine.SoundEngine;

public class Main {
	
	private static final int PORT=1099; //port used for network communication
	
	public static final int GAMEENGINEFREQ=10;
	public static final int PHYSICSENGINEFREQ=1;
	public static final int NETWORKENGINEFREQ=1;
	public static final long PERIOD = (long)((1.0/60.0)*1E9);
	public static  final int COMBATFREQ = 10;

	private PhysicsEngine physicsEngine;
	private GraphicsEngine graphicsEngine;
	private GameEngine gameEngine;
	private NetworkClient networkClient;
	private GuiEngine guiEngine;
	private SoundEngine soundEngine;
	private DatabaseEngine databaseEngine;
	private Generator generator;
	private JFrame jf;
	private Random random;
	
	public Main(String host, int port, JFrame jf) throws ParserConfigurationException, UnknownHostException, IOException, ClassNotFoundException {
		
		this.jf=jf;
		/*jf.setUndecorated(true);
		
		GraphicsEnvironment env = GraphicsEnvironment.
		 getLocalGraphicsEnvironment();
		 GraphicsDevice[] devices = env.getScreenDevices();
		 GraphicsDevice device = devices[0];
		 
		 
		 device.setFullScreenWindow(jf);
		 jf.validate();*/
	
		
		networkClient = new NetworkClient(this, host, port);
		soundEngine = new SoundEngine();
		databaseEngine = new DatabaseEngine();
		physicsEngine = new PhysicsEngine(this);
		gameEngine = new GameEngine(this);
		//guiEngine = new GuiEngine(this);
		graphicsEngine = new GraphicsEngine(this);
		generator = new Generator(this);
		
		
		generator.generate(512);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
		jf.setTitle("Errare Version Alpha");
		jf.setBounds(0,0,dimScreen.width,dimScreen.height);
		jf.setUndecorated(true);
		jf.getContentPane().add(getGraphicsEngine().getGLCanvas());
		jf.setVisible(true);
		
		run();
	}
	
	
	/**
	 * Method the menu calls when the game starts.
	 * La methode se charge d'appeler les moteurs chacun leur tour.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 *
	 */
	public void run() throws IOException, ClassNotFoundException {
		
		
		
		long begin, end;
		long tmp=0;
		long sleeptime=0;
		long runtime=0;
		int levelOfDetail=100;
		long counter=0;
		
		//soundEngine.play("medias/Sounds/longSounds/02_fanatic-conquerer.ogg",true);
		
		begin = System.nanoTime();
		
		while(true) {
			
			if(sleeptime>0) {
				try {
					Thread.sleep((long)(sleeptime/1000000), (int)(sleeptime%1000000));
				} catch (InterruptedException ex) {}
			}
			
			
			/*if(levelOfDetail>80) {	
				graphicsEngine.run(100);
			}
			else {
				graphicsEngine.run(levelOfDetail);
			}*/
			try {
				graphicsEngine.run();
				if(counter%PHYSICSENGINEFREQ==0)
					physicsEngine.run();
				if(counter%GAMEENGINEFREQ==0)
					gameEngine.run();
				if(counter%NETWORKENGINEFREQ==0)
					networkClient.run();
				counter++;
			} catch(Exception e) {
				e.printStackTrace();
			} //TODO:horrible try catch, for demo purposes, to be removed !
			
			tmp = begin;
			begin = System.nanoTime();
			end = System.nanoTime();
			runtime = end - tmp;
			sleeptime += PERIOD - runtime;
			
			if(runtime<PERIOD) {
				levelOfDetail+=5;
			}
			else {
				levelOfDetail-=5;
			}
			
			if(levelOfDetail>100)
				levelOfDetail=100;
			else if(levelOfDetail<0)
				levelOfDetail=0;
			
			
		}
		
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public GraphicsEngine getGraphicsEngine() {
		return graphicsEngine;
	}

	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	public NetworkClient getNetworkClient() {
		return networkClient;
	}
	
	public GuiEngine getGuiEngine(){
		return guiEngine;
	}
	
	public JFrame getJFrame(){
		return jf;
	}
	

	public void setMainChar(HeroRep c){
		physicsEngine.setMainChar(c.getPhysics());
		graphicsEngine.setMainChar(c);
		gameEngine.setMainChar(c.getHero());
		//guiEngine.setMainChar(c.getPhysics());
		//aiEngine.setMainChar(c.getPhysics());
	}
	
	
	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(final String[] args) throws ParserConfigurationException, NumberFormatException, UnknownHostException, IOException, ClassNotFoundException, InterruptedException  {
		
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(0, 0, 800, 600);
		
		if(args.length != 0 && args.length != 3){
			System.out.println("USAGE: " +
					"\n   java main.Main --client host port"
					+"\n   java main.Main --server port nbplayers"
					+"\n   java main.Main --dedicated port nbplayers");
			System.exit(0);
		}
		
		if(args.length==0) {
			new Thread() {
				public void run() {
					try {
						new NetworkServer(PORT, 1, 100);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}.start();
			Thread.sleep(100);
			new Main("localhost", PORT, jf);
			
		}
		else if(args[0].compareTo("--server")==0) {
			new Thread() {
				public void run() {
					try {
						new NetworkServer(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 100);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}.start();
			Thread.sleep(100);
			new Main("localhost", Integer.parseInt(args[1]), jf);
			
		}
		else if(args[0].compareTo("--client")==0) {
			new Main(args[1], Integer.parseInt(args[2]), jf);
			
		}
		else if(args[0].compareTo("--dedicated")==0) {
			new Thread() {
				public void run() {
					try {
						new NetworkServer(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 100);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						System.exit(0);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}.start();
		}
		else {
			System.out.println("USAGE: " +
					"\nclient: java main.Main client host port"
					+"\nserver: java main.Main server port nbplayers");
			System.exit(0);
		}
	}


	public SoundEngine getSoundEngine() {
		return soundEngine;
	}


	public DatabaseEngine getDatabaseEngine() {
		return databaseEngine;
	}


	public Random getRandom() {
		return random;
	}
	
	public void setRandom(Random r) {
		random=r;
	}



}
