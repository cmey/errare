/* Errare Humanum Est Project
 * Copyright (C) 2007 Christophe MEYER <cyberchrist@hotmail.fr>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package persistenceEngine;

import java.io.IOException;

import logger.Logger;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

/**
 * This is a client for Persistence Services. It connects to a PersistenceServer.
 * This should only be used by the server, the account creator, and the web service.
 * This class has nothing to do in the Client side !
 * 
 * @author Christophe
 */
public class PersistenceClient {

	/** DB client */
	private ObjectContainer client;
	
	/** Server hostname */
	final public static String SERVER = "localhost";
	
	public PersistenceClient() throws IOException{
		Logger.printDEBUG("Starting");
		client = Db4o.openClient(SERVER, PersistenceServer.PORT, PersistenceServer.USER, PersistenceServer.PASSWORD);
		Logger.printDEBUG("PersistenceClient successfully connected to database ! using server hostname: '"+SERVER + "' port: '"+PersistenceServer.PORT+"' user: '"+PersistenceServer.USER +"' password: '"+PersistenceServer.PASSWORD + "'");
	}
	
	/**
	 * Get the client to access the storage.
	 * 
	 * @return The DB client object.
	 */
	public ObjectContainer getClient() {
		return client;
	}
}
