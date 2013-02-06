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

package kvjcompiler.item.structure;

import java.util.ArrayList;
import java.util.List;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class ScrollRequirements implements IStructure {
	private final List<Integer> reqs;

	public ScrollRequirements() {
		reqs = new ArrayList<Integer>();
	}

	@Override
	public void setProperty(String key, String value) {
		reqs.add(Integer.valueOf(value));
	}

	@Override
	public int size() {
		return Size.INT + Size.INT * reqs.size();
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(reqs.size());
		for (Integer i : reqs)
			lew.writeInt(i.intValue());
	}
}
