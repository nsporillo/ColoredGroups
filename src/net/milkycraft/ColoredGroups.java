package net.milkycraft;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.krinsoft.privileges.Privileges;
import net.milkycraft.addons.Addon;
import net.milkycraft.addons.AddonLoader;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.imp.Permissions;

public class ColoredGroups extends JavaPlugin {

	protected List<ChatProfile> profiles = new ArrayList<ChatProfile>();
	private List<Addon> addons = new ArrayList<Addon>();
	protected ConfigSettings conf;	
	private Privileges priv;
	private PermissionsEx pex;
	private GroupManager gm;
	private PermissionsPlugin pb;
	private Permissions bp;
	private boolean tried = false;
	

	@Override
	public void onEnable() {
		conf = new ConfigSettings(this, "config.yml");
		conf.load();
		hook();
		getServer().getPluginManager().registerEvents(new ChatHandler(this),
				this);
		getCommand("coloredgroups").setExecutor(new Commands(this));
		addons.addAll(new AddonLoader(this).load(this.getDataFolder() + File.separator + "Addons"));
		log("Loaded " + profiles.size() + " groups from config");
	}

	@Override
	public void onDisable() {
		this.profiles.clear();
		this.profiles = null;
	}

	private void hook() {
		Plugin pri = getServer().getPluginManager().getPlugin("Privileges");
		Plugin pex = getServer().getPluginManager().getPlugin("PermissionsEx");
		Plugin gm = getServer().getPluginManager().getPlugin("GroupManager");
		Plugin bp = getServer().getPluginManager().getPlugin("bPermissions");
		Plugin pb = getServer().getPluginManager().getPlugin(
				"PermissionsBukkit");
		if (pri != null && pri.isEnabled()) {
			this.priv = (Privileges) pri;
			log("Hooked " + pri.getDescription().getName() + " v"
					+ pri.getDescription().getVersion() + " by "
					+ pri.getDescription().getAuthors().get(0));
			return;
		} else if (pex != null && pex.isEnabled()) {
			this.pex = (PermissionsEx) pex;
			log("Hooked " + pex.getDescription().getName() + " v"
					+ pex.getDescription().getVersion() + " by "
					+ pex.getDescription().getAuthors().get(0));
			return;
		} else if (gm != null && gm.isEnabled()) {
			this.gm = (GroupManager) gm;
			log("Hooked " + gm.getDescription().getName() + " v"
					+ gm.getDescription().getVersion() + " by "
					+ gm.getDescription().getAuthors().get(0));
			return;
		} else if (bp != null && bp.isEnabled()) {
			this.bp = (Permissions) bp;
			log("Hooked " + bp.getDescription().getName() + " v"
					+ bp.getDescription().getVersion() + " by "
					+ bp.getDescription().getAuthors().get(0));
			return;
		} else if (pb != null && pb.isEnabled()) {
			this.pb = (PermissionsPlugin) pb;
			log("Hooked " + pb.getDescription().getName() + " v"
					+ pb.getDescription().getVersion() + " by "
					+ pb.getDescription().getAuthors().get(0));
			return;
		} else {
			tryAgain();
		}
	}

	private void unhook() {
		this.priv = null;
		this.pex = null;
		this.bp = null;
		this.pb = null;
		this.gm = null;
		tried = false;
		log("Unhooked from permissions plugins");
	}

