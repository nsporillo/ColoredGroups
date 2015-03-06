package net.porillo;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class YamlConfig extends YamlLoader {

    public boolean debug, igs, backup, importer, cchat, override;

    public YamlConfig(ColoredGroups plugin, String fileName) {
        super(plugin, fileName);
        super.saveIfNotExist();
        super.load();
    }

    @Override
    protected void loadKeys() {
        this.update();
        FileConfiguration yaml = super.getYaml();
        this.debug = yaml.getBoolean("options.debug", false);
        this.igs = yaml.getBoolean("options.in-game-set", true);
        this.backup = yaml.getBoolean("options.backup-on-disable", true);
        this.cchat = yaml.getBoolean("options.allow-color-codes");
        this.importer = yaml.getBoolean("options.import", false);
        this.override = yaml.getBoolean("options.override", false);
        ConfigurationSection groups = yaml.getConfigurationSection("groups");
        for (String keys : groups.getKeys(false)) {
            ConfigurationSection vars = groups.getConfigurationSection(keys);
            if (vars.getString("ShownGroup") == null) {
                vars.set("ShownGroup", vars.getName());
                super.saveConfig();
            }
            if (vars.getString("Color") == null) {
                vars.set("Color", "&f");
                super.saveConfig();
            }
            super.plugin.getChatProfiles().add(
                    new ChatProfile(vars.getName(), vars.getString("ShownGroup"), vars
                            .getString("Prefix"), vars.getString("Suffix"), vars
                            .getString("Muffix"), vars.getString("Format"), vars
                            .getString("Color")));
        }
    }

    protected void reload() {
        super.rereadFromDisk();
        super.load();
    }

    protected void update() {
        ConfigurationSection options = super.getYaml().getConfigurationSection("options");
        if (options != null) {
            if (options.get("debug") == null) {
                this.set("options", "debug", false);
            }
            if (options.get("import") == null) {
                this.set("options", "import", false);
            }
            if (options.get("in-game-set") == null) {
                this.set("options", "in-game-set", true);
            }
            if (options.get("backup-on-disable") == null) {
                this.set("options", "backup-on-disable", true);
            }
            if (options.get("allow-color-codes") == null) {
                this.set("options", "allow-color-codes", false);
            }
            if (options.get("override") == null) {
                this.set("options", "override", false);
            }
        } else {
            super.getYaml().createSection("options");
            super.saveConfig();
            update();
        }
        super.saveConfig();
    }

    protected void set(String section, String key, Object value) {
        super.getYaml().getConfigurationSection(section).set(key, value);
        super.saveConfig();
    }
}
