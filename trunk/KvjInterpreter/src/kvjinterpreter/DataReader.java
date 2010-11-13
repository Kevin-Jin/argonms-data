/*
 *  Sample interpreter for data files compiled from XML using KvJ
 *  Copyright (C) 2010  GoldenKevin
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
package kvjinterpreter;

import kvjinterpreter.map.MapDataReader;
import kvjinterpreter.mob.MobDataReader;
import kvjinterpreter.reactor.ReactorDataReader;

public abstract class DataReader {
	public abstract WzType getWzType();
	public abstract void initialize(int id, LittleEndianReader reader);
	public abstract Object doWork();
	
	public static DataReader getReader(String source) {
		if (source.equals("Map.wz")) {
			return new MapDataReader();
		} else if (source.equals("Mob.wz")) {
			return new MobDataReader();
		} else if (source.equals("Reactor.wz")) {
			return new ReactorDataReader();
		}
		return null;
	}
	
	public enum WzType {
		MAP, MOB, REACTOR
	}
}
