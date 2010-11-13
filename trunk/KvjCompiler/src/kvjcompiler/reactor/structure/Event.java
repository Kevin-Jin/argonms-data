/*
 *  KvJ Compiler for XML WZ data files
 *  Copyright (C) 2010  GoldenKevin
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

public class Event implements IStructure {
	private int eventid;
	private int type;
	private int nextState;
	
	//item event only
	private int itemid, quantity;
	private int ltx, lty;
	private int rbx, rby;
	
	public Event(int id) {
		this.eventid = id;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("type")) {
			this.type = Integer.parseInt(value);
		} else if (key.equals("state")) {
			this.nextState = Integer.parseInt(value);
		} else if (key.equals("0")) {
			this.itemid = Integer.parseInt(value);
		} else if (key.equals("1")) {
			this.quantity = Integer.parseInt(value);
		} else if (key.equals("lt")) {
			String[] c = value.split(",");
			ltx = Integer.parseInt(c[0]);
			lty = Integer.parseInt(c[1]);
		} else if (key.equals("rb")) {
			String[] c = value.split(",");
			rbx = Integer.parseInt(c[0]);
			rby = Integer.parseInt(c[1]);
		} else {
			System.out.println("WARNING: Unhandled property " + key + " in event " + eventid + ".");
		}
	}
	
	public int size() {
		int size = Size.INT; //eventid
		size += Size.INT; //type
		size += Size.INT; //nextState
		if (isItemEvent()) {
			size += Size.INT; //itemid
			size += Size.INT; //quantity
			size += Size.INT; //ltx
			size += Size.INT; //lty
			size += Size.INT; //rbx
			size += Size.INT; //rby
		}
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(eventid);
		lew.writeInt(type);
		lew.writeInt(nextState);
		if (isItemEvent()) {
			lew.writeInt(itemid);
			lew.writeInt(quantity);
			lew.writeInt(ltx);
			lew.writeInt(lty);
			lew.writeInt(rbx);
			lew.writeInt(rby);
		}
	}
	
	public boolean isItemEvent() {
		return (type == 100);
	}
}