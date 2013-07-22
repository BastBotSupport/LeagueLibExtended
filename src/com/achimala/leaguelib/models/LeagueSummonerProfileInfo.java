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
import java.util.List;

import com.gvaneyck.rtmp.TypedObject;

public class LeagueSummonerProfileInfo {
    private LeagueRankedTier _seasonOneTier, _seasonTwoTier;
    private List<LeagueRunePage> _runePages = new ArrayList<LeagueRunePage>();
    // TODO: Runes and masteries...
    
    public LeagueSummonerProfileInfo() {
    }
    
    public LeagueSummonerProfileInfo(TypedObject obj) {
        _seasonOneTier = LeagueRankedTier.valueOf(obj.getString("seasonOneTier"));
        _seasonTwoTier = LeagueRankedTier.valueOf(obj.getString("seasonTwoTier"));
    }
    
    public void setSeasonOneTier(LeagueRankedTier tier) {
        _seasonOneTier = tier;
    }
    
    public void setSeasonTwoTier(LeagueRankedTier tier) {
        _seasonTwoTier = tier;
    }
    
    public void setRunePages(TypedObject obj) {
    	Object[] runePages = obj.getArray("bookPages");
    	 for(Object bookPage : runePages){
 			TypedObject runePage = (TypedObject) bookPage;
        	_runePages.add(new LeagueRunePage(runePage));
        }
    }
    
    public LeagueRunePage getCurrentRunePage() {
    	for(LeagueRunePage page : _runePages){
    		if(page.isCurrentRunePage() && page.getRunes().size() > 0){
    			return page;
    		}
    	}
    	return null;
    }
    
    public List<LeagueRunePage> getRunePages() {
        return _runePages;
    }
    
    public LeagueRankedTier getSeasonOneTier() {
        return _seasonOneTier;
    }
    
    public LeagueRankedTier getSeasonTwoTier() {
        return _seasonTwoTier;
    }
}