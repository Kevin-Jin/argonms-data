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

public abstract class LittleEndianReader {
	protected abstract int read();
	
	public long readLong() {
		long b1, b2, b3, b4, b5, b6, b7, b8;
		
		b1 = read();
		b2 = read();
		b3 = read();
		b4 = read();
		b5 = read();
		b6 = read();
		b7 = read();
		b8 = read();
		
		return (b8 << 56) + (b7 << 48) + (b6 << 40) + (b5 << 32) + (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
	}
	
	public int readInt() {
		int b1, b2, b3, b4;
		
		b1 = read();
		b2 = read();
		b3 = read();
		b4 = read();
		
		return (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
	}
	
	public short readShort() {
		int b1, b2;
		
		b1 = read();
		b2 = read();
		
		return (short) ((b2 << 8) + b1);
	}
	
	public byte readByte() {
		return (byte) read();
	}
	
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}
	
	public String readNullTerminatedString() {
		StringBuilder builder = new StringBuilder();
		
		for (byte current = readByte(); current != 0; current = readByte())
			builder.append((char) current);
		
		return builder.toString();
	}
	
	public char readChar() {
		return (char) readByte();
	}
	
	public boolean readBool() {
		return (readByte() > 0);
	}
	
	public byte[] readBytes(int size) {
		byte[] bArray = new byte[size];
		for (int i = 0; i < size; i++)
			bArray[i] = readByte();
		return bArray;
	}
}
