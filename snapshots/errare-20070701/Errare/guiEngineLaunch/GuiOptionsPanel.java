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
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import databaseEngine.DatabaseEngine;

public class GuiOptionsPanel extends JPanel {
	
	private static final Color color = new Color(0.5f, 0.5f, 0.5f,0f);
	
	private Image center;
	
	private GuiGamePanel guiGame;
	private GuiVideoPanel guiVideo;
	private GuiAudioPanel guiAudio;
	private GuiEngineLaunch engine;
	private GuiLow low;
	
	public GuiOptionsPanel (GuiEngineLaunch engine, GuiLow low) {
		
		this.engine = engine;
		this.low = low;
		center = (new ImageIcon("medias/GuiEngineLaunch/center.png")).getImage();
		this.setLayout(new BorderLayout());
		guiGame = new GuiGamePanel(engine);
		guiVideo = new GuiVideoPanel(engine);
		guiAudio = new GuiAudioPanel(engine);
JTabbedPane tabOptions = new JTabbedPane();
tabOptions.setFont(new Font("Diaolog", Font.BOLD, 15));
tabOptions.add("Game",guiGame);
tabOptions.add("Graphic",guiVideo);
tabOptions.add("Sound",guiAudio);
tabOptions.setTabPlacement(JTabbedPane.TOP);
tabOptions.setOpaque(false);
tabOptions.setToolTipTextAt(0,"Game options");
tabOptions.setToolTipTextAt(1,"Graphic options");
tabOptions.setToolTipTextAt(2,"Sound options");
tabOptions.setBackground(color);
this.setBackground(color);
this.add(tabOptions,"Center");
this.add(new GuiOptionValidatePanel(),"South");
this.setSize(524,244);
this.setLocation(100,50);

tabOptions.addMouseMotionListener(new MouseMotionListener() {

	public void mouseDragged(MouseEvent arg0) {
		repaint();		
	}
	public void mouseMoved(MouseEvent arg0) {
		repaint();	
	}		
});

tabOptions.addChangeListener(new ChangeListener() {

	public void stateChanged(ChangeEvent arg0) {
repaint();
	}
	
});

}
	/**
	 * Methode paintComponent
	 * Display objects on the JPanel  
	 */
	
public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(center,0,0,null);
				
}

private class GuiOptionValidatePanel extends JPanel implements ActionListener {
	
	private JButton ok,cancel,defaut;
	
	private Image background;
	
	public GuiOptionValidatePanel () {
		
		background = (new ImageIcon("medias/GuiEngineLaunch/background.png")).getImage();
		
		this.setLayout(new FlowLayout());
		 	
		String description = ""; 
		
		ok = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/ok.png")); 
		ok.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/ok.png")); 
		ok.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/ok.png")); 
		ok.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/ok.png"));
		ok.setMargin(new Insets(0,0,0,0));
	 	ok.addActionListener(this);
	 	
	 	cancel = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/cancel.png")); 
	 	cancel.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/cancel.png")); 
	 	cancel.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/cancel.png")); 
	 	cancel.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/cancel.png"));
	 	cancel.setMargin(new Insets(0,0,0,0));
	 	cancel.addActionListener(this);
	 	
	 	defaut = new JButton(description, new ImageIcon("medias/GuiEngineLaunch/default.png")); 
	 	defaut.setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/default.png")); 
	 	defaut.setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/default.png")); 
	 	defaut.setDisabledIcon(new ImageIcon("medias/GuiEngineLaunch/default.png"));
	 	defaut.setMargin(new Insets(0,0,0,0));
	 	defaut.addActionListener(this);
	 	
	 	this.setBackground(color);
	 	this.add(ok);
		this.add(cancel);
		this.add(defaut);
		
		
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource()==ok) {
			
				try {
					DatabaseEngine t = new DatabaseEngine();
					t.set("options.difficulty",guiGame.getDifficulty());
					 t.set("options.gamespeed",guiGame.getGameSpeed());
					 t.set("options.mousespeed",guiGame.getMouseSpeed());
					 t.set("options.loudness", guiAudio.getloudness());
					 t.set("options.screenwidth",guiVideo.getWidth());
					 t.set("options.screenheight",guiVideo.getHeight());
					 t.set("options.bitcolor",guiVideo.getBitColor());
					 t.set("options.frequency",guiVideo.getFrequency());
					 t.set("options.gamma", guiVideo.getGamma());
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}
				//engine.deleteCenter();
				//repaint();
				low.getLoadGame().setEnabled(true);
				low.getOptions().setEnabled(true);
				low.getExit().setEnabled(true);

		}else if (event.getSource()==cancel) {
			
		}else if (event.getSource()==defaut) {
			
		}	
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
