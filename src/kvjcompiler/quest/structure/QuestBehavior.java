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
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public abstract class QuestBehavior implements IStructure {
	private final List<QuestItem> items;
	private final List<QuestQuest> quests;
	protected short minLevel;
	protected short fame;
	protected int endDate;

	public QuestBehavior() {
		items = new ArrayList<QuestItem>();
		quests = new ArrayList<QuestQuest>();
	}

	public void addItem(byte index, QuestItem item) {
		items.add(item);
	}

	public void addQuest(byte index, QuestQuest qu) {
		quests.add(qu);
	}

	public void setMinLevel(short level) {
		this.minLevel = level;
	}

	public void setFame(short fame) {
		this.fame = fame;
	}

	public int size() {
		int size = Size.BYTE; //amount of items
		for (QuestItem item : items)
			size += item.size();
		size += Size.BYTE; //amount of quests
		for (QuestQuest qu : quests)
			size += qu.size();
		return size;
	}

	public void writeBytes(LittleEndianWriter lew) {
		lew.writeByte((byte) items.size());
		for (QuestItem item : items)
			item.writeBytes(lew);
		lew.writeByte((byte) quests.size());
		for (QuestQuest qu : quests)
			qu.writeBytes(lew);
	}

	public void setEndDate(int idate) {
		endDate = idate;
	}
}
