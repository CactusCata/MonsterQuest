package com.gmail.cactuscata.mq.mana.spells.list;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.mana.spells.ItemIsSpell;

public class Explosion {

	private int stop = 0;

	public Explosion(Player player, int lvl) {

		World world = player.getWorld();

		stop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			public void run() {

				world.spawnParticle(Particle.SMOKE_LARGE, 0, 0, 0, 1, 10);
				if (stop == 5) {
					world.spawnParticle(Particle.FLAME, 0, 0, 0, 1, 10);
				}
				if (stop == 6) {
					world.spawnParticle(Particle.EXPLOSION_HUGE, 0, 0, 0, 1, 2);
					List<Entity> bats = player.getNearbyEntities(8, 8, 8);
					for (Entity entity : bats) {
						if (entity.getType() != EntityType.PLAYER) {
							entity.getWorld().spawnParticle(Particle.BLOCK_DUST, 0, 0, 0, 0, 5);
							ItemIsSpell.addAndRemovePotionEffect((Player) entity, PotionEffectType.HARM, 1, 0);
							Vector dir = entity.getLocation().getDirection();
							Vector vec = new Vector(dir.getX() * -0.8D, dir.getY() * 0.4, dir.getZ() * -0.8D);
							entity.setVelocity(vec);
						}
					}
					Bukkit.getScheduler().cancelTask(stop);
				}

			}
		}, 0L, 300L);

	}

}
