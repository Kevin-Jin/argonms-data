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
package kvjinterpreter.reactor;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import kvjinterpreter.reactor.structure.State;

public class MapleReactor {
	private int reactorid;
	private int link;
	private Map<Integer, State> states;
	
	public MapleReactor(int id) {
		this.reactorid = id;
		this.states = new TreeMap<Integer, State>();
	}
	
	public void setLink(int reactorid) {
		this.link = reactorid;
	}
	
	public void addState(int stateid, State s) {
		states.put(Integer.valueOf(stateid), s);
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("Reactor ").append(reactorid).append("\n---------------");
		if (link != 0)
			ret.append("\nLinked to ").append(link);
		
		//perhaps we load stats from link before doing states?
		
		if (!states.isEmpty())
			ret.append("\nStates:");
		for (Entry<Integer, State> s : states.entrySet())
			ret.append("\n\t").append(s.getKey()).append(": ").append(s.getValue());
		return ret.toString();
	}
}
