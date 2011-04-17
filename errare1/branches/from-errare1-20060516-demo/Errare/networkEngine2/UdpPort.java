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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * a new udp port with threadened incomming trafic listener
 *
 */
public class UdpPort extends DatagramSocket {

	private int nbClConnected;
	private ListenThread listener;
	
	
	/**
	 * @param port
	 * @throws SocketException
	 */
	public UdpPort(int port) throws SocketException {
		super(port);
		this.
		nbClConnected = 0;
		listener = new ListenThread() {
			public void run() {
				while(run) {
					DatagramPacket p = new DatagramPacket(new byte[65536],65536);
					try {
						receive(p);
						UdpPacket up = UdpPacket.fromArray(p.getData());
						if(up instanceof UdpPacket) {
							System.out.println("Unhandled UdpPacket received");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	public boolean spaceLeft() {
		if(nbClConnected <= Server.MAX_UDP_CLIENT) return true;
		else return false;
	}
	
	public void removeClient() throws Exception {
		if(nbClConnected > 0) nbClConnected--;
		else throw new Exception("no Clients connected to port");
	}
	
	public void addClient() throws Exception {
		if(spaceLeft()) nbClConnected++;
		else throw new Exception("max client allready reached");
	}
	
	public void close() {
		listener.end();
		super.close();
		nbClConnected = 0;
	}

}
