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

package kvjcompiler.item;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.DataType;
import kvjcompiler.Effects;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.item.structure.*;

/**
 *
 * @author GoldenKevin
 */
public class ItemConverter extends Converter {
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
		RECOVER = 10,
		RAND_STAT = 11,
		PREVENT_SLIP = 12,
		WARM_SUPPORT = 13,
		CASH = 14,
		OPERATING_HOURS = 15,
		SKILL = 16,
		UNIT_PRICE = 17,
		REQ_STAT = 18,
		UPGRADE_SLOTS = 19,
		SCROLL_REQUIREMENTS = 20,
		ITEM_EFFECT = 21,
		TRIGGER_ITEM = 22,
		MESO_VALUE = 23,

		PET_COMMAND = 24,
		PET_HUNGER = 25,
		PET_PERIOD = 26,
		PET_EVOLVE = 27
	;

	private File binDir;
	private String current;
	private boolean pet;
	private Map<Integer, Integer> evolIds;
	private Map<Integer, Integer> evolProbs;

	@Override
	public String getWzName() {
		return "Item.wz";
	}

	@Override
	public void compile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		this.evolIds = new HashMap<Integer, Integer>();
		this.evolProbs = new HashMap<Integer, Integer>();
		startCompile(outPath, internalPath, imgName, r);

		for (Entry<Integer, Integer> pair : evolIds.entrySet())
			System.out.println("Evolve petid #" + pair.getKey() + " (value " + pair.getValue() + ") not paired with a probability.");

		for (Entry<Integer, Integer> pair : evolProbs.entrySet())
			System.out.println("Evolve probability #" + pair.getKey() + " (value " + pair.getValue() + ") not paired with a pet id.");

