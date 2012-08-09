/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010-2012  GoldenKevin
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

package kvjcompiler.item.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class PetCommand implements IStructure {
	private final byte command;
	private int prob;
	private int expInc;

	public PetCommand(byte command) {
		this.command = command;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("prob")) {
			prob = Integer.parseInt(value);
		} else if (key.equals("inc")) {
			expInc = Integer.parseInt(value);
		}
	}

	@Override
	public int size() {
		return (Size.BYTE + 2 * Size.INT);
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeByte(command);
		lew.writeInt(prob);
		lew.writeInt(expInc);
	}
}
