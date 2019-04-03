package com.gmail.cactuscata.mq;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gmail.cactuscata.mq.board.BoardManager;

public class QuitEvent implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Player player = event.getPlayer();

		Team team = board.getTeam(player.getName());

		if (team == null)
			return;

		team.unregister();
		
		BoardManager boardManager = new BoardManager();
		boardManager.getScoreSign().remove(player);

	}

}
