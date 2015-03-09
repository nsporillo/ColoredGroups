package net.porillo.commands;

import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class CreateCommand extends AbstractCommand {

    public CreateCommand(ColoredGroups cg) {
        super(cg);
        super.setName("create");
        super.setPermission("coloredgroups.create");
        super.setRequiredArgs(1);
        super.addUsage("Creates new group in config", "group");
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }
        String arg = args.get(0);
        try {
            super.plugin.getConfiguration().createNewGroup(arg);
            super.sendMessage(sender, GREEN + "Created an empty section in config for " + arg);
        } catch (Exception ex) {
            if (ex.getLocalizedMessage().equals("1")) {
                super.sendMessage(sender, RED + "Error: /cg create <Group>");
            } else {
                ex.printStackTrace();
            }
        }
    }
}