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

package kvjcompiler.item.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import kvjcompiler.Converter;
import kvjcompiler.Effects;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class ItemEffect implements IStructure {
	private final Map<Byte, Short> shortProps;
	private final Map<Byte, Integer> intProps;
	private boolean poison, seal, darkness, weakness, curse;
	private boolean consumeOnPickup;
	private final List<Integer> consumableByPets;

	public ItemEffect() {
		shortProps = new HashMap<Byte, Short>();
		intProps = new HashMap<Byte, Integer>();
		consumableByPets = new ArrayList<Integer>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("time")) {
			intProps.put(Byte.valueOf(Effects.DURATION), Integer.valueOf(Integer.parseInt(value)));
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
		} else if (key.equals("morph")) {
			intProps.put(Byte.valueOf(Effects.MORPH), Integer.valueOf(Integer.parseInt(value)));
		} else if (key.equals("hpR")) {
			shortProps.put(Byte.valueOf(Effects.HP_RECOVER_PERCENT), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("mpR")) {
			shortProps.put(Byte.valueOf(Effects.MP_RECOVER_PERCENT), Short.valueOf(Short.parseShort(value)));
		} else if (key.equals("moveTo")) {
			intProps.put(Byte.valueOf(Effects.MOVE_TO), Integer.parseInt(value));
		} else if (key.equals("poison")) {
			poison = Integer.parseInt(value) == 1;
		} else if (key.equals("seal")) {
			seal = Integer.parseInt(value) == 1;
		} else if (key.equals("darkness")) {
			darkness = Integer.parseInt(value) == 1;
		} else if (key.equals("weakness")) {
			weakness = Integer.parseInt(value) == 1;
		} else if (key.equals("curse")) {
			curse = Integer.parseInt(value) == 1;
		} else if (key.equals("consumeOnPickup")) {
			consumeOnPickup = Integer.parseInt(value) == 1;
		} else if (key.equals("inc")) {
			shortProps.put(Byte.valueOf(Effects.PET_FULLNESS_RECOVER), Short.parseShort(value));
		} else if (Converter.isNumber(key)) {
			consumableByPets.add(Integer.parseInt(value));
		} else {
			//System.out.println("Unhandled effect: " + key);
		}
	}

	@Override
	public int size() {
		int sum = 0;
		sum += (intProps.size() * (Size.INT + Size.BYTE));
		sum += (shortProps.size() * (Size.SHORT + Size.BYTE));
		if (poison)
			sum += Size.BYTE;
		if (seal)
			sum += Size.BYTE;
		if (darkness)
			sum += Size.BYTE;
		if (weakness)
			sum += Size.BYTE;
		if (curse)
			sum += Size.BYTE;
		if (consumeOnPickup)
			sum += Size.BYTE;
		sum += consumableByPets.size() * (Size.INT + Size.BYTE);
		return sum;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		for (Entry<Byte, Integer> entry : intProps.entrySet())
			lew.writeByte(entry.getKey().byteValue()).writeInt(entry.getValue().intValue());
		for (Entry<Byte, Short> entry : shortProps.entrySet())
			lew.writeByte(entry.getKey().byteValue()).writeShort(entry.getValue().shortValue());
		if (poison)
			lew.writeByte(Effects.POISON);
		if (seal)
			lew.writeByte(Effects.SEAL);
		if (darkness)
			lew.writeByte(Effects.DARKNESS);
		if (weakness)
			lew.writeByte(Effects.WEAKNESS);
		if (curse)
			lew.writeByte(Effects.CURSE);
		if (consumeOnPickup)
			lew.writeByte(Effects.CONSUME_ON_PICKUP);
		for (Integer i : consumableByPets)
			lew.writeByte(Effects.PET_CONSUMABLE_BY).writeInt(i);
	}
}
