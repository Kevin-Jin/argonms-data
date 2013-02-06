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
public class Portal implements IStructure {
	private final int portalid;
	private String pn; //portal name
	private byte pt; //portal type
	private short x; //x
	private short y; //y
	private int tm; //target map
	private String tn; //target name
	private String script;

	public Portal(int portalid) {
		this.portalid = portalid;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("pn")) {
			this.pn = value;
		} else if (key.equals("pt")) {
			this.pt = Byte.parseByte(value);
		} else if (key.equals("x")) {
			this.x = Short.parseShort(value);
		} else if (key.equals("y")) {
			this.y = Short.parseShort(value);
		} else if (key.equals("tm")) {
			this.tm = Integer.parseInt(value);
		} else if (key.equals("tn")) {
			this.tn = value;
		} else if (key.equals("script")) {
			this.script = value;
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in portal " + portalid + ".");
		}
	}

	@Override
	public int size() {
		int size = Size.INT; //portalid
		size += pn.length() + 1; //pn
		size += Size.BYTE; //pt
		size += Size.SHORT; //x
		size += Size.SHORT; //y
		size += Size.INT; //tm
		size += tn.length() + 1; //tn
		size += script != null ? script.length() + 1 : 1; //script

		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(portalid);
		lew.writeNullTerminatedString(pn);
		lew.writeByte(pt);
		lew.writeShort(x);
		lew.writeShort(y);
		lew.writeInt(tm);
		lew.writeNullTerminatedString(tn);
		lew.writeNullTerminatedString(script != null ? script : "");
	}
}