package com.gmail.cactuscata.mq.mana.spells.list;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.cactuscata.mq.Main;

import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.GenericAttributes;

public class Invocation {

	private HashMap<Player, Bat> map = new HashMap<>();

	public Invocation(Player player, int lvl) {

		Location loc = player.getLocation();
		Vector dir = loc.getDirection();
		Vector vec = new Vector(dir.getX(), dir.getY() * 2D, dir.getZ());
		player.setVelocity(vec);

		new BukkitRunnable() {
			public void run() {

				Bat bat = (Bat) player.getWorld().spawnEntity(loc, EntityType.BAT);
				bat.setCustomName("§6§lInvocation");
				bat.setCustomNameVisible(true);
				EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) bat).getHandle();
				nmsEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(8);
				bat.addPassenger(player);
				map.put(player, bat);
				List<Entity> bats = player.getNearbyEntities(6, 6, 6);
				for (Entity entity : bats)
					if (entity.getType() == EntityType.BAT)
						entity.getWorld().spawnParticle(Particle.SPELL_WITCH, 2, 1, 2, 1, 5);

			}
		}.runTaskLater(Main.getInstance(), 260L);
		
		new BukkitRunnable() {
			public void run() {

				Bat bat = map.get(player);
				bat.remove();

			}
		}.runTaskLater(Main.getInstance(), 310L);

	}

}
