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

import java.io.Serializable;
import java.util.LinkedList;

import javax.media.opengl.GL;
import logger.Logger;


@SuppressWarnings("serial")
public class AnimatedMesh extends Mesh implements Serializable {
	

	protected MeshData[] keyframes;
	
	transient private int currentFrame = 0;
	
	// Start and end frames for the animation running
	transient private int startFrame = 0;
	transient private int endFrame = 0;
	transient private boolean loop = true;
	transient private String currentAnimName = DEFAULT_ANIM_NAME;
	transient private String previousAnimName = DEFAULT_ANIM_NAME;
	
	
	public static final String DEFAULT_ANIM_NAME = "stand";
	
	transient private int nextAnimFrame = 0; // temp
	
	// Animation timings
	transient protected long lastAnimTime = 0;
	
	// Animation FPS for number of interpolations. VARIES FROM MODEL TO MODEL
	protected int fps = 30;
	transient private float timedelta;
	transient private long lastTime;

	
	public AnimatedMesh(int[] topology, MeshData[] keyframes, float[][] texcoords){
		this.topology = topology;
		this.keyframes = keyframes;
		this.texcoords = texcoords;
		if(texcoords == null || texcoords[0]==null) { this.textured = false; }
	}
	
	
	
	/**
	 * Allows animation remote launching. The start frame will be the first frame of the animation
	 * with the same name. The last frame will be the first (minus 1) to not have the same name.
	 * @param name the name of the animation inside the keyframes structure (you can get a list of all possible 
	 * names with the method getAnimationsNames)
	 */
	public void startAnimation(String aname){
		boolean found = false;
		boolean found_end = false;
		int tempstartFrame = 0;
		int tempendFrame = 0;
		
		for(int i=0; i<keyframes.length;i++) {
			if(keyframes[i].name.startsWith(aname)){
				tempstartFrame=i;
				found=true;
				break;
			}
		}
		
		if(found)
			for(int i=tempstartFrame+1; i<keyframes.length;i++) {
				if(!keyframes[i].name.startsWith(aname)){
					tempendFrame=i-1;
					found_end=true;
					break;
				}
		}
		
		if(!found || !found_end){
			Logger.printERROR("animation: "+aname+" in "+path+" not found!");
			//System.exit(-1);
		}
		if(tempstartFrame==tempendFrame){
			Logger.printERROR("(warning) animation: "+aname+" in "+path+" contains only ONE frame: "+aname);
		}
		
		setFrameRange(tempstartFrame,tempendFrame);
	}
	
	
	

	/**
	 * Set animation FPS.
	 * @param fps FPS to play animation by
	 */
	public void setAnimationFPS(int fps)
	{        
		this.fps = fps;
		this.timedelta = 1f/fps;
	}
	
	
	/**
	 * Reset animation.
	 */
	public void resetAnim()
	{
		// Setup current and next anim frames
		currentFrame = startFrame;
		nextAnimFrame = startFrame+1;
		if(nextAnimFrame > endFrame) nextAnimFrame = endFrame;
		lastAnimTime = 0;
	}
	
	
	/**
	 * Set start and end frames.
	 * @param startFrame First animation frame
	 * @param endFrame Last animation frame
	 */
	private void setFrameRange(int startFrame, int endFrame)
	{
		this.startFrame = startFrame;
		this.endFrame = endFrame;
		
		// Limit for the dummies
		if (startFrame < 0) startFrame = 0;
		if (startFrame >= keyframes.length) startFrame = keyframes.length-1;
		if (endFrame < startFrame) endFrame = startFrame;
		if (endFrame >= keyframes.length) endFrame = keyframes.length-1;
		
		// Setup current and next anim frames
		currentFrame = startFrame;
	}
	
	
	
	
	/**
	 * Gives a list of all animations names you can further call with the method startAnimation.
	 * @return a list containing the names of every animation.
	 */
	public LinkedList<String> getAnimationsNames() {
		LinkedList<String> res = new LinkedList<String>();
		for(int i=0; i<keyframes.length;i++){
			res.add(keyframes[i].name);
		}
		return res;
	}
	
	
	
	public String getCurrentKeyframeName(){
		return keyframes[currentFrame].name;
	}
	
	
	@Override
	public float[] getGraphicalBoundaries() {
		return keyframes[currentFrame].getGraphicalBoundaries();
	}



	@Override
	protected MeshData getMeshData() {
		return keyframes[currentFrame];
	}
	
	public void advanceNextAnimationFrame(){
		if((1f*(System.currentTimeMillis() - lastTime)/1000) < timedelta){
			return;
		}else{
			lastTime = System.currentTimeMillis();
		}
		
		currentFrame++;
		
		if(currentFrame >= endFrame)
			if(loop==true){
				currentFrame = startFrame;
			}else{
				currentAnimName = previousAnimName;
				startAnimation(previousAnimName);
			}
	}
	
	public void playAnimationOnce(String animName){
		if(animName.equals(this.currentAnimName)){
			return;
		}else{
			if(this.loop){
				this.previousAnimName = currentAnimName;
			}
			this.currentAnimName = animName;
			startAnimation(animName);
		}
		this.loop = false;
	}
	
	public void playAnimationInLoop(String animName){
		if(animName.equals(this.currentAnimName)){
			return;
		}else{
			this.previousAnimName = currentAnimName;
			this.currentAnimName = animName;
			startAnimation(animName);
		}
		this.loop = true;
	}
	
	public void setKeyframes(MeshData[] keyframes){
		this.keyframes = keyframes;
	}
	
	public String getCurrentAnimation(){
		return currentAnimName;
	}


	@Override
	protected AnimatedMesh collectorClone() {
		AnimatedMesh a = new AnimatedMesh(this.topology, this.keyframes, this.texcoords);
		a.setFilename(this.path);
		a.setAnimationFPS(this.fps);
		a.startAnimation(DEFAULT_ANIM_NAME);
		return a;
	}
	
	protected void createVBOs(GL gl){
		if(!Extensions.isVBOSupported) return;
		for(MeshData md : keyframes){
			md.createVBOs(gl);
		}
		super.createVBOsSuper(gl);
	}
	
	protected void free(GL gl){
		for(MeshData md : keyframes){
			md.free(gl);
		}
		this.freeMeshSuper(gl);
	}
	
	protected void free(){
		for(MeshData md : keyframes){
			md.free();
		}
		this.freeMeshSuper();
	}

}