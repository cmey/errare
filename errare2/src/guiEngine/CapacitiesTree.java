/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Arnaud KNOBLOCH

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package guiEngine;

import java.awt.Rectangle;
import java.util.ArrayList;

public class CapacitiesTree {
	

	
	private Rectangle capacitiesTreeSite;
	private Rectangle spell01Site;
	

	private boolean visible;
	 
	private GuiEngine engine;
	
	public CapacitiesTree(GuiEngine engine) {
		
		this.engine = engine;	
		spell01Site = new Rectangle(0,engine.getScreenHeight(),1024,256);
		
	}
	
	
}