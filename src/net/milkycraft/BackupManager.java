package net.milkycraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

public class BackupManager {
	private File original;
	private File backupdir;
	private ColoredGroups cg;
	private Random r = new Random();

	protected BackupManager(ColoredGroups cg) {
		this.cg = cg;
		this.original = new File(cg.getDataFolder(), File.separator
				+ "config.yml");
		this.backupdir = new File(cg.getDataFolder(), File.separator
				+ "backups" + File.separator);
	}

	/**
	 * Create a config backup
	 * @param bounds what the Random next int uses 
	 */
	public void create(int bounds) {
		int i = r.nextInt(bounds);
		if (!this.backupdir.exists()) {
			this.backupdir.mkdir();
		}
		File backup = new File(this.backupdir, File.separator + "config_" + i
				+ ".yml");
		try {
			this.copyFile(original, backup);
		} catch (Exception e) {
			cg.warn("Exception occured during backup process");
		}
	}

	/**
	 * Purges backup directory
	 */
	public void purge() {
		for (File f : this.backupdir.listFiles()) {
			f.delete();
		}
		this.backupdir.delete();
	}

	@SuppressWarnings("resource")
	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			cg.debug("Transfered "
					+ destination.transferFrom(source, 0, source.size())
					+ " bytes of " + source.size() + " total");
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
