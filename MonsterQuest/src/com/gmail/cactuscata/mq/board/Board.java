package com.gmail.cactuscata.mq.board;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Board implements CommandExecutor {

	private BoardManager boardManager;
	private ArrayList<UUID> activeBoard = new ArrayList<>();

	public Board(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("board")) {

			if (args.length < 1) {

				if (sender instanceof Player) {

					Player player = (Player) sender;

					if (!activeBoard.contains(player.getUniqueId())) {

						boardManager.getScoreSign().get(player).destroy();
						activeBoard.add(player.getUniqueId());
						return true;
					}
					boardManager.updateBoard(player);
					activeBoard.remove(player.getUniqueId());
					return true;
				}
				sender.sendMessage(PrefixMessage.BOARD_BE_PLAYER + "");
				return true;

			}

			if (!sender.hasPermission("mq.admin.board")) {
				sender.sendMessage(PrefixMessage.ERROR_PERMISSION + "");
				return true;
			}

			if (args[0].equalsIgnoreCase("update")) {

				if (args.length < 2) {
					sender.sendMessage(PrefixMessage.BOARD_WARN + "Veuillez préciser le nom du joueur !");
					return true;
				}

				Player player = Bukkit.getPlayerExact(args[1]);

				if (player == null || !player.isOnline()) {
					sender.sendMessage(PrefixMessage.BOARD_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
					return true;
				}

				boardManager.updateBoard(player);
				return true;

			}

			if (args[0].equalsIgnoreCase("destroy")) {

				if (args.length < 2) {
					sender.sendMessage(PrefixMessage.BOARD_WARN + "Veillez préciser le nom du joueur !");
					return true;
				}

				Player player = Bukkit.getPlayerExact(args[1]);

				if (player == null || !player.isOnline()) {
					sender.sendMessage(PrefixMessage.BOARD_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
					return true;
				}

				boardManager.getScoreSign().get(player).destroy();
				return true;

			}

			sender.sendMessage(PrefixMessage.BOARD_FATALITY + "La section " + args[0] + " est introuvable !");
			return true;

		}

		return false;
	}

}