	private void tryAgain() {
		if (tried == false) {
			tried = true;
		} else {
			log("Failed to hook into a permissions plugin");
			return;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				hook();
			}
		}, 1L);
	}

	/**
	 * Gets a players group name for chat
	 * 
	 * @param player
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getGroup(Player player) {
		if (this.priv != null) {
			return this.priv.getGroupManager().getGroup(player).getName();
		} else if (this.pex != null) {
			PermissionGroup[] groups = PermissionsEx.getUser(player).getGroups(
					player.getWorld().getName());
			return groups[0].getName();
		} else if (this.gm != null) {
			return this.gm.getWorldsHolder().getWorldPermissions(player)
					.getGroup(player.getName());
		} else if (this.pb != null) {
			return this.pb.getGroups(player.getName()).get(0).getName();
		} else if (this.bp != null) {
			String[] groups = ApiLayer.getGroups(player.getWorld().getName(),
					CalculableType.USER, player.getName());
			return groups[0];
		}
		return "Unknown";
	}

	/**
	 * Log stuff to console using our prefix
	 * 
	 * @param log
	 */
	public void log(String log) {
		this.getLogger().info(log);
	}

	/**
	 * Reloads configuration values
	 */
	public void reload() {
		this.profiles.clear();
		this.conf.reload();
	}

	/**
	 * Unhooks and then rehooks into permissions plugins Sort of a clarification
	 * method..
	 */
	public void rehook() {
		this.unhook();
		this.hook();
	}
	
	public void reloadAddons() {
		this.addons.clear();
		this.addons.addAll(new AddonLoader(this).load(this.getDataFolder() + File.separator + "Addons"));
		this.log("Reloaded the following addons:");
		for(Addon a : this.addons) {
			this.log(a.getName() + " v" + a.getVersion() + " by " + a.getAuthors().get(0));
		}
	}

	/**
	 * Gets you an example of what that chat profile will look like
	 * 
	 * @param group
	 * @return
	 */
	public String getTestMessage(String group) {
		for (ChatProfile c : this.profiles) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return c.getExample();
			}
		}
		return ChatColor.RED + "No groups match that name";
	}

	/**
	 * Returns instance of Privileges' main class
	 * 
	 * @return
	 */
	public Privileges getPrivileges() {
		return this.priv;
	}

	/**
	 * Returns instance of PermissionsEx's main class
	 * 
	 * @return
	 */
	public PermissionsEx getPermissionsEx() {
		return this.pex;
	}

	/**
	 * Returns instance of PermissionsBukkit's main class
	 * 
	 * @return
	 */
	public PermissionsPlugin getPermissionsBukkit() {
		return this.pb;
	}

	/**
	 * Gets the list of chat profiles Doesnt let you add any because creating a
	 * chat profile shouldn't work
	 * 
	 * @return
	 */
	public List<ChatProfile> getChatProfiles() {
		return this.profiles;
	}
	
	/**
	 * Gets a list of all installed addons
	 * @return
	 */
	public List<Addon> getAddons() {
		return this.addons;
	}
	
	/**
	 * Creates a temporary chat profile Has sync block because profiles might be
	 * iterated though while this new profile is being added Redundant profiles
	 * only decrease performance of plugin
	 * 
	 * @param group
	 * @param prefix
	 * @param suffix
	 * @param muffix
	 * @param format
	 */
	public final void createTempChatProfile(final String group,
			final String prefix, final String suffix, final String muffix,
			final String format) {
		synchronized (this) {
			profiles.add(new ChatProfile(group, prefix, suffix, muffix, format));
		}
	}

	/**
	 * Creates a new group in config and reloads
	 * 
	 * @param group
	 * @param prefix
	 * @param suffix
	 * @param muffix
	 * @param format
	 */
	public final void createChatProfile(final String group,
			final String prefix, final String suffix, String muffix,
			String format) {
		this.conf.createNewGroup(group, prefix, suffix, muffix, format);
	}

	/**
	 * Thread-safely removes chat profile from cache
	 * 
	 * @param group
	 */
	public final void deleteTempChatProfile(final String group) {
		synchronized (this) {
			Iterator<ChatProfile> it = this.profiles.iterator();
			while (it.hasNext()) {
				if (it.next().getGroup().equalsIgnoreCase(group)) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Deletes a chat profile from config and thread-safely removes it from the
	 * cache
	 * 
	 * @param group
	 */
	public final void deleteChatProfile(final String group) {
		this.conf.deleteGroup(group);
		synchronized (this) {
			Iterator<ChatProfile> it = this.profiles.iterator();
			while (it.hasNext()) {
				if (it.next().getGroup().equalsIgnoreCase(group)) {
					it.remove();
				}
			}
		}
	}
}
