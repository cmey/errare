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

import main.ItemRep;
import physicsEngine.CharacterPRep;


/**
 * Class GuiInventory extends JInternalFrame
 * Inventory organisation
 */


@SuppressWarnings("serial")

public class GuiInventory {
	
	
	private GuiEngine engine;
	
	private CharacterPRep charPRep;
	
	// The Inventory
	private ItemRep [] [] tabInventory;
	
	// Items on character
	// helm,amulet,weapon,armor,shield,gloves,ring,belt,ring,boot
	private ItemRep [] tabOnCharacter;
	
	
	// Era (gold)
	private int gold;
	
	/**
	 * GuiInventory Constructor
	 * Create a JInternalFrame that popup on the screen whitch the user can
	 * organise his character
	 */
	
	public GuiInventory(GuiEngine engine) {
	
	this.engine=engine;
		tabInventory = new ItemRep [5][12];
		tabOnCharacter = new ItemRep[10];
		gold=0;
		
	}


	/**
	 * getInventory Fonction
	 * @return tabInventory the Inventory whith ItemRep representation
	 */
	
	public ItemRep [][] getItemInventory() {
		return tabInventory;
	}
	
	/**
	 * getItemOnCharacter Fonction
	 * @return tabOnCharacter the equipments of the character with ItemRep representation
	 */
	
	public ItemRep[] getItemOnCharacter() {
		return tabOnCharacter;
	}
	

	
	/**
	 * getWeapons Fonction
	 * @return tabWeapons the first and the second weapons used
	 */
	
	
	/**
	 * getInventory Fonction
	 * @return tabInventory the Inventory whith GuiRep representation
	 */
	
	public GuiRep [][] getGuiInventory() {
		ItemRep[][] tabItem  = tabInventory.clone();
		GuiRep[][] tab = new GuiRep[tabItem.length][tabItem[0].length];
		for (int i=0;i<tabItem.length;i++) {
			for (int j=0;j<tabItem[0].length;j++) {
				if (tabItem[i][j]!=null) {
					assert tabItem[i][j]!=null : "Inventory null";
					tab[i][j]=tabItem[i][j].getGui();
				}
			}
		}
		return tab;
	}
	
	
	/**
	 * getGuiOnCharacter Fonction
	 * @return tabOnCharacter the equipments of the character with GuiRep representation
	 */
	
	public GuiRep [] getGuiOnCharacter() {
		ItemRep[] tabItem  = tabOnCharacter.clone();
		GuiRep[] tab = new GuiRep[tabItem.length];
		for (int i=0;i<tabItem.length;i++) {
			if (tabItem[i]!=null) {
				assert tabItem[i]!=null : "Setting null";
				tab[i]=tabItem[i].getGui();
			}
		}
		return tab;
	}
	
	
	
	/**
	 * pick Fonction
	 * @param it The GuiRep of the item whitch you want to add to the Inventory
	 * @return true if the item was add to the Inventory, else false
	 */
	
