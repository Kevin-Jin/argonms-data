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

package kvjcompiler.quest.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class QuestItem implements IStructure {
	private int id;
	private short count;
	private int prop; //I believe Nexon meant to name it prob...
	private byte gender;
	private short job;
	private int dateExpire;
	private int period;

	public QuestItem() {
		this.prop = 100;
		this.gender = 2;
		this.job = -1;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("id")) {
			id = Integer.parseInt(value);
		} else if (key.equals("count")) {
			count = Short.parseShort(value);
		} else if (key.equals("prop")) {
			prop = Integer.parseInt(value);
		} else if (key.equals("gender")) {
			gender = Byte.parseByte(value);
		} else if (key.equals("job")) {
			job = Short.parseShort(value);
		} else if (key.equals("dateExpire")) {
			//really a string (YYYYMMDD), but it's 4 bytes vs 8 chars...
			dateExpire = Integer.parseInt(value);
		} else if (key.equals("period")) {
			period = Integer.parseInt(value);
		}/* else if (key.equals("var")) { //we don't care about these
			var = Byte.parseByte(value);
		} else if (key.equals("name")) {
			name = Integer.parseInt(value) > 0;
		}*/
	}

	@Override
	public int size() {
		int size = Size.INT; //itemid
		size += Size.SHORT; //quantity
		if (prop != 100)
			size += Size.BYTE + Size.INT; //prop
		if (gender != 2)
			size += Size.BYTE + Size.BYTE; //gender
		if (job != -1)
			size += Size.BYTE + Size.SHORT; //job
		if (dateExpire != 0)
			size += Size.BYTE + Size.INT; //dateExpiration
		if (period != 0)
			size += Size.BYTE + Size.INT; //expiration
		size += Size.BYTE; //end struct
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(id);
		lew.writeShort(count);
		if (prop != 100)
			lew.writeByte(QuestBehaviors.ITEM_PROP).writeInt(prop);
		if (gender != 2)
			lew.writeByte(QuestBehaviors.ITEM_GENDER).writeByte(gender);
		if (job != -1)
			lew.writeByte(QuestBehaviors.ITEM_JOB).writeShort(job);
		if (dateExpire != 0)
			lew.writeByte(QuestBehaviors.ITEM_DATE_EXPIRE).writeInt(dateExpire);
		if (period != 0)
			lew.writeByte(QuestBehaviors.ITEM_PERIOD).writeInt(period);
		lew.writeByte(QuestBehaviors.END_ITEM);
	}
}
