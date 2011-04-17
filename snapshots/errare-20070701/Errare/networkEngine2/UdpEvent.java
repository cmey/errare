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

/**
 * @author linxsam
 *
 */
public class UdpEvent<E extends Event> extends UdpPacket {

	private static final long serialVersionUID = 3343570114672488613L;

	
	
	private E event;
	
	private String signature;
	
	/**
	 * udp enveloppe for events
	 * @param id the performing clients ID
	 * @param event the Event to propagate
	 * @param password the clients password for signature generation
	 */
	public UdpEvent(String id, E event, String password) {
		super(id);
		this.event = event;
		signature = Secured.md5Hash((password+event.toString()).getBytes());
	}
	
	/**
	 * @param password the clients password for signature check
	 * @return true if the signature is ok
	 */
	public boolean signed(String password) {
		return signature == Secured.md5Hash((password+event.toString()).getBytes());
	}
	
	/**
	 * @return the propagated Event
	 */
	public E getEvent() {
		return event;
	}
	
	

}
