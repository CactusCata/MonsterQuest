package com.gmail.cactuscata.mq;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.gmail.cactuscata.mq.tab.TitleTab;

public class ChangeWorld implements Listener {

	private TitleTab titleTab;

	public ChangeWorld(TitleTab titleTab) {
		this.titleTab = titleTab;
	}

	@EventHandler
	public void changeWorld(PlayerChangedWorldEvent event) {

		titleTab.Sendtab(event.getPlayer());

	}

}
