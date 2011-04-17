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

package main2;

import gameEngine.Hero;
import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicsEngine;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import databaseEngine.DatabaseEngine;

import networkEngine2.NetworkEngine; // renamed networkEngine2.Handler to comply with the general design (linxsam)

import physicsEngine2.PhysicalRep;
import physicsEngine2.PhysicsEngine;
import userInputEngine.UserInputController;

public class Main {
	

	public static final long PERIOD = (long)((1.0/60.0)*1E9);
	
	private PhysicsEngine physicsEngine;
	private GraphicsEngine graphicsEngine;
	private NetworkEngine networkEngine;
	
	private JFrame jf;
	private Random random;

	private DatabaseEngine databaseEngine;

	private UserInputController userInputControler;

	
	

	public Main(JFrame jf) throws ParserConfigurationException, UnknownHostException, IOException, ClassNotFoundException {
		
		this.jf=jf;
		
		databaseEngine = new DatabaseEngine();
		userInputControler = new UserInputController("test", databaseEngine);
		
		jf.getContentPane().addMouseListener(userInputControler);
		jf.getContentPane().addMouseMotionListener(userInputControler);
		jf.getContentPane().addMouseWheelListener(userInputControler);
		jf.getContentPane().addKeyListener(userInputControler);
		
		
		physicsEngine = new PhysicsEngine(this);
		graphicsEngine = new GraphicsEngine(this);
		networkEngine = new NetworkEngine(this);
		random = new Random(1);
		
		jf.setTitle("Errare Version Alpha");
		jf.setUndecorated(true);
		setWindowedMode(jf);
		jf.getContentPane().add(getGraphicsEngine().getGLCanvas());
		jf.validate();
		jf.setVisible(true);
		
		run();
	}
	
	private void setFullScreenMode(JFrame jf) {
		GraphicsEnvironment env = GraphicsEnvironment.
		getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		GraphicsDevice device = devices[0];
		
		
		device.setFullScreenWindow(jf);
	}
	
	private void setWindowedMode(JFrame jf) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
		jf.setBounds(0,0,dimScreen.width,dimScreen.height);
	}
	
	
	public void run() throws IOException, ClassNotFoundException {	
		
		long begin, end;
		long tmp=0;
		long sleeptime=0;
		long runtime=0;
		
		begin = System.nanoTime();
		
		while(true) {
			
			if(sleeptime>0) {
				try {
					Thread.sleep((long)(sleeptime/1000000), (int)(sleeptime%1000000));
				} catch (InterruptedException ex) {}
			}
			
			
			physicsEngine.run();
			graphicsEngine.run();
			networkEngine.run();
			
			tmp = begin;
			begin = System.nanoTime();
			end = System.nanoTime();
			runtime = end - tmp;
			sleeptime += PERIOD - runtime;
			
		}
		
	}

	

	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	
	public JFrame getJFrame(){
		return jf;
	}
	

	public void setMainChar(CharacterRep<Hero, PhysicalRep, GraphicalRep> c){
		physicsEngine.setMainChar(c.getPhysicalRep());
		//networkEngine.setMainChar(c.getPhysicalRep()); // I would prefer not to have a "main" caracter... but will do with (linxsam)
		//graphicsEngine.setMainChar(c.getPhysicalRep());
	}

	public Random getRandom() {
		return random;
	}
	


	public GraphicsEngine getGraphicsEngine() {
		return graphicsEngine;
	}



	public NetworkEngine getNetworkEngine() {
		return networkEngine;
	}	

	public DatabaseEngine getDatabaseEngine() {
		return databaseEngine;
	}

	public UserInputController getUserInputControler() {
		return userInputControler;
	}

	public static void main(final String[] args) throws UnknownHostException, ParserConfigurationException, IOException, ClassNotFoundException{
		
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new Main(jf);
	}
}
