package net.milkycraft;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listener to handle chat,Chat cant be handled in main class due to hooking
 * bugs
 */
public class ChatHandler implements Listener {

	ColoredGroups cg;
	public ChatHandler(ColoredGroups cg) {
		this.cg = cg;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent e) {
		final String group = cg.getGroup(e.getPlayer());
		for (ChatProfile c : cg.profiles) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				e.setFormat(c.getFormat());
				break;
			}
		}
		if (e.getFormat() == "<%1$s> %2$s") {
			e.setFormat(ChatColor.GRAY + "[" + group + "]" + "%s:"
					+ ChatColor.WHITE + " %s");
		}
	}
	
}
