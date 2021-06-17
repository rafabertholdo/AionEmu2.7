/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.CreatureController;
import com.aionemu.gameserver.controllers.SummonController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.attack.PlayerAggroList;
import com.aionemu.gameserver.controllers.movement.SummonMoveController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.SummonGameStats;
import com.aionemu.gameserver.model.stats.container.SummonLifeStats;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.SummonStatsTemplate;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author ATracer
 */
public class Summon extends Creature {

	private Player master;
	private SummonMode mode;
	private byte level;
	private int liveTime = 0;

	public static enum SummonMode {
		ATTACK(0),
		GUARD(1),
		REST(2),
		RELEASE(3),
		UNK(5);

		private int id;

		private SummonMode(int id) {
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
	}

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param position
	 * @param level
	 */
	public Summon(int objId, CreatureController<? extends Creature> controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate, byte level) {
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition());
		controller.setOwner(this);
		moveController = new SummonMoveController(this);
		this.level = level;
		this.mode = SummonMode.GUARD;
		SummonStatsTemplate statsTemplate = DataManager.SUMMON_STATS_DATA.getSummonTemplate(objectTemplate.getTemplateId(),
			level);
		setGameStats(new SummonGameStats(this, statsTemplate));
		setLifeStats(new SummonLifeStats(this));
	}

	@Override
	protected AggroList createAggroList() {
		return new PlayerAggroList(this);
	}

	@Override
	public SummonGameStats getGameStats() {
		return (SummonGameStats) super.getGameStats();
	}

	@Override
	public Player getMaster() {
		return master;
	}

	/**
	 * @param master
	 *          the master to set
	 */
	public void setMaster(Player master) {
		this.master = master;
	}

	@Override
	public String getName() {
		return objectTemplate.getName();
	}

	/**
	 * @return the level
	 */
	@Override
	public byte getLevel() {
		return level;
	}

	@Override
	public NpcTemplate getObjectTemplate() {
		return (NpcTemplate) super.getObjectTemplate();
	}

	public int getNpcId() {
		return getObjectTemplate().getTemplateId();
	}

	public int getNameId() {
		return getObjectTemplate().getNameId();
	}

	/**
	 * @return NpcObjectType.SUMMON
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.SUMMON;
	}

	@Override
	public SummonController getController() {
		return (SummonController) super.getController();
	}

	/**
	 * @return the mode
	 */
	public SummonMode getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *          the mode to set
	 */
	public void setMode(SummonMode mode) {
		this.mode = mode;
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return master != null ? master.isEnemy(creature) : false;
	}

	@Override
	public boolean isEnemyFrom(Npc npc) {
		return master != null ? master.isEnemyFrom(npc) : false;
	}

	@Override
	public boolean isEnemyFrom(Player player) {
		return master != null ? master.isEnemyFrom(player) : false;
	}

	@Override
	public TribeClass getTribe() {
		return master.getTribe();
	}

	@Override
	public final boolean isAggroFrom(Npc npc) {
		if (getMaster() == null)
			return false;

		return getMaster().isAggroFrom(npc);
	}
	
	@Override
	public SummonMoveController getMoveController() {
		return (SummonMoveController) super.getMoveController();
	}
	
	@Override
	public Creature getActingCreature() {
		return getMaster() == null ? this : getMaster();
	}
	
	@Override
	public Race getRace() {
		return getMaster() != null ? getMaster().getRace() : Race.NONE;
	}

	/**
	 * @return liveTime in sec.
	 */
	public int getLiveTime() {
		return liveTime;
	}
	
	/**
	 * @param liveTime in sec.
	 */
	public void setLiveTime(int liveTime) {
		this.liveTime = liveTime;
	}
}
