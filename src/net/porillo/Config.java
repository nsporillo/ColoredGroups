package net.porillo;

import org.bukkit.configuration.ConfigurationSection;

public final class Config {

    private final ColoredGroups plugin;
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
            ChatStyle cf = new ChatStyle(vars.getName(), vars.getString("format"), vars.getString("shown-group"), vars.getString("tag-color"));
            plugin.getFormats().add(cf);
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
        } else throw new UnsupportedOperationException("Group '" + group + "' does not exist");
    }

    public void deleteGroup(final String group) throws NullPointerException {
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("groups");
        if (existsGroup(group)) groups.set(group, null);
        else throw new UnsupportedOperationException("Group '" + group + "' does not exist");
        plugin.saveConfig();
    }

    public boolean existsGroup(String group) {
        return plugin.getConfig().getConfigurationSection("groups").getKeys(false).contains(group);
    }
}
