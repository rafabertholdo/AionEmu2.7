/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.brusthonin;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Nephis
 */
public class _4042MessageinaBottle extends QuestHandler {

	private final static int questId = 4042;

	public _4042MessageinaBottle() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730150).addOnQuestStart(questId); // Bottle
		qe.registerQuestNpc(730150).addOnTalkEvent(questId);
		qe.registerQuestNpc(205192).addOnTalkEvent(questId); // Sahnu
		qe.registerQuestNpc(204225).addOnTalkEvent(questId); // Gunter
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		switch (targetId) {
			case 730150: {
				return giveQuestItem(env, 182209024, 1);
			}
			case 205192: {
				if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
					if (env.getDialog() == QuestDialog.START_DIALOG)
						return sendQuestDialog(env, 1352);
					else if (env.getDialog() == QuestDialog.STEP_TO_1) {
						if (!giveQuestItem(env, 182209025, 1))
							return true;
						removeQuestItem(env, 182209024, 1);
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					else
						return sendQuestStartDialog(env);
				}

				else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
					if (env.getDialog() == QuestDialog.START_DIALOG)
						return sendQuestDialog(env, 2375);
					else if (env.getDialogId() == 1009) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
						return sendQuestStartDialog(env);
				}

				else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
					return sendQuestEndDialog(env);
				}
			}

			case 204225: {
				if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
					if (env.getDialog() == QuestDialog.START_DIALOG)
						return sendQuestDialog(env, 1693);
					else if (env.getDialog() == QuestDialog.STEP_TO_2) {
						removeQuestItem(env, 182209025, 1);
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					else
						return sendQuestStartDialog(env);
				}
			}

			case 0: {
				if (env.getDialogId() == 1002) {
					QuestService.startQuest(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
					return true;
				}
			}
		}
		return false;
	}
}
