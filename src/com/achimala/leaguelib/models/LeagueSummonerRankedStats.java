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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueException;
import com.gvaneyck.rtmp.TypedObject;

public class LeagueSummonerRankedStats {
    private HashMap<Integer, Map<LeagueRankedStatType, Object>> _stats;
    
    public LeagueSummonerRankedStats() {
    }
    
    public LeagueSummonerRankedStats(TypedObject obj) {
        _stats = new HashMap<Integer, Map<LeagueRankedStatType, Object>>();
        for(Object o : obj.getArray("lifetimeStatistics")) {
            TypedObject to = (TypedObject)o;
            int champId = to.getInt("championId");
            LeagueRankedStatType type = LeagueRankedStatType.valueOf(to.getString("statType"));
            if(!_stats.containsKey(champId))
                _stats.put(champId, new HashMap<LeagueRankedStatType, Object>());
            _stats.get(champId).put(type, to.getInt("value"));
        }
    }
    
    public Map<LeagueRankedStatType, Object> getAllStatsForChampion(LeagueChampion champion) {
        return _stats.get(champion.getId());
    }
    
    public Map<LeagueRankedStatType, Object> getAllPlayerStats() {
        return _stats.get(0);
    }
    
    public List<LeagueChampion> getAllPlayedChampions() {
        List<LeagueChampion> champs = new LinkedList<LeagueChampion>();
        for(Integer champId : _stats.keySet()) {
            if(champId.intValue() == 0)
                continue;
            champs.add(LeagueChampion.getChampionWithId(champId.intValue()));
        }
        return champs;
    }
    
    public Object getStatForChampion(LeagueChampion champion, LeagueRankedStatType statType) {
        if(_stats == null)
            return 0;
        Map<LeagueRankedStatType, Object> stats = _stats.get(champion.getId());
        if(stats == null)
            return 0;
        return _stats.get(champion.getId()).get(statType);
    }
    
    public Map<String,Object> getKdrForChampion(LeagueChampion champion) {
    	Map<String,Object> kdr = new HashMap<String,Object>();
    	kdr.put("kills", 0);
    	kdr.put("deaths", 0);
    	kdr.put("assists", 0);
    	kdr.put("kdr", 0);
    	
        if(_stats == null){
            System.out.println("_stats is null");
            return kdr;
        }
        
        Map<LeagueRankedStatType, Object> stats = _stats.get(champion.getId());
        
        if(stats == null){
            System.out.println("stats is null for champion " + champion);
            return kdr;
        }
        
        if(Integer.parseInt(stats.get(LeagueRankedStatType.TOTAL_SESSIONS_PLAYED).toString()) == 0){
            System.out.println("no games played for champion " + champion);
            return kdr;
        }
    
        double kills = Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_CHAMPION_KILLS).toString())/Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_SESSIONS_PLAYED).toString());
        double deaths = Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_DEATHS_PER_SESSION).toString())/Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_SESSIONS_PLAYED).toString());
        double assists = Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_ASSISTS).toString())/Double.parseDouble(stats.get(LeagueRankedStatType.TOTAL_SESSIONS_PLAYED).toString());
        double kda = (kills+assists)/deaths;

        int precision = 10; //keep 4 digits
        kills= Math.floor(kills * precision +.5)/precision;
        deaths= Math.floor(deaths * precision +.5)/precision;
        assists= Math.floor(assists * precision +.5)/precision;
        kda= Math.floor(kda * precision +.5)/precision;
        

    	kdr.put("kills", kills);
    	kdr.put("deaths", deaths);
    	kdr.put("assists", assists);
    	kdr.put("kdr", kda);
    	
        return kdr ;
    }
    
    public Object getPlayerStat(LeagueRankedStatType statType) {
        return _stats.get(0).get(statType);
    }
}