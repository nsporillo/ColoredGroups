package net.milkycraft;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigSettings extends ConfigLoader {

	public ConfigSettings(ColoredGroups plugin, String fileName) {
		super(plugin, fileName);
		config = plugin.getConfig();
		super.saveIfNotExist();
	}

	@Override
	public void load() {
		// If it doesn't exist, copy it from the .jar
		if (!configFile.exists()) {
			dataFolder.mkdir();
			plugin.saveDefaultConfig();
		}
		super.addDefaults();
		loadKeys();
	}

	@Override
	protected void loadKeys() {
		loadVariables();
	}

	private void loadVariables() {
		ConfigurationSection groups = config.getConfigurationSection("groups");
		for (String keys : groups.getKeys(false)) {
			ConfigurationSection vars = groups.getConfigurationSection(keys);
			plugin.profiles.add(new ChatProfile(vars.getName(), vars
					.getString("Prefix"), vars.getString("Suffix"), vars.getString("Muffix"), vars.getString("Format")));
		}
	}

	public void reload() {
		super.rereadFromDisk();
		load();
		plugin.log("Variables reloaded from disk!");
	}
}
