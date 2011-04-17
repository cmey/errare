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
public class RstPacket extends TcpPacket {

	private static final long serialVersionUID = -5933121646796095997L;
	/**
	 * the reason of the rst request
	 */
	private String reason;
	
	/**
	 * instanciates a new rst packet, as disconnection request
	 * @param id the requesting client's id
	 * @param reason the closing reason
	 */
	public RstPacket(String id, String reason) {
		super(id, "closing");
		this.reason = reason;
	}
	
	/**
	 * @return the reason of the rst request
	 */
	public String getReason() {
		return reason;
	}

}
