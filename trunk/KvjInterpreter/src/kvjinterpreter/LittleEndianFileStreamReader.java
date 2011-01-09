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

//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianFileStreamReader extends LittleEndianReader {
	private InputStream fis;

	public LittleEndianFileStreamReader(InputStream fis) {
		this.fis = fis;
	}

	//the problem with this is that there is no way to close fis...
	/*public LittleEndianFileStreamReader(File f) throws IOException {
		this.fis = new BufferedInputStream(new FileInputStream(f));
	}*/

	protected int read() {
		try {
			return fis.read() & 0xFF;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
