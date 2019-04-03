package com.gmail.cactuscata.mq.tab;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;

public class TitleTab {

	private FileConfiguration config;

	public TitleTab(FileConfiguration configYAMLTabText) {
		this.config = configYAMLTabText;
	}

	public void Sendtab(Player player) {

		String headerText = "[\"\",{\"text\":\"";
		String footherText = "[\"\",{\"text\":\"";

		for (String text : config.getStringList("Header")) {
			if (text.contains("%w")) {
				headerText += text.replace("%w", player.getWorld().getName().replace('_', ' ')) + "§r\n";
			} else {
				headerText += text + "§r\n";
			}
		}

		for (String text : config.getStringList("Foother")) {
			if (text.contains("%w")) {
				footherText += text.replace("%w", player.getWorld().getName().replace('_', ' ')) + "§r\n";
			} else {
				footherText += text + "§r\n";
			}
		}

		headerText += "\"}]";
		footherText += "\"}]";

		PacketPlayOutPlayerListHeaderFooter headerfooter = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field header = headerfooter.getClass().getDeclaredField("a");
			Field footer = headerfooter.getClass().getDeclaredField("b");
			header.setAccessible(true);
			footer.setAccessible(true);
			header.set(headerfooter, ChatSerializer.a(headerText.replace('&', '§')));
			footer.set(headerfooter, ChatSerializer.a(footherText.replace('&', '§')));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(headerfooter);
	}

}
