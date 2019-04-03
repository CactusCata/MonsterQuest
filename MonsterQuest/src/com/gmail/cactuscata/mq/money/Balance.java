package com.gmail.cactuscata.mq.money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Balance implements CommandExecutor {

	private MoneyManager moneyManager;

	public Balance(MoneyManager moneyManager) {
		this.moneyManager = moneyManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("balance") || cmd.getName().equalsIgnoreCase("money")) {

			if (args.length == 0) {

				if (!(sender instanceof Player)) {
					sender.sendMessage(PrefixMessage.MONEY_BE_PLAYER + "");
					return true;
				}

				Player player = (Player) sender;
				player.sendMessage(PrefixMessage.MONEY + "La valeur de votre solde est de " + moneyManager.getMoney(player.getUniqueId()));
				return true;

			} else {

				if (!sender.hasPermission("essentials.eco.balance.other")) {
					sender.sendMessage(PrefixMessage.ERROR_PERMISSION + "");
					return true;
				}

				Player player = Bukkit.getPlayerExact(args[0]);
				if (player == null || !player.isOnline()) {
					sender.sendMessage(PrefixMessage.MONEY_FATALITY + "Le joueur " + args[0] + " n'est pas en ligne !");
					return true;
				}

				sender.sendMessage(PrefixMessage.MONEY + "Le joueur " + player.getDisplayName() + " a comme argent " + moneyManager.getMoney(player.getUniqueId()) + " !");
				return true;
			}

		}

		return false;
	}

}
