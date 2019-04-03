package com.gmail.cactuscata.mq.mana;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.enums.PrefixMessage;

public class LaunchExpBootle implements Listener {

	private ManaManager manaManager;
	private HashMap<UUID, Player> map = new HashMap<>();
	private HashMap<UUID, Integer> level = new HashMap<>();

	public LaunchExpBootle(ManaManager manaManager) {
		this.manaManager = manaManager;
	}

	@EventHandler
	public void launchBottleXp(ProjectileLaunchEvent event) {
		if (event.getEntityType() == EntityType.THROWN_EXP_BOTTLE) {
			ThrownExpBottle bottleEntity = (ThrownExpBottle) event.getEntity();
			if (bottleEntity.getShooter() instanceof Player) {
				Player player = (Player) bottleEntity.getShooter();

				ItemStack bottle = getSlot(player);
				if (bottle.hasItemMeta()) {
					ItemMeta bottleM = bottle.getItemMeta();
					if (bottleM.getDisplayName() != null) {

						int lvl = Integer.parseInt(bottleM.getDisplayName().replaceAll("[^0-9]", ""));
						Random random = new Random();
						map.put(bottleEntity.getUniqueId(), player);
						level.put(bottleEntity.getUniqueId(), lvl * 10 + random.nextInt(lvl * 10 / 2) - lvl * 10 / 4);

					}
				}
			}

		}
	}

	@EventHandler
	public void expBottleExplod(ExpBottleEvent event) {
		event.setExperience(0);
		Player player = map.get(event.getEntity().getUniqueId());
		int manaMax = getMaxMana(player);
		if (manaMax == player.getLevel()) {
			player.sendMessage(PrefixMessage.MANA_WARN + "Votre mana est à fond !");
			return;
		}
		int manaAdded = level.get(event.getEntity().getUniqueId());
		manaManager.addMana(player, manaAdded);
		map.remove(event.getEntity().getUniqueId());
		level.remove(event.getEntity().getUniqueId());
	}

	@EventHandler
	public void ToggleSneak(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			Player player = event.getPlayer();
			Location loc = player.getLocation();
			Location blockLocation = new Location(player.getWorld(), loc.getBlockX(), loc.getBlockY() - 1,
					loc.getBlockZ());
			Block block = blockLocation.getBlock();
			if (block.getType() != Material.LAPIS_BLOCK)
				return;
			int manaMax = getMaxMana(player);
			if (manaMax == player.getLevel()) {
				player.sendMessage(PrefixMessage.MANA_WARN + "Votre mana est à fond !");
				return;
			}

			manaManager.addMana(player, manaMax / 50);
		}
	}

	private ItemStack getSlot(Player player) {
		ItemStack bottleMain = player.getInventory().getItemInMainHand();
		ItemStack bottleOff = player.getInventory().getItemInOffHand();
		if (bottleMain != null && bottleMain.getType() != Material.AIR) {
			return bottleMain;
		}
		return bottleOff;

	}

	private int getMaxMana(Player player) {
		FileConfiguration config = Main.getConfigPlayer(player.getUniqueId());
		return config.getInt("monsterquest.mana.max");
	}

}
