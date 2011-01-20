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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import kvjcompiler.Effects;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.skill.SkillConverter;

public class Skill implements IStructure {
	private int skillid;
	private Map<Byte, SkillEffect> levels;
	private String elemAttr;
	private boolean isBuff, isCharged;
	private int animationTime;
	//private boolean effectExists, hitExists, ballExists, actionExists;
	//private int skillType;

	public Skill(int skillid) {
		this.skillid = skillid;
		this.levels = new TreeMap<Byte, SkillEffect>();
		this.isBuff = isBuff();
		this.isCharged = isChargedSkill();
	}

	public void setProperty(String key, String value) {
		if (key.equals("elemAttr")) {
			this.elemAttr = value;
		}/* else if (key.equals("skillType")) {
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

	public int size() {
		int sum = Size.INT;
		sum += isBuff ? Size.BYTE : 0;
		sum += (elemAttr == null ? 0 : elemAttr.length()) + Size.BYTE;
		sum += Size.INT;
		sum += isCharged ? Size.BYTE : 0;
		for (SkillEffect effect : levels.values())
			sum += Size.BYTE + Size.BYTE + effect.size() + Size.BYTE;
		return sum;
	}

	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(skillid);
		if (isBuff)
			lew.writeByte(SkillConverter.IS_BUFF);
		lew.writeNullTerminatedString(elemAttr == null ? "" : elemAttr);
		lew.writeInt(animationTime);
		if (isCharged)
			lew.writeByte(SkillConverter.IS_CHARGE);
		for (Entry<Byte, SkillEffect> entry : levels.entrySet()) {
			lew.writeByte(SkillConverter.NEXT_LEVEL).writeByte(entry.getKey().byteValue());
			entry.getValue().writeBytes(lew);
			lew.writeByte(Effects.END_EFFECT);
		}
	}

	private boolean isChargedSkill() {
		switch (skillid) {
			case 2121001:
			case 2221001:
			case 2321001:
			case 5101004:
			case 5201002:
				return true;
			default:
				return false;
		}
	}

	private boolean isBuff() {
		//if (skillType == 2)
			//return true;

		//boolean isBuff = effectExists && !hitExists && !ballExists;
		//isBuff |= actionExists;
		//isBuff = isBuff(id, isBuff);
		switch (skillid) {
			case 1001: // recovery
			case 1002: // nimble feet
			case 1004: // monster riding
			case 1005: // echo of hero
			case 1001003: // iron body
			case 1101004: // sword booster
			case 1201004: // sword booster
			case 1101005: // axe booster
			case 1201005: // bw booster
			case 1301004: // spear booster
			case 1301005: // polearm booster
			case 3101002: // bow booster
			case 3201002: // crossbow booster
			case 4101003: // claw booster
			case 4201002: // dagger booster
			case 1101007: // power guard
			case 1201007: // power guard
			case 1101006: // rage
			case 1301006: // iron will
			case 1301007: // hyperbody
			case 1111002: // combo attack
			case 1211006: // blizzard charge bw
			case 1211004: // fire charge bw
			case 1211008: // lightning charge bw
			case 1221004: // divine charge bw
			case 1211003: // fire charge sword
			case 1211005: // ice charge sword
			case 1211007: // thunder charge sword
			case 1221003: // holy charge sword
			case 1311008: // dragon blood
			case 1121000: // maple warrior
			case 1221000: // maple warrior
			case 1321000: // maple warrior
			case 2121000: // maple warrior
			case 2221000: // maple warrior
			case 2321000: // maple warrior
			case 3121000: // maple warrior
			case 3221000: // maple warrior
			case 4121000: // maple warrior
			case 4221000: // maple warrior
			case 1121002: // power stance
			case 1221002: // power stance
			case 1321002: // power stance
			case 1121010: // enrage
			case 1321007: // beholder
			case 1320008: // beholder healing
			case 1320009: // beholder buff
			case 2001002: // magic guard
			case 2001003: // magic armor
			case 2101001: // meditation
			case 2201001: // meditation
			case 2301003: // invincible
			case 2301004: // bless
			case 2111005: // spell booster
			case 2211005: // spell booster
			case 2311003: // holy symbol
			case 2311006: // summon dragon
			case 2121004: // infinity
			case 2221004: // infinity
			case 2321004: // infinity
			case 2321005: // holy shield
			case 2121005: // elquines
			case 2221005: // ifrit
			case 2321003: // bahamut
			case 3121006: // phoenix
			case 3221005: // frostprey
			case 3111002: // puppet
			case 3211002: // puppet
			case 3111005: // silver hawk
			case 3211005: // golden eagle
			case 3001003: // focus
			case 3101004: // soul arrow bow
			case 3201004: // soul arrow crossbow
			case 3121002: // sharp eyes
			case 3221002: // sharp eyes
			case 3121008: // concentrate
			case 3221006: // blind
			case 4001003: // dark sight
			case 4101004: // haste
			case 4201003: // haste
			case 4111001: // meso up
			case 4111002: // shadow partner
			case 4121006: // shadow stars
			case 4211003: // pick pocket
			case 4211005: // meso guard
			case 5111005: // Transformation (Buccaneer)
			case 5121003: // Super Transformation (Viper)
			case 5220002: // wrath of the octopi
			case 5211001: // Pirate octopus summon
			case 5211002: // Pirate bird summon
			case 5221006: // BattleShip
			case 9001000: // haste
			case 9101001: // super haste
			case 9101002: // holy symbol
			case 9101003: // bless
			case 9101004: // hide
			case 9101008: // hyper body
			case 1121011: // hero's will
			case 1221012: // hero's will
			case 1321010: // hero's will
			case 2321009: // hero's will
			case 2221008: // hero's will
			case 2121008: // hero's will
			case 3121009: // hero's will
			case 3221008: // hero's will
			case 4121009: // hero's will
			case 4221008: // hero's will
			case 2101003: // slow
			case 2201003: // slow
			case 2111004: // seal
			case 2211004: // seal
			case 1111007: // armor crash
			case 1211009: // magic crash
			case 1311007: // power crash
			case 2311005: // doom
			case 2121002: // mana reflection
			case 2221002: // mana reflection
			case 2321002: // mana reflection
			case 2311001: // dispel
			case 1201006: // threaten
			case 4121004: // ninja ambush
			case 4221004: // ninja ambush
				return true;
			case 1121006: // rush
			case 1221007: // rush
			case 1311005: // sacrifice
			case 1321003: // rush
			case 2111002: // explosion
			case 2111003: // poison mist
			case 2301002: // heal
			case 3110001: // mortal blow
			case 3210001: // mortal blow
			case 4101005: // drain
			case 4111003: // shadow web
			case 4201004: // steal
			case 4221006: // smokescreen
			case 9101000: // heal + dispel
			case 5201006: // Recoil Shot
			case 1121001: // monster magnet
			case 1221001: // monster magnet
			case 1321001: // monster magnet
			case 2121001: //big bang
			case 2221001: //big bang
			case 2321001: //big bang
			default:
				return false;
		}
	}
}
