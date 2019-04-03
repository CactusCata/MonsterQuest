package com.gmail.cactuscata.mq.mana;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Mana implements CommandExecutor {

	private ManaManager manaManager;

	public Mana(ManaManager manaManager) {
		this.manaManager = manaManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("mana")) {

			if (args.length < 1) {
				sender.sendMessage(PrefixMessage.MANA_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null || !player.isOnline()) {
				sender.sendMessage(
						PrefixMessage.MANA_FATALITY + "Le joueur " + args[0] + " n'est pas en ligne !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.MANA_WARN + "Veuillez préciser [ADD/SET/CLEAR/REMOVE] !");
				return true;
			}

			if (args[1].equalsIgnoreCase("clear")) {
				manaManager.clearMana(player);
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.MANA_WARN + "Veuillez préciser le nombre");
				return true;
			}

			if (args[1].equalsIgnoreCase("add")) {
				manaManager.addMana(player, Integer.parseInt(args[2]));
				return true;
			}

			if (args[1].equalsIgnoreCase("remove")) {
				manaManager.removeMana(player, Integer.parseInt(args[2]));
				return true;
			}

			if (args[1].equalsIgnoreCase("set")) {
				manaManager.setMana(player, Integer.parseInt(args[2]));
				return true;
			}

			sender.sendMessage(PrefixMessage.MANA_FATALITY + "La section " + args[1] + " n'existe pas !");
			return true;

		}

		return false;
	}

}
