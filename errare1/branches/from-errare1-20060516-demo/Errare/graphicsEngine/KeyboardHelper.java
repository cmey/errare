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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

public class KeyboardHelper implements KeyListener{	
	
	private boolean[] keys = new boolean[256];
	private MouseHelper mh;
	public boolean wire_mode = false;
	public boolean display_tree = false;
	public boolean shadow_mode = false;
	public boolean draw_shadowmap = false;
	public boolean fullscreen_mode = false;
	public boolean glow_on = false;
	public boolean fog_on = false;
	public boolean far_view = true;
	public boolean reflection_on = false;
	public boolean create_blood_test = false;
	public boolean tremble = false;
	public boolean culling_on = true;
	public boolean draw_culling = false;
	public boolean inventory_on = false;
	public boolean characteristics_on = true;
	public boolean pause_on = false;
	public static boolean terrain_on = true;
	
	public KeyboardHelper (MouseHelper mh) {
		this.mh=mh;
	}
	
	public void updateKeyInfo(){
		if(keys[KeyEvent.VK_Q]) {
			mh.rayonMax -= mh.rayonMax/20;
			mh.update();
		}
		if(keys[KeyEvent.VK_W]) {
			mh.rayonMax += mh.rayonMax/20;
			mh.update();
		}
		if(keys[KeyEvent.VK_T]) {
			tremble=true;
			}else{
			tremble=false;
		}	
		if(keys[KeyEvent.VK_ESCAPE]){
			System.exit(0);
		}
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE && !keys[e.getKeyCode()]){
			culling_on = !culling_on;
		}
		else if(e.getKeyCode() == KeyEvent.VK_F2 && !keys[e.getKeyCode()]){
			wire_mode = !wire_mode;
		}
		else if(e.getKeyCode() == KeyEvent.VK_F3 && !keys[e.getKeyCode()]){
			terrain_on = !terrain_on;
		}
		else if(e.getKeyCode() == KeyEvent.VK_S && !keys[e.getKeyCode()]){
			shadow_mode = !shadow_mode;
		}
		else if(e.getKeyCode() == KeyEvent.VK_D && !keys[e.getKeyCode()]){
			draw_shadowmap = !draw_shadowmap;
		}
		else if(e.getKeyCode() == KeyEvent.VK_G && !keys[e.getKeyCode()]){
			glow_on = !glow_on;
		}
		else if(e.getKeyCode() == KeyEvent.VK_N && !keys[e.getKeyCode()]){
			fog_on = !fog_on;
		}
		else if(e.getKeyCode() == KeyEvent.VK_U && !keys[e.getKeyCode()]){
			display_tree = !display_tree;
		}
		else if(e.getKeyCode() == KeyEvent.VK_O && !keys[e.getKeyCode()]){
			draw_culling = !draw_culling;
		}
		else if(e.getKeyCode() == KeyEvent.VK_F && !keys[e.getKeyCode()]){
			far_view = !far_view;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R && !keys[e.getKeyCode()]){
			reflection_on = !reflection_on;
		}
		else if(e.getKeyCode() == KeyEvent.VK_B && !keys[e.getKeyCode()]){
			create_blood_test = !create_blood_test;
		}
		else if(e.getKeyCode() == KeyEvent.VK_F11 && !keys[e.getKeyCode()]){
			fullscreen_mode = !fullscreen_mode;
			if(fullscreen_mode)
				GraphicsEngine.goFullScreen();
			else
				GraphicsEngine.goWindowed();
		}
		else if(e.getKeyCode() == KeyEvent.VK_C && !keys[e.getKeyCode()]){
			if(Cinema.getCinemaState()==true)
				Cinema.cinemaOff();
			else
				Cinema.cinemaOn();
		}
		
		if(e.getKeyCode() < 255)
		{
			keys[e.getKeyCode()] = true;
		}
	}
	
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() < 255)
		{
			keys[e.getKeyCode()] = false;
		}
	}
	
	public void tick(){
			updateKeyInfo();
	}
}
