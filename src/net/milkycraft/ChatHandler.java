package net.milkycraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

	private ColoredGroups cg;

	public ChatHandler(ColoredGroups cg) {
		this.cg = cg;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent e) {
		final String group = cg.getGroup(e.getPlayer());
		for (ChatProfile c : cg.getChatProfiles()) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				if (canColorize(e.getPlayer())) {
					if (e.getMessage().contains("%")) {
						e.setFormat(c.getFormat());
						return;
					}
					e.setFormat(c.getFormat(e.getMessage()));
					return;
				}
				e.setFormat(c.getFormat());
				return;
			}
		}
		if (e.getFormat().equals("<%1$s> %2$s")) {
			e.setFormat("[" + group + "]" + "%1$s: %2$s");
		}
	}

	private boolean canColorize(Player sender) {
		return cg.getConfiguration().cchat ? true : sender
				.hasPermission("coloredgroups.coloredchat");
	}
}
