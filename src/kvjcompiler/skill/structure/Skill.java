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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import kvjcompiler.Effects;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.skill.SkillConverter;

/**
 *
 * @author GoldenKevin
 */
public class Skill implements IStructure {
	private final int skillid;
	private final Map<Byte, SkillEffect> levels;
	private String elemAttr;
	private byte summon;
	private boolean prepared, keydown, keydownend;
	private int animationTime;
	//private boolean effectExists, hitExists, ballExists, actionExists;
	//private int skillType;

	public Skill(int skillid) {
		this.skillid = skillid;
		this.levels = new TreeMap<Byte, SkillEffect>();
		this.summon = -1;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("elemAttr")) {
			this.elemAttr = value;
		} else if (key.equals("prepare")) {
			this.prepared = true;
		} else if (key.equals("keydown")) {
			this.keydown = true;
		} else if (key.equals("keydownend")) {
			this.keydownend = true;
		} /* else if (key.equals("skillType")) {
			skillType = Integer.parseInt(value);
		} else if (key.equals("hit")) {
			this.hitExists = true;
		} else if (key.equals("ball")) {
			this.ballExists = true;
		} else if (key.equals("effect")) {
			this.effectExists = true;
		} else if (key.equals("action")) {
			this.actionExists = true;
		}*/
	}

	public void addLevel(byte level, SkillEffect effect) {
		levels.put(Byte.valueOf(level), effect);
	}

	public void addDelay(int time) {
		animationTime += time;
	}

	public void setSummonType(byte type) {
		summon = type;
	}

	@Override
	public int size() {
		int sum = Size.INT;
		if (elemAttr != null)
			sum += Size.HEADER + elemAttr.length() + Size.BYTE;
		if (animationTime != 0)
			sum += Size.HEADER + Size.INT;
		if (summon != -1)
			sum += Size.BYTE + Size.BYTE;
		if (prepared)
			sum += Size.BYTE;
		if (keydown)
			sum += Size.BYTE;
		if (keydownend)
			sum += Size.BYTE;
		for (SkillEffect effect : levels.values())
			sum += Size.BYTE + Size.BYTE + effect.size() + Size.BYTE;
		return sum;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(skillid);
		if (elemAttr != null)
			lew.writeByte(SkillConverter.ELEM_ATTR).writeNullTerminatedString(elemAttr);
		if (animationTime != 0)
			lew.writeByte(SkillConverter.DELAY).writeInt(animationTime);
		if (summon != -1)
			lew.writeByte(SkillConverter.SUMMON).writeByte(summon);
		if (prepared)
			lew.writeByte(SkillConverter.PREPARED);
		if (keydown)
			lew.writeByte(SkillConverter.KEY_DOWN);
		if (keydownend)
			lew.writeByte(SkillConverter.KEY_DOWN_END);
		for (Entry<Byte, SkillEffect> entry : levels.entrySet()) {
			lew.writeByte(SkillConverter.NEXT_LEVEL).writeByte(entry.getKey().byteValue());
			entry.getValue().writeBytes(lew);
			lew.writeByte(Effects.END_EFFECT);
		}
	}
}
