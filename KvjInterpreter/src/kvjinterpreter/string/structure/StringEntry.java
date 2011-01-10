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
package kvjinterpreter.string.structure;

public class StringEntry {
	private String name, mapName, streetName, msg;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (name != null) sb.append("name=").append(name).append(", ");
		if (mapName != null) sb.append("mapName=").append(mapName).append(", ");
		if (streetName != null) sb.append("streetName=").append(streetName).append(", ");
		if (msg != null) sb.append("msg=").append(msg).append(", ");
		return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 2);
	}
}
