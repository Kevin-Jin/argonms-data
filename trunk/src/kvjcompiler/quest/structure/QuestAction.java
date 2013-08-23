/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010-2013  GoldenKevin
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package kvjcompiler.quest.structure;

import java.util.ArrayList;
import java.util.List;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class QuestAction extends QuestBehavior {
	private final List<QuestSkillAction> skills;
	private int expGain;
	private short nextQuest;
	private int money;
	private int buffToGive;
	private short petTameness;
	private short petSkill;

	public QuestAction() {
		skills = new ArrayList<QuestSkillAction>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("exp")) {
			expGain = Integer.parseInt(value);
		} else if (key.equals("nextQuest")) {
			nextQuest = Short.parseShort(value);
		} else if (key.equals("money")) {
			money = Integer.parseInt(value);
		} else if (key.equals("npcAct")) {
		} else if (key.equals("buffItemID")) {
			buffToGive = Integer.parseInt(value);
		}/* else if (key.equals("petspeed")) {
			//???
		}*/ else if (key.equals("pettameness")) {
			petTameness = Short.parseShort(value);
		} else if (key.equals("petskill")) {
			petSkill = Short.parseShort(value);
		} /*else if (key.equals("message")) {
		} else if (key.equals("info")) { //what the heck is this???

		} else if (key.equals("ask")) {

		}*/
	}

	public void addSkill(QuestSkillAction s) {
		skills.add(s);
	}

	@Override
	public int size() {
		int size = super.size();
		size += Size.BYTE; //amount of skills
		for (QuestSkillAction s : skills)
			size += s.size();
		if (expGain != 0)
			size += Size.BYTE + Size.INT;
		if (nextQuest != 0)
			size += Size.BYTE + Size.SHORT;
		if (money != 0)
			size += Size.BYTE + Size.INT;
		if (buffToGive != 0)
			size += Size.BYTE + Size.INT;
		if (petTameness != 0)
			size += Size.BYTE + Size.SHORT;
		if (petSkill != 0)
			size += Size.BYTE + Size.SHORT;
		if (minLevel != 0)
			size += Size.BYTE + Size.SHORT;
		if (fame != 0)
			size += Size.BYTE + Size.SHORT;
		if (endDate != 0)
			size += Size.BYTE + Size.INT;
		if (repeatInterval != -1)
			size += Size.BYTE + Size.INT;
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		super.writeBytes(lew);
		lew.writeByte((byte) skills.size());
		for (QuestSkillAction skill : skills)
			skill.writeBytes(lew);
		if (expGain != 0)
			lew.writeByte(QuestBehaviors.REWARD_EXP_GAIN).writeInt(expGain);
		if (nextQuest != 0)
			lew.writeByte(QuestBehaviors.REWARD_NEXT_QUEST).writeShort(nextQuest);
		if (money != 0)
			lew.writeByte(QuestBehaviors.REWARD_MESOS).writeInt(money);
		if (buffToGive != 0)
			lew.writeByte(QuestBehaviors.REWARD_BUFF).writeInt(buffToGive);
		if (petTameness != 0)
			lew.writeByte(QuestBehaviors.REWARD_PET_TAMENESS).writeShort(petTameness);
		if (petSkill != 0)
			lew.writeByte(QuestBehaviors.REWARD_PET_SKILL).writeShort(petSkill);
		if (minLevel != 0)
			lew.writeByte(QuestBehaviors.MIN_LEVEL).writeShort(minLevel);
		if (fame != 0)
			lew.writeByte(QuestBehaviors.FAME).writeShort(fame);
		if (endDate != 0)
			lew.writeByte(QuestBehaviors.QUEST_END_DATE).writeInt(endDate);
		if (repeatInterval != -1)
			lew.writeByte(QuestBehaviors.REPEAT_INTERVAL).writeInt(repeatInterval);
	}
}
