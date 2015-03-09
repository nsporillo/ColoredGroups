package net.porillo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class Commands implements CommandExecutor {

    private final String pre = AQUA + "[ColoredGroups] ";
    private final String[] cmds = {
            "/cg help",
            "/cg test all",
            "/cg import",
            "/cg test <group>",
            "/cg reload config",};
    private final String[] helps = {
            "Help command for ColoredGroups",
            "Tests all groups chat msgs",
            "Imports values from prev. plugin",
            "Tests specific groups chat msgs",
            "Reloads variables from config",};
    private ColoredGroups cg;

    protected Commands(final ColoredGroups cg1) {
        this.cg = cg1;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(GREEN + "Using " + cg.getDescription().getName() + " v" + cg.getDescription().getVersion() + " by " + cg.getDescription().getAuthors().get(0));
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
                    sender.sendMessage(pre + RED + "You dont have permission to reload");
                }
            } else if (args[1].equalsIgnoreCase("tags")) {
                if (sender.hasPermission("coloredgroups.reload")) {
                    sender.sendMessage(pre + GREEN + "Reloaded Tags");
                    cg.retag();
                } else {
                    sender.sendMessage(pre + RED + "You dont have permission to reload");
                }
            }
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(GREEN + "== ColoredGroups help ==");
            for (int i = 0; i < cmds.length; i++) {
                sender.sendMessage(YELLOW + "[" + (i + 1) + "] " + cmds[i] + WHITE + " - " + GOLD + helps[i]);
            }
        } else if (args[0].equalsIgnoreCase("import")) {
            if (sender.hasPermission("coloredgroups.import")) {
                try {
                    new VaultImporter(cg).run(true);
                } catch (Exception ex) {
                    sender.sendMessage(RED + "Error: " + ex.getMessage());
                    sender.sendMessage(RED + "Vault couldn't find any chat providers to import from");
                    return true;
                }
                sender.sendMessage(GREEN + "Imported groups via vault!");
            } else {
                sender.sendMessage(RED + "You don't have permission to import groups");
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            if (sender.hasPermission("coloredgroups.create")) {
                try {
                    this.cg.getConfiguration().createNewGroup(args[1]);
                    sender.sendMessage(GREEN + "Created an empty section in config for " + args[1]);
                } catch (Exception ex) {
                    if (ex.getLocalizedMessage().equals("1")) {
                        sender.sendMessage(RED + "Error: /cg create <Group>");
                    }
                }
            } else {
                sender.sendMessage(RED + "You don't have permission to create groups");
            }
        } else if (args[0].equalsIgnoreCase("test")) {
            if (sender.hasPermission("coloredgroups.test")) {
                try {
                    if (args[1].equalsIgnoreCase("all")) {
                        for (ChatStyle c : cg.getFormats()) {
                            sender.sendMessage(c.getExample());
                        }
                    } else {
                        sender.sendMessage(cg.getTestMessage(args[1]));
                    }
                } catch (Exception exe) {
                    sender.sendMessage(RED + "Error: Argument " + exe.getLocalizedMessage() + " was empty");
                }
            } else {
                sender.sendMessage(RED + "You don't have permission to test the colors");
            }
        }
        return false;
    }
}
