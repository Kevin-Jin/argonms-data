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

package kvjcompiler.skill.structure;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import kvjcompiler.Converter;
import kvjcompiler.Effects;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class SkillEffect implements IStructure {
	private final Map<Byte, Integer> intProps;
	private final Map<Byte, Short> shortProps;
	private final Map<Byte, Byte> byteProps;
	private final Map<Byte, Integer> summons;
	private Point lt;
	private Point rb;

	public SkillEffect() {
		this.intProps = new TreeMap<Byte, Integer>();
		this.shortProps = new TreeMap<Byte, Short>();
		this.byteProps = new TreeMap<Byte, Byte>();
		this.summons = new TreeMap<Byte, Integer>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("mpCon")) {
			shortProps.put(Byte.valueOf(Effects.MP_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("hpCon")) {
			shortProps.put(Byte.valueOf(Effects.HP_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("time")) {
			intProps.put(Byte.valueOf(Effects.DURATION), Integer.valueOf(Integer.parseInt(value) * 1000));
		} else if (key.equals("x")) { //grr, mobskills has one with a value of 50000. that would work with unsigned short, but java doesn't have that...
			intProps.put(Byte.valueOf(Effects.X), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("y")) {
			intProps.put(Byte.valueOf(Effects.Y), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("z")) {
			if (Converter.isNumber(value))
				intProps.put(Byte.valueOf(Effects.Z), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("damage")) {
			shortProps.put(Byte.valueOf(Effects.DAMAGE), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("lt")) {
			int comma = value.indexOf(',');
			lt = new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())));
		} else if (key.equals("rb")) {
			int comma = value.indexOf(',');
			rb = new Point(Integer.parseInt(value.substring(0, comma)), Integer.parseInt(value.substring(comma + 1, value.length())));
		} else if (key.equals("mobCount")) {
			byteProps.put(Byte.valueOf(Effects.MOB_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("prop")) {
			shortProps.put(Byte.valueOf(Effects.PROP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mastery")) {
			byteProps.put(Byte.valueOf(Effects.MASTERY), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("cooltime") || key.equals("interval")) {
			shortProps.put(Byte.valueOf(Effects.COOLTIME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("range")) {
			shortProps.put(Byte.valueOf(Effects.RANGE), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("pad")) {
			shortProps.put(Byte.valueOf(Effects.WATK), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("pdd")) {
			shortProps.put(Byte.valueOf(Effects.WDEF), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mad")) {
			shortProps.put(Byte.valueOf(Effects.MATK), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mdd")) {
			shortProps.put(Byte.valueOf(Effects.MDEF), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("acc")) {
			shortProps.put(Byte.valueOf(Effects.ACCY), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("eva")) {
			shortProps.put(Byte.valueOf(Effects.AVOID), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("hp")) {
			shortProps.put(Byte.valueOf(Effects.HP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mp")) {
			shortProps.put(Byte.valueOf(Effects.MP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("speed")) {
			shortProps.put(Byte.valueOf(Effects.SPEED), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("jump")) {
			shortProps.put(Byte.valueOf(Effects.JUMP), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("attackCount")) {
			byteProps.put(Byte.valueOf(Effects.ATTACK_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("bulletCount")) {
			byteProps.put(Byte.valueOf(Effects.BULLET_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("itemCon")) {
			intProps.put(Byte.valueOf(Effects.ITEM_CONSUME), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("itemConNo")) {
			byteProps.put(Byte.valueOf(Effects.ITEM_CONSUME_COUNT), Byte.valueOf(Byte.parseByte(value)));
		} else if (key.equals("bulletConsume")) {
			shortProps.put(Byte.valueOf(Effects.BULLET_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("moneyCon")) {
			shortProps.put(Byte.valueOf(Effects.MONEY_CONSUME), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("morph")) {
			intProps.put(Byte.valueOf(Effects.MORPH), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("limit")) {
			shortProps.put(Byte.valueOf(Effects.LIMIT), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("summonEffect")) {
			byteProps.put(Byte.valueOf(Effects.SUMMON_EFFECT), Byte.valueOf(Byte.parseByte(value)));
		} else if (Converter.isNumber(key)) {
			//as of GMS v0.62, Shout level 4's prop is glitched, but we can't
			//check that here, so we'll have to make a special case in the Kvj
			//parser. :(
			summons.put(Byte.valueOf(Byte.parseByte(key)), Integer.valueOf(Integer.parseInt(value)));
		}
	}

	@Override
	public int size() {
		int sum = 0;
		sum += (intProps.size() * (Size.INT + Size.BYTE));
		sum += (shortProps.size() * (Size.SHORT + Size.BYTE));
		sum += (byteProps.size() * (Size.BYTE + Size.BYTE));
		if (lt != null)
			sum += Size.BYTE + 2 * Size.SHORT;
		if (rb != null)
			sum += Size.BYTE + 2 * Size.SHORT;
		sum += (summons.size() * (Size.BYTE + Size.BYTE + Size.INT));
		return sum;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		for (Entry<Byte, Integer> entry : intProps.entrySet())
			lew.writeByte(entry.getKey().byteValue()).writeInt(entry.getValue().intValue());
		for (Entry<Byte, Short> entry : shortProps.entrySet())
			lew.writeByte(entry.getKey().byteValue()).writeShort(entry.getValue().shortValue());
		for (Entry<Byte, Byte> entry : byteProps.entrySet())
			lew.writeByte(entry.getKey().byteValue()).writeByte(entry.getValue().byteValue());
		if (lt != null)
			lew.writeByte(Effects.LT).writeShort((short) lt.x).writeShort((short) lt.y);
		if (rb != null)
			lew.writeByte(Effects.RB).writeShort((short) rb.x).writeShort((short) rb.y);
		for (Entry<Byte, Integer> entry : summons.entrySet())
			lew.writeByte(Effects.SUMMON).writeByte(entry.getKey().byteValue()).writeInt(entry.getValue().intValue());
	}
}
