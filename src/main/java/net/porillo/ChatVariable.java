package net.porillo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
class ChatVariable {

    private String root, replace, permission;

    ChatVariable(String root, String replace) {
        this(root, replace, "coloredgroups.variables." + root.replace("%", ""));
    }

    String run(Player player) {
        return player.hasPermission(permission) ? replace : "";
    }
}
