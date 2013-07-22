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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gvaneyck.rtmp.TypedObject;

public class LeagueRunePage {
	private String _runePageName;
	private boolean _currentRunePage;
    private List<LeagueRune> _runes = new ArrayList<LeagueRune>();
    
    public LeagueRunePage(String name, boolean current, List<LeagueRune> runesList) {
    	_runes.addAll(runesList);
    	_runePageName = name;
    	_currentRunePage = current;
    }
    
    public LeagueRunePage(TypedObject obj) {
    	_runePageName = obj.getString("name");
    	_currentRunePage = obj.getBool("current");
        Object[] slotEntries = obj.getArray("slotEntries");
        for(Object slotEntry : slotEntries){
			TypedObject runeSlot = (TypedObject) slotEntry;
        	_runes.add(new LeagueRune(runeSlot));
        }
    }
    
    public List<LeagueRune> getRunes() {
        return _runes;
    }
    
    public String getName() {
        return _runePageName;
    }
    
    public boolean isCurrentRunePage() {
        return _currentRunePage;
    }
    
    public String getRuneNames(){
    	Map<String, Integer> runeMap = new HashMap<String,Integer>();
    	String runes = "";
    	for(LeagueRune rune: _runes){
    		if(runeMap.containsKey(rune.getRuneName())){
    			runeMap.put(rune.getRuneName(), runeMap.get(rune.getRuneName())+1);
    		}else{
    			runeMap.put(rune.getRuneName(), 1);
    		}
    	}
    	for(Entry<String,Integer> en : runeMap.entrySet()){
    		runes += en.getKey() + " x" + en.getValue() + ", ";
    	}
    	
    	return runes;
    	
    }
    
    public String toString(){
    	Map<String, Integer> runeMap = new HashMap<String,Integer>();
    	String runes = _runePageName + ": ";
    	for(LeagueRune rune: _runes){
    		if(runeMap.containsKey(rune.getRuneName())){
    			runeMap.put(rune.getRuneName(), runeMap.get(rune.getRuneName())+1);
    		}else{
    			runeMap.put(rune.getRuneName(), 1);
    		}
    	}
    	for(Entry<String,Integer> en : runeMap.entrySet()){
    		runes += en.getKey() + " x" + en.getValue() + ", ";
    	}
    	
    	return runes;
    }
    
}