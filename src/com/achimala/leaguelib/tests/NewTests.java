package com.achimala.leaguelib.tests;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.connection.LeagueServer;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueGame;
import com.achimala.leaguelib.models.LeagueRunePage;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.MatchHistoryEntry;
import com.achimala.util.LeagueLibHelper;

public class NewTests {

	private static int count = 0;
	private static ReentrantLock lock = new ReentrantLock();
	private static Condition done = lock.newCondition();

	private static void incrementCount() {
		lock.lock();
		count++;
		// System.out.println("+ count = " + count);
		lock.unlock();
	}

	private static void decrementCount() {
		lock.lock();
		count--;
		if (count == 0)
			done.signal();
		// System.out.println("- count = " + count);
		lock.unlock();
	}

	public static void main(String[] args) throws Exception {

		// Get the connection
		final LeagueConnection c = new LeagueConnection(LeagueServer.NORTH_AMERICA);

		// Add an account to use for lookups
		c.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.NORTH_AMERICA, "3.9.13_07_11_12_46", "username", "password"));

		// Summoner we are testing on
		final String SUMMONER_TO_LOOK_UP = "SummonerName";

		if (!LeagueLibHelper.connectAllAccounts(c)) {
			return;
		}

		lock.lock();
		incrementCount();

		// Get the LeagueSummoner object for the summoner's name
		LeagueSummoner sum = c.getSummonerService().getSummonerByName(SUMMONER_TO_LOOK_UP);

		// Testing match history entry changes
		List<MatchHistoryEntry> matchHistory = LeagueLibHelper.getMatchHistoryEntries(c, sum);
		for (MatchHistoryEntry en : matchHistory) {
			try {
				
				System.out.println(LeagueLibHelper.getSummonerTeamInfoFromMatchHistoryEntry(c, en, false));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
