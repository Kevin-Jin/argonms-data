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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import kvjcompiler.map.MapConverter;

public abstract class Converter {
	public abstract byte[] handleSpecial(String name, XMLStreamReader r) throws XMLStreamException;
	public abstract byte[] getEncodedBytes(String key, String value);
	public abstract WzType getWzType();
	public abstract void finished();
	
	public static Converter getConverter(String source) {
		if (source.equals("Map.wz")) {
			return new MapConverter();
		}
		return null;
	}
	
	public enum WzType {
		MAP
	}
}