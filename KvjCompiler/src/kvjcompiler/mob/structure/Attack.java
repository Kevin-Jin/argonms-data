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
package kvjcompiler.mob.structure;

import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

public class Attack implements IStructure {
	private int attackid;
	private boolean deadlyAttack;
	private int mpBurn;
	private int diseaseSkill;
	private int diseaseLevel;
	private int conMp;
	
	public Attack(int id) {
		this.attackid = id;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("deadlyAttack")) {
			this.deadlyAttack = Integer.parseInt(value) > 0;
		} else if (key.equals("mpBurn")) {
			this.mpBurn = Integer.parseInt(value);
		} else if (key.equals("disease")) {
			this.diseaseSkill = Integer.parseInt(value);
		} else if (key.equals("level")) {
			this.diseaseLevel = Integer.parseInt(value);
		} else if (key.equals("conMP")) {
			this.conMp = Integer.parseInt(value);
		}/* else {
			System.out.println("WARNING: Unhandled property " + key + " in attack " + attackid + ".");
		}*/
	}
	
	public int size() {
		int size = Size.INT; //attackid
		size += Size.BOOL; //deadlyAttack
		size += Size.INT; //mpBurn
		size += Size.INT; //diseaseSkill
		size += Size.INT; //diseaseLevel
		size += Size.INT; //conMp
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(attackid);
		lew.writeBool(deadlyAttack);
		lew.writeInt(mpBurn);
		lew.writeInt(diseaseSkill);
		lew.writeInt(diseaseLevel);
		lew.writeInt(conMp);
	}
}
