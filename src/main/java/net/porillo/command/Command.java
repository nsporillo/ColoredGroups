package net.porillo.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    public void runCommand(CommandSender sender, List<String> args);

    public void showHelp(CommandSender sender, String label);

    public boolean checkPermission(CommandSender sender);

    public int getRequiredArgs();
}
