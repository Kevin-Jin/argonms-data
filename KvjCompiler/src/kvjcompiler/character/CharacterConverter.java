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
package kvjcompiler.character;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.item.ItemConverter;

public class CharacterConverter extends Converter {
	private static final byte
		WHOLE_PRICE = 1,
		SLOT_MAX = 2,
		IS_TRADE_BLOCKED = 3,
		IS_ONE_ONLY = 4,
		IS_QUEST_ITEM = 5,
		BONUS_STAT = 6,
		SUMMON = 7,
		SUCCESS = 8,
		CURSED = 9,
		CASH = 10,
		OPERATING_HOURS = 11,
		SKILL = 12,
		UNIT_PRICE = 13,
		REQ_STAT = 14,
		UPGRADE_SLOTS = 15,
		SCROLL_REQUIREMENTS = 16,
		ITEM_EFFECT = 17,
		TRIGGER_ITEM = 18,
		MESO_VALUE = 19
	;

	public String getWzName() {
		return "Character.wz";
	}

	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		traverseBlock(nestedPath);
	}

	protected void handleProperty(String nestedPath, String value) throws IOException {
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("price")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, WHOLE_PRICE).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("cash")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, CASH).toArray());
			} else if (dirs[1].equals("tuc")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE, UPGRADE_SLOTS).writeByte(Byte.parseByte(value)).toArray());
			} else {
				if (dirs[1].length() > 2) {
					String prefix = dirs[1].substring(0, 3);
					if (prefix.equals("inc") || prefix.equals("req")) {
						LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + Size.BYTE + Size.SHORT, prefix.equals("inc") ? BONUS_STAT : REQ_STAT);
						byte stat = ItemConverter.getStat(dirs[1].substring(3, dirs[1].length()));
						if (stat != -1) {
							lew.writeByte(stat);
							lew.writeShort(Short.parseShort(value));
							fos.write(lew.toArray());
						}
					}
				}
			}
		}
	}
}
