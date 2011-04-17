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
import java.awt.event.MouseListener;
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

public class GuiAudioPanel extends JPanel {
	
	private static final Color color = new Color(0.5f, 0.5f, 0.5f,0f);
	
	private int loudness;
	
	private GuiEngineLaunch engine;
	
	public GuiAudioPanel (GuiEngineLaunch engine) {
		
		this.engine = engine;
		loudness = new Integer(engine.getDataBase().getString("options.loudness")).intValue();
		
		this.setLayout(new BorderLayout());
		this.add(new GuiAudioLoudnessPanel(),"Center");
	}
	
	public int getloudness() {
		return loudness;
	}
	
	public void setloudness(int loudness) {
		this.loudness = loudness;
	}
	
	
	
	
	private class GuiAudioLoudnessPanel extends JPanel {
		
		private Image background;
		
		public GuiAudioLoudnessPanel () {
			
			background = (new ImageIcon("medias/GuiEngineLaunch/background.png")).getImage();
			
			this.setLayout(new BorderLayout());
			
			final JLabel textVolume = new JLabel("Volume : "+loudness+"%");
			textVolume.setFont(new Font("Diaolog", Font.BOLD, 15));	
			final JSlider sliderVolume = new JSlider(0,100,loudness); 
			sliderVolume.setPaintTicks(false);
			sliderVolume.setMajorTickSpacing(1);
			sliderVolume.setSnapToTicks(false);
			sliderVolume.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					textVolume.setText("Volume : "+sliderVolume.getValue()+"%");
					loudness=sliderVolume.getValue();
					repaint();
				}}
			);
			sliderVolume.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			this.setBorder(new CompoundBorder(new TitledBorder(null,"Volume Sonore", 
					TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 
			
			textVolume.setBackground(color);
			sliderVolume.setBackground(color);
			
			JPanel volume = new JPanel();
			volume.setLayout(new BorderLayout());
			volume.add(textVolume,"West");
			volume.add(sliderVolume,"East");
			volume.setBackground(color);
			
			this.setBackground(color);
			this.add(volume,"Center");
	
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
