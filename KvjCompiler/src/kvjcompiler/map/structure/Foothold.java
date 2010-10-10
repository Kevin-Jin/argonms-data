/*
 *  KvJ Compiler for XML WZ data files
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
package kvjcompiler.map.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

public class Foothold implements IStructure {
	private int footholdid;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int prev;
	private int next;
	
	public Foothold(int id) {
		this.footholdid = id;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("x1")) {
			this.x1 = Integer.parseInt(value);
		} else if (key.equals("y1")) {
			this.y1 = Integer.parseInt(value);
		} else if (key.equals("x2")) {
			this.x2 = Integer.parseInt(value);
		} else if (key.equals("y2")) {
			this.y2 = Integer.parseInt(value);
		} else if (key.equals("prev")) {
			this.prev = Integer.parseInt(value);
		} else if (key.equals("next")) {
			this.next = Integer.parseInt(value);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in foothold.");
		}
	}
	
	public int size() {
		int size = Size.INT; //footholdid
		size += Size.INT; //x1
		size += Size.INT; //y1
		size += Size.INT; //x2
		size += Size.INT; //y2
		size += Size.INT; //prev
		size += Size.INT; //next
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(footholdid);
		lew.writeInt(x1);
		lew.writeInt(y1);
		lew.writeInt(x2);
		lew.writeInt(y2);
		lew.writeInt(prev);
		lew.writeInt(next);
	}
}
