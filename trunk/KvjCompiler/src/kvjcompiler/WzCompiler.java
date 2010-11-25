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
package kvjcompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class WzCompiler {
	private static final String log = "wzlog.txt";
	private static final String outPath = "/home/kevin/KvjBin/out/"; //MUST HAVE TRAILING SLASH!
	private static final String wzPath = "/home/kevin/KvjBin/wz/"; //MUST HAVE TRAILING SLASH!
	private static final String wzFile = "Item.wz";
	
	public static void main(String[] args) throws XMLStreamException, IOException {
		File dir;
		String absDir;
		Converter converter = Converter.getConverter(wzFile);
		XMLStreamReader r;
		XMLInputFactory f = XMLInputFactory.newInstance();
		
		dir = new File(wzPath + wzFile);
		if (!dir.exists())
			throw new IllegalStateException("ERROR: Could not find input directory " + wzPath + wzFile);
		
		dir = new File(outPath);
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new IllegalStateException("ERROR: Could not create output directory " + outPath);
		
		FileOutputStream fos = new FileOutputStream(outPath + log);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		
		String inputPath = wzPath + wzFile + File.separatorChar;
		int count = 0;
		long start, end;
		start = System.currentTimeMillis();
		if (converter.getWzName().equals("Map.wz")) {
			for (String mapDir : new File(inputPath + "Map").list()) {
				dir = new File(inputPath, "Map" + File.separatorChar + mapDir);
				if (dir.isDirectory()) {
					absDir = "Map" + File.separatorChar + mapDir + File.separatorChar;
					for (String fileName : dir.list()) {
						r = f.createXMLStreamReader(new FileInputStream(new File(inputPath + absDir + fileName)));
						converter.compile(outPath, absDir, fileName.substring(0, fileName.lastIndexOf(".xml")), r);
						count++;
					}
				} else {
					System.out.println("Skipping " + dir.getPath());
				}
			}
		} else if (converter.getWzName().equals("Mob.wz") || converter.getWzName().equals("Reactor.wz")) {
			dir = new File(inputPath);
			for (String fileName : dir.list()) {
				r = f.createXMLStreamReader(new FileInputStream(new File(inputPath + fileName)));
				converter.compile(outPath, "", fileName.substring(0, fileName.lastIndexOf(".xml")), r);
				count++;
			}
		} else if (converter.getWzName().equals("Item.wz")) {
			for (String catDir : new File(inputPath).list()) {
				for (String fileName : new File(inputPath + catDir).list()) {
					r = f.createXMLStreamReader(new FileInputStream(new File(inputPath + catDir + File.separatorChar + fileName)));
					converter.compile(outPath, catDir + File.separatorChar, fileName.substring(0, fileName.lastIndexOf(".xml")), r);
					count++;
				}
			}
		}
		end = System.currentTimeMillis();
		
		System.err.println(count + " file(s) compiled successfully in " + (end - start) + "ms!. Check the logs at " + log + " for more information.");
	}
}