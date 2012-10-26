package net.milkycraft;

import java.io.File;
import java.io.IOException;

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