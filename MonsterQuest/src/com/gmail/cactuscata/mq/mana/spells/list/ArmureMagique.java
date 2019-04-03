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
import com.gmail.cactuscata.mq.mana.spells.Spells;

public class ArmureMagique extends Spells {

	private final String name;
	private final int manaCost;
	private final long timeWait;
	private final Player player;
	private final int spellLevel;

	public ArmureMagique(Player player, int levelSpell) {
		this.name = "Armure Magique";
		this.spellLevel = levelSpell;
		this.manaCost = this.spellLevel * 8 + 10;
		this.timeWait = this.spellLevel * 4 + 2;
		this.player = player;

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int manaCost() {
		return this.manaCost;
	}

	@Override
	public long timeWait() {
		return this.timeWait;
	}

	@Override
	public void action() {
		Location loc = player.getLocation();
		World w = loc.getWorld();
		w.spawnParticle(Particle.VILLAGER_ANGRY, 0.3, 0.3, 0.3, 1, 10);
		ItemIsSpell.addAndRemovePotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 200, 2);
		List<Entity> players = player.getNearbyEntities(6, 6, 6);
		for (Entity entity : players) {
			if (entity.getType() == EntityType.PLAYER) {
				ItemIsSpell.addAndRemovePotionEffect((Player) entity, PotionEffectType.DAMAGE_RESISTANCE, 100, 1);
				entity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, 0.3, 0.8, 0.3, 1, 10);
				entity.sendMessage("Le joueur " + player.getDisplayName()
						+ " boost votre defense avec le sort Armure Magique LvL." + this.spellLevel + " !");
			}
		}

	}

	@Override
	public int getSpellLevel() {
		return this.spellLevel;
	}

}
