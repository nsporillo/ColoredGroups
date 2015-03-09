package net.porillo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TagListener implements Listener {

    private ColoredGroups cg;

    public TagListener(ColoredGroups cg) {
        this.cg = cg;
    }

    @EventHandler
    public void onPing(AsyncPlayerReceiveNameTagEvent e) {
        final Player p = e.getNamedPlayer();
        final String group = cg.getGroup(p);
        for (ChatStyle c : cg.getFormats()) {
            if (c.getGroup().equalsIgnoreCase(group)) {
                String tahg = c.getTagColor() + p.getDisplayName();
                if (tahg.length() < 16) {
                    e.setTag(tahg);
                    return;
                }
                String tag = c.getTagColor() + p.getName();
                e.setTag(tag.length() < 16 ? tag : c.getTagColor() + p.getName().substring(0, 14));
            }
        }
    }
}
