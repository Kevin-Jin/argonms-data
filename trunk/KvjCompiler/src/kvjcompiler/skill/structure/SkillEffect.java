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
package kvjcompiler.skill.structure;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import kvjcompiler.Converter;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.skill.SkillConverter;

public class SkillEffect implements IStructure {
	private Map<Byte, Integer> intProps;
	private Map<Byte, Short> shortProps;
	private Map<Byte, Byte> byteProps;
	public static int[] lawl = new int[48];
	private Point lt;
	private Point rb;

	public SkillEffect() {
		this.intProps = new TreeMap<Byte, Integer>();
		this.shortProps = new TreeMap<Byte, Short>();
		this.byteProps = new TreeMap<Byte, Byte>();
	}

	public void setProperty(String key, String value) {
		if (key.equals("mpCon")) {
			shortProps.put(Byte.valueOf(SkillConverter.MP_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("hpCon")) {
			shortProps.put(Byte.valueOf(SkillConverter.HP_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("time")) {
			intProps.put(Byte.valueOf(SkillConverter.DURATION), Integer.valueOf(Integer.parseInt(value) * 1000));
		} else if (key.equals("x")) { //grr, mobskills has one with a value of 50000. that would work with unsigned short, but java doesn't have that...
			intProps.put(Byte.valueOf(SkillConverter.X), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("y")) {
			intProps.put(Byte.valueOf(SkillConverter.Y), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("z")) {
			if (Converter.isNumber(value))
				intProps.put(Byte.valueOf(SkillConverter.Z), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("damage")) {
			shortProps.put(Byte.valueOf(SkillConverter.DAMAGE), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("lt")) {
			int comma = value.indexOf(',');
			lt = new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())));
		} else if (key.equals("rb")) {
			int comma = value.indexOf(',');
			rb = new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())));
		} else if (key.equals("mobCount")) {
			byteProps.put(Byte.valueOf(SkillConverter.MOB_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("prop")) {
			shortProps.put(Byte.valueOf(SkillConverter.PROP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mastery")) {
			byteProps.put(Byte.valueOf(SkillConverter.MASTERY), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("cooltime") || key.equals("interval")) {
			shortProps.put(Byte.valueOf(SkillConverter.COOLTIME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("range")) {
			shortProps.put(Byte.valueOf(SkillConverter.MASTERY), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("pad")) {
			shortProps.put(Byte.valueOf(SkillConverter.WATK), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("pdd")) {
			shortProps.put(Byte.valueOf(SkillConverter.WDEF), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mad")) {
			shortProps.put(Byte.valueOf(SkillConverter.MATK), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mdd")) {
			shortProps.put(Byte.valueOf(SkillConverter.MDEF), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("acc")) {
			shortProps.put(Byte.valueOf(SkillConverter.ACC), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("eva")) {
			shortProps.put(Byte.valueOf(SkillConverter.AVOID), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("hp")) {
			shortProps.put(Byte.valueOf(SkillConverter.HP_BONUS), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mp")) {
			shortProps.put(Byte.valueOf(SkillConverter.MP_BONUS), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("speed")) {
			shortProps.put(Byte.valueOf(SkillConverter.SPEED), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("jump")) {
			shortProps.put(Byte.valueOf(SkillConverter.JUMP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("attackCount")) {
			byteProps.put(Byte.valueOf(SkillConverter.ATTACK_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("bulletCount")) {
			byteProps.put(Byte.valueOf(SkillConverter.BULLET_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("itemCon")) {
			intProps.put(Byte.valueOf(SkillConverter.ITEM_CONSUME), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("itemConNo")) {
			byteProps.put(Byte.valueOf(SkillConverter.ITEM_CONSUME_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("bulletConsume")) {
			shortProps.put(Byte.valueOf(SkillConverter.BULLET_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("moneyCon")) {
			shortProps.put(Byte.valueOf(SkillConverter.MONEY_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("morph")) {
			intProps.put(Byte.valueOf(SkillConverter.MORPH), Integer.valueOf(Integer.parseInt(value)));
		}
	}

	public int size() {
		int sum = 0;
		sum += (intProps.size() * (Size.INT + Size.BYTE));
		sum += (shortProps.size() * (Size.SHORT + Size.BYTE));
		sum += (byteProps.size() * (Size.BYTE + Size.BYTE));
		if (lt != null)
			sum += Size.BYTE + 2 * Size.SHORT;
		if (rb != null)
			sum += Size.BYTE + 2 * Size.SHORT;
		return sum;
	}

	public void writeBytes(LittleEndianWriter lew) {
		for (Entry<Byte, Integer> entry : intProps.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeInt(entry.getValue().intValue());
		}
		for (Entry<Byte, Short> entry : shortProps.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeShort(entry.getValue().shortValue());
		}
		for (Entry<Byte, Byte> entry : byteProps.entrySet()) {
			lew.writeByte(entry.getKey().byteValue());
			lew.writeByte(entry.getValue().byteValue());
		}
		if (lt != null)
			lew.writeByte(SkillConverter.LT).writeShort((short) lt.x).writeShort((short) lt.y);
		if (rb != null)
			lew.writeByte(SkillConverter.RB).writeShort((short) rb.x).writeShort((short) rb.y);
	}
}
