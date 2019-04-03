package com.gmail.cactuscata.mq.guild;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.api.UUIDFetcher;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.money.MoneyManager;
import com.gmail.cactuscata.mq.utils.SaveFile;
import com.gmail.cactuscata.mq.utils.TitleManager;

public class Guild implements CommandExecutor {

	private MoneyManager moneyManager;
	private FileConfiguration configYAMLMonsterQuest;
	private HashMap<String, ArrayList<String>> invitation = new HashMap<>();
	private ArrayList<String> guildName = new ArrayList<>();
	private ArrayList<String> waitAfterInvitation = new ArrayList<>();
	private ArrayList<String> waitAfterWantJoinGuild = new ArrayList<>();
	private ArrayList<String> waitDeleteGuildOwner = new ArrayList<>();
	private Main main;
	private GuildManager guildManager;
	private BoardManager boardManager;

	public Guild(MoneyManager moneyManager, FileConfiguration configYAMLMonsterQuest, Main main,
			GuildManager guildManager, BoardManager boardManager) {
		this.moneyManager = moneyManager;
		this.configYAMLMonsterQuest = configYAMLMonsterQuest;
		this.main = main;
		this.guildManager = guildManager;
		this.boardManager = boardManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length < 1) {
			sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser un argument !");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(PrefixMessage.GUILD_BE_PLAYER + "");
			return true;
		}

		Player playerSender = (Player) sender;
		File fileSender = Main.getFilePlayer(playerSender.getUniqueId());
		FileConfiguration configSender = YamlConfiguration.loadConfiguration(fileSender);
		int powerSender = configSender.getInt("monsterquest.guild.power");
		String guildNameSender = configSender.getString("monsterquest.guild.actuel");

		/*
		 * ------------------------- CREATE -------------------------
		 */

		if (args[0].equalsIgnoreCase("create")) {

			if (powerSender != 0) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous êtes déjà dans une guilde !");
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nom de la guilde !");
				return true;
			}

