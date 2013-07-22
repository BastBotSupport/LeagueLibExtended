/*
 *  This file is part of LeagueLib.
 *  LeagueLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LeagueLib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LeagueLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.achimala.leaguelib.models;

import com.gvaneyck.rtmp.TypedObject;

public class LeagueRune {
    private String _runeName;
    private Integer _runeID;
    private Integer _runeSlotID;
    private Integer _runeTier;
    
    public LeagueRune(String name, Integer ID, Integer slotID, Integer tier) {
    	_runeName = name;
    	_runeID = ID;
    	_runeSlotID = slotID;
    	_runeTier = tier;
    }
    
    public LeagueRune(TypedObject obj) {
    	TypedObject rune = obj.getTO("rune");
    	_runeName = rune.getString("name");
    	_runeID = obj.getInt("runeId");
    	_runeSlotID = obj.getInt("runeSlotId");
    	_runeTier = rune.getInt("tier");
    }
    
    public String getRuneName() {
        return _runeName;
    }
    
    public Integer getRuneID() {
        return _runeID;
    }
    
    public Integer getRuneSlotID() {
        return _runeSlotID;
    }
    
    public String toString(){
    	return "<" + _runeName + " Tier: " + _runeTier + ">";
    }
}