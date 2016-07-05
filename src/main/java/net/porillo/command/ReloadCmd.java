package net.porillo.command;

import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class ReloadCmd extends AbstractCmd {

    public ReloadCmd(ColoredGroups cg) {
        super(cg);
        super.setName("reload");
        super.setPermission("coloredgroups.reload");
        super.setRequiredArgs(1);
        super.addUsage("Reloads setting (config or tag)", "setting");
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }
        String arg = args.get(0);
        if (arg.equalsIgnoreCase("config")) {
            super.plugin.reload();
            super.sendMessage(sender, GREEN + "Reloaded config");
        } else if (arg.equalsIgnoreCase("tags")) {
            super.sendMessage(sender, GREEN + "Reloaded tags");
            super.plugin.retag();
        } else {
            super.sendMessage(sender, RED + "Error, that option not recognized");
        }
    }
}
