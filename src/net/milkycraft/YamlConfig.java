package net.milkycraft;

import org.bukkit.configuration.ConfigurationSection;

public final class YamlConfig extends YamlLoader {

	public boolean debug, igs, backup;

	public YamlConfig(ColoredGroups plugin, String fileName) {
		super(plugin, fileName);
		super.saveIfNotExist();
	}

	@Override
	protected void loadKeys() {
		update();
		this.debug = super.config.getBoolean("options.debug");
		this.igs = super.config.getBoolean("options.in-game-set");
		this.backup = super.config.getBoolean("options.backup-on-disable");
		ConfigurationSection groups = config.getConfigurationSection("groups");
		for (String keys : groups.getKeys(false)) {
			ConfigurationSection vars = groups.getConfigurationSection(keys);
			if (vars.getString("ShownGroup") == null) {
				vars.set("ShownGroup", vars.getName());
				super.saveConfig();
			}
			super.plugin.getChatProfiles().add(
					new ChatProfile(vars.getName(), vars
							.getString("ShownGroup"), vars.getString("Prefix"),
							vars.getString("Suffix"), vars.getString("Muffix"),
							vars.getString("Format")));
		}
	}

	protected void reload() {
		super.rereadFromDisk();
		super.load();
		super.plugin.log("Variables reloaded from disk!");
	}

	protected void update() {
		if (config.get("options.debug") == null) {
			ConfigurationSection options = config.createSection("options");
			options.set("debug", false);
			options.set("in-game-set", true);
			options.set("backup-on-disable", true);
			saveConfig();
		}
	}
}
