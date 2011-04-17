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
import java.awt.GridLayout;
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

public class GuiVideoPanel extends JPanel {
	
	private static final Color color = new Color(0.5f, 0.5f, 0.5f,0f); 
	
	private int width;
	private int height;
	private int bitColor;
	private int frequency;
	private int gamma;
	private GuiEngineLaunch engine;
	
	public GuiVideoPanel (GuiEngineLaunch engine) {
		this.engine = engine;
		
		width = new Integer(engine.getDataBase().getString("options.screenwidth")).intValue();
		height = new Integer(engine.getDataBase().getString("options.screenheight")).intValue();
		bitColor = new Integer(engine.getDataBase().getString("options.bitcolor")).intValue();
		frequency = new Integer(engine.getDataBase().getString("options.frequency")).intValue();
		gamma = new Integer(engine.getDataBase().getString("options.gamma")).intValue();
		
		this.setLayout(new BorderLayout());
		this.add(new GuiVideoScreenPanel(),"Center");
	}
	
	public int getBitColor() {
		return bitColor;
	}
	
	public void setBitColor(int bitColor) {
		this.bitColor = bitColor;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public int getGamma() {
		return gamma;
	}
	
	public void setGamma(int gamma) {
		this.gamma = gamma;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	private class GuiVideoScreenPanel extends JPanel {
		
		private Image background;
		
		public GuiVideoScreenPanel () {
			
			background = (new ImageIcon("medias/GuiEngineLaunch/background.png")).getImage();
			
			this.setLayout(new GridLayout(4,1));
			
			final JLabel textResolution = new JLabel(width+"x"+height+"   ");
			textResolution.setFont(new Font("Diaolog", Font.BOLD, 15));
			JSlider sliderResolution=null;
			switch (height) { // le height a tjs une valeur differente
			case 480: sliderResolution = new JSlider(0,5,0);break;
			case 600: sliderResolution = new JSlider(0,5,1);break;
			case 768: sliderResolution = new JSlider(0,5,2);break;
			case 864: sliderResolution = new JSlider(0,5,3);break;
			case 800: sliderResolution = new JSlider(0,5,4);break;
			case 1024: sliderResolution = new JSlider(0,5,5);break;
			}		
			sliderResolution.setPaintTicks(true);
			sliderResolution.setMajorTickSpacing(1);
			sliderResolution.setSnapToTicks(true);
			sliderResolution.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textResolution.setText("640x480   ");width=640;height=480;break;
					case 1: textResolution.setText("800x600   ");width=800;height=600;break;
					case 2: textResolution.setText("1024x768   ");width=1024;height=768;break;
					case 3: textResolution.setText("1152x864   ");width=1152;height=864;break;
					case 4: textResolution.setText("1280x800   ");width=1280;height=800;break;
					case 5: textResolution.setText("1280x1024   ");width=1280;height=1024;break;
					}
					repaint();
				}}
			);
			sliderResolution.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			final JLabel textColorBit= new JLabel(""+bitColor+" bits");
			textColorBit.setFont(new Font("Diaolog", Font.BOLD, 15));	
			JSlider sliderColorBit = null;
			switch (bitColor) {
			case 16: sliderColorBit = new JSlider(0,1,0); break;
			case 32: sliderColorBit = new JSlider(0,1,1);break;
			}
			sliderColorBit.setPaintTicks(true);
			sliderColorBit.setMajorTickSpacing(1);
			sliderColorBit.setSnapToTicks(true);
			sliderColorBit.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textColorBit.setText("16 bits");bitColor=16;break;
					case 1: textColorBit.setText("32 bits");bitColor=32;break;
					}
					repaint();
				}}
			);
			sliderColorBit.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			final JLabel textFrequency= new JLabel(""+frequency+" hertz");
			textFrequency.setFont(new Font("Diaolog", Font.BOLD, 15));	
			JSlider sliderFrequency = null ;
			switch (frequency) {
			case 60: sliderFrequency = new JSlider(0,2,0); break;
			case 70: sliderFrequency = new JSlider(0,2,1); break;
			case 75: sliderFrequency = new JSlider(0,2,2); break;
			}
			sliderFrequency.setPaintTicks(true);
			sliderFrequency.setMajorTickSpacing(1);
			sliderFrequency.setSnapToTicks(true);
			sliderFrequency.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textFrequency.setText("60 hertz");frequency=60;break;
					case 1: textFrequency.setText("70 hertz");frequency=70;break;
					case 2: textFrequency.setText("75 hertz");frequency=75;break;
					}
					repaint();
				}}
			);
			sliderFrequency.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			final JLabel textGama = new JLabel("Gama : "+gamma);
			textGama.setFont(new Font("Diaolog", Font.BOLD, 15));	
			JSlider sliderGama = null ;
			switch (gamma) { // le height a tjs une valeur differente
			case 1: sliderGama = new JSlider(0,5,0);break;
			case 2: sliderGama = new JSlider(0,5,1);break;
			case 3: sliderGama = new JSlider(0,5,2);break;
			case 4: sliderGama = new JSlider(0,5,3);break;
			case 5: sliderGama = new JSlider(0,5,4);break;
			case 6: sliderGama = new JSlider(0,5,5);break;
			}
			sliderGama.setPaintTicks(true);
			sliderGama.setMajorTickSpacing(1);
			sliderGama.setSnapToTicks(true);
			sliderGama.addChangeListener(new ChangeListener () {	
				public void stateChanged(ChangeEvent e) {
					JSlider listener = (JSlider)e.getSource();
					switch (listener.getValue()) {
					case 0: textGama.setText("Gama : 1");gamma=1;break;
					case 1: textGama.setText("Gama : 2");gamma=2;break;
					case 2: textGama.setText("Gama : 3");gamma=3;break;
					case 3: textGama.setText("Gama : 4");gamma=4;break;
					case 4: textGama.setText("Gama : 5");gamma=5;break;
					case 5: textGama.setText("Gama : 6");gamma=6;break;
					}
					repaint();
				}}
			);
			sliderGama.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					repaint();			
				}			
			});
			
			this.setBorder(new CompoundBorder(new TitledBorder(null,"Affichage", 
					TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 
			
			textResolution.setBackground(color);
			sliderResolution.setBackground(color);
			textColorBit.setBackground(color);
			sliderColorBit.setBackground(color);
			textFrequency.setBackground(color);
			sliderFrequency.setBackground(color);
			textGama.setBackground(color);
			sliderGama.setBackground(color);
			
			JPanel resolution = new JPanel();
			resolution.setLayout(new BorderLayout());
			resolution.add(textResolution,"West");
			resolution.add(sliderResolution,"East");
			resolution.setBackground(color);
			
			JPanel colorbit = new JPanel();
			colorbit.setLayout(new BorderLayout());
			colorbit.add(textColorBit,"West");
			colorbit.add(sliderColorBit,"East");
			colorbit.setBackground(color);
			
			JPanel frequency = new JPanel();
			frequency.setLayout(new BorderLayout());
			frequency.add(textFrequency ,"West");
			frequency.add(sliderFrequency ,"East");
			frequency.setBackground(color);
			
			JPanel gama = new JPanel();
			gama.setLayout(new BorderLayout());
			gama.add(textGama,"West");
			gama.add(sliderGama,"East");
			gama.setBackground(color);
			
			this.setBackground(color);
			this.add(resolution);
			this.add(colorbit);
			this.add(frequency);
			this.add(gama);
			
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
