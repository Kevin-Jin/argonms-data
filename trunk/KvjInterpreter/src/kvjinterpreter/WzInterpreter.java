/*
 *  Sample interpreter for data files compiled from XML using KvJ
 *  Copyright (C) 2010, 2011  GoldenKevin
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
package kvjinterpreter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import kvjinterpreter.DataReader.WzType;
import kvjinterpreter.item.ItemDataReader;

public class WzInterpreter {
	public static final String binPath = "/home/kevin/KvjBin/out/"; //MUST HAVE TRAILING SLASH!
	public static final String wzFile = "Item.wz";
	private static final int id = 5000000;
	
	public static void main(String[] args) throws IOException {
		DataReader reader = DataReader.getReader(wzFile);
		
		if (reader == null) {
			throw new IllegalStateException("ERROR: Wz type '" + wzFile + "' is not recognized. Please try again.");
		}
		
		File f = new File(getPath(reader.getWzType()));
		InputStream fis = new BufferedInputStream(new FileInputStream(f));
		reader.initialize(id, new LittleEndianFileStreamReader(fis));
		System.out.println(reader.doWork());
		fis.close();
	}

	private static String getPath(WzType type) {
		String paddedId;
		switch (type) {
			case MAP:
				paddedId = String.format("%09d", id);
				return new StringBuilder(binPath).append(wzFile).append(File.separator).append("Map").append(File.separator).append("Map").append(paddedId.charAt(0)).append(File.separator).append(paddedId).append(".img.kvj").toString();
			case MOB:
				paddedId = String.format("%07d", id);
				return new StringBuilder(binPath).append(wzFile).append(File.separator).append(paddedId).append(".img.kvj").toString();
			case REACTOR:
				paddedId = String.format("%07d", id);
				return new StringBuilder(binPath).append(wzFile).append(File.separator).append(paddedId).append(".img.kvj").toString();
			default:
				return null;
		}
	}
}