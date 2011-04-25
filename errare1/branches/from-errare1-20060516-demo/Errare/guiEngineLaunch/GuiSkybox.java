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

package guiEngineLaunch;

import graphicsEngine.Skybox;

import javax.swing.JPanel;

public class GuiSkybox extends JPanel implements Runnable {
	
	private Skybox skybox;
	
	public GuiSkybox() {
		skybox=new Skybox();
		this.add(skybox.getGLCanvas());

	}
	
	public void run() {
		while(true) {
			skybox.timeStep(20);
			//System.out.println("run");
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}
