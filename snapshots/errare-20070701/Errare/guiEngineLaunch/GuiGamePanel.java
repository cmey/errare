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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import databaseEngine.DatabaseEngine;

public class GuiGamePanel extends JPanel {
	
	private static final Color color = new Color(0.5f, 0.5f, 0.5f,0f);
	
	// Easy : 0, Medium : 1, Hard : 2
	private int difficulty;
	// slow : 0, Medium : 1, Hight : 2
	private int gameSpeed;
	// 0 (slow)... 100(hight)
	private int mouseSpeed;
	
	private GuiEngineLaunch engine;
	
	public GuiGamePanel (GuiEngineLaunch engine) {
		this.engine = engine;
		
        difficulty = new Integer(engine.getDataBase().getString("options.difficulty")).intValue();
		gameSpeed = new Integer(engine.getDataBase().getString("options.gamespeed")).intValue();
		mouseSpeed = new Integer(engine.getDataBase().getString("options.mousespeed")).intValue();
		
		this.setLayout(new BorderLayout());
		this.add(new GuiGamePrefPanel(),"Center");
		this.setBackground(color);
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public int getMouseSpeed() {
		return mouseSpeed;
	}

	public void setMouseSpeed(int mouseSpeed) {
		this.mouseSpeed = mouseSpeed;
	}
	
	private class GuiGamePrefPanel extends JPanel {

		private Image background;
		
		public GuiGamePrefPanel () {
			
			background = (new ImageIcon("medias/GuiEngineLaunch/background.png")).getImage();
			
			this.setLayout(new BorderLayout());
			final JLabel textDifficulty;
			if (difficulty==0)textDifficulty = new JLabel("Difficulty : easy");
			else if (difficulty==1)textDifficulty = new JLabel("Difficulty : medium");
			else textDifficulty = new JLabel("Difficulty : hard");
			textDifficulty.setFont(new Font("Diaolog", Font.BOLD, 15));	
			JSlider sliderDifficulty = new JSlider(0,2,difficulty); 
			sliderDifficulty.setPaintTicks(true);
			sliderDifficulty.setMajorTickSpacing(1);
			sliderDifficulty.setSnapToTicks(true);
			sliderDifficulty.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textDifficulty.setText("Difficulty : easy");difficulty=0;break;
					case 1: textDifficulty.setText("Difficulty : medium");difficulty=1;break;
					case 2: textDifficulty.setText("Difficulty : hard");difficulty=2;break;
					}
					repaint();
			}}
			);
			sliderDifficulty.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			final JLabel textGameSpeed;
			if (gameSpeed==0)textGameSpeed = new JLabel("Game speed : slow");
			else if (gameSpeed==1)textGameSpeed = new JLabel("Game speed  : medium");
			else textGameSpeed = new JLabel("Game speed: hight");
			textGameSpeed.setFont(new Font("Diaolog", Font.BOLD, 15));	
			JSlider sliderGameSpeed = new JSlider(0,2,gameSpeed); 
			sliderGameSpeed.setPaintTicks(true);
			sliderGameSpeed.setMajorTickSpacing(1);
			sliderGameSpeed.setSnapToTicks(true);
			sliderGameSpeed.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textGameSpeed.setText("Game speed : slow");gameSpeed=0;break;
					case 1: textGameSpeed.setText("Game speed : medium");gameSpeed=1;break;
					case 2: textGameSpeed.setText("Game speed : hight");gameSpeed=2;break;
					}
					repaint();
			}}
			);
			sliderGameSpeed.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			final JLabel textMouseSpeed = new JLabel("Mouse speed : "+mouseSpeed);
			textMouseSpeed.setFont(new Font("Diaolog", Font.BOLD, 15));	
			final JSlider sliderMouseSpeed = new JSlider(0,100,mouseSpeed); 
			sliderMouseSpeed.setPaintTicks(false);
			sliderMouseSpeed.setMajorTickSpacing(1);
			sliderMouseSpeed.setSnapToTicks(false);
			sliderMouseSpeed.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					textMouseSpeed.setText("Mouse speed : "+sliderMouseSpeed.getValue());
					mouseSpeed=sliderMouseSpeed.getValue();
					repaint();
				}}
			);
			sliderMouseSpeed.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			this.setBorder(new CompoundBorder(new TitledBorder(null,"Préférences", 
					  TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 

			textDifficulty.setBackground(color);
			sliderDifficulty.setBackground(color);
			textGameSpeed.setBackground(color);
			sliderGameSpeed.setBackground(color);
			textMouseSpeed.setBackground(color);
			sliderMouseSpeed.setBackground(color);
			
			JPanel difficulty = new JPanel();
			difficulty.setLayout(new BorderLayout());
			difficulty.add(textDifficulty,"West");
			difficulty.add(sliderDifficulty,"East");
			difficulty.setBackground(color);
			
			JPanel gameSpeed = new JPanel();
			gameSpeed.setLayout(new BorderLayout());
			gameSpeed.add(textGameSpeed,"West");
			gameSpeed.add(sliderGameSpeed,"East");
			gameSpeed.setBackground(color);
			
			JPanel mouseSpeed = new JPanel();
			mouseSpeed.setLayout(new BorderLayout());
			mouseSpeed.add(textMouseSpeed,"West");
			mouseSpeed.add(sliderMouseSpeed,"East");
			mouseSpeed.setBackground(color);
			
			this.setBackground(color);
			this.add(difficulty,"North");
			this.add(gameSpeed,"Center");
			this.add(mouseSpeed,"South");

		}
		
		/**
		 * Methode paintComponent
		 * Display objects on the JPanel  
		 */
		
	public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			g.drawImage(background,0,0,null);
					
	}

	}

}
