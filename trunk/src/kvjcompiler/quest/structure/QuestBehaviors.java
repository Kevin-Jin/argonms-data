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

		ITEM_PROP = 5,
		ITEM_GENDER = 6,
		ITEM_JOB = 7,
		ITEM_DATE_EXPIRE = 8,
		ITEM_PERIOD = 9,
		END_ITEM = 10,

		REWARD_EXP_GAIN = 11,
		REWARD_NEXT_QUEST = 12,
		REWARD_MESOS = 13,
		REWARD_BUFF = 14,
		REWARD_PET_TAMENESS = 15,
		REWARD_PET_SKILL = 16
	;

	private int questId;
	private QuestBehavior start, complete;

	public QuestBehaviors(int questId) {
		this.questId = questId;
	}

	public void setType(String key, QuestBehavior q) {
		if (key.equals("0")) //(actions to do/requirements to check) upon quest start
			start = q;
		else if (key.equals("1")) //(actions to do/requirements to check) upon quest completion
			complete = q;
	}

	public void setProperty(String key, String value) {
		
	}

	public int size() {
		int size = Size.INT; //quest Id
		size += start.size(); //start
		size += Size.BYTE; //delimiter b/w start & complete
		size += complete.size(); //complete
		size += Size.BYTE; //delimiter w/ next quest
		return size;
	}

	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(questId);
		start.writeBytes(lew);
		lew.writeByte(END_BEHAVIOR);
		complete.writeBytes(lew);
		lew.writeByte(END_BEHAVIOR);
	}
}
