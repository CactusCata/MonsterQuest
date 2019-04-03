package com.gmail.cactuscata.mq.mana.spells;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.mana.ManaManager;

public class SpellManager implements Listener {

	private Main main;
	private ManaManager manaManager;

	public SpellManager(Main main, ManaManager manaManager) {

		this.main = main;
		this.manaManager = manaManager;

	}

	@EventHandler
	public void getItem(PlayerInteractEvent event) {

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();

			if (itemInHand != null && event.getHand() != EquipmentSlot.OFF_HAND) {

				ItemMeta itemInHandM = itemInHand.getItemMeta();

				if (itemInHandM != null && itemInHand.getType() == Material.BOOK) {

					new ItemIsSpell().activeSpell(main, event.getPlayer(), itemInHandM, manaManager);
					return;

				}
			}

		} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

			ItemStack itemInOffHand = event.getPlayer().getInventory().getItemInOffHand();

			if (itemInOffHand != null) {

				ItemMeta itemInOffHandM = itemInOffHand.getItemMeta();

				if (itemInOffHandM != null && itemInOffHand.getType() == Material.BOOK) {

					new ItemIsSpell().activeSpell(main, event.getPlayer(), itemInOffHandM, manaManager);
					return;

				}

			}
		}

	}

}
