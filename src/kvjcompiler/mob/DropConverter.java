/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010-2012  GoldenKevin
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

		populateCustomDrops();
		populateQuestItemDrops();
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
			case 133: // Dangger
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

	private void addToNoMesos(int mobId) {
		noMesos.add(Integer.valueOf(mobId));
	}

	//TODO: read these from a script or text file rather than hardcode
	private void populateCustomDrops() {
		//Bodyguard A mob
		customDrops.add(9400112, 4000139); //Bodyguard A's Tie Pin
		customDrops.add(9400112, 2002011, POTION_RATE * 3); //Pain Reliever
		customDrops.add(9400112, 2000004, POTION_RATE * 2); //Elixir
		//Bodyguard B mob
		customDrops.add(9400113, 4000140); //Bodyguard B's Bullet Shell
		customDrops.add(9400113, 2022027, POTION_RATE * 2); //Yakisoba (x2)
		customDrops.add(9400113, 2000004, POTION_RATE * 2); //Elixir
		customDrops.add(9400113, 2002008, POTION_RATE * 2); //Sniper Pill
		//The Boss mob
		customDrops.add(9400300, 4000141); //Big Boss's flashlight
		customDrops.add(9400300, 2000004); //Elixir
		customDrops.add(9400300, 2040813); //Cursed Scroll for Gloves for HP 30%
		customDrops.add(9400300, 2041030); //Cursed Scroll for Cape for HP 70%
		customDrops.add(9400300, 2041040); //Cursed Scroll for Cape for LUK 70%
		customDrops.add(9400300, 1072238); //Violet Snowshoes
		customDrops.add(9400300, 1032026); //Gold Emerald Earrings
		customDrops.add(9400300, 1372011); //Zhu-Ge-Liang Wand
		//Dreamy Ghost mob
		customDrops.add(9400013, 4000225); //Kimono Piece
		customDrops.add(9400013, 2000006); //Mana Elixir
		customDrops.add(9400013, 2000004); //Elixir
		customDrops.add(9400013, 2070013); //Orange
		customDrops.add(9400013, 2002005); //Sniper Potion
		customDrops.add(9400013, 2022018); //Kinoko Ramen (Roasted Pork)
		customDrops.add(9400013, 2040306); //Cursed Scroll for Earring for DEX 70%
		customDrops.add(9400013, 2043704); //Cursed Scroll for Wand for Magic Att 70%
		customDrops.add(9400013, 2044605); //Cursed Scroll for Crossbow for ATT 30%
		customDrops.add(9400013, 2041034); //Cursed Scroll for Cape for STR 70%
		customDrops.add(9400013, 1032019); //Crystal Flower Earrings
		customDrops.add(9400013, 1102013); //White Justice Cape
		customDrops.add(9400013, 1322026); //Colorful Tube
		customDrops.add(9400013, 1092015); //Steel Ancient Shield
		customDrops.add(9400013, 1382016); //Pyogo Mushroom
		customDrops.add(9400013, 1002276); //Red Falcon
		customDrops.add(9400013, 1002403); //Blue Arlic Helmet
		customDrops.add(9400013, 1472027); //Green Scarab
		//Zakum mob
		customDrops.add(8800002, 1372049); //Zakum Tree Branch
		//Horntail mob
		customDrops.add(8810018, 4001094); //Nine Spirit Egg
		customDrops.add(8810018, 2290125); //Maple Warrior 30
		//Female Boss mob
		customDrops.add(9400121, 4000138); //Lady Boss Comb
		customDrops.add(9400121, 4010006); //Gold Ore
		customDrops.add(9400121, 2000006); //Mana Elixir
		customDrops.add(9400121, 2000011); //Mana Elixir Pill
		customDrops.add(9400121, 2020016); //Cheesecake
		customDrops.add(9400121, 2022024); //Takoyaki (Octopus Ball)
		customDrops.add(9400121, 2022026); //Yakisoba
		customDrops.add(9400121, 2043705); //Cursed Scroll for Wand for Magic Att 30%
		customDrops.add(9400121, 2040716); //Cursed Scroll for Shoes for Speed 30%
		customDrops.add(9400121, 2040908); //Cursed Scroll for Shield for HP 70%
		customDrops.add(9400121, 2040510); //Cursed Scroll for Overall Armor for DEF 70%
		customDrops.add(9400121, 1072239); //Yellow Snowshoes
		customDrops.add(9400121, 1422013); //Leomite
		customDrops.add(9400121, 1402016); //Devil's Sunrise
		customDrops.add(9400121, 1442020); //Hellslayer
		customDrops.add(9400121, 1432011); //Fairfrozen
		customDrops.add(9400121, 1332022); //Angelic Betrayal
		customDrops.add(9400121, 1312015); //Bipennis
		customDrops.add(9400121, 1382010); //Dark Ritual
		customDrops.add(9400121, 1372009); //Magicodar
		customDrops.add(9400121, 1082085); //Red Willow
		customDrops.add(9400121, 1332022); //Angelic Betrayal
		customDrops.add(9400121, 1472033); //Casters
		//Wolf Spider mob
		customDrops.add(9400545, 4032024); //Jumper Cable
		customDrops.add(9400545, 4032025); //T-1 Socket Adapter
		customDrops.add(9400545, 4020006); //Topaz Ore
		customDrops.add(9400545, 4020008); //Black Crystal Ore
		customDrops.add(9400545, 4010001); //Steel Ore
		customDrops.add(9400545, 4004001); //Wisdom Crystal Ore
		customDrops.add(9400545, 2070006); //Ilbi Throwing Star
		customDrops.add(9400545, 2044404); //Cursed Scroll for Pole Arm for ATT 70%
		customDrops.add(9400545, 2044702); //Scroll for Claw for ATT 10%
		customDrops.add(9400545, 2044305); //Cursed Scroll for Spear for ATT 30^
		customDrops.add(9400545, 1102029); //White Seraph Cape
		customDrops.add(9400545, 1032023); //Strawberry Earrings
		customDrops.add(9400545, 1402004); //Blue Screamer
		customDrops.add(9400545, 1072210); //Red Rivers Boots
		customDrops.add(9400545, 1040104); //Orihalcon Platine
		customDrops.add(9400545, 1060092); //Orihalcon Platine Pants
		customDrops.add(9400545, 1082129); //Purple Imperial
		customDrops.add(9400545, 1442008); //The Gold Dragon
		customDrops.add(9400545, 1072178); //Purple Enigma Shoes
		customDrops.add(9400545, 1050092); //Green Oriental Fury Coat
		customDrops.add(9400545, 1002271); //Green Galaxy
		customDrops.add(9400545, 1051053); //Red Requierre
		customDrops.add(9400545, 1382008); //Kage
		customDrops.add(9400545, 1002275); //Blue Falcon
		customDrops.add(9400545, 1051082); //Red Anes
		customDrops.add(9400545, 1050064); //Dark Linnex
		customDrops.add(9400545, 1472028); //Blue Scarab
		customDrops.add(9400545, 1072193); //Brown Osfa Boots
		customDrops.add(9400545, 1072172); //Green Pirate Boots
		customDrops.add(9400545, 1002285); //Blood Nightfox

		//Todd's How-to-Hunt quest
		customDrops.add(9409000, 4000300, 800000); //Tutorial Leatty => Letty's Hairball
		customDrops.add(9409001, 4000301, 800000); //Tutorial Drumming Rabbit => Toy Drum
		addToNoMesos(9409000);
		addToNoMesos(9409001);

		//Kerning PQ
		customDrops.add(9300000, 4001008, 1000000); //Jr. Necki (PC) => Pass
		customDrops.add(9300001, 4001007, 1000000); //Ligator (PC) => Coupon
		customDrops.add(9300002, 4001008, 1000000); //Curse Eye (PC) => Pass
		customDrops.add(9300003, 4001008, 1000000); //King Slime (PC) => Pass
		addToNoMesos(9300000); //Jr. Necki (PC)
		addToNoMesos(9300001); //Ligator (PC)
		addToNoMesos(9300002); //Curse Eye (PC)
		addToNoMesos(9300003); //King Slime (PC)

		//Magician's 2nd job advancement challenge
		customDrops.add(9000001, 4031013, 1000000); //Curse Eye 2 => Dark Marble
		customDrops.add(9000002, 4031013, 1000000); //Horned Mushroom 2 => Dark Marble
		//Warrior's 2nd job advancement challenge
		customDrops.add(9000100, 4031013, 1000000); //Fire Boar 2 => Dark Marble
		customDrops.add(9000101, 4031013, 1000000); //Lupin 2 => Dark Marble
		//Bowmen's 2nd job advancement challenge
		customDrops.add(9000200, 4031013, 1000000); //Evil Eye 2 => Dark Marble
		customDrops.add(9000201, 4031013, 1000000); //Zombie Mushroom 2 => Dark Marble
		//Thief's 2nd job advancement challenge
		customDrops.add(9000300, 4031013, 1000000); //Cold Eye 2 => Dark Marble
		customDrops.add(9000301, 4031013, 1000000); //Blue Mushroom 2 => Dark Marble
		//How to Become a Brawler quest
		customDrops.add(9001006, 4031856, 1000000); //OctoPirate => Potent Power Crystal
		//How to Become a Gunslinger quest
		customDrops.add(9001005, 4031857, 1000000); //OctoPirate => Potent Wind Crystal

		//Henesys PQ
		addToNoMesos(9300062); //Flyeye
		addToNoMesos(9300063); //Stirge
		addToNoMesos(9300064); //Goblin Fire
		addToNoMesos(9300081); //Flyeye
		addToNoMesos(9300082); //Stirge
		addToNoMesos(9300083); //Goblin Fire
	}

	private void addQuestItemDrop(int mobId, int itemId, int chance, int questId) {
		List<QuestItemDropEntry> existingDrops = questItemDrops.get(Integer.valueOf(mobId));
		if (existingDrops == null) {
			existingDrops = new ArrayList<QuestItemDropEntry>();
			questItemDrops.put(Integer.valueOf(mobId), existingDrops);
		}
		existingDrops.add(new QuestItemDropEntry(itemId, chance, (short) questId));
	}

	private void addQuestItemDrop(int mobId, int itemId, int questId) {
		addQuestItemDrop(mobId, itemId, getChance(itemId), questId);
	}

	private void populateQuestItemDrops() {
		addQuestItemDrop(2230102, 4031155, 2071);
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

		public void add(int mobid, int itemid) {
			add(mobid, itemid, getChance(itemid));
		}

		public void appendCustomDrops(int mobid, Map<Integer, Integer> originals) {
			List<IdAndChance> list = get(Integer.valueOf(mobid));
			if (list != null) {
				Iterator<IdAndChance> iter = list.iterator();
				while (iter.hasNext()) {
					IdAndChance custom = iter.next();
					originals.put(Integer.valueOf(custom.id), Integer.valueOf(custom.chance));
					iter.remove();
				}
				remove(Integer.valueOf(mobid));
			}
		}

		public void appendRemaining(Map<Integer, Map<Integer, Integer>> drops) {
			for (Entry<Integer, List<IdAndChance>> entry : entrySet()) {
				Map<Integer, Integer> retList = new HashMap<Integer, Integer>();
				Iterator<IdAndChance> iter = entry.getValue().iterator();
				while (iter.hasNext()) {
					IdAndChance custom = iter.next();
					retList.put(Integer.valueOf(custom.id), Integer.valueOf(custom.chance));
					iter.remove();
				}
				drops.put(entry.getKey(), retList);
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