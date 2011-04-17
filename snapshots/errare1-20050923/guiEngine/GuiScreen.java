package guiEngine;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * class GuiScreen
 * @author Ak
 * @version 0.1
 */

public class GuiScreen {
	
//	Graphics Card
	private GraphicsDevice gd;
	
	/**
	 * GuiScreen Constructor 
	 * Initialise GraphicsDevice
	 */
	
	public GuiScreen() {
		
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
	}
	
	/**
	 * setFullScreen fonction
	 * @param window the window that you want change the resolution
	 * @param displayMode the graphic mode
	 */
	
	public void setFullScreen(JFrame window, DisplayMode displayMode) {
		if (window == null) return;
		window.setUndecorated(true);
		window.setResizable(false);
		// FullScreen
		if (gd.isFullScreenSupported()) gd.setFullScreenWindow(window);
		// Display Mode
		if ((displayMode != null) && (gd.isDisplayChangeSupported())) {
			try {
				gd.setDisplayMode(displayMode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
		//		gd.setDisplayMode(new DisplayMode(displayMode.getWidth(),displayMode.getHeight(),displayMode.getBitDepth(),displayMode.getRefreshRate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * getFullScreen fonction
	 * @return the window (JFrame)
	 */
	
	public JFrame getFullScreen() {
		return (JFrame) gd.getFullScreenWindow();
	}
	
	/**
	 * getGraphicsDevice
	 * @return the graphic card
	 */
	
	public GraphicsDevice getGraphicsDevice(){
		return gd;
	}
	
	/**
	 * restoreFullScreen fonction
	 * Stop the fullscreen mode
	 */
	
	public void restoreFullScreen() {
		JFrame window = getFullScreen();
		if (window != null) window.dispose();
		gd.setFullScreenWindow(null);
	}
	
	public static void main(String[] args) {
		
		GuiScreen sm = new GuiScreen();
		DisplayMode dm = new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
		sm.setFullScreen(new JFrame(),dm);
		
	}
}