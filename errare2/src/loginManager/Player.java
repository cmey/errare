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

package loginManager;

import persistenceEngine.*;
import logger.*;
import gameEngine.GameCharacter;

import java.net.*;


/**
 * This container class aggregates several elements allowing the server to
 * identify a connection, relate a socket with the account and gameCharacter being
 * used, etc.
 * 
 * A player can be only associated with a socket, so this object contained has
 * to be provided when creating it, and it can't be changed later.
 * 
 * @author mafm@users.sourceforge.net
 */
public class Player {
	/** The account of the player */
	private Account account;

	/** The gameCharacter of the player */
	private GameCharacter character;

	/** The socket of the player */
	private Socket socket;

	/**
	 * Default constructor.
	 * 
	 * @param socket
	 *            The socket that represents the player.
	 */
	public Player(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Get socket of the player.
	 * 
	 * @return Socket of the player.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Get account of the player.
	 * 
	 * @return Account of the player (fully validated, it can't be faked), null
	 *         if not set yet.
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Set account of the player.
	 * 
	 * @param account
	 *            Account of the player (fully validated, it can't be faked),
	 *            and it can only be set once.
	 */
	public void setAccount(Account account) {
		if (account == null) {
			this.account = account;
		} else {
			Logger.printWARNING("Socket tried to set the account twice (ignoring), existing='"
							+ this.account.getName() + "', pretended='"
							+ account.getName() + "'");
		}
	}

	/**
	 * Get gameCharacter of the player.
	 * 
	 * @return GameCharacter of the player (fully validated, it can't be faked),
	 *         null if not set yet.
	 */
	public GameCharacter getCharacter() {
		return character;
	}

	/**
	 * Set gameCharacter of the player.
	 * 
	 * @param gameCharacter
	 *            Account of the player (fully validated, it can't be faked),
	 *            and it can only be set once.
	 */
	public void setCharacter(GameCharacter character) {
		if (character == null) {
			this.character = character;
		} else {
			Logger.printWARNING("Socket tried to set the gameCharacter twice (ignoring), existing='"
							+ this.character.getName() + "', pretended='"
							+ character.getName() + "'");
		}
	}

	/**
	 * Is the gameCharacter playing, i.e., fully logged in and present in the world?
	 * 
	 * @return Whether it's playing or not yet.
	 */
	public boolean isPlaying() {
		return (account != null && character != null);
	}
}
