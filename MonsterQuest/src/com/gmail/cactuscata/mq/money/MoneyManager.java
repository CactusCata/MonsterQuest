package com.gmail.cactuscata.mq.money;

import java.io.File;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.utils.SaveFile;
import com.gmail.cactuscata.mq.utils.TitleManager;

public class MoneyManager {

	private BoardManager boardManager;

	public MoneyManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public int getMoney(UUID uuid) {
		FileConfiguration config = Main.getConfigPlayer(uuid);
		return config.getInt("monsterquest.money");
	}

	public void addMoney(Player player, long amount) {
		File file = Main.getFilePlayer(player.getUniqueId());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		boardManager.updateMoney(amount + config.getLong("monsterquest.money"), player);
		config.set("monsterquest.money", config.getLong("monsterquest.money") + amount);
		SaveFile.savingFile(file, config);
		TitleManager.sendMoneyActionBarre(amount, "add", player);
	}

	public void removeMoney(Player player, long amount) {
		File file = Main.getFilePlayer(player.getUniqueId());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		boardManager.updateMoney(config.getLong("monsterquest.money") - amount, player);
		config.set("monsterquest.money", config.getLong("monsterquest.money") - amount);
		SaveFile.savingFile(file, config);
		TitleManager.sendMoneyActionBarre(amount, "remove", player);
	}

	public void setMoney(Player player, long amount) {
		File file = Main.getFilePlayer(player.getUniqueId());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		boardManager.updateMoney(amount, player);
		config.set("monsterquest.money", amount);
		SaveFile.savingFile(file, config);
		TitleManager.sendMoneyActionBarre(amount, "other", player);
	}

	public boolean haveManyMoney(Player player, long amount) {
		FileConfiguration config = Main.getConfigPlayer(player.getUniqueId());
		long moneyActual = config.getLong("monsterquest.money");
		if (moneyActual < amount)
			return false;
		return true;
	}

}