		finalizeCompile(internalPath, imgName);
	}

	@Override
	protected void startCompile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		System.err.print("Building " + internalPath + imgName + "...\t");

		if (r.getEventType() != XMLStreamReader.START_DOCUMENT)
			throw new IllegalStateException("ERROR: Received an XML that has already been partially read.");

		r.next();
		if (DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || !r.getAttributeValue(0).equals(imgName))
			throw new IllegalStateException("ERROR: Received a non-WZ XML file.");

		this.r = r;

		String id = imgName.substring(0, imgName.lastIndexOf(".img"));
		if (!isNumber(id)) {
			return;
		} else {
			int parsed = Integer.parseInt(id);
			pet = (parsed >= 5000000 && parsed <= 5000100);
			if (pet) {
				this.binDir = new File(outPath + getWzName() + File.separatorChar + internalPath);
				if (!binDir.exists())
					if (!binDir.mkdirs())
						throw new IllegalStateException("ERROR: Could not create compiled directory " + binDir.getAbsolutePath());
				this.fos = new BufferedOutputStream(new FileOutputStream(binDir.getAbsolutePath() + File.separatorChar + imgName + ".kvj"));
			} else {
				this.binDir = new File(outPath + getWzName() + File.separatorChar + internalPath + File.separatorChar + imgName);
				if (!binDir.exists())
					if (!binDir.mkdirs())
						throw new IllegalStateException("ERROR: Could not create compiled directory " + binDir.getAbsolutePath());
			}
		}

		traverseBlock("");
	}

	@Override
	protected void finalizeCompile(String internalPath, String imgName) throws IOException, XMLStreamException {
		try {
			if (r.getEventType() != XMLStreamReader.END_ELEMENT || DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || r.next() != XMLStreamReader.END_DOCUMENT) {
				System.out.println("Skipping " + getWzName() + File.separatorChar + internalPath + imgName);
				System.err.println("Skipped.");
				return;
			}

			System.out.println(getWzName() + File.separatorChar + internalPath + imgName + " done.");
			System.err.println("Complete.");
		} finally {
			if (fos != null) {
				this.fos.close();
				this.fos = null;
			}
			if (r != null) {
				this.r.close();
				this.r = null;
			}
		}
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		String[] dirs = nestedPath.split("/");

		if (!pet) {
			if (handleItemDir(dirs))
				return;
		} else {
			if (handlePetDir(dirs))
				return;
		}

		traverseBlock(nestedPath);
	}

	private boolean handleItemDir(String[] dirs) throws XMLStreamException, IOException {
		if (!dirs[0].equals(current))
			if (isNumber(dirs[0])) {
				if (this.fos != null)
					this.fos.close();
				this.fos = new BufferedOutputStream(new FileOutputStream(binDir.getAbsolutePath() + File.separatorChar + dirs[0] + ".kvj"));
				this.current = dirs[0];
			}

		if (dirs.length > 1) {
			if (dirs[1].equals("req")) {
				ScrollRequirements sr;
				LittleEndianWriter lew;
				for (int open1 = 1, event; open1 > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open1++;
						sr = new ScrollRequirements();
						for (int open = 1; open > 0;) {
							event = r.next();
							if (event == XMLStreamReader.START_ELEMENT) {
								open++;
								sr.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
							} else if (event == XMLStreamReader.END_ELEMENT) {
								open--;
							}
						}
						lew = new LittleEndianWriter(Size.HEADER + sr.size(), SCROLL_REQUIREMENTS);
						sr.writeBytes(lew);
						fos.write(lew.toArray());
					}
					if (event == XMLStreamReader.END_ELEMENT) {
						open1--;
					}
				}
				return true;
			} else if (dirs[1].equals("mob")) {
				SummonMob m;
				LittleEndianWriter lew;
				for (int open1 = 1, event; open1 > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open1++;
						m = new SummonMob();
						for (int open = 1; open > 0;) {
							event = r.next();
							if (event == XMLStreamReader.START_ELEMENT) {
								open++;
								m.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
							} else if (event == XMLStreamReader.END_ELEMENT) {
								open--;
							}
						}
						lew = new LittleEndianWriter(Size.HEADER + m.size(), SUMMON);
						m.writeBytes(lew);
						fos.write(lew.toArray());
					}
					if (event == XMLStreamReader.END_ELEMENT) {
						open1--;
					}
				}
				return true;
			} else if (dirs[1].equals("spec")) {
				ItemEffect e = new ItemEffect();
				for (int open = 1, event; open > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open++;
						e.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						if (DataType.getFromString(r.getLocalName()).isDirectory()) {
							for (int open1 = 1; open1 > 0;) {
								event = r.next();
								if (event == XMLStreamReader.START_ELEMENT)
									open1++;
								else if (event == XMLStreamReader.END_ELEMENT)
									open1--;
							}
						}
					}
					if (event == XMLStreamReader.END_ELEMENT) {
						open--;
					}
				}
				LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + e.size() + Size.BYTE, ITEM_EFFECT);
				e.writeBytes(lew);
				lew.writeByte(Effects.END_EFFECT);
				fos.write(lew.toArray());
				return true;
			}
		}
		return false;
	}

	private boolean handlePetDir(String[] dirs) throws XMLStreamException, IOException {
		if (dirs[0].equals("interact")) {
			PetCommand c;
			LittleEndianWriter lew;
			for (int open1 = 1, event; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					c = new PetCommand(Byte.parseByte(r.getAttributeValue(0)));
					for (int open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							c.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					lew = new LittleEndianWriter(Size.HEADER + c.size(), PET_COMMAND);
					c.writeBytes(lew);
					fos.write(lew.toArray());
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
		String[] dirs = nestedPath.split("/");
		if (!pet)
			handleItemProperty(dirs, value);
		else
			handlePetProperty(dirs, value);
	}

	private void handleItemProperty(String[] dirs, String value) throws IOException {
		if (dirs[1].equals("info")) {
			if (dirs[2].equals("stateChangeItem")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, TRIGGER_ITEM).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("slotMax")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.SHORT, SLOT_MAX).writeShort(Short.parseShort(value)).toArray());
			} else if (dirs[2].equals("meso")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, MESO_VALUE).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("price")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, WHOLE_PRICE).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("unitPrice")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.DOUBLE, UNIT_PRICE).writeDouble(Double.parseDouble(value)).toArray());
			} else if (dirs[2].equals("tradeBlock")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_TRADE_BLOCKED).toArray());
			} else if (dirs[2].equals("quest")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_QUEST_ITEM).toArray());
			} else if (dirs[2].equals("only")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_ONE_ONLY).toArray());
			} else if (dirs[2].equals("tuc")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE, UPGRADE_SLOTS).writeByte(Byte.parseByte(value)).toArray());
			} else if (dirs[2].equals("success")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, SUCCESS).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("cursed")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, CURSED).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("recover")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, RECOVER).toArray());
			} else if (dirs[2].equals("randstat")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, RAND_STAT).toArray());
			} else if (dirs[2].equals("preventslip")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, PREVENT_SLIP).toArray());
			} else if (dirs[2].equals("warmsupport")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, WARM_SUPPORT).toArray());
			} else if (dirs[2].equals("cash")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, CASH).toArray());
			} else if (dirs[2].equals("time")) {
				if (!isNumber(value)) {
					String[] splitted = value.split(":");
					String[] hours = splitted[1].split("-");
					byte day = getDayByteFromString(splitted[0]);
					byte startHour = Byte.parseByte(hours[0]);
					byte endHour = Byte.parseByte(hours[1]);
					fos.write(new LittleEndianWriter(Size.HEADER + 3 * Size.BYTE, OPERATING_HOURS).writeByte(day).writeByte(startHour).writeByte(endHour).toArray());
				} //5281000 ("Passed Gas") and 5281001 ("Floral Scent") time is how many seconds the special effects last on screen?
			} else if (dirs[2].equals("skill")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, SKILL).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[2].equals("maxLevel")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE + Size.SHORT, REQ_STAT).writeByte(Effects.MaxLevel).writeShort(Short.parseShort(value)).toArray());
			} else {
				if (dirs[2].length() > 2) {
					String prefix = dirs[2].substring(0, 3);
					if (prefix.equals("inc") || prefix.equals("req")) {
						LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + Size.BYTE + Size.SHORT, prefix.equals("inc") ? BONUS_STAT : REQ_STAT);
						byte stat = getStat(dirs[2].substring(3, dirs[2].length()));
						if (stat != -1) {
							lew.writeByte(stat);
							lew.writeShort(Short.parseShort(value));
							fos.write(lew.toArray());
						}
					}
				}
			}
		}
	}

	private void handlePetProperty(String[] dirs, String value) throws IOException {
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("hungry")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, PET_HUNGER).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("life")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE, PET_PERIOD).writeByte(Byte.parseByte(value)).toArray());
			} else if (dirs[1].equals("evol")) {
				//kinda unnecessary... if evolNo == 0, or is not in the file at all, then we know it's not evolvable...
			} else if (dirs[1].equals("evolNo")) {
				//unnecessary... once you processed the entire binary, you can realize how many evolve paths there are...
			} else if (dirs[1].length() > 8 && dirs[1].substring(0, 8).equals("evolProb")) {
				int num = Integer.parseInt(dirs[1].substring(8, dirs[1].length()));
				if (evolIds.containsKey(Integer.valueOf(num)))
					fos.write(new LittleEndianWriter(Size.HEADER + 2 * Size.INT, PET_EVOLVE).writeInt(evolIds.remove(Integer.valueOf(num)).intValue()).writeInt(Integer.parseInt(value)).toArray());
				else
					evolProbs.put(Integer.valueOf(num), Integer.valueOf(value));
			} else if (dirs[1].length() > 4 && dirs[1].substring(0, 4).equals("evol")) {
				String end = dirs[1].substring(4, dirs[1].length());
				if (isNumber(end)) {
					int num = Integer.parseInt(end);
					if (evolProbs.containsKey(Integer.valueOf(num)))
						fos.write(new LittleEndianWriter(Size.HEADER + 2 * Size.INT, PET_EVOLVE).writeInt(Integer.parseInt(value)).writeInt(evolProbs.remove(Integer.valueOf(num)).intValue()).toArray());
					else
						evolIds.put(Integer.valueOf(num), Integer.valueOf(value));
				}
			}
		}
	}

	private static byte getDayByteFromString(String str) {
		if (str.equals("SUN"))
			return Calendar.SUNDAY;
		else if (str.equals("MON"))
			return Calendar.MONDAY;
		else if (str.equals("TUE"))
			return Calendar.TUESDAY;
		else if (str.equals("WED"))
			return Calendar.WEDNESDAY;
		else if (str.equals("THU"))
			return Calendar.THURSDAY;
		else if (str.equals("FRI"))
			return Calendar.FRIDAY;
		else if (str.equals("SAT"))
			return Calendar.SATURDAY;
		else if (str.equals("HOL"))
			return 8;
		else
			return 0;
	}

	public static byte getStat(String str) {
		if (str.equals("STR"))
			return Effects.STR;
		else if (str.equals("DEX"))
			return Effects.DEX;
		else if (str.equals("INT"))
			return Effects.INT;
		else if (str.equals("LUK"))
			return Effects.LUK;
		else if (str.equals("PAD"))
			return Effects.PAD;
		else if (str.equals("PDD"))
			return Effects.PDD;
		else if (str.equals("MAD"))
			return Effects.MAD;
		else if (str.equals("MDD"))
			return Effects.MDD;
		else if (str.equals("ACC"))
			return Effects.ACC;
		else if (str.equals("EVA"))
			return Effects.EVA;
		else if (str.equals("MHP"))
			return Effects.MHP;
		else if (str.equals("MMP"))
			return Effects.MMP;
		else if (str.equals("Speed"))
			return Effects.Speed;
		else if (str.equals("Jump"))
			return Effects.Jump;
		else if (str.equals("Level"))
			return Effects.Level;
		return -1;
	}
}
