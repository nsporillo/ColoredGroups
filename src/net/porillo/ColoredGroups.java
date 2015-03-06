package net.porillo;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.ChatColor.RED;

public class ColoredGroups extends JavaPlugin {

    private List<ChatProfile> profiles = new ArrayList<ChatProfile>(5);
    private ImportationWorker importer;
    private BackupManager backup;
    private YamlConfig conf;
    private Permission perms;

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

    private Permission getPerms() {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager()
                    .getRegistration(Permission.class);
            return rsp.getProvider();
        }
        return null;
    }

    private void afterEnable() {
        getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
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
                log();
            }
        }, 1L);
    }


    private void log() {
        Plugin p = Bukkit.getPluginManager().getPlugin(perms.getName());
        if (p != null && p.isEnabled()) {
            this.log("Hooked " + p.getDescription().getName() + " v" + p.getDescription().getVersion()
                    + " by " + p.getDescription().getAuthors().get(0));
        }
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

    public String getGroup(final Player p) {
        return perms.getPrimaryGroup(p.getWorld().getName(), p);
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
