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

package kvjcompiler.mob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.DataType;

/**
 * Creates MCDB compatible drop chances for mobs using a list of droppable items
 * from MonsterBook.img in the String.wz file included with Global MapleStory
 * v0.62 and later.
 * This Converter should not be used in the same ways as the main ones for the
 * WZ types. It should only be used as a helper class for Mob converting, as no
 * files are actually saved to anywhere.
 * Props to LaiLaiNoob/LightPepsi for making the actual getChance method...
 * @author GoldenKevin (actual XML reader and stuffs)
 *         LightPepsi (chance logic)
 */
public class DropConverter extends Converter {
	private final Map<Integer, Map<Integer, Integer>> drops;
	private final Set<Integer> noMesos;
	private final CustomDropsMap customDrops;
	private final Map<Integer, List<QuestItemDropEntry>> questItemDrops;

	public DropConverter() {
		drops = new HashMap<Integer, Map<Integer, Integer>>();
		noMesos = new HashSet<Integer>();
		customDrops = new CustomDropsMap();
		questItemDrops = new HashMap<Integer, List<QuestItemDropEntry>>();
	}

	@Override
	public String getWzName() {
		return null;
	}

	@Override
	protected void startCompile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		System.err.print("Getting drop chances...\t");

		if (r.getEventType() != XMLStreamReader.START_DOCUMENT)
			throw new IllegalStateException("ERROR: Received an XML that has already been partially read.");

		r.next();
		if (DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || !r.getAttributeValue(0).equals("MonsterBook.img"))
			throw new IllegalStateException("ERROR: Received a non-WZ XML file.");

