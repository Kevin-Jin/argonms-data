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
package kvjinterpreter.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import kvjinterpreter.map.structure.Area;
import kvjinterpreter.map.structure.Foothold;
import kvjinterpreter.map.structure.Life;
import kvjinterpreter.map.structure.Portal;
import kvjinterpreter.map.structure.Reactor;

public class MapleMap {
	private int mapid;
	private Map<Integer, Portal> portals;
	private Map<String, Area> areas;
	private int returnMapId;
	private float monsterRate;
	private Map<Integer, Foothold> footholds;
	private boolean town;
	private boolean clock;
	private boolean everlast;
	private boolean boat;
	private int forcedReturn;
	private int protectItem;
	private int decHp;
	private int timeLimit;
	private Map<Integer, Life> life;
	private Map<Integer, Reactor> reactors;
	
	public MapleMap(int id) {
		mapid = id;
		portals = new HashMap<Integer, Portal>();
		areas = new HashMap<String, Area>();
		footholds = new HashMap<Integer, Foothold>();
		life = new HashMap<Integer, Life>();
		reactors = new HashMap<Integer, Reactor>();
	}
	
	public void setTown() {
		this.town = true;
	}
	
	public void setReturnMap(int mapid) {
		this.returnMapId = mapid;
	}
	
	public void setForcedReturn(int mapid) {
		this.forcedReturn = mapid;
	}
	
	public void setMobRate(float rate) {
		this.monsterRate = rate;
	}
	
	public void setDecHp(int dec) {
		this.decHp = dec;
	}
	
	public void setTimeLimit(int limit) {
		this.timeLimit = limit;
	}
	
	public void setProtectItem(int item) {
		this.protectItem = item;
	}
	
	public void setEverlast() {
		this.everlast = true;
	}
	
	public void addLife(int id, Life l) {
		life.put(Integer.valueOf(id), l);
	}
	
	public void addArea(String id, Area a) {
		areas.put(id, a);
	}
	
	public void setClock() {
		this.clock = true;
	}
	
	public void setShip() {
		this.boat = true;
	}
	
	public void addReactor(int id, Reactor rt) {
		reactors.put(Integer.valueOf(id), rt);
	}
	
	public void addFoothold(int id, Foothold fh) {
		footholds.put(Integer.valueOf(id), fh);
	}
	
	public void addPortal(int id, Portal p) {
		portals.put(Integer.valueOf(id), p);
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("Map ").append(mapid).append("\n---------------");
		ret.append("\nReturn Map:\t").append(returnMapId);
		ret.append("\nMob Rate:\t").append(monsterRate);
		ret.append("\nIs Town:\t").append(town);
		ret.append("\nHas Clock:\t").append(clock);
		ret.append("\nIs Everlast:\t").append(everlast);
		ret.append("\nHas Boat:\t").append(boat);
		ret.append("\nForced Return:\t").append(forcedReturn);
		ret.append("\nProtect item:\t").append(protectItem);
		ret.append("\nDec HP:\t").append(decHp);
		ret.append("\nTime Limit:\t").append(timeLimit);
		ret.append("\nAreas:");
		for (Entry<String, Area> area : areas.entrySet())
			ret.append("\n\t").append(area.getKey()).append(": ").append(area.getValue());
		for (Entry<Integer, Foothold> foothold : footholds.entrySet())
			ret.append("\n\t").append(foothold.getKey()).append(": ").append(foothold.getValue());
		for (Entry<Integer, Portal> portal : portals.entrySet())
			ret.append("\n\t").append(portal.getKey()).append(": ").append(portal.getValue());
		
		return ret.toString();
	}
}