package com.gmail.cactuscata.mq.guild;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.maths.Maths;
import com.gmail.cactuscata.mq.utils.SaveFile;

public class GuildExperienceManager {

	private GuildManager guildManager;

	public GuildExperienceManager(GuildManager guildManager) {
		this.guildManager = guildManager;
	}

	public void addXp(String guildName, long xpAdded) {

		File file = Main.getFileGuild(guildName);
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		long actualXp = config.getLong("guild.xp.actuel");
		long nextXp = config.getLong("guild.xp.next");
		double coefTotal = 1;
		coefTotal += config.getDouble("monsterquest.xp.coef") - 1;

		File secondFile = new File(Main.getInstance().getDataFolder(), "monster.yml");
		FileConfiguration secondConfig = YamlConfiguration.loadConfiguration(secondFile);
		coefTotal += secondConfig.getDouble("monsterquest.xpBoostServer") - 1;

		coefTotal = Maths.arrondidouble(coefTotal, 2);

		long xpMultiplie = (long) (xpAdded * coefTotal);
		long totalXp = xpMultiplie + actualXp;

		int actualLevel = config.getInt("guild.level");

		if (totalXp < nextXp) {
			config.set("guild.xp.actuel", totalXp);
			SaveFile.savingFile(file, config);
			return;
		}

		while (totalXp > nextXp) {
			totalXp = totalXp - nextXp;
			nextXp = (long) (nextXp * (Math.pow(4.601999999999879, -8) * Math.pow(actualLevel + 1, 2) + 1.03));
			actualLevel++;
			for (Player playerGuild : guildManager.getOnlinePlayerInGuild(guildName, 0))
				playerGuild.sendMessage(PrefixMessage.GUILD + "La guilde est passé lvl " + actualLevel + " !");
		}

		config.set("guild.level", actualLevel);
		config.set("guild.xp.actuel", totalXp);
		config.set("guild.xp.next", nextXp);
		SaveFile.savingFile(file, config);
	}

}
