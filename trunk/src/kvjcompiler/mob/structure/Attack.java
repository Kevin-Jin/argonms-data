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

package kvjcompiler.mob.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class Attack implements IStructure {
	private final byte attackid;
	private boolean deadlyAttack;
	private short mpBurn;
	private byte diseaseSkill;
	private byte diseaseLevel;
	private int conMp;

	public Attack(byte id) {
		this.attackid = id;
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("deadlyAttack")) {
			this.deadlyAttack = Integer.parseInt(value) > 0;
		} else if (key.equals("mpBurn")) { //how can it burn more than 30000 mp from a character???
			//stupid Bodyguard B... just set his MP drain to 32767
			this.mpBurn = (short) Math.min(Integer.parseInt(value), Short.MAX_VALUE);
		} else if (key.equals("disease")) {
			this.diseaseSkill = Byte.parseByte(value);
		} else if (key.equals("level")) {
			this.diseaseLevel = Byte.parseByte(value);
		} else if (key.equals("conMP")) {
			this.conMp = Integer.parseInt(value);
		}/* else {
			System.out.println("WARNING: Unhandled property " + key + " in attack " + attackid + ".");
		}*/
	}

	@Override
	public int size() {
		int size = Size.BYTE; //attackid
		size += Size.BOOL; //deadlyAttack
		size += Size.SHORT; //mpBurn
		size += Size.BYTE; //diseaseSkill
		size += Size.BYTE; //diseaseLevel
		size += Size.INT; //conMp
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeByte(attackid);
		lew.writeBool(deadlyAttack);
		lew.writeShort(mpBurn);
		lew.writeByte(diseaseSkill);
		lew.writeByte(diseaseLevel);
		lew.writeInt(conMp);
	}
}
