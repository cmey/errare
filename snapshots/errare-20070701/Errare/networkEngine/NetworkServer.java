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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkServer extends ServerSocket {
	
	private int nbPlayers;
	
	private int nbAi; 
	
	private int seed;
	
	private ArrayList<Client> clients;
	
	private ArrayList<NetworkMove> events;
	
	private int nbReadyClient;
	
	private int clientlist;
	
	public NetworkServer(int port,int nbPlayers,int nbAi) throws IOException {
		super(port);
		this.nbPlayers=nbPlayers;
		this.nbAi=nbAi;
		clientlist=0;
		seed=(int)(Math.random()*1000);
		clients=new ArrayList<Client>();
		events=new ArrayList<NetworkMove>();
		System.out.println("Server launched, waiting for clients");
		waitForClients();
	}
	
	private void waitForClients() throws IOException {
		int currentPlayers=0;
		Socket temp;
		while(currentPlayers<nbPlayers){
			temp=accept();
			System.out.println("Client connected: "+temp.getInetAddress());
			new Client(temp,this,currentPlayers);
			currentPlayers++;
		}
		startGame();
	}
	
	
	
	private void startGame() throws IOException {
		while(nbReadyClient!=nbPlayers){}
		System.out.println("Be ready clients!");
		for(Client c : clients){
			c.ready();
		}
	}
	
	private void sendAck() throws IOException{
		synchronized(events){
			for(Client c : clients){
				new SendingThread(c).start();
			}
			clientlist=0;
			events.clear();
		}
	}
	
	
	
	public void remove(Client client) {
		int index=0;
		synchronized(clients){
			for(Client c : clients){
				if (c.equals(client)){
					clients.remove(index);
				}else{
					try {
						c.sendSignOut(index);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				index++;
			}
		}
		System.out.println("Client disconnected");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new NetworkServer(1099,2,3);
	}
	
	public int getSeed() {
		return seed;
	}
	
	public int getNbAi() {
		return nbAi;
	}
	
	public int getNbPlayers() {
		return nbPlayers;
	}
	
	public void clientReady() {
		nbReadyClient++;
	}
	
	public void addEvent() throws IOException {
			clientlist++;
		if(clientlist==nbPlayers){
			sendAck();
		}
	}

	public void add(Client client) {
		synchronized(clients){
		clients.add(client);
		}
	}
	
	private class SendingThread extends Thread{
		
		private Client client;
		
		public SendingThread(Client c){
			client=c;
		}
		
		public void run(){
			try {
				client.sendOK();
			} catch (IOException e) {
				
			}
		}
	}

	public void send(NetworkMove nm) throws IOException {
		for(Client c : clients){
			c.send(nm);
		}
	}
}
