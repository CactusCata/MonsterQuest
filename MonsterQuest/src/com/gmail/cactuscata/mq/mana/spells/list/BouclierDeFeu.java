package com.gmail.cactuscata.mq.mana.spells.list;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.gmail.cactuscata.mq.mana.spells.ItemIsSpell;

public class BouclierDeFeu {

	@SuppressWarnings("unused")
	public BouclierDeFeu(Player player, int lvl) {

		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, 1000, 0);

		List<Entity> players = player.getNearbyEntities(6, 6, 6);
		for (Entity entity : players)
			if (entity.getType() == EntityType.PLAYER) {
				entity.getWorld().spawnParticle(Particle.SPELL_WITCH, 2, 1, 2, 1, 5);
				ItemIsSpell.addAndRemovePotionEffect(entity, PotionEffectType.FIRE_RESISTANCE, 400, 0);
			}
		
		double raduis = 1.5;
		int angle = 5;
		double ascend = 0.6;
		int size = 4;
		double number = 360 / angle;
		double gain = ascend / number;
		double shrink = raduis / (size / gain);
		int height = 0;
		int current = 0;
		while (height < size) {
			raduis -= shrink;
			height += gain;
			current += angle;
			double calc = (current / 180) * Math.PI;
			double x = Math.cos(calc) * raduis;
			double z = Math.sin(calc) * raduis;
			Location locationPlayer = player.getLocation();
			Location loc = new Location(locationPlayer.getWorld(), locationPlayer.getX(), locationPlayer.getY() + 0.2, locationPlayer.getZ());
			
			
		}
		

	}

}
