package net.porillo;

import org.bukkit.ChatColor;

public class ChatStyle {

    private final String group;
    private final String format;
    private final String shownGroup;
    private final String tagColor;

    public ChatStyle(String... options) {
        this.group = options[0];
        this.format = options[1];
        this.shownGroup = options[2];
        this.tagColor = options[3];
    }

    public String format(boolean colorize, String... vars) {
        String chat = format.replace("%group", shownGroup);
        chat = chat.replace("%world", vars[0]);
        chat = chat.replace("%username", vars[1]);
        if (!colorize) {
            ChatColor.translateAlternateColorCodes('&', chat);
            return chat.replace("%message", vars[2]);
        }
        chat = chat.replace("%message", vars[2]);
        return ChatColor.translateAlternateColorCodes('&', chat);
    }

    public String getExample() {
        return format(true, "world", "player", "message");
    }

    public String getTagColor() {
        return tagColor;
    }

    public String getGroup() {
        return group;
    }
}
