package com.gmail.cactuscata.mq.board;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.api.ScoreboardSign;

public class BoardManager {

	private HashMap<Player, ScoreboardSign> scoreboardSignMap = new HashMap<>();

	public void updateBoard(Player player) {

		File file = Main.getFilePlayer(player.getUniqueId());
		if (!file.exists()) {
			System.out.println("Le file n'existe pas !");
			return;
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		ScoreboardSign scoreboardSign = new ScoreboardSign(player, "§b§lPleaseCraftMe");

		if (scoreboardSignMap.containsKey(player)) {
			scoreboardSignMap.get(player).destroy();
			scoreboardSignMap.remove(player);
			scoreboardSign.create();
		} else {
			scoreboardSign.create();
		}

		scoreboardSign.setLine(1, "§6☢☢☢☢☢☢☢☢☢☢☢☢☢☢☢§a");
		scoreboardSign.setLine(2, "§b§l➽§e§l§nMes Infos");
		if (config.getLong("monsterquest.xp.actuel") != 0)
			scoreboardSign.setLine(3, "-§7Level:    §6[" + config.getLong("monsterquest.level") + "§6] §a"
					+ config.getInt("monsterquest.xp.actuel") * 100 / config.getInt("monsterquest.xp.next") + "%");
		else
			scoreboardSign.setLine(3, "-§7Level:    §6[" + config.getLong("monsterquest.level") + "§6] §a" + 0 + "%");
		scoreboardSign.setLine(4, "-§7Money:    §a" + config.getLong("monsterquest.money") + "$");
		scoreboardSign.setLine(5, "-§7Honneur:  §a" + config.getLong("monsterquest.honneur"));
		scoreboardSign.setLine(6, " §f");

		if (config.getString("monsterquest.guild.actuel") == null) {
			System.out.println("monsterquest.guild.actuel == null");
			return;
		}

		if (!config.getString("monsterquest.guild.actuel").equalsIgnoreCase("aucun")) {

			String actuel = config.getString("monsterquest.guild.actuel");
			config = Main.getConfigGuilde(actuel);

			scoreboardSign.setLine(7, "§b§l➽§e§l§nMa Guilde");
			scoreboardSign.setLine(8, "-§7Nom:     §b" + config.getString("guild.name"));
			if (config.getLong("general.niveau") != 0)
				scoreboardSign.setLine(9, "-§7Niveau:    §6[" + config.getLong("guild.level") + "§6] §a"
						+ config.getInt("general.xp.actual") * 100 / config.getInt("guild.xp.next") + "%");
			else
				scoreboardSign.setLine(9, "-§7Niveau:    §6[" + config.getLong("guild.level") + "§6] §a" + 0 + "%");
			scoreboardSign.setLine(10, "-§7Tag       §d[§b" + config.getString("guild.tag") + "§d]");
			scoreboardSign.setLine(11, " §a");
		}

		scoreboardSign.setLine(12, "§6☢☢☢☢☢☢☢☢☢☢☢☢☢☢☢§f");

		if (!scoreboardSignMap.containsKey(player))
			scoreboardSignMap.put(player, scoreboardSign);

	}

	public void updateLevel(long level, int percent, Player player) {
		scoreboardSignMap.get(player).setLine(3, "-§7Level:    §6[" + level + "§6] §a" + percent + "%");
	}

	public void updateMoney(long amount, Player player) {
		scoreboardSignMap.get(player).setLine(4, "-§7Money:   §a" + amount + "$");
	}

	public void updateHonnor(long amount, Player player) {
		scoreboardSignMap.get(player).setLine(5, "-§7Honneur:  §a" + amount);
	}

	public void updateGuildName(String name, Player player) {
		scoreboardSignMap.get(player).setLine(8, "-§7Nom:     §b" + name);
	}

	public void updateGuildLevel(long level, int percent, Player player) {
		scoreboardSignMap.get(player).setLine(9, "-§7Niveau:    §6[" + level + "§6] §a" + percent + "%");
	}

	public void updateGuildTag(String tag, Player player) {
		scoreboardSignMap.get(player).setLine(10, "-§7Tag       §d[§b" + tag + "§d]");
	}

	public void updateGuild(Player player) {
		FileConfiguration configPlayer = Main.getConfigPlayer(player.getUniqueId());
		FileConfiguration configGuild = Main.getConfigGuilde(configPlayer.getString("monsterquest.guild.actuel"));
		scoreboardSignMap.get(player).setLine(7, "§b§l➽§e§l§nMa Guilde");
		updateGuildName(configGuild.getString("guild.name"), player);
		updateGuildLevel(configGuild.getLong("guild.level"),
				configGuild.getInt("guild.xp.actuel") * 100 / configGuild.getInt("guild.xp.next"), player);
		updateGuildTag(configGuild.getString("guild.tag"), player);
		scoreboardSignMap.get(player).setLine(11, " §a");

	}

	public HashMap<Player, ScoreboardSign> getScoreSign() {
		return scoreboardSignMap;
	}

}
