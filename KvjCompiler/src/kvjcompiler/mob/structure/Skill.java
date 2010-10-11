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

public class Skill implements IStructure {
	private int skillid;
	private int skill;
	private int level;
	
	public Skill(int id) {
		this.skillid = id;
	}
	
	public void setProperty(String key, String value) {
		if (key.equals("skill")) {
			this.skill = Integer.parseInt(value);
		} else if (key.equals("level")) {
			this.level = Integer.parseInt(value);
		}/* else {
			System.out.println("WARNING: Unhandled property " + key + " in skill " + skillid + ".");
		}*/
	}
	
	public int size() {
		int size = Size.INT; //skillid
		size += Size.INT; //skill
		size += Size.INT; //level
		return size;
	}
	
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(skillid);
		lew.writeInt(skill);
		lew.writeInt(level);
	}
}
