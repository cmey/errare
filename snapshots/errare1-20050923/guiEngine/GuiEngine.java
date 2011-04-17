/**
 * 
 */
package guiEngine;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

/**
 * @author Administrateur
 *
 */
public class GuiEngine {
	
	private JFrame window;
	private JDesktopPane desktop;
	
	public GuiEngine(JFrame window) {
		
		this.window=window;
		desktop = new JDesktopPane();	    
		this.window.getContentPane().add(desktop);
		GuiInventory guiInv = new GuiInventory();	
		GuiStatistics guiSta = new GuiStatistics();
		GuiBar guiBar = new GuiBar();
		GuiInfos guiInf = new GuiInfos();
		desktop.add(guiInv);	
		desktop.add(guiSta);
		desktop.add(guiBar);
		desktop.add(guiInf);
		try   {
			guiInv.setSelected(true);
			guiSta.setSelected(true);
			guiBar.setSelected(true);
			guiInf.setSelected(true);
		} catch(java.beans.PropertyVetoException e)    {
			System.out.println("Erreur: " + e.toString());
		}	
		window.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setSize(new Dimension(1024,768));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GuiEngine guiEngine = new GuiEngine(window);	
		
	}
	
}
