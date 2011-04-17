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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;

import networkEngine.NetworkServer;

import main.Main;

public class GuiLoadGamePanel extends JPanel {
	
	private static final Color color = new Color(0.0f, 0.0f, 0.0f,0f);
	private static final Color color2 = new Color(0.5f, 0.0f, 0.0f,0.2f);
	private Image background;
	private JRadioButton [] tabSave = new JRadioButton [4]; 
	private JTextField serverPort,clientIp,clientPort;
	private JButton serverGo,clientGo;
	private JLabel serverLab,clientLab1,clientLab2;
	private JCheckBox dedicated;
	private ButtonGroup group;
	private GuiEngineLaunch engine;
	
	public GuiLoadGamePanel (final GuiEngineLaunch engine) {
		
		this.engine = engine;
		background = (new ImageIcon("medias/GuiEngineLaunch/background.png")).getImage();
		
		this.setLayout(new GridLayout(3,1,0,5));
		
		// Create Server Panel
		JPanel server = new JPanel();
		server.setBorder(new CompoundBorder(new TitledBorder(null,"Create a server", 
				TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 
		server.setLayout(new FlowLayout());
		serverLab = new JLabel("Server Port : ");
		serverPort = new JTextField("1099");
		serverPort.setColumns(4);
		serverPort.setBackground(Color.orange);
		dedicated = new JCheckBox("Create a dedicated server");
		dedicated.setSelected(false);
		dedicated.setBackground(Color.orange);
		serverGo = new JButton("Launch Server");
		serverGo.setBackground(Color.yellow);
		serverGo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				if (dedicated.isSelected()) {
				new Thread() {
					public void run() {
						
						try {
							System.out.println("dedicated");
							new NetworkServer(1099, 1, 100);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							System.exit(0);
						} catch (IOException e) {
							e.printStackTrace();
							System.exit(0);
						}
					}
				}.start();
				}else {
					new Thread() {
						public void run() {
							try {
								new NetworkServer(1099, 1, 100);
							} catch (NumberFormatException e) {
								e.printStackTrace();
								System.exit(0);
							} catch (IOException e) {
								e.printStackTrace();
								System.exit(0);
							}
						}
					}.start();
					
					try {
						Thread.sleep(100);
						engine.dispose();
						new Main("localhost", 1099, new JFrame());
					} catch (HeadlessException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
		
			}
			
		});
		server.add(serverLab);
		server.add(serverPort);
		server.add(dedicated);
		server.add(serverGo);
		server.setBackground(color);
		
		// Create Client Panel
		JPanel client = new JPanel();
		client.setBorder(new CompoundBorder(new TitledBorder(null,"Join a game", 
				TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 
		client.setLayout(new FlowLayout());
		clientLab1 = new JLabel("Server IP : ");
		clientIp = new JTextField("192.168.0.X");
		clientIp.setColumns(15);
		clientIp.setBackground(Color.orange);
		clientLab2 = new JLabel("Server port : ");
		clientPort = new JTextField("1099");
		clientPort.setColumns(4);
		clientPort.setBackground(Color.orange);
		clientGo = new JButton("Join");
		clientGo.setBackground(Color.yellow);
		clientGo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				try {
					engine.dispose();
					JFrame jf = new JFrame();
					Main main = new Main(clientIp.getText(),new Integer(clientPort.getText()).intValue(),jf);
				
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		});
		client.add(clientLab1);
		client.add(clientIp);
		client.add(clientLab2);
		client.add(clientPort);
		client.add(clientGo);
		client.setBackground(color);
		
		// Battle Net
		JPanel netServer = new JPanel();
		netServer.setBorder(new CompoundBorder(new TitledBorder(null,"Net Server", 
				TitledBorder.LEFT, TitledBorder.TOP),new EmptyBorder(5,5,5,5))); 
		netServer.setLayout(new GridLayout(2,1,0,2));
		group = new ButtonGroup();
		
		for (int i=0;i<2;i++) {
			String description = "Vide";
			tabSave[i] = new JRadioButton (description,new ImageIcon("medias/GuiEngineLaunch/save.png"));
			tabSave[i].setPressedIcon(new ImageIcon("medias/GuiEngineLaunch/save.png")); 
			tabSave[i].setRolloverIcon(new ImageIcon("medias/GuiEngineLaunch/saveOn.png")); 
			tabSave[i].setRolloverSelectedIcon(new ImageIcon("medias/GuiEngineLaunch/savePress.png")); 
			tabSave[i].setSelectedIcon(new ImageIcon("medias/GuiEngineLaunch/savePress.png")); 
			tabSave[i].setMargin(new Insets(0,0,0,0));
			tabSave[i].setBackground(color2);
			group.add(tabSave[i]);
			tabSave[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					for (int i=0;i<2;i++) {
						if (tabSave[i].isSelected()) {
							//tabSave[i].getName();
							System.out.println("Battle Net load not implemented");
						}
					}
					repaint();
				}
			});
			netServer.add(tabSave[i]);
		}
		netServer.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				repaint();
			}
		});
		netServer.setBackground(color);
		
		this.add(server);
		this.add(client);
		this.add(netServer);
		
		this.setBackground(color);
		this.setSize(524,244);
		this.setLocation(100,50);


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
