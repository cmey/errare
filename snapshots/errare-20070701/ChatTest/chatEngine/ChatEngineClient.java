package chatEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The main chat program client with the entry point.
 * @author Christophe
 *
 */
public class ChatEngineClient{

	boolean dontstop; // while this is true, the program runs
	ChatRep me;
	BufferedReader val;
	
	public ChatEngineClient(){
		this.dontstop = true;
		val = new BufferedReader(new InputStreamReader(System.in)); // get a handle to something that can read input from keyboard
		//TODO: connect to network in any way
		
		System.out.println("Welcome in the chat, type \"quit\" to exit the program.");
	}
	
	void setupMyName(String myName){
		me = new ChatRep(myName);
	}
	
	public void run(){
		try { // try to read from keyboard
			String txtentered = val.readLine(); // it's a blocking call
			if(txtentered.equalsIgnoreCase("quit")){
				this.dontstop = false;
				return;
			}
			ChatTextMessage msg = new ChatTextMessage(txtentered);
			msg.setWhoSends(me);
			// TODO: send this ChatTextMessage over the network to the server
			
		} catch (IOException e) {
			// keyboard unplugged, out of memory, etc... we dont care
			e.printStackTrace();
		}
	}
	
	/**
	 * Shut down properly.
	 * For demonstration purpose, it will say goodbye and then disconnect properly from the server
	 */
	public void quit(){
		// SAY GOODBYE
		ChatTextMessage msg = new ChatTextMessage("(Client Quit) : GOODBYE.");
		msg.setWhoSends(me);
		//TODO: send this ChatTextMessage over the network to the server
		
		// DISCONNECT
		//TODO: disconnect properly
	}

	
	
	/**
	 * This should be called by the network engine when a message arrives for the ChatEngine
	 * I suppose the caller will be in another thread
	 * @param msg 
	 */
	public void incommingNetworkMessage(NetworkMessage msg){
		if(msg instanceof ChatTextMessage){
			// CAPTURE :
			ChatTextMessage txt = (ChatTextMessage) msg;
			ChatRep sender = txt.who;
			
			// PROCESS :
			System.out.println(sender.name + ": "+ txt.getText());
		}
	}
	
	
	
	/**
	 * Main entry point
	 * @param args you must give one parameter : give your nickname
	 */
	public static void main(String[] args){
		if(args.length!=1){
			System.out.println("Wrong parameter or no parameter");
			System.out.println("Usage : chatEngine/ChatEngineClient <your nickname>");
		}
		ChatEngineClient chat = new ChatEngineClient();
		chat.setupMyName(args[0]); // set the nickname of this client
		while(chat.dontstop){
			chat.run();
		}
		chat.quit();
	}
}
