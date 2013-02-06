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
import kvjcompiler.quest.QuestInfoConverter;

/**
 *
 * @author GoldenKevin
 */
public class QuestInfo implements IStructure {
	private final short questId;

	public QuestInfo(short questId) {
		this.questId = questId;
	}

	private String name;
	private boolean autoStart, autoPreComplete;

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("name")) {
			name = value;
		} else if (key.equals("autoStart")) {
			autoStart = Integer.parseInt(value) != 0;
		} else if (key.equals("autoPreComplete")) {
			autoPreComplete = Integer.parseInt(value) != 0;
		}
	}

	@Override
	public int size() {
		int size = Size.SHORT; //questid
		size += name.length() + 1; //name
		if (autoStart)
			size += Size.BYTE; //autoStart
		if (autoPreComplete)
			size += Size.BYTE; //autoPreComplete
		size += Size.BYTE; //end struct
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeShort(questId);
		lew.writeNullTerminatedString(name);
		if (autoStart)
			lew.writeByte(QuestInfoConverter.AUTO_START);
		if (autoPreComplete)
			lew.writeByte(QuestInfoConverter.AUTO_PRE_COMPLETE);
		lew.writeByte(QuestInfoConverter.END_QUEST_INFO);
	}
}
