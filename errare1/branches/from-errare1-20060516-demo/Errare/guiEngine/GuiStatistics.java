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


/**
 * Class GuiStatistics
 * Use to determinate the pourcent of life, mana ... of the hero to diplay it
 */

public class GuiStatistics {
	
	private double level,_Pourcentlife,_Pourcentmana; // EN %
	
	public GuiStatistics (GuiEngine engine) {
		
		this.level = 0.0;
		this._Pourcentlife = 100;
		this._Pourcentmana = 100;
		
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}

	public double getPourcentLife() {
		return _Pourcentlife;
	}

	public void setPourcentLife(double l) {
		this._Pourcentlife = l*100;
	}
	
	public double getPourcentMana() {
		return _Pourcentmana;
	}

	public void setPourcentMana(double m) {
		this._Pourcentmana = m*100;
	}

}
