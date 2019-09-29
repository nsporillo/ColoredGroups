package net.porillo.listener;

import net.porillo.ChatStyle;
import net.porillo.ColoredGroups;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TagListener implements Listener {

    private final ColoredGroups cg;

    public TagListener(ColoredGroups cg) {
        this.cg = cg;
    }

    @EventHandler
    public void onPing(AsyncPlayerReceiveNameTagEvent e) {
        final Player p = e.getNamedPlayer();
        final String group = cg.getGroup(p);
        if (cg.getChatStyleMap().containsKey(group)) {
            ChatStyle chatStyle = cg.getChatStyleMap().get(group);
            String tahg = chatStyle.getTagColor() + p.getDisplayName();

            if (tahg.length() < 16) {
                e.setTag(tahg);
                return;
            }

            String tag = chatStyle.getTagColor() + p.getName();
            e.setTag(tag.length() < 16 ? tag : chatStyle.getTagColor() + p.getName().substring(0, 14));
        }
    }
}
