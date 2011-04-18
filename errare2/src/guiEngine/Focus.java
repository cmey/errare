package guiEngine;

import java.util.ArrayList;

public class Focus {
	
	private ArrayList<Integer> focusPriorityList;
	
	public static final int CHAT_FOCUS=0;
	public static final int EQUIPEMENT_FOCUS=1;
	public static final int INVENTORY_FOCUS=2;
	
	public Focus () {
		
		this.focusPriorityList = new ArrayList<Integer>(3);
		this.focusPriorityList.add(0,CHAT_FOCUS);
		this.focusPriorityList.add(1,EQUIPEMENT_FOCUS);
		this.focusPriorityList.add(2,INVENTORY_FOCUS);
	}
	
	public void setPriority(int window) {
		
		// if the window isn't the prioritier
		if (getPriority(window)!=0) {
			
			this.focusPriorityList.set(0,window);
			
			switch (window) {
			
			case CHAT_FOCUS:
				this.focusPriorityList.set(1,EQUIPEMENT_FOCUS);
				this.focusPriorityList.set(2,INVENTORY_FOCUS);
				break;
				
			case EQUIPEMENT_FOCUS:
				this.focusPriorityList.set(1,CHAT_FOCUS);
				this.focusPriorityList.set(2,INVENTORY_FOCUS);
				break;
				
			case INVENTORY_FOCUS:
				this.focusPriorityList.set(1,CHAT_FOCUS);
				this.focusPriorityList.set(2,EQUIPEMENT_FOCUS);
				break;
			}
		}
		
		//System.out.println("Focus : \n1) "+focusPriorityList.get(0)+"\n2) "+focusPriorityList.get(1)+"\n3) "+focusPriorityList.get(2));
	}
	
	public int getPriority(int window) {
		
		//System.out.println("Get priority : "+ this.focusPriorityList.indexOf(window));
		return this.focusPriorityList.indexOf(window);
	} 

}
