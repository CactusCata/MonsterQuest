package com.gmail.cactuscata.mq.money;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class Pay implements CommandExecutor {

	private MoneyManager moneyManager;

	public Pay(MoneyManager moneyManager) {
		this.moneyManager = moneyManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("pay")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(PrefixMessage.MONEY_BE_PLAYER + "");
				return true;
			}

			if (args.length < 1) {
				sender.sendMessage(PrefixMessage.MONEY_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player player = Bukkit.getPlayerExact(args[0]);
			System.out.println(player);
			System.out.println(UUID.randomUUID());
			if (player == null || !player.isOnline()) {
				sender.sendMessage(
						PrefixMessage.MONEY_FATALITY + "Le joueur " + args[0] + " n'est pas en ligne !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.MONEY_WARN + "Veillez préciser le montant !");
				return true;
			}

			long amount = Integer.parseInt(args[1]);

			if (amount <= 0 || amount >= Long.MAX_VALUE) {
				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "Le montant ne peut être inférieur à 1 $!");
				return true;
			}

			Player playerSender = (Player) sender;

			if (moneyManager.haveManyMoney(playerSender, amount)) {
				sender.sendMessage(PrefixMessage.MONEY_FATALITY + "Vous n'avez pas assez d'argent !");
				return true;
			}

			moneyManager.addMoney(player, amount);
			moneyManager.removeMoney(playerSender, amount);

			player.sendMessage(
					PrefixMessage.MONEY + "Vous avez reçu " + amount + " $ du joueur " + playerSender.getDisplayName()
							+ ", votre nouveau montant est de " + moneyManager.getMoney(player.getUniqueId()) + " $ !");
			playerSender.sendMessage(PrefixMessage.MONEY + "Vous avez envoyé " + amount + " $ au joueur "
					+ player.getDisplayName() + ", votre nouveau montant est de "
					+ moneyManager.getMoney(playerSender.getUniqueId()) + " $ !");
			return true;

		}

		return false;
	}

}
