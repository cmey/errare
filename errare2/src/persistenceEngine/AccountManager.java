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

import gameEngine.GameCharacter;

import com.db4o.*;
import com.db4o.query.*;
import logger.*;

/**
 * This class encapsulates the operations that have to deal with accounts
 * (addition and removal, addition and removal of characters, etc).
 * 
 * @author mafm@users.sourceforge.net
 * 
 * Update : removed static singleton design
 * 
 * @author cyberchrist
 */
public class AccountManager {

	/** DB client */
	private ObjectContainer client;

	/** Limit to the number of characters per account */
	private static final int MAX_CHARACTERS_PER_ACCOUNT = 8;

	/** Default constructor. */
	public AccountManager(PersistenceClient dbClient) {
		if(dbClient == null){
			Logger.printFATAL("A PersistenceClient argument is needed and was null ! Create a PersistenceClient before the AccountManager and pass it as an argument.");		
			System.exit(0);
		}
		client = dbClient.getClient();
	}

	/**
	 * Get the account with given name, if exists.
	 * 
	 * @param name
	 *            Name of the account.
	 * @return The account, null if not found.
	 */
	public Account getAccount(final String name) {
		ObjectSet<Account> result = client.query(new Predicate<Account>() {
			public boolean match(Account account) {
				if (account.getName().equals(name)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return result.next();
	}

	/**
	 * Add an account, usually asked by a player to register itself.
	 * 
	 * @param account
	 *            Account data (from register message).
	 * @return Whether it was created or not.
	 */
	public boolean addAccount(Account account) {

		// check whether account name already exists
		Account test = new Account(account.getName(), null);
		ObjectSet result = client.get(test);
		if (result.size() != 0) {
			Logger.printINFO("Username '"
					+ account.getName()
					+ "' already exists, rejecting registration request");
			return false;
		}

		// create
		client.set(account);
		Logger.printINFO("Registered new account '" + account.getName() + "'");
		return true;

	}

	/**
	 * Delete an account, usually asked by a player.
	 * 
	 * @param account
	 *            Account data (from delete message).
	 * @return Whether it was deleted or not.
	 */
	public boolean removeAccount(Account account) {
		// check if it exists and password matches
		Account test = new Account(account.getName(), account.getPassword());
		ObjectSet resultExists = client.get(test);
		if (resultExists.size() == 0) {
			Logger.printINFO("Username '"
					+ account.getName()
					+ "' not found (or password doesn't match), "
					+ "can't remove account");
			return false;
		}

		// delete
		client.delete(account);
		Logger.printINFO("Removed account '"
				+ account.getName() + "'");
		return true;
	}

	/**
	 * Get a gameCharacter by name.
	 * 
	 * @param name
	 *            The name of the gameCharacter
	 * @return The gameCharacter if found, null otherwise.
	 */
	public GameCharacter getCharacter(String name) {
		Account fullMatch = new Account(null, null);
		for (ObjectSet result = client.get(fullMatch); result.hasNext();) {
			Account a = (Account) result.next();
			for (GameCharacter c : a.getCharacters()) {
				if (c.getName().equals(name))
					return c;
			}
		}
		return null;
	}

	/**
	 * Add a gameCharacter to an account, usually asked by a player.
	 * 
	 * @param Character
	 *            GameCharacter to be added.
	 * @param account
	 *            Account to add the gameCharacter to. The account does exist,
	 *            because it has to come from a logged in connection -- and thus
	 *            is validated.
	 * @return Whether it was created or not.
	 */
	public boolean addCharacterToAccount(GameCharacter character, Account account) {

		// whether the account has free slots for new characters
		if (account.getNumberOfCharacters() >= MAX_CHARACTERS_PER_ACCOUNT) {
			Logger.printINFO("Username '"
					+ account.getName() + "' reached the gameCharacter limited: "
					+ account.getNumberOfCharacters());
			return false;
		}

		// whether the gameCharacter name already exists
		for (GameCharacter c : account.getCharacters()) {
			if (c.getName().equals(character.getName())) {
				Logger.printINFO("GameCharacter '"
						+ character.getName()
						+ "' already registered, for user '"
						+ account.getName() + "'");
				return false;
			}
		}

		// all checks passed, create the new gameCharacter
		account.addCharacter(character);
		client.set(account);
		Logger.printINFO("Added new gameCharacter '"
				+ character.getName() + "' to '" + account.getName() + "'");
		return true;
	}

	/**
	 * Remove a gameCharacter from an account, usually asked by a player.
	 * 
	 * @param Character
	 *            GameCharacter to be added.
	 * @param account
	 *            Account to remove the gameCharacter from. The account does exist,
	 *            because it has to come from a logged in connection -- and thus
	 *            is validated.
	 * @return Whether it was created or not.
	 */
	public boolean removeCharacterFromAccount(GameCharacter character,
			Account account) {
		// whether the gameCharacter name belongs to the account
		for (GameCharacter c : account.getCharacters()) {
			if (c.getName().equals(character.getName())) {
				// all checks passed, remove the gameCharacter
				account.removeCharacter(character);
				client.set(account);
				Logger.printINFO("Removed gameCharacter '" + character.getName()
								+ "' from '" + account.getName() + "'");
				return true;
			}
		}

		Logger.printINFO("GameCharacter '"
				+ character.getName() + "' not found for user '"
				+ account.getName() + "', ignoring deletion request");
		return false;
	}

	/**
	 * Get the number of accounts registered.
	 * 
	 * @return The number of accounts registered.
	 */
	public int getNumberOfAccounts() {
		Account fullMatch = new Account(null, null);
		return client.get(fullMatch).size();
	}

	/**
	 * Get the number of characters registered.
	 * 
	 * @return The number of characters registered.
	 */
	public int getNumberOfCharacters() {
		Account fullMatch = new Account(null, null);
		int total = 0;
		for (ObjectSet result = client.get(fullMatch); result.hasNext();) {
			Account a = (Account) result.next();
			total += a.getNumberOfCharacters();
		}
		return total;
	}
}
