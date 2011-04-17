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
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * the Errare dedicated server
 * -> server will soon be changed as realy separated server dedication will be implemented
 * @author linxsam
 */
public class Server {
	
	private int MAX_CLIENTS = 50; // TODO max 50 clients for testing

	private String ServerID;
	private ServerSocket tcp;
	private Threaded tcpListener, clientCleaner, pingloop;
	private Hashtable<String,SClient> clients;

	/**
	 * instanciates a new Errare server
	 *
	 */
	public Server() {
		ServerID = "server";
		clients =new Hashtable<String, SClient>();
		try {
			tcp = new ServerSocket(15001);
			tcpListener = new Threaded() {
				public void run() {
					while(run) {
						Socket soc;
						try {
							soc = tcp.accept();
							if(!isBanned(soc.getInetAddress())) {
								if(clients.size() >= MAX_CLIENTS) {
									NetworkEngine.LOG.printINFO("Refusing new client connection, MAX_CLIENTS already reached");
									ObjectOutputStream o = new ObjectOutputStream(soc.getOutputStream());
									o.writeObject(new RstPacket("Server","MAX_CLIENT already reached"));
									o.flush();
									o.close();
									soc.close();
								} else newClient(soc);
							} else {
								NetworkEngine.LOG.printINFO("Refusing client connection, banned IP: "+soc.getInetAddress().getHostAddress());
								ObjectOutputStream o = new ObjectOutputStream(soc.getOutputStream());
								o.writeObject(new RstPacket("Server","your IP has been banned"));
								o.flush();
								o.close();
								soc.close();
							}
						} catch (IOException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						}
					}
				}
			};
			clientCleaner = new Threaded() {
				public void run() {
					while(run) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						}
						NetworkEngine.LOG.printDEBUG("Starting client activity check");
						if(clients.size()>0) {
							Enumeration<String> k = clients.keys();
							while(k.hasMoreElements()) {
								String key = k.nextElement();
								if(!clients.get(key).isRunning()) {
									NetworkEngine.LOG.printINFO("Removing inactive client: "+key);
									clients.remove(key);
								}
							}
						}
					}
				}
			};
			pingloop = new Threaded() {
				public void run() {
					while(run) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e2) {
							NetworkEngine.LOG.printFATAL(e2.getMessage());
						}
						Enumeration<SClient> e = clients.elements();
						while(e.hasMoreElements()) {
							(e.nextElement()).sendPingTcp();
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								NetworkEngine.LOG.printFATAL(e1.getMessage());
							}
						}
					}
				}
			};
		} catch (IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
	}
	
	/**
	 * starts the servers working process
	 *
	 */
	public void start() {
		NetworkEngine.LOG.printINFO("Server is starting");
		NetworkEngine.LOG.printDEBUG("starting tcpListener");
		tcpListener.start();
		NetworkEngine.LOG.printDEBUG("starting clientCleaner");
		clientCleaner.start();
		NetworkEngine.LOG.printDEBUG("starting pingloop");
		pingloop.start();
	}
	
	/**
	 * shuts down the server
	 *
	 */
	public void stop() {
		NetworkEngine.LOG.printINFO("Server is going down");
		NetworkEngine.LOG.printDEBUG("stopping pingloop");
		pingloop.end();
		NetworkEngine.LOG.printDEBUG("stopping tcpListener");
		tcpListener.end();
		NetworkEngine.LOG.printDEBUG("Closing connections");
		Enumeration<SClient> e = clients.elements();
		while(e.hasMoreElements()) {
			(e.nextElement()).end();
		}
		NetworkEngine.LOG.printDEBUG("stopping clientCleaner");
		clientCleaner.end();
		try {
			tcp.close();
		} catch (IOException e1) {
			NetworkEngine.LOG.printFATAL(e1.getMessage());
		}
	}
	
	/**
	 * adding a new client to the server
	 * @param soc the clients tcp connection socket
	 */
	private void newClient(Socket soc) {
		NetworkEngine.LOG.printINFO("new incomming Client from: "+soc.getInetAddress().getHostAddress());
		SClient cl = new SClient(soc,this);
		String challenge = Secured.md5Hash((""+System.currentTimeMillis()+Math.random()).getBytes());
		try {
			NetworkEngine.LOG.printDEBUG("sending challenge: "+challenge+" to: "+soc.getInetAddress().getHostAddress());
			cl.overTcp(new AuthPacket(ServerID,"NEW_CLIENT",challenge,0,0));
			NetworkEngine.LOG.printDEBUG("awayting challenge response from : "+soc.getInetAddress().getHostAddress());
			AuthPacket p = (AuthPacket) cl.fromTcp();
			if(p.getSignature().compareTo(Secured.md5Hash((challenge+Secured.getPassFor(p.getID())).getBytes())) == 0) {
				NetworkEngine.LOG.printINFO("login suceeded from: "+soc.getInetAddress().getHostAddress()+"used ID: "+p.getID());
				cl.remoteID = p.getID();
				cl.setID("server",null);
				cl.start();
				cl.overTcp(new AuthPacket(ServerID,"CONNECTION_ACCEPTED",challenge,cl.getUdp1Listenport(),cl.getUdp2Listenport()));
				clients.put(cl.remoteID,cl);
			} else {
				NetworkEngine.LOG.printINFO("wrong answer to challenge from: "+soc.getInetAddress().getHostAddress());
				cl.end("BAD_SIGNATURE");
			}
		} catch (ClassNotFoundException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * tests if the ip is banned from this server
	 * @param ip the ip to test
	 * @return true if the ip is banned
	 */
	private boolean isBanned(InetAddress ip) {
		NetworkEngine.LOG.printDEBUG("checking ban on IP "+ip.getHostAddress());
		return false; // TODO no one will be banned for now
	}
	
	/**
	 * packet forwarding over the udp ports
	 * @param p
	 */
	public void forward(TcpPacket p) {
		if(p.getDest().compareTo("server") == 0) {
			NetworkEngine.LOG.printDEBUG("starting multiple forwards");
			if(clients.size()>0) {
				Enumeration<String> k = clients.keys();
				while(k.hasMoreElements()) {
					String key = k.nextElement();
					if(!clients.get(key).isRunning()) {
						NetworkEngine.LOG.printDEBUG("forwarding to: "+key);
						p.setDest(key);
						clients.get(key).overTcp(p);
					}
				}
			}
		} else {
			NetworkEngine.LOG.printDEBUG("forwarding to: "+p.getDest());
			clients.get(p.getDest()).overTcp(p);
		}
	}
	
}
