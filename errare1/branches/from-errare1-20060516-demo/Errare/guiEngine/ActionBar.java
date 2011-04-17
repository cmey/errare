package guiEngine;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ActionBar {
	
	private final int MAX_OBJECT=8;
	private final int MAX_ACTION=10;
	
	private Chat chat;
	private Inventory inventory;
	private Characteristic characteristic;
	
	private Rectangle actionBarSite, barSite, mapSite,
	generalChatSite, guildChatSite, groupChatSite, privateChatSite,inventorySite,characteristicSit,
	meleeXP, rangedXP, magicalXP,
	object1Site,object2Site,object3Site,object4Site,object5Site,object6Site,object7Site,object8Site,
	action1Site,action2Site,action3Site,action4Site,action5Site,actio6Site,action7Site,action8Site,action9Site,action10Site,
	backward,forward;
	
	private int [] tabObject = new int[MAX_OBJECT];
	private int [] tabAction = new int[MAX_ACTION];
	
	private ArrayList comboList;
	private boolean visible;
	
	public ActionBar() {
		
		actionBarSite = new Rectangle(0,0,0,0);
		// ...
		chat = new Chat();
		inventory = new Inventory();
		characteristic = new Characteristic();
		

		comboList = new ArrayList();
	}
	
	public void addActionToComboList() {
		
	}
	
	public void addAction() {
		
	}
	
	public void addObject() {
		
	}
	
	public void doAction (int action) {
		
	}
	
	public void useObject (int object) {
		
		if (tabObject[object]!=0) {
			// Object action
			tabObject[object]=0;
		}
	}
	
	public void updateXP() {
		
	}
	
	public void openChat(int channel) {
		chat.openChannel(channel);
	}
	
	public void openInventory() {
		inventory.open();
	}
	
	public void openCharacteristic() {
		characteristic.open();
	}

}