	public boolean pick (ItemRep it) {
		System.out.println("PICK !");
		boolean add=false;
		boolean onCharacter=false;
		boolean inBelt=false;
		
		int nb=0;
		int h=it.getGui().getHeightCase();
		int w=it.getGui().getWidthCase();
		// GERER SI LE PERSO A LE DROIT DE PORTER DIRECT L'OBJET (if...)	
		
		int t = it.getGui().getType();
		
		switch (t) {
		
		case 1:case 2:case 3:
			ItemRep [] tabBelt = engine.getBelt().getItemBelt();
			for (int i=0;i<tabBelt.length;i++) {
				if (tabBelt[i]==null) {
					System.out.println("i: "+i);
				tabBelt[i]=it;
				inBelt=true;
				add=true;
				break;
			}
			}
			break;
		case 13:case 14:case 15: 
			if(tabOnCharacter[0]==null) {
				tabOnCharacter[0]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 4:case 5:case 6:
			if(tabOnCharacter[1]==null) {
				tabOnCharacter[1]=it;
				onCharacter=true;
				add=true;
			}
	break;
		case 29:case 30:case 31:case 32:case 33:case 34:case 35:case 36:
			if(tabOnCharacter[2]==null) {
				tabOnCharacter[2]=it;
				onCharacter=true;
				add=true;
				int [] tabWeaponsStates = engine.getBelt().getWeaponsStatesTab();
				if (t==29)tabWeaponsStates[0]=1;
				else if (t==30)tabWeaponsStates[0]=2;
				else if (t==31)tabWeaponsStates[0]=5;
				else if (t==32)tabWeaponsStates[0]=6;
				else if (t==33)tabWeaponsStates[0]=3;
				else if (t==34)tabWeaponsStates[0]=4;
				else if (t==35)tabWeaponsStates[0]=7;
				else if (t==36)tabWeaponsStates[0]=8;
			}
			break;
		case 22:case 23:case 24: case 25:
			if(tabOnCharacter[3]==null) {
				tabOnCharacter[3]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 26:case 27:case 28:
			if(tabOnCharacter[4]==null) {
				tabOnCharacter[4]=it;
				onCharacter=true;
				add=true;
			}	
			break;
		case 19:case 20:case 21:
			if(tabOnCharacter[5]==null) {
				tabOnCharacter[5]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 7:case 8:case 9:
			if (tabOnCharacter[6]==null) {
				tabOnCharacter[6]=it;
				onCharacter=true;
			}else if (tabOnCharacter[8]==null) {
				tabOnCharacter[8]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 10:case 11:case 12:
			if(tabOnCharacter[7]==null) {
				tabOnCharacter[7]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 16:case 17:case 18:
			if(tabOnCharacter[9]==null) {
				tabOnCharacter[9]=it;
				onCharacter=true;
				add=true;
			}
			break;
		case 37:
			gold+=it.getCharacteristics().getGold();
			onCharacter=true;
			add=true;
			break;
			
		default: break;
		}

		if ((!onCharacter)&&(!inBelt)) {	
			for (int i=0;i<=tabInventory.length-h;i++) {
				for (int j=0;j<=tabInventory[0].length-w;j++) {
					if (tabInventory[i][j] == null) {
						for (int k=0;k<h;k++) {
							for (int l=0;l<w;l++) {
								if ((tabInventory[i+k][j+l]==null)) {
									nb++;
								}
							}
						}
						if (nb==((h)*(w))) {
							for (int m=0;(m<h);m++) {
								for (int n=0;(n<w);n++) {
									tabInventory[i+m][j+n] = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),0,null));
								}
							}
							tabInventory[i][j] = it;
							i=tabInventory.length;
							j=tabInventory[0].length;
							add=true;
						}else {
							nb=0;
						}
					}
				}
			}
		}
		return add;
	}
	
	/**
	 * dropFonction
	 * @param i the first index in the tab
	 * @param j the second index in the tab
	 */
	
	public void drop (int i, int j) {
	//charPRep.drop(tabInventory[i][j].getPhysics()); // BUG ICI
	 int h = tabInventory[i][j].getGui().getHeightCase();
	 int w = tabInventory[i][j].getGui().getWidthCase();
	 for (int k=0;k<h;k++) {
		 for (int l=0;l<w;l++) {
			 tabInventory[k+i][l+j]=null;		
		 }
	 }
	}
	
	/**
	 * dropFonction
	 * @param itemSelect the item to drop
	 */
	
	public void drop(ItemRep itemSelect) {
		charPRep.drop(itemSelect.getPhysics());	
	}
	
	/**
	 * inventoryFull Fonction
	 * @return boolean true if the Inventory is full else false
	 */
	
	public boolean inventoryFull() {
		
		boolean full=true;
		
		for (int i=0;i<tabInventory.length;i++) {
			for (int j=0;j<tabInventory[0].length;j++) {
				if (tabInventory[i][j] == null) {
					full=false;
				}
			}
		}	
		return full;
	}
	
	/**
	 * SettingFull Fonction
	 * @return boolean true if the setting of the chractere is full else false
	 */
	
	public boolean settingFull() {
		
		boolean full=true;
		
		for (int i=0;i<tabOnCharacter.length;i++) {
				if (tabOnCharacter[i]== null) {
					full=false;
				}
		}	
		return full;
	}
	
	/**
	 * listInventory
	 * Write in the console the Inventory content
	 */
	
	public void listInventory() {
		
		for(int i=0; i<tabInventory.length; i++) { 
			System.out.println();
			for(int j=0; j<tabInventory[0].length; j++) {
				if(tabInventory[i][j] != null) {
					String type =""+tabInventory[i][j].getGui().getType();
					if (tabInventory[i][j].getGui().getType()<=9) {
						type+="0";
					}
					System.out.print(type+" ");
				}else{
					System.out.print("99 ");
				}
			}
		}
		System.out.println("");
	}
	
	public void setCharPRep(CharacterPRep rep) {
		charPRep = rep;
	}
	
	public CharacterPRep getCharPRep() {
		return charPRep;
	}


	/**
	 * test Fonction
	 * Add item on the character and in the Inventory while there are not full
	 */
	public void testpick() {
		
		ItemRep ap = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),1,"allPotion.png"));
		ItemRep hp = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),2,"healthPotion.png"));
		ItemRep mp = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),3,"manaPotion.png"));
		ItemRep amu1 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),4,"amulet1.png"));
		ItemRep amu2 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),5,"amulet2.png"));
		ItemRep amu3 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),6,"amulet3.png"));
		ItemRep rin1 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),7,"ring1.png"));
		ItemRep rin2 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),8,"ring2.png"));
		ItemRep rin3 = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,1),9,"ring3.png"));
		ItemRep sbel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,1),10,"smallBelt.png"));
		ItemRep mbel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,1),11,"mediumBelt.png"));
		ItemRep lbel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,1),12,"largeBelt.png"));
		ItemRep shel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),13,"smallHelm.png"));
		ItemRep mhel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),14,"mediumHelm.png"));
		ItemRep lhel = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),15,"largeHelm.png"));
		ItemRep sboo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),16,"smallBoot.png"));
		ItemRep mboo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),17,"mediumBoot.png"));
		ItemRep lboo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),18,"largeBoot.png"));
		ItemRep sglo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),19,"smallGlove.png"));
		ItemRep mglo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),20,"mediumGlove.png"));
		ItemRep lglo = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),21,"largeGlove.png"));
		ItemRep sarm = new ItemRep(null,null,null,new GuiRep(new Rectangle(3,3),22,"smallArmor.png"));
		ItemRep marm = new ItemRep(null,null,null,new GuiRep(new Rectangle(3,3),23,"mediumArmor.png"));
		ItemRep larm = new ItemRep(null,null,null,new GuiRep(new Rectangle(3,4),24,"largeArmor.png"));
		ItemRep dres = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,4),25,"dress.png"));
		ItemRep sshi = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,3),26,"smallShield.png"));
		ItemRep mshi = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,3),27,"mediumShield.png"));
		ItemRep lshi = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,4),28,"largeShield.png"));	
		ItemRep ssti = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,3),29,"smallStick.png"));
		ItemRep lsti = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,3),30,"largeStick.png"));
		ItemRep saxe = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,3),31,"smallAxe.png"));
		ItemRep laxe = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,3),32,"largeAxe.png"));
		ItemRep sswo = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,3),33,"smallSword.png"));
		ItemRep lswo = new ItemRep(null,null,null,new GuiRep(new Rectangle(1,4),34,"largeSword.png"));
		ItemRep sarc = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,2),35,"smallArc.png"));
		ItemRep larc = new ItemRep(null,null,null,new GuiRep(new Rectangle(2,3),36,"largeArc.png"));
		
		while ((!this.inventoryFull())||(!this.settingFull())) {
			int hasard = (int)(Math.random()*180);
			if (hasard<=5) this.pick(ap);
			if ((hasard>5)&&(hasard<=10)) this.pick(hp);
			if ((hasard>10)&&(hasard<=15)) this.pick(mp);
			if ((hasard>15)&&(hasard<=20)) this.pick(sarc);
			if ((hasard>20)&&(hasard<=25)) this.pick(larc);
			if ((hasard>25)&&(hasard<=30)) this.pick(ssti);
			if ((hasard>30)&&(hasard<=35)) this.pick(lsti);
			if ((hasard>35)&&(hasard<=40)) this.pick(saxe);
			if ((hasard>40)&&(hasard<=45)) this.pick(laxe);
			if ((hasard>45)&&(hasard<=50)) this.pick(sswo);
			if ((hasard>50)&&(hasard<=55)) this.pick(lswo);
			if ((hasard>55)&&(hasard<=60)) this.pick(dres);
			if ((hasard>60)&&(hasard<=65)) this.pick(sarm);
			if ((hasard>65)&&(hasard<=70)) this.pick(marm);
			if ((hasard>70)&&(hasard<=75)) this.pick(larm);
			if ((hasard>75)&&(hasard<=80)) this.pick(sglo);
			if ((hasard>80)&&(hasard<=85)) this.pick(mglo);
			if ((hasard>85)&&(hasard<=90)) this.pick(lglo);
			if ((hasard>90)&&(hasard<=95)) this.pick(sboo);
			if ((hasard>95)&&(hasard<=100)) this.pick(mboo);
			if ((hasard>100)&&(hasard<=105)) this.pick(lboo);
			if ((hasard>105)&&(hasard<=110)) this.pick(sbel);
			if ((hasard>110)&&(hasard<=115)) this.pick(mbel);
			if ((hasard>115)&&(hasard<=120)) this.pick(lbel);
			if ((hasard>120)&&(hasard<=125)) this.pick(shel);
			if ((hasard>125)&&(hasard<=130)) this.pick(mhel);
			if ((hasard>130)&&(hasard<=135)) this.pick(lhel);
			if ((hasard>135)&&(hasard<=140)) this.pick(rin1);
			if ((hasard>140)&&(hasard<=145)) this.pick(rin2);
			if ((hasard>145)&&(hasard<=150)) this.pick(rin3);
			if ((hasard>150)&&(hasard<=155)) this.pick(amu1);
			if ((hasard>155)&&(hasard<=160)) this.pick(amu2);
			if ((hasard>160)&&(hasard<=165)) this.pick(amu3);
			if ((hasard>165)&&(hasard<=170)) this.pick(sshi);
			if ((hasard>170)&&(hasard<=175)) this.pick(mshi);
			if ((hasard>175)&&(hasard<=180)) this.pick(lshi);		
		}
	}

	public void update(ItemRep[][] inveTab) {
		
		tabInventory = inveTab;
		
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public ItemRep[][] getTabInventory() {
		return tabInventory;
	}

	public void setTabInventory(ItemRep[][] tabInventory) {
		this.tabInventory = tabInventory;
	}

	public ItemRep[] getTabOnCharacter() {
		return tabOnCharacter;
	}

	public void setTabOnCharacter(ItemRep[] tabOnCharacter) {
		this.tabOnCharacter = tabOnCharacter;
	}

	public void updateCharacter(ItemRep[] characterTab) {
		this.tabOnCharacter=characterTab;	
	}

}
