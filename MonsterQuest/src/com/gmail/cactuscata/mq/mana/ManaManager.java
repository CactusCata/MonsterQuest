package com.gmail.cactuscata.mq.mana;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.gmail.cactuscata.mq.Main;

public class ManaManager {

	public void addMana(Player player, int mana) {
		FileConfiguration config = Main.getConfigPlayer(player.getUniqueId());
		if (player.getLevel() + mana > config.getInt("monsterquest.mana.max"))
			player.setLevel(config.getInt("monsterquest.mana.max"));
		else
			player.setLevel(mana + player.getLevel());
		updateXpBar(player, config);
	}

	public void removeMana(Player player, int mana) {
		FileConfiguration config = Main.getConfigPlayer(player.getUniqueId());
		if (player.getLevel() - mana < 0)
			player.setLevel(0);
		else
			player.setLevel(player.getLevel() - mana);
		updateXpBar(player, config);
	}

	public void clearMana(Player player) {
		player.setLevel(0);
		player.setExp(0.0f);
	}

	public void setMana(Player player, int mana) {
		FileConfiguration config = Main.getConfigPlayer(player.getUniqueId());
		if (mana > config.getInt("monsterquest.mana.max"))
			player.setLevel(config.getInt("monsterquest.mana.max"));
		else
			player.setLevel(mana);
		updateXpBar(player, config);
	}

	private void updateXpBar(Player player, FileConfiguration config) {
		player.setExp(0.0f);
		int manaActual = player.getLevel();
		int manaMax = config.getInt("monsterquest.mana.max");
		float percent = (manaActual * 100 / manaMax);

		player.setExp(percent / 100);

	}

}
