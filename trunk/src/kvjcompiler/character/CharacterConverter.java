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

package kvjcompiler.character;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.item.ItemConverter;

/**
 *
 * @author GoldenKevin
 */
public class CharacterConverter extends Converter {
	private static final byte
		WHOLE_PRICE = 1,
		IS_TRADE_BLOCKED = 3,
		IS_ONE_ONLY = 4,
		IS_QUEST_ITEM = 5,
		BONUS_STAT = 6,
		CASH = 14,
		REQ_STAT = 18,
		UPGRADE_SLOTS = 19,

		TAMING_MOB_ID = 28
	;

	private String beautyCategory;

	@Override
	public String getWzName() {
		return "Character.wz";
	}

	@Override
	public void compile(String outPath, String internalPath, String imgName, XMLStreamReader r) throws XMLStreamException, IOException {
		if (r != null) {
			//equips
			if (beautyCategory != null) {
				//first, cleanup last beauty category if there was one.
				System.out.println(getWzName() + File.separatorChar + beautyCategory + " done.");
				System.err.println("Complete.");
				fos.close();
				beautyCategory = null;
			}

			startCompile(outPath, internalPath, imgName, r);
			finalizeCompile(internalPath, imgName);
		} else {
			//beauty
			File binDir = new File(outPath + getWzName() + File.separatorChar);
			if (!binDir.exists())
				if (!binDir.mkdirs())
					throw new IllegalStateException("ERROR: Could not create compiled directory " + binDir.getAbsolutePath());

			if (beautyCategory == null) {
				//first iteration in a beauty category (after an equip category).
				System.err.print("Building " + internalPath + "...\t");
				fos = new BufferedOutputStream(new FileOutputStream(binDir.getAbsolutePath() + File.separatorChar + internalPath + ".kvj"));
				beautyCategory = internalPath;
			} else if (!beautyCategory.equals(internalPath)) {
				//first iteration in a beauty category after another beauty category.
				//first, cleanup last beauty category.
				System.out.println(getWzName() + File.separatorChar + beautyCategory + " done.");
				System.err.println("Complete.");
				fos.close();

				System.err.print("Building " + internalPath + "...\t");
				fos = new BufferedOutputStream(new FileOutputStream(binDir.getAbsolutePath() + File.separatorChar + internalPath + ".kvj"));
				beautyCategory = internalPath;
			}
			fos.write(new LittleEndianWriter(Size.SHORT).writeShort(Short.parseShort(imgName.substring(0, imgName.lastIndexOf(".img")))).toArray());
		}
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		traverseBlock(nestedPath);
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("cash")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, CASH).toArray());
			} else if (dirs[1].equals("tuc")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE, UPGRADE_SLOTS).writeByte(Byte.parseByte(value)).toArray());
			} else if (dirs[1].equals("price")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, WHOLE_PRICE).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("tradeBlock")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_TRADE_BLOCKED).toArray());
			} else if (dirs[1].equals("only")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_ONE_ONLY).toArray());
			} else if (dirs[1].equals("quest")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, IS_QUEST_ITEM).toArray());
			} else if (dirs[1].equals("tamingMob")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.BYTE, TAMING_MOB_ID).writeByte(Byte.parseByte(value)).toArray());
			} else {
				if (dirs[1].length() > 2) {
					String prefix = dirs[1].substring(0, 3);
					if (prefix.equals("inc") || prefix.equals("req")) {
						LittleEndianWriter lew = new LittleEndianWriter(Size.HEADER + Size.BYTE + Size.SHORT, prefix.equals("inc") ? BONUS_STAT : REQ_STAT);
						byte stat = ItemConverter.getStat(dirs[1].substring(3, dirs[1].length()));
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
}
