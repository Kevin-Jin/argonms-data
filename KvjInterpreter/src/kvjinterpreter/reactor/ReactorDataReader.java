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

import kvjinterpreter.DataReader;
import kvjinterpreter.LittleEndianReader;
import kvjinterpreter.reactor.structure.State;

public class ReactorDataReader extends DataReader {
	private static final byte
		LINK = 1,
		HIT_EVENT = 2,
		ITEM_EVENT = 3
	;
	
	private MapleReactor reactor;
	private LittleEndianReader reader;
	
	public WzType getWzType() {
		return WzType.REACTOR;
	}
	
	public void initialize(int reactorid, LittleEndianReader reader) {
		this.reactor = new MapleReactor(reactorid);
		this.reader = reader;
	}
	
	public MapleReactor doWork() {
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case LINK:
					reactor.setLink(reader.readInt());
					break;
				case HIT_EVENT:
					processHitEvent();
					break;
				case ITEM_EVENT:
					processItemEvent();
					break;
			}
		}
		return reactor;
	}
	
	private State processHitEvent() {
		int stateid = reader.readInt();
		State s = new State();
		s.setType(reader.readInt());
		s.setNextState(reader.readInt());
		reactor.addState(stateid, s);
		return s;
	}
	
	private void processItemEvent() {
		State s = processHitEvent();
		s.setItem(reader.readInt(), reader.readInt());
		s.setLt(reader.readInt(), reader.readInt());
		s.setRb(reader.readInt(), reader.readInt());
	}
}
