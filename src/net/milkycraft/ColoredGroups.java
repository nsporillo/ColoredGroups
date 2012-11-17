package net.milkycraft;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private static ColoredGroups instance;
	private YamlConfig conf;
	private Privileges priv;
	private PermissionsEx pex;
	private GroupManager gm;
	private PermissionsPlugin pb;
	private Permissions bp;

	@Override
	public void onEnable() {
		instance = this;
		this.conf = new YamlConfig(this, "config.yml");
		this.conf.load();
		this.hook_();
		Bukkit.getPluginManager().registerEvents(new ChatHandler(this), this);
		this.getCommand("coloredgroups").setExecutor(new Commands(this));
		this.addons.addAll(new AddonLoader(this).load(this.getDataFolder()
				+ File.separator + "addons"));
	}

	@Override
	public void onDisable() {
		for (Addon a : this.getAddons()) {
			a.disable();
		}
		this.profiles = null;
		this.addons = null;
		instance = null;
	}

	private void hook_() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				hook();
			}
		}, 1L);
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
			this.log("Could not find a supported permissions plugin");
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

	public void log(Addon a, String log) {
		this.getLogger().info("[" + a.getName() + "] " + log);
	}

	public void reload() {
		this.profiles.clear();
		this.conf.reload();
	}

	public void reloadAddons() {
		Iterator<Addon> it = this.getAddons().iterator();
		while (it.hasNext()) {
			it.next().disable();
			it.remove();
		}
		this.addons.addAll(new AddonLoader(this).load(this.getDataFolder()
				+ File.separator + "addons"));
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
		log("Unhooked from permissions plugins");
	}

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
			String[] groups = ApiLayer.getGroups(player.getWorld().getName(),
					CalculableType.USER, name);
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

	public static ColoredGroups getPlugin() {
		return ColoredGroups.instance;
	}

	public List<ChatColor> getColors() {
		return Arrays.asList(ChatColor.values());
	}

	public void createTempChatProfile(final String group,
			final String showngroup, final String prefix, final String suffix,
			final String muffix, final String format) {
		profiles.add(new ChatProfile(group, showngroup, prefix, suffix, muffix,
				format));
	}

	public void createChatProfile(final String group, final String prefix,
			final String suffix, final String muffix, final String format) {
		this.conf.createNewGroup(group, prefix, suffix, muffix, format);
	}

	public void deleteTempChatProfile(final String group) {
		synchronized (this.profiles) {
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
		synchronized (this.profiles) {
			Iterator<ChatProfile> it = this.profiles.iterator();
			while (it.hasNext()) {
				if (it.next().getGroup().equalsIgnoreCase(group)) {
					it.remove();
				}
			}
		}
	}
	
	public void modifyChatProfile(final String group, Map<String, String> modifiers) {
		this.conf.modifyGroup(group, modifiers);
	}
}
