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

import java.nio.charset.Charset;

/**
 *
 * @author GoldenKevin
 */
public class LittleEndianWriter {
	private static final int DEF_SIZE = 32;

	private byte[] bytes;
	private int index;

	static Charset asciiEncoder = Charset.forName("US-ASCII");

	public LittleEndianWriter(int size, byte initial) {
		this(size);
		bytes[index++] = initial;
	}

	public LittleEndianWriter(byte initial) {
		this(DEF_SIZE);
		bytes[index++] = initial;
	}

	public LittleEndianWriter(int size) {
		index = 0;
		bytes = new byte[size];
	}

	private void grow() {
		System.out.println("WARNING: Growing a LittleEndianWriter...");
		byte[] copy = new byte[bytes.length + bytes.length / 2];
		System.arraycopy(bytes, 0, copy, 0, bytes.length);
		bytes = copy;
	}

	public LittleEndianWriter writeLong(long l) {
		writeByte((byte) (l & 0xFF));
		writeByte((byte) ((l >>> 8) & 0xFF));
		writeByte((byte) ((l >>> 16) & 0xFF));
		writeByte((byte) ((l >>> 24) & 0xFF));
		writeByte((byte) ((l >>> 32) & 0xFF));
		writeByte((byte) ((l >>> 40) & 0xFF));
		writeByte((byte) ((l >>> 48) & 0xFF));
		writeByte((byte) ((l >>> 56) & 0xFF));
		return this;
	}

	public LittleEndianWriter writeInt(int i) {
		writeByte((byte) (i & 0xFF));
		writeByte((byte) ((i >>> 8) & 0xFF));
		writeByte((byte) ((i >>> 16) & 0xFF));
		writeByte((byte) ((i >>> 24) & 0xFF));
		return this;
	}

	public LittleEndianWriter writeShort(short s) {
		writeByte((byte) (s & 0xFF));
		writeByte((byte) ((s >>> 8) & 0xFF));
		return this;
	}

	public LittleEndianWriter writeByte(byte b) {
		if (index == bytes.length)
			grow();

		bytes[index++] = b;
		return this;
	}

	public LittleEndianWriter writeFloat(float f) {
		writeInt(Float.floatToRawIntBits(f));
		return this;
	}

	public LittleEndianWriter writeDouble(double d) {
		writeLong(Double.doubleToRawLongBits(d));
		return this;
	}

	public LittleEndianWriter writeNullTerminatedString(String str) {
		if (str != null)
			writeBytes(str.getBytes(asciiEncoder));
		writeByte((byte) 0);
		return this;
	}

	public LittleEndianWriter writeChar(char c) {
		writeByte((byte) c);
		return this;
	}

	public LittleEndianWriter writeBool(boolean b) {
		writeByte((byte) (b ? 1 : 0));
		return this;
	}

	public LittleEndianWriter writeBytes(byte[] b) {
		for (int i = 0; i < b.length; i++)
			writeByte(b[i]);
		return this;
	}

	public byte[] toArray() {
		if (index == bytes.length)
			return bytes;

		System.out.println("WARNING: Shrinking a LittleEndianWriter...");
		byte[] trimmed = new byte[index];
		System.arraycopy(bytes, 0, trimmed, 0, index);
		return trimmed;
	}

	public String toString() {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < index; i++)
			ret.append(bytes[i]).append(' ');

		return ret.substring(0, ret.length() - 1);
	}
}