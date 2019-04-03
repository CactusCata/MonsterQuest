package com.gmail.cactuscata.mq.xp;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.maths.Maths;
import com.gmail.cactuscata.mq.utils.SaveFile;
import com.gmail.cactuscata.mq.utils.TitleManager;

public class ExperienceManager {

	private BoardManager boardManager;

	public ExperienceManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public void addXp(Player player, long xpAdded) {
		File file = Main.getFilePlayer(player.getUniqueId());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		long actualXp = config.getLong("monsterquest.xp.actuel");
		long nextXp = config.getLong("monsterquest.xp.next");
		double coefTotal = 1;

		for (String key : config.getConfigurationSection("monsterquest.coef").getKeys(true)) {
			coefTotal += config.getDouble("monsterquest.coef." + key) - 1;
		}

		File secondFile = new File(Main.getInstance().getDataFolder(), "monster.yml");
		FileConfiguration secondConfig = YamlConfiguration.loadConfiguration(secondFile);
		coefTotal += secondConfig.getDouble("monsterquest.xpBoostServer") - 1;

		coefTotal = Maths.arrondidouble(coefTotal, 2);

		long xpMultiplie = (long) (xpAdded * coefTotal);
		long totalXp = xpMultiplie + actualXp;

		int actualLevel = config.getInt("monsterquest.level");

		if (totalXp < nextXp) {
			config.set("monsterquest.xp.actuel", totalXp);
			TitleManager.sendExperienceActionBarre(xpAdded, coefTotal, player);
			boardManager.updateLevel(actualLevel, (int) (totalXp * 100 / nextXp), player);
			SaveFile.savingFile(file, config);
			return;
		}

		while (totalXp > nextXp) {
			totalXp = totalXp - nextXp;
			nextXp = (long) (nextXp * (Math.pow(4.601999999999879, -8) * Math.pow(actualLevel + 1, 2) + 1.03));
			actualLevel++;
			player.sendMessage(PrefixMessage.XP + "Bravo tu es passé lvl " + actualLevel + " !");

		}

		TitleManager.sendExperienceActionBarre(xpAdded, coefTotal, player);

		boardManager.updateLevel(actualLevel, (int) (totalXp * 100 / nextXp), player);
		config.set("monsterquest.level", actualLevel);
		config.set("monsterquest.xp.actuel", totalXp);
		config.set("monsterquest.xp.next", nextXp);
		SaveFile.savingFile(file, config);
	}

}
