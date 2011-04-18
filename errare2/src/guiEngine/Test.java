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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * To Test only the guiEngine
 */

public class Test extends JFrame {

	private int width;
	private int height;
	private GuiEngine guiEngine;

	
	public Test (int width, int height) {
		
		super("Errare GU V2.0");
		
		this.width=width;
		this.height=height;

		guiEngine = new GuiEngine();
		this.add(guiEngine.getCanvas());
		
		guiEngine.getActionBar().addAction(10001,0);
		guiEngine.getActionBar().addAction(10002,1);
		guiEngine.getActionBar().addAction(10003,2);
		guiEngine.getActionBar().addAction(10004,3);
		guiEngine.getActionBar().addAction(10005,4);
		guiEngine.getActionBar().addAction(10006,5);
		guiEngine.getActionBar().addAction(10007,6);
		guiEngine.getActionBar().addAction(10008,7);
		guiEngine.getActionBar().addAction(10009,8);
		guiEngine.getActionBar().addAction(10010,9);
		guiEngine.getActionBar().addAction(10011,10);
		guiEngine.getActionBar().addAction(10012,11);
		guiEngine.getActionBar().addAction(10013,12);
		guiEngine.getActionBar().addAction(10014,13);
		guiEngine.getActionBar().addAction(10015,14);
		guiEngine.getActionBar().addAction(10016,15);
		guiEngine.getActionBar().addAction(10101,16);
		guiEngine.getActionBar().addAction(10102,17);
		guiEngine.getActionBar().addAction(10103,18);
		guiEngine.getActionBar().addAction(10104,19);
		/*
		guiEngine.getActionBar().addAction(10105,19);
		guiEngine.getActionBar().addAction(10106,19);
		guiEngine.getActionBar().addAction(10107,19);
		guiEngine.getActionBar().addAction(10108,19);
		guiEngine.getActionBar().addAction(10109,19);
		guiEngine.getActionBar().addAction(10110,19);
		guiEngine.getActionBar().addAction(10111,19);
		guiEngine.getActionBar().addAction(10112,19);
		guiEngine.getActionBar().addAction(10113,19);
		guiEngine.getActionBar().addAction(10114,19);
		guiEngine.getActionBar().addAction(10115,19);
		guiEngine.getActionBar().addAction(10116,19);
		guiEngine.getActionBar().addAction(10117,19);
		*/
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(0301,0),0,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1507,1),1,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1602,13),2,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1703,6),13,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1801,16),6,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1804,8),16,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1806,18),8,0);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(0301,0),18,0);	
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1801,16),1,1);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(0301,0),12,1);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1507,1),3,1);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1804,8),14,1);
		guiEngine.getActionBar().getEquipement().addEquipement(new GuiRep(1801,16),15,1);
		
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(0301,0));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1507,1));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1509,2));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1602,13));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1703,6));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1801,16));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1804,8));
		guiEngine.getActionBar().getInventory().pickGui(new GuiRep(1806,18));
		
		this.setBounds(0,0,width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);

	}
	
	public static void main(String[] args) {
		
		
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension dimScreen = toolkit.getScreenSize();
	    int screenWidth = dimScreen.width;
	    int screenHeight = dimScreen.height;
	    
		Test gui = new Test(screenWidth,screenHeight);
		
	}


}
