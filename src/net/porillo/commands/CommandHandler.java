package net.porillo.commands;

import net.porillo.ColoredGroups;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class CommandHandler {
    private Map<String, Command> cmds = new HashMap<String, Command>();

    public CommandHandler(ColoredGroups plugin) {
        cmds.put("reload", new ReloadCommand(plugin));
        cmds.put("import", new ImportCommand(plugin));
        cmds.put("create", new CreateCommand(plugin));
        cmds.put("test", new TestCommand(plugin));
    }

    public void runCommand(CommandSender s, String l, String[] a) {
        if (a.length == 0 || this.cmds.get(a[0].toLowerCase()) == null) {
            this.showHelp(s, l);
            return;
        }
        List<String> args = new ArrayList<String>(Arrays.asList(a));
        Command cmd = this.cmds.get(args.remove(0).toLowerCase());
        if (args.size() < cmd.getRequiredArgs()) {
            cmd.showHelp(s, l);
            return;
        }
        cmd.runCommand(s, args);
    }

    public void showHelp(CommandSender s, String l) {
        s.sendMessage(ChatColor.BLUE + "------ " + ChatColor.GOLD + "ColoredGroups " + ChatColor.BLUE + "------");
        for (Command cmd : this.cmds.values())
            if (cmd.checkPermission(s)) cmd.showHelp(s, l);
    }
}
