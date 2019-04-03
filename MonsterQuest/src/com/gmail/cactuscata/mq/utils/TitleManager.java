package com.gmail.cactuscata.mq.utils;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

public class TitleManager {

	public static void sendExperienceActionBarre(long amount, double totalCoef, Player player) {

		IChatBaseComponent cbc;

		if (totalCoef == 1)
			cbc = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§2+ "
					+ amount + "xp \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
		else
			// §6§l[§a+254XP§6§l] [§bBooster§cx8§7: §a+6545XP§6§l]
			cbc = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"§6§l[§bBooster§cx" + totalCoef + "§7: §a+"
					+ (long) (amount * totalCoef) + "XP§6§l]\"}]");

		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);

	}

	public static void sendMoneyActionBarre(long amount, String type, Player player) {

		IChatBaseComponent cbc = null;

		switch (type) {
		case "add":
			cbc = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§2+ "
					+ amount + "$ \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
			break;
		case "remove":
			cbc = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§c- "
					+ amount + "$ \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
			break;
		default:
			cbc = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§3 "
					+ amount + "$ \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
			break;
		}

		PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);

	}

	public static void sendMessage(String text, Player player) {

		IChatBaseComponent comp = ChatSerializer.a(text);
		PacketPlayOutChat packet = new PacketPlayOutChat(comp);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

	}

}
