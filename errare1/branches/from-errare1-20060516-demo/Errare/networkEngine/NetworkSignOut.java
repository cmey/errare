package networkEngine;

import java.io.Serializable;

public class NetworkSignOut implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	
	public NetworkSignOut(int id){
		this.id=id;
	}
	
	public int getID(){
		return this.id;
	}
}
