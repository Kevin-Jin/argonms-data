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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import kvjinterpreter.Effects;
import kvjinterpreter.LittleEndianFileStreamReader;
import kvjinterpreter.LittleEndianReader;
import kvjinterpreter.WzInterpreter;

/*
 * idt = itemid / 10000
 *
 * //consume
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
 *
 * //setup
 * idt == 301 { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, REQ_STAT }
 * idt == 399 { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 *
 * //etc
 * idt == 400 (???) { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY, IS_QUEST_ITEM }
 * idt == 401 (???) { WHOLE_PRICE }
 * idt == 402 (???) { WHOLE_PRICE }
 * idt == 403 (???) { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY, IS_QUEST_ITEM }
 * idt == 405 (???) { SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 408 (???) { WHOLE_PRICE }
 * idt == 413 (???) { WHOLE_PRICE, SLOT_MAX }
 * idt == 414 (???) { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 416 (???) { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY, IS_QUEST_ITEM }
 * idt == 417 (???) { WHOLE_PRICE, SLOT_MAX }
 * idt == 421 (???) { WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED, IS_ONE_ONLY }
 * idt == 422 (???) { WHOLE_PRICE, SLOT_MAX, IS_ONE_ONLY, IS_QUEST_ITEM }
 *
 * //cash
 * idt == 501 (???) { CASH }
 * idt == 503 (???) { CASH, SLOT_MAX }
 * idt == 504 { CASH }
 * idt == 505 { CASH }
 * idt == 506 { CASH }
 * idt == 507 { CASH }
 * idt == 508 { CASH, SLOT_MAX }
 * idt == 509 { CASH }
 * idt == 510 { CASH }
 * idt == 511 { CASH, SLOT_MAX }
 * idt == 512 { CASH, TRIGGER_ITEM }
 * idt == 513 { CASH }
 * idt == 514 { CASH }
 * idt == 515 { CASH }
 * idt == 516 { CASH }
 * idt == 517 { CASH, SLOT_MAX }
 * idt == 518 { CASH, SLOT_MAX }
 * idt == 519 { CASH }
 * idt == 520 { CASH, MESO_VALUE }
 * idt == 521 { CASH, SLOT_MAX, OPERATING_HOURS }
 * idt == 522 { CASH }
 * idt == 523 { CASH }
 * idt == 524 { CASH, SLOT_MAX, PET_CONSUMABLE_BY }
 * idt == 525 { CASH, SLOT_MAX }
 * idt == 528 { CASH }
 * idt == 529 { CASH, SLOT_MAX }
 * idt == 530 { CASH, WHOLE_PRICE, SLOT_MAX, IS_TRADE_BLOCKED }
 * idt == 533 { CASH }
 * idt == 536 { CASH, SLOT_MAX, OPERATING_HOURS }
 * idt == 537 { CASH }
 * idt == 538 { CASH }
 * idt == 539 { CASH }
 * idt == 540 { CASH }
 * idt == 542 { CASH }
 * idt == 543 { CASH }
 * idt == 545 { CASH }
 * idt == 546 { CASH, SLOT_MAX }
 * idt == 547 { CASH }
 * idt == 548 { CASH }
 * idt == 549 { CASH }
 * idt == 599 { CASH, SLOT_MAX }
 */
//I suppose in the real thing, we should split pets into a separate factory...
public class ItemDataReader {
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
		MESO_VALUE = 19,
		
		PET_COMMAND = 20,
		PET_HUNGER = 21,
		PET_EVOLVE = 22
	;
	
	private List<Integer> loaded;
	private Map<Integer, Integer> wholePrice;
	private Map<Integer, Short> slotMax;
	private List<Integer> tradeBlocked;
	private List<Integer> onlyOne;
	private List<Integer> questItem;
	private Map<Integer, short[]> bonusStats;
	private Map<Integer, List<int[]>> summons;
	private Map<Integer, Integer> success;
	private Map<Integer, Integer> cursed;
	private List<Integer> cash; //I don't think this is really needed...
	private Map<Integer, List<byte[]>> operatingHours;
	private List<Integer> useOnPickup;
	private Map<Integer, List<Integer>> skills;
	private Map<Integer, List<Integer>> petConsumableBy;
	private Map<Integer, Double> unitPrice;
	private Map<Integer, short[]> reqStats;
	private Map<Integer, List<Integer>> scrollReqs;
	private Map<Integer, Integer> triggerItem;
	private Map<Integer, Byte> tuc;
	private Map<Integer, Integer> mesoValue;

