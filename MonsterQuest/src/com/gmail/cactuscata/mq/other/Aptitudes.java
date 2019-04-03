package com.gmail.cactuscata.mq.other;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Aptitudes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("aptitudes") || cmd.getName().equalsIgnoreCase("aptitude")
				|| cmd.getName().equalsIgnoreCase("apt")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(PrefixMessage.APTITUDES_BE_PLAYER + "");
				return true;
			}

			Player playerSender = (Player) sender;

			Inventory inventory = Bukkit.createInventory(playerSender, 27, "§d§lAptitudes");

			inventory.addItem(getSkull(
					"http://textures.minecraft.net/texture/b6965e6a58684c277d18717cec959f2833a72dfa95661019dbcdf3dbf66b048"));

			playerSender.openInventory(inventory);

		}

		return false;
	}

	// private void setSkullUrl(String skinUrl, Block block) {
	// block.setType(Material.SKULL);
	// Skull skullData = (Skull) block.getState();
	// skullData.setSkullType(SkullType.PLAYER);
	//
	// TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld)
	// block.getWorld()).getHandle()
	// .getTileEntity(new BlockPosition(block.getX(), block.getY(),
	// block.getZ()));
	// skullTile.setGameProfile(getNonPlayerProfile(skinUrl));
	// block.getState().update(true);
	// }
	//
	// private GameProfile getNonPlayerProfile(String skinURL) {
	// GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
	// newSkinProfile.getProperties().put("textures",
	// new Property("textures",
	// Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL +
	// "\"}}}")));
	// return newSkinProfile;
	// }

	public ItemStack getSkull(String url) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (url.isEmpty())
			return head;

		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}

}
