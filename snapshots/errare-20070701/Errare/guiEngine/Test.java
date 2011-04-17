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

import main.ItemRep;
import net.java.games.jogl.GLCanvas;
import net.java.games.jogl.GLCapabilities;
import net.java.games.jogl.GLDrawableFactory;

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
		
		guiEngine.getActionBar().addAction(5001,0);
		guiEngine.getActionBar().addAction(5101,1);
		guiEngine.getActionBar().addAction(5201,2);
		guiEngine.getActionBar().addAction(5202,3);
		guiEngine.getActionBar().addAction(5305,4);
		guiEngine.getActionBar().addAction(5401,5);
		
		guiEngine.getActionBar().getEquipement().addEquipement(0301,0,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1507,1,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1509,2,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1602,13,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1703,6,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1801,16,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1804,8,0);
		guiEngine.getActionBar().getEquipement().addEquipement(1806,18,0);	
		guiEngine.getActionBar().getEquipement().addEquipement(1507,1,1);
		guiEngine.getActionBar().getEquipement().addEquipement(1602,12,1);
		guiEngine.getActionBar().getEquipement().addEquipement(1804,3,1);
		guiEngine.getActionBar().getEquipement().addEquipement(1806,14,1);
		guiEngine.getActionBar().getEquipement().addEquipement(1603,15,1);
		
		guiEngine.getActionBar().getInventory().pick(new ItemRep(null,null,null,new GuiRep(1,1)));

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
