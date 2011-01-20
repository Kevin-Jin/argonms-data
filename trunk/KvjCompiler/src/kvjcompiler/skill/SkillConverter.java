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
package kvjcompiler.skill;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.DataType;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.skill.structure.Skill;
import kvjcompiler.skill.structure.SkillEffect;

public class SkillConverter extends Converter {
	public String getWzName() {
		return "Skill.wz";
	}

	public static final byte //skill props
		NEXT_SKILL = 1,
		ELEM_ATTR = 2,
		IS_BUFF = 3,
		DELAY = 4,
		IS_CHARGE = 5,
		NEXT_LEVEL = 6
	;

	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		Skill s;
		SkillEffect e;
		LittleEndianWriter lew;
		byte level;
		String key, value;
		if (isNumber(nestedPath)) {
			s = new Skill(Integer.parseInt(nestedPath));
			for (int open2 = 1, event; open2 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open2++;
					key = r.getAttributeValue(0);
					if (key.equals("level")) {
						for (int open1 = 1; open1 > 0;) {
							event = r.next();
							if (event == XMLStreamReader.START_ELEMENT) {
								open1++;
								if (isNumber(r.getAttributeValue(0))) {
									level = Byte.parseByte(r.getAttributeValue(0));
									e = new SkillEffect();
									for (int open = 1; open > 0;) {
										event = r.next();
										if (event == XMLStreamReader.START_ELEMENT) {
											key = r.getAttributeValue(0);
											value = r.getAttributeValue(1);
											if (DataType.getFromString(r.getLocalName()).isPoint())
												value += ',' + r.getAttributeValue(1);
											e.setProperty(key, value);
											open++;
										}
										if (event == XMLStreamReader.END_ELEMENT) {
											open--;
										}
									}
									s.addLevel(level, e);
								}
							}
							if (event == XMLStreamReader.END_ELEMENT) {
								open1--;
							}
						}
					} else {
						/*if (r.getAttributeValue(0).equals("action")) {
							for (int open1 = 1; open1 > 0;) {
								event = r.next();
								if (event == XMLStreamReader.START_ELEMENT) {
									open1++;
									if (r.getAttributeValue(0).equals("0") && r.getAttributeValue(1).equals("alert2"))
										s.setProperty("action", null);
								}
								if (event == XMLStreamReader.END_ELEMENT) {
									open1--;
								}
							}
						} else*/ if (key.equals("effect")) {
							for (int open1 = 1; open1 > 0;) {
								event = r.next();
								if (event == XMLStreamReader.START_ELEMENT) {
									open1++;
									for (int open = 1; open > 0;) {
										event = r.next();
										if (event == XMLStreamReader.START_ELEMENT) {
											open++;
											if (r.getAttributeValue(0).equals("delay"))
												s.addDelay(Integer.parseInt(r.getAttributeValue(1)));
										}
										if (event == XMLStreamReader.END_ELEMENT) {
											open--;
										}
									}
								}
								if (event == XMLStreamReader.END_ELEMENT) {
									open1--;
								}
							}
						} else
							s.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
					}
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open2--;
				}
			}
			lew = new LittleEndianWriter(Size.HEADER + s.size(), NEXT_SKILL);
			s.writeBytes(lew);
			fos.write(lew.toArray());
		}
	}

	protected void handleProperty(String nestedPath, String value) throws IOException {
	}
}
