/*
 *  Sample interpreter for data files compiled from XML using KvJ
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
package kvjinterpreter.mob;

import kvjinterpreter.DataReader;
import kvjinterpreter.LittleEndianReader;
import kvjinterpreter.mob.structure.*;

public class MobDataReader extends DataReader {
	private static final byte
		LEVEL = 1,
		MAX_HP = 2,
		MAX_MP = 3,
		PHYSICAL_DAMAGE = 4,
		EXP = 5,
		UNDEAD = 6,
		ELEM_ATTR = 7,
		REMOVE_AFTER = 8,
		HIDE_HP = 9,
		HIDE_NAME = 10,
		HP_TAG_COLOR = 11,
		HP_TAG_BG_COLOR = 12,
		BOSS = 13,
		SELF_DESTRUCT = 14,
		LOSE_ITEM = 15,
		INVINCIBLE = 16,
		REVIVE = 17,
		FIRST_ATTACK = 18,
		ATTACK = 19,
		SKILL = 20,
		BUFF = 21,
		DELAY = 22
	;
	
	private MapleMob mob;
	private LittleEndianReader reader;
	
	public WzType getWzType() {
		return WzType.MOB;
	}
	
	public void initialize(int mobid, LittleEndianReader reader) {
		this.mob = new MapleMob(mobid);
		this.reader = reader;
	}
	
	public MapleMob doWork() {
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case LEVEL:
					mob.setLevel(reader.readInt());
					break;
				case MAX_HP:
					mob.setMaxHp(reader.readInt());
					break;
				case MAX_MP:
					mob.setMaxMp(reader.readInt());
					break;
				case PHYSICAL_DAMAGE:
					mob.setPhysicalDamage(reader.readInt());
					break;
				case EXP:
					mob.setExp(reader.readInt());
					break;
				case UNDEAD:
					mob.setUndead();
					break;
				case ELEM_ATTR:
					mob.setElementAttribute(reader.readNullTerminatedString());
					break;
				case REMOVE_AFTER:
					mob.setRemoveAfter(reader.readInt());
					break;
				case HIDE_HP:
					mob.setHideHp();
					break;
				case HIDE_NAME:
					mob.setHideName();
					break;
				case HP_TAG_COLOR:
					mob.setHpTagColor(reader.readInt());
					break;
				case HP_TAG_BG_COLOR:
					mob.setHpTagBgColor(reader.readInt());
					break;
				case BOSS:
					mob.setBoss();
					break;
				case SELF_DESTRUCT:
					processSelfDestruct();
					break;
				case LOSE_ITEM:
					mob.addLoseItem(reader.readInt());
					break;
				case INVINCIBLE:
					mob.setInvincible();
					break;
				case REVIVE:
					mob.addSummon(reader.readInt());
					break;
				case FIRST_ATTACK:
					mob.setFirstAttack();
					break;
				case ATTACK:
					processAttack();
					break;
				case SKILL:
					processSkill();
					break;
				case BUFF:
					mob.setBuffToGive(reader.readInt());
					break;
				case DELAY:
					String name = reader.readNullTerminatedString();
					int delay = reader.readInt();
					mob.addDelay(name, delay);
					break;
			}
		}
		return mob;
	}
	
	private void processSelfDestruct() {
		SelfDestruct sd = new SelfDestruct();
		sd.setAction(reader.readInt());
		sd.setHp(reader.readInt());
		sd.setRemoveAfter(reader.readInt());
		mob.setSelfDestruct(sd);
	}
	
	private void processAttack() {
		int attackid = reader.readInt();
		Attack a = new Attack();
		a.setDeadlyAttack(reader.readBool());
		a.setMpBurn(reader.readInt());
		a.setDiseaseSkill(reader.readInt());
		a.setDiseaseLevel(reader.readInt());
		a.setMpConsume(reader.readInt());
		mob.addAttack(attackid, a);
	}
	
	private void processSkill() {
		int skillid = reader.readInt();
		Skill s = new Skill();
		s.setSkill(reader.readInt());
		s.setLevel(reader.readInt());
		mob.addSkill(skillid, s);
	}
}
