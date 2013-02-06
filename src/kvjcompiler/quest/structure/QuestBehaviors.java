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

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class QuestBehaviors implements IStructure {
	public static final byte
		END_BEHAVIOR = 0,
		MIN_LEVEL = 1,
		MAX_LEVEL = 2,
		FAME = 3,
		QUEST_END_DATE = 4,
		START_SCRIPT = 5,
		END_SCRIPT = 6,
		REPEAT_INTERVAL = 7,
		REQ_PET_TAMENESS = 8,
		REQ_MOUNT_TAMENESS = 9,
		REQ_MESOS = 10,
		MIN_POPULATION = 11,
		MAX_POPULATION = 12,

		ITEM_PROP = 13,
		ITEM_GENDER = 14,
		ITEM_JOB = 15,
		ITEM_DATE_EXPIRE = 16,
		ITEM_PERIOD = 17,
		END_ITEM = 18,

		REWARD_EXP_GAIN = 19,
		REWARD_NEXT_QUEST = 20,
		REWARD_MESOS = 21,
		REWARD_BUFF = 22,
		REWARD_PET_TAMENESS = 23,
		REWARD_PET_SKILL = 24
	;

	private final short questId;
	private QuestBehavior start, complete;

	public QuestBehaviors(short questId) {
		this.questId = questId;
	}

	public void setType(String key, QuestBehavior q) {
		if (key.equals("0")) //(actions to do/requirements to check) upon quest start
			start = q;
		else if (key.equals("1")) //(actions to do/requirements to check) upon quest completion
			complete = q;
	}

	@Override
	public void setProperty(String key, String value) {

	}

	@Override
	public int size() {
		int size = Size.SHORT; //quest Id
		size += start.size(); //start
		size += Size.BYTE; //delimiter b/w start & complete
		size += complete.size(); //complete
		size += Size.BYTE; //delimiter w/ next quest
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeShort(questId);
		start.writeBytes(lew);
		lew.writeByte(END_BEHAVIOR);
		complete.writeBytes(lew);
		lew.writeByte(END_BEHAVIOR);
	}
}
