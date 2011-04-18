package gameEngine;

import java.util.ArrayList;

public abstract class Action{
	
	protected String id;
	protected int current_step;
	protected ArrayList<Step> steps;
	protected GameCharacter creator;
	protected GameCharacter target;
	
	/** rend true qd l'action est finie */
	public abstract boolean perform();
}
