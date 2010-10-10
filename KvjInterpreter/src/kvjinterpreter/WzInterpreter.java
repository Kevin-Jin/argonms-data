/*
 *  Sample interpreter for data files compiled from XML using KvJ
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
package kvjinterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import kvjinterpreter.DataReader.WzType;
import kvjinterpreter.map.MapDataReader;

public class WzInterpreter {
	private static final String binPath = "/home/kevin/KvjBin/out/"; //MUST HAVE TRAILING SLASH!
	private static final String wzFile = "Map.wz";
	private static final int id = 102020100;
	
	public static void main(String[] args) throws IOException {
		DataReader reader = DataReader.getReader(wzFile);
		
		if (reader == null) {
			throw new IllegalStateException("ERROR: Wz type '" + wzFile + "' is not recognized. Please try again.");
		}
		
		if (reader.getWzType() == WzType.MAP) {
			File f = new File(getMapPath());
			FileInputStream fis = new FileInputStream(f);
			((MapDataReader) reader).initialize(id, new LittleEndianByteArrayReader(fis));
			System.out.println(((MapDataReader) reader).doWork());
		}
	}
	
	private static String getMapPath() {
		String paddedId = String.format("%09d", id);
		return new StringBuilder(binPath).append(wzFile).append(File.separator).append("Map").append(File.separator)
				.append("Map").append(paddedId.charAt(0)).append(File.separator).append(paddedId).append(".img.kvj").toString();
	}
}