	private Map<Integer, Map<Byte, int[]>> petCommands;
	private Map<Integer, Integer> petHunger;
	private Map<Integer, List<int[]>> evolveChoices;

	public ItemDataReader() {
		loaded = new ArrayList<Integer>();
		wholePrice = new HashMap<Integer, Integer>();
		slotMax = new HashMap<Integer, Short>();
		tradeBlocked = new ArrayList<Integer>();
		onlyOne = new ArrayList<Integer>();
		questItem = new ArrayList<Integer>();
		bonusStats = new HashMap<Integer, short[]>();
		summons = new HashMap<Integer, List<int[]>>();
		success = new HashMap<Integer, Integer>();
		cursed = new HashMap<Integer, Integer>();
		cash = new ArrayList<Integer>();
		operatingHours = new HashMap<Integer, List<byte[]>>();
		useOnPickup = new ArrayList<Integer>();
		skills = new HashMap<Integer, List<Integer>>();
		petConsumableBy = new HashMap<Integer, List<Integer>>();
		unitPrice = new HashMap<Integer, Double>();
		reqStats = new HashMap<Integer, short[]>();
		scrollReqs = new HashMap<Integer, List<Integer>>();
		triggerItem = new HashMap<Integer, Integer>();
		tuc = new HashMap<Integer, Byte>();
		mesoValue = new HashMap<Integer, Integer>();

		petCommands = new HashMap<Integer, Map<Byte, int[]>>();
		petHunger = new HashMap<Integer, Integer>();
		evolveChoices = new HashMap<Integer, List<int[]>>();
	}

