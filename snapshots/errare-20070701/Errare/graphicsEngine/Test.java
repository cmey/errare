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

package graphicsEngine;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import javax.swing.JFrame;

public class Test implements Runnable{
	
	static GraphicsEngine ge;
	public static long period = (long)((1.0/60.0)*1E9);
	
	public static void main (String[] args) {
		
		JFrame window = new JFrame("Graphics Engine");
		ge = new GraphicsEngine(window);
		window.setUndecorated(true);
		window.setSize(new Dimension(800,600));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.requestFocusInWindow();
		// FULL SCREEN
		/*
		window.setUndecorated(true);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		 GraphicsDevice[] devices = env.getScreenDevices();
		 GraphicsDevice device = devices[0]; 
		 device.setFullScreenWindow(window);
		 window.validate();
		 */
		// FULL SCREEN
		LinkedList<GraphicalRep> glist = new LinkedList<GraphicalRep>();
		
		//GraphicalRep p2 = new Particles_Ring("medias/GraphicsEngine/Sky/star.png");
		//glist.add(p2);
		
		//GraphicalRep f = new MD2("medias/GraphicsEngine/md2/tree1.md2");
		for(int i=0;i<1;i++){
			glist.add(new MD2("data/md2/bauul/bauul.md2"));
			glist.add(new MD2("data/md2/evilernie/evilernie.md2"));
			glist.add(new MD2("data/md2/goblin/goblin.md2"));
			glist.add(new MD2("data/md2/ichabod/ichabod.md2"));
			glist.add(new MD2("data/md2/ogre/ogre.md2"));
			glist.add(new MD2("data/md2/purgator/purgator.md2"));
			glist.add(new MD2("data/md2/rat/rat.md2"));
			glist.add(new MD2("data/md2/hellpig/hellpig.md2"));
			glist.add(new MD2("data/md2/cobra/cobra.md2"));
			glist.add(new MD2("data/md2/demoness/demoness.md2"));
			glist.add(new MD2("data/md2/yohko/yohko.md2"));
		}
		
		//MD2 shad = new MD2("medias/GraphicsEngine/md2/terminator.md2");
		//shad.cast_shadow=true;
		//glist.add(shad);
		
		
		//GraphicalRep p = new Particles_Vortex("medias/GraphicsEngine/Sky/star.png");
		//glist.add(p);
		
		//glist.add(new GraphicalRep("medias/sword.3ds", GraphicalRep.FORMAT_3DS));
		
		// animation test
		System.out.println("Animation Test :");
		//MD2 ani = new MD2("medias/GraphicsEngine/md2/hobgoblin.md2");
		//System.out.println(ani.getAnimationsNames());
		//ani.startAnimation("death");
		//glist.add(ani);
		//float[] bounds = ani.getPhysicalBounds();
		//for(int i=0; i<bounds.length; i++)
		//	System.out.print((int)bounds[i]+" ");
		System.out.println();
		
		ge.debugsetDisplayList(glist);
		ge.debugsetDisplayList(glist);
		
		
		window.add(ge.getGLCanvas());
		
		window.setVisible(true);
		
		Thread graphicsEngineMainLoop = new Thread(new Test());
		graphicsEngineMainLoop.start();
	}
	
	public void run(){
		long begin, end;
		int sleeptime=0;
		System.out.println("Engine running !");
		while(true){
			begin = System.nanoTime();
			ge.run();
			end = System.nanoTime();
			sleeptime = (int) (period - (end-begin) + sleeptime);
			//System.out.println("must sleep:"+sleeptime);
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
				//System.out.println("missed: "+sleeptime);
			}
		}
	}
}
