package com.gmail.cactuscata.mq;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gmail.cactuscata.mq.blockbreak.BlockBreack;
import com.gmail.cactuscata.mq.blockbreak.RepopBlock;
import com.gmail.cactuscata.mq.board.Board;
import com.gmail.cactuscata.mq.board.BoardManager;
import com.gmail.cactuscata.mq.guild.Guild;
import com.gmail.cactuscata.mq.guild.GuildAdmin;
import com.gmail.cactuscata.mq.guild.GuildExperienceManager;
import com.gmail.cactuscata.mq.guild.GuildManager;
import com.gmail.cactuscata.mq.mana.LaunchExpBootle;
import com.gmail.cactuscata.mq.mana.Mana;
import com.gmail.cactuscata.mq.mana.ManaManager;
import com.gmail.cactuscata.mq.mana.spells.SpellManager;
import com.gmail.cactuscata.mq.money.Balance;
import com.gmail.cactuscata.mq.money.Eco;
import com.gmail.cactuscata.mq.money.MoneyManager;
import com.gmail.cactuscata.mq.money.Pay;
import com.gmail.cactuscata.mq.other.Aptitudes;
import com.gmail.cactuscata.mq.other.Menu;
import com.gmail.cactuscata.mq.settag.Settag;
import com.gmail.cactuscata.mq.tab.TitleTab;
import com.gmail.cactuscata.mq.tpserver.TpOtherServer;
import com.gmail.cactuscata.mq.utils.FileDefaultSaving;
import com.gmail.cactuscata.mq.xp.Experience;
import com.gmail.cactuscata.mq.xp.ExperienceManager;

public class Main extends JavaPlugin implements PluginMessageListener {

	private static Main main;

	public void onEnable() {

		main = this;

		File fileYAMLRepopblock = new File(getDataFolder(), "repopblock.yml");
		FileConfiguration configYAMLRepopblock = YamlConfiguration.loadConfiguration(fileYAMLRepopblock);
		File filmYAMLTabText = new File(getDataFolder(), "tabtext.yml");
		FileConfiguration configYAMLTabText = YamlConfiguration.loadConfiguration(filmYAMLTabText);
		File fileYAMLMonsterQuest = new File(getDataFolder(), "monster.yml");
		FileConfiguration configYAMLMonsterQuest = YamlConfiguration.loadConfiguration(fileYAMLMonsterQuest);

		saveAndUpdate(fileYAMLRepopblock, configYAMLRepopblock);
		saveAndUpdate(filmYAMLTabText, configYAMLTabText);
		saveAndUpdate(fileYAMLMonsterQuest, configYAMLMonsterQuest);

		PluginManager pm = getServer().getPluginManager();
		BoardManager boardManager = new BoardManager();
		MoneyManager moneyManager = new MoneyManager(boardManager);
		ManaManager manaManager = new ManaManager();
		ExperienceManager xpManager = new ExperienceManager(boardManager);
		GuildManager guildManager = new GuildManager(boardManager);
		GuildExperienceManager guildExpManager = new GuildExperienceManager(guildManager);
		TitleTab titleTab = new TitleTab(configYAMLTabText);

		pm.registerEvents(new BlockBreack(this, configYAMLRepopblock), this);
		pm.registerEvents(new QuitEvent(), this);
		pm.registerEvents(new JoinEvent(titleTab), this);
		pm.registerEvents(new ChangeWorld(titleTab), this);
		pm.registerEvents(new LaunchExpBootle(manaManager), this);
		pm.registerEvents(new SpellManager(this, manaManager), this);

		getCommand("board").setExecutor(new Board(boardManager));
		getCommand("repopblock").setExecutor(new RepopBlock());
		getCommand("settag").setExecutor(new Settag());
		getCommand("tpotherserver").setExecutor(new TpOtherServer(this));
		getCommand("eco").setExecutor(new Eco(moneyManager));
		getCommand("balance").setExecutor(new Balance(moneyManager));
		getCommand("pay").setExecutor(new Pay(moneyManager));
		getCommand("addxp").setExecutor(new Experience(xpManager));
		getCommand("mana").setExecutor(new Mana(manaManager));
		getCommand("guild").setExecutor(new Guild(moneyManager, configYAMLMonsterQuest, this, guildManager, boardManager));
		getCommand("guildadmin").setExecutor(new GuildAdmin(guildManager, boardManager, guildExpManager));
		getCommand("menu").setExecutor(new Menu());
		getCommand("aptitudes").setExecutor(new Aptitudes());

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

	}

	private void saveAndUpdate(File file, FileConfiguration config) {
		FileDefaultSaving fileDefaultSaving = new FileDefaultSaving(this);
		fileDefaultSaving.checkIfFileExist(file, config);
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
	}

	public static Main getInstance() {
		return main;
	}

	// TODO update les fichiers joueurs/guilde quand on fait join avec le guildadmin
	
	public static FileConfiguration getConfigPlayer(UUID uuid) {
		File file = new File("plugins/MonsterQuest/players/" + uuid.toString() + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}

	public static FileConfiguration getConfigGuilde(String actuel) {
		File file = new File("plugins/MonsterQuest/guilds/" + actuel + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}

	public static File getFilePlayer(UUID uuid) {
		return new File("plugins/MonsterQuest/players/" + uuid.toString() + ".yml");
	}

	public static File getFileGuild(String actuel) {
		return new File("plugins/MonsterQuest/guilds/" + actuel + ".yml");
	}
	
//	public static FileConfiguration getConfigPlayer(UUID uuid) {
//		File file = new File("../nmqdata/players/" + uuid.toString() + ".yml");
//		return YamlConfiguration.loadConfiguration(file);
//	}
//	
//	public static File getFilePlayer(UUID uuid) {
//		return new File("../nmqdata/players/" + uuid.toString() + ".yml");
//	}
//	
//	public static File getFileGuild(String actuel) {
//		return new File("../nmqdata/guilds/" + actuel + ".yml");
//	}
//
//	public static FileConfiguration getConfigGuilde(String actuel) {
//		File file = new File("../nmqdata/guilds/" + actuel + ".yml");
//		return YamlConfiguration.loadConfiguration(file);
//	}

}
