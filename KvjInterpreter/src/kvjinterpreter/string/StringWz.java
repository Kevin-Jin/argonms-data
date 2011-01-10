/*
 *  Sample interpreter for data files compiled from XML using KvJ
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
package kvjinterpreter.string;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import kvjinterpreter.string.structure.StringEntry;

public class StringWz {
	private String name;
	private Map<Integer, StringEntry> entries;
	
	public StringWz(String img) {
		this.name = img;
		this.entries = new TreeMap<Integer, StringEntry>();
	}
	
	public void add(int id, StringEntry e) {
		entries.put(Integer.valueOf(id), e);
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("String.wz/").append(name).append(".img\n---------------");
		if (!entries.isEmpty())
			ret.append('\n').append(entries.size()).append(" entries:");
		for (Entry<Integer, StringEntry> s : entries.entrySet())
			ret.append("\n\t").append(s.getKey()).append(": ").append(s.getValue());
		return ret.toString();
	}
}
