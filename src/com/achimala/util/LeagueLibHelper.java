package com.achimala.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueChampion;
import com.achimala.leaguelib.models.LeagueGame;
import com.achimala.leaguelib.models.LeagueItem;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.MatchHistoryEntry;
import com.achimala.leaguelib.models.MatchHistoryStatType;

public class LeagueLibHelper {

	public static Map<String, Map<String, Object>> getSummonerTeamInfo(LeagueConnection c, LeagueGame game, boolean enemy) throws LeagueException {

		Map<String, Map<String, Object>> players = new HashMap<String, Map<String, Object>>();
		if (game != null) {
			List<LeagueSummoner> playerList = null;
			if (enemy) {
				playerList = game.getEnemyTeam();
			} else {
				playerList = game.getPlayerTeam();
			}
			for (LeagueSummoner s : playerList) {
				Map<String, Object> playerData = new HashMap<String, Object>();

				LeagueChampion champ = game.getChampionSelectionForSummoner(s);

				// Player name and champion
				playerData.put("name", s.getName());
				playerData.put("champion", champ.getName());

				// Player league information, wins/losses, and elo
				if (s.getLeagueStats() == null)
					c.getLeaguesService().fillSoloQueueLeagueData(s);
				if (summonerIsRanked(s)) {
					playerData.put("league", s.getLeagueStats().getLeagueName());
					playerData.put("tier", s.getLeagueStats().getTier());
					playerData.put("rank", s.getLeagueStats().getRank());
					playerData.put("wins", s.getLeagueStats().getWins());
					playerData.put("losses", s.getLeagueStats().getLosses());
					playerData.put("elo", s.getLeagueStats().getApproximateElo());
				}

				if (s.getRankedStats() == null)
					c.getPlayerStatsService().fillRankedStats(s);
				playerData.put("kdrwithchampion", s.getRankedStats().getKdrForChampion(champ));
				players.put(s.getName(), playerData);
			}
		}

		return players;
	}

	public static Map<String, Map<String, Object>> getSummonerTeamInfoFromMatchHistoryEntry(LeagueConnection c, MatchHistoryEntry game, boolean enemy) throws LeagueException {

		Map<String, Map<String, Object>> players = new HashMap<String, Map<String, Object>>();
		if (game != null) {
			List<LeagueSummoner> playerList = null;
			if (enemy) {
				playerList = game.getEnemyTeam();
			} else {
				playerList = game.getPlayerTeam();
			}
			for (LeagueSummoner s : playerList) {
				Map<String, Object> playerData = new HashMap<String, Object>();

				LeagueChampion champ = game.getChampionSelectionForSummoner(s);

				// Player name and champion
				playerData.put("name", s.getName());
				playerData.put("champion", champ.getName());

				/* Disabled for now. Don't need this info for match history
				  
				 
				// Player league information, wins/losses, and elo
				if (s.getLeagueStats() == null)
					c.getLeaguesService().fillSoloQueueLeagueData(s);
				if (summonerIsRanked(s)) {
					playerData.put("league", s.getLeagueStats().getLeagueName());
					playerData.put("tier", s.getLeagueStats().getTier());
					playerData.put("rank", s.getLeagueStats().getRank());
					playerData.put("wins", s.getLeagueStats().getWins());
					playerData.put("losses", s.getLeagueStats().getLosses());
					playerData.put("elo", s.getLeagueStats().getApproximateElo());
				}
				

				
				if (s.getRankedStats() == null)
					c.getPlayerStatsService().fillRankedStats(s);
					
				*/
				playerData.put("kdrwithchampion", game.getPlayerKdr());
				playerData.put("spells", game.getSpellList());
				playerData.put("stats", game.getImportantPlayerStats());
				playerData.put("items",game.getItemList());
				
				players.put(s.getName(), playerData);
			}
		}

		return players;
	}

	public static List<MatchHistoryEntry> getMatchHistoryEntries(LeagueConnection c, LeagueSummoner summoner) throws LeagueException {

		List<MatchHistoryEntry> matchHistory = new ArrayList<MatchHistoryEntry>();

		if (summoner.getMatchHistory() == null) {
			c.getPlayerStatsService().fillMatchHistory(summoner);
		}

		matchHistory = summoner.getMatchHistory();

		// Get names of all of the summoners in match history
		for (MatchHistoryEntry en : matchHistory) {
			List<Object> summonerIds = new ArrayList<Object>();
			List<LeagueSummoner> sumList = en.getAllPlayers();
			for (LeagueSummoner s : sumList) {
				summonerIds.add(s.getId());
			}

			String[] names = c.getSummonerService().getSummonerNames(summonerIds.toArray());

			int count = 0;
			for (LeagueSummoner s : sumList) {
				s.setName(names[count]);
				count++;
			}

		}

		return matchHistory;
	}
    
    public static boolean connectAllAccounts(LeagueConnection c){

		// Connect all accounts, if we find any errors, print them and quit
		Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
		if (exceptions != null) {
			for (LeagueAccount account : exceptions.keySet())
				System.out.println(account + " error: " + exceptions.get(account));
			return false;
		}
		return true;
    }

	public static boolean summonerIsRanked(LeagueSummoner summoner) {
		if (summoner.getLeagueStats() == null)
			return false;
		return true;
	}
}
