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
import kvjcompiler.quest.structure.QuestBehavior;
import kvjcompiler.quest.structure.QuestItem;
import kvjcompiler.quest.structure.QuestQuest;

/**
 *
 * @author GoldenKevin
 */
public class QuestConverter extends Converter {
	public static final byte
		QUEST_ACTION = 1,
		QUEST_CHECK = 2,
		QUEST_INFO = 3
	;

	@Override
	public String getWzName() {
		return "Quest.wz";
	}

	@Override
	public void compile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		//don't worry about not using a singleton, we're only instantiating each once (only one copy of each file)
		if (imgName.equals("Act.img"))
			new QuestActionsConverter().compile(outPath, internalPath, imgName, r);
		else if (imgName.equals("Check.img"))
			new QuestRequirementsConverter().compile(outPath, internalPath, imgName, r);
		else if (imgName.equals("QuestInfo.img"))
			new QuestInfoConverter().compile(outPath, internalPath, imgName, r);
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {

	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}

	public static int[] convertCommon(XMLStreamReader r, QuestBehavior q) throws XMLStreamException {
		int event = r.getEventType();
		String key = r.getAttributeValue(0);
		if (key.equals("item")) {
			for (int item = 1; item > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					item++;
					QuestItem qItem = new QuestItem();
					for (int itemProp = 1; itemProp > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							itemProp++;
							qItem.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						}
						if (event == XMLStreamReader.END_ELEMENT) {
							itemProp--;
						}
					}
					q.addItem(qItem);
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					item--;
				}
			}
		} else if (key.equals("pop")) {
			q.setFame(Short.parseShort(r.getAttributeValue(1)));
		} else if (key.equals("npc")) {

		} else if (key.equals("lvmin")) {
			//quest 4900 start requirements check has a lvmin of 100200 for some reason. T.T
			q.setMinLevel((short) Math.min(Integer.parseInt(r.getAttributeValue(1)), 200));
		} else if (key.equals("quest")) {
			for (int quest = 1; quest > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					quest++;
					QuestQuest qu = new QuestQuest(); //funny name, no?
					for (int questProp = 1; questProp > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							questProp++;
							qu.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						}
						if (event == XMLStreamReader.END_ELEMENT) {
							questProp--;
						}
					}
					q.addQuest(qu);
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					quest--;
				}
			}
		} else if (key.equals("job")) {
			for (int job = 1; job > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					job++;
					q.addJob(Short.parseShort(r.getAttributeValue(1)));
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					job--;
				}
			}
		} else if (key.equals("interval")) {
			q.setRepeatInterval(Integer.parseInt(r.getAttributeValue(1)));
		} else if (key.equals("end")) {
			q.setEndDate(Integer.parseInt(r.getAttributeValue(1)));
		//conversation stuff. we don't care about them, but we still have to skip them...
		} else if (isNumber(key)) {

		} else {
			return new int[] { event, 0 };
		}
		return new int[] { event, 1 };
	}
}
