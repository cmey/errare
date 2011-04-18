/* Errare Humanum Est Project
 * Copyright (C) 2007 Manuel A. Fernandez Montecelo <mafm@users.sourceforge.net>
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

import com.db4o.*;
import logger.*;

/**
 * This is a server for Persistence Services. It starts a database server.
 * 
 * @author mafm@users.sourceforge.net
 * 
 * 
 * Update : removed static singleton design, and extracted the client to a separate class 'PersistenceClient'.
 * 
 * @author cyberchrist
 */
public class PersistenceServer {

	/** DB server */
	private ObjectServer db;

	/** Filename to user as DB */
	final private static String DBFILENAME = "dberrare.db4o";

	/** Server port */
	final protected static int PORT = 13668;

	/** User with priviledges */
	final protected static String USER = "enter";

	/** Password for user with priviledges */
	final protected static String PASSWORD = "enter";

	
	/** Default constructor */
	public PersistenceServer() {
		Logger.printDEBUG("Starting");
		configureServer();
		db = Db4o.openServer(DBFILENAME, PORT);
		db.grantAccess(USER, PASSWORD);
	}

	/** Configure the server. */
	private void configureServer() {
		Db4o.configure().exceptionsOnNotStorable(true);
		Db4o.configure().objectClass("java.lang.Object").cascadeOnDelete(true);
		Db4o.configure().generateUUIDs(Integer.MAX_VALUE);
		Db4o.configure().generateVersionNumbers(Integer.MAX_VALUE);
	}

	/** Close the database when exit. */
	protected void finalize() {
		db.close();
	}
}