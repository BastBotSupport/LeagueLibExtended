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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gvaneyck.rtmp.TypedObject;

public class MatchHistoryEntry implements PlayerList {
	int _gameId;
	private String _gameType;
	// TODO: Summoner spells
	private boolean _leaver;
	private Date _createDate;
	private LeagueMatchmakingQueue _queue;
	private List<LeagueSummoner> _playerTeam, _enemyTeam;
	private Map<Integer, LeagueChampion> _playerChampionSelections;
	private Map<MatchHistoryStatType, Integer> _stats;
	private List<String> _itemList;
	private List<LeagueSummonerSpell> _spellList;
	private int _daysAgoPlayed;

	public MatchHistoryEntry() {
	}

	public MatchHistoryEntry(TypedObject obj, LeagueSummoner primarySummoner) {
		_gameId = obj.getInt("gameId");
		_gameType = obj.getString("gameType");
		_leaver = obj.getBool("leaver");
		_createDate = (Date) obj.get("createDate");
		_queue = LeagueMatchmakingQueue.valueOf(obj.getString("queueType"));
		List<Integer> spellIds = new ArrayList<Integer>();
		spellIds.add(obj.getInt("spell1"));
		spellIds.add(obj.getInt("spell2"));
		_spellList = getSummonerSpellsFromIds(spellIds);

		_playerTeam = new ArrayList<LeagueSummoner>();
		_enemyTeam = new ArrayList<LeagueSummoner>();
		_playerChampionSelections = new HashMap<Integer, LeagueChampion>();
		_stats = new HashMap<MatchHistoryStatType, Integer>();

		// (for some unknown reason, sometimes the "summonerId" key is 0 in the data returned from Riot)
		// This is the only reason we have to pass the primary summoner into this constructor
		// ...which is pretty dumb
		_playerChampionSelections.put(primarySummoner.getId(), LeagueChampion.getChampionWithId(obj.getInt("championId")));

		int playerTeamId = obj.getInt("teamId");

		// Riot doesn't include this person in the "fellow players" list, which I suppose makes sense
		_playerTeam.add(primarySummoner);
		for (Object playerObj : obj.getArray("fellowPlayers")) {
			TypedObject player = (TypedObject) playerObj;
			LeagueSummoner summoner = new LeagueSummoner();
			summoner.setId(player.getInt("summonerId"));
			_playerChampionSelections.put(summoner.getId(), LeagueChampion.getChampionWithId(player.getInt("championId")));
			if (player.getInt("teamId") == playerTeamId)
				_playerTeam.add(summoner);
			else
				_enemyTeam.add(summoner);
		}

		for (Object statObj : obj.getArray("statistics")) {
			TypedObject stat = (TypedObject) statObj;
			MatchHistoryStatType type = MatchHistoryStatType.valueOf(stat.getString("statType"));
			_stats.put(type, stat.getInt("value"));
		}


		try {
			_daysAgoPlayed = getHowManyDaysAgoMatchWasPlayed(obj.getDate("createDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		_itemList = getItemNamesFromStatMap();
	}

	public void setItemList(List<String> li) {
		_itemList = li;
	}

	public void setGameId(int id) {
		_gameId = id;
	}

	public void setGameType(String type) {
		_gameType = type;
	}

	public void setIsLeaver(boolean leaver) {
		_leaver = leaver;
	}

	public void setCreationDate(Date date) {
		_createDate = date;
	}

	public void setQueue(LeagueMatchmakingQueue queue) {
		_queue = queue;
	}

	public int getDaysAgoPlayed() {
		return _daysAgoPlayed;
	}

	public List<String> getItemList() {
		return _itemList;
	}

	public List<LeagueSummonerSpell> getSpellList() {
		return _spellList;
	}

	public int getGameId() {
		return _gameId;
	}

	public String getGameType() {
		return _gameType;
	}

	public boolean isLeaver() {
		return _leaver;
	}

	public Date getCreationDate() {
		return _createDate;
	}

	public LeagueMatchmakingQueue getQueue() {
		return _queue;
	}

	public List<LeagueSummoner> getPlayerTeam() {
		return _playerTeam;
	}

	public List<LeagueSummoner> getEnemyTeam() {
		return _enemyTeam;
	}

	public List<LeagueSummoner> getAllPlayers() {
		List<LeagueSummoner> players = new ArrayList<LeagueSummoner>(_playerTeam);
		players.addAll(_enemyTeam);
		return players;
	}

	public LeagueChampion getChampionSelectionForSummoner(LeagueSummoner summoner) {
		return _playerChampionSelections.get(summoner.getId());
	}

	public int getStat(MatchHistoryStatType type) {
		Integer stat = _stats.get(type);
		if (stat == null)
			return 0;
		return stat.intValue();
	}

	public Map<String, Object> getPlayerKdr() {
		Map<String, Object> kdr = new HashMap<String, Object>();

		double kills = Double.parseDouble(_stats.get(MatchHistoryStatType.CHAMPIONS_KILLED).toString());
		double deaths = Double.parseDouble(_stats.get(MatchHistoryStatType.NUM_DEATHS).toString());
		double assists = Double.parseDouble(_stats.get(MatchHistoryStatType.ASSISTS).toString());
		double kda = (kills + assists) / deaths;

		int precision = 10; //keep 4 digits
		kills = Math.floor(kills * precision + .5) / precision;
		deaths = Math.floor(deaths * precision + .5) / precision;
		assists = Math.floor(assists * precision + .5) / precision;
		kda = Math.floor(kda * precision + .5) / precision;

		kdr.put("kills", kills);
		kdr.put("deaths", deaths);
		kdr.put("assists", assists);
		kdr.put("kdr", kda);

		return kdr;
	}

	public List<LeagueSummonerSpell> getSummonerSpellsFromIds(List<Integer> ids) {
		List<LeagueSummonerSpell> spells = new ArrayList<LeagueSummonerSpell>();

		for (Integer id : ids) {
			spells.add(LeagueSummonerSpell.getSpellWithId(id));

		}

		return spells;
	}

	public Map<MatchHistoryStatType, Integer> getImportantPlayerStats() {
		Map<MatchHistoryStatType, Integer> stats = new HashMap<MatchHistoryStatType, Integer>();

		stats.put(MatchHistoryStatType.TOTAL_DAMAGE_DEALT_TO_CHAMPIONS, _stats.get(MatchHistoryStatType.TOTAL_DAMAGE_DEALT_TO_CHAMPIONS));
		stats.put(MatchHistoryStatType.MAGIC_DAMAGE_DEALT_TO_CHAMPIONS, _stats.get(MatchHistoryStatType.MAGIC_DAMAGE_DEALT_TO_CHAMPIONS));
		stats.put(MatchHistoryStatType.PHYSICAL_DAMAGE_DEALT_TO_CHAMPIONS, _stats.get(MatchHistoryStatType.PHYSICAL_DAMAGE_DEALT_TO_CHAMPIONS));
		stats.put(MatchHistoryStatType.GOLD_EARNED, _stats.get(MatchHistoryStatType.GOLD_EARNED));
		stats.put(MatchHistoryStatType.SIGHT_WARDS_BOUGHT_IN_GAME, _stats.get(MatchHistoryStatType.SIGHT_WARDS_BOUGHT_IN_GAME));
		stats.put(MatchHistoryStatType.VISION_WARDS_BOUGHT_IN_GAME, _stats.get(MatchHistoryStatType.VISION_WARDS_BOUGHT_IN_GAME));
		stats.put(MatchHistoryStatType.LEVEL, _stats.get(MatchHistoryStatType.LEVEL));

		if (_stats.containsKey(MatchHistoryStatType.WIN)) {
			stats.put(MatchHistoryStatType.WIN, 1);
		} else {
			stats.put(MatchHistoryStatType.WIN, 0);

		}

		return stats;
	}

	public List<String> getItemNamesFromStatMap() {
		List<Integer> itemIds = new ArrayList<Integer>();

		if(_stats.containsKey((MatchHistoryStatType.ITEM0))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM0));
		}else{
			itemIds.add(0);
		}
		if(_stats.containsKey((MatchHistoryStatType.ITEM1))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM1));
		}else{
			itemIds.add(0);
		}
		if(_stats.containsKey((MatchHistoryStatType.ITEM2))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM2));
		}else{
			itemIds.add(0);
		}
		if(_stats.containsKey((MatchHistoryStatType.ITEM3))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM3));
		}else{
			itemIds.add(0);
		}
		if(_stats.containsKey((MatchHistoryStatType.ITEM4))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM4));
		}else{
			itemIds.add(0);
		}
		if(_stats.containsKey((MatchHistoryStatType.ITEM5))){
			itemIds.add(_stats.get(MatchHistoryStatType.ITEM5));
		}else{
			itemIds.add(0);
		}
		
		return getItemNamesFromItemIds(itemIds);
	}

	public static List<String> getItemNamesFromItemIds(List<Integer> itemIds) {
		List<String> itemNames = new ArrayList<String>();
		for (Integer i : itemIds) {
			if (i != 0) {
				itemNames.add(LeagueItem.getItemNameFromId(i));
			}
		}

		return itemNames;
	}

	public static int daysBetween(Date d1, Date d2) {
		return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static int getHowManyDaysAgoMatchWasPlayed(Date date) throws ParseException{
		
		Date today = new Date();

		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();

		cal1.setTime(today);
		cal2.setTime(date);
		
		return daysBetween(today,date);
	}

	public Map<MatchHistoryStatType, Integer> getAllStats() {
		return _stats;
	}
}