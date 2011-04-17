package guiEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import graphicsEngine.Skybox;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class GuiEngine2 extends JFrame {
	
	public GuiEngine2 (GuiSplashScreen gsc) {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
        gsc.setLocation((dimScreen.width - gsc.getSize().width)/2,(dimScreen.height - gsc.getSize().height)/2); 
        gsc.setVisible(true); 
        this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.setSize(1024,768);
		this.setLayout(null);
		this.setBackground(Color.black);
		
		Container container = this.getContentPane();
		container.setBackground(Color.black);
		
		GuiTop top = new GuiTop();
		top.setLocation(0,0);
		container.add(top);
		
		Skybox skybox=new Skybox();
		skybox.setLocation(0,145);
		container.add(skybox);
		
		GuiMenu menu = new GuiMenu();
		menu.setLocation(0,300);
		container.add(menu);
		
		gsc.setVisible(false); 
		
		
		try {
		    /*Look and feel Windows : "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
			 Look and feel CDE/Motif : "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
			 Look and feel GTK+ : "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
			 Look and feel metal : "javax.swing.plaf.metal.MetalLookAndFeel");
			 Look and feel Macintosh : "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"
			 look and feel Kunststoff : "com.incors.plaf.kunststoff.KunststoffLookAndFeel ")*/
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		
		this.setVisible(true);
		
		while(true)
		{
			skybox.timeStep(20);
			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

		public static void main(String[] args) {
		
			GuiEngine2 a = new GuiEngine2(new GuiSplashScreen());
		}
	

}
