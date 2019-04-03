package com.gmail.cactuscata.mq.guild;

public enum GuildPower {

	MAITRE("Maître", 5, "§3§l[§cMaître de Guilde§3§l] §c"),
	BRAS_DROIT("Bras-Droit", 4, "§c[§9Bras-Droit§c] §9"),
	REPRESENTANT("Représentant,", 3, "§1[§2Représentant§1] §2"),
	MEMBRE("Membre", 8, "§2[§eMembre§2] §e"),
	RECRUE("Recrue", 1, "§e[§fMembre§e] §f");

	private final String nameOfRang, displayName;
	private final int power;

	private GuildPower(final String nameOfRang, final int power, final String diplayName) {
		this.nameOfRang = nameOfRang;
		this.power = power;
		this.displayName = diplayName;
	}

	public String getNameOfRang() {
		return nameOfRang;
	}

	public int getPowerRang() {
		return power;
	}
	
	public String getDisplayName(){
		return displayName;
	}

}