		this.r = r;
		traverseBlock("");
	}

	@Override
	protected void finalizeCompile(String internalPath, String imgName) throws IOException, XMLStreamException {
		try {
			customDrops.appendRemaining(drops);
			customDrops.clear();
			if (r.getEventType() != XMLStreamReader.END_ELEMENT || DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || r.next() != XMLStreamReader.END_DOCUMENT)
				throw new IllegalStateException("ERROR: End of " + internalPath + imgName + " not yet reached.");

			System.out.println("MonsterDrops done.");
			System.err.println("Finished loading drop chances.");
		} finally {
			if (r != null) {
				this.r.close();
				this.r = null;
			}
		}
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		for (int open1 = 1, event, open; open1 > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open1++;
				if (r.getAttributeValue(0).equals("reward")) {
					Map<Integer, Integer> idAndChance = new HashMap<Integer, Integer>();
					int mobid = Integer.parseInt(nestedPath);
					for (open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							int itemid = Integer.parseInt(r.getAttributeValue(1));
							int chance = getChance(itemid);
							idAndChance.put(Integer.valueOf(itemid), Integer.valueOf(chance));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					drops.put(Integer.valueOf(mobid), idAndChance);
				}
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				open1--;
			}
		}
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}

	public Map<Integer, Map<Integer, Integer>> getDrops() {
		return drops;
	}

	public Map<Integer, List<QuestItemDropEntry>> getQuestItemDrops() {
		return questItemDrops;
	}

	public Set<Integer> getNoMesos() {
		return noMesos;
	}

	private static final int COMMON_ETC_RATE = 600000; // 60% Rate
	private static final int SUPER_BOSS_ITEM_RATE = 300000; // 30% Rate
	private static final int POTION_RATE = 20000; //2%
	private static final int ARROWS_RATE = 25000; //2.5%

	//I love you, LightPepsi...
	private static int getChance(int id) {
		switch (id / 10000) {
			case 100: // Hat
				switch (id) {
					case 1002926: // Targa hat [Malaysia boss]
					case 1002906: // Scarlion hat [Malaysia boss]
					case 1002927: // Scarlion boss hat [Malaysia boss]
					case 1002905: // Targa hat [Malaysia boss]
					case 1002357: // Zakum helmet
					case 1002390: // Zakum helmet 2
					case 1002430: // Zakum helmet 3
					case 1002972: // Auf haven
						return SUPER_BOSS_ITEM_RATE;
				}
				return 1500;
			case 103: // Earring
				switch (id) {
					case 1032062: // Element Pierce - Neo Tokyo
						return 100; // 7 Slots earring with +2 to str/dex/int/luk/ma
				}
				return 1000;
			case 105: // Overall
			case 109: // Shield
				switch (id) {
					case 1092049: // Dragon Khanjar
						return 100;
				}
				return 700;
			case 104: // Topwear
			case 106: // Pants
			case 107: // Shoes
				switch (id) {
					case 1072369: // Squachy shoe [King slime, Kerning PQ]
						return 300000; // 30%
				}
				return 800;
			case 108: // Gloves
			case 110: // Cape
				return 1000;
			case 112: // Pendant
				switch (id) {
					case 1122000: // HT Necklace
						return SUPER_BOSS_ITEM_RATE;
					case 1122011: // Timeless pendant lvl 30
					case 1122012: // Timeless pendant lvl 140
						return 800000; // 80%
				}
			case 130: // 1 Handed sword
			case 131: // 1 Handed Axe
			case 132: // 1 Handed BW
			case 137: // Wand
				switch (id) {
					case 1372049: // Zakum Tree Branch
						return 999999;
				}
				return 700;
			case 138: // Staff
			case 140: // 1 Handed sword and 2 Handed sword
			case 141: // 2 Handed axe
			case 142: // 2 Handed BW
			case 144: // Pole arm
				return 700;
			case 133: // Dagger
			case 143: // Spear
			case 145: // Bow
			case 146: // Crossbow
			case 147: // Claw
			case 148: // Knuckle
			case 149: // Gun
				return 500;
			case 204: // Scrolls
				switch (id) {
					case 2049000: // Chaos scroll
						return 150;
				}
				return 300;
			case 205: // All cure potion, Antidote, eyedrop
				return 50000; // 5%
			case 206: // Arrows
				return 30000;
			case 228: // Skillbook
				return 30000;
			case 229: // Mastery book
				switch (id) {
					case 2290096: // Maple Hero 20
						return 800000; // 80% rate for HT
					case 2290125: // Maple Hero 30
						return 100000;
				}
				return 500;
			case 233: // Bullets and capsules
				switch (id) {
					case 2330007: // Armor-Piercing bullet
						return 50;
				}
				return 500;
			case 400:
				switch (id) {
					case 4000021: // Leather
						return 50000;
					case 4001094: // Nine spirit egg
						return 999999;
					case 4001000: // Awren's glass shoe
						return 5000;
					case 4000157: // Seal meat
						return 100000; // 10%
					case 4001024: // Rubian [Guild PQ]
					case 4001023: // Key of dimension [Ludi PQ]
						return 999999; // 100%
					case 4000245: // Dragon Scale
					case 4000244: // Dragon Spirit
						return 2000;
					case 4001005: // Ancient scroll
						return 5000;
					case 4001006: // Flaming feather
						return 10000; // 1%
					case 4000017: // Pig's head
					case 4000082: // Gold smelly tooth =.="
						return 40000; // 4%
					case 4000446: // Smiling cone hat
					case 4000451: // Expressionless cone hat
					case 4000456: // Sad cone hat
						return 10000; // 1%
					case 4000459: // Black armour piece
						return 20000; // 2%
					case 4000030: // Dragon Skin
						return 60000; // 6%
					case 4000339: // High-Tier Ninja Giant Star
						return 70000; // 7%
					case 4007000: // Magic Powder (Brown)
					case 4007001: // Magic Powder (White)
					case 4007002: // Magic Powder (Blue)
					case 4007003: // Magic Powder (Green)
					case 4007004: // Magic Powder (Yellow)
					case 4007005: // Magic Powder (Purple)
					case 4007006: // Magic Powder (Red)
					case 4007007: // Magic Powder (Black)
						return 50000; // 5%
				}
				switch (id / 1000) {
					case 4000: // ETC
					case 4001: // Story book, manon cry, orbis rock, eraser, certificate
						return COMMON_ETC_RATE;
					case 4003: // Screw, Processed wood, Piece of Ice, Fairy Wing, Stiff Feather, Soft Feather
						return 200000;
					case 4004: // Crystal Ore
					case 4006: // Magic rock, summoning rock
						return 10000;
					case 4005: // Crystal, refined
						return 1000;
				}
			case 401: // mineral Ore and refined
			case 402: // Jewel ore and refined
				switch (id) {
					case 4020009: // Piece of time
						return 5000; // 0.5%
					case 4021010: // Time rock
						return SUPER_BOSS_ITEM_RATE;
				}
				return 9000;
			case 403: // Lip lock key, cracked dimension, omok, monster card
				switch (id) {
					case 4032024: // Jumper Cable [Wolf Spider]
						return 50000; // 5%
					case 4032005: // Typhon Feather
					case 4032025: // T-1 Socket Adapter [Wolf Spider]
					case 4032156: // Overload Lens - Neo Tokyo
					case 4032155: // Afterroad Caterpillar - Neo Tokyo
					case 4032161: // Eruwater Lazer gun - Neo Tokyo
					case 4032163: // Maverick booster - Neo Tokyo
					case 4032159: // Protoroad Spoiler - Neo Tokyo
						return COMMON_ETC_RATE;
					case 4032166: // Nano Plant(Y)
					case 4032167: // Nano Plant(Sigma)
					case 4032168: // Nano Plant(Omega)
						return 10000; // 3%
					case 4032158: // Twisted Radar - Neo Tokyo
					case 4032151: // Operation unit - Neo Tokyo
					case 4032180: // Eruwater Transmitter - Neo Tokyo
					case 4032164: // Portalble lazer Guidance - Neo Tokyo
						return 2000; // 0.1%
					case 4032152: // Macro Molecule Autualater - Neo Tokyo
					case 4032153: // Conductive Polymer Gain - Neo Tokyo
					case 4032154: // Calculating Domino - Neo Tokyo
						return 4000;
				}
				return 300;
			case 413: // Production stimulator
				return 6000; // 0.6%
			case 416: // attendance book, pet guide, production manual
				return 6000; // 0.6%
		}
		switch (id / 1000000) {
			case 1: // Equipmenet, for others that's not stated.
				return 999999;
			case 2:
				switch (id) {
					case 2000004: // Elixir
					case 2000005:
						return /*boss ? 999999 : */POTION_RATE; // 5%
					case 2000006: // Power Elixir
						// Gallopera has a higher rate x_X
						return /*boss ? 999999 : mobid == 9420540 ? 50000 : */POTION_RATE; // 3%
					case 2022345: // Power up Drink - Neo Tokyo
						return /*boss ? 999999 : */3000; // 0.3%
					case 2012002: // Sap of Ancient Tree
						return 40000; //4%
					case 2020013:
					case 2020015:
						return /*boss ? 999999 : */POTION_RATE;
					case 2060000:
					case 2061000:
					case 2060001:
					case 2061001:
						return ARROWS_RATE;
					case 2070000: // Subi Throwing-Stars
					case 2070001: // Wolbi Throwing-Stars
					case 2070002: // Mokbi Throwing-Stars
					case 2070003: // Kumbi Throwing-Stars
					case 2070004: // Tobi Throwing-Stars
					case 2070008: // Snowball
					case 2070009: // Wooden Top
					case 2070010: // Icicle
						return 500;
					case 2070005: // Steely Throwing-Knives"
						return 400;
					case 2070006: // Ilbi Throwing-Stars
					case 2070007: // Hwabi Throwing-Stars
						return 200;
					//case 2070011: // Maple Throwing star
					case 2070012: // Paper Fighter Plane
					case 2070013: // Orange
						return 1500;
					case 2070019: // Magic throwing star - Neo Tokyo
						return 100;
					case 2210006: // Rainbow colored shell
						return 999999;
					default: // Mana Elixir Pill, unagi.. etc
						return POTION_RATE;
				}
			case 3:
				switch (id) {
					case 3010007:
					case 3010008:
						return 500;
				}
				return 2000;
		}
		return 999999; //99.9999%
	}

	private String getContent(String line) {
		int comment = line.indexOf('#');
		if (comment == -1)
			comment = line.length();
		return line.substring(0, comment).trim();
	}

	public void populateCustomDrops(String customDropsFile, String noMesosFile, String questDropsFile) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(customDropsFile));
		try {
			while (fileScan.hasNext()) {
				String line = getContent(fileScan.nextLine());
				if (line.isEmpty())
					continue;

				Scanner lineScan = new Scanner(line);
				int mobId = lineScan.nextInt();
				int itemId = lineScan.nextInt();
				int chance;
				if (!lineScan.hasNext()) {
					chance = getChance(itemId);
				} else {
					String chanceStr = lineScan.next();
					if (chanceStr.startsWith("*"))
						chance = getChance(itemId) * Integer.parseInt(chanceStr.substring(1));
					else
						chance = Integer.parseInt(chanceStr);
				}
				lineScan.close();
				customDrops.add(mobId, itemId, chance);
			}
		} finally {
			fileScan.close();
		}

		fileScan = new Scanner(new File(noMesosFile));
		try {
			while (fileScan.hasNext()) {
				String line = getContent(fileScan.nextLine());
				if (line.isEmpty())
					continue;

				noMesos.add(Integer.valueOf(Integer.parseInt(line)));
			}
		} finally {
			fileScan.close();
		}

		fileScan = new Scanner(new File(questDropsFile));
		try {
			while (fileScan.hasNext()) {
				String line = getContent(fileScan.nextLine());
				if (line.isEmpty())
					continue;

				Scanner lineScan = new Scanner(line);
				int mobId = lineScan.nextInt();
				int itemId = lineScan.nextInt();
				short questId = lineScan.nextShort();
				int chance;
				if (!lineScan.hasNext()) {
					chance = getChance(itemId);
				} else {
					String chanceStr = lineScan.next();
					if (chanceStr.startsWith("*"))
						chance = getChance(itemId) * Integer.parseInt(chanceStr.substring(1));
					else
						chance = Integer.parseInt(chanceStr);
				}
				lineScan.close();

				List<QuestItemDropEntry> existingDrops = questItemDrops.get(Integer.valueOf(mobId));
				if (existingDrops == null) {
					existingDrops = new ArrayList<QuestItemDropEntry>();
					questItemDrops.put(Integer.valueOf(mobId), existingDrops);
				}
				existingDrops.add(new QuestItemDropEntry(itemId, chance, questId));
			}
		} finally {
			fileScan.close();
		}
	}

	private static class IdAndChance {
		public int id;
		public int chance;

		public IdAndChance(int itemid, int chance) {
			this.id = itemid;
			this.chance = chance;
		}
	}

	@SuppressWarnings("serial")
	private static class CustomDropsMap extends HashMap<Integer, List<IdAndChance>> {
		public void add(int mobid, int itemid, int chance) {
			Integer oKey = Integer.valueOf(mobid);
			List<IdAndChance> list = get(oKey);
			if (list == null) {
				list = new ArrayList<IdAndChance>();
				put(oKey, list);
			}
			list.add(new IdAndChance(itemid, chance));
		}

		public void appendRemaining(Map<Integer, Map<Integer, Integer>> drops) {
			for (Entry<Integer, List<IdAndChance>> entry : entrySet()) {
				Map<Integer, Integer> existingDrops = drops.get(entry.getKey());
				if (existingDrops == null) {
					existingDrops = new HashMap<Integer, Integer>();
					drops.put(entry.getKey(), existingDrops);
				}
				for (IdAndChance custom : entry.getValue())
					existingDrops.put(Integer.valueOf(custom.id), Integer.valueOf(custom.chance));
			}
		}
	}

	public static class QuestItemDropEntry {
		private int itemId;
		private int chance;
		private short questId;

		public QuestItemDropEntry(int itemId, int chance, short questId) {
			this.itemId = itemId;
			this.chance = chance;
			this.questId = questId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getDropChance() {
			return chance;
		}

		public short getQuestId() {
			return questId;
		}
	}
}