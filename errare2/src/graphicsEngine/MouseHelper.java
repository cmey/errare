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

package graphicsEngine;

import userInputEngine.UserInputController;

/**
 * Handles every mouse user input that is related to the 3D graphics engine.
 * Implements scene rotation and zoom.
 * @author cyberchrist
 */
public class MouseHelper
{
	protected GraphicsEngine ge;		// the caller
	public int lastX, lastY;			// to handle drags
	private float XmouseSpeed = 5;
	private float YmouseSpeed = 5;
	private float mouseX, mouseY;		// intermediate mouse position calculation around the sphere
	private float rayon;				// rayon of the position of the camera around the sphere of view
	public float rayonMax=1000;			// maximum rayon of the sphere of view
	
	private boolean leftBorder=false;	// of the screen
	private boolean rightBorder=false;	// of the screen
	private int BorderTouchProportion = 10;// percentage of the screen covered by borders
	
	
	
	/**
	 * Construct new mousehelper.
	 * @param ge the caller GraphicsEngine
	 */
	public MouseHelper(GraphicsEngine ge) {
		this.ge=ge;
		this.registerActionsForController();
	}
	
	
	/**
	 * Registers in the UserInputController the actions that are handled here.
	 */
	protected void registerActionsForController(){
		/*UserInputController uic = ge.getMain().getUserInputControler();
		uic.register(ge, "rotate_view");
		uic.register(ge, "zoom_in");
		uic.register(ge, "zoom_out");
		uic.register(ge, "mouse_move"); // needed to always have the last position of the mouse in order to handle a drag without "jumps"
	*/}
	
	
	/**
	 * User input (mouse) arrival.
	 * @return false if we consummed the event
	 */
	public boolean invokeMouseEvent(String action, int x, int y) {
		if(action.equals("rotate_start")) {
			lastY=y;
			lastX=x;
		}
		else if(action.equals("rotate_view")){
			ge.world.cam.rotateXZ((y-lastY)*0.01f);

			lastY=y;

			ge.world.cam.rotateY((x-lastX)*0.01f);
			lastX=x;
			
			return true;
		}else if(action.equals("zoom_in")){
			if(ge.world.cam instanceof FollowCamera){
				((FollowCamera)ge.world.cam).zoomin();
			}
		}else if(action.equals("zoom_out")){
			if(ge.world.cam instanceof FollowCamera){
				((FollowCamera)ge.world.cam).zoomout();
			}
		}
		
		return true; // event not consumed
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
		if(percentage < 100 && percentage > 0) {
			BorderTouchProportion = percentage;
			return true;
		}
		return false;
	}

	
	/**
	 * Updates the values for the calculation of the camera view.
	 */
	private void updateBorders() {
		if(rightBorder)
			mouseX+=2*(XmouseSpeed)/360;
		else
			mouseX-=2*(XmouseSpeed)/360;
		
	}
	
	
	/**
	 * We receive a tick every 1/60 seconds.
	 */
	public void tick() {
		if(rightBorder || leftBorder){
			updateBorders();
		}
	}
	
}
