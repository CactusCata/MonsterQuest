package com.gmail.cactuscata.mq.settag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Settag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("settag")) {

			if (args.length < 1) {
				sender.sendMessage(
						"§6- §9/settag §bprefix §e<§cjoueur§e> <§2prefix§e>\n§6- §9/settag §bsuffix §e<§cjoueur§e> <§2suffix§e>\n§6- §9/settag §bclear §e<§cjoueur§e>");
				return true;
			}

			if (args[0].equalsIgnoreCase("prefix")) {

				if (args.length < 2) {
					sender.sendMessage(PrefixMessage.SETTAG_WARN + "Veillez préciser le nom du joueur !");
					return true;
				}

				if (args.length < 3) {
					sender.sendMessage(PrefixMessage.SETTAG_WARN + "Veillez préciser le prefix !");
					return true;
				}

				ScoreboardManager manager = Bukkit.getScoreboardManager();
				Scoreboard board = manager.getMainScoreboard();
				Player player = Bukkit.getPlayerExact(args[1]);
						
				if (player != null && player.isOnline()) {

					createTeam(player.getName(), board);
					joinTeam(player.getName(), board);
					Team team = board.getTeam(player.getName());
					
					team.setPrefix(args[2].substring(0, Math.min(args[2].length(), 16)).replace('&', '§'));
					return true;

				}

				sender.sendMessage(PrefixMessage.SETTAG_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
				return true;

			}

			if (args[0].equalsIgnoreCase("suffix")) {

				if (args.length < 2) {
					sender.sendMessage(PrefixMessage.SETTAG_WARN + "Veillez préciser le nom du joueur !");
					return true;
				}

				if (args.length < 3) {
					sender.sendMessage(PrefixMessage.SETTAG_WARN + "Veillez préciser le suffix !");
					return true;
				}

				ScoreboardManager manager = Bukkit.getScoreboardManager();
				Scoreboard board = manager.getMainScoreboard();
				Player player = Bukkit.getPlayerExact(args[1]);
				if (player != null && player.isOnline()) {

					createTeam(player.getName(), board);
					joinTeam(player.getName(), board);
					Team team = board.getTeam(player.getName());
					team.setSuffix(args[2].substring(0, Math.min(args[2].length(), 16)).replace('&', '§'));
					return true;

				}

				sender.sendMessage(PrefixMessage.SETTAG_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
				return true;

			}

			if (args[0].equalsIgnoreCase("clear")) {

				if (args.length < 2) {
					sender.sendMessage(PrefixMessage.SETTAG_WARN + "Veillez préciser le joueur !");
					return true;
				}

				ScoreboardManager manager = Bukkit.getScoreboardManager();
				Scoreboard board = manager.getMainScoreboard();
				Player player = Bukkit.getPlayerExact(args[1]);
				if (player != null && player.isOnline()) {

					Team team = board.getTeam(player.getName());

					if (board.getTeam(player.getName()) == null) {
						sender.sendMessage(PrefixMessage.SETTAG_FATALITY + "Le joueur n'a déjà plus de team !");
						return true;
					}

					team.unregister();
					return true;

				}

				sender.sendMessage(PrefixMessage.SETTAG_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
				return true;

			}

			sender.sendMessage(PrefixMessage.SETTAG_FATALITY + "La section " + args[0] + " n'est pas valide !");
			return true;

		}
		return false;
	}

	private void createTeam(String NameOfPlayer, Scoreboard board) {
		if (board.getTeam(NameOfPlayer) == null)
			board.registerNewTeam(NameOfPlayer);
	}

	private void joinTeam(String NameOfPlayer, Scoreboard board) {

		Team team = board.getTeam(NameOfPlayer);
		Player player = Bukkit.getPlayerExact(NameOfPlayer);
		team.addEntry(player.getName());

	}

}
