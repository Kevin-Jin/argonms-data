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

package kvjcompiler.quest;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.quest.structure.QuestInfo;

/**
 *
 * @author GoldenKevin
 */
public class QuestInfoConverter extends Converter {
	public static final byte
		END_QUEST_INFO = 0,
		AUTO_START = 1,
		AUTO_PRE_COMPLETE = 2
	;

	@Override
	public String getWzName() {
		return "Quest.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		QuestInfo i = new QuestInfo(Short.parseShort(r.getAttributeValue(0)));
		for (int open1 = 1, event, open; open1 > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open1++;
				if (r.getLocalName().equals("imgdir")) {
					for (open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
						}
						if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
				} else {
					i.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
				}
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				open1--;
			}
		}
		LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + i.size(), QuestConverter.QUEST_INFO);
		i.writeBytes(lew);
		fos.write(lew.toArray());
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}
}