	private void load(int itemid)  {
		String cat = getCategory(itemid);
		File f;
		String id = String.format("%08d", itemid);
		if (cat.equals("Pet"))
			f = new File(new StringBuilder(WzInterpreter.binPath).append("Item.wz").append(File.separator).append(cat).append(File.separator).append(String.format("%07d", itemid)).append(".img.kvj").toString());
		else if (cat.equals("Equip"))
			f = new File(new StringBuilder(WzInterpreter.binPath).append("Character.wz").append(File.separator).append(getCharCat(itemid)).append(File.separator).append(id).append(".img.kvj").toString());
		else
			f = new File(new StringBuilder(WzInterpreter.binPath).append("Item.wz").append(File.separator).append(cat).append(File.separator).append(id.substring(0, 4)).append(".img").append(File.separator).append(id).append(".kvj").toString());
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(f));
			doWork(itemid, new LittleEndianFileStreamReader(fis));
			fis.close();
			loaded.add(Integer.valueOf(itemid));
		} catch (IOException e) {
			//Log Error: "Could not read data file for item " + itemid
		}
	}

	public void loadAll() throws IOException {
		//loaded.clear();
		File root = new File(WzInterpreter.binPath + "Item.wz");
		for (String cat : root.list()) {
			File catFolder = new File(root.getAbsolutePath() + File.separatorChar + cat);
			if (cat.equals("Pet")) {
				for (String kvj : catFolder.list()) {
					int itemid = Integer.parseInt(kvj.substring(0, kvj.lastIndexOf(".img.kvj")));
					//if (!loaded.contains(Integer.valueOf(itemid))) {
						InputStream fis = new BufferedInputStream(new FileInputStream(catFolder.getAbsolutePath() + File.separatorChar + kvj));
						doWork(itemid, new LittleEndianFileStreamReader(fis));
						fis.close();
						loaded.add(Integer.valueOf(itemid));
					//}
				}
			} else {
				for (String pref : catFolder.list()) {
					File prefFolder = new File(catFolder.getAbsolutePath() + File.separatorChar + pref);
					for (String kvj : prefFolder.list()) {
						int itemid = Integer.parseInt(kvj.substring(0, kvj.lastIndexOf(".kvj")));
						//if (!loaded.contains(Integer.valueOf(itemid))) {
							InputStream fis = new BufferedInputStream(new FileInputStream(prefFolder.getAbsolutePath() + File.separatorChar + kvj));
							doWork(itemid, new LittleEndianFileStreamReader(fis));
							fis.close();
							loaded.add(Integer.valueOf(itemid));
						//}
					}
				}
			}
		}
	}

	public int loadedItems() {
		return loaded.size();
	}

	public void doWork(int itemid, LittleEndianReader reader) {
		Integer oId = Integer.valueOf(itemid);
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case WHOLE_PRICE:
					wholePrice.put(oId, Integer.valueOf(reader.readInt()));
					break;
				case SLOT_MAX:
					slotMax.put(oId, Short.valueOf(reader.readShort()));
					break;
				case IS_TRADE_BLOCKED:
					tradeBlocked.add(oId);
					break;
				case IS_ONE_ONLY:
					onlyOne.add(oId);
					break;
				case IS_QUEST_ITEM:
					questItem.add(oId);
					break;
				case BONUS_STAT:
					if (!bonusStats.containsKey(oId))
						bonusStats.put(oId, new short[16]);
					processBonusStat(reader, oId);
					break;
				case SUMMON:
					if (!summons.containsKey(oId))
						summons.put(oId, new ArrayList<int[]>());
					summons.get(oId).add(processSummon(reader));
					break;
				case SUCCESS:
					success.put(oId, Integer.valueOf(reader.readInt()));
					break;
				case CURSED:
					cursed.put(oId, Integer.valueOf(reader.readInt()));
					break;
				case CASH:
					cash.add(oId);
					break;
				case OPERATING_HOURS:
					if (!operatingHours.containsKey(oId))
						operatingHours.put(oId, new ArrayList<byte[]>());
					operatingHours.get(oId).add(processOperatingHours(reader));
					break;
				case SKILL:
					if (!skills.containsKey(oId))
						skills.put(oId, new ArrayList<Integer>());
					skills.get(oId).add(Integer.valueOf(reader.readInt()));
					break;
				case UNIT_PRICE:
					unitPrice.put(oId, Double.valueOf(reader.readDouble()));
					break;
				case REQ_STAT:
					if (!reqStats.containsKey(oId))
						reqStats.put(oId, new short[16]);
					processReqStat(reader, oId);
					break;
				case UPGRADE_SLOTS:
					tuc.put(oId, Byte.valueOf(reader.readByte()));
					break;
				case SCROLL_REQUIREMENTS:
					scrollReqs.put(oId, processScrollReqs(reader));
					break;
				case ITEM_EFFECT:
					while (reader.readByte() != Effects.END_EFFECT);
					break;
				case TRIGGER_ITEM:
					triggerItem.put(oId, Integer.valueOf(reader.readInt()));
					break;
				case MESO_VALUE:
					mesoValue.put(oId, Integer.valueOf(reader.readInt()));
					break;
				
				case PET_COMMAND:
					if (!petCommands.containsKey(oId))
						petCommands.put(oId, new HashMap<Byte, int[]>());
					processPetCmd(reader, oId);
					break;
				case PET_HUNGER:
					petHunger.put(oId, Integer.valueOf(reader.readInt()));
					break;
				case PET_EVOLVE:
					if (!evolveChoices.containsKey(oId))
						evolveChoices.put(oId, new ArrayList<int[]>());
					evolveChoices.get(oId).add(processPetEvolve(reader));
					break;
			}
		}
	}
	
	private void processBonusStat(LittleEndianReader reader, Integer oId) {
		byte stat = reader.readByte();
		short value = reader.readShort();
		bonusStats.get(oId)[stat] = value;
	}
	
	private void processReqStat(LittleEndianReader reader, Integer oId) {
		byte stat = reader.readByte();
		short value = reader.readShort();
		reqStats.get(oId)[stat] = value;
	}
	
	private List<Integer> processScrollReqs(LittleEndianReader reader) {
		List<Integer> reqs = new ArrayList<Integer>();
		for (int i = reader.readInt(); i > 0; i--)
			reqs.add(Integer.valueOf(reader.readInt()));
		return reqs;
	}
	
	private int[] processSummon(LittleEndianReader reader) {
		int mobId = reader.readInt();
		int prob = reader.readInt();
		return new int[] { mobId, prob };
	}
	
	private byte[] processOperatingHours(LittleEndianReader reader) {
		byte day = reader.readByte();
		byte startHour = reader.readByte();
		byte endHour = reader.readByte();
		return new byte[] { day, startHour, endHour };
	}
	
	private void processPetCmd(LittleEndianReader reader, Integer oId) {
		byte commandId = reader.readByte();
		int prob = reader.readInt();
		int expInc = reader.readInt();
		petCommands.get(oId).put(Byte.valueOf(commandId), new int[] { prob, expInc });
	}
	
	private int[] processPetEvolve(LittleEndianReader reader) {
		int itemId = reader.readInt();
		int prob = reader.readInt();
		return new int[] { itemId, prob };
	}

	//the below public methods are just examples of how to use some properties
	public int getWholePrice(int itemId) {
		Integer oId = Integer.valueOf(itemId);
		if (!loaded.contains(oId))
			load(itemId);
		return wholePrice.get(oId).intValue();
	}

	public boolean isTradeBlocked(int itemId) {
		Integer oId = Integer.valueOf(itemId);
		if (!loaded.contains(oId))
			load(itemId);
		return tradeBlocked.contains(oId);
	}

	public short getReqLevel(int itemId) {
		Integer oId = Integer.valueOf(itemId);
		if (!loaded.contains(oId))
			load(itemId);
		short[] stats = reqStats.get(oId);
		if (stats != null)
			return stats[Effects.Level];
		return 0;
	}

	public boolean canConsume(int petId, int itemId) {
		Integer oId = Integer.valueOf(itemId);
		if (!loaded.contains(oId))
			load(itemId);
		List<Integer> consumableBy = petConsumableBy.get(oId);
		if (consumableBy != null)
			return consumableBy.contains(Integer.valueOf(petId));
		return false;
	}

	public boolean isRateCardOperating(int itemId) {
		Integer oId = Integer.valueOf(itemId);
		if (!loaded.contains(oId))
			load(itemId);
		Calendar nowInLa = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		int today = nowInLa.get(Calendar.DAY_OF_WEEK);
		int thisHour = nowInLa.get(Calendar.HOUR_OF_DAY);
		for (byte[] t : operatingHours.get(oId))
			if (t[0] == 8) {
				if (isHoliday(nowInLa) && thisHour >= t[1] && thisHour <= t[2])
					return true;
			} else if (t[0] == today && thisHour >= t[1] && thisHour <= t[2])
				return true;
		return false;
	}

	private boolean isHoliday(Calendar now) {
		return false;
	}

	public static String getCategory(int itemid) {
		switch (itemid / 1000000) {
			case 1:
				return "Equip";
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

	public static String getCharCat(int id) {
		switch (id / 10000) {
			case 2:
				return "Face";
			case 3:
				return "Hair";
			case 100:
				return "Cap";
			case 101:
			case 102:
			case 103:
			case 112:
				return "Accessory";
			case 104:
				return "Coat";
			case 105:
				return "Longcoat";
			case 106:
				return "Pants";
			case 107:
				return "Shoes";
			case 108:
				return "Glove";
			case 109:
				return "Shield";
			case 110:
				return "Cape";
			case 111:
				return "Ring";
			case 130:
			case 131:
			case 132:
			case 133:
			case 137:
			case 138:
			case 139:
			case 140:
			case 141:
			case 142:
			case 143:
			case 144:
			case 145:
			case 146:
			case 147:
			case 148:
			case 149:
			case 160:
			case 170:
				return "Weapon";
			case 180:
			case 181:
			case 182:
			case 183:
				return "PetEquip";
			case 190:
			case 191:
			case 193:
				return "TamingMob";
			default:
				return null;
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		long start, end;
		ItemDataReader idr;
		//start = System.nanoTime();
		idr = new ItemDataReader();
		/*System.out.println(idr.isRateCardOperating(5210000));
		System.out.println(idr.getReqLevel(2060005));
		System.out.println(idr.canConsume(5000008, 2120000));
		end = System.nanoTime();
		System.out.println("Retrieving and processing this data took " + ((end - start) / 1000000.0) + "ms.");*/

		System.out.println("Pausing 3 seconds before loading all. Current memory usage: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024));
		Thread.sleep(3000);
		start = System.nanoTime();
		idr.loadAll();
		end = System.nanoTime();
		System.out.println("Took " + ((end - start) / 1000000.0) + "ms to load " + idr.loadedItems() + " items. Current memory usage: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024));
		Thread.sleep(10000);
	}
}
