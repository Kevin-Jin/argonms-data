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

import java.io.FileInputStream;
import java.io.IOException;

public class LittleEndianByteArrayReader extends LittleEndianReader {
	private FileInputStream fis;
	
	public LittleEndianByteArrayReader(FileInputStream fis) {
		this.fis = fis;
	}

	protected int read() {
		try {
			return fis.read() & 0xFF;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
