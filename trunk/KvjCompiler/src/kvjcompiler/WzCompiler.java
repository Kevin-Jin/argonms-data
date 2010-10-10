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
import kvjcompiler.Converter.WzType;

public class WzCompiler {
	private static final String log = "wzlog.txt";
	private static final String outPath = "/home/kevin/KvjBin/out/"; //MUST HAVE TRAILING SLASH!
	private static final String wzPath = "/home/kevin/KvjBin/wz/"; //MUST HAVE TRAILING SLASH!
	private static final String wzFile = "Map.wz";
	
	public static void main(String[] args) throws Exception {
		//System.out.println("Using " + new File(".").getCanonicalPath() + " as the working directory...");
		if (!new File(outPath).exists()) {
			if (!(new File(outPath).mkdirs())) {
				throw new IllegalStateException("ERROR: Could not create output directory " + outPath);
			}
		}
		FileOutputStream fos = new FileOutputStream(outPath + log);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		
		Converter converter = Converter.getConverter(wzFile);
		
		if (converter == null) {
			throw new IllegalStateException("ERROR: Wz type '" + wzFile + "' is not recognized. Please try again.");
		}

		XMLInputFactory f = XMLInputFactory.newInstance(); 
		WzCompiler comp = new WzCompiler(f, converter);
		int i = 0;
		String path = wzPath + wzFile, absDir;
		File dir;
		long start, end;
		start = System.currentTimeMillis();
		try {
			if (converter.getWzType() == WzType.MAP) {
				for (String mapDir : new File(path + "/Map").list()) {
					dir = new File(path + '/', "Map/" + mapDir);
					if (dir.isDirectory()) {
						absDir = "Map" + File.separatorChar + mapDir + File.separatorChar;
						for (String fileName : dir.list()) {
							comp.compile(path, absDir + fileName);
							i++;
						}
					} else {
						System.out.println("Skipping " + dir.getPath());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		end = System.currentTimeMillis();

		System.err.println(i + " file(s) compiled successfully in " + (end - start) + "ms!. Check the logs at " + log + " for more information.");
	}
	
	private XMLInputFactory f;
	private Converter converter;
	
	private WzCompiler(XMLInputFactory f, Converter converter) {
		this.f = f;
		this.converter = converter;
	}
	
	public void compile(String path, String location) throws XMLStreamException, IOException {
		System.err.print("Building " + location + "...\t");
		XMLStreamReader r = f.createXMLStreamReader(new FileInputStream(new File(path + File.separatorChar + location)));
		
		if (r.getEventType() != XMLStreamReader.START_DOCUMENT) {
			throw new IllegalStateException("Malformed Xml document at " + location);
		}
		String imgName = location.substring(location.lastIndexOf(File.separatorChar) + 1, location.lastIndexOf(".xml"));
		
		/*int event = */r.next();
		
		if (!r.getAttributeValue(0).equals(imgName)) {
			throw new IllegalStateException("ERROR: <imgdir name=\"" + imgName + "\"> expected as first tag in " + imgName + ".");
		}
		
		String binFile = outPath + wzFile + File.separatorChar + location.substring(0, location.lastIndexOf(".xml")) + ".kvj";
		File binDir = new File(binFile.substring(0, binFile.lastIndexOf(File.separatorChar)));
		if (!binDir.exists()) {
			if (!binDir.mkdirs()) {
				throw new IllegalStateException("ERROR: Could not create compiled directory " + binDir.getAbsolutePath());
			}
		}
		XmlReader traverser = new XmlReader(converter, r, new FileOutputStream(binFile));
		
		while (r.hasNext())
			if (r.next() == XMLStreamReader.START_ELEMENT)
				traverser.traverseBlock();
		
		converter.finished();
		System.out.println(binFile + " done.");
		System.err.println("Complete.");
	}
}
