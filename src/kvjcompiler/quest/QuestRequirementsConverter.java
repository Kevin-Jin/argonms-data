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
import kvjcompiler.quest.structure.QuestBehaviors;
import kvjcompiler.quest.structure.QuestMob;
import kvjcompiler.quest.structure.QuestRequirement;
import kvjcompiler.quest.structure.QuestSkillRequirement;

/**
 *
 * @author GoldenKevin
 */
public class QuestRequirementsConverter extends Converter {
	@Override
	public String getWzName() {
		return "Quest.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		QuestBehaviors group = new QuestBehaviors(Short.parseShort(r.getAttributeValue(0)));
		QuestRequirement req;
		for (int open1 = 1, event, open; open1 > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open1++;
				req = new QuestRequirement();
				group.setType(r.getAttributeValue(0), req);
				for (open = 1; open > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open++;
						int[] ret = QuestConverter.convertCommon(r, req);
						event = ret[0];
						if (ret[1] == 0) {
							String key = r.getAttributeValue(0);
							if (key.equals("mob")) {
								for (int mob = 1; mob > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										mob++;
										QuestMob m = new QuestMob();
										for (int mobProp = 1; mobProp > 0;) {
											event = r.next();
											if (event == XMLStreamReader.START_ELEMENT) {
												mobProp++;
												m.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
											}
											if (event == XMLStreamReader.END_ELEMENT) {
												mobProp--;
											}
										}
										req.addMob(m);
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										mob--;
									}
								}
							} else if (key.equals("skill")) {
								for (int skill = 1; skill > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										skill++;
										QuestSkillRequirement s = new QuestSkillRequirement();
										for (int skillProp = 1; skillProp > 0;) {
											event = r.next();
											if (event == XMLStreamReader.START_ELEMENT) {
												skillProp++;
												s.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
											}
											if (event == XMLStreamReader.END_ELEMENT) {
												skillProp--;
											}
										}
										req.addSkill(s);
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										skill--;
									}
								}
							} else if (key.equals("pet")) {
								for (int pet = 1; pet > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										pet++;
										for (int petProp = 1; petProp > 0;) {
											event = r.next();
											if (event == XMLStreamReader.START_ELEMENT) {
												petProp++;
												if (r.getAttributeValue(0).equals("id"))
													req.addPet(Integer.parseInt(r.getAttributeValue(1)));
											}
											if (event == XMLStreamReader.END_ELEMENT) {
												petProp--;
											}
										}
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										pet--;
									}
								}
							} else if (key.equals("info")) { //what the heck is this???
								for (int info = 1; info > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										info++;
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										info--;
									}
								}
							} else if (key.equals("fieldEnter")) { //what the heck is this???
								for (int fieldEnter = 1; fieldEnter > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										fieldEnter++;
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										fieldEnter--;
									}
								}
							//conversation stuff. we don't care about them, but we still have to skip them...
							} else if (key.equals("stop")) {
								for (int choice = 1; choice > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										choice++;
										for (int msg = 1; msg > 0;) {
											event = r.next();
											if (event == XMLStreamReader.START_ELEMENT) {
												msg++;
											}
											if (event == XMLStreamReader.END_ELEMENT) {
												msg--;
											}
										}
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										choice--;
									}
								}
							} else if (key.equals("yes")) {
								for (int msg = 1; msg > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										msg++;
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										msg--;
									}
								}
							} else if (key.equals("no")) {
								for (int msg = 1; msg > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										msg++;
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										msg--;
									}
								}
							} else {
								req.setProperty(key, r.getAttributeValue(1));
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
		LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + group.size(), QuestConverter.QUEST_CHECK);
		group.writeBytes(lew);
		fos.write(lew.toArray());
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}
}
