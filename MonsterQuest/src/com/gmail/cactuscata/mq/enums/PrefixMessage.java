package com.gmail.cactuscata.mq.enums;

public enum PrefixMessage {

	BASIC_PREFIX("§5§l[§3MQ§5§l]§a "),

	SENDER_BE_PLAYER("La commande ne peut etre execute que par un joueur !"),

	BOARD("§2[§eBoard§2] §3"),
	BOARD_WARN("§2[§eBoard§2] §6"),
	BOARD_FATALITY("§2[§eBoard§2] §c"),
	BOARD_BE_PLAYER(BOARD_FATALITY + "" + SENDER_BE_PLAYER),

	MANA("§6[§9Mana§6] §3"),
	MANA_WARN("§6[§9Mana§6] §6"),
	MANA_FATALITY("§6[§9Mana§6] §c"),
	MANA_BE_PLAYER(MANA_FATALITY + "" + SENDER_BE_PLAYER),

	SPELL("§9[§5Sort§5] §3"),
	SPELL_WARN("§5[§6Mana§5] §6"),
	SPELL_FATALITY("§5[§4Board§5] §c"),
	SPELL_BE_PLAYER(SPELL_FATALITY + "" + SENDER_BE_PLAYER),

	MONEY("§4[§6Money§4] §3"),
	MONEY_WARN("§4[§6Money§4] §6"),
	MONEY_FATALITY("§4[§6Money§4] §c"),
	MONEY_BE_PLAYER(MONEY_FATALITY + "" + SENDER_BE_PLAYER),

	SETTAG("§5[§bSettag§5] §3"),
	SETTAG_WARN("§5[§bSettag§5] §6"),
	SETTAG_FATALITY("§5[§bSettag§5] §c"),
	SETTAG_BE_PLAYER(SETTAG_FATALITY + "" + SENDER_BE_PLAYER),
	
	XP("§5[§eXP§5] §3"),
	XP_WARN("§5[§eXP§5] §6"),
	XP_FATALITY("§5[§eXP§5] §c"),
	XP_BE_PLAYER(XP_FATALITY + "" + SENDER_BE_PLAYER),
	
	GUILD("§3[§9Guilde§3] §3"),
	GUILD_WARN("§3[§9Guilde§3] §6"),
	GUILD_FATALITY("§3[§9Guilde§3] §c"),
	GUILD_BE_PLAYER(GUILD_FATALITY + "" + SENDER_BE_PLAYER),
	
	MENU("§e[§dMenu§e] §e"),
	MENU_WARN("§e[§dMenu§e] §6"),
	MENU_FATALITY("§e[§dMenu§e] §c"),
	MENU_BE_PLAYER(MENU_FATALITY + "" + SENDER_BE_PLAYER),
	
	APTITUDES("§4[§aAptitudes§4] §e"),
	APTITUDES_WARN("§4[§aAptitudes§4] §6"),
	APTITUDES_FATALITY("§4[§aAptitudes§4] §c"),
	APTITUDES_BE_PLAYER(APTITUDES_FATALITY + "" + SENDER_BE_PLAYER),

	CONFIG("§5[§7Config§5]§e "),

	ERROR_PERMISSION("§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");

	private final String text;

	private PrefixMessage(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
