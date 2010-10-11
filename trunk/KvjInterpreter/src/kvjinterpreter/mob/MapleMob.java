/*
 *  KvJ Compiler for XML WZ data files
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
package kvjinterpreter.mob;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import kvjinterpreter.mob.structure.*;

public class MapleMob {
	private int mobid;
	private int level;
	private int maxHp;
	private int maxMp;
	private int pad;
	private int exp;
	private boolean undead;
	private String elemAttr;
	private int removeAfter;
	private boolean hideHp;
	private boolean hideName;
	private int hpTagColor;
	private int hpTagBgColor;
	private boolean boss;
	private SelfDestruct sd;
	private List<Integer> loseItems;
	private boolean invincible;
	private List<Integer> summons;
	private boolean firstAttack;
	private Map<Integer, Attack> attacks;
	private Map<Integer, Skill> skills;
	private int buff;
	private Map<String, Integer> delays;
	
	public MapleMob(int id) {
		this.mobid = id;
		this.loseItems = new ArrayList<Integer>();
		this.summons = new ArrayList<Integer>();
		this.attacks = new TreeMap<Integer, Attack>();
		this.skills = new TreeMap<Integer, Skill>();
		this.delays = new TreeMap<String, Integer>();
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setMaxHp(int value) {
		this.maxHp = value;
	}
	
	public void setMaxMp(int value) {
		this.maxMp = value;
	}
	
	public void setPhysicalDamage(int pad) {
		this.pad = pad;
	}
	
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	public void setUndead() {
		this.undead = true;
	}
	
	public void setElementAttribute(String attr) {
		this.elemAttr = attr;
	}
	
	public void setRemoveAfter(int time) {
		this.removeAfter = time;
	}
	
	public void setHideHp() {
		this.hideHp = true;
	}
	
	public void setHideName() {
		this.hideHp = true;
	}
	
	public void setHpTagColor(int color) {
		this.hpTagColor = color;
	}
	
	public void setHpTagBgColor(int color) {
		this.hpTagBgColor = color;
	}
	
	public void setBoss() {
		this.boss = true;
	}
	
	public void setSelfDestruct(SelfDestruct sd) {
		this.sd = sd;
	}
	
	public void addLoseItem(int itemid) {
		this.loseItems.add(Integer.valueOf(itemid));
	}
	
	public void setInvincible() {
		this.invincible = true;
	}
	
	public void addSummon(int mobid) {
		this.summons.add(Integer.valueOf(mobid));
	}
	
	public void setFirstAttack() {
		this.firstAttack = true;
	}
	
	public void addAttack(int attackid, Attack attack) {
		this.attacks.put(Integer.valueOf(attackid), attack);
	}
	
	public void addSkill(int skillid, Skill skill) {
		this.skills.put(Integer.valueOf(skillid), skill);
	}
	
	public void setBuffToGive(int buffid) {
		this.buff = buffid;
	}
	
	public void addDelay(String name, int delay) {
		this.delays.put(name, Integer.valueOf(delay));
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("Mob ").append(mobid).append("\n---------------");
		ret.append("\nLevel:\t\t").append(level);
		ret.append("\nMax Hp:\t\t").append(maxHp);
		ret.append("\nMax Mp:\t\t").append(maxMp);
		ret.append("\nPAD:\t\t").append(pad);
		ret.append("\nExp:\t\t").append(exp);
		ret.append("\nUndead:\t\t").append(undead);
		ret.append("\nElemAttr:\t").append(elemAttr);
		ret.append("\nRemoveAfter:\t").append(removeAfter);
		ret.append("\nHide Hp:\t").append(hideHp);
		ret.append("\nHide name:\t").append(hideName);
		ret.append("\nHp Tag Color:\t").append(hpTagColor);
		ret.append("\nHp Tag Bg Color:").append(hpTagBgColor);
		ret.append("\nBoss:\t\t").append(boss);
		ret.append("\nInvincible:\t").append(invincible);
		ret.append("\nFirst Attack:\t").append(firstAttack);
		ret.append("\nBuff:\t\t").append(buff);
		if (sd != null)
			ret.append("\nSelf Destruct:\t").append(sd);
		if (!loseItems.isEmpty()) {
			ret.append("\nLose Items:\t");
			for (Integer itemid : loseItems)
				ret.append(itemid).append(", ");
			ret = ret.delete(0, ret.length() - 2);
		}
		if (!attacks.isEmpty())
			ret.append("\nAttacks:");
		for (Entry<Integer, Attack> a : attacks.entrySet())
			ret.append("\n\t").append(a.getKey()).append(": ").append(a.getValue());
		if (!skills.isEmpty())
			ret.append("\nSkills:");
		for (Entry<Integer, Skill> s : skills.entrySet())
			ret.append("\n\t").append(s.getKey()).append(": ").append(s.getValue());
		if (!delays.isEmpty())
			ret.append("\nAnimation Times:");
		for (Entry<String, Integer> d : delays.entrySet())
			ret.append("\n\t").append(d.getKey()).append(": ").append(d.getValue()).append("ms");
		
		return ret.toString();
	}
}
