package guiEngine;

import graphicsEngine.Skybox;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class GuiSkybox extends JPanel implements Runnable {
	
	private Skybox skybox;
	
	public GuiSkybox() {
		skybox=new Skybox();
		skybox.setSize(800,300);
		skybox.setVisible(true);
	
	}
	
	public void run() {
		while(true) {
			skybox.timeStep(20);
			System.out.println("run");
			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}
