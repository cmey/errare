package guiEngine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Class GuiInventory extends JInternalFrame
 * @author Ak
 * @version 0.1
 */

@SuppressWarnings("serial")

public class GuiBar extends JInternalFrame {
	

	
	/**
	 * GuiInventory Constructor
	 * Create a JInternalFrame that popup on the screen whitch the user can
	 * organise his character
	 */
	
	public GuiBar() {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();

		this.setTitle("Title");
		this.setLocation((1024/2)-143,768-60);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.getContentPane().add(new GuiBarPanel(286,60,this));
		this.pack();
		try {
		    /*Look and feel Windows : "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
			 Look and feel CDE/Motif : "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
			 Look and feel GTK+ : "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
			 Look and feel metal : "javax.swing.plaf.metal.MetalLookAndFeel");
			 Look and feel Macintosh : "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"
			 look and feel Kunststoff : "com.incors.plaf.kunststoff.KunststoffLookAndFeel ")*/
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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

	}
	
	
}
