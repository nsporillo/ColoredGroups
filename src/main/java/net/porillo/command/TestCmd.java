package net.porillo.command;


import net.porillo.ChatStyle;
import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestCmd extends AbstractCmd {

    public TestCmd(ColoredGroups cg) {
        super(cg);
        super.setName("test");
        super.setPermission("coloredgroups.test");
        super.setRequiredArgs(1);
        super.addUsage("Test what chat will look like", "all");
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }
        String arg = args.get(0);

        if (arg.equalsIgnoreCase("all")) {
            for (ChatStyle c : super.plugin.getFormats()) {
                sender.sendMessage(c.getExample());
            }
        } else {
            sender.sendMessage(super.plugin.getTestMessage(arg));
        }
    }
}
