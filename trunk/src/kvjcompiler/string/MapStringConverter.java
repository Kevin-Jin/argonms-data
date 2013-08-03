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

package kvjcompiler.string;

import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import static kvjcompiler.Converter.isNumber;
import kvjcompiler.DataType;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;
import kvjcompiler.string.structure.*;

/**
 *
 * @author GoldenKevin
 */
public class MapStringConverter extends Converter {
	@Override
	public String getWzName() {
		return "String.wz";
	}

	@Override
	protected void finalizeCompile(String internalPath, String imgName) throws IOException, XMLStreamException {
		try {
			if (r.getEventType() != XMLStreamReader.END_ELEMENT || DataType.getFromString(r.getLocalName()) != DataType.IMGDIR || r.next() != XMLStreamReader.END_DOCUMENT)
				throw new IllegalStateException("ERROR: End of " + internalPath + imgName + " not yet reached.");

			System.out.println(getWzName() + File.separatorChar + internalPath + imgName + " done.");
			System.err.println("Complete.");

			fos.write(new LittleEndianWriter(Size.INT).writeInt(-1).toArray());
		} finally {
			if (fos != null) {
				this.fos.close();
				this.fos = null;
			}
			if (r != null) {
				this.r.close();
				this.r = null;
			}
		}
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		LittleEndianWriter lew;
		int size;
		if (isNumber(nestedPath)) {
			MapStrings e = new MapStrings(Integer.parseInt(nestedPath));
			for (int open = 1, event; open > 0;) {
				event = r.next();
				if (event == XMLStreamReader.START_ELEMENT) {
					open++;
					e.setProperty(r.getAttributeValue(0), r.getAttributeValue(1));
				} else if (event == XMLStreamReader.END_ELEMENT) {
					open--;
				}
			}
			size = e.size();
			if (size != 0) {
				lew = new LittleEndianWriter(size);
				e.writeBytes(lew);
				fos.write(lew.toArray());
			}
		}
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {
	}
}
