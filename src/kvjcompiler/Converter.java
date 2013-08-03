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

package kvjcompiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.cashshop.EtcConverter;
import kvjcompiler.character.CharacterConverter;
import kvjcompiler.item.ItemConverter;
import kvjcompiler.map.MapConverter;
import kvjcompiler.mob.MobConverter;
import kvjcompiler.npc.NpcConverter;
import kvjcompiler.quest.QuestConverter;
import kvjcompiler.reactor.ReactorConverter;
import kvjcompiler.skill.SkillConverter;
import kvjcompiler.string.StringConverter;

//TODO - try replacing nestedPath String with ArrayList<String>
//[and replace "String[] dirs = nestedPath.split("/")" with "String[] dirs = nestedPath.toArray()"]
//to see if it improves performance.
//Be careful because ArrayList is not immutable, so added elements have to be removed after processing.
//Simply remove the last element from the list to achieve this.
/**
 *
 * @author GoldenKevin
 */
public abstract class Converter {
	protected OutputStream fos;
	protected XMLStreamReader r;

	public abstract String getWzName();

	public void compile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		startCompile(outPath, internalPath, imgName, r);
		finalizeCompile(internalPath, imgName);
	}

	protected void startCompile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		System.err.print("Building " + internalPath + imgName + "...\t");

		if (r.getEventType() != XMLStreamReader.START_DOCUMENT)
			throw new IllegalStateException("ERROR: Received an XML that has already been partially read.");

		r.next();
		if (DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || !r.getAttributeValue(0).equals(imgName))
			throw new IllegalStateException("ERROR: Received a non-WZ XML file.");

		File binDir = new File(outPath + getWzName() + File.separatorChar + internalPath);
		if (!binDir.exists())
			if (!binDir.mkdirs())
				throw new IllegalStateException("ERROR: Could not create compiled directory " + binDir.getAbsolutePath());
		this.fos = new BufferedOutputStream(new FileOutputStream(binDir.getAbsolutePath() + File.separatorChar + imgName + ".kvj"));
		this.r = r;
		traverseBlock("");
	}

	protected void finalizeCompile(String internalPath, String imgName) throws IOException, XMLStreamException {
		try {
			if (r.getEventType() != XMLStreamReader.END_ELEMENT || DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || r.next() != XMLStreamReader.END_DOCUMENT)
				throw new IllegalStateException("ERROR: End of " + internalPath + imgName + " not yet reached.");

			System.out.println(getWzName() + File.separatorChar + internalPath + imgName + " done.");
			System.err.println("Complete.");
		} finally {
			if (fos != null) {
				this.fos.close();
				this.fos = null;
			}
			if (r != null) {
				this.r.close();
				this.r = null;
			}
		}
	}

	protected int traverseBlock(String nestedPath) throws XMLStreamException, IOException {
		//System.out.println("DEBUG: Entering " + nestedPath);
		DataType type;
		String key, value;

		int event = r.getEventType();
		for (int nestedLevel = 1; nestedLevel > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				nestedLevel++;
				type = DataType.getFromString(r.getLocalName());
				key = r.getAttributeValue(0);

				if (type.isDirectory()) {
					handleDir((nestedPath.length() == 0 ? "" : nestedPath + '/') + key);
				} else {
					value = r.getAttributeValue(1);
					handleProperty((nestedPath.length() == 0 ? "" : nestedPath + '/') + key, value);
				}
				event = r.getEventType();
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				nestedLevel--;
				type = DataType.getFromString(r.getLocalName());

				if (type.isDirectory()) {

				}
			}
		}
		return event;
	}

	protected abstract void handleDir(String nestedPath) throws XMLStreamException, IOException;
	protected abstract void handleProperty(String nestedPath, String value) throws IOException;

	public static boolean isNumber(String str) {
		char ch;
		for (int i = str.length() - 1; i >= 0; i--) {
			ch = str.charAt(i);
			if (ch < '0' || ch > '9')
				return false;
		}
		return true;
	}

	public static Converter getConverter(String source) {
		if (source.equals("Character.wz"))
			return new CharacterConverter();
		else if (source.equals("Map.wz"))
			return new MapConverter();
		else if (source.equals("Mob.wz"))
			return new MobConverter();
		else if (source.equals("Reactor.wz"))
			return new ReactorConverter();
		else if (source.equals("Item.wz"))
			return new ItemConverter();
		else if (source.equals("String.wz"))
			return new StringConverter();
		else if (source.equals("Skill.wz"))
			return new SkillConverter();
		else if (source.equals("Quest.wz"))
			return new QuestConverter();
		else if (source.equals("Npc.wz"))
			return new NpcConverter();
		else if (source.equals("Etc.wz"))
			return new EtcConverter();
		return null;
	}
}