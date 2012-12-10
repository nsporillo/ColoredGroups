package net.milkycraft;

import static de.bananaco.bpermissions.api.ApiLayer.getGroups;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.List;

import net.krinsoft.privileges.Privileges;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.imp.Permissions;

public final class ColoredGroups extends JavaPlugin {

	private List<ChatProfile> profiles;
	private ImportationWorker importer;
	private BackupManager backup;
	private YamlConfig conf;
	private Privileges priv;
	private PermissionsEx pex;
	private GroupManager gm;
	private PermissionsPlugin pb;
	private Permissions bp;

	@Override
	public void onEnable() {
		this.profiles = new ArrayList<ChatProfile>();
		this.conf = new YamlConfig(this, "config.yml");
		this.conf.load();
		this.afterEnable();
		Bukkit.getPluginManager().registerEvents(new ChatHandler(this), this);
		this.getCommand("coloredgroups").setExecutor(new Commands(this));
		this.backup = new BackupManager(this);
		this.importer = new ImportationWorker(this);
		this.runImport(false);
	}

	@Override
	public void onDisable() {
		this.profiles.clear();
		if (this.getConfiguration().backup) {
			this.getBackupManager().create(50);
		}
	}

	private void afterEnable() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				hook();
				ride_();
			}
		}, 1L);
	}

	private void ride_() {
		if (this.getConfiguration().override) {
			for (RegisteredListener rl : AsyncPlayerChatEvent.getHandlerList()
					.getRegisteredListeners()) {
				if (rl.getListener().getClass().getSimpleName()
						.equals("ChatHandler")) {
					continue;
				}
				AsyncPlayerChatEvent.getHandlerList().unregister(
						rl.getListener());
				this.log("Overrided " + rl.getPlugin().getName()
						+ "'s chat listening");
			}
		}
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
			this.log(priv);
			return;
		} else if (pex != null && pex.isEnabled()) {
			this.pex = (PermissionsEx) pex;
			this.log(pex);
			return;
		} else if (gm != null && gm.isEnabled()) {
			this.gm = (GroupManager) gm;
			this.log(gm);
			return;
		} else if (bp != null && bp.isEnabled()) {
			this.bp = (Permissions) bp;
			this.log(bp);
			return;
		} else if (pb != null && pb.isEnabled()) {
			this.pb = (PermissionsPlugin) pb;
			this.log(pb);
			return;
		} else {
			this.warn("Could not find a supported permissions plugin");
		}
	}

	private void log(Plugin p) {
		this.log("Hooked " + p.getDescription().getName() + " v"
				+ p.getDescription().getVersion() + " by "
				+ p.getDescription().getAuthors().get(0));
	}

	/**
	 * Logs information to console
	 * 
	 * @param log
	 */
	public void log(String log) {
		this.getLogger().info(log);
	}

	/**
	 * Sends a debug message if debugging enabled
	 * 
	 * @param debug
	 */
	public void debug(String debug) {
		if (this.getConfiguration().debug) {
			this.getLogger().info("[Debug] " + debug);
		}
	}

	/**
	 * <p>
	 * Runs a import
	 * </p>
	 * <p>
	 * If override is true then run will disregard config
	 * </p>
	 * 
	 * @param override
	 */
	public void runImport(boolean override) {
		this.importer.run(override);
	}

	/**
	 * Display warning to console
	 * 
	 * @param string
	 */
	public void warn(String string) {
		this.getLogger().warning(string);
	}

	/**
	 * Clear profiles and reload from config
	 */
	public void reload() {
		this.profiles.clear();
		this.conf.reload();
	}

	/**
	 * Unhooks from plugins then hook again
	 */
	public void rehook() {
		this.unhook();
		this.hook();
	}

	private void unhook() {
		this.priv = null;
		this.pex = null;
		this.bp = null;
		this.pb = null;
		this.gm = null;
		this.log("Unhooked from permissions plugins");
	}

	/**
	 * <p>
	 * Gets players group from permissions
	 * <p>
	 * <p>
	 * Called alot! However caching would probably cause more issues then not,
	 * since chat executing is in another thread and this check is fairly fast
	 * depending on the perms system used
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getGroup(final Player player) {
		final String name = player.getName();
		if (this.priv != null) {
			return this.priv.getGroupManager().getGroup(player).getName();
		} else if (this.pex != null) {
			PermissionGroup[] groups = PermissionsEx.getUser(player).getGroups(
					player.getWorld().getName());
			return groups[0].getName();
		} else if (this.gm != null) {
			return this.gm.getWorldsHolder().getWorldPermissions(player)
					.getGroup(name);
		} else if (this.pb != null) {
			return this.pb.getGroups(name).get(0).getName();
		} else if (this.bp != null) {
			String[] groups = getGroups(player.getWorld().getName(),
					CalculableType.USER, name);
			return groups[0];
		}
		return "Unknown";
	}

	/**
	 * Gets a test message for a group
	 * 
	 * @param group
	 * @return Example of group chatting
	 */
	public String getTestMessage(String group) {
		for (ChatProfile c : this.profiles) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return c.getExample();
			}
		}
		return RED + "No groups match that name";
	}

	/**
	 * Checks if string represents a valid group
	 * 
	 * @param group
	 * @return true if is valid, false if not valid
	 */
	public boolean isGroup(String group) {
		for (ChatProfile c : this.getChatProfiles()) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public List<ChatProfile> getChatProfiles() {
		return this.profiles;
	}

	/**
	 * Gets the YamlConfig instance
	 * 
	 * @return ColoredGroups config
	 */
	public YamlConfig getConfiguration() {
		return this.conf;
	}

	/**
	 * Gets the backup manager
	 * 
	 * @return BackupManager instance
	 */
	public BackupManager getBackupManager() {
		return this.backup;
	}
}
