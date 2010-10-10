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

public class Life implements IStructure {
	private int lifeid;
	private char type;
	private int id;
	private int x;
	private int y;
	private int mobTime;
	private boolean f;
	private boolean hide;
	private int fh;
	private int cy;
	private int rx0;
	private int rx1;
	
	public Life(int id) {
		lifeid = id;
	}
	
	/* known unhandleds:
	 * int life/info (in 103000002)
	 * int life/team
	 * string life/limitedname
	 */
	public void setProperty(String key, String value) {
		if (key.equals("type")) {
			this.type = value.charAt(0);
		} else if (key.equals("id")) {
			this.id = Integer.parseInt(value);
		} else if (key.equals("x")) {
			this.x = Integer.parseInt(value);
		} else if (key.equals("y")) {
			this.y = Integer.parseInt(value);
		} else if (key.equals("mobTime")) {
			this.mobTime = Integer.parseInt(value);
		} else if (key.equals("f")) {
			this.f = Integer.parseInt(value) > 0;
		} else if (key.equals("hide")) {
			this.hide = Integer.parseInt(value) > 0;
		} else if (key.equals("fh")) {
			this.fh = Integer.parseInt(value);
		} else if (key.equals("cy")) {
			this.cy = Integer.parseInt(value);
		} else if (key.equals("rx0")) {
			this.rx0 = Integer.parseInt(value);
		} else if (key.equals("rx1")) {
			this.rx1 = Integer.parseInt(value);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in life.");
		}
	}
	
	public int size() {
		int size = Size.INT; //lifeid
		size += Size.CHAR; //type
		size += Size.INT; //id
		size += Size.INT; //x
		size += Size.INT; //y
		size += Size.INT; //mobTime
		size += Size.BOOL; //f
		size += Size.BOOL; //hide
		size += Size.INT; //fh
		size += Size.INT; //cy
		size += Size.INT; //rx0
		size += Size.INT; //rx1
		
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(lifeid);
		lew.writeChar(type);
		lew.writeInt(id);
		lew.writeInt(x);
		lew.writeInt(y);
		lew.writeInt(mobTime);
		lew.writeBool(f);
		lew.writeBool(hide);
		lew.writeInt(fh);
		lew.writeInt(cy);
		lew.writeInt(rx0);
		lew.writeInt(rx1);
	}
}
