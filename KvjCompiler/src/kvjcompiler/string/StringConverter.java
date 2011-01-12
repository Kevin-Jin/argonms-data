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
package kvjcompiler.string;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.string.structure.StringEntry;

public class StringConverter extends Converter {
	public String getWzName() {
		return "String.wz";
	}
	
	public static final byte
		NEXT = 1,
		NAME = 2,
		MAP_NAME = 3,
		STREET_NAME = 4,
		MSG = 5
	;
	
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		LittleEndianWriter lew;
		int size;
		if (isNumber(nestedPath)) {
			StringEntry e = new StringEntry(Integer.parseInt(nestedPath));
			for (int open = 1, event; open > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open++;
					e.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
				} else if (event == XMLStreamReader.END_ELEMENT) {
					open--;
				}
			}
			size = e.size();
			if (size != 0) {
				lew = new LittleEndianWriter(Size.HEADER + e.size(), NEXT);
				e.writeBytes(lew);
				fos.write(lew.toArray());
			}
		}
	}
	
	protected void handleProperty(String nestedPath, String value) throws IOException {
	}
}
