package net.porillo.command;


import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class ImportCmd extends AbstractCmd {


    public ImportCmd(ColoredGroups cg) {
        super(cg);
        super.setName("import");
        super.setPermission("coloredgroups.import");
        super.addUsage("Import groups from your permissions plugin");
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }

        try {
            super.plugin.runImport(true);
        } catch (Exception ex) {
            sender.sendMessage(RED + "Error: " + ex.getMessage());
            return;
        }
        super.sendMessage(sender, GREEN + "Imported groups using vault!");
    }
}
