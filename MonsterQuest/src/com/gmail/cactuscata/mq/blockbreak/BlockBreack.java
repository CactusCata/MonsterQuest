package com.gmail.cactuscata.mq.blockbreak;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.mq.Main;

public class BlockBreack implements Listener {

	private Main main;
	private FileConfiguration config;
	private static HashMap<Location, Material> blockmaterial = new HashMap<>();

	public BlockBreack(Main main, FileConfiguration config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler
	public void blockBreackEvent(BlockBreakEvent event) {

		boolean c = false;

		if (config.getStringList("Worlds").contains("*")
				|| config.getStringList("Worlds").contains(event.getBlock().getWorld().getName()))
			c = true;

		// event.getPlayer().hasPermission("mq.admin.repopblock") ||

		if (!c)
			return;

		Block blockloc = event.getBlock();
		Location loc = new Location(blockloc.getWorld(), blockloc.getX(), blockloc.getY(), blockloc.getZ());
		Material material = blockloc.getType();
		boolean b = false;
		long time = 0L;

		for (String key : config.getConfigurationSection("Block").getKeys(false)) {
			if (Material.getMaterial(key) == blockloc.getType()) {
				time = config.getLong("Block." + key) * 20;
				blockmaterial.put(loc, Material.getMaterial(key));
				b = true;
				break;
			}
		}

		if (b) {

			new BukkitRunnable() {

				public void run() {

					if (!blockloc.getWorld().isChunkLoaded(blockloc.getWorld().getChunkAt(loc)))
						blockloc.getWorld().loadChunk(blockloc.getWorld().getChunkAt(loc));

					loc.getBlock().setType(material);
					blockmaterial.clear();
				}
			}.runTaskLater(main, time);

		}
	}

	public static HashMap<Location, Material> getBlockMaterial() {
		return blockmaterial;
	}

}
