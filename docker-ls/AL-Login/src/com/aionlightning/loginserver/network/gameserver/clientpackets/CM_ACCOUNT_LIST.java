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
package com.aionlightning.loginserver.network.gameserver.clientpackets;

import com.aionlightning.loginserver.GameServerTable;
import com.aionlightning.loginserver.controller.AccountController;
import com.aionlightning.loginserver.model.Account;
import com.aionlightning.loginserver.network.gameserver.GsClientPacket;
import com.aionlightning.loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;

/**
 * Reads the list of accoutn id's that are logged to game server
 * 
 * @author SoulKeeper
 */
public class CM_ACCOUNT_LIST extends GsClientPacket {

	/**
	 * Array with accounts that are logged in
	 */
	private String[] accountNames;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		accountNames = new String[readD()];
		for (int i = 0; i < accountNames.length; i++) {
			accountNames[i] = readS();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		for (String s : accountNames) {
			Account a = AccountController.loadAccount(s);
			if (GameServerTable.isAccountOnAnyGameServer(a)) {
				this.getConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(a.getId()));
				continue;
			}
			getConnection().getGameServerInfo().addAccountToGameServer(a);
		}
	}
}
