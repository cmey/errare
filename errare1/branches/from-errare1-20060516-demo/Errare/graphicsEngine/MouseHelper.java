/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package graphicsEngine;/**

 * Implements simple mouse rotation/zoom.
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;

public class MouseHelper implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private GraphicsEngine ge;
	private GL gl;
	
	private boolean left_click_done;
	private boolean right_click_done;
	private MouseEvent eleft, eright;
	
	private float XmouseSpeed = 5;
	private float YmouseSpeed = 5;
	private float mouseX, mouseY, lastX, lastY;
	
   /**
    * real actual position of the cursor on-screen,
    * used by the Robot in GraphicsEngine.
    */
	public int cursorX,cursorY;
	
	private float rayon;
	public float rayonMax=1000;
	
	private boolean bord_ecran_gauche=false;
	private boolean bord_ecran_droit=false;
	private int BorderTouchProportion = 10;
	private int button;
	
	
	
	/**
	 * Construct new mousehelper.
	 */
	public MouseHelper(GraphicsEngine ge)
	{
		this.ge=ge;
		mouseY=0.01f;
	}
	
	
	public void setGLGLU(GL gl){
		this.gl=gl;
	}
	
	public void mouseClicked(MouseEvent e) {
		if(!e.isConsumed()){
			if(toButton(e)==MouseEvent.BUTTON1){
				left_click_done=true;
				eleft=e;
			}
			if(toButton(e)==MouseEvent.BUTTON3){
				GraphicsEngine.cast_spell();
				right_click_done=true;
				eright=e;
			}
		}
	}
	
	
	public void mouseEntered(MouseEvent e) {
		cursorX = e.getX();
		cursorY = e.getY();
	}
	
	public void mouseExited(MouseEvent e) {}
	
	
	public void mousePressed(MouseEvent e) {
		if(!e.isConsumed()){
			lastX=e.getX();
			lastY=e.getY();
			button=toButton(e);
			System.out.println("mod "+toButton(e));
		}
	}
	
	
	private int toButton(MouseEvent me){
		int mod = me.getModifiers();
		String t = MouseEvent.getMouseModifiersText(mod);
		t=t.substring(t.length()-1);
		return Integer.parseInt(t);
	}
	
	
	public void mouseReleased(MouseEvent e) {
		button=MouseEvent.NOBUTTON;
	}
	
	
//	MouseMotionListener 
	public void mouseDragged(MouseEvent e) 
	{
		if(!e.isConsumed()){
			
			if(toButton(e)==MouseEvent.BUTTON3){
				mouseX+=(((float)e.getX()-lastX)*XmouseSpeed)/360;
				mouseY+=(((float)e.getY()-lastY)*YmouseSpeed)/360;
				
				if(mouseY >= Math.PI/2) mouseY=(float) (Math.PI/2)-0.01f;
				if(mouseY <= 0) mouseY=0.01f;
				
				update();
				
				lastX=e.getX();
				lastY=e.getY();
			}else if(toButton(e)==MouseEvent.BUTTON1){
				left_click_done=true;
				eleft=e;
			}
			
			cursorX = e.getX();
			cursorY = e.getY();
		}
	}
	
	/**
	 * Sets the mouse sensibility to the desired values
	 * @param xms value for the sensibility of the mouse on the x axis
	 * @param yms value for the sensibility of the mouse on the y axis
	 * @return
	 */
	public boolean setMouseSpeed(float xms, float yms) {
		this.XmouseSpeed=xms;
		this.YmouseSpeed=yms;
		return true;
	}
	
	/**
	 * Sets the proportion of the screen that will be 'active' to the mouse approaching the border of the screen.
	 * When the mouse hits the right border of the screen, the camera will rotate right.
	 * When the mouse hits the left border of the screen, the camera will rotate left.
	 * @param percentage how much of the screen should be active. This is a percentage from the total screen width!
	 * @return
	 */
	public boolean setBorderTouchProportion( int percentage) {
		int h = ge.getGLCanvas().getHeight();
		int w = ge.getGLCanvas().getWidth();
		//int h = ge.getGLJPanel().getHeight();
		//int w = ge.getGLJPanel().getWidth();
		
		if(percentage < 100 && percentage > 0) {
			BorderTouchProportion = percentage;
			return true;
		}
		return false;
	}
	
	public void update() {
		if(rayonMax < 1) rayonMax =1;
		rayon= (float) (Math.cos(mouseY) * rayonMax);
		ge.camY= (float) (Math.sin(mouseY) * rayonMax);
		
		ge.camX= (float) (Math.sin(mouseX) * rayon);
		ge.camZ= (float) (Math.cos(mouseX) * rayon);
		GraphicsEngine.frustum_just_changed=true;
		//System.out.println("camX=" + ge.camX + " camY=" + ge.camY + " camZ=" + ge.camZ);		
	}
	
	public void mouseMoved(MouseEvent e) {
		int h = GraphicsEngine.window_height;
		int w = GraphicsEngine.window_width;
		
		if(e.getX() > w-w/BorderTouchProportion) {
			bord_ecran_droit=true;
			bord_ecran_gauche=false;
			ge.setRightBorderAction();
		}
		else if(e.getX() < w/BorderTouchProportion) {
			bord_ecran_gauche=true;
			bord_ecran_droit=false;
			ge.setLeftBorderAction();
		}
		else {
			bord_ecran_gauche=false;
			bord_ecran_droit=false;
			ge.setStopBorderAction();
		}
		
		cursorX = e.getX();
		cursorY = e.getY();
	}	
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		rayonMax += e.getWheelRotation() * rayonMax/10;
		update();
	}
	
	private void updateBords() {
		if(bord_ecran_droit)
			mouseX+=2*(XmouseSpeed)/360;
		else
			mouseX-=2*(XmouseSpeed)/360;
		update();
	}
	
	public void tick() {
		if(bord_ecran_droit || bord_ecran_gauche)
			updateBords();
		if(left_click_done){
			ge.click(eleft);
			left_click_done=false;
		}
		if(right_click_done){
			ge.click_spell(eright);
			right_click_done=false;
		}
		//if(toButton(e)==MouseEvent.toButton(e)1){
		//	left_click_done=true;
		//}
	}
	
}