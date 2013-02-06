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
import kvjcompiler.quest.structure.QuestAction;
import kvjcompiler.quest.structure.QuestBehaviors;
import kvjcompiler.quest.structure.QuestSkillAction;

/**
 *
 * @author GoldenKevin
 */
public class QuestActionsConverter extends Converter {
	private QuestAction a;

	@Override
	public String getWzName() {
		return "Quest.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		QuestBehaviors group = new QuestBehaviors(Short.parseShort(r.getAttributeValue(0)));
		LittleEndianWriter lew;
		for (int open1 = 1, event, open; open1 > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open1++;
				a = new QuestAction();
				group.setType(r.getAttributeValue(0), a);
				for (open = 1; open > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open++;
						int[] ret = QuestConverter.convertCommon(r, a);
						event = ret[0];
						if (ret[1] == 0) {
							String key = r.getAttributeValue(0);
							if (key.equals("skill")) {
								event = handleSkills();
							} else {
								a.setProperty(key, r.getAttributeValue(1));
							}
						}
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
		lew = new LittleEndianWriter(Size.HEADER + group.size(), QuestConverter.QUEST_ACTION);
		group.writeBytes(lew);
		fos.write(lew.toArray());
	}

	private int handleSkills() throws XMLStreamException {
		int event = r.getEventType();
		for (int skill = 1; skill > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				skill++;
				QuestSkillAction s = new QuestSkillAction();
				for (int skillProp = 1; skillProp > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						skillProp++;
						String key = r.getAttributeValue(0);
						if (key.equals("job")) {
							for (int job = 1; job > 0;) {
								event = r.next();
								if (event == XMLStreamReader.START_ELEMENT) {
									s.addJob(Short.parseShort(r.getAttributeValue(1)));
									job++;
								}
								if (event == XMLStreamReader.END_ELEMENT) {
									job--;
								}
							}
						} else if (key.equals("map")) {
							for (int map = 1; map > 0;) {
								event = r.next();
								if (event == XMLStreamReader.START_ELEMENT) {
									map++;
								}
								if (event == XMLStreamReader.END_ELEMENT) {
									map--;
								}
							}
						} else {
							s.setProperty(key, r.getAttributeValue(1));
						}
					}
					if (event == XMLStreamReader.END_ELEMENT) {
						skillProp--;
					}
				}
				a.addSkill(s);
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				skill--;
			}
		}
		return event;
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}
}
