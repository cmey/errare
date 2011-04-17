package networkEngine2;

import geom.Vector;

import java.util.ArrayList;

import physicsEngine2.PhysicalRep;

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

/**
 * networked representation of a player
 * @author linxsam
 */
public class Player {
	
	private String ID;
    private PhysicalRep pRep;
	private Client cl;
	private String passwordMD5;
	private ArrayList<Event> inEventBuffer, outEventBuffer;

	/**
	 * handler representation for the player
	 */
	public Player(String id, Client c,PhysicalRep prep) {
		ID = id;
		cl = c;
        cl.setID(id,this);
        pRep = prep;
		passwordMD5 = c.passMD5();
		// get player info from database with the id
		inEventBuffer = new ArrayList<Event>();
		outEventBuffer = new ArrayList<Event>();
	}
	
	/**
	 * the player's ID
	 * @return
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * creates a spwan event for the player to get into the game world
	 *
	 */
	public void spawnPlayer() {
		NetworkEngine.LOG.printINFO("spawning player :"+ID);
        inEventBuffer.add(new Event("logon"));
	}
	
	/**
	 * removes player from the gameworld at logoff
	 *
	 */
	public void removeplayer() {
		NetworkEngine.LOG.printINFO("client logged off removing from game");
        inEventBuffer.add(new Event("logoff"));
	}
	
	/**
	 * forces the buffered outgoing events to be writtent to the network interface
	 *
	 */
	public void flushOutgoingBuffer() {
		NetworkEngine.LOG.printDEBUG("flushing the outgoing event buffer");
		while(!outEventBuffer.isEmpty()) {
			Event e = outEventBuffer.remove(0);
			cl.overUdp1(new UdpEvent<Event>(ID,e,passwordMD5));
		}
		NetworkEngine.LOG.printDEBUG("flushed the outgoing event buffer");
	}
	
	/**
	 * add a new incomming event to the buffer
	 * @param e the event to add
	 */
	public void newIncommingEvent(Event e) {
		NetworkEngine.LOG.printDEBUG("adding new incomming event to the players buffer");
		inEventBuffer.add(e);
	}
	
	/**
	 * @return the next outgoing event from buffer, FIFO style and removes this event
	 */
	public Event nextIncommingEvent() {
		return inEventBuffer.remove(0);
	}
	
	/**
	 * @return the full incomming event buffer, does NOT empty it
	 */
	public ArrayList<Event> getIncommingEventBuffer() {
		return inEventBuffer;
	}
    
    /**
     * clears the incomming events buffer
     */
    public void clearIncommingEvtBuffer() {
        inEventBuffer.clear();
    }
	
	/**
	 * add a new outgoing event to the buffer
	 * @param e the event to add
	 */
	public void newOutgoingEvent(Event e) {
		NetworkEngine.LOG.printDEBUG("adding new outgoing event to the players buffer");
		outEventBuffer.add(e);
	}
	
	/**
	 * @return the full outgoing event buffer, does NOT empty it
	 */
	public ArrayList<Event> getOutgoingEventBuffer() {
		return outEventBuffer;
	}
    
    /**
     * clears the outgoing events buffer
     */
    public void clearOutgoingEventBuffer() {
        outEventBuffer.clear();
    }
	
	/**
	 * @return the next outgoing event from buffer, FIFO style and removes this event
	 */
	public Event nextOutgoingEvent() {
		return inEventBuffer.remove(0);
	}
    
    /**
     * @return the players physical representation
     */
    public PhysicalRep getpRep() {
        return pRep;
    }
    

}
