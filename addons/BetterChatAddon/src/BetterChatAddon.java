import java.util.ArrayList;
import java.util.List;

import net.milkycraft.ChatHandler;
import net.milkycraft.ChatProfile;
import net.milkycraft.addons.Addon;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BetterChatAddon extends Addon implements Listener {

	private String version = "1.0";
	private String name = "BetterChatAddon";
	private String description = "Adds world to chat";
	private List<String> authors = new ArrayList<String>();
	
	@Override
	public void enable() {
		authors.add("milkywayz");
		try {
			/*Disable the default chat listener*/
			AsyncPlayerChatEvent.getHandlerList().unregister(new ChatHandler(super.plugin));
			/*Enable the new chat listener */
			super.plugin.getServer().getPluginManager().registerEvents(this, super.plugin);
		} catch (Exception exe) {
			System.out.println("Enabling of " + this.getClass().getSimpleName()
					+ " has failed due to " + exe.getLocalizedMessage());
		}
	}
	
	@Override
	public void disable() {
		/*Register our default chat listener again*/
		super.plugin.getServer().getPluginManager().registerEvents(new ChatHandler(super.plugin), super.plugin);
		/*Disable the one we made in this addon*/
		AsyncPlayerChatEvent.getHandlerList().unregister(this);
	}

	@Override
	public List<String> getAuthors() {
		return authors;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		final String group = super.plugin.getGroup(e.getPlayer());
		String world = e.getPlayer().getWorld().getName()
				.replace("world_the_end", "The_End")
				.replace("world_nether", "Nether").replace("w", "W");
		for (ChatProfile c : super.plugin.getChatProfiles()) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				e.setFormat(ChatColor.YELLOW + "[" + world + "]"
						+ ChatColor.RESET + c.getFormat());
				break;
			}
		}
		if (e.getFormat() == "<%1$s> %2$s") {
			e.setFormat(ChatColor.GRAY + "[" + group + "]" + "%s:"
					+ ChatColor.WHITE + " %s");
		}
	}
}
