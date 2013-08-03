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

package kvjcompiler.cashshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import kvjcompiler.Converter;
import kvjcompiler.LittleEndianWriter;
import kvjcompiler.Size;

/**
 *
 * @author GoldenKevin
 */
public class CashPackageConverter extends Converter {
	@Override
	public String getWzName() {
		return "Etc.wz";
	}

	@Override
	protected void handleDir(String nestedPath) throws XMLStreamException, IOException {
		List<Integer> serialNumbers = new ArrayList<Integer>();
		for (int open1 = 1, event; open1 > 0;) {
			event = r.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				open1++;
				if (!r.getAttributeValue(0).equals("SN"))
					continue;

				for (int open = 1; open > 0;) {
					event = r.next();
					if (event == XMLStreamReader.START_ELEMENT) {
						open++;
						serialNumbers.add(Integer.valueOf(Integer.parseInt(r.getAttributeValue(1))));
					} else if (event == XMLStreamReader.END_ELEMENT) {
						open--;
					}
				}
			}
			if (event == XMLStreamReader.END_ELEMENT) {
				open1--;
			}
		}
		LittleEndianWriter lew = new LittleEndianWriter(Size.INT + Size.BYTE + serialNumbers.size() * Size.INT);
		lew.writeInt(Integer.parseInt(nestedPath));
		lew.writeByte((byte) serialNumbers.size());
		for (Integer sn : serialNumbers)
			lew.writeInt(sn.intValue());
		fos.write(lew.toArray());
	}

	@Override
	protected void handleProperty(String nestedPath, String value) throws IOException {

	}
}
