package graphicsEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.java.games.jogl.GL;

public class KeyboardHelper implements KeyListener, Runnable{	
	private float zRotation;
	private float xRotation;
	public float xPosition;
	public float yPosition;
	public float zPosition;
	public float camX;
	public float camY;
	public float camZ;
	public boolean[] keys = new boolean[256];
	
	public void updateCamera(GL gl){
		gl.glTranslatef(xPosition, yPosition, zPosition);
		gl.glRotated(zRotation, 0, 0, 1);
		gl.glRotated(xRotation, 1, 0, 0);
	}
	
	public void updateKeyInfo(){
		if(keys['z']) {
			xPosition += (float) Math.sin(-zRotation*(3.14)/180) * 50f; 
			zPosition += (float) Math.cos(-zRotation*(3.14/180)) * 50f;
		}else if(keys['s']){
			xPosition -= (float) Math.sin(-zRotation*(3.14/180)) * 50f;
			zPosition -= (float) Math.cos(-zRotation*(3.14)/180) * 50f;
		}else if(keys['q'])
			xPosition+=20;
		else if(keys['d'])
			xPosition-=20;
		else if(keys['g'])
			yPosition-=20;
		else if(keys['t'])
			yPosition+=20;
	}
	
	public void keyTyped(KeyEvent e) {		
	}
	
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyChar() < 255)
		{
			keys[e.getKeyChar()] = true;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyChar() < 255)
		{
			keys[e.getKeyChar()] = false;
		}
	}
	
	public void run(){
		while (true) {
			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateKeyInfo();
		}
	}
}
