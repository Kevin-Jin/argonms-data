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
	private static final String binPath = "/home/kevin/KvjBin/out/"; //MUST HAVE TRAILING SLASH!
	private static final String wzFile = "Item.wz";
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
		
		/*File d = new File(binPath + wzFile + File.separatorChar + "Consume/0200.img");
		for (String str : d.list()) {
				File f = new File(d.getAbsolutePath() + File.separatorChar + str);
				FileInputStream fis = new FileInputStream(f);
				reader.initialize(Integer.parseInt(str.substring(0, str.lastIndexOf(".kvj"))), new LittleEndianByteArrayReader(fis));
				reader.doWork();
				fis.close();
		}
		for (java.util.Map.Entry<Byte, java.util.List<Integer>> b : ((ItemDataReader) reader).getOps().entrySet()) {
			System.out.print(b.getKey() + ": ");
			for (Integer i : b.getValue())
				System.out.print(i + ", ");
			System.out.println();
		}*/
		
		/*File d = new File(binPath + wzFile);
		for (String str : d.list()) {
			if (!str.equals("Pet")) {
				File cat = new File(d.getAbsolutePath() + File.separatorChar + str);
				for (String str1 : cat.list()) {
					File pref = new File(cat.getAbsolutePath() + File.separatorChar + str1);
					for (String str2 : pref.list()) {
						File f = new File(pref.getAbsolutePath() + File.separatorChar + str2);
						FileInputStream fis = new FileInputStream(f);
						reader.initialize(Integer.parseInt(str2.substring(0, str2.lastIndexOf(".kvj"))), new LittleEndianByteArrayReader(fis));
						reader.doWork();
						fis.close();
					}
				}
			}
		}
		for (java.util.Map.Entry<Byte, java.util.List<Integer>> b : ((ItemDataReader) reader).getOps().entrySet()) {
			System.out.print(b.getKey() + ": ");
			System.out.print(b.getValue().size());
			System.out.println();
		}*/
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
			case ITEM:
				paddedId = String.format("%08d", id);
				String cat = ItemDataReader.getCategory(id);
				if (!cat.equals("Pet"))
					return new StringBuilder(binPath).append(wzFile).append(File.separator).append(cat).append(File.separator).append(paddedId.substring(0, 4)).append(".img").append(File.separator).append(paddedId).append(".kvj").toString();
				else
					return new StringBuilder(binPath).append(wzFile).append(File.separator).append(cat).append(File.separator).append(String.format("%07d", id)).append(".img.kvj").toString();
			default:
				return null;
		}
	}
}