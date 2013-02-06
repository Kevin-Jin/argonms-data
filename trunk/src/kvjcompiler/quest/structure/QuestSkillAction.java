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

import java.util.ArrayList;
import java.util.List;
import kvjcompiler.IStructure;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class QuestSkillAction implements IStructure {
	private final List<Short> jobs;
	private int id;
	private byte skillLevel;
	private byte masterLevel;
	private boolean onlyMasterLevel;

	public QuestSkillAction() {
		jobs = new ArrayList<Short>();
	}

	@Override
	public void setProperty(String key, String value) {
		if (key.equals("id")) {
			id = Integer.parseInt(value);
		} else if (key.equals("skillLevel")) {
			skillLevel = Byte.parseByte(value);
		} else if (key.equals("masterLevel")) {
			masterLevel = Byte.parseByte(value);
		} else if (key.equals("onlyMasterLevel")) {
			onlyMasterLevel = Integer.parseInt(value) != 0;
		}
	}

	public void addJob(short jobId) {
		jobs.add(Short.valueOf(jobId));
	}

	@Override
	public int size() {
		int size = Size.INT; //skill id
		size += Size.BYTE; //skill level
		size += Size.BYTE; //master level
		size += Size.BOOL; //only master level
		size += Size.BYTE; //amount of jobs
		size += jobs.size() * Size.SHORT; //jobs
		return size;
	}

	@Override
	public void writeBytes(LittleEndianWriter lew) {
		lew.writeInt(id);
		lew.writeByte(skillLevel);
		lew.writeByte(masterLevel);
		lew.writeBool(onlyMasterLevel);
		lew.writeByte((byte) jobs.size());
		for (Short job : jobs)
			lew.writeShort(job.shortValue());
	}
}
