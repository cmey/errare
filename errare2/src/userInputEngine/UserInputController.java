package userInputEngine;

import genericEngine.Engine;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.KeyStroke;

import logger.Logger;
import xmlEngine.XmlEngine;

/**
 * This manages inputs with the mouse and the keyboard. Engines register
 * for an action name, name which is binded to a physical event in an
 * XML file.
 * When that key is pressed, the corresponding action is triggered, and
 * the engine which registered that action, receives an Event.
 * This allows for an easy keyboard and mouse button configuration by the final user.
 * The engine must implement the interface Engine.
 * @author trueblue
 * 
 * Added support for multiple actions binded on the same key.
 * @author Christophe
 */
public class UserInputController implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

	private XmlEngine database;
	public static final String GAME_CONTEXT = "gameKeys";
	public static final String EDITOR_CONTEXT = "editorKeys";
	private String context;
	
	private Hashtable<Integer, List<String>> mouse2action;
	private Hashtable<KeyStroke, List<String>> keyboard2action;
	
	private Hashtable<String, List<Engine>> action2engines;
	
	
	public UserInputController(String context, XmlEngine database){
		this.database = database;
		this.context = context;
		mouse2action = new Hashtable<Integer, List<String>>();
		keyboard2action = new Hashtable<KeyStroke, List<String>>();
		action2engines = new Hashtable<String, List<Engine>>();
	}
	
	public boolean register(Engine e, String action){
		List<Engine> alreadyregistered = action2engines.get(action);
		if(alreadyregistered == null){
			if(! lookup_association(action)) {
				Logger.printERROR("Could not register "+action+ ". Not bound in database !");
				return false;
			}
			
			alreadyregistered = new LinkedList<Engine>();
			action2engines.put(action, alreadyregistered);
		}
		alreadyregistered.add(e);
		return true;
	}
	
	private boolean lookup_association(String action){
		String input = database.get(context+"."+action);
		if(input == null){
			return false; //action doesnt exist
		}else{
			if(input.startsWith("mouse.")){
				int code = getMouseCode(input);
				List<String> alreadyassociated = mouse2action.get(code);
				if(alreadyassociated == null){					
					alreadyassociated = new LinkedList<String>();
					mouse2action.put(code, alreadyassociated);
				}
				alreadyassociated.add(action);
			}else if(input.startsWith("key.")){
				KeyStroke code = getKeyCode(input);
				List<String> alreadyassociated = keyboard2action.get(code);
				if(alreadyassociated == null){					
					alreadyassociated = new LinkedList<String>();
					keyboard2action.put(code, alreadyassociated);
				}
				alreadyassociated.add(action);
			}else{
				return false;
			}
			return true;
		}
	}
	
	public static int getMouseCode(String input){
		int mask=0;
		
		String[] decomp = input.split("\\.");
		for(int i=1; i<decomp.length; i++) {
			if(decomp[i].equalsIgnoreCase("shift")){
				mask |= MouseEvent.SHIFT_MASK;
			}else if(decomp[i].equalsIgnoreCase("alt")){
				mask |= MouseEvent.ALT_MASK;
			}else if(decomp[i].equalsIgnoreCase("ctrl")){
				mask |= MouseEvent.CTRL_MASK;
			}else if(decomp[i].equalsIgnoreCase("control")){
				mask |= MouseEvent.CTRL_MASK;
			}else if(decomp[i].equalsIgnoreCase("meta")){
				mask |= MouseEvent.META_MASK;
			}else if(decomp[i].equalsIgnoreCase("altGraph")){
				mask |= MouseEvent.ALT_GRAPH_MASK;
			}else if(decomp[i].equalsIgnoreCase("clicked")){
				mask |= MouseEvent.MOUSE_CLICKED<<5;
			}else if(decomp[i].equalsIgnoreCase("pressed")){
				mask |= MouseEvent.MOUSE_PRESSED<<5;
			}else if(decomp[i].equalsIgnoreCase("released")){
				mask |= MouseEvent.MOUSE_RELEASED<<5;
			}else if(decomp[i].equalsIgnoreCase("wheelup")){
				mask |= MouseWheelEvent.MOUSE_WHEEL<<5;
			}else if(decomp[i].equalsIgnoreCase("wheeldown")){
				mask |= (MouseWheelEvent.MOUSE_WHEEL<<5 | 1<<27);
			}else if(decomp[i].equalsIgnoreCase("moved")){
				mask |= MouseEvent.MOUSE_MOVED<<5;
			}else if(decomp[i].equalsIgnoreCase("dragged")){
				mask |= MouseEvent.MOUSE_DRAGGED<<5;
			}
			else if(decomp[i].equalsIgnoreCase("button1")){
				mask |= MouseEvent.BUTTON1_MASK;
			}
			else if(decomp[i].equalsIgnoreCase("button2")){
				mask |= MouseEvent.BUTTON2_MASK;
			}
			else if(decomp[i].equalsIgnoreCase("button3")){
				mask |= MouseEvent.BUTTON3_MASK;
			}else throw new IllegalArgumentException("Invalid key combination in XML file");
		}
		return mask;
	}
	
	private void dispatchMouseEvent(String action){
		List<Engine> list = action2engines.get(action);
		if(list!=null){
			Point location = MouseInfo.getPointerInfo().getLocation();
			for(Engine engine : list){
				if(!engine.invokeMouseEvent(action, location.x, location.y))
					break;
			}
		}
	}
	
	private void dispatchKeyEvent(String action){
		List<Engine> list = action2engines.get(action);
		if(list!=null){
			for(Engine engine : list){
				if(!engine.invokeKeyEvent(action))
					break;
			}
		}
	}
	
	public static KeyStroke getKeyCode(String input){
		String[] decomp = input.split("\\.");
		input="";
		for(int i=1; i<decomp.length; i++) {
			input+=decomp[i]+" ";
		}
		return KeyStroke.getKeyStroke(input);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_WHEEL<<5);
		if(e.getWheelRotation() < 0){
			event |= 1<<27;
		}
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void mouseDragged(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_DRAGGED<<5);
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void mouseMoved(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_MOVED<<5);
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void mouseClicked(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_CLICKED<<5);
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_PRESSED<<5);
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void mouseReleased(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_RELEASED<<5);
		List<String> actions = mouse2action.get(event);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchMouseEvent(action);
	}

	public void keyPressed(KeyEvent e) {
		keyTyped(e);
	}

	public void keyReleased(KeyEvent e) {
		keyTyped(e);
		
	}

	public void keyTyped(KeyEvent e) {
		KeyStroke ks = KeyStroke.getKeyStrokeForEvent(e);
		List<String> actions = keyboard2action.get(ks);
		if(actions != null)
			for(String action : actions)
				if(action!=null)
					dispatchKeyEvent(action);
	}

	
}
