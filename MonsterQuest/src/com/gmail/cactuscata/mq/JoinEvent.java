package com.gmail.cactuscata.mq;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.cactuscata.mq.tab.TitleTab;

public class JoinEvent implements Listener {

	TitleTab titleTab;

	public JoinEvent(TitleTab titleTab) {
		this.titleTab = titleTab;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		titleTab.Sendtab(event.getPlayer());

	}

}
