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
import java.net.InetAddress;
import java.net.Socket;

/**
 * Client superclass, defines all common fields for client and server side
 * ${TODO} add client timeout detection
 */
public class Client {
	
	/**
	 * autoping send frequence in seconds
	 */
	private static final int pingrate = 1;
	
	/**
	 * persistant tcp connection
	 */
	private Socket tcp;
	/**
	 * destionation udp ports
	 */
	private int udp1,udp2;
	/**
	 * networkID, clients reference trought the network
	 */
	protected String IDR;
	/**
	 * ingameID, internal reference of the client, for mapping to the game engine
	 */
	protected String IDI;
	/**
	 * the clients net address
	 */
	protected InetAddress ip;
	/**
	 * the tcp communication streams
	 */
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	
	private ListenThread tcpListener;
	
	//	 ${TODO} add various statistics about the network-----------
	/**
	 * the clients latency in milliseconds
	 */
	private long latency;
	/**
	 * data quantities that have been sent to the relative udp ports
	 */
	private long udp1DataQ,udp2DataQ;
	/**
	 * number of received/sent packets over the corresponding interface
	 */
	private int nbtcppRcv, nbtcpSnd, nbudp1Rcv, nbudp1Snd, nbudp2Rcv, nbudp2Snd;
	
	
	private long lastPingSent;
	/**
	 * creates a new default client
	 * @param tcp the tcp socket between server and client
	 */
	public Client(Socket tcp) {
		this.tcp = tcp;
		this.udp1 = -1;
		udp1DataQ = 0;
		udp2DataQ = 0;
		nbtcppRcv = 0;
		nbtcpSnd = 0;
		nbudp1Rcv = 0;
		nbudp1Snd = 0;
		nbudp2Rcv = 0;
		nbudp2Snd = 0;
		this.udp2 = -1;
		IDR = "";
		IDI = "";
		ip = tcp.getInetAddress();
		ois = null;
		oos = null;
		tcpListener = null;
		lastPingSent = System.currentTimeMillis();
		try {
			oos = new ObjectOutputStream(tcp.getOutputStream());
			ois = new ObjectInputStream(tcp.getInputStream());
			tcpListener = new ListenThread() {
				public void run() {
					while(this.run) {
						if(System.currentTimeMillis()>= lastPingSent+(1000*pingrate)) sendPing();
						TcpPacket p;
						try {
							p = fromTCP();
							nbtcppRcv++;
							if(p instanceof PingPacket) {// handles the ping traffic
								System.out.println(IDI+" incomming ping");
								if ((p.getID()).compareTo(IDR) == 0) {
									latency = ((PingPacket) p).getLatency();
									System.out.println("new latency: "+latency);
								} else {
									overTCP(p);
								}
							} else if(p instanceof AuthPacket) {
								if(((AuthPacket) p).getID().compareTo("server") == 0) {
									String chall = "password"+((AuthPacket) p).getSignature();// ${TODO} change default password to the real password
									AuthPacket ap = new AuthPacket("",new String(ConteneurMD5.md5Hash(chall.getBytes())),udp1,udp2);
									overTCP(ap);
								} else {
									IDR = ((AuthPacket) p).getID();
									String chall = "password"+((AuthPacket) p).getSignature();// ${TODO} change default password to the real password
									AuthPacket ap = new AuthPacket("",new String(ConteneurMD5.md5Hash(chall.getBytes())),udp1,udp2);
									overTCP(ap);
								}
							} else if(p instanceof DataPacket) {// handles data transfering traffic
								
							} else if(p instanceof TcpPacket) {
								System.out.println("incomming tcp packet not recongized");
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			};

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		System.out.println("Client started");
		tcpListener.start();
	}
	
	/**
	 * generates the networkID from the supplyed data for unique identification thought
	 * the network
	 * @param password the connexion password
	 * @param challenge the chalenge the server has sent upon connection
	 * @return String representation of the ID used for network identification
	 */
	public static String generateIDR(String password, String challenge,InetAddress ip) {
		String id = challenge+password+ip.getHostAddress();
		return new String(ConteneurMD5.md5Hash(id.getBytes()));
	}
	
	/**
	 * unsigned datagram for port udp1 destination
	 * @param trame the udppacket to be inserted in the datagram
	 * @return
	 */
	public DatagramPacket paquetUdp1(UdpPacket trame) {
		byte[] ar = trame.toArray();
		udp1DataQ += trame.size();
		DatagramPacket paq = new DatagramPacket(ar,ar.length,ip,udp1);
		return paq;
	}
	
	/**
	 * signed datagram for port udp1 destination uses ConteneurMD5 for signing
	 * @param trame the udppacket to be inserted in the datagram
	 * @return
	 */
	public DatagramPacket paquetUdp1MD5(UdpPacket trame) {
		ConteneurMD5 cnt = new ConteneurMD5(trame, IDR.getBytes());
		return new DatagramPacket(cnt.toArray(),cnt.toArray().length,ip,udp1);
		
	}
	
	/**
	 * unsigned datagram for port udp2 destination
	 * @param trame the udppacket to be inserted in the datagram
	 * @return
	 */
	public DatagramPacket paquetUdp2(UdpPacket trame) {
		byte[] ar = trame.toArray();
		udp2DataQ += trame.size();
		DatagramPacket paq = new DatagramPacket(ar,ar.length,ip,udp2);
		return paq;
	}
	
	/**
	 * signed datagram for port udp2 destination uses ConteneurMD5 for signing
	 * @param trame the udppacket to be inserted in the datagram
	 * @return
	 */
	public DatagramPacket paquetUdp2MD5(UdpPacket trame) {
		ConteneurMD5 cnt = new ConteneurMD5(trame, IDR.getBytes());
		return new DatagramPacket(cnt.toArray(),cnt.toArray().length,ip,udp2);
	}
	
	/**
	 * writes the packet to the tcp socket
	 * @param packet
	 * @throws IOException 
	 */
	public void overTCP(TcpPacket packet) throws IOException {
		oos.writeObject(packet);
		oos.flush();
	}
	
	/**
	 * methode used to read from the tcp socket, this method should never
	 * be directly used, as it will be used by the listener on top of the
	 * socket and as this method is halting
	 * @return the received packet
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public TcpPacket fromTCP() throws IOException, ClassNotFoundException {
		TcpPacket tcpp = (TcpPacket) ois.readObject();
		return tcpp;
	}
	
	/**
	 * sends a ping request trought the tcp connection
	 * does not wait for reply, it'll be hadled by the
	 * listener on top of the socket
	 */
	public void sendPing() {
		try {
			lastPingSent = System.currentTimeMillis();
			overTCP(new PingPacket(IDR));
			System.out.println("sending ping request");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the latency value but does not ensure the
	 * value is accurate. (call first the sendPing method
	 * to force update af the latency value)
	 * @return the value of the latency (maybe outdated)
	 */
	public int getLatency() {
		return (int) latency;
	}
	
	/**
	 * closes the tcp connexion
	 * @throws IOException
	 */
	public void close() throws IOException {
		ois.close();
		oos.flush();
		oos.close();
		tcp.close();
	}

	/**
	 * @return Returns the number of tcppackets received.
	 */
	public int getNbtcppRcv() {
		return nbtcppRcv;
	}

	/**
	 * @return Returns the number of tcp packets sent.
	 */
	public int getNbtcpSnd() {
		return nbtcpSnd;
	}

	/**
	 * @return Returns the number of udppackets received on the udp1 port.
	 */
	public int getNbudp1Rcv() {
		return nbudp1Rcv;
	}

	/**
	 * @return Returns the number of udppackets sent trought the udp1 port.
	 */
	public int getNbudp1Snd() {
		return nbudp1Snd;
	}

	/**
	 * @return Returns the number of udppackets received on the udp2 port.
	 */
	public int getNbudp2Rcv() {
		return nbudp2Rcv;
	}

	/**
	 * @return Returns the number of udppackets sent trought the udp2 port.
	 */
	public int getNbudp2Snd() {
		return nbudp2Snd;
	}

	/**
	 * @return Returns the number of bytes sent trought the udp1 port.
	 */
	public long getUdp1DataQ() {
		return udp1DataQ;
	}

	/**
	 * @return Returns the number of bytes sent trought the udp2 port.
	 */
	public long getUdp2DataQ() {
		return udp2DataQ;
	}

	/**
	 * @param idi The iDI to set.
	 */
	public void setIDI(String idi) {
		IDI = idi;
	}

	/**
	 * @param idr The iDR to set.
	 */
	public void setIDR(String idr) {
		IDR = idr;
	}

	/**
	 * @param udp1 The udp1 to set.
	 */
	public void setUdp1(int udp1) {
		this.udp1 = udp1;
	}

	/**
	 * @param udp2 The udp2 to set.
	 */
	public void setUdp2(int udp2) {
		this.udp2 = udp2;
	}

}
