package net.milkycraft;

import java.util.HashSet;
import java.util.Set;

import net.krinsoft.privileges.Privileges;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.imp.Permissions;

public class ColoredGroups extends JavaPlugin implements Listener {

	private Privileges priv;
	private PermissionsEx pex;
	private GroupManager gm;
	private PermissionsPlugin pb;
	private Permissions bp;
	protected ConfigSettings conf;
	protected Set<ChatProfile> profiles = new HashSet<ChatProfile>();

	@Override
	public void onEnable() {
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {
					@Override
					public void run() {
						hook();
					}
				}, 4L);
		conf = new ConfigSettings(this, "config.yml");
		conf.load();
		getServer().getPluginManager().registerEvents(new ChatHandler(this),
				this);
		getCommand("coloredgroups").setExecutor(new Commands(this));
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
			log("Found " + pri.getDescription().getName() + " v"
					+ pri.getDescription().getVersion() + " by "
					+ pri.getDescription().getAuthors().get(0));
			return;
		} else if (pex != null && pex.isEnabled()) {
			this.pex = (PermissionsEx) pex;
			log("Found " + pex.getDescription().getName() + " v"
					+ pex.getDescription().getVersion() + " by "
					+ pex.getDescription().getAuthors().get(0));
			return;
		} else if (gm != null && gm.isEnabled()) {
			this.gm = (GroupManager) gm;
			log("Found " + gm.getDescription().getName() + " v"
					+ gm.getDescription().getVersion() + " by "
					+ gm.getDescription().getAuthors().get(0));
			return;
		} else if (bp != null && bp.isEnabled()) {
			this.bp = (Permissions) bp;
			log("Found " + bp.getDescription().getName() + " v"
					+ bp.getDescription().getVersion() + " by "
					+ bp.getDescription().getAuthors().get(0));
			return;
		} else if (pb != null && pb.isEnabled()) {
			this.pb = (PermissionsPlugin) pb;
			log("Found " + pb.getDescription().getName() + " v"
					+ pb.getDescription().getVersion() + " by "
					+ pb.getDescription().getAuthors().get(0));
			return;
		} else {
			log("A supported permissions plugin is needed for full functionality");
		}
	}

	private void unhook() {
		this.priv = null;
		this.pex = null;
		this.bp = null;
		this.pb = null;
		this.gm = null;
		log("Unhooked from permissions plugins");
	}

	/*
	 * Below are Internally and possible externally accessed methods
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

	public void log(String log) {
		this.getLogger().info(log);
	}

	public void reload() {
		this.profiles.clear();
		this.conf.reload();
	}

	public void rehook() {
		this.unhook();
		this.hook();
	}

	public Privileges getPrivileges() {
		return this.priv;
	}

	public PermissionsEx getPermissionsEx() {
		return this.pex;
	}

	public Set<ChatProfile> getChatProfiles() {
		return this.profiles;
	}

}
