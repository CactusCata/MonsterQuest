package com.gmail.cactuscata.mq.mana.spells.list;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.gmail.cactuscata.mq.mana.spells.ItemIsSpell;

public class BouclierEnergie {

	public BouclierEnergie(Player player, int lvl) {

		Location loc = player.getLocation();
		World w = loc.getWorld();
		w.spawnParticle(Particle.REDSTONE, 0, 0, 0, 15);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.ABSORPTION, 600, 8);
		List<Entity> players = player.getNearbyEntities(6, 6, 6);
		for (Entity entity : players) {
			if (entity.getType() == EntityType.PLAYER) {
				ItemIsSpell.addAndRemovePotionEffect((Player) entity, PotionEffectType.ABSORPTION, 300, 4);
				entity.getWorld().spawnParticle(Particle.SPELL_WITCH, 0.3, 0.8, 0.3, 1, 10);
				entity.sendMessage("Le joueur " + player.getDisplayName()
						+ " vous a soigné avec le sort Bouclier d'enrgie LvL." + lvl + " !");
			}
		}

	}

}
