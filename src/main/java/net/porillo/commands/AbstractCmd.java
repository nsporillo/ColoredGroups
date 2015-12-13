package net.porillo.commands;

import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public abstract class AbstractCmd implements Command {

    protected ColoredGroups plugin;
    private String name;
    private String permission;
    private int required = 0;
    private List<String> usages = new ArrayList<String>();

    public AbstractCmd(ColoredGroups cg) {
        this.plugin = cg;
    }

    @Override
    public void showHelp(CommandSender sender, String label) {
        for (final String usage : this.usages) {
            sender.sendMessage(GRAY + String.format("%1$-" + 10 + "s", label) + translateAlternateColorCodes('&', usage));
        }
    }

    @Override
    public boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    @Override
    public int getRequiredArgs() {
        return this.required;
    }

    public void setRequiredArgs(final int req) {
        this.required = req;
    }

    public void noPermission(final CommandSender sender) {
        sender.sendMessage(RED + "You don't have permission to run that command.");
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPermission(final String perm) {
        this.permission = perm;
    }

    public void addUsage(String desc, String... uses) {
        final StringBuilder usage = new StringBuilder().append(BLUE).append(String.format("%1$-" + 8 + "s", this.name));
        boolean color = true;
        for (String use : uses) {
            if (color)
                usage.append(YELLOW);
            else
                usage.append(AQUA);
            color = !color;
            usage.append(String.format("%1$-" + 8 + "s", use));
        }
        usage.append(GREEN);
        usage.append(desc);
        this.usages.add(usage.toString());
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(AQUA + "[CG] " + message);
    }
}
