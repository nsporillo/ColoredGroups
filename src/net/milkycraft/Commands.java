package net.milkycraft;

import net.milkycraft.addons.Addon;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	private final String pre = ChatColor.AQUA + "[ColoredGroups] ";
	private final String[] cmds = { "/cg help", "/cg test all",
			"/cg test <group>", "/cg reload hooks", "/cg reload config", "/cg reload addons" };
	private final String[] helps = { "Help command for ColoredGroups",
			"Tests all groups chat msgs", "Tests specific groups chat msgs",
			"Rehooks into permissions", "Reloads variables from config", "Reloads addons if any" };
	ColoredGroups mc;

	public Commands(final ColoredGroups mc) {
		this.mc = mc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GREEN + "Using "
					+ mc.getDescription().getName() + " v"
					+ mc.getDescription().getVersion() + " by "
					+ mc.getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.GREEN + "Use /cg help");
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if(args.length == 1) {
				sender.sendMessage(ChatColor.RED + "Choose either config, hooks, or addons");
				return true;
			}
			if (args[1].equalsIgnoreCase("config")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					mc.reload();
					sender.sendMessage(pre + ChatColor.GREEN
							+ "Reloaded variables");
				} else {
					sender.sendMessage(pre + ChatColor.RED
							+ "You dont have permission to reload");
				}
			} else if (args[1].equalsIgnoreCase("hooks")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					mc.rehook();
					sender.sendMessage(pre + ChatColor.GREEN + "Reloaded hooks");
				} else {
					sender.sendMessage(pre + ChatColor.RED
							+ "You dont have permission to reload");
				}
			} else if (args[1].equalsIgnoreCase("addons")) {
				if (sender.hasPermission("coloredgroups.reload")) {
					mc.reloadAddons();
					sender.sendMessage(pre + ChatColor.GREEN + "Reloaded addons:");
					for(Addon a : mc.getAddons()) {
						sender.sendMessage(ChatColor.YELLOW + a.getClass().getSimpleName() + " v" + a.getVersion() + " by " + a.getAuthors().get(0));
					}
				} else {
					sender.sendMessage(pre + ChatColor.RED
							+ "You dont have permission to reload");
				}
			}
		} else if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.GREEN + "== ColoredGroups help ==");
			for (int i = 0; i < cmds.length; i++) {
				sender.sendMessage(ChatColor.YELLOW + "[" + (i + 1) + "] "
						+ cmds[i] + ChatColor.WHITE + " - " + ChatColor.GOLD
						+ helps[i]);
			}
		} else if (args[0].equalsIgnoreCase("test")) {
			if (sender.hasPermission("coloredgroups.test")) {
				try {
					if (args[1].equalsIgnoreCase("all")) {
						for (ChatProfile c : mc.getChatProfiles()) {
							sender.sendMessage(c.getExample());
						}
					} else {
						sender.sendMessage(mc.getTestMessage(args[1]));
					}
				} catch (Exception exe) {
					sender.sendMessage(ChatColor.RED + "Error: Argument "
							+ exe.getLocalizedMessage() + " was empty");
				}
			}
		}
		return false;
	}
	
}
