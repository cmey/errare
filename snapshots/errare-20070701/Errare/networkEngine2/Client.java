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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


/**
 * Class of the networked clients, they are the link bentween the player and the network interface
 * @author linxsam
 */
public class Client {
	
	protected String ID;
	private InetAddress ip;
	protected String passMD5;
	private int udp1,udp2;
	private Socket tcp;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Threaded tcpListener,udp1Listener,udp2Listener;
	private boolean isRunning;
	private long tcplatency,udplatency,lastping;
	private Player pl;
	private DatagramSocket udp1p,udp2p;
	
	/**
	 * instanciates a new client
	 * @param soc the socket the remote side is connected to
	 * @param id the clients id
	 */
	public Client(Socket soc, String id) {
		udp1p = null;
		udp2p = null;
		pl = null;
		tcplatency = 0;
		udplatency = 0;
		isRunning = false;
		tcp = soc;
		udp1 = 0;
		udp2 = 0;
		ID = id;
		ip = soc.getInetAddress();
		passMD5 = Secured.getPassFor(id);
		lastping = System.currentTimeMillis();
		try {
			oos = new ObjectOutputStream(tcp.getOutputStream());
			ois = new ObjectInputStream(tcp.getInputStream());
			tcpListener = new Threaded() {
				public void run() {
					while(run) {
						TcpPacket p;
						try {
							if(lastping+2000 <= System.currentTimeMillis()) {
								sendPingTcp();
								lastping = System.currentTimeMillis();
							}
							p = fromTcp();
							if(p instanceof PingPacketTcp) {
								NetworkEngine.LOG.printDEBUG("incomming tcp ping");
								PingPacketTcp pp = (PingPacketTcp) p;
								if(pp.getID().compareTo(ID) == 0) {
									tcplatency = pp.getLatency();
									NetworkEngine.LOG.printINFO("new tcp lantency: "+tcplatency);
								} else {
									overTcp(pp);
									NetworkEngine.LOG.printDEBUG("tcp ping packet replyed");
								}
							} else if(p instanceof TchatPacket) {
								TchatPacket tp = (TchatPacket) p;
								if (tp.getDest().compareTo(ID) == 0) NetworkEngine.LOG.printINFO(tp.getID()+":"+tp.getText());
								else forwardto(tp);
							} else if(p instanceof InfoPacket) {
								//InfoPacket i = (InfoPacket) p;
								// TODO see what type of infopacket it is and operate on it
							} else if(p instanceof RstPacket) {
								this.end();
								stopClient();
								NetworkEngine.LOG.printDEBUG("have received a rstpacket, reason: "+((RstPacket)p).getReason());
							} else if(p instanceof AuthPacket) {
								NetworkEngine.LOG.printDEBUG("Authenticating");
								AuthPacket ap = (AuthPacket) p;
								if(ap.getDest().compareTo("NEW_CLIENT") == 0) {
								AuthPacket rp = new AuthPacket(ID,"server",Secured.md5Hash((ap.getSignature()+passMD5).getBytes()),udp1,udp2);
								overTcp(rp);
								} else if(ap.getDest().compareTo("CONNECTION_ACCEPTED") == 0) {
									NetworkEngine.LOG.printDEBUG("Authentication complete");
									udp1 = ap.getUdp1();
									udp2 = ap.getUdp2();
								} else NetworkEngine.LOG.printDEBUG("unknown authpacket received. state:"+ap.getDest());
								
							} else throw new IOException("unknown tcppacket received");
						} catch (IOException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						} catch (ClassNotFoundException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			};
			udp1Listener = new Threaded() {
				public void run() {
					while(run) {
						byte[] t;
						try {
							t = new byte[udp1p.getReceiveBufferSize()];
							DatagramPacket p = new DatagramPacket(t,t.length);
							udp1p.receive(p);
							UdpPacket up = UdpPacket.fromArray(p.getData());
							if(up instanceof UdpEvent) {
								UdpEvent um = (UdpEvent) up;
								// TODO test signature
								pl.newIncommingEvent(um.getEvent());
							} else if(up instanceof PingPacket) {
								PingPacket pp = (PingPacket) up;
								if(pp.getDest().compareTo("SERVER") == 0) {
									
								}
							}
						} catch (SocketException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						} catch (IOException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						}
					}
				}
			};
			udp2Listener = new Threaded() {
				public void run() {
					while(run) {
						byte[] t;
						try {
							t = new byte[udp1p.getReceiveBufferSize()];
							DatagramPacket p = new DatagramPacket(t,t.length);
							udp1p.receive(p);
							UdpPacket up = UdpPacket.fromArray(p.getData());
							if(up instanceof UdpEvent) {
								UdpEvent um = (UdpEvent) up;
								// TODO test signature
								pl.newIncommingEvent(um.getEvent());
							}
						} catch (SocketException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						} catch (IOException e) {
							NetworkEngine.LOG.printFATAL(e.getMessage());
						}
					}
				}
			};
		} catch(IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
	}
	
	/**
	 * forward the received packet to the main game system
	 * -> here it won't do anything, this methode is only a place holder
	 * @param p
	 */
	public void forwardto(TcpPacket p) {
		
	}
	
	/**
	 * start the listenning thread
	 *
	 */
	public void start() {
		tcpListener.start();
		udp1p = nextFreeUdpPort(tcp.getInetAddress(), udp1);
		udp2p = nextFreeUdpPort(tcp.getInetAddress(), udp2);
		udp1Listener.start();
		udp2Listener.start();
		isRunning = true;
	}
	
	/**
	 * stop the listenning thread and close all connctions
	 * -> only use in critical state prefer the end() method with sends rst packet prior to stream
	 * closing
	 * @throws IOException
	 */
	public void stopClient() throws IOException {
		tcpListener.end();
		udp1Listener.end();
		udp2Listener.end();
		ois.close();
		oos.close();
		tcp.close();
		udp1p.close();
		udp2p.close();
		isRunning = false;
	}
	
	/**
	 * send a tcppacket over the tcp stream
	 * @param p the packet to send
	 * @throws IOException if something went wrong
	 */
	public void overTcp(TcpPacket p) {
		if(tcp.isConnected())
			try {
				oos.writeObject(p);
			} catch (IOException e) {
				NetworkEngine.LOG.printFATAL(e.getMessage());
				try {
					stopClient();
				} catch (IOException e1) {
					NetworkEngine.LOG.printFATAL(e1.getMessage());
				}
			}
	}
	
	/**
	 * receive a new tcppacket from the tcp stream
	 * @return the received packet
	 * @throws IOException if something went wrong during reception
	 * @throws ClassNotFoundException if the received object is not a compatible tcppacket
	 */
	public TcpPacket fromTcp() throws ClassNotFoundException {
		if(tcp.isConnected())
			try {
				return (TcpPacket) ois.readObject();
			} catch (IOException e) {
				NetworkEngine.LOG.printFATAL(e.getMessage());
				try {
					stopClient();
				} catch (IOException e1) {
					NetworkEngine.LOG.printFATAL(e1.getMessage());
				}
			}
			return null;
	}
	
	/**
	 * tells the client to stop, and close anything
	 *
	 */
	public void end() {
		NetworkEngine.LOG.printINFO("Client is ending his connection");
		tcpListener.end();
		try {
			overTcp(new RstPacket(ID,"client will close"));
			ois.close();
			oos.close();
			tcp.close();
			isRunning = false;
		} catch (IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * tells the client to stop, and close anything
	 * leaves custom reason
	 * @param reason the closing reason
	 */
	public void end(String reason) {
		NetworkEngine.LOG.printINFO("Client is ending his connection");
		tcpListener.end();
		try {
			overTcp(new RstPacket(ID,reason));
			ois.close();
			oos.close();
			tcp.close();
			isRunning = false;
		} catch (IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * forges suited datagrams from udppackets
	 * @param p the udppacket to send
	 */
	public void overUdp1(UdpPacket p) {
		byte[] ar = p.toArray();
		try {
			udp1p.send(new DatagramPacket(ar,ar.length,ip,udp1));
		} catch (IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
	}
	
	/**
	 * forges suited datagrams from udppackets
	 * @param p the udppacket to send
	 */
	public void overUdp2(UdpPacket p) {
		byte[] ar = p.toArray();
		try {
			udp1p.send(new DatagramPacket(ar,ar.length,ip,udp2));
		} catch (IOException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
	}
	
	/**
	 * @return the running state of the client
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * @return the tcplink latency
	 */
	public long getTcpLatency() {
		return tcplatency;
	}
	
	/**
	 * updates the udplink latency
	 * @param latency
	 */
	public void setUdpLatency(long latency) {
		udplatency = latency;
	}
	
	/**
	 * @return the udplink latency
	 */
	public long getUdpLatency() {
		return udplatency;
	}
	
	/**
	 * sends a ping request over the tcp link
	 *
	 */
	public void sendPingTcp() {
		NetworkEngine.LOG.printDEBUG("sending ping request with ID: "+ID);
		overTcp(new PingPacketTcp(ID,""));
	}
	
	public String getID() {
		return ID;
	}
	
	public String passMD5() {
		return passMD5;
	}
	
	public void setID(String id,Player p) {
		ID = id;
		pl = p;
	}
	
	public static DatagramSocket nextFreeUdpPort(InetAddress remoteAddress, int remotePort) {
		DatagramSocket ret = null;
		try {
			ret = new DatagramSocket();
		} catch (SocketException e) {
			NetworkEngine.LOG.printFATAL(e.getMessage());
		}
		while(!ret.isBound()) {
			ret.connect(remoteAddress, remotePort);
		}
		return ret;
	}
	
	public int getUdp1Listenport() {
		return udp1p.getLocalPort();
	}
	
	public int getUdp2Listenport() {
		return udp2p.getLocalPort();
	}
	
	public Player getPlayer() {
		return pl;
	}

}
