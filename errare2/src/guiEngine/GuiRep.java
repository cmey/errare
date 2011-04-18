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

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Hashtable;

import javax.swing.ImageIcon;

/**
 * Class GuiRep
 * The Gui representation of the items
 */

public class GuiRep {

	
	/* ***************************************************************
	 * The category of the item                                      *
	 * ***************************************************************
	 * 01 : potion          07 : wrist            13 : boot          *
	 * 02 : amulet          08 : armor            14 : shield        *
	 * 03 : bracelet        09 : glove            15 : sword         *
	 * 04 : ring            10 : belt             16 : axe           *
	 * 05 : buckle          11 : trousers         17 : stick         *
	 * 06 : helm            12 : tibia            18 : arc   		 *
	 * 																 *
	 * 50 : ThunderSpell	51 : IceSpell	52 : FireSpell
	 * 53 : EarthSpell		54 : WindSpell													 *
	 * ***************************************************************
	 * The type of the item                                          *
	 * ***************************************************************
	 * 01 : smallTraditional      04 : smallMagic      07 : quest    *
	 * 02 : mediumTraditional     05 : mediumMagic     08 : rare     *
	 * 03 : largeTraditional      06 : largeMagic      09 : single   * 
	 * 	
	 * 
	 * 															 *
	 * 01 Chaine d'eclairs
	 * 02 Eclair
	 * 03 Foudroyer
	 * 
	 * 01 Nova de glace
	 * 02 Trait/epieu de glace
	 * 03 Blizzard
	 * 04 prison/serment de glace 
	 * 
	 * 01 Boule de feu
	 * 02 Mur de feu 
	 * 03 Colonne de feu
	 * 04 Cone de feu 
	 * 
	 * 01 Bouclier
	 * 02 Restauration
	 * 03 Mur de terre
	 * 04 Sables mouvants
	 * 05 Tremblement de terre
	 * 
	 * 01 Dissipation 
	 * 02 Absorption/resistance
	 * 03 Heal
	 * 04 Resurection
	 * 05 Augmentation
	 * 06 Reduction de vitesse
	 * ***************************************************************/

	
	/* 162 (18*9) Gui Representation
	 * GuiRep : Category+Type
	 * Ex : A large traditional helm, Category=06 and Type=03 -> 0603
	 */
    
	private int category;
	private int type;
	
	// The item picture name
	private String pictureName;
	
	/**
	 * GuiRep Constructor
	 * Create instance of guiRepElement
	 * @param type The type of the item
	 * @param pictureName The item picture name
	 */
	
	public GuiRep(int category, int type) {

		this.category=category;
		this.type=type;
	}
	
	/**
	 * getCategory fonction
	 * @return The category of the item (int)
	 */
	
	public int getCategory() {
		return category;
	}
	
	/**
	 * getType fonction
	 * @return The type of the item (int)
	 */
	
	public int getType() {
		return type;
	}
	
	/**
	 * getPictureName fonction
	 * @return The item picture name (String)
	 */
	
	public String getPictureName() {
		String pictureName="";
		if (category<10) pictureName+="0";
		pictureName+=category;
		if (type<10) pictureName+="0";
		pictureName+=type;
		return pictureName;
	}
	
	/**
	 * getPicturePath fonction
	 * @return The path of the item picture (String)
	 */
	public String getPicturePath() {
		return "data/images/"+this.pictureName;
	}
	
	/**
	 * getImage fonction
	 * @return The item image
	 */
	
	public Image getImage() {
		return (new ImageIcon("data/images/"+this.pictureName)).getImage();
	}	

}
