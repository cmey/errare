package guiEngine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Class GuiInventory extends JInternalFrame
 * @author Ak
 * @version 0.1
 */

@SuppressWarnings("serial")

public class GuiInventory extends JInternalFrame {
	
	// The Inventory
	public static GuiRep [] [] tabCharacterItems;
	
	/**
	 * GuiInventory Constructor
	 * Create a JInternalFrame that popup on the screen whitch the user can
	 * organise his character
	 */
	
	public GuiInventory() {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimScreen = tk.getScreenSize();
  
		this.setTitle("Errare");
		this.setLocation(1024-256,0+101);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.getContentPane().add(new GuiInventoryPanel(256,384,this));
		this.pack();
		tabCharacterItems = new GuiRep [5][12];
		try {
		    /*Look and feel Windows : "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
			 Look and feel CDE/Motif : "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
			 Look and feel GTK+ : "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
			 Look and feel metal : "javax.swing.plaf.metal.MetalLookAndFeel");
			 Look and feel Macintosh : "it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel"
			 look and feel Kunststoff : "com.incors.plaf.kunststoff.KunststoffLookAndFeel ")*/
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		this.setVisible(false);

	}
	
	/**
	 * getInventory Fonction
	 * @return tabCharacterItems the inventory
	 */
	
	public static GuiRep [][] getInventory() {
		return tabCharacterItems;
	}
	
	/**
	 * pic Fonction
	 * @param it The GuiRep of the item whitch you want to add to the inventory
	 */
	
	public void pic (GuiRep it) {
		boolean add=false;
		int nb=0;
		
		for (int i=0;i<tabCharacterItems.length;i++) {
			for (int j=0;j<tabCharacterItems[0].length;j++) {
				if (tabCharacterItems[i][j] == null) {
					int h=it.getHeightCase();
					int w=it.getWidthCase();
					for (int k=i;k<h+i;k++) {
						for (int l=j;l<w+j;l++) {
							try {
								add = (tabCharacterItems[k][l]==null);
							}
							catch (ArrayIndexOutOfBoundsException ex) {
								i=99;
								j=99;
							}
							if (add) nb++;
						}
					}
					if (nb==((it.getHeightCase())*(it.getWidthCase()))) {
						for (int m=i;(m<it.getHeightCase()+i) && (m<tabCharacterItems.length);m++) {
							for (int n=j;(n<it.getWidthCase()+j) && (n<tabCharacterItems[0].length);n++) {
								tabCharacterItems[m][n] = new GuiRep(new Rectangle(1,1),0,null);;;
							}
						}
						tabCharacterItems[i][j] = it;
						nb=0;
						i=99;
						j=99;
					}
				}
			}
		}
		
	}
	
	/**
	 * inventoryFull Fonction
	 * @return boolean true if the invetory is full else false
	 */
	
	public boolean inventoryFull() {
		
		boolean full=true;
		
		for (int i=0;i<tabCharacterItems.length;i++) {
			for (int j=0;j<tabCharacterItems[0].length;j++) {
				if (tabCharacterItems[i][j] == null) {
					full=false;
				}
			}
		}	
		return full;
	}
	
	/**
	 * listInventory
	 * Write in the console the inventory content
	 */
	
	public void listInventory() {
		
		for(int i=0; i<tabCharacterItems.length; i++) { 
			System.out.println();
			for(int j=0; j<tabCharacterItems[0].length; j++) {
				if(tabCharacterItems[i][j] != null)
					System.out.print(tabCharacterItems[i][j].getPictureName()+" ");
				else
					System.out.print("00000 ");
			}
		}
	}
	
	/**
	 * test Fonction
	 * Add item in the inventory while it's not full
	 */
	public void testPic() {
		
		GuiRep ap = new GuiRep(new Rectangle(1,1),1,"allPotion.png");
		GuiRep hp = new GuiRep(new Rectangle(1,1),2,"healthPotion.png");
		GuiRep mp = new GuiRep(new Rectangle(1,1),3,"manaPotion.png");
		GuiRep amu1 = new GuiRep(new Rectangle(1,1),4,"amulet1.png");
		GuiRep amu2 = new GuiRep(new Rectangle(1,1),5,"amulet2.png");
		GuiRep amu3 = new GuiRep(new Rectangle(1,1),6,"amulet3.png");
		GuiRep rin1 = new GuiRep(new Rectangle(1,1),7,"ring1.png");
		GuiRep rin2 = new GuiRep(new Rectangle(1,1),8,"ringt2.png");
		GuiRep rin3 = new GuiRep(new Rectangle(1,1),9,"ring3.png");
		GuiRep sbel = new GuiRep(new Rectangle(2,1),10,"smallBelt.png");
		GuiRep mbel = new GuiRep(new Rectangle(2,1),11,"mediumBelt.png");
		GuiRep lbel = new GuiRep(new Rectangle(2,1),12,"largeBelt.png");
		GuiRep shel = new GuiRep(new Rectangle(2,2),13,"smallHelm.png");
		GuiRep mhel = new GuiRep(new Rectangle(2,2),14,"mediumHelm.png");
		GuiRep lhel = new GuiRep(new Rectangle(2,2),15,"largeHelm.png");
		GuiRep sboo = new GuiRep(new Rectangle(2,2),16,"smallBoot.png");
		GuiRep mboo = new GuiRep(new Rectangle(2,2),17,"mediumBoot.png");
		GuiRep lboo = new GuiRep(new Rectangle(2,2),18,"largeBoot.png");
		GuiRep sglo = new GuiRep(new Rectangle(2,2),19,"smallGlove.png");
		GuiRep mglo = new GuiRep(new Rectangle(2,2),20,"mediumGlove.png");
		GuiRep lglo = new GuiRep(new Rectangle(2,2),21,"largeGlove.png");
		GuiRep sarm = new GuiRep(new Rectangle(3,3),22,"smallAmor.png");
		GuiRep marm = new GuiRep(new Rectangle(3,3),23,"mediumArmor.png");
		GuiRep larm = new GuiRep(new Rectangle(3,4),24,"largeArmor.png");
		GuiRep dres = new GuiRep(new Rectangle(2,4),25,"dress.png");
		GuiRep sshi = new GuiRep(new Rectangle(2,3),26,"smallShield.png");
		GuiRep mshi = new GuiRep(new Rectangle(2,3),27,"mediumShield.png");
		GuiRep lshi = new GuiRep(new Rectangle(2,4),28,"largeShield.png");	
		GuiRep ssti = new GuiRep(new Rectangle(1,3),29,"smallStick.png");
		GuiRep lsti = new GuiRep(new Rectangle(2,3),30,"largeStick.png");
		GuiRep saxe = new GuiRep(new Rectangle(1,3),31,"smallAxe.png");
		GuiRep laxe = new GuiRep(new Rectangle(2,3),32,"largeAxe.png");
		GuiRep sswo = new GuiRep(new Rectangle(1,3),33,"smallSword.png");
		GuiRep lswo = new GuiRep(new Rectangle(1,4),34,"largeSword.png");
		GuiRep sarc = new GuiRep(new Rectangle(2,2),35,"smallArc.png");
		GuiRep larc = new GuiRep(new Rectangle(2,3),36,"largeArc.png");
		
		while (!this.inventoryFull()) {
			int hasard = (int)(Math.random()*180);
			if (hasard<=5) this.pic(ap);
			if ((hasard>5)&&(hasard<=10)) this.pic(hp);
			if ((hasard>10)&&(hasard<=15)) this.pic(mp);
			if ((hasard>15)&&(hasard<=20)) this.pic(sarc);
			if ((hasard>20)&&(hasard<=25)) this.pic(larc);
			if ((hasard>25)&&(hasard<=30)) this.pic(ssti);
			if ((hasard>30)&&(hasard<=35)) this.pic(lsti);
			if ((hasard>35)&&(hasard<=40)) this.pic(saxe);
			if ((hasard>40)&&(hasard<=45)) this.pic(laxe);
			if ((hasard>45)&&(hasard<=50)) this.pic(sswo);
			if ((hasard>50)&&(hasard<=55)) this.pic(lswo);
			if ((hasard>55)&&(hasard<=60)) this.pic(dres);
			if ((hasard>60)&&(hasard<=65)) this.pic(sarm);
			if ((hasard>65)&&(hasard<=70)) this.pic(marm);
			if ((hasard>70)&&(hasard<=75)) this.pic(larm);
			if ((hasard>75)&&(hasard<=80)) this.pic(sglo);
			if ((hasard>80)&&(hasard<=85)) this.pic(mglo);
			if ((hasard>85)&&(hasard<=90)) this.pic(lglo);
			if ((hasard>90)&&(hasard<=95)) this.pic(sboo);
			if ((hasard>95)&&(hasard<=100)) this.pic(mboo);
			if ((hasard>100)&&(hasard<=105)) this.pic(lboo);
			if ((hasard>105)&&(hasard<=110)) this.pic(sbel);
			if ((hasard>110)&&(hasard<=115)) this.pic(mbel);
			if ((hasard>115)&&(hasard<=120)) this.pic(lbel);
			if ((hasard>120)&&(hasard<=125)) this.pic(shel);
			if ((hasard>125)&&(hasard<=130)) this.pic(mhel);
			if ((hasard>130)&&(hasard<=135)) this.pic(lhel);
			if ((hasard>135)&&(hasard<=140)) this.pic(rin1);
			if ((hasard>140)&&(hasard<=145)) this.pic(rin2);
			if ((hasard>145)&&(hasard<=150)) this.pic(rin3);
			if ((hasard>150)&&(hasard<=155)) this.pic(amu1);
			if ((hasard>155)&&(hasard<=160)) this.pic(amu2);
			if ((hasard>160)&&(hasard<=165)) this.pic(amu3);
			if ((hasard>165)&&(hasard<=170)) this.pic(sshi);
			if ((hasard>170)&&(hasard<=175)) this.pic(mshi);
			if ((hasard>175)&&(hasard<=180)) this.pic(lshi);		
		}
	}
}
