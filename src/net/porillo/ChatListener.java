package net.porillo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private ColoredGroups cg;

    public ChatListener(ColoredGroups cg) {
        this.cg = cg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        final String group = cg.getGroup(player);
        for(ChatStyle cf : cg.getFormats()) {
            if(cf.getGroup().equals(group)) {
                e.setFormat("%2$s"); // set format so only our msg is displayed
                String msg = cf.format(canColorize(player), player.getWorld().getName(), player.getName(), e.getMessage());
                e.setMessage(msg);
                return;
            }
        }
        if (e.getFormat().equals("<%1$s> %2$s")) {
            e.setFormat("[" + group + "]" + "%1$s: %2$s");
        }
    }

    private boolean canColorize(Player sender) {
        return cg.getConfiguration().cchat || sender.hasPermission("coloredgroups.coloredchat");
    }
}
