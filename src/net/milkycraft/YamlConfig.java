package net.milkycraft;

import org.bukkit.configuration.ConfigurationSection;

public final class YamlConfig extends YamlLoader {

	public YamlConfig(ColoredGroups plugin, String fileName) {
		super(plugin, fileName);
		super.saveIfNotExist();
	}

	@Override
	protected void loadKeys() {
		ConfigurationSection groups = config.getConfigurationSection("groups");
		for (String keys : groups.getKeys(false)) {
			ConfigurationSection vars = groups.getConfigurationSection(keys);
			super.plugin.getChatProfiles().add(new ChatProfile(vars.getName(), vars
					.getName(), vars.getString("Prefix"), vars
					.getString("Suffix"), vars.getString("Muffix"), vars
					.getString("Format")));
		}
	}

	protected void reload() {
		super.rereadFromDisk();
		super.load();
		super.plugin.log("Variables reloaded from disk!");
	}
}
