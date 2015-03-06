package net.porillo;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class VaultConnector extends Chat {

    private final String name = "ColoredGroups";
    private Plugin plugin;
    private ColoredGroups cg;

    public VaultConnector(Plugin plugin, Permission perms) {
        super(perms);
        this.plugin = plugin;
        if (cg == null) {
            this.cg = (ColoredGroups) plugin.getServer().getPluginManager().getPlugin(name);
        }
        Bukkit.getServer().getPluginManager().registerEvents(new PermissionServerListener(), plugin);
    }

    public class PermissionServerListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (cg == null) {
                cg = (ColoredGroups) plugin.getServer().getPluginManager().getPlugin(name);
            }
        }
    }

    @Override
    public boolean getGroupInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
        return false;
    }

    @Override
    public double getGroupInfoDouble(String arg0, String arg1, String arg2, double arg3) {
        return 0;
    }

    @Override
    public int getGroupInfoInteger(String arg0, String arg1, String arg2, int arg3) {
        return 0;
    }

    @Override
    public String getGroupInfoString(String arg0, String arg1, String arg2, String arg3) {
        return null;
    }

    @Override
    public String getGroupPrefix(String world, String name) {
        String group = cg.getGroup(world, name);
        for(ChatProfile cp : cg.getChatProfiles()) {
            if(cp.getGroup().equals(group)) {
                return cp.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getGroupSuffix(String world, String name) {
        String group = cg.getGroup(world, name);
        for(ChatProfile cp : cg.getChatProfiles()) {
            if(cp.getGroup().equals(group)) {
                return cp.getSuffix();
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getPlayerInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
        return false;
    }

    @Override
    public double getPlayerInfoDouble(String arg0, String arg1, String arg2, double arg3) {
        return 0;
    }

    @Override
    public int getPlayerInfoInteger(String arg0, String arg1, String arg2, int arg3) {
        return 0;
    }

    @Override
    public String getPlayerInfoString(String arg0, String arg1, String arg2, String arg3) {
        return null;
    }

    @Override
    public String getPlayerPrefix(String arg0, String arg1) {
        return null;
    }

    @Override
    public String getPlayerSuffix(String arg0, String arg1) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public void setGroupInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
    }

    @Override
    public void setGroupInfoDouble(String arg0, String arg1, String arg2, double arg3) {
    }

    @Override
    public void setGroupInfoInteger(String arg0, String arg1, String arg2, int arg3) {
    }

    @Override
    public void setGroupInfoString(String arg0, String arg1, String arg2, String arg3) {
    }

    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        cg.getConfiguration().editGroup(group, "Prefix", prefix);
    }

    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        cg.getConfiguration().editGroup(group, "Suffix", suffix);
    }

    @Override
    public void setPlayerInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
    }

    @Override
    public void setPlayerInfoDouble(String arg0, String arg1, String arg2, double arg3) {
    }

    @Override
    public void setPlayerInfoInteger(String arg0, String arg1, String arg2, int arg3) {
    }

    @Override
    public void setPlayerInfoString(String arg0, String arg1, String arg2, String arg3) {
    }

    @Override
    public void setPlayerPrefix(String arg0, String arg1, String arg2) {
    }

    @Override
    public void setPlayerSuffix(String arg0, String arg1, String arg2) {
    }

}
