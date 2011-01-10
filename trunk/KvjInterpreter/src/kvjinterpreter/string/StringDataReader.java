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

import kvjinterpreter.DataReader;
import kvjinterpreter.LittleEndianReader;
import kvjinterpreter.string.structure.StringEntry;

public class StringDataReader extends DataReader {
	private static final byte
		NEXT = 1,
		NAME = 2,
		MAP_NAME = 3,
		STREET_NAME = 4,
		MSG = 5
	;
	
	private StringWz collection;
	private LittleEndianReader reader;
	
	public WzType getWzType() {
		return WzType.STRING;
	}
	
	public void initialize(String img, LittleEndianReader reader) {
		this.collection = new StringWz(img);
		this.reader = reader;
	}
	
	public Object doWork() {
		StringEntry e = null;
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case NEXT:
					e = new StringEntry();
					collection.add(reader.readInt(), e);
					break;
				case NAME:
					e.setName(reader.readNullTerminatedString());
					break;
				case MAP_NAME:
					e.setMapName(reader.readNullTerminatedString());
					break;
				case STREET_NAME:
					e.setStreetName(reader.readNullTerminatedString());
					break;
				case MSG:
					e.setMessage(reader.readNullTerminatedString());
					break;
			}
		}
		return collection;
	}
}
