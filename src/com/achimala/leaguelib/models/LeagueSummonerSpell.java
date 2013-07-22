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

import java.util.HashMap;

import com.achimala.util.BidirectionalMap;

public class LeagueSummonerSpell {
    private static BidirectionalMap<Integer, String> _modelMap;
    private static HashMap<Integer, LeagueSummonerSpell> _spellMap;
    
    static {
        _modelMap = new BidirectionalMap<Integer, String>();
        _modelMap.put(  0,  null); // represents a catch-all champion for stats
        _modelMap.put(  1, "Cleanse");
        _modelMap.put(  2, "Clairvoyance");
        _modelMap.put(  3, "Exhaust");
        _modelMap.put(  4, "Flash");
        _modelMap.put(  6, "Ghost");
        _modelMap.put(  7, "Heal");
        _modelMap.put(  9, "Cleanse");
        _modelMap.put( 10, "Revive");
        _modelMap.put( 11, "Smite");
        _modelMap.put( 12, "Teleport");
        _modelMap.put( 13, "Clarity");
        _modelMap.put( 14, "Ignite");
        _modelMap.put( 16, "Surge");
        _modelMap.put( 17, "Garrison");
        _modelMap.put( 20, "Promote");
        _modelMap.put( 21, "Barrier");
        
        _spellMap = new HashMap<Integer, LeagueSummonerSpell>();
    }
    
    public static String getNameForSpell(int id) {
        return _modelMap.get(id);
    }
    
    public static int getIdForSpell(String name) {
        return _modelMap.getKey(name);
    }
    
    public static LeagueSummonerSpell getSpellWithName(String name) {
        return getSpellWithId(_modelMap.getKey(name));
    }
    
    public static LeagueSummonerSpell getSpellWithId(int id) {
        if(!_spellMap.containsKey(id))
        	_spellMap.put(id, new LeagueSummonerSpell(id));
        return _spellMap.get(id);
    }
    
    private String _name;
    private int _id;
    
    private LeagueSummonerSpell(String name) {
        _name = name;
        _id = getIdForSpell(name);
    }
    
    private LeagueSummonerSpell(int id) {
        _name = getNameForSpell(id);
        _id = id;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public void setId(int id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public int getId() {
        return _id;
    }
    
    public String toString() {
        return "<Spell " + _name + "(#" + _id + ")>";
    }
    
    // public String getFilename() {
    //     return getName().toLowerCase().replaceAll("[^a-z0-9]", "");
    // }
}
