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

public class Portal implements IStructure {
	private int portalid;
	private String pn; //portal name
	private int pt; //portal type
	private int x; //x
	private int y; //y
	private int tm; //target map
	private String tn; //target name
	private String script;
	
	public Portal(int portalid) {
		this.portalid = portalid;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("pn")) {
			this.pn = value;
		} else if (key.equals("pt")) {
			this.pt = Integer.valueOf(Integer.parseInt(value));
		} else if (key.equals("x")) {
			this.x = Integer.valueOf(Integer.parseInt(value));
		} else if (key.equals("y")) {
			this.y = Integer.valueOf(Integer.parseInt(value));
		} else if (key.equals("tm")) {
			this.tm = Integer.valueOf(Integer.parseInt(value));
		} else if (key.equals("tn")) {
			this.tn = value;
		} else if (key.equals("script")) {
			this.script = value;
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in portal " + portalid + ".");
		}
	}
	
	public int size() {
		int size = Size.INT; //portalid
		size += pn.length() + 1; //pn
		size += Size.INT; //pt
		size += Size.INT; //x
		size += Size.INT; //y
		size += Size.INT; //tm
		size += tn.length() + 1; //tn
		size += script != null ? script.length() + 1 : 1; //script
		
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(portalid);
		lew.writeNullTerminatedString(pn);
		lew.writeInt(pt);
		lew.writeInt(x);
		lew.writeInt(y);
		lew.writeInt(tm);
		lew.writeNullTerminatedString(tn);
		lew.writeNullTerminatedString(script != null ? script : "");
	}
}