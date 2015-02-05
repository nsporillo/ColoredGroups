package net.milkycraft;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;
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
import org.kitteh.tag.TagAPI;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.imp.Permissions;

@SuppressWarnings("deprecation")
public class ColoredGroups extends JavaPlugin {

	private List<ChatProfile> profiles = new ArrayList<ChatProfile>(5);
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
		this.conf = new YamlConfig(this, "config.yml");
		this.afterEnable();
		getPluginManager().registerEvents(new ChatHandler(this), this);
		if (getPluginManager().getPlugin("TagAPI") != null) {
			getPluginManager().registerEvents(new TagListener(this), this);
		}
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
		getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				hook();
				if (ColoredGroups.this.getConfiguration().override) {
					for (RegisteredListener rl : AsyncPlayerChatEvent.getHandlerList()
							.getRegisteredListeners()) {
						if (rl.getListener().getClass().getSimpleName().equals("ChatHandler")) {
							continue;
						}
						AsyncPlayerChatEvent.getHandlerList().unregister(rl.getListener());
						ColoredGroups.this.warn("Overrode " + rl.getPlugin().getName() + "'s chat listening");
					}
				}
			}
		}, 1L);
	}

	void hook() {
		Plugin pri = getServer().getPluginManager().getPlugin("Privileges");
		Plugin pex = getServer().getPluginManager().getPlugin("PermissionsEx");
		Plugin gm = getServer().getPluginManager().getPlugin("GroupManager");
		Plugin bp = getServer().getPluginManager().getPlugin("bPermissions");
		Plugin pb = getServer().getPluginManager().getPlugin("PermissionsBukkit");
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
		this.log("Hooked " + p.getDescription().getName() + " v" + p.getDescription().getVersion()
				+ " by " + p.getDescription().getAuthors().get(0));
	}

	public void log(String log) {
		this.getLogger().info(log);
	}

	public void debug(String debug) {
		if (this.getConfiguration().debug) {
			this.getLogger().info("[Debug] " + debug);
		}
	}

	public void runImport(boolean override) {
		this.importer.run(override);
	}

	public void warn(String string) {
		this.getLogger().warning(string);
	}

	public void reload() {
		this.profiles.clear();
		this.conf.reload();
	}

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

	public String getGroup(final Player p) {
		final String name = p.getName();
		final String world = p.getWorld().getName();
		if (this.priv != null) {
			return this.priv.getGroupManager().getGroup(p).getName();
		} else if (this.pex != null) {
			String[] g = PermissionsEx.getPermissionManager().getUser(p).getGroupsNames(world);
			return g[0];
		} else if (this.gm != null) {
			return this.gm.getWorldsHolder().getWorldPermissions(p).getGroup(name);
		} else if (this.pb != null) {
			return this.pb.getGroups(name).get(0).getName();
		} else if (this.bp != null) {			
			String[] groups = ApiLayer.getGroups(world, CalculableType.USER, name);
			return groups[0];
		}
		return "Unknown";
	}

	public void retag() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			TagAPI.refreshPlayer(p);
		}
	}

	public String getTestMessage(String group) {
		for (ChatProfile c : this.profiles) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return c.getExample();
			}
		}
		return RED + "No groups match that name";
	}

	public boolean isGroup(String group) {
		for (ChatProfile c : this.getChatProfiles()) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return true;
			}
		}
		return false;
	}

	public List<ChatProfile> getChatProfiles() {
		return profiles;
	}

	public YamlConfig getConfiguration() {
		return this.conf;
	}

	public BackupManager getBackupManager() {
		return this.backup;
	}
}
