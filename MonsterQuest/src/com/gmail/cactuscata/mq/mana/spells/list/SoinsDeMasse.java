package com.gmail.cactuscata.mq.mana.spells.list;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.cactuscata.mq.mana.spells.ItemIsSpell;

public class SoinsDeMasse {

	public SoinsDeMasse(Player player, int lvl) {

		Location loc = player.getLocation();
		World w = loc.getWorld();
		w.playSound(loc, Sound.ENTITY_SPLASH_POTION_BREAK, 0.3F, 0.1F);
		w.spawnParticle(Particle.SPELL_WITCH, loc, (int) 10 * lvl, 0.3, 0.3, 0.3);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.REGENERATION, lvl * 20 + 100, lvl * 5 / 3);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.SLOW, 200, 3);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.WEAKNESS, 400, 5);
		List<Entity> players = player.getNearbyEntities(6, 6, 6);
		for (Entity entity : players) {
			if (entity.getType() == EntityType.PLAYER) {
				((LivingEntity) entity).removePotionEffect(PotionEffectType.REGENERATION);
				((LivingEntity) entity)
						.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, lvl * 10 + 50, lvl));
				entity.getWorld().spawnParticle(Particle.SPELL_WITCH, 0.3, 0.8, 0.3, 1, 10);
				entity.sendMessage("Le joueur " + player.getDisplayName()
						+ " vous a soigné avec le sort Soins de masse LvL." + lvl + " !");
			}
		}

	}

}