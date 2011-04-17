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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread{
	
	private NetworkServer server;
	
	private ObjectOutputStream oos;
	
	private ObjectInputStream ois;
	
	private Socket s;
	
	private int id;
	
	public Client(Socket s,NetworkServer server,int id) throws IOException{
		super(s.getInetAddress().toString());
		this.s=s;
		this.id=id;
		this.server=server;
		start();
	}
	
	public void run(){
		try {
			System.out.println("Getting the oos");
			oos=new ObjectOutputStream(s.getOutputStream());
			System.out.println("Getting the ois");
			ois=new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			server.add(this);
			oos.writeInt(server.getSeed());
			oos.writeInt(server.getNbAi());
			oos.writeInt(server.getNbPlayers());
			oos.writeInt(id);
			oos.flush();
			ois.readBoolean();
			server.clientReady();
			System.out.println("Ready to get Objects");
			NetworkMove nm;
			while(true){
				nm=(NetworkMove)ois.readObject();
				if(nm.getId()==-1){
					server.addEvent();
				}else{
					server.send(nm);
				}
			}
		} catch (IOException e) {
			server.remove(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void send(NetworkMove move) throws IOException {
			oos.writeObject(move);
			oos.flush();
	}

	public void ready() throws IOException {
		oos.writeBoolean(true);
		oos.flush();
	}

	public void sendOK() throws IOException {
		oos.writeBoolean(true);
		oos.flush();
	}

	public void sendSignOut(int id2) throws IOException {
		oos.writeObject(new NetworkSignOut(id2));
		oos.flush();
	}
	
}


