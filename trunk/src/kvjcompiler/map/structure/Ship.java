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
public class Ship implements IStructure {
	private String shipObj;
	private byte shipKind;

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("shipObj")) {
			this.shipObj = value;
		} else if (key.equals("shipKind")) {
			this.shipKind = Byte.parseByte(value);
		}
	}

	@Override
	public int size() {
		int size = shipObj.length() + 1; //shipObj
		size += Size.BYTE; //shipKind

		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeNullTerminatedString(shipObj);
		lew.writeByte(shipKind);
	}
}
