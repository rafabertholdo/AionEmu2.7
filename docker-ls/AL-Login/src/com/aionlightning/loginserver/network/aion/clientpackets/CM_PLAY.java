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
package com.aionlightning.loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;

import com.aionlightning.loginserver.GameServerInfo;
import com.aionlightning.loginserver.GameServerTable;
import com.aionlightning.loginserver.network.aion.AionAuthResponse;
import com.aionlightning.loginserver.network.aion.AionClientPacket;
import com.aionlightning.loginserver.network.aion.LoginConnection;
import com.aionlightning.loginserver.network.aion.SessionKey;
import com.aionlightning.loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import com.aionlightning.loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import com.aionlightning.loginserver.network.aion.serverpackets.SM_PLAY_OK;

/**
 * @author -Nemesiss-
 */
public class CM_PLAY extends AionClientPacket {

	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private int accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private int loginOk;
	/**
	 * Id of game server that this client is trying to play on.
	 */
	private byte servId;
	
	public CM_PLAY(ByteBuffer buf, LoginConnection client)
	{
		super(buf, client, 0x02);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
		loginOk = readD();
		servId = (byte) readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		LoginConnection con = getConnection();
		SessionKey key = con.getSessionKey();
		if (key.checkLogin(accountId, loginOk))
		{
			GameServerInfo gsi = GameServerTable.getGameServerInfo(servId);
			if (gsi == null || !gsi.isOnline())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_DOWN));
			else if (gsi.isFull())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_FULL));
			else
			{
				con.setJoinedGs();
				sendPacket(new SM_PLAY_OK(key, servId));
			}
		}
		else
			con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), false);
	}
}
