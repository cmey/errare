package gameEngine;

public abstract class Step {
	
	/** time of a steps in milisec*/
	protected long timer;
	
	protected Action action;
	
	public Step(Action a){
		action = a;
	}
	
	/** rend true qd l'action est finie*/
	public abstract boolean perform();

}
