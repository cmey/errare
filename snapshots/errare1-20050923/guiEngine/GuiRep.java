package guiEngine;

import java.awt.Rectangle;

/**
 * Class GuiRep
 * @author Ak
 * @version 0.1
 */

public class GuiRep {
	
	// The place of the item in the inventory
	protected Rectangle rectangle;
	
	/* ***************************************************************
	 * The type of the item (Armor,sword,potion...)                  *
	 * ***************************************************************
	 * 1 : allPotion         13 : smallHelm        25 : dress        *
	 * 2 : healthPotion      14 : mediumHelm       26 : smallShield  *
	 * 3 : manaPotion        15 : largeHelm        27 : mediumShield *
	 * 4 : amulet1           16 : smallBoot        28 : largeShield  *
	 * 5 : amulet2           17 : mediumBoot       29 : smallStick   *
	 * 6 : amulet3           18 : largeBoot        30 : largeStick   *
	 * 7 : ring1             19 : smallGlove       31 : smallAxe     *
	 * 8 : ring2             20 : mediumGlove      32 : largeAxe     *
	 * 9 : ring3             21 : largeGlove       33 : smallSword   *
	 * 10 : smallBelt        22 : smallAmor        34 : largeSword   *
	 * 11 : mediumBelt       23 : mediumArmor      35 : smallArc     *
	 * 12 : largeBelt        24 : largeArmor       36 : largeArc     *
	 * ***************************************************************/
	private int type;
	
	// The item picture name
	private String pictureName;
	
	/**
	 * GuiRep Constructor
	 * Create instance of guiRepElement
	 * @param rectangle The place of the item in the inventory (case)
	 * @param type The type of the item
	 * @param pictureName The item picture name
	 * 
	 */
	
	public GuiRep(Rectangle rectangle, int type, String pictureName) {
		
		this.rectangle = rectangle;
		this.rectangle.width *=20;
		this.rectangle.height *=20;
		this.type=type;
		this.pictureName=pictureName;
	}
	
	
	/**
	 * getWidth fonction
	 * @return The width of the item (pixel) (int)
	 */
	
	public int getWidthPixel() {
		return this.rectangle.width;
	}
	
	/**
	 * getHeight fonction
	 * @return The height of the item (pixel) (int)
	 */
	
	public int getHeightPixel() {
		return this.rectangle.height;
	}
	
	/**
	 * getWidth fonction
	 * @return The width of the item (case) (int)
	 */
	
	public int getWidthCase() {
		return this.rectangle.width/20;
	}
	
	/**
	 * getHeight fonction
	 * @return The height of the item (case) (int)
	 */
	
	public int getHeightCase() {
		return this.rectangle.height/20;
	}
	
	/**
	 * getType fonction
	 * @return The type of the item (int)
	 */
	
	public int getType() {
		return this.type;
	}
	
	/**
	 * getPictureName fonction
	 * @return The item picture name (String)
	 */
	
	public String getPictureName() {
		return this.pictureName;
	}
	
}
