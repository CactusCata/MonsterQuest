package com.gmail.cactuscata.mq.mana.spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.mana.ManaManager;
import com.gmail.cactuscata.mq.mana.spells.list.SpellsList;

public class ItemIsSpell {

	public void activeSpell(Main main, Player player, ItemMeta itemM, ManaManager manaManager) {

		SpellsList spell = check(itemM);

		if (spell == null)
			return;

		if (spell.getXp() > player.getLevel()) {
			player.sendMessage(PrefixMessage.MANA_FATALITY + "Vous n'avez pas le mana nécéssaire !");
			return;
		}

		if (MapSpell.getMap().contains(player.getName() + spell.getName())) {
			player.sendMessage(PrefixMessage.SPELL_WARN + "Votre sort est en cooldown !");
			return;
		}

		MapSpell.getMap().add(player.getName() + spell.getName());

		player.sendMessage(PrefixMessage.SPELL + "Vous avez reçu l'effet " + spell.getName());
		player.setLevel(player.getLevel() - spell.getXp());
		manaManager.removeMana(player, spell.getXp());

		int lvl = Integer.parseInt(itemM.getDisplayName().replaceAll("[^0-9]", ""));

		spell.action(spell, player, lvl);

		new BukkitRunnable() {
			public void run() {

				MapSpell.getMap().remove(player.getName() + spell.getName());

			}
		}.runTaskLater(main, spell.getTime());

	}

	private SpellsList check(ItemMeta itemM) {
		if (itemM.getDisplayName() == null)
			return null;

		for (SpellsList spells : SpellsList.values())
			if (itemM.getDisplayName().contains(spells.getName()))
				return spells;

		return null;
	}
	
	public static void addAndRemovePotionEffect(Entity entity, PotionEffectType effect, int duration, int amplifier){
		((LivingEntity) entity).removePotionEffect(effect);
		((LivingEntity) entity).addPotionEffect(new PotionEffect(effect, duration, amplifier));
	}

}
