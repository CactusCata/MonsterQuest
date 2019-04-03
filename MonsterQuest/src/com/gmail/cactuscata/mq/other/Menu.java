package com.gmail.cactuscata.mq.other;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.cactuscata.mq.Main;
import com.gmail.cactuscata.mq.enums.PrefixMessage;
import com.gmail.cactuscata.mq.utils.Helper;

public class Menu implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("menu")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(PrefixMessage.MENU_BE_PLAYER + "");
				return true;
			}

			if (args.length < 1) {

				Player player = (Player) sender;
				FileConfiguration configSender = Main.getConfigPlayer(player.getUniqueId());
				File fileMonster = new File(Main.getInstance().getDataFolder(), "monster.yml");
				FileConfiguration configMonster = YamlConfiguration.loadConfiguration(fileMonster);
				Inventory inventory = Bukkit.createInventory(player, 27, "§8§lMenu de " + player.getName());

				inventory.setItem(0,
						Helper.createItem(Material.BOOK_AND_QUILL, 1, (byte) 0, "§6§lMon niveau",
								new String[] { "Cliquez pour améliorer vos aptitudes", "►" + player.getName() + "◄",
										"§d§l[Classe] " + configSender.getString("monsterquest.classe.actuel"),
										"§f=§e=§f=§e=§f=§e=§f=§e=§f=§e=",
										"§6§l[§e§lNiveau§6§l]: §f" + configSender.getInt("monsterquest.level"),
										"§6§l[§e§lXP§6§l]: §f" + configSender.getLong("monsterquest.xp.actuel") + "/"
												+ configSender.getLong("monsterquest.xp.next"),
								"§6§l[§e§lB.XP.MQ§6§l]: §fx " + configMonster.getInt("monsterquest.xpBoostServer"),
								"§6§l[§e§lBonus XP grade§6§l]: §f"
										+ configSender.getDouble("monsterquest.coef.coefgrade"),
								"§f=§e=§f=§e=§f=§e=§f=§e=§f=§e=", "§d§l[Stats Total]",
								"§dCumul de vos aptitudes/Armure/classe",
								"§9§l[§b§lMagie§9§l]: (à faire) §f%{_manamax}+{_ab.tot.Magie}*4%",
								"§4§l[§c§lForce§4§l]: (à faire) §f%{_force}+{_ab.tot.Force}%",
								"§2§l[§a§lVitalité§2§l]: §f%{_coeur}+{_ab.tot.Vita}/3%",
								"§8§l[§7§lResistance§8§l]: §7%{_resistance}%", "§f=§e=§f=§e=§f=§e=§f=§e=§f=§e=",
								"§6§l[§e§lGrade§6§l]: §9" + configSender.getString("general.gradeboutique") }));

				inventory.setItem(1,
						Helper.createItem(Material.BOOK_AND_QUILL, 1, (byte) 0, "§6✾✾✾ §6§lHonneur §6✾✾✾",
								new String[] { "§e✾ §f§l" + configSender.getInt("monsterquest.honneur") + " §e✾",
										"§aVous permet de prétendre", "§aau titre de §6§lRoi du MonsterQuest",
										"§7Grade gratuit limité à 7 jours", "§7Un seul Roi en même temps",
										"§7-Commandes uniques", "§7-Kit unique", "§7-Permet de lancer des events §6❤!",
										" ", "§e[✭ROI✭]▶ XXXXXXXXXX ◀", " ", "§a§l[BIENTOT] !" }));

				inventory.setItem(2,
						Helper.createItem(Material.BOOK_AND_QUILL, 1, (byte) 0, "§6✵✵✵ §6§lMon Score §6✵✵✵",
								new String[] { "§e✵ §f§l" + configSender.getInt("monsterquest.score") + "§e✵",
										"§6=§a=§6=§a=§6=§b[Récompenses]§6=§a=§6=§a=§6=", "§f[1K]", "§f[10K]", "§f[50K]",
										"§f[100K]", "§f[250K]", "§f[500K]", "§f[1M]",
										"§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=§6=§a=",
										"§a§l[Activé]" }));

				inventory.setItem(4, Helper.createItem(Material.IRON_AXE, 1, (byte) 0, "§6§lBucheron",
						new String[] { "§aRang %{_bucheron.lvl}% / 10*", "§eExpérience %{_bucheron.xp}%", "§fBois:",
								"§7Rg 1: Récolte 1", "§7Rg 2: Récolte 2, §bbois enchanté ✭",
								"§7Rg 3: Récolte 3, §bbois enchanté ✭✭", "§7Rg 4: Récolte 4, §bbois enchanté ✭✭✭",
								"§7Rg 5: ???", "§7Rg 6: ???", "§7Rg 7: ???", "§7Rg 8: ???", "§7Rg 9: ???",
								"§7Rg 10: ???", "§aTous les arbres sont", "§adisponibles à la coupe",
								"§bbois enchanté §f= Bois précieux", "§fpour craft les prochains ",
								"§fstuffs, objets magiques, arcs etc", "§a§l Activé !!" }));

				inventory.setItem(5,
						Helper.createItem(Material.ANVIL, 1, (byte) 0, "§6§lForgeron",
								new String[] { "§aRang %{_forgeron.lvl}% / 10*", "§eExpérience %{_forgeron.xp}%",
										"§fForge:", "§7Rg 1: Stuff Cuir", "§7Rg 2: Armatures", "§7Rg 3: Stuff Fer",
										"§7Rg 4: Stuff Acier", "§7Rg 5: ???", "§7Rg 6: ???", "§7Rg 7: ???",
										"§7Rg 8: ???", "§7Rg 9: ???", "§7Rg 10: ???", "§aUn Equipement avec un Rang",
										"§aexemple: [Rg E], [Rg A]", "§apeut être enchanté !",
										"§eClassement des équipements:", "§fE < D < C < B < A < S < SS < SSS",
										"§bUtilise les gemmes et le bois enchanté", "§c[En développement]*" }));

				inventory.setItem(6,
						Helper.createItem(Material.BREWING_STAND_ITEM, 1, (byte) 0, "§6§lAlchimiste",
								new String[] { "§aRang %{_alchimiste.lvl}% / 10*", "§eExpérience %{_alchimiste.xp}%",
										"§fRecettes Principales:", "§7Rg 1:Herbes / Pdre. Précieuse",
										"§7Rg 2:Potion Soin / Endurance, Fiole etc..",
										"§7Rg 3:Potion Mana / Alchimie, Craft Magique .. etc",
										"§7Rg 4:Potion Soin II / Papier / Sorcellerie, Alchimie ... etc",
										"§7Rg 5: Non disponible !", "§7Rg 6:", "§7Rg 7:", "§7Rg 8:", "§7Rg 9:",
										"§7Rg 10:", "§bUtilise les gemmes et le bois enchanté", "§a§l Activé !!*" }));

				inventory.setItem(8, Helper.createItem(Material.BARRIER, 1, (byte) 0, "§c§lFermer le menu",
						new String[] { "§a Fermer son menu" }));

				inventory.setItem(9, Helper.createItem(Material.GOLD_BARDING, 1, (byte) 0, "§6§lMa monture",
						new String[] { "§a Invoque ta monture*", "§r", "§cInformation :",
								"§7- Permet des déplacements rapides", "§7- N'est pas utilisable comme §eun pet", "§r",
								"§bBientôt :", "§7- Différentes montures possible!" }));

				inventory.setItem(10,
						Helper.createItem(Material.SULPHUR, 1, (byte) 0, "§6§lPierre de foyer",
								new String[] { "§a Retour à votre foyer*", "§r", "§cInformation :",
										"§7 Discuter avec un aubergiste pour", "§7  revenir automatiquement sur lui !",
										"§r", "§bFoyer :", "§7 %{_pdf}%" }));

				inventory.setItem(11, Helper.createItem(Material.ENCHANTED_BOOK, 1, (byte) 0,
						configSender.getString("monsterquest.guild.actuel"), new String[] { "%{_loreguilde}%*" }));

				inventory.setItem(13, Helper.createItem(Material.IRON_PICKAXE, 1, (byte) 0, "§6§lMineur",
						new String[] { "§aRang %{_mineur.lvl}% / 10", "§eExpérience (à faire) %{_bucheron.xp}%",
								"§fMinerais:", "§7Rg 1: Pierre", "§7Rg 2: Minerai Fer, Charbon ,Gravier",
								"7Rg 3: Minerai d'Or, §agemme ✭", "§7Rg 4: Minerai Titan,  §agemme ✭✭", "§7Rg 5: ???",
								"§7Rg 6: ???", "§7Rg 7: ???", "§7Rg 8: ???", "§7Rg 9: ???", "§7Rg 10: ???",
								"§aLa quantité de minerai récoltée", "§aaugmente en fonction", "§ade votre niveau",
								"§aGemme §f= Pierre précieuse", "§fpour craft les prochaines",
								"§farmures, armes, outils etc", "§a§l Activé !!" }));

				inventory.setItem(14, Helper.createItem(Material.ENCHANTMENT_TABLE, 1, (byte) 0, "§6§lEnchanteur",
						new String[] { "§c[En développement]" }));

				inventory.setItem(15, Helper.createItem(Material.WORKBENCH, 1, (byte) 0, "§6§lArtisan",
						new String[] { "§aRang %{_artisan.lvl}% / 10*", "§eExpérience %{_artisan.xp}%", "§fFabrique:",
								"§7Rg 1: Ressources, Clef ???", "§7Rg 2: Ressources, Clef ???, bloc parcelle",
								"§7Rg 3: Ressources, Clef ???, bloc parcelle",
								"§7Rg 4: Ressources, Clef ???, bloc parcelle", "§7Rg 5:", "§7Rg 6:", "§7Rg 7:",
								"§7Rg 8:", "§7Rg 9:", "§7Rg 10:", "§bUtilise les gemmes et le bois enchanté",
								"§c[En développement]*" }));

				inventory.setItem(17,
						Helper.createItem(Material.STAINED_GLASS_PANE, 1, (byte) 4, "§b§lToggle Scoreboard",
								new String[] { " §aCliquez pour cacher ou rendre visible le scoreboard*" }));

				inventory.setItem(18, Helper.createItem(Material.CHEST, 1, (byte) 0, "§6§lBanque",
						new String[] { "§c[En développement]*" }));

				inventory.setItem(19, Helper.createItem(Material.BRICK, 1, (byte) 0, "§6§lMa parcelle",
						new String[] { "§c[En développement]*" }));

				inventory
						.setItem(20,
								Helper.createItem(Material.CHEST, 1, (byte) 0, "§6§lHôtel de vente",
										new String[] { " §aCliquez pour voir vos objets en vente*",
												"      §7------------", "§bNbr en vente : §9%{_nbSell}%",
												"§bMoney: §9%{_money}%" }));

				inventory.setItem(21, Helper.createItem(Material.CHEST, 1, (byte) 0, "§6[§e⛃§6] §6§lLotterie §6[§e⛃§6]",
						new String[] { "§c[En développement]*" }));

				inventory
						.setItem(22,
								Helper.createItem(Material.IRON_SPADE, 1, (byte) 0, "§6§l?????",
										new String[] { "§fMétier de la pelle", "§fxd?", "§eAu choix:", "§eFossoyeur",
												"§ePelleteuse", "§eTerrassier", "§eet +", "",
												"§c[En développement]*" }));

				inventory.setItem(23, Helper.createItem(Material.IRON_SWORD, 1, (byte) 0, "§6§lBanque",
						new String[] { "§6§lChasseur de Prime" }));

				inventory.setItem(24, Helper.createItem(Material.FISHING_ROD, 1, (byte) 0, "§6§lPêcheur",
						new String[] { "§c[En développement]*" }));

				inventory.setItem(26, Helper.createItem(Material.GOLD_INGOT, 1, (byte) 0, "§6§lBoutique",
						new String[] { "§aClique pour plus d'infos*" }));

				player.openInventory(inventory);

			}

		}

		return false;
	}

}
