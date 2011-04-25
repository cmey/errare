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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class GuiLow extends JPanel implements ActionListener {
	
	private JButton loadGame,options,exit;
	private Image low;
	private JFrame mainWindow ;
	private GuiEngineLaunch engine;

	
	
	public GuiLow(JFrame mainWindow, GuiEngineLaunch engine) {

		this.mainWindow = mainWindow;
		this.engine=engine;
		
		String description = ""; 
		
	 	loadGame = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/loadGame.png")); 
	 	loadGame.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/loadGamePress.png")); 
	 	loadGame.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/loadGameOn.png")); 
	 	loadGame.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/loadGame.png"));
	 	loadGame.setMargin(new Insets(-2,-2,-2,-2));
	 	loadGame.addActionListener(this);
	 	
		options = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/options.png")); 
		options.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/optionsPress.png")); 
		options.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/optionsOn.png")); 
		options.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/options.png"));
		options.setMargin(new Insets(-2,-2,-2,-2));
		options.addActionListener(this);
	 	
	 	exit = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/exit.png")); 
		exit.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/exitPress.png")); 
		exit.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/exitOn.png")); 
		exit.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/exit.png"));
	 	exit.setMargin(new Insets(-2,-2,-2,-2));
	 	exit.addActionListener(this);

		this.add(loadGame);
		this.add(options);
		this.add(exit);
	
		low = (new ImageIcon("medias/GuiEngineLaunch/low.png")).getImage();

		this.setSize(1024,200);

	}
	
	
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource()==loadGame) {
			
			engine.deleteCenter();
			GuiLoadGamePanel loadGame = new GuiLoadGamePanel(engine);
			loadGame.setLocation(250,324);
			mainWindow.add(loadGame);
			mainWindow.validate();
			mainWindow.repaint();

			
		}else if (event.getSource()==options) {

			engine.deleteCenter();
			GuiOptionsPanel options = new GuiOptionsPanel(engine,this);
			options.setLocation(250,324);
			mainWindow.add(options);
			loadGame.setEnabled(false);
			options.setEnabled(false);
			exit.setEnabled(false);
		    mainWindow.validate();
			mainWindow.repaint();


		}else if (event.getSource()==exit) {
			System.exit(0);
		}
		
	}

	
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(low,0,0,null);
		
	}


	public JButton getExit() {
		return exit;
	}


	public JButton getLoadGame() {
		return loadGame;
	}


	public JButton getOptions() {
		return options;
	}


	
}
