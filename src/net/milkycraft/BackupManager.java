package net.milkycraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupManager {

	private File backupdir;
	private ColoredGroups cg;

	protected BackupManager(ColoredGroups cg) {
		this.cg = cg;
		this.backupdir = new File(cg.getDataFolder(), File.separator + "backups"
				+ File.separator);
	}

	/**
	 * Create a config backup
	 * 
	 * @param limit
	 *            max iterations to check for config
	 */
	public void create(int limit) {
		if (!this.backupdir.exists()) {
			this.backupdir.mkdir();
		}
		boolean completed = false;
		for (int i = 0; i < limit; i++) {
			File backup = new File(this.backupdir, File.separator + "config_" + i
					+ ".yml");
			if (!backup.exists()) {
				try {
					this.copyFile(new File(cg.getDataFolder(), File.separator
							+ "config.yml"), backup);
				} catch (Exception e) {
					cg.warn("Exception occured during backup process");
				}
				completed = true;
				cg.log("Backup " + i + " of config was saved!");
				break;
			}
		}
		if(!completed) {
			this.purge();
			this.create(limit);
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

	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			cg.debug("Transfered " + destination.transferFrom(source, 0, source.size())
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
