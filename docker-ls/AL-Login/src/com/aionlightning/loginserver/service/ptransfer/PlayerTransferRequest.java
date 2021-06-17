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
package com.aionlightning.loginserver.service.ptransfer;

import com.aionlightning.loginserver.model.Account;

/**
 * @author KID
 */
public class PlayerTransferRequest {
	public PlayerTransferStatus status;
	public byte serverId;
	public byte targetServerId;
	public Account targetAccount;
	public byte[] db;
	public String name;
	public int targetAccountId;
	public int playerId;
	public Account account;
	public Account saccount;
	public int taskId;

	public PlayerTransferRequest(PlayerTransferStatus status) {
		this.status = status;
	}
}
