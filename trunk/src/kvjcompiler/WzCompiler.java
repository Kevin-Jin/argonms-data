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

package kvjcompiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.mob.DropConverter;
import kvjcompiler.mob.MobConverter;

/**
 *
 * @author GoldenKevin
 */
public class WzCompiler {
	private static String log;
	private static String outPath;
	private static String wzPath;
	private static List<String> files;

	private static String customDropsFile, noMesosFile, questDropsFile;

	private static void printUsage() {
		System.out.println("Please check your arguments.");
		System.out.println("The -i, -o, and -l options are mandatory, and at least one -f must be provided.");
		System.out.println("If a space exists in an argument, please surround the arg with double quotes.");
		System.out.println("Usage: [-i <dir>] [-f <folder>]... [-o <dir>] [-l <file>]");
		System.out.println(" -i\tInput directory that contains XML WZ directories (usually named \"wz\")");
		System.out.println(" -f\tWZ folders to compile that are in the input directory (named \"*.wz\")");
		System.out.println(" -o\tThe directory to output compiled WZ folders");
		System.out.println(" -l\tThe name of the log file (will be placed in output directory)");
	}

	private static boolean parseParameters(String[] args) {
		files = new ArrayList<String>();
		int i = 0;
		while (i < args.length) {
			String arg = args[i++];
			if (arg.charAt(0) != '-')
				return false;
			switch (arg.charAt(1)) {
				case 'l':
					log = args[i++];
					if (log.isEmpty())
						return false;
					break;
				case 'o':
					outPath = args[i++];
					if (outPath.isEmpty())
						return false;
					if (outPath.charAt(outPath.length() - 1) != File.separatorChar)
						outPath += File.separatorChar;
					break;
				case 'i':
					wzPath = args[i++];
					if (wzPath.isEmpty())
						return false;
					if (wzPath.charAt(wzPath.length() - 1) != File.separatorChar)
						wzPath += File.separatorChar;
					break;
				case 'f':
					String wzFile = args[i++];
					if (wzFile.isEmpty())
						return false;
					files.add(wzFile);
					break;
				case 'd':
					customDropsFile = args[i++];
					if (customDropsFile.isEmpty())
						return false;
					break;
				case 'm':
					noMesosFile = args[i++];
					if (noMesosFile.isEmpty())
						return false;
					break;
				case 'q':
					questDropsFile = args[i++];
					if (questDropsFile.isEmpty())
						return false;
					break;
				default:
					return false;
			}
		}
		return (log != null && log.length() > 0
				&& outPath != null && outPath.length() > 0
				&& wzPath != null && wzPath.length() > 0
				&& !files.isEmpty());
	}

