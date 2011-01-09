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
package kvjinterpreter.item;

import kvjinterpreter.DataReader;
import kvjinterpreter.LittleEndianReader;

/*
 * idt = itemid / 10000
 * idt == 200 (potions) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED }
 * idt == 201 (food, more potions) { WHOLE_PRICE, IS_QUEST_ITEM }
 * idt == 202 (more food, more potions) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_QUEST_ITEM, IS_ONE_ONLY, CONSUME_ON_PICKUP }
 * idt == 203 (warp scrolls + milk???) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 204 (upgrade scroll) { SL0T_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY, BONUS_STAT, CURSED, SUCCESS, SCROLL_REQUIREMENTS }
 * idt == 205 (antidote, tonic, holy water, more potions) { WHOLE_PRICE, IS_TRADE_BLOCKED }
 * idt == 206 (arrows) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, REQ_STAT, BONUS_STAT }
 * idt == 207 (throwing star) { SLOT_MAX, WHOLE_PRICE, UNIT_PRICE, IS_TRADE_BLOCKED, REQ_STAT, BONUS_STAT }
 * idt == 210 (summoning bags) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_QUEST_ITEM, IS_ONE_ONLY }
 * idt == 212 (pet food) { SLOT_MAX, WHOLE_PRICE, PET_CONSUMABLE_BY }
 * idt == 219 (lie detector test) { WHOLE_PRICE, IS_TRADE_BLOCKED }
 * idt == 221 (more [transformation] potions) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_QUEST_ITEM }
 * idt == 224 (engagement rings) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 226 (revitalizer) { SLOT_MAX, WHOLE_PRICE }
 * idt == 227 (more magical potions) { WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 228 (skill books 2280003-2280010 + 2280012) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY, SUCCESS, SKILL }
 * idt == 229 (mastery books) { SLOT_MAX, WHOLE_PRICE, SUCCESS, SKILL }
 * idt == 231 (Owl of minerva) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 232 (teleport rock) { SLOT_MAX, WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 233 (bullets) { SLOT_MAX, WHOLE_PRICE, UNIT_PRICE, IS_TRADE_BLOCKED, REQ_STAT, BONUS_STAT }
 * idt == 236 (change to ghost + ghost candy) { SLOT_MAX, WHOLE_PRICE }
 * idt == 237 (songs + stories + travels + writs of Solomon) { WHOLE_PRICE, REQ_STAT }
 * idt == 238 (monster cards) { WHOLE_PRICE, IS_TRADE_BLOCKED, IS_ONE_ONLY, CONSUME_ON_PICKUP }
 */
public class ItemDataReader extends DataReader {
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
		CONSUME_ON_PICKUP = 12,
		SKILL = 13,
		PET_CONSUMABLE_BY = 14,
		UNIT_PRICE = 15,
		REQ_STAT = 16,
		UPGRADE_SLOTS = 17,
		SCROLL_REQUIREMENTS = 18,
		TRIGGER_ITEM = 19,
		MESO_VALUE = 20,
		
		PET_COMMAND = 21,
		PET_HUNGER = 22,
		PET_EVOL_AMOUNT = 24,
		PET_EVOLVE = 25
	;
	
	private static final byte //stats
		STR = 0,
		DEX = 1,
		INT = 2,
		LUK = 3,
		PAD = 4,
		PDD = 5,
		MAD = 6,
		MDD = 7,
		ACC = 8,
		EVA = 9,
		MHP = 10,
		MMP = 11,
		Speed = 12,
		Jump = 13,
		Level = 14
	;
	
	private MapleItem item;
	private LittleEndianReader reader;
	//private java.util.Map<Byte, java.util.List<Integer>> ops;
	
	public WzType getWzType() {
		return WzType.ITEM;
	}
	
	/*public ItemDataReader() {
		ops = new java.util.TreeMap<Byte, java.util.List<Integer>>();
	}*/
	
	public void initialize(int itemid, LittleEndianReader reader) {
		this.item = new MapleItem(itemid);
		this.reader = reader;
	}
	
	public MapleItem doWork() {
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case WHOLE_PRICE:
					reader.readInt();
					break;
				case SLOT_MAX:
					reader.readShort();
					break;
				case IS_TRADE_BLOCKED:
					//boolean
					break;
				case IS_ONE_ONLY:
					//boolean
					break;
				case IS_QUEST_ITEM:
					//boolean
					break;
				case BONUS_STAT:
					processBonusStat();
					break;
				case SUMMON:
					processSummon();
					break;
				case SUCCESS:
					reader.readInt();
					break;
				case CURSED:
					reader.readInt();
					break;
				case CASH:
					//boolean
					break;
				case OPERATING_HOURS:
					processOperatingHours();
					break;
				case CONSUME_ON_PICKUP:
					//boolean
					break;
				case SKILL:
					reader.readInt();
					break;
				case PET_CONSUMABLE_BY:
					reader.readInt();
					break;
				case UNIT_PRICE:
					reader.readDouble();
					break;
				case REQ_STAT:
					processReqStat();
					break;
				case UPGRADE_SLOTS:
					reader.readByte();
					break;
				case SCROLL_REQUIREMENTS:
					processScrollReqs();
					break;
				case TRIGGER_ITEM:
					reader.readInt();
					break;
				case MESO_VALUE:
					reader.readInt();
					break;
				
				case PET_COMMAND:
					processPetCmd();
					break;
				case PET_HUNGER:
					reader.readInt();
					break;
				case PET_EVOL_AMOUNT:
					reader.readInt();
					break;
				case PET_EVOLVE:
					processPetEvolve();
					break;
			}
			/*Byte i = Byte.valueOf(now);
			if (!ops.containsKey(i))
				ops.put(i, new java.util.ArrayList<Integer>());
			ops.get(i).add(item.getId());*/
		}
		return item;
	}
	
	private void processBonusStat() {
		byte stat = reader.readByte();
		short value = reader.readShort();
	}
	
	private void processReqStat() {
		byte stat = reader.readByte();
		short value = reader.readShort();
	}
	
	private void processScrollReqs() {
		for (int i = reader.readInt(); i > 0; i--)
			/*item.addScrollReq(*/reader.readInt()/*)*/;
	}
	
	private void processSummon() {
		int mobId = reader.readInt();
		int prob = reader.readInt();
	}
	
	private void processOperatingHours() {
		byte day = reader.readByte();
		byte startHour = reader.readByte();
		byte endHour = reader.readByte();
	}
	
	private void processPetCmd() {
		byte commandId = reader.readByte();
		int prob = reader.readInt();
		int expInc = reader.readInt();
	}
	
	private void processPetEvolve() {
		int itemId = reader.readInt();
		int prob = reader.readInt();
	}
	
	public static String getCategory(int itemid) {
		switch (itemid / 1000000) {
			/*case 1:
				return "Equip";*/
			case 2:
				return "Consume";
			case 3:
				return "Install";
			case 4:
				return "Etc";
			case 5:
				if (itemid >= 5000000 && itemid <= 5000100)
					return "Pet";
				else
					return "Cash";
			default:
				return null;
		}
	}
	
	/*public java.util.Map<Byte, java.util.List<Integer>> getOps() {
		return ops;
	}*/
}
