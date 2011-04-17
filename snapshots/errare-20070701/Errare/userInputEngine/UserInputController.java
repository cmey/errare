package userInputEngine;

import java.awt.event.InputEvent;
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
import javax.swing.SwingUtilities;

import databaseEngine.DatabaseEngine;

import main.Main;
import main2.Engine;

public class UserInputController implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

	private DatabaseEngine database;
	public static final String GAME_CONTEXT = "gameKeys";
	public static final String EDITOR_CONTEXT = "editorKeys";
	private String context;
	
	private Hashtable<Integer, String> mouse2action;
	private Hashtable<KeyStroke, String> keyboard2action;
	
	private Hashtable<String, List<Engine>> action2engines;
	
	
	public UserInputController(String context, DatabaseEngine database){
		this.database = database;
		this.context = context;
		mouse2action = new Hashtable<Integer, String>();
		keyboard2action = new Hashtable<KeyStroke, String>();
		action2engines = new Hashtable<String, List<Engine>>();
		
	}
	
	public boolean register(Engine e, String action){
		List<Engine> alreadyregistered = action2engines.get(action);
		if(alreadyregistered == null){
			if(! lookup_association(action)) return false;
			
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
				mouse2action.put(code, action);
				System.out.println("put: "+code);
			}else if(input.startsWith("key.")){
				KeyStroke code = getKeyCode(input);
				keyboard2action.put(code, action);
				System.out.println("put: "+code);
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
			}else if(decomp[i].equalsIgnoreCase("wheel")){
				mask |= MouseWheelEvent.MOUSE_WHEEL<<5;
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
	
	private void dispatchMouseEvent(String action, int x, int y){
		List<Engine> list = action2engines.get(action);
		if(list!=null){
			for(Engine engine : list){
				if(!engine.invokeMouseEvent(action, x, y))
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
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void mouseDragged(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_DRAGGED<<5);
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_MOVED<<5);
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void mouseClicked(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_CLICKED<<5);
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_PRESSED<<5);
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		int event = e.getModifiers()|(MouseEvent.MOUSE_RELEASED<<5);
		String action = mouse2action.get(event);
		if(action!=null)
			dispatchMouseEvent(action, e.getX(), e.getY());
	}

	public void keyPressed(KeyEvent e) {
		keyTyped(e);
	}

	public void keyReleased(KeyEvent e) {
		keyTyped(e);
		
	}

	public void keyTyped(KeyEvent e) {
		KeyStroke ks = KeyStroke.getKeyStrokeForEvent(e);
		String action = keyboard2action.get(ks);
		if(action!=null)
			dispatchKeyEvent(action);
	}

	
}
