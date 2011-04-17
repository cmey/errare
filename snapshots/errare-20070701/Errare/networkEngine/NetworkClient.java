/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Antoine PIERRONNET

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package networkEngine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Random;

import main.Main;
import physicsEngine.CharacterPRep;

public class NetworkClient {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Main main;
	private int nbai;
	private int nbplayers;
	private int id;
	private Hashtable<Integer, CharacterPRep> hashtable;
	private int counter; 
	WaitingThread wt;
	
	public NetworkClient(Main main, String server, int port) throws UnknownHostException, IOException {
		this.main=main;
		System.out.println("Solving host..."+server+" port "+port);
		socket = new Socket(InetAddress.getByName(server), port);
		System.out.println("Solved");
		connectToServer();
	}

	public void connectToServer() throws IOException {
		
		System.out.println("Connecting...");
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		System.out.println("Connected");
		
		System.out.println("Receiving initialisation data");
		int seed = in.readInt();
		System.out.println("seed="+seed);
		main.setRandom(new Random(seed));
		nbai = in.readInt();
		System.out.println("nbai="+nbai);
		nbplayers = in.readInt();
		System.out.println("nbplayers="+nbplayers);
		id = in.readInt();
		System.out.println("id="+id);
		System.out.println("Received, sending ready");
		out.writeBoolean(true);
		out.flush();
		in.readBoolean();
		System.out.println("Game start !");
		wt=new WaitingThread();
		wt.start();
	}
	
	public void addMove(CharacterPRep mainChar, int x, int y) {
		send(new NetworkMove(id, x, y));
	}
	
	private void send(NetworkMove move) {
		try {
			out.writeObject(move);
		out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException, ClassNotFoundException {
		if(counter++==10) {
			send(new NetworkMove(-1,-1,-1));
			wt.waitAck();
		}
	}
	
	public int getId() {
		return id;
	}

	public int getNbai() {
		return nbai;
	}

	public int getNbplayers() {
		return nbplayers;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new NetworkClient(null, "192.168.0.2", 1099);
	}

	public void setMainChars(Hashtable<Integer, CharacterPRep> set) {
		this.hashtable=set;
		
	}

	private class WaitingThread extends Thread{
		
		private boolean ack;

		public void run(){
			Object nm;
			while(true){
				try {
					if(ack){
						in.readBoolean();
						System.out.println("read");
						ack=false;
					}else{
						nm=in.readObject();
						if(nm instanceof NetworkMove)
						main.getPhysicsEngine().addNetworkMove(hashtable.get(((NetworkMove)nm).getId()),((NetworkMove)nm).getX(),((NetworkMove)nm).getY());
						
					}
				} catch (IOException e) {
					//e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		public void waitAck() {
			ack=true;
		}
		
	}

}
