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

package networkLayer.messages;

/**
 * Simple Login message.
 * 
 * @author mafm@users.sourceforge.net
 */
public class LoginNetworkMessage extends AbstractNetworkMessage {
	public String username;

	public String pwdHash;

	public LoginNetworkMessage() {
		super("LoginNetworkMessage");
	}

	public String toString() {
		return "[" + name + "|" + username + "|" + pwdHash + "]";
	}
}
