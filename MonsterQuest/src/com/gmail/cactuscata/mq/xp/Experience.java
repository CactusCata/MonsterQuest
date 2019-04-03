package com.gmail.cactuscata.mq.xp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Experience implements CommandExecutor {

	private ExperienceManager xpManager;

	public Experience(ExperienceManager xpManager) {
		this.xpManager = xpManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("addxp")) {

			if (args.length < 1) {
				sender.sendMessage(PrefixMessage.XP_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null || !player.isOnline()) {
				sender.sendMessage(PrefixMessage.XP_FATALITY + "Le joueur " + args[0] + " n'est pas en ligne !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.XP_WARN + "Veuillez préciser l'xp ajouté !");
				return true;
			}

			try {
				xpManager.addXp(player, Long.parseLong(args[1]));
			} catch (NumberFormatException e) {
				sender.sendMessage(PrefixMessage.XP_FATALITY + "Le nombre " + args[1]
						+ " est trop grand, la valeur maximum est " + Long.MAX_VALUE + " !");
			}

			return true;

		}

		return false;
	}
}
