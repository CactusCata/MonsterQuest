package com.gmail.cactuscata.mq.blockbreak;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RepopBlock implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("repopblock")) {

			if (args.length < 1) {

				sender.sendMessage(ChatColor.RED + "Veillez préciser l'argument !");
				return true;
			}

			if (!args[0].equalsIgnoreCase("reload")) {

				sender.sendMessage(ChatColor.RED + "Argument " + args[0] + " inconnue !");
				return true;

			}

			for (Location key : BlockBreack.getBlockMaterial().keySet()) {

				if (!key.getWorld().isChunkLoaded(key.getWorld().getChunkAt(key)))
					key.getWorld().loadChunk(key.getWorld().getChunkAt(key));
				key.getBlock().setType(BlockBreack.getBlockMaterial().get(key));

			}

			BlockBreack.getBlockMaterial().clear();
			sender.sendMessage(ChatColor.GREEN + "La config a bien été reload !");
			return true;

		}

		return false;

	}

}
