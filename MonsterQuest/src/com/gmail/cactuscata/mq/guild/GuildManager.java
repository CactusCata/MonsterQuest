package com.gmail.cactuscata.mq.guild;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.utils.SaveFile;

public class GuildManager {

	private BoardManager boardManager;

	public GuildManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	/**
	 * Récupère les joueurs d'une guilde qui sont en ligne
	 * 
	 * @param guildName
	 *            Nom de la guilde
	 * @param powerRequired
	 *            Le "power" requis pour être mis dans le liste
	 */
	public ArrayList<Player> getOnlinePlayerInGuild(String guildName, int powerRequired) {

		ArrayList<Player> guildMembers = new ArrayList<>();
		File fileGuild = Main.getFileGuild(guildName);
		FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

		for (String playersNames : configGuild.getConfigurationSection("guild.members").getKeys(true)) {
			int powerPlayer = configGuild.getInt("guild.members." + playersNames);
			if (powerPlayer < powerRequired)
				continue;
			Player player = Bukkit.getPlayer(UUID.fromString(playersNames));
			if (player != null && player.isOnline())
				guildMembers.add(player);
		}
		return guildMembers;
	}

	/**
	 * Récupère tout les joueurs d'une guilde
	 * 
	 * @param guildName
	 *            Nom de la guilde
	 */
	public ArrayList<UUID> getPlayerInGuild(String guildName) {

		ArrayList<UUID> guildMembers = new ArrayList<>();
		FileConfiguration configGuilde = Main.getConfigGuilde(guildName);

		for (String playersNames : configGuilde.getConfigurationSection("guild.members").getKeys(false))
			guildMembers.add(UUID.fromString(playersNames));
		return guildMembers;

	}

	/**
	 * Récupère le rang d'une personne par rapport à une entrée donnée
	 * 
	 * @param name
	 *            Nom du rang
	 */
	public GuildPower getRang(String name) {
		for (GuildPower rang : GuildPower.values())
			if (rang.getNameOfRang().equalsIgnoreCase(name))
				return rang;
		return null;
	}

	/**
	 * Récupère le rang d'une personne par rapport à son power
	 * 
	 * @param power
	 *            par rapport à un rang
	 */
	public GuildPower getRang(int power) {
		for (GuildPower rang : GuildPower.values())
			if (rang.getPowerRang() == power)
				return rang;
		return null;
	}

	/**
	 * Créer une guilde
	 * 
	 * @param nameOfGuild
	 *            Nom de la guilde voulu
	 * @param tagOfGuild
	 *            Tag de la guilde voulu
	 * @param uuidOfPlayer
	 *            Permet de mettre un premier membre dans la nouvelle guilde
	 * @param nameOfPlayer
	 *            Permet d'ajouter le nom du joueur en temps qu'owner
	 */
	public void createGuild(String nameOfGuild, String tagOfGuild, String uuidOfPlayer, String nameOfPlayer) {
		File fileGuild = Main.getFileGuild(nameOfGuild);
		FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

		SaveFile.savingFile(fileGuild, configGuild);
		configGuild.set("guild.name", nameOfGuild);
		configGuild.set("guild.tag", tagOfGuild);
		configGuild.set("guild.creationDate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		configGuild.set("guild.author", nameOfPlayer);
		configGuild.set("guild.guildOwner", uuidOfPlayer);
		configGuild.set("guild.numberOfActualMembers", 1);
		configGuild.set("guild.membersMax", 5);
		configGuild.set("guild.level", 1);
		configGuild.set("guild.xp.actuel", 0);
		configGuild.set("guild.xp.next", 100);
		configGuild.set("guild.xp.coef", 1);
		configGuild.set("guild.members." + uuidOfPlayer, 5);
		SaveFile.savingFile(fileGuild, configGuild);
	}

	/**
	 * Supprime une guilde (met à jour les fichiers des joueurs)
	 * 
	 * @param nameOfGuild
	 *            Nom de la guilde à supprimer
	 * @param sender
	 *            Celui qui détruit la guilde
	 */
	public void deleteGuild(String nameOfGuild, CommandSender sender) {
		File fileGuild = Main.getFileGuild(nameOfGuild);
		Player playerSender = null;
		if (sender instanceof Player)
			playerSender = (Player) sender;

		for (UUID playerUUID : getPlayerInGuild(nameOfGuild)) {
			updateFilePlayer("aucun", 0, playerUUID);
			Player player = Bukkit.getPlayer(playerUUID);
			if (player.isOnline()) {
				if (playerSender == null)
					player.sendMessage(PrefixMessage.GUILD_FATALITY + "Votre guilde du nom de " + nameOfGuild
							+ " a été supprimée comme par magie !");
				else
					player.sendMessage(PrefixMessage.GUILD_FATALITY + "Votre guilde du nom de " + nameOfGuild
							+ " a été supprimée par votre du chef du nom de " + player.getDisplayName() + "!");

				boardManager.updateBoard(player);
			}

		}

		fileGuild.delete();

	}

	/**
	 * Met à jour le fichier yml pour la section "guild" à jour
	 * 
	 * @param nameOfGuild
	 *            Nom de la guilde à mettre à jour
	 * @param power
	 *            Nouveau power du joueur
	 * @param uuidOfPlayer
	 *            Permet de trouver le fichier
	 */
	public void updateFilePlayer(String nameOfGuild, int power, UUID uuidOfPlayer) {
		File file = Main.getFilePlayer(uuidOfPlayer);
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("monsterquest.guild.actuel", nameOfGuild);
		config.set("monsterquest.guild.power", power);
		SaveFile.savingFile(file, config);
	}

}
