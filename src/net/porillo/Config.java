package net.porillo;

import org.bukkit.configuration.ConfigurationSection;

public final class Config {

    private ColoredGroups plugin;
    public boolean debug, importer, cchat, override;

    public Config(ColoredGroups plugin) {
        this.plugin = plugin;
        this.load();
    }

    protected void reload() {
        plugin.reloadConfig();
        plugin.getFormats().clear();
        this.load();
    }

    private void load() {
        plugin.saveDefaultConfig();
        this.debug = plugin.getConfig().getBoolean("options.debug", false);
        this.cchat = plugin.getConfig().getBoolean("options.allow-color-codes");
        this.importer = plugin.getConfig().getBoolean("options.import", false);
        this.override = plugin.getConfig().getBoolean("options.override", false);
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        for (String keys : groups.getKeys(false)) {
            ConfigurationSection vars = groups.getConfigurationSection(keys);
            ChatStyle cf = new ChatStyle(vars.getName(), vars.getString("Format"), vars.getString("ShownGroup"), vars.getString("TagColor"));
            plugin.getFormats().add(cf);
        }
    }


    public void set(String section, String key, Object value) {
        plugin.getConfig().getConfigurationSection(section).set(key, value);
        plugin.saveConfig();
    }

    public void createNewGroup(String group) {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        if (groups.contains(group)) throw new UnsupportedOperationException("Cannot create duplicate group in config!");
        groups.createSection(group);
        plugin.saveConfig();
    }

    public void editGroup(String group, String key, String value) {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        if (groups.contains(group)) {
            ConfigurationSection keys = groups.getConfigurationSection(group);
            keys.set(key, value);
            plugin.saveConfig();
        }
    }

    public void deleteGroup(final String group) {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        if (groups.contains(group)) groups.set(group, null);
        else throw new NullPointerException("Group does not exist");
        plugin.saveConfig();
    }
}
