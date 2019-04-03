package com.gmail.cactuscata.mq.tpserver;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;

public class TpOtherServer implements CommandExecutor {

	private Main main;
	private final String ERROR = "§cERROR : ";

	public TpOtherServer(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("tpotherserver")) {

			if (args.length < 1) {
				sender.sendMessage(ERROR + "Veuilez préciser le nom du joueur !");
				return true;
			}

			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null || !player.isOnline()) {
				sender.sendMessage(ERROR + "Le joueur " + args[0] + " n'est pas en ligne !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(ERROR + "Veuillez préciser le server !");
				return false;
			}

			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);

			try {
				out.writeUTF("Connect");
				out.writeUTF(args[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			player.sendPluginMessage(main, "BungeeCord", b.toByteArray());

			return true;

		}

		return false;
	}

}
