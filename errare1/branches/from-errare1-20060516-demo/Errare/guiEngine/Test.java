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

import javax.swing.JFrame;

/**
 * To Test only the guiEngine
 */

public class Test extends JFrame {

	private int width,height;
	private GuiEngine guiEngine;

	
	public Test (int width, int height) {
		
		super("Errare V1.0");
		
		this.width=width;
		this.height=height;

		guiEngine = new GuiEngine();
		this.add(guiEngine.getCanvas());
		this.setBounds(0,0,width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);

	}
	
	public static void main(String[] args) {
		
		//DisplayMode dm = new DisplayMode(1024,768,32,DisplayMode.REFRESH_RATE_UNKNOWN);
		Test errare = new Test(1024,768);
		
	}


}
