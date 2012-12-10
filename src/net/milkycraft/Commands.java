package net.milkycraft;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class Commands implements CommandExecutor {

	private final String pre = AQUA + "[ColoredGroups] ";
	private final String[] cmds = { "/cg help", "/cg test all", "/cg backup",
			"/cg import", "/cg test <group>", "/cg reload hooks",
			"/cg reload config", };
	private final String[] helps = { "Help command for ColoredGroups",
			"Tests all groups chat msgs", "Backs up config",
			"Imports values from prev. plugin",
			"Tests specific groups chat msgs", "Rehooks into permissions",
			"Reloads variables from config", };
	private ColoredGroups cg;

	protected Commands(final ColoredGroups cg1) {
		this.cg = cg1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(GREEN + "Using " + cg.getDescription().getName()
					+ " v" + cg.getDescription().getVersion() + " by "
					+ cg.getDescription().getAuthors().get(0));
			sender.sendMessage(GREEN + "Use /cg help");
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if (args.length == 1) {
				sender.sendMessage(RED + "Choose either config or hooks");
				return true;
			}
			if (args[1].equalsIgnoreCase("config")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					cg.reload();
					sender.sendMessage(pre + GREEN + "Reloaded variables");
				} else {
					sender.sendMessage(pre + RED
							+ "You dont have permission to reload");
				}
			} else if (args[1].equalsIgnoreCase("hooks")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					cg.rehook();
					sender.sendMessage(pre + GREEN + "Reloaded hooks");
				} else {
					sender.sendMessage(pre + RED
							+ "You dont have permission to reload");
				}
			} else if (args[1].equalsIgnoreCase("addons")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					sender.sendMessage(pre + GREEN
							+ "Addons have been removed from ColoredGroups");
				} else {
					sender.sendMessage(pre + RED
							+ "You dont have permission to reload");
				}
			}
		} else if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(GREEN + "== ColoredGroups help ==");
			for (int i = 0; i < cmds.length; i++) {
				sender.sendMessage(YELLOW + "[" + (i + 1) + "] " + cmds[i]
						+ WHITE + " - " + GOLD + helps[i]);
			}
		} else if (args[0].equalsIgnoreCase("backup")) {
			if (sender.hasPermission("coloredgroups.backup")) {
				this.cg.getBackupManager().create(50);
				sender.sendMessage(GREEN
						+ "Created a backup of the current config");
			} else {
				sender.sendMessage(RED
						+ "You don't have permission to create backups");
			}
		} else if (args[0].equalsIgnoreCase("import")) {
			if (sender.hasPermission("coloredgroups.import")) {
				try {
					new ImportationWorker(cg).run(true);
				} catch (Exception ex) {
					sender.sendMessage(RED + "Error: " + ex.getMessage());
					sender.sendMessage(RED
							+ "Vault couldn't find any chat providers to import from");
					return true;
				}
				sender.sendMessage(GREEN + "Imported groups via vault!");
			} else {
				sender.sendMessage(RED
						+ "You don't have permission to import groups");
			}
		} else if (args[0].equalsIgnoreCase("purge")) {
			if (sender.hasPermission("coloredgroups.backup.purge")) {
				this.cg.getBackupManager().purge();
				sender.sendMessage(GREEN
						+ "Purged all backups from the backup directory");
			} else {
				sender.sendMessage(RED
						+ "You don't have permission to purge backups");
			}
		} else if (args[0].equalsIgnoreCase("create")) {
			if (sender.hasPermission("coloredgroups.create")) {
				try {
					this.cg.getConfiguration().createNewGroup(args[1], "&",
							"&", "&", "[%g]%p: %m", args[1]);
					sender.sendMessage(GREEN
							+ "Created an empty section in config for "
							+ args[1]);
				} catch (Exception ex) {
					if (ex.getLocalizedMessage().equals("1")) {
						sender.sendMessage(RED + "Error: /cg create <Group>");
					}
				}
			} else {
				sender.sendMessage(RED
						+ "You don't have permission to purge backups");
			}
		} else if (args[0].equalsIgnoreCase("test")) {
			if (sender.hasPermission("coloredgroups.test")) {
				try {
					if (args[1].equalsIgnoreCase("all")) {
						for (ChatProfile c : cg.getChatProfiles()) {
							sender.sendMessage(c.getExample());
						}
					} else {
						sender.sendMessage(cg.getTestMessage(args[1]));
					}
				} catch (Exception exe) {
					sender.sendMessage(RED + "Error: Argument "
							+ exe.getLocalizedMessage() + " was empty");
				}
			} else {
				sender.sendMessage(RED
						+ "You don't have permission to test the colors");
			}
		}
		return false;
	}
}
