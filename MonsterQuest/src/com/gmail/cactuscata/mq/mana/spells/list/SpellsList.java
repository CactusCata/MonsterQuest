package com.gmail.cactuscata.mq.mana.spells.list;

import org.bukkit.entity.Player;

public enum SpellsList {

	BOUCLIER_ENERGIE("Bouclier d'energie", 10, 30L),
	SOINS_DE_MASSE("Soins de masse", 20, 80L),
	ASSAUT("Assaut", 30, 150L),
	RAYON_SOIN("Rayon soins", 15, 20L),
	ARMURE_MAGIQUE("Armure magique", 20, 40L),
	INVOCATION("Invocation", 80, 120L),
	EXPLOSION("Explosion", 40, 100L),
	BOUCLIER_DE_FEU("Bouclier de feu", 50, 80L);

	private final String displayName;
	private final int xp;
	private final long seconde;

	private SpellsList(final String displayName, final int xp, final long seconde) {
		this.displayName = displayName;
		this.seconde = seconde;
		this.xp = xp;
	}

	public String getName() {
		return displayName;
	}

	public int getXp() {
		return xp;
	}

	public long getTime() {
		return seconde;
	}

	public void action(SpellsList spell, Player player, int lvl) {

		switch (spell) {
		case BOUCLIER_ENERGIE:
			new BouclierEnergie(player, lvl);
			break;
		case SOINS_DE_MASSE:
			new SoinsDeMasse(player, lvl);
			break;
		case ASSAUT:
			new Assaut(player, lvl);
			break;
		case RAYON_SOIN:
			new RayonSoins(player, lvl);
			break;
		case ARMURE_MAGIQUE:
			new ArmureMagique(player, lvl);
			break;
		case INVOCATION:
			new Invocation(player, lvl);
			break;
		case EXPLOSION:
			new Explosion(player, lvl);
			break;
		case BOUCLIER_DE_FEU:
			new BouclierDeFeu(player, lvl);
			break;
		default:
			break;
		}

	}

}
