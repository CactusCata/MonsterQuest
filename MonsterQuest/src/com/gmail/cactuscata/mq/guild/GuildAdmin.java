package com.gmail.cactuscata.mq.guild;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.api.UUIDFetcher;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.utils.SaveFile;

public class GuildAdmin implements CommandExecutor {

	private GuildManager guildManager;
	private BoardManager boardManager;
	private GuildExperienceManager guildExpManager;

	public GuildAdmin(GuildManager guildManager, BoardManager boardManager, GuildExperienceManager guildExpManager) {
		this.guildManager = guildManager;
		this.boardManager = boardManager;
		this.guildExpManager = guildExpManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length < 1) {
			sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser l'action !");
			return true;
		}

		if (args[0].equalsIgnoreCase("create")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nom de la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le tag de la guilde !");
				return true;
			}

			if (args[2].length() > 5 || args[2].length() < 3) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le tag doit être entre 3 et 5 caractères !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			if (fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " existe déjà !");
				return true;
			}

			if (args.length < 4) {
				sender.sendMessage(
						PrefixMessage.GUILD_WARN + "Veuillez préciser le joueur qui sera maître de la guilde !");
				return true;
			}

			Player player = Bukkit.getPlayerExact(args[3]);
			UUID uuidTarget;
			if (player == null || !player.isOnline())
				uuidTarget = UUIDFetcher.getUUIDOf(args[3]);
			else
				uuidTarget = player.getUniqueId();

			guildManager.updateFilePlayer(args[1], 5, uuidTarget);
			sender.sendMessage(
					PrefixMessage.GUILD + "La guilde " + args[1] + " avec le tag " + args[2] + " a bien été créée !");
			guildManager.createGuild(args[1], args[2], uuidTarget.toString(), args[3]);
			boardManager.updateGuild(player);
			return true;

		}

		if (args[0].equalsIgnoreCase("delete")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + " Veuillez préciser la guilde ");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			guildManager.deleteGuild(args[1], sender);
			sender.sendMessage(PrefixMessage.GUILD + "La guilde " + args[1] + " a bien été supprimée !");
			return true;

		}

		if (args[0].equalsIgnoreCase("join")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nom de la guilde !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player playerTarget = Bukkit.getPlayerExact(args[2]);
			UUID uuidTarget;
			if (playerTarget == null || !playerTarget.isOnline())
				uuidTarget = UUIDFetcher.getUUIDOf(args[2]);
			else
				uuidTarget = playerTarget.getUniqueId();

			File fileTarget = Main.getFilePlayer(uuidTarget);
			FileConfiguration configTarget = YamlConfiguration.loadConfiguration(fileTarget);

			File fileOldGuildPlayer = Main.getFileGuild(configTarget.getString("monsterquest.guild.actuel"));
			FileConfiguration configOldGuildPlayer = YamlConfiguration.loadConfiguration(fileOldGuildPlayer);

			int numberOfActualMembers = configOldGuildPlayer.getInt("guild.numberOfActualMembers");

			if (numberOfActualMembers == 1) {
				guildManager.deleteGuild(configOldGuildPlayer.getString("monsterquest.guild.actuel"), sender);
			} else {
				configOldGuildPlayer.set("guild.members." + uuidTarget.toString(), null);
				configOldGuildPlayer.set("guild.numberOfActualMembers", numberOfActualMembers - 1);
			}

			configGuild.set("guild.members." + uuidTarget.toString(), 1);
			configGuild.set("guild.numberOfActualMembers", configGuild.getInt("guild.numberOfActualMembers") + 1);
			SaveFile.savingFile(fileGuild, configGuild);
			guildManager.updateFilePlayer(args[1], 1, uuidTarget);

			playerTarget.sendMessage(PrefixMessage.GUILD + "Vous avez rejoins avec succès la guilde " + args[1] + " !");
			sender.sendMessage(
					PrefixMessage.GUILD + "Vous avez bien ajouté le joueur " + args[2] + " dans la guilde " + args[1]);
			if (playerTarget != null && playerTarget.isOnline())
				boardManager.updateBoard(playerTarget);
			return true;

		}

		if (args[0].equalsIgnoreCase("kick")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nom de la guilde !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le joueur !");
				return true;
			}

			Player playerTarget = Bukkit.getPlayerExact(args[2]);
			UUID uuidTarget;
			if (playerTarget == null || !playerTarget.isOnline())
				uuidTarget = UUIDFetcher.getUUIDOf(args[2]);
			else
				uuidTarget = playerTarget.getUniqueId();

			configGuild.set("guild.members." + uuidTarget, null);

			for (Player playerInGuild : guildManager.getOnlinePlayerInGuild(configGuild.getString("guild.name"), 0)) {
				playerInGuild.sendMessage(PrefixMessage.GUILD_FATALITY + "Le joueur " + args[2]
						+ " a été kick de la guidle par un admin !");
			}

		}

		if (args[0].equalsIgnoreCase("setname")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nouveau nom de la guilde !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			for (UUID playersUUID : guildManager.getPlayerInGuild(args[1])) {
				File fileTarget = Main.getFilePlayer(playersUUID);
				FileConfiguration configTarget = YamlConfiguration.loadConfiguration(fileTarget);
				configTarget.set("monsterquest.guild.actuel", args[2]);
				SaveFile.savingFile(fileTarget, configTarget);
			}
			configGuild.set("guild.name", args[2]);
			SaveFile.savingFile(fileGuild, configGuild);
			fileGuild.renameTo(Main.getFileGuild(args[2]));
			sender.sendMessage(PrefixMessage.GUILD + "La guilde " + args[1] + " a bien été rennomé en " + args[2]);
			return true;

		}

		/*
		 * Pensez à faire les updates dans les teams du tab
		 */

		if (args[0].equalsIgnoreCase("settag")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nouveau tag de la guilde !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			configGuild.set("guild.tag", args[2]);
			SaveFile.savingFile(fileGuild, configGuild);
			sender.sendMessage(PrefixMessage.GUILD + "La guilde " + args[1] + " a bien reçu le tag " + args[2]);
			return true;

		}

		if (args[0].equalsIgnoreCase("setmembermax")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(
						PrefixMessage.GUILD_WARN + "Veuillez préciser le nouveau nombre maximum de la guilde !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			int membersMax;

			try {
				membersMax = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le nombre " + args[2] + " n'est pas correct !");
				return true;
			}

			if (membersMax > 54) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Le nombre maximum de joueur dans une guilde est de 54 maximum !");
				return true;
			}

			configGuild.set("guild.membersMax", membersMax);
			SaveFile.savingFile(fileGuild, configGuild);
			sender.sendMessage(PrefixMessage.GUILD + "La guilde " + args[1] + " a le nouveau maximum de membre à "
					+ membersMax + " !");
			return true;

		}

		if (args[0].equalsIgnoreCase("addxp")) {

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser l'xp !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);

			if (!fileGuild.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas !");
				return true;
			}

			long exp;

			try {
				exp = Long.parseLong(args[2]);
			} catch (NumberFormatException e) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le nombre " + args[2] + " n'est pas correct !");
				return true;
			}

			guildExpManager.addXp(args[1], exp);
			return true;

		}

		sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La section " + args[0] + " inconnue !");
		return true;

	}

}
