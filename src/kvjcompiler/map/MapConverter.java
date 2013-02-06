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

package kvjcompiler.map;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.map.structure.*;

/**
 *
 * @author GoldenKevin
 */
public class MapConverter extends Converter {
	private static final byte
		TOWN = 1,
		RETURN_MAP = 2,
		FORCED_RETURN = 3,
		MOB_RATE = 4,
		FIELD_LIMIT = 5,
		DEC_HP = 6,
		TIME_LIMIT = 7,
		PROTECT_ITEM = 8,
		EVERLAST = 9,
		LIFE = 10,
		AREA = 11,
		CLOCK = 12,
		BOAT = 13,
		REACTOR = 14,
		FOOTHOLD = 15,
		PORTAL = 16
	;

	@Override
	public String getWzName() {
		return "Map.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("portal")) {
			Portal p;
			LittleEndianWriter lew;
			for (int open1 = 1, event; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					p = new Portal(Integer.parseInt(r.getAttributeValue(0)));
					for (int open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							p.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					lew = new LittleEndianWriter(Size.HEADER + p.size(), PORTAL);
					p.writeBytes(lew);
					fos.write(lew.toArray());
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return;
		} else if (dirs[0].equals("life")) {
			Life l;
			LittleEndianWriter lew;
			for (int open1 = 1, event; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					l = new Life(Integer.parseInt(r.getAttributeValue(0)));
					for (int open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							l.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					lew = new LittleEndianWriter(Size.HEADER + l.size(), LIFE);
					l.writeBytes(lew);
					fos.write(lew.toArray());
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return;
		} else if (dirs[0].equals("area")) {
			Area a;
			LittleEndianWriter lew;
			for (int open1 = 1, event; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					a = new Area(r.getAttributeValue(0));
					for (int open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							a.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					lew = new LittleEndianWriter(Size.HEADER + a.size(), AREA);
					a.writeBytes(lew);
					fos.write(lew.toArray());
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return;
		} else if (dirs[0].equals("reactor")) {
			LittleEndianWriter lew;
			Reactor rt;
			for (int open1 = 1, event; open1 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open1++;
					rt = new Reactor(Integer.parseInt(r.getAttributeValue(0)));
					for (int open = 1; open > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open++;
							rt.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
						} else if (event == XMLStreamReader.END_ELEMENT) {
							open--;
						}
					}
					lew = new LittleEndianWriter(Size.HEADER + rt.size(), REACTOR);
					rt.writeBytes(lew);
					fos.write(lew.toArray());
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open1--;
				}
			}
			return;
		} else if (dirs[0].equals("foothold")) {
			Foothold f;
			LittleEndianWriter lew;

			for (int open3 = 1, open2, open1, open, event; open3 > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open3++;
					for (open2 = 1; open2 > 0;) {
						event = r.next();
						if (event == XMLStreamReader.START_ELEMENT) {
							open2++;
							if (r.getLocalName().equals("imgdir")) {
								for (open1 = 1; open1 > 0;) {
									event = r.next();
									if (event == XMLStreamReader.START_ELEMENT) {
										open1++;
										if (r.getLocalName().equals("imgdir")) {
											f = new Foothold(Short.parseShort(r.getAttributeValue(0)));
											for (open = 1; open > 0;) {
												event = r.next();
												if (event == XMLStreamReader.START_ELEMENT) {
													open++;
													f.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
												}
												if (event == XMLStreamReader.END_ELEMENT) {
													open--;
												}
											}
											lew = new LittleEndianWriter(Size.HEADER + f.size(), FOOTHOLD);
											f.writeBytes(lew);
											fos.write(lew.toArray());
										}
									}
									if (event == XMLStreamReader.END_ELEMENT) {
										open1--;
									}
								}
							}
						}
						if (event == XMLStreamReader.END_ELEMENT) {
							open2--;
						}
					}
				}
				if (event == XMLStreamReader.END_ELEMENT) {
					open3--;
				}
			}
			return;
		} else if (dirs[0].equals("shipObj")) {
			LittleEndianWriter lew;
			Ship ship = new Ship();
			for (int open = 1, event; open > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open++;
					ship.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
				} else if (event == XMLStreamReader.END_ELEMENT) {
					open--;
				}
			}
			lew = new LittleEndianWriter(Size.HEADER + ship.size(), BOAT);
			ship.writeBytes(lew);
			fos.write(lew.toArray());
			return;
		}
		traverseBlock(nestedPath);
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
		//System.out.println("DEBUG: Handling " + nestedPath);
		String[] dirs = nestedPath.split("/");
		if (dirs[0].equals("info")) {
			if (dirs[1].equals("town")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, TOWN).toArray());
			} else if (dirs[1].equals("returnMap")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, RETURN_MAP).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("forcedReturn")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, FORCED_RETURN).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("mobRate")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.FLOAT, MOB_RATE).writeFloat(Float.parseFloat(value)).toArray());
			} else if (dirs[1].equals("fieldLimit")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, FIELD_LIMIT).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("decHP")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, DEC_HP).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("timeLimit")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, TIME_LIMIT).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("protectItem")) {
				fos.write(new LittleEndianWriter(Size.HEADER + Size.INT, PROTECT_ITEM).writeInt(Integer.parseInt(value)).toArray());
			} else if (dirs[1].equals("everlast")) {
				if (Integer.parseInt(value) == 1)
					fos.write(new LittleEndianWriter(Size.HEADER, EVERLAST).toArray());
			}
		} else if (dirs[0].equals("clock")) {
			fos.write(new LittleEndianWriter(Size.HEADER, CLOCK).toArray());
		}
	}
}
