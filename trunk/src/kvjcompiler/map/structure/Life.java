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
public class Life implements IStructure {
	private final int lifeid;
	private char type;
	private int id;
	private short x;
	private short y;
	private int mobTime;
	private boolean f;
	private boolean hide;
	private short fh;
	private short cy;
	private short rx0;
	private short rx1;

	public Life(int id) {
		lifeid = id;
	}

	/* known unhandleds:
	 * int life/info (in 103000002)
	 * int life/team
	 * string life/limitedname
	 */
	@Override
	public void setProperty(String key, String value) {
		if (key.equals("type")) {
			this.type = value.charAt(0);
		} else if (key.equals("id")) {
			this.id = Integer.parseInt(value);
		} else if (key.equals("x")) {
			this.x = Short.parseShort(value);
		} else if (key.equals("y")) {
			this.y = Short.parseShort(value);
		} else if (key.equals("mobTime")) {
			this.mobTime = Integer.parseInt(value);
		} else if (key.equals("f")) {
			this.f = Integer.parseInt(value) > 0;
		} else if (key.equals("hide")) {
			this.hide = Integer.parseInt(value) > 0;
		} else if (key.equals("fh")) {
			this.fh = Short.parseShort(value);
		} else if (key.equals("cy")) {
			this.cy = Short.parseShort(value);
		} else if (key.equals("rx0")) {
			this.rx0 = Short.parseShort(value);
		} else if (key.equals("rx1")) {
			this.rx1 = Short.parseShort(value);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in life " + lifeid + ".");
		}
	}

	@Override
	public int size() {
		int size = Size.INT; //lifeid
		size += Size.CHAR; //type
		size += Size.INT; //id
		size += Size.SHORT; //x
		size += Size.SHORT; //y
		size += Size.INT; //mobTime
		size += Size.BOOL; //f
		size += Size.BOOL; //hide
		size += Size.SHORT; //fh
		size += Size.SHORT; //cy
		size += Size.SHORT; //rx0
		size += Size.SHORT; //rx1

		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(lifeid);
		lew.writeChar(type);
		lew.writeInt(id);
		lew.writeShort(x);
		lew.writeShort(y);
		lew.writeInt(mobTime);
		lew.writeBool(f);
		lew.writeBool(hide);
		lew.writeShort(fh);
		lew.writeShort(cy);
		lew.writeShort(rx0);
		lew.writeShort(rx1);
	}
}
