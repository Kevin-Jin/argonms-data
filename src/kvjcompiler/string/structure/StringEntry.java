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

package kvjcompiler.string.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.string.StringConverter;

/**
 *
 * @author GoldenKevin
 */
public class StringEntry implements IStructure {
	private int entryid;
	
	private String name;
	private String mapName;
	private String streetName;
	private String msg;
	
	public StringEntry(int id) {
		this.entryid = id;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("name")) {
			this.name = value;
		} else if (key.equals("mapName")) {
			this.mapName = value;
		} else if (key.equals("streetName")) {
			this.streetName = value;
		} else if (key.equals("msg")) {
			this.msg = value;
		}/* else {
			System.out.println("WARNING: Unhandled property " + key + " in entry " + entryid + ".");
		}*/
	}
	
	public int size() {
		int size = Size.INT;
		if (name != null) size += name.length() + Size.BYTE + Size.BYTE;
		if (mapName != null) size += mapName.length() + Size.BYTE + Size.BYTE;
		if (streetName != null) size += streetName.length() + Size.BYTE + Size.BYTE;
		if (msg != null) size += msg.length() + Size.BYTE + Size.BYTE;
		return (size == Size.INT) ? 0 : size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(entryid);
		if (name != null) lew.writeByte(StringConverter.NAME).writeNullTerminatedString(name);
		if (mapName != null) lew.writeByte(StringConverter.MAP_NAME).writeNullTerminatedString(mapName);
		if (streetName != null) lew.writeByte(StringConverter.STREET_NAME).writeNullTerminatedString(streetName);
		if (msg != null) lew.writeByte(StringConverter.MSG).writeNullTerminatedString(msg);
	}
}
