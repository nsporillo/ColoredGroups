package net.porillo.listener;

import net.porillo.ChatStyle;
import net.porillo.ColoredGroups;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class ChatListener implements Listener {

    private final ColoredGroups cg;

    public ChatListener(ColoredGroups cg) {
        this.cg = cg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        final String group = cg.getGroup(player);
        ChatStyle chatStyle = getStyle(group);
        if (chatStyle != null) {
            e.setFormat("%2$s"); // set format so only our msg is displayed
            String msg = chatStyle.format(
                    canColor(player),
                    mapAlias(player.getWorld()),
                    player.getName(),
                    player.getDisplayName(),
                    e.getMessage());
            e.setMessage(msg);
        } else {
            // fall back to a default format
            e.setFormat("[" + group + "]" + "%1$s: %2$s");
        }
    }

    private ChatStyle getStyle(String group) {
        final String lowerGroup = group.toLowerCase();
        if (cg.getChatStyleMap().containsKey(group)) {
            return cg.getChatStyleMap().get(group);
        } else if (cg.getChatStyleMap().containsKey(lowerGroup)) {
            return cg.getChatStyleMap().get(lowerGroup);
        }

        return null;
    }

    private boolean canColor(Player sender) {
        return cg.getConfiguration().cchat || sender.hasPermission("coloredgroups.coloredchat");
    }

    private String mapAlias(World world) {
        Map<String, String> aliases = cg.getConfiguration().getWorldAliases();
        return aliases.containsKey(world.getName()) ? aliases.get(world.getName()) : world.getName();
    }
}
