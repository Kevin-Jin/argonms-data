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

/**
 *
 * @author GoldenKevin
 */
public class Size {
	public static final int HEADER = 1; //currently 1-byte (255 possible values)

	public static final int
		LONG = 8,
		INT = 4,
		SHORT = 2,
		BYTE = 1,
		DOUBLE = 8,
		FLOAT = 4,
		CHAR = 1,
		BOOL = 1
	;

	public static final int NULL_TERMINATED_STRING(String str) {
		return str.length() + 1;
	}

	private Size() {

	}
}
