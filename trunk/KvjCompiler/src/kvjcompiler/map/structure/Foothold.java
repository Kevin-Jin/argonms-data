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
package kvjcompiler.map.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

public class Foothold implements IStructure {
	private short x1;
	private short y1;
	private short x2;
	private short y2;
	private short prev;
	private short next;
	
	public Foothold() {
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("x1")) {
			this.x1 = Short.parseShort(value);
		} else if (key.equals("y1")) {
			this.y1 = Short.parseShort(value);
		} else if (key.equals("x2")) {
			this.x2 = Short.parseShort(value);
		} else if (key.equals("y2")) {
			this.y2 = Short.parseShort(value);
		} else if (key.equals("prev")) {
			this.prev = Short.parseShort(value);
		} else if (key.equals("next")) {
			this.next = Short.parseShort(value);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in foothold.");
		}
	}
	
	public int size() {
		int size = Size.SHORT; //x1
		size += Size.SHORT; //y1
		size += Size.SHORT; //x2
		size += Size.SHORT; //y2
		size += Size.SHORT; //prev
		size += Size.SHORT; //next
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeShort(x1);
		lew.writeShort(y1);
		lew.writeShort(x2);
		lew.writeShort(y2);
		lew.writeShort(prev);
		lew.writeShort(next);
	}
}
