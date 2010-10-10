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

import kvjinterpreter.DataReader;
import kvjinterpreter.LittleEndianReader;
import kvjinterpreter.map.structure.*;

public class MapDataReader extends DataReader {
	private static final byte
		TOWN = 1,
		RETURN_MAP = 2,
		FORCED_RETURN = 3,
		MOB_RATE = 4,
		DEC_HP = 5,
		TIME_LIMIT = 6,
		PROTECT_ITEM = 7,
		EVERLAST = 8,
		LIFE = 9,
		AREA = 10,
		CLOCK = 11,
		BOAT = 12,
		REACTOR = 13,
		FOOTHOLD = 14,
		PORTAL = 15
	;
	
	private MapleMap map;
	private LittleEndianReader reader;
	
	public MapDataReader() {
	}
	
	public WzType getWzType() {
		return WzType.MAP;
	}
	
	public void initialize(int mapid, LittleEndianReader reader) {
		this.map = new MapleMap(mapid);
		this.reader = reader;
	}
	
	public MapleMap doWork() {
		for (byte now = reader.readByte(); now != -1; now = reader.readByte()) {
			switch (now) {
				case TOWN:
					map.setTown();
					break;
				case RETURN_MAP:
					map.setReturnMap(reader.readInt());
					break;
				case FORCED_RETURN:
					map.setForcedReturn(reader.readInt());
					break;
				case MOB_RATE:
					map.setMobRate(reader.readFloat());
					break;
				case DEC_HP:
					map.setDecHp(reader.readInt());
					break;
				case TIME_LIMIT:
					map.setTimeLimit(reader.readInt());
					break;
				case PROTECT_ITEM:
					map.setProtectItem(reader.readInt());
					break;
				case EVERLAST:
					map.setEverlast();
					break;
				case LIFE:
					processLife();
					break;
				case AREA:
					processArea();
					break;
				case CLOCK:
					map.setClock();
					break;
				case BOAT:
					map.setShip();
					break;
				case REACTOR:
					processReactor();
					break;
				case FOOTHOLD:
					processFoothold();
					break;
				case PORTAL:
					processPortal();
					break;
			}
		}
		return map;
	}
	
	private void processLife() {
		int id = reader.readInt();
		Life l = new Life();
		l.setType(reader.readChar());
		l.setDataId(reader.readInt());
		l.setX(reader.readInt());
		l.setY(reader.readInt());
		l.setMobTime(reader.readInt());
		l.setF(reader.readBool());
		l.setHide(reader.readBool());
		l.setFoothold(reader.readInt());
		l.setCy(reader.readInt());
		l.setRx0(reader.readInt());
		l.setRx1(reader.readInt());
		map.addLife(id, l);
	}
	
	private void processArea() {
		String id = reader.readNullTerminatedString();
		Area a = new Area();
		a.setX1(reader.readInt());
		a.setX2(reader.readInt());
		a.setY1(reader.readInt());
		a.setY2(reader.readInt());
		map.addArea(id, a);
	}
	
	private void processReactor() {
		int id = reader.readInt();
		Reactor rt = new Reactor();
		rt.setDataId(reader.readInt());
		rt.setX(reader.readInt());
		rt.setY(reader.readInt());
		rt.setReactorTime(reader.readInt());
		rt.setName(reader.readNullTerminatedString());
		map.addReactor(id, rt);
	}
	
	private void processFoothold() {
		int id = reader.readInt();
		Foothold fh = new Foothold();
		fh.setX1(reader.readInt());
		fh.setY1(reader.readInt());
		fh.setX2(reader.readInt());
		fh.setY2(reader.readInt());
		fh.setPrev(reader.readInt());
		fh.setNext(reader.readInt());
		map.addFoothold(id, fh);
	}
	
	private void processPortal() {
		int id = reader.readInt();
		Portal p = new Portal();
		p.setPortalName(reader.readNullTerminatedString());
		p.setPortalType(reader.readInt());
		p.setX(reader.readInt());
		p.setY(reader.readInt());
		p.setTargetMapId(reader.readInt());
		p.setTarget(reader.readNullTerminatedString());
		p.setScript(reader.readNullTerminatedString());
		map.addPortal(id, p);
	}
}
