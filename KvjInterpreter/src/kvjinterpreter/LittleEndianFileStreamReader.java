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

public class LittleEndianFileStreamReader extends LittleEndianReader {
	private byte[] bytes;
	private int index;
	
	public LittleEndianFileStreamReader(byte[] bytes) {
		this.bytes = bytes;
		this.index = 0;
	}

	protected int read() {
		if (index >= bytes.length)
			return -1;
		return (int) bytes[index++] & 0xFF;
	}
	
	//Copies the remaining portion of the packet stream to a byte array
	public byte[] toArray() {
		byte[] trimmed = new byte[bytes.length - index];
		System.arraycopy(bytes, index, trimmed, 0, bytes.length - index);
		return trimmed;
	}
	
	public String toString() {
		String all = "", now = "";
		
		for (int i = 0; i < bytes.length; i++)
			all += bytes[i] + " ";
		all = all.substring(0, all.length() - 1);
		
		for (int i = index; i < bytes.length; i++)
			now += bytes[i] + " ";
		now = now.substring(0, now.length() - 1);
		
		return "All: " + all + "\nNow: " + now;
	}
}
