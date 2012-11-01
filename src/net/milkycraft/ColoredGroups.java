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

public final class ColoredGroups extends JavaPlugin {

	private List<ChatProfile> profiles = new ArrayList<ChatProfile>();
	private List<Addon> addons = new ArrayList<Addon>();
	private ConfigSettings conf;	
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
		addons.addAll(new AddonLoader(this).load(this.getDataFolder() + File.separator + "addons"));
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
			h(priv);
			return;
		} else if (pex != null && pex.isEnabled()) {
			this.pex = (PermissionsEx) pex;
			h(pex);
			return;
		} else if (gm != null && gm.isEnabled()) {
			this.gm = (GroupManager) gm;
			h(gm);
			return;
		} else if (bp != null && bp.isEnabled()) {
			this.bp = (Permissions) bp;
			h(bp);
			return;
		} else if (pb != null && pb.isEnabled()) {
			this.pb = (PermissionsPlugin) pb;
			h(pb);
			return;
		} else {
			tryAgain();
		}
	}

	private void h(Plugin p) {
		log("Hooked " + p.getDescription().getName() + " v"
				+ p.getDescription().getVersion() + " by "
				+ p.getDescription().getAuthors().get(0));	
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

	public void reloadAddons() {
		Iterator<Addon> it = this.getAddons().iterator();
		while(it.hasNext()) {
			it.next().disable();
			it.remove();
		}
		this.addons.addAll(new AddonLoader(this).load(this.getDataFolder() + File.separator + "addons"));
		for(Addon a : this.addons) {
			a.enable();
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

	public String getTestMessage(String group) {
		for (ChatProfile c : this.profiles) {
			if (c.getGroup().equalsIgnoreCase(group)) {
				return c.getExample();
			}
		}
		return ChatColor.RED + "No groups match that name";
	}

	public List<ChatProfile> getChatProfiles() {
		return this.profiles;
	}

	public List<Addon> getAddons() {
		return this.addons;
	}
	
	
	public void createTempChatProfile(final String group, final String showngroup,
			final String prefix, final String suffix, final String muffix,
			final String format) {
		synchronized (this) {
			profiles.add(new ChatProfile(group, showngroup, prefix, suffix, muffix, format));
		}
	}

	public void createChatProfile(final String group,
			final String prefix, final String suffix, String muffix,
			String format) {
		this.conf.createNewGroup(group, prefix, suffix, muffix, format);
	}

	public void deleteTempChatProfile(final String group) {
		synchronized (this) {
			Iterator<ChatProfile> it = this.profiles.iterator();
			while (it.hasNext()) {
				if (it.next().getGroup().equalsIgnoreCase(group)) {
					it.remove();
				}
			}
		}
	}

	public void deleteChatProfile(final String group) {
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
