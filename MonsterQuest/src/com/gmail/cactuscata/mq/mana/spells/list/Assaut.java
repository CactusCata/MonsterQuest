package com.gmail.cactuscata.mq.mana.spells.list;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.mana.spells.ItemIsSpell;

public class Assaut {

	public Assaut(Player player, int lvl) {

		Location loc = player.getLocation();
		World w = loc.getWorld();
		w.spawnParticle(Particle.FLAME, loc, (int) 10 * lvl, 0.3, 0.3, 0.3);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.INCREASE_DAMAGE, lvl * 20 + 100, lvl * 5 / 3);
		Vector dir = player.getLocation().getDirection();
		Vector vec = new Vector(dir.getX() * -1.4D, dir.getY(), dir.getZ() * -1.4D);
		player.setVelocity(vec);

		new BukkitRunnable() {
			public void run() {

				Vector dir2 = player.getLocation().getDirection();
				Vector vec2 = new Vector(dir2.getX() * 10.9D, dir2.getY() + 0.6, dir2.getZ() * 10.9D);
				player.setVelocity(vec2);

			}
		}.runTaskLater(Main.getInstance(), 20L);

	}

}
