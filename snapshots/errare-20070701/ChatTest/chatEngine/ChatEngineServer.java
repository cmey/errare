package chatEngine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The main chat program server with the entry point.
 * @author Christophe
 *
 */
public class ChatEngineServer{

	boolean dontstop; // while this is true, the program runs
	Vector<ChatRep> people;
	ChatRep me;
	
	public ChatEngineServer(){
		this.dontstop = true;
		me = new ChatRep("Server");
		
		//TODO: connect to network in any way
	}
	
	
	public void run(){
		
	}
	
	
	/**
	 * Shut down properly.
	 * For demonstration purpose, it will say goodbye to all clients and then disconnect
	 */
	public void quit(){
		// SAY GOODBYE
		ChatTextMessage msg = new ChatTextMessage("(Server shutdown) : GOODBYE.");
		msg.setWhoSends(me);
		//TODO: send this ChatTextMessage over the network to every client
		for(ChatRep client : people){
			//TODO: send message
		}

		// DISCONNECT
		//TODO: disconnect properly
		// maybe do nothing here, as the networkEngine will close sockets in his quit() method
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
			ChatRep who = txt.who;
			
			// PROCESS :
			for(ChatRep client : people){
				//TODO: send msg to client
			}
		}
	}
	
	
	
	/**
	 * Main entry point
	 * @param args you must give one parameter : give your nickname
	 */
	public static void main(String[] args){
		ChatEngineServer chat = new ChatEngineServer();
		while(chat.dontstop){
			chat.run();
		}
		chat.quit();
	}
}
