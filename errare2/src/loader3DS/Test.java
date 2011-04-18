/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package loader3DS;

import genericReps.SettingRep;
import geom.AABox;
import geom.Point;
import geom.Vector;
import graphicsEngine.FollowCamera;
import graphicsEngine.GraphicalRep;
import graphicsEngine.GraphicalRepFactory;
import graphicsEngine.GraphicsEngine;
import graphicsEngine.Skydome;
import graphicsEngine.World;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.opengl.util.BufferUtil;


import loader3DS.ModelLoader;
import physicsEngine.Octree;
import physicsEngine.PhysicalRep;
import userInputEngine.UserInputController;
import xmlEngine.XmlEngine;

public class Test implements Runnable{
	
	static GraphicsEngine ge;
	public static long period = (long)((1.0/60.0)*1E9);
	private static JFrame window;
	private static XmlEngine xmlEngine;
	private static UserInputController userInputController;
	
	public static void main (String[] args) throws Exception {
		initGraphics();
		//initFullscreen();
		
		AABox rootbox = new AABox(new Point(-128,-128,-128), new Point(128,128,128));
		Octree root = new Octree(rootbox, null, 3);
		
		
		// 3DS TEST
		GraphicalRep model3DS = GraphicalRepFactory.load("loader3DS/box_normal.3ds");
		//GraphicalRep model3DS = GraphicalRepFactory.load("loader3DS/box.3ds");
		//GraphicalRep model3DS = GraphicalRepFactory.load("freeworld/meshes/unsorted/maison01.3ds");
		PhysicalRep phys = new PhysicalRep(new Point(0,0,0), new Point(10,10,10));
		SettingRep re = new SettingRep(phys, model3DS, 2);
		AABox md2BB = new AABox(model3DS.getGraphicalBoundaries());
		AABox md2BB2 = new AABox(model3DS.getGraphicalBoundaries());
		PhysicalRep physmd2 = new PhysicalRep(md2BB.getLeftFrontBottom(), md2BB.getRightBackTop());
		PhysicalRep physmd22 = new PhysicalRep(md2BB2.getLeftFrontBottom(), md2BB2.getRightBackTop());
		root.addContent(phys);
		
		model3DS.playAnimationInLoop("anim");
		//model3DS.setAnimationFPS(10);
		// MD2 TEST
		/*GraphicalRep modelMD2 = GraphicalRepFactory.load("data/md2/pknight/tris.md2");
		AABox md2BB = new AABox(modelMD2.getGraphicalBoundaries());
		PhysicalRep physmd2 = new PhysicalRep(md2BB.getLeftFrontBottom(), md2BB.getRightBackTop());
		SettingRep remd2 = new SettingRep(physmd2, modelMD2, 3);
		root.addContent(physmd2);
		modelMD2.setAnimationFPS(30);*/
		
		// MD2 TEST SHARING
		/*GraphicalRep modelMD22 = GraphicalRepFactory.load("data/md2/pknight/tris.md2");
		AABox md2BB2 = new AABox(modelMD22.getGraphicalBoundaries());
		md2BB2.translate(100, 0, 0);
		PhysicalRep physmd22 = new PhysicalRep(md2BB2.getLeftFrontBottom(), md2BB2.getRightBackTop());
		SettingRep remd22 = new SettingRep(physmd22, modelMD22, 3);
		root.addContent(physmd22);
		modelMD22.setAnimationFPS(30);
		
		ObjectOutputStream w = new ObjectOutputStream(new FileOutputStream("AGraphicalRep.era"));
		w.writeObject(modelMD2);
		w.close();
		ObjectInputStream i = new ObjectInputStream(new FileInputStream("AGraphicalRep.era"));
		try {
			modelMD2 = (GraphicalRep)i.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		i.close();
		*/
		
		//System.out.println("topology length = "+model3DS.mesh.topology.length);
		
		
		Skydome sky = new Skydome(Skydome.SKY_CLEAR);
		
		FollowCamera cam = new FollowCamera(physmd2.getCenter());
		//EyeCamera cam = new EyeCamera();
		
		World world1 = new World(root, sky, cam);
		ge.changeWorld(world1);
		
		finish();
	}
	
	
	/**
	 * Set visible the frame and start running the graphics engine
	 */
	private static void finish(){		
		window.add(ge.getGLComponent());
		window.setVisible(true);
		Thread graphicsEngineMainLoop = new Thread(new Test());
		graphicsEngineMainLoop.start();
	}
	
	/**
	 * Create the frame, configure it and create a graphics engine
	 */
	private static void initGraphics(){
		window = new JFrame("Graphics Engine");
		window.setUndecorated(true);
		window.setSize(new Dimension(800,600));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			xmlEngine = new XmlEngine();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(0);
		}
		userInputController = new UserInputController("test", xmlEngine);
		boolean debug = true;
		ge = new GraphicsEngine(window, userInputController, debug);
		window.requestFocusInWindow();
		
		
		
	}
	
	/**
	 * go to full screen mode
	 */
	private static void initFullscreen(){
		window.setUndecorated(true);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		GraphicsDevice device = devices[0]; 
		device.setFullScreenWindow(window);
		window.validate();
	}

	/**
	 * Makes the graphics engine run, delivering ticks at a constant speed
	 */
	public void run(){
		long begin, end;
		int sleeptime=0;
		System.out.println("Graphics Engine running !");
		while(true){
			begin = System.nanoTime();
			ge.run();
			end = System.nanoTime();
			sleeptime = (int) (period - (end-begin) + sleeptime);
			ge.info_sleeptime(sleeptime);
			if(sleeptime < 0) sleeptime = 0;
			if(sleeptime>0) {
				long sleepstart = System.nanoTime();
				try {
					Thread.sleep(sleeptime/1000000, sleeptime%1000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sleeptime = (int) (sleeptime - (System.nanoTime()-sleepstart));
			}
		}
	}
	
}
