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

package kvjcompiler.npc;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class NpcConverter extends Converter {
	private static final byte
		SCRIPT_NAME = 1,
		TRUNK_PUT = 2,
		TRUNK_GET = 3
	;

	@Override
	public String getWzName() {
		return "Npc.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		traverseBlock(nestedPath);
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("script")) {
				fos.write(new LittleEndianWriter(Size.HEADER + value.length() + Size.BYTE, SCRIPT_NAME).writeNullTerminatedString(value).toArray());
			} else if (dirs[1].equals("trunkPut")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, TRUNK_PUT).writeInt(Integer.parseInt(value)).toArray());
			} else if(dirs[1].equals("trunkGet")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, TRUNK_GET).writeInt(Integer.parseInt(value)).toArray());
			}
		}
	}
}
