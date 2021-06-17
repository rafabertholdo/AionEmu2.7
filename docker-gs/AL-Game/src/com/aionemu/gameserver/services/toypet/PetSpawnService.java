/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.toypet;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.controllers.PetController;
import com.aionemu.gameserver.dao.PlayerPetsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.player.PetCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.pet.PetFunction;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.model.templates.pet.PetTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WAREHOUSE_INFO;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.sql.Timestamp;

/**
 * @author ATracer
 */
public class PetSpawnService {

	/**
	 * @param player
	 * @param petId
	 */
	public static void summonPet(Player player, int petId, boolean isManualSpawn) {
		PetCommonData lastPetCommonData;

		if (player.getPet() != null) {
			if (player.getPet().getPetId() == petId) {
				PacketSendUtility.broadcastPacket(player, new SM_PET(3, player.getPet()), true);
				return;
			}

			lastPetCommonData = player.getPet().getCommonData();
			dismissPet(player, isManualSpawn);
		} else {
			lastPetCommonData = player.getPetList().getLastUsedPet();
		}

		if(lastPetCommonData != null) {
			// reset mood if other pet is spawned
			lastPetCommonData.clearMoodStatistics();
		}

		player.getController().addTask(
			TaskId.PET_UPDATE,
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new PetController.PetUpdateTask(player),
				PeriodicSaveConfig.PLAYER_PETS * 1000, PeriodicSaveConfig.PLAYER_PETS * 1000));

		Pet pet = VisibleObjectSpawner.spawnPet(player, petId);
		//It means serious error or cheater - why its just nothing say "null"?
		if (pet != null) {
			sendWhInfo(player, petId);

			if(System.currentTimeMillis() - pet.getCommonData().getDespawnTime().getTime() > 10 * 60 * 1000) {
				// reset mood if pet was despawned for longer than 10 mins.
				player.getPet().getCommonData().clearMoodStatistics();
			}

			player.getPetList().setLastUsedPetId(petId);
		}
	}

	/**
	 * @param player
	 * @param petId
	 */
	private static void sendWhInfo(Player player, int petId) {
		PetTemplate petTemplate = DataManager.PET_DATA.getPetTemplate(petId);
		PetFunction pf = petTemplate.getWarehouseFunction();
		if (pf != null) {
			int itemLocation = 0;

			switch (pf.getSlots()) {
				case 6:
					itemLocation = 32;
					break;
				case 12:
					itemLocation = 33;
					break;
				case 18:
					itemLocation = 34;
					break;
				case 24:
					itemLocation = 35;
					break;
			}

			if (itemLocation != 0) {
				PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(player.getStorage(itemLocation).getItemsWithKinah(),
					itemLocation, 0, true, player));

				PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(null, itemLocation, 0, false, player));
			}
		}
	}

	/**
	 * @param player
	 * @param isManualDespawn
	 */
	public static void dismissPet(Player player, boolean isManualDespawn) {
		Pet toyPet = player.getPet();
		if (toyPet != null) {
			toyPet.getCommonData().setCancelFood(true);
			if (toyPet.getPetTemplate().ContainsFunction(PetFunctionType.FOOD)) {
				DAOManager.getDAO(PlayerPetsDAO.class).setHungryLevel(player, toyPet.getPetId(),
					toyPet.getCommonData().getHungryLevel());
			}

			player.getController().cancelTask(TaskId.PET_UPDATE);

			// TODO needs for pet teleportation
			if (isManualDespawn)
				toyPet.getCommonData().setDespawnTime(new Timestamp(System.currentTimeMillis()));

			toyPet.getCommonData().savePetMoodData();

			player.setToyPet(null);
			toyPet.getController().delete();
		}

	}
}
