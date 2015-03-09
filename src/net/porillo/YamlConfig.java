package net.porillo;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class YamlConfig extends YamlLoader {

    public boolean debug, importer, cchat, override;

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
        this.cchat = yaml.getBoolean("options.allow-color-codes");
        this.importer = yaml.getBoolean("options.import", false);
        this.override = yaml.getBoolean("options.override", false);
        ConfigurationSection groups = yaml.getConfigurationSection("groups");
        for (String keys : groups.getKeys(false)) {
            ConfigurationSection vars = groups.getConfigurationSection(keys);
            ChatStyle cf = new ChatStyle(vars.getName(), vars.getString("Format"), vars.getString("ShownGroup"), vars.getString("TagColor"));
            plugin.getFormats().add(cf);
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
