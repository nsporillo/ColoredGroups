package net.porillo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ChatStyle {

    private List<ChatVariable> chatVariables = new ArrayList<>();
    private final String group;
    private final String format;
    private final String shownGroup;
    private final String tagColor;

    public String format(boolean colorize, String... vars) {
        String chat = format.replace("%group", shownGroup);
        chat = chat.replace("%world", vars[0]);
        chat = chat.replace("%username", vars[1]);
        chat = chat.replace("%displayname", vars[2]);

        if (!colorize) {
            chat = ChatColor.translateAlternateColorCodes('&', chat);
            return chat.replace("%message", vars[3]);
        }

        chat = chat.replace("%message", vars[3]);
        final Player player = Bukkit.getPlayer(vars[1]);

        if (player != null && player.isOnline()) {
            for (ChatVariable cv : chatVariables) {
                chat = chat.replace(cv.getRoot(), cv.run(player));
            }
        }

        return ChatColor.translateAlternateColorCodes('&', chat);
    }

    public String getExample() {
        return format(true, "world", "player", "player", "message");
    }

    void addVariable(ChatVariable cv) {
        this.chatVariables.add(cv);
    }
}
