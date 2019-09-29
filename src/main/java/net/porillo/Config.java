package net.porillo;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

@Data
public final class Config {

    private final ColoredGroups plugin;
    private Map<String, String> worldAliases = new HashMap<>();
    public boolean debug, importer, cchat, override;

    Config(ColoredGroups plugin) {
        this.plugin = plugin;
        this.load();
    }

    void reload() {
        plugin.reloadConfig();
        plugin.getChatStyleMap().clear();
        this.load();
    }

    private void load() {
        plugin.saveDefaultConfig();
        this.debug = plugin.getConfig().getBoolean("options.debug", false);
        this.cchat = plugin.getConfig().getBoolean("options.allow-color-codes");
        this.importer = plugin.getConfig().getBoolean("options.import", false);
        this.override = plugin.getConfig().getBoolean("options.override", false);

        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        ConfigurationSection variables = plugin.getConfig().getConfigurationSection("variables");

        for (String keys : groups.getKeys(false)) {
            ConfigurationSection vars = groups.getConfigurationSection(keys);
            ChatStyle chatStyle = new ChatStyle(
                    vars.getName(),
                    vars.getString("format"),
                    vars.getString("shown-group"),
                    vars.getString("tag-color"));

            for (String var : variables.getKeys(false)) {
                ConfigurationSection v = variables.getConfigurationSection(var);
                chatStyle.addVariable(new ChatVariable(var, v.getString("replace")));
            }

            plugin.getChatStyleMap().put(vars.getName(), chatStyle);
        }

        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("worlds");

        if (worlds == null) {
            worlds = plugin.getConfig().createSection("worlds");

            for (World world : Bukkit.getWorlds()) {
                worlds.set(world.getName(), world.getName());
            }
        }

        for (String world : worlds.getKeys(false)) {
            worldAliases.put(world, worlds.getString(world));
        }
    }

    public void set(String section, String key, Object value) {
        plugin.getConfig().getConfigurationSection(section).set(key, value);
        plugin.saveConfig();
    }

    public void createNewGroup(String group) throws UnsupportedOperationException {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");

        if (existsGroup(group)) {
            throw new UnsupportedOperationException("Group '" + group + "' is already in config!");
        }

        ConfigurationSection keys = groups.createSection(group);
        keys.set("format", "[%group]%username:&f %message");
        keys.set("shown-group", group);
        keys.set("tag-color", "&f");
        plugin.saveConfig();
    }

    public void editGroup(String group, String key, String value) throws UnsupportedOperationException {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        if (existsGroup(group)) {
            ConfigurationSection keys = groups.getConfigurationSection(group);
            keys.set(key, value);
            plugin.saveConfig();
        } else {
            throw new UnsupportedOperationException("Group '" + group + "' does not exist");
        }
    }

    void deleteGroup(final String group) throws NullPointerException {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");

        if (existsGroup(group)) {
            groups.set(group, null);
        } else {
            throw new UnsupportedOperationException("Group '" + group + "' does not exist");
        }

        plugin.saveConfig();
    }

    boolean existsGroup(String group) {
        return plugin.getConfig().getConfigurationSection("groups").getKeys(false).contains(group);
    }
}
