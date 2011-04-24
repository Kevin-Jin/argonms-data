/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010, 2011  GoldenKevin
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

	public QuestRequirement() {
		skills = new ArrayList<QuestSkillRequirement>();
		mobs = new ArrayList<QuestMob>();
		pets = new ArrayList<Integer>();
	}

	public void setProperty(String key, String value) {
		if (key.equals("lvmax")) {
			maxLevel = Short.parseShort(value);
		}/* else if (key.equals("start")) {
			
		} else if (key.equals("startscript")) {
			
		}*/ else if (key.equals("normalAutoStart")) {
			
		} else if (key.equals("pettamenessmin")) {
			
		}/* else if (key.equals("infoNumber")) {
			
		}*/ else if (key.equals("endmeso")) {
			
		}/* else if (key.equals("endscript")) {
			
		}*/ else if (key.equals("petRecallLimit")) {
			
		} else if (key.equals("petAutoSpeakingLimit")) {
			
		} else if (key.equals("dayByDay")) {
			
		} else if (key.equals("tamingmoblevelmin")) {
			
		} else if (key.equals("worldmin")) {
			
		} else if (key.equals("worldmax")) {
			
		}
	}

	public void addSkill(byte index, QuestSkillRequirement s) {
		skills.add(s);
	}

	public void addMob(byte index, QuestMob m) {
		mobs.add(m);
	}

	public void addPet(byte index, int itemId) {
		pets.add(Integer.valueOf(itemId));
	}

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
		return size;
	}

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
	}
}
