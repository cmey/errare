/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Sebastien FISCHER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package networkEngine2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * the main game server
 *
 */
public class Server {
	
	public static final int MAX_UDP_CLIENT = 5;
	public static final int MAX_TCP_CLIENT = 100;
	
	public static final int listenPort = 15001;
	public static final int udp1start = 16000;
	public static final int udp2start = 17000;
	
	private ListenThread tcplisten;
	private ArrayList<UdpPort> udp1;
	private ArrayList<UdpPort> udp2;
	private ArrayList<Client> clients;
	
	private ServerSocket mainTCP;
	
	/**
	 * creates a new main game server instance
	 *
	 */
	public Server() {
		clients = new ArrayList<Client>();
		mainTCP = null;
		udp1 = null;
		udp2 = null;
		try {
			mainTCP = new ServerSocket(listenPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tcplisten = new ListenThread() {
			public void run() {
				while(run) {
					try {
						newClient(mainTCP.accept());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	/**
	 * the server starts it's duty
	 * @throws SocketException
	 */
	public void start() throws SocketException {
		System.out.println("Server is starting");
		tcplisten.start();
		udp1 = new ArrayList<UdpPort>();
		udp2 = new ArrayList<UdpPort>();
		udp1.add(new UdpPort(udp1start));
		udp2.add(new UdpPort(udp2start));
		System.out.println("Server  started");
	}
	
	/**
	 * the server shuts down and closes all connections
	 * @throws IOException
	 */
	public void stop() throws IOException {
		tcplisten.end();
		mainTCP.close();
		for(int i = 0 ; i < udp1.size();i++) {
			udp1.get(i).close();
		}
		for(int i = 0 ; i < udp2.size();i++) {
			udp2.get(i).close();
		}
		for(int i = 0 ; i < clients.size();i++) {
			clients.get(i).close();
		}
	}
	
	/**
	 * performs client check and adds it to the connected clients list
	 * called each time a new client connects
	 * @param soc the tcp socket between the client and the server
	 */
	private void newClient(Socket soc) {
		System.out.println("new client");
		long cot = System.currentTimeMillis();
		cot += Math.random();
		String challenge = new String(ConteneurMD5.md5Hash(Long.toHexString(cot).getBytes()));
		try {
			if(udp1.get(udp1.size()-1).spaceLeft()) udp1.get(udp1.size()-1).addClient();
			else udp1.add(new UdpPort(udp1.get(udp1.size()-1).getPort()+1));
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AuthPacket p =  new AuthPacket("",challenge,udp1.get(udp1.size()-1).getPort(),udp2.get(udp2.size()-1).getPort());
		Client cl = new Client(soc);
		try {
			cl.overTCP(p);
			TcpPacket tp = cl.fromTCP();
			AuthPacket ap = null;
			if(tp instanceof AuthPacket) ap = (AuthPacket) tp;
			String clientPass = "password"+challenge; // ${TODO} get password from database
			String compare = new String(ConteneurMD5.md5Hash(clientPass.getBytes()));
			if(ap != null && ap.getSignature().compareTo(compare) == 0) {
				// client is auth:
				cl.setUdp1(ap.getUdp1());
				cl.setUdp2(ap.getUdp2());
				cl.setIDR(ap.getID());
				// ${TODO} get the real ingame ID from game engine
				cl.setIDI(challenge);
				clients.add(cl);
				cl.start();
			}else cl.close(); // rejecting connection
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}