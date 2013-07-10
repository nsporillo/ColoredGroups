package net.milkycraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TagListener implements Listener {

	private ColoredGroups cg;
	public TagListener(ColoredGroups cg) {
		this.cg = cg;
	}
	
	@EventHandler
	public void onPing(org.kitteh.tag.PlayerReceiveNameTagEvent e) {
		final Player p = e.getNamedPlayer();
		final String group = cg.getGroup(p);
		for (ChatProfile c : cg.getChatProfiles()) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				String tahg = c.getTagColor() + p.getDisplayName();
				if (tahg.length() < 16) {
					e.setTag(tahg);
					return;
				}			
				String tag = c.getTagColor() + p.getName();
				if (tag.length() < 16) {
					e.setTag(tag);
				} else {
					e.setTag(c.getTagColor() + p.getName().substring(0, 14));
				}
			}
		}
	}
}
