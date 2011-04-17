package guiEngine;

import java.awt.Rectangle;

public class Quest {
	
	private Rectangle questSite,
	upwardsQuestSite, scrollQuestSite, downwardsQuestSite;
	
	
	public boolean visible;
	
	public Quest() {
		
		questSite = new Rectangle(0,0,0,0);
		// ...
		
		visible=false;
	}

}
