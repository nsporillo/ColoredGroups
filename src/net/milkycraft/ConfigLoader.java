package net.milkycraft;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigLoader {

	protected String fileName;
	protected File configFile;
	protected File dataFolder;
	protected ColoredGroups plugin;
	protected static FileConfiguration config;

	public ConfigLoader(ColoredGroups plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.dataFolder = plugin.getDataFolder();
		this.configFile = new File(this.dataFolder, File.separator + fileName);
		config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	/**
	 * Load this config file.
	 */
	public void load() {
		if (!this.configFile.exists()) {
			this.dataFolder.mkdir();
			saveConfig();
		}
		addDefaults();
		loadKeys();
		this.saveIfNotExist();
	}

	/**
	 * Save this config file.
	 */
	protected void saveConfig() {
		try {
			config.save(this.configFile);
		} catch (IOException ex) {
		}
	}

	/**
	 * Save if not exist.
	 */
	protected void saveIfNotExist() {
		if (!this.configFile.exists()) {
			if (this.plugin.getResource(this.fileName) != null) {
				this.plugin.saveResource(this.fileName, false);
			}
		}
		rereadFromDisk();
	}

	/**
	 * Reread from disk.
	 */
	protected void rereadFromDisk() {
		config = YamlConfiguration.loadConfiguration(this.configFile);
	}
	
	/**
	 * Api method to create a new config section for a group
	 * @throws UnsupportedOperationException if group already exists in config
	 * @param group
	 * @param prefix
	 * @param suffix
	 * @param muffix
	 * @param format
	 */
	protected void createNewGroup(String group, String prefix, String suffix, String muffix, String format) {
		ConfigurationSection groups = config.getConfigurationSection("groups");
		if(groups.contains(group)) {
			throw new UnsupportedOperationException("Cannot create duplicate group in config!");
		}
		groups.createSection(group);
		ConfigurationSection keys = groups.getConfigurationSection(group);
		keys.set("Prefix", prefix);
		keys.set("Suffix", suffix);
		keys.set("Muffix", muffix);
		keys.set("Format", format);
		this.saveConfig();
		plugin.reload();
	}
	
	/**
	 * Api method to delete a group from config
	 * @throws NullPointerException if the group doesn't exist
	 * @param group
	 */
	protected void deleteGroup(String group) {
		ConfigurationSection groups = config.getConfigurationSection("groups");
		if(groups.contains(group)) {
			groups.set(group, null);
		} else {
			throw new NullPointerException("Group does not exist");
		}
		this.saveConfig();
		plugin.reload();
	}

	/**
	 * Add the defaults to this config file.
	 */
	protected void addDefaults() {
		config.options().copyDefaults(true);
		saveConfig();
	}

	/**
	 * Load the keys from this config file.
	 */
	protected abstract void loadKeys();

}