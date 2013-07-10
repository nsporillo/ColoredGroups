package net.milkycraft;

import org.bukkit.configuration.ConfigurationSection;

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
		this.debug = super.getYaml().getBoolean("options.debug", false);
		this.igs = super.getYaml().getBoolean("options.in-game-set", true);
		this.backup = super.getYaml().getBoolean("options.backup-on-disable",
				true);
		this.cchat = super.getYaml().getBoolean("options.allow-color-codes");
		this.importer = super.getYaml().getBoolean("options.import", false);
		this.override = super.getYaml().getBoolean("options.override", false);
		ConfigurationSection groups = getYaml().getConfigurationSection(
				"groups");
		for (String keys : groups.getKeys(false)) {
			ConfigurationSection vars = groups.getConfigurationSection(keys);
			if (vars.getString("ShownGroup") == null) {
				vars.set("ShownGroup", vars.getName());
				super.saveConfig();
			} 
			if (vars.getString("TagColor") == null) {
				vars.set("TagColor", "&f");
				super.plugin.log("Updated " + vars.getName() + " for TagColor");
				super.saveConfig();
			}
			super.plugin.getChatProfiles().add(
					new ChatProfile(vars.getName(), vars
							.getString("ShownGroup"), vars.getString("Prefix"),
							vars.getString("Suffix"), vars.getString("Muffix"),
							vars.getString("Format"), vars.getString("TagColor")));
		}
	}

	protected void reload() {
		super.rereadFromDisk();
		super.load();
	}

	protected void update() {
		ConfigurationSection options = super.getYaml().getConfigurationSection(
				"options");
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
