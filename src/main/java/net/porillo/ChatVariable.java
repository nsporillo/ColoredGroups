package net.porillo;

import org.bukkit.entity.Player;

public class ChatVariable {

    private String root, replace, permission;

    public ChatVariable(String root, String replace) {
        this.root = root;
        this.replace = replace;
        this.permission = "coloredgroups.variables." + root.replace("%", "");
    }

    public String getRoot() {
        return root;
    }

    public String run(Player player) {
        return player.hasPermission(permission) ? replace : "";
    }
}
