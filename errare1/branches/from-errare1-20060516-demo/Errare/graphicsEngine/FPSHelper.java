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

public class FPSHelper {

	GraphicsEngine ge;
	long last_tick_time;
	
	
	public FPSHelper(GraphicsEngine ge){
		this.ge=ge;
	}
	
	public void tick(){
		if(System.currentTimeMillis() - last_tick_time >= 1000){
			ge.actual_fps = ge.fpscount;
			ge.fpscount = 0;
			last_tick_time = System.currentTimeMillis();
		}
	}
}
