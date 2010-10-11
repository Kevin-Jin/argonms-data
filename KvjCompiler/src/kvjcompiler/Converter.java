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

import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.map.MapConverter;
import kvjcompiler.mob.MobConverter;

public abstract class Converter {
	public abstract boolean handleDir(String parent, XMLStreamReader r, FileOutputStream fos) throws XMLStreamException, IOException;
	public abstract byte[] getEncodedBytes(String key, String value);
	public abstract WzType getWzType();
	public abstract void finished(FileOutputStream fos) throws IOException;
	
	public static Converter getConverter(String source) {
		if (source.equals("Map.wz")) {
			return new MapConverter();
		} else if (source.equals("Mob.wz")) {
			return new MobConverter();
		}
		return null;
	}
	
	public enum WzType {
		MAP, MOB
	}
}