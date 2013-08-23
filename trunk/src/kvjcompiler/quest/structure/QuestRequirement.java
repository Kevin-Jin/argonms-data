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
public class QuestRequirement extends QuestBehavior {
	private final List<QuestSkillRequirement> skills;
	private final List<QuestMob> mobs;
	private final List<Integer> pets;
	private short maxLevel;
	private String startScript, endScript;
	private short petTamenessMin;
	private short reqMesos;
	private short mountTamenessMin;
	private short minPopulation, maxPopulation;

	public QuestRequirement() {
		skills = new ArrayList<QuestSkillRequirement>();
		mobs = new ArrayList<QuestMob>();
		pets = new ArrayList<Integer>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("lvmax")) {
			maxLevel = Short.parseShort(value);
		}/* else if (key.equals("start")) {

		}*/ else if (key.equals("startscript")) {
			startScript = value;
		} else if (key.equals("normalAutoStart")) {

		} else if (key.equals("pettamenessmin")) {
			petTamenessMin = Short.parseShort(value);
		}/* else if (key.equals("infoNumber")) {

		}*/ else if (key.equals("endmeso")) {
			reqMesos = Short.parseShort(value);
		} else if (key.equals("endscript")) {
			endScript = value;
		}/* else if (key.equals("petRecallLimit")) {

		} else if (key.equals("petAutoSpeakingLimit")) {

		} else if (key.equals("dayByDay")) {

		}*/ else if (key.equals("tamingmoblevelmin")) {
			mountTamenessMin = Short.parseShort(value);
		} else if (key.equals("worldmin")) {
			minPopulation = Short.parseShort(value);
		} else if (key.equals("worldmax")) {
			maxPopulation = Short.parseShort(value);
		}
	}

	public void addSkill(QuestSkillRequirement s) {
		skills.add(s);
	}

	public void addMob(QuestMob m) {
		mobs.add(m);
	}

	public void addPet(int itemId) {
		pets.add(Integer.valueOf(itemId));
	}

	@Override
	public int size() {
		int size = super.size();
		size += Size.BYTE; //amount of skills
		for (QuestSkillRequirement s : skills)
			size += s.size();
		size += Size.BYTE; //amount of mobs
		for (QuestMob m : mobs)
			size += m.size();
		size += Size.BYTE; //amount of pets
		size += pets.size() * Size.INT;
		if (maxLevel != 0)
			size += Size.BYTE + Size.SHORT;
		if (minLevel != 0)
			size += Size.BYTE + Size.SHORT;
		if (fame != 0)
			size += Size.BYTE + Size.SHORT;
		if (endDate != 0)
			size += Size.BYTE + Size.INT;
		if (startScript != null)
			size += Size.BYTE + startScript.length() + Size.BYTE;
		if (endScript != null)
			size += Size.BYTE + endScript.length() + Size.BYTE;
		if (repeatInterval != -1)
			size += Size.BYTE + Size.INT;
		if (petTamenessMin != 0)
			size += Size.BYTE + Size.SHORT;
		if (mountTamenessMin != 0)
			size += Size.BYTE + Size.SHORT;
		if (reqMesos != 0)
			size += Size.BYTE + Size.SHORT;
		if (minPopulation != 0)
			size += Size.BYTE + Size.SHORT;
		if (maxPopulation != 0)
			size += Size.BYTE + Size.SHORT;
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		super.writeBytes(lew);
		lew.writeByte((byte) skills.size());
		for (QuestSkillRequirement quest : skills)
			quest.writeBytes(lew);
		lew.writeByte((byte) mobs.size());
		for (QuestMob mob : mobs)
			mob.writeBytes(lew);
		lew.writeByte((byte) pets.size());
		for (Integer id : pets)
			lew.writeInt(id.intValue());
		if (maxLevel != 0)
			lew.writeByte(QuestBehaviors.MAX_LEVEL).writeShort(maxLevel);
		if (minLevel != 0)
			lew.writeByte(QuestBehaviors.MIN_LEVEL).writeShort(minLevel);
		if (fame != 0)
			lew.writeByte(QuestBehaviors.FAME).writeShort(fame);
		if (endDate != 0)
			lew.writeByte(QuestBehaviors.QUEST_END_DATE).writeInt(endDate);
		if (startScript != null)
			lew.writeByte(QuestBehaviors.START_SCRIPT).writeNullTerminatedString(startScript);
		if (endScript != null)
			lew.writeByte(QuestBehaviors.END_SCRIPT).writeNullTerminatedString(endScript);
		if (repeatInterval != -1)
			lew.writeByte(QuestBehaviors.REPEAT_INTERVAL).writeInt(repeatInterval);
		if (petTamenessMin != 0)
			lew.writeByte(QuestBehaviors.REQ_PET_TAMENESS).writeShort(petTamenessMin);
		if (mountTamenessMin != 0)
			lew.writeByte(QuestBehaviors.REQ_MOUNT_TAMENESS).writeShort(mountTamenessMin);
		if (reqMesos != 0)
			lew.writeByte(QuestBehaviors.REQ_MESOS).writeShort(reqMesos);
		if (minPopulation != 0)
			lew.writeByte(QuestBehaviors.MIN_POPULATION).writeShort(minPopulation);
		if (maxPopulation != 0)
			lew.writeByte(QuestBehaviors.MAX_POPULATION).writeShort(maxPopulation);
	}
}
