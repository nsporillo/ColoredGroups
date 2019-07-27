package net.porillo;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import net.porillo.command.CmdHandler;
import net.porillo.listener.ChatListener;
import net.porillo.listener.TagListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.*;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.event.player.AsyncPlayerChatEvent.getHandlerList;
import static org.kitteh.tag.TagAPI.refreshPlayer;

public class ColoredGroups extends JavaPlugin {

    @Getter private final List<ChatStyle> formats = new ArrayList<ChatStyle>();
    @Getter private Config configuration;
    private CmdHandler handler;
    private VaultImporter importer;
    private Permission perms;

    @Override
    public void onEnable() {
        this.configuration = new Config(this);
        this.perms = getPerms();

        if (this.perms == null) {
            super.getLogger().severe("Vault not found. ColoredGroups disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.afterEnable();
        getPluginManager().registerEvents(new ChatListener(this), this);

        if (getPluginManager().getPlugin("TagAPI") != null) {
            getPluginManager().registerEvents(new TagListener(this), this);
        }

        this.handler = new CmdHandler(this);
        this.importer = new VaultImporter(this);
        this.runImport(false);

        Metrics metrics = new Metrics(this);
    }

    @Override
    public void onDisable() {
        this.formats.clear();
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        handler.runCommand(s, l, a);
        return true;
    }

    private Permission getPerms() {
        Plugin vault = getPluginManager().getPlugin("Vault");

        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Permission> rsp = getServicesManager().getRegistration(Permission.class);
            if (rsp != null) {
                return rsp.getProvider();
            }
        }

        return null;
    }

    private void afterEnable() {
        getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if (ColoredGroups.this.getConfiguration().override)
                    for (RegisteredListener rl : getHandlerList().getRegisteredListeners()) {
                        if (rl.getListener().getClass().getSimpleName().equals("ChatListener"))
                            continue;

                        getHandlerList().unregister(rl.getListener());
                        ColoredGroups.this.debug("Overrode " + rl.getPlugin().getName() + "'s chat listening");
                    }
            }
        }, 1L);
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
        this.formats.clear();
        this.configuration.reload();
    }

    public String getGroup(final Player p) {
        return getGroup(p.getWorld().getName(), p.getName());
    }

    @SuppressWarnings("deprecation")
    private String getGroup(String world, String name) {
        return perms.getPrimaryGroup(world, name);
    }

    public void retag() {
        for (Player p : getOnlinePlayers()) {
            refreshPlayer(p);
        }
    }

    public String getTestMessage(String group) {
        for (ChatStyle c : this.formats) {
            if (c.getGroup().equalsIgnoreCase(group)) {
                return c.getExample();
            }
        }

        return RED + "No groups match that name";
    }
}
