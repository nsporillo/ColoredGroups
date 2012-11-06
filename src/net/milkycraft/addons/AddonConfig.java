package net.milkycraft.addons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import net.milkycraft.ColoredGroups;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AddonConfig {

	private String fileName;
	private File configFile;
	private File dataFolder;
	private ColoredGroups plugin;
	private FileConfiguration config;
	private Addon a;
	private BufferedInputStream in = null;
	private FileOutputStream fout = null;
	private final int BYTES = 1024;

	public AddonConfig(final ColoredGroups plugin, final Addon a,
			final String filename) {
		this.a = a;
		this.plugin = plugin;
		this.fileName = filename;
		this.dataFolder = plugin.getDataFolder();
		this.configFile = new File(this.dataFolder, File.separator + "addons"
				+ File.separator + a.getName() + File.separator + fileName);
		config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	public void load() {
		if (!this.configFile.exists()) {
			this.dataFolder.mkdir();
			saveConfig();
		}
		this.saveIfNotExist();
	}

	public void saveConfig() {
		try {
			config.save(this.configFile);
		} catch (IOException ex) {
			plugin.log("Task of saving " + a.getName() + "'s config failed");
		}
	}

	public void saveIfNotExist() {
		if (!this.configFile.exists()) {
			if (this.plugin.getResource(this.fileName) != null) {
				this.plugin.saveResource(this.fileName, false);
			}
		}
		rereadFromDisk();
	}

	public void rereadFromDisk() {
		config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	public void delete() {
		try {
			this.configFile.delete();
			config = null;
		} catch (Exception ex) {
			plugin.log("Deleting " + a.getName() + "'s config has failed!");
		}
	}

	public String getName() {
		return this.configFile.getName();
	}

	public void installConfigFromUrl(String url) throws IOException {
		this.load();
		if (configFile.length() != 0) {
			return;
		}
		if (!url.substring(url.length() - 3).equals("yml")) {
			// Only download ymls, this code is meant to be used legitimately
			plugin.log(a.getName() + " tried to download a non yml file!");
			return;
		}
		download(new URL(url));
	}

	private void download(URL url) throws IOException {
		plugin.log(a, "Downloading "
				+ format(url.openConnection().getContentLength())
				+ " config from " + url);
		in = new BufferedInputStream(url.openStream());
		fout = new FileOutputStream(configFile);
		byte[] data = new byte[BYTES];
		int count;
		while ((count = in.read(data, 0, BYTES)) != -1) {
			fout.write(data, 0, count);
		}
		in.close();
		fout.flush();
		fout.close();
	}

	public static String format(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "b", "kb", "mb" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1024, digitGroups))
				+ "" + units[digitGroups];
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public Addon getAddon() {
		return this.a;
	}
}
