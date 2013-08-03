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

package kvjcompiler.reactor;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.reactor.structure.*;

/**
 *
 * @author GoldenKevin
 */
public class ReactorConverter extends Converter {
	private static final byte
		LINK = 1,
		HIT_EVENT = 2,
		ITEM_EVENT = 3,
		SCRIPT_NAME = 4
	;

	@Override
	public String getWzName() {
		return "Reactor.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		String[] dirs = nestedPath.split("/");
		if (!dirs[0].equals("info")) {
			Event e;
			LittleEndianWriter lew;
			for (int open1 = 1, event, open; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					if (r.getLocalName().equals("imgdir") && r.getAttributeValue(0).equals("0")) {
						e = new Event(Byte.parseByte(dirs[0]));
						for (open = 1; open > 0;) {
							event = r.next();
							if (event == XMLStreamReader.START_ELEMENT) {
								open++;
								e.setProperty(r.getAttributeValue(0), r.getLocalName().equals("vector") ?
										r.getAttributeValue(1) + ',' + r.getAttributeValue(2) : r.getAttributeValue(1));
							}
							if (event == XMLStreamReader.END_ELEMENT) {
								open--;
							}
						}
						lew = new LittleEndianWriter(Size.HEADER + e.size(), e.isItemEvent() ? ITEM_EVENT : HIT_EVENT);
						e.writeBytes(lew);
						fos.write(lew.toArray());
					}
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return;
		}
		traverseBlock(nestedPath);
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
		//System.out.println("DEBUG: Handling " + nestedPath);
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("link")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, LINK).writeInt(Integer.parseInt(value)).toArray());
			}
		} else if (dirs[0].equals("action")) {
			fos.write(new LittleEndianWriter(Size.HEADER + value.length() + Size.BOOL, SCRIPT_NAME).writeNullTerminatedString(value).toArray());
		}
	}
}
