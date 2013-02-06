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

package kvjcompiler.reactor.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class Event implements IStructure {
	private final byte eventid;
	private byte type;
	private byte nextState;

	//item event only
	private int itemid;
	private short quantity;
	private short ltx, lty;
	private short rbx, rby;

	public Event(byte id) {
		this.eventid = id;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("type")) {
			this.type = Byte.parseByte(value);
		} else if (key.equals("state")) {
			this.nextState = Byte.parseByte(value);
		} else if (key.equals("0")) {
			this.itemid = Integer.parseInt(value);
		} else if (key.equals("1")) {
			this.quantity = Short.parseShort(value);
		} else if (key.equals("lt")) {
			String[] c = value.split(",");
			ltx = Short.parseShort(c[0]);
			lty = Short.parseShort(c[1]);
		} else if (key.equals("rb")) {
			String[] c = value.split(",");
			rbx = Short.parseShort(c[0]);
			rby = Short.parseShort(c[1]);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in event " + eventid + ".");
		}
	}

	@Override
	public int size() {
		int size = Size.BYTE; //eventid
		size += Size.BYTE; //type
		size += Size.BYTE; //nextState
		if (isItemEvent()) {
			size += Size.INT; //itemid
			size += Size.SHORT; //quantity
			size += Size.SHORT; //ltx
			size += Size.SHORT; //lty
			size += Size.SHORT; //rbx
			size += Size.SHORT; //rby
		}
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeByte(eventid);
		lew.writeByte(type);
		lew.writeByte(nextState);
		if (isItemEvent()) {
			lew.writeInt(itemid);
			lew.writeShort(quantity);
			lew.writeShort(ltx);
			lew.writeShort(lty);
			lew.writeShort(rbx);
			lew.writeShort(rby);
		}
	}

	public boolean isItemEvent() {
		return (type == 100);
	}
}