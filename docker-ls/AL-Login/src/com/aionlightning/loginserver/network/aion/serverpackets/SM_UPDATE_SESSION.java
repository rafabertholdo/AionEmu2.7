/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionlightning.loginserver.network.aion.serverpackets;

import com.aionlightning.loginserver.network.aion.AionServerPacket;
import com.aionlightning.loginserver.network.aion.LoginConnection;
import com.aionlightning.loginserver.network.aion.SessionKey;

/**
 * This packet is send to client to update sessionKey [for fast reconnection feature]
 * 
 * @author -Nemesiss-
 */
public class SM_UPDATE_SESSION extends AionServerPacket {

	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private final int accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private final int loginOk;

	/**
	 * Constructs new instance of <tt>SM_UPDATE_SESSION </tt> packet.
	 * 
	 * @param key
	 *          session key
	 */
	public SM_UPDATE_SESSION(SessionKey key) {
		super(0x0c);
		this.accountId = key.accountId;
		this.loginOk = key.loginOk;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginConnection con) {
		writeD(accountId);
		writeD(loginOk);
		writeC(0x00);// sysmsg if smth is wrong
	}
}