	public static void main(String[] args) throws XMLStreamException, IOException {
		if (!parseParameters(args)) {
			printUsage();
			return;
		}

		File dir;
		String absDir;
		XMLStreamReader r;
		XMLInputFactory f = XMLInputFactory.newInstance();
		Converter converter;

		dir = new File(outPath);
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new IllegalStateException("ERROR: Could not create output directory " + outPath);

		PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(outPath + log)));
		System.setOut(ps);

		int count = 0;
		long start, end;
		start = System.currentTimeMillis();
		String newLine = System.getProperty("line.separator");
		//80 chars wide, for standard consoles.
		String separator = "--------------------------------------------------------------------------------" + newLine;
		for (String wzFile : files) {
			System.out.println(separator + "Compiling " + wzFile + "...");
			System.err.println(separator + "Compiling " + wzFile);
			converter = Converter.getConverter(wzFile);
			dir = new File(wzPath + wzFile);
			if (!dir.exists())
				throw new IllegalStateException("ERROR: Could not find input directory " + wzPath + wzFile);

			String inputPath = wzPath + wzFile + File.separatorChar;
			if (converter.getWzName().equals("Character.wz")) {
				for (String cat : new File(inputPath).list()) {
					absDir = inputPath + cat + File.separatorChar;
					dir = new File(absDir);
					if (dir.isDirectory()) {
						if (cat.equals("Face") || cat.equals("Hair")) {
							for (String fileName : dir.list()) {
								converter.compile(outPath, cat, fileName.substring(0, fileName.lastIndexOf(".xml")), null);
							}
						} else if (!cat.equals("Afterimage")) {
							for (String fileName : dir.list()) {
								r = f.createXMLStreamReader(new FileInputStream(new File(absDir + fileName)));
								converter.compile(outPath, cat + File.separatorChar, fileName.substring(0, fileName.lastIndexOf(".xml")), r);
								count++;
							}
						}
					}
				}
			} else if (converter.getWzName().equals("Map.wz")) {
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
			} else if (converter.getWzName().equals("Mob.wz") || converter.getWzName().equals("Reactor.wz") || converter.getWzName().equals("Npc.wz")) {
				if (converter.getWzName().equals("Mob.wz")) {
					r = f.createXMLStreamReader(new FileInputStream(new File(wzPath + "String.wz" + File.separatorChar + "MonsterBook.img.xml")));
					DropConverter d = new DropConverter();
					d.populateCustomDrops(customDropsFile, noMesosFile, questDropsFile);
					d.compile(null, null, null, r);
					((MobConverter) converter).setDrops(d.getDrops());
					((MobConverter) converter).setNoMesos(d.getNoMesos());
					((MobConverter) converter).setQuestItemDrops(d.getQuestItemDrops());
				}
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
			} else if (converter.getWzName().equals("String.wz")) {
				//servers only need these files...
				converter.compile(outPath, "", "Cash.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Cash.img.xml"))));
				converter.compile(outPath, "", "Consume.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Consume.img.xml"))));
				converter.compile(outPath, "", "Eqp.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Eqp.img.xml"))));
				converter.compile(outPath, "", "Etc.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Etc.img.xml"))));
				converter.compile(outPath, "", "Ins.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Ins.img.xml"))));
				converter.compile(outPath, "", "Map.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Map.img.xml"))));
				converter.compile(outPath, "", "Mob.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Mob.img.xml"))));
				converter.compile(outPath, "", "Npc.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Npc.img.xml"))));
				converter.compile(outPath, "", "Pet.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Pet.img.xml"))));
				converter.compile(outPath, "", "Skill.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Skill.img.xml"))));
				count += 10;
			} else if (converter.getWzName().equals("Skill.wz")) {
				for (String fileName : new File(inputPath).list()) {
					String id = fileName.substring(0, fileName.lastIndexOf(".img.xml"));
					if (Converter.isNumber(id)) {
						converter.compile(outPath, "", id + ".img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + fileName))));
						count++;
					}
				}
				converter.compile(outPath, "", "MobSkill.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "MobSkill.img.xml"))));
				count++;
			} else if (converter.getWzName().equals("Quest.wz")) {
				//server only need these files (Say.img is meant for the client)...
				converter.compile(outPath, "", "Act.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Act.img.xml"))));
				converter.compile(outPath, "", "Check.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Check.img.xml"))));
				converter.compile(outPath, "", "QuestInfo.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "QuestInfo.img.xml"))));
				count += 3;
			} else if (converter.getWzName().equals("Etc.wz")) {
				//server only need these files...
				converter.compile(outPath, "", "Commodity.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "Commodity.img.xml"))));
				converter.compile(outPath, "", "CashPackage.img", f.createXMLStreamReader(new FileInputStream(new File(inputPath + "CashPackage.img.xml"))));
				count += 2;
			}
			System.out.println("Finished compiling " + wzFile + "!");
			System.err.println("Compiled " + wzFile);
		}
		end = System.currentTimeMillis();
		System.out.println(separator + "Processing completed in " + (end - start) + "ms!");
		ps.close();

		System.err.println(count + " file(s) compiled successfully in " + (end - start) + "ms! Check the logs at " + log + " for more information.");
	}
}