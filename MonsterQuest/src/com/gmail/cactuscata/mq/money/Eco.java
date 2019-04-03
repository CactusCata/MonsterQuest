package com.gmail.cactuscata.mq.money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Eco implements CommandExecutor {

	private MoneyManager moneyManager;

	public Eco(MoneyManager moneyManager) {
		this.moneyManager = moneyManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("eco")) {

			if (args.length < 1) {
				sender.sendMessage(PrefixMessage.MONEY_WARN + "Veillez préciser <give|set|remove> !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.MONEY_WARN + "Veuillez préciser l'argent donné !");
				return true;
			}

			long money;
			try {
				money = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "La valeur " + args[1] + " n'est pas un nombre correct !");
				return true;
			}

			if (money <= 0 || money >= Long.MAX_VALUE) {
				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "Veuillez mettre une valeur au dessus de 0 $ !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.MONEY_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player target = Bukkit.getPlayerExact(args[2]);

			if (target != null && target.isOnline()) {

				if (args[0].equalsIgnoreCase("set")) {

					moneyManager.addMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Vous avez ajouté " + money + " $ au joueur " + target.getDisplayName() + ", son nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + " vous a ajouté " + money + " $ , votre nouveau solde est de "+ moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a ajouté " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId())+ " $ !");
					return true;

				}

				if (args[0].equalsIgnoreCase("remove")) {

					if (!moneyManager.haveManyMoney(target, money)) {
						sender.sendMessage(PrefixMessage.MONEY_FATALITY + "La somme " + money+ " est supérieur au solde du joueur " + target.getDisplayName() + " qui est de "+ moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					moneyManager.removeMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Vous avez retiré " + money + " $ au joueur " + target.getDisplayName() + ", son nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + "  vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId())+ " $ !");
					return true;
				}

				if (args[0].equalsIgnoreCase("set")) {

					moneyManager.setMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Le nouveau solde du joueur " + target.getDisplayName() + " est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + " vous a mis le nouveau solde de " + moneyManager.getMoney(target.getUniqueId())+ " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					return true;
				}

				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "L'argument " + args[0] + " est inconnue !");
				return true;

			} else {

				try {
					moneyManager.addMoney(target, 0);
				} catch (NullPointerException e) {
					sender.sendMessage(PrefixMessage.MONEY_FATALITY + "Le joueur n'existe pas !");
					return true;
				}

				if (args[0].equalsIgnoreCase("add")) {

					moneyManager.addMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Vous avez ajouté " + money + " $ au joueur "+ target.getDisplayName() + ", son nouveau solde est de "+ moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + "  vous a ajouté " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a ajouté " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId())+ " $ !");
					return true;

				}

				if (args[0].equalsIgnoreCase("remove")) {

					if (moneyManager.getMoney(target.getUniqueId()) < money) {
						sender.sendMessage(PrefixMessage.MONEY_FATALITY + "La somme " + money + " est supérieur au solde du joueur " + target.getDisplayName() + " qui est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					moneyManager.removeMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Vous avez retiré " + money + " $ au joueur " + target.getDisplayName() + ", son nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + " vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					return true;

				}

				if (args[0].equalsIgnoreCase("set")) {

					moneyManager.setMoney(target, money);
					sender.sendMessage(PrefixMessage.MONEY + "Le nouveau solde du joueur " + target.getDisplayName() + " est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					if (sender instanceof Player) {
						Player playersender = (Player) sender;
						target.sendMessage(PrefixMessage.MONEY + playersender.getDisplayName() + " vous a mis le nouveau solde de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
						return true;
					}

					target.sendMessage(PrefixMessage.MONEY + "La console vous a retiré " + money + " $ , votre nouveau solde est de " + moneyManager.getMoney(target.getUniqueId()) + " $ !");
					return true;
				}

				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "L'argument " + args[0] + " est inconnue !");
				return true;

			}

		}

		return false;
	}

}
