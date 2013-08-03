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

package kvjcompiler.cashshop.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class Commodity implements IStructure {
	private int sn;
	private int itemDataId;
	private short quantity;
	private int price;
	private byte period;
	private byte gender;
	private boolean onSale;

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("SN")) {
			sn = Integer.parseInt(value);
		} else if (key.equals("ItemId")) {
			itemDataId = Integer.parseInt(value);
		} else if (key.equals("Count")) {
			quantity = Short.parseShort(value);
		} else if (key.equals("Price")) {
			price = Integer.parseInt(value);
		} else if (key.equals("Period")) {
			period = Byte.parseByte(value);
		} else if (key.equals("Priority")) {
			//we don't care about this value?
		} else if (key.equals("Gender")) {
			//for packet editing detection
			gender = Byte.parseByte(value);
		} else if (key.equals("OnSale")) {
			//for packet editing detection
			onSale = (Integer.parseInt(value) == 1);
		} else if (key.equals("Class")) {
			//we don't care about this value?
		} else if (key.equals("PbCash")) {
			//we don't care about this value?
			//for pets - always 30
		} else if (key.equals("PbPoint")) {
			//we don't care about this value?
			//for pets - always 30
		} else if (key.equals("PbGift")) {
			//we don't care about this value?
			//for pets - always 30
		}
	}

	@Override
	public int size() {
		return Size.INT + Size.INT + Size.SHORT + Size.INT + Size.BYTE + Size.BYTE + Size.BOOL;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(sn);
		lew.writeInt(itemDataId);
		lew.writeShort(quantity);
		lew.writeInt(price);
		lew.writeByte(period);
		lew.writeByte(gender);
		lew.writeBool(onSale);
	}
}
