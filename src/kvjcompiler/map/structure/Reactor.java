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

package kvjcompiler.map.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class Reactor implements IStructure {
	private final int reactorid;
	private int id;
	private short x;
	private short y;
	private int reactorTime;
	private String name;

	public Reactor(int id) {
		this.reactorid = id;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("id")) {
			this.id = Integer.parseInt(value);
		} else if (key.equals("x")) {
			this.x = Short.parseShort(value);
		} else if (key.equals("y")) {
			this.y = Short.parseShort(value);
		} else if (key.equals("reactorTime")) {
			this.reactorTime = Integer.parseInt(value);
		} else if (key.equals("f")) {
			//this.f = Integer.parseInt(value) > 0;
		} else if (key.equals("name")) {
			this.name = value;
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in reactor " + reactorid + ".");
		}
	}

	@Override
	public int size() {
		int size = Size.INT; //reactorid
		size += Size.INT; //id
		size += Size.SHORT; //x
		size += Size.SHORT; //y
		size += Size.INT; //reactorTime
		size += name != null ? name.length() + 1 : 1; //name

		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(reactorid);
		lew.writeInt(id);
		lew.writeShort(x);
		lew.writeShort(y);
		lew.writeInt(reactorTime);
		lew.writeNullTerminatedString(name != null ? name : "");
	}
}
