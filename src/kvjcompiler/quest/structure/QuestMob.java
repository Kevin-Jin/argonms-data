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
public class QuestMob implements IStructure {
	private int id;
	private short count;

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("id")) {
			id = Integer.parseInt(value);
		} else if (key.equals("count")) {
			count = Short.parseShort(value);
		}
	}

	@Override
	public int size() {
		return
			Size.INT //id
			+ Size.SHORT //count
		;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(id);
		lew.writeShort(count);
	}
}