			if (args.length < 3) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le tag de la guilde !");
				return true;
			}

			// prix de la création d'un guilde dans le monster.yml
			int guildCost = configYAMLMonsterQuest.getInt("monsterquest.createGuildCost");

			if (!moneyManager.haveManyMoney(playerSender, guildCost)) {
				sender.sendMessage(
						PrefixMessage.GUILD_FATALITY + "Vous n'avez pas les " + guildCost + " $ requis, il vous manque "
								+ (guildCost - moneyManager.getMoney(playerSender.getUniqueId())) + " $ !");
				return true;
			}

			if (args[2].length() > 5 || args[2].length() < 3) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le tag doit être entre 3 et 5 caractères !");
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			if (fileGuild.exists()) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " existe déjà !");
				return true;
			}

			guildManager.createGuild(args[1], args[2], playerSender.getUniqueId().toString(), playerSender.getName());
			guildManager.updateFilePlayer(args[1], 5, playerSender.getUniqueId());
			moneyManager.removeMoney(playerSender, guildCost);
			playerSender.sendMessage(
					PrefixMessage.GUILD + "La guilde " + args[1] + " avec le tag " + args[2] + " a bien été créée !");
			boardManager.updateGuild(playerSender);
			return true;
			/*
			 * ------------------------- INVITE -------------------------
			 */
		}

		if (args[0].equalsIgnoreCase("invite")) {

			if (args.length < 2) {
				playerSender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le joueur à inviter !");
				return true;
			}

			if (powerSender == 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes actuellement dans aucune guilde !");
				return true;
			}

			if (waitAfterInvitation.contains(playerSender.getName())) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Veuillez attendre "
						+ configYAMLMonsterQuest.getInt("monsterquest.timeToWaitForReSendGuildInvitation")
						+ " secondes avant un autre invitation !");
				return true;
			}

			Player playerTarget = Bukkit.getPlayerExact(args[1]);
			if (playerTarget == null || !playerTarget.isOnline()) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Le joueur " + args[1] + " n'est pas en ligne !");
				return true;
			}

			FileConfiguration configTarget = Main.getConfigPlayer(playerTarget.getUniqueId());
			String nameOfTargetGuild = configTarget.getString("monsterquest.guild.actuel");

			if (!nameOfTargetGuild.equalsIgnoreCase("aucun")) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le joueur " + playerTarget.getDisplayName()
						+ " est déjà dans la guilde " + nameOfTargetGuild + " !");
				playerTarget.sendMessage(PrefixMessage.GUILD_WARN + "Le joueur " + playerSender.getDisplayName()
						+ " a tenté de vous invité dans le guilde " + guildNameSender
						+ " mais vous êtes déjà dans la guilde " + nameOfTargetGuild + " !");
				return true;
			}

			TitleManager.sendMessage(
					"[\"\",{\"text\":\"[\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"MQ\",\"color\":\"dark_aqua\"},{\"text\":\"]\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\" \"},{\"text\":\"Vous avez été invité dans la guilde\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\""
							+ guildNameSender
							+ "\",\"color\":\"dark_aqua\"},{\"text\":\" \"},{\"text\":\"par le joueur\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\""
							+ playerSender.getDisplayName()
							+ "\",\"color\":\"gold\"},{\"text\":\", l'invitation expirera dans\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\"60 secondes\",\"color\":\"red\"},{\"text\":\", cliquez sur\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\"[\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild join "
							+ guildNameSender
							+ "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Clic pour rejoindre la guilde !\",\"color\":\"dark_aqua\"}}},{\"text\":\"REJOINDRE\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild join "
							+ guildNameSender
							+ "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Clic pour rejoindre la guilde !\",\"color\":\"dark_aqua\"}}},{\"text\":\"]\",\"bold\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/guild join "
							+ guildNameSender
							+ "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Clic pour rejoindre la guilde !\",\"color\":\"dark_aqua\"}}},{\"text\":\" \"},{\"text\":\" \"},{\"text\":\"!\",\"color\":\"yellow\"}]",
					playerTarget);
			playerSender.sendMessage(PrefixMessage.GUILD + "Vous avez invité " + playerTarget.getDisplayName()
					+ " dans votre guilde, la " + guildNameSender + " !");

			FileConfiguration configGuild = Main.getConfigGuilde(guildNameSender);

			int membersMax = configGuild.getInt("guild.membersMax");
			int membersActualMember = configGuild.getInt("guild.numberOfActualMembers");
			if (membersActualMember + 1 > membersMax) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Vous êtes déjà au nombre maximum de personne dans la guilde qui est de " + membersMax
						+ " !");
				return true;
			}

			for (Player player : guildManager.getOnlinePlayerInGuild(guildNameSender, 0))
				if (player != playerSender)
					player.sendMessage(PrefixMessage.GUILD + playerSender.getDisplayName() + " à invité le joueur "
							+ playerTarget.getDisplayName() + "!");

			guildName.add(guildNameSender);
			invitation.put(playerTarget.getName(), guildName);
			waitAfterInvitation.add(playerSender.getName());

			new BukkitRunnable() {

				public void run() {

					if (invitation.get(playerTarget.getName()).contains(guildNameSender)) {
						invitation.get(playerTarget.getName()).remove(guildNameSender);
						waitAfterInvitation.remove(playerSender.getName());
						playerTarget.sendMessage(PrefixMessage.GUILD_WARN + "L'invitation de la guilde "
								+ guildNameSender + " a expiré !");
					}

				}
			}.runTaskLater(main, configYAMLMonsterQuest.getInt("monsterquest.timeToWaitForReSendGuildInvitation") * 20);
			return true;
		}

		/*
		 * ------------------------- JOIN -------------------------
		 */

		if (args[0].equalsIgnoreCase("join")) {

			if (powerSender != 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous êtes déjà dans la guilde " + guildNameSender);
				return true;
			}

			if (args.length < 2) {
				sender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le nom de la guilde !");
				return true;
			}

			if (!invitation.containsKey(playerSender.getName())) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'avez aucun invitation en cours !");
				return true;
			}

			File guildExist = Main.getFileGuild(args[1]);
			if (!guildExist.exists()) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "La guilde " + args[1] + " n'existe pas");
				return true;
			}

			if (!invitation.get(playerSender.getName()).contains(args[1])) {

				if (waitAfterWantJoinGuild.contains(playerSender.getName())) {
					playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
							+ "Vous avez déjà envoyé une invitation à la guilde " + args[1] + ", vous devez attendre "
							+ configYAMLMonsterQuest.getInt("monsterquest.timeToWaitForWantToSendRequestJoin") + " !");
					return true;
				}

				playerSender.sendMessage(PrefixMessage.GUILD + "Vous avez envoyé une requète d'invitation à la guilde "
						+ args[1] + " !");

				for (Player player : guildManager.getOnlinePlayerInGuild(args[1], 3))
					TitleManager.sendMessage(
							"[\"\",{\"text\":\"[\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"MQ\",\"color\":\"dark_aqua\"},{\"text\":\"]\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\" \"},{\"text\":\"Le joueur\",\"color\":\"yellow\"},{\"text\":\" \"},{\"text\":\""
									+ playerSender.getDisplayName()
									+ "\",\"color\":\"gold\"},{\"text\":\" demande à rejoidre la guilde " + args[1]
									+ ", \",\"color\":\"yellow\"},{\"text\":\"[\",\"bold\":true,\"color\":\"yellow\"},{\"text\":\"INVITER\",\"color\":\"dark_green\"},{\"text\":\"]\",\"bold\":true,\"color\":\"yellow\"},{\"text\":\" !\",\"color\":\"yellow\"}]",
							player);

				waitAfterWantJoinGuild.add(playerSender.getName());

				new BukkitRunnable() {
					public void run() {

						waitAfterWantJoinGuild.remove(playerSender.getName());

					}
				}.runTaskLater(main,
						configYAMLMonsterQuest.getInt("monsterquest.timeToWaitForReSendGuildInvitation") * 20);
				return true;
			}

			File fileGuild = Main.getFileGuild(args[1]);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);
			configGuild.set("guild.members." + playerSender.getUniqueId().toString(), 1);
			configGuild.set("guild.numberOfActualMembers", configGuild.getInt("guild.numberOfActualMembers") + 1);
			SaveFile.savingFile(fileGuild, configGuild);
			guildManager.updateFilePlayer(args[1], 1, playerSender.getUniqueId());
			invitation.get(playerSender.getName()).remove(guildNameSender);
			waitAfterInvitation.remove(playerSender.getName());

			for (Player player : guildManager.getOnlinePlayerInGuild(args[1], 1))
				player.sendMessage(PrefixMessage.GUILD + "Félicitation au joueur " + playerSender.getDisplayName()
						+ " qui a rejoins la guilde " + args[1] + " !");

			playerSender.sendMessage(PrefixMessage.GUILD + "Vous avez rejoins avec succès la guilde " + args[1] + " !");
			boardManager.updateBoard(playerSender);
			return true;
		}
		/*
		 * LEAVE GUILD
		 */

		if (args[0].equalsIgnoreCase("leave")) {

			if (powerSender == 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes actuellement dans aucune guilde !");
				return true;
			}

			if (powerSender == 5 && !waitDeleteGuildOwner.contains(playerSender.getName())) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Attention, vous êtes le chef de votre guilde !\nSi vous la quitté, tout ses membres en seront expulsé\nEnvoyez une nouvelle fois la commande pour confirmer !");
				waitDeleteGuildOwner.add(playerSender.getName());

				new BukkitRunnable() {
					public void run() {

						waitDeleteGuildOwner.remove(playerSender.getName());

					}
				}.runTaskLater(main, 120L);
				return true;
			}

			if (powerSender == 5 && waitDeleteGuildOwner.contains(playerSender.getName())) {

				guildManager.deleteGuild(guildNameSender, sender);
				playerSender
						.sendMessage(PrefixMessage.GUILD + "La guilde " + guildNameSender + " a bien été supprimée !");
				return true;

			}

			for (Player player : guildManager.getOnlinePlayerInGuild(guildNameSender, 0))
				player.sendMessage(PrefixMessage.GUILD_FATALITY + "Le joueur " + playerSender.getDisplayName()
						+ " a quitté la guilde !");

			File fileGuild = Main.getFileGuild(guildNameSender);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);
			guildManager.updateFilePlayer("aucun", 0, playerSender.getUniqueId());
			configGuild.set("guild.members." + playerSender.getUniqueId().toString(), null);
			configGuild.set("guild.numberOfActualMembers", configGuild.getInt("guild.numberOfActualMembers") - 1);
			SaveFile.savingFile(fileGuild, configGuild);
			playerSender.sendMessage(PrefixMessage.GUILD + "Vous avez quitté la guilde " + guildNameSender + " !");
			boardManager.updateBoard(playerSender);
			return true;
		}

		/*
		 * LIST OF MEMBERS IN GUILD
		 */

		if (args[0].equalsIgnoreCase("list")) {

			if (powerSender == 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes actuellement dans aucune guilde !");
				return true;
			}

			FileConfiguration configGuild = Main.getConfigGuilde(guildNameSender);
			int numberOfActualMember = configGuild.getInt("guild.numberOfActualMembers");
			int caseInventory = 9;

			while (numberOfActualMember > caseInventory)
				caseInventory += 9;

			System.out.println(caseInventory);

			Inventory inventory = Bukkit.createInventory(playerSender, caseInventory, "Guilde : " + guildNameSender);

			for (String playersUUID : configGuild.getConfigurationSection("guild.members").getKeys(false)) {
				FileConfiguration configPlayerUUID = Main.getConfigPlayer(UUID.fromString(playersUUID));
				Player playerGuild = Bukkit.getPlayer(UUID.fromString(playersUUID));
				int power = configGuild.getInt("guild.members." + playersUUID);
				GuildPower guildPower = guildManager.getRang(power);
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
				ArrayList<String> lore = new ArrayList<>();

				lore.add("§7§m§l-----------");

				if (playerGuild != null && playerGuild.isOnline()) {
					meta.setOwner(playerGuild.getName());
					meta.setDisplayName(guildPower.getDisplayName() + playerGuild.getName());
					lore.add("§fStatut : §2Connecté");
					lore.add("§7§m§l-----------");
				} else {
					OfflinePlayer offlinePlayer = Bukkit
							.getOfflinePlayer(UUID.fromString("9b8d31d5-420c-4f0c-80f0-de834b737a99"));
					meta.setOwner(offlinePlayer.getName());
					meta.setDisplayName(guildPower.getDisplayName() + offlinePlayer.getName());
					lore.add("§fStatut : §cDéconnecté");
					lore.add("§7§m§l-----------");
				}
				lore.add("§7Niveau : §6[§f" + configPlayerUUID.getInt("monsterquest.level") + "§6] ");
				lore.add("§7Honneur : §a" + configPlayerUUID.getInt("monsterquest.honnor"));
				lore.add("§7Classe : §8[§3" + configPlayerUUID.getString("monsterquest.classe.actuel")
						+ "§8] §9| lvl : " + configPlayerUUID.getInt("monsterquest.classe.classe1level"));
				lore.add("§7§m§l-----------");
				for (String key : configPlayerUUID.getConfigurationSection("monsterquest.metier").getKeys(true))
					lore.add("§6" + key + " : §5[§f" + configPlayerUUID.getInt("monsterquest.metier." + key) + "§5]");

				meta.setLore(lore);
				skull.setItemMeta(meta);
				inventory.addItem(skull);
			}

			playerSender.openInventory(inventory);
			return true;
		}

		/*
		 * ANNONCE
		 */

		if (args[0].equalsIgnoreCase("annonce")) {

			if (powerSender == 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes actuellement dans aucune guilde !");
				return true;
			}

			if (powerSender == 1) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Vous n'avez pas la permission d'utilsier cette commande avec le rang "
						+ guildManager.getRang(1).getNameOfRang() + "!");
				return true;
			}

			if (args.length < 2) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Veuillez préciser le message !");
				return true;
			}

			String message = PrefixMessage.GUILD + "[ANNONCE " + playerSender.getDisplayName() + "] ";
			for (int nbrArg = 1, sizeArg = args.length; nbrArg < sizeArg; nbrArg++) {
				message += args[nbrArg];
			}

			for (Player playerGuildOnline : guildManager.getOnlinePlayerInGuild(guildNameSender, 0)) {
				playerGuildOnline.sendMessage(message);
			}
			return true;
		}

		/*
		 * KICK
		 */

		if (args[0].equalsIgnoreCase("kick")) {

			if (powerSender == 0) {
				playerSender
						.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes actuellement dans aucune guilde !");
				return true;
			}

			if (powerSender < 3) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Vous n'avez pas la permission d'utilsier cette commande avec le rang "
						+ guildManager.getRang(powerSender).getNameOfRang() + " !");
				return true;
			}

			if (args.length < 2) {
				playerSender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le pseudo du joueur !");
				return true;
			}

			File fileGuild = Main.getFileGuild(guildNameSender);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);

			Player playerTarget = Bukkit.getPlayerExact(args[1]);
			UUID targetUUID;
			if (playerTarget != null && playerTarget.isOnline()) {
				targetUUID = playerTarget.getUniqueId();
				playerTarget.sendMessage(PrefixMessage.GUILD + "Vous avez été éjecté de la guilde " + guildNameSender
						+ " par " + playerSender.getDisplayName() + " !");
				boardManager.updateBoard(playerTarget);
			} else
				targetUUID = UUIDFetcher.getUUIDOf(args[1]);

			File fileTarget = Main.getFilePlayer(targetUUID);
			FileConfiguration configTarget = YamlConfiguration.loadConfiguration(fileTarget);
			if (!configTarget.getString("monsterquest.guild.actuel").equals(guildNameSender)) {
				playerSender.sendMessage(
						PrefixMessage.GUILD_FATALITY + "Le joueur " + args[1] + " n'est pas dans votre guilde !");
				return true;
			}

			for (Player playerInGuild : guildManager.getOnlinePlayerInGuild(guildNameSender, 0))
				playerInGuild.sendMessage(PrefixMessage.GUILD_FATALITY + "Le joueur " + playerSender.getDisplayName()
						+ " a quitté la guilde !");

			guildManager.updateFilePlayer("aucun", 0, targetUUID);
			configGuild.set("guild.members." + targetUUID.toString(), null);
			configGuild.set("guild.numberOfActualMembers", configGuild.getInt("guild.numberOfActualMembers") - 1);
			SaveFile.savingFile(fileGuild, configGuild);
			playerSender.sendMessage(PrefixMessage.GUILD + "Le joueur " + args[1] + " a bien été éjecté de la guilde "
					+ guildNameSender + " !");
			return true;
		}

		/*
		 * SetGrade
		 */

		if (args[0].equalsIgnoreCase("setgrade")) {

			if (powerSender == 0) {
				sender.sendMessage(PrefixMessage.GUILD_FATALITY + "Vous n'êtes dans aucune guilde !");
				return true;
			}

			if (powerSender != 5) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY
						+ "Vous n'avez pas les permission nécéssaire pour grader quelqu'un !");
				return true;
			}

			if (args.length < 2) {
				playerSender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le joueur à grader !");
				return true;
			}

			if (args.length < 3) {
				playerSender.sendMessage(PrefixMessage.GUILD_WARN + "Veuillez préciser le grade !");
				return true;
			}

			File fileGuild = Main.getFileGuild(guildNameSender);
			FileConfiguration configGuild = YamlConfiguration.loadConfiguration(fileGuild);
			GuildPower guildPower = guildManager.getRang(args[2]);
			if (guildPower == null) {
				playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "Le grade " + args[2] + " n'existe pas !");
				String msg = PrefixMessage.GUILD + "Liste des grades :";
				for (GuildPower guildRang : GuildPower.values()) {
					msg += "\n" + guildRang.getNameOfRang();
				}
				playerSender.sendMessage(msg);
				return true;
			}

			UUID targetUUID;
			System.out.println(args[1]);
			Player playerTarget = Bukkit.getPlayerExact(args[1]);
			if (playerTarget != null && playerTarget.isOnline()) {
				targetUUID = playerTarget.getUniqueId();
				playerTarget.sendMessage(PrefixMessage.GUILD + "Vous avez été gradé de la guilde "
						+ configGuild.getString("guild.name") + " par " + playerSender.getDisplayName()
						+ " au grade de " + guildPower.getNameOfRang() + " !");
			} else
				targetUUID = UUIDFetcher.getUUIDOf(args[1]);

			guildManager.updateFilePlayer(guildNameSender, guildPower.getPowerRang(), targetUUID);

			for (Player player : guildManager.getOnlinePlayerInGuild(guildNameSender, 0))
				player.sendMessage(PrefixMessage.GUILD + "Félicitation au membre " + playerTarget.getDisplayName()
						+ " qui passe " + guildPower.getNameOfRang() + " !");

			configGuild.set("guild.members." + playerTarget.getUniqueId().toString(), guildPower.getPowerRang());
			SaveFile.savingFile(fileGuild, configGuild);
			return true;

		}

		/*
		 * Help
		 */

		if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("aide")) {

			final String classement = PrefixMessage.GUILD + "/guilde classement";
			final String create = PrefixMessage.GUILD + "/guilde create [nom] [tag]";
			final String join = PrefixMessage.GUILD + "/guilde [join] [guilde]";
			final String leave = PrefixMessage.GUILD + "/guilde [leave]";
			final String list = PrefixMessage.GUILD + "/guilde list";
			final String annonce = PrefixMessage.GUILD + "/guilde annonce [message]";
			final String invite = PrefixMessage.GUILD + "/guilde invite [joueur]";
			final String kick = PrefixMessage.GUILD + "/guilde kick [joueur]";
			final String setgarde = PrefixMessage.GUILD + "/guilde setgrade [joueur] [grade]";
			final String delete = PrefixMessage.GUILD + "/guilde delete";
			String helpMessage = "";

			switch (powerSender) {
			case 0:
				helpMessage += classement + "\n";
				helpMessage += create + "\n";
				helpMessage += join;
				break;

			case 5:
				helpMessage += setgarde + "\n";
				helpMessage += delete + "\n";
			case 4:
				helpMessage += kick + "\n";
			case 3:
				helpMessage += invite + "\n";
			case 2:
				helpMessage += annonce + "\n";
			case 1:
				helpMessage += classement + "\n";
				helpMessage += leave + "\n";
				helpMessage += list;
			}
			playerSender.sendMessage(helpMessage);
			return true;

		}

		/*
		 * Classement
		 */

		if (args[0].equalsIgnoreCase("classement")) {
			playerSender.sendMessage(PrefixMessage.BOARD_WARN + "Commande en maintenance !");
			return true;
		}

		playerSender.sendMessage(PrefixMessage.GUILD_FATALITY + "L'argument " + args[0]
				+ " est inconnu, faites /guilde help pour plus d'information !");
		return true;

	}

}