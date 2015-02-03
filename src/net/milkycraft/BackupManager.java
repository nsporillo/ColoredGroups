package net.milkycraft;

import static java.io.File.*;

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
		this.backupdir = new File(cg.getDataFolder(), separator + "backups" + separator);
	}

	/**
	 * Create a config backup
	 * 
	 * @param limit
	 *            max number of backups
	 */
	public void create(int limit) {
		if (!this.backupdir.exists()) {
			this.backupdir.mkdir();
		}
		boolean completed = false;
		for (int i = 0; i < limit; i++) {
			File backup = new File(this.backupdir, separator + "config_" + i + ".yml");
			if (!backup.exists()) {
				try {
					this.copyFile(new File(cg.getDataFolder(), separator + "config.yml"), backup);
				} catch (Exception e) {
					cg.warn("Exception occured during backup process");
				}
				completed = true;
				cg.log("Backup " + i + " of config was saved!");
				break;
			}
		}
		if (!completed) {
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

	private void copyFile(File src, File dst) throws IOException {
		if (!dst.exists()) {
			dst.createNewFile();
		}
		FileChannel schan = null;
		FileChannel dchan = null;
		try {
			schan = new FileInputStream(src).getChannel();
			dchan = new FileOutputStream(dst).getChannel();
			cg.debug("Copied " + dchan.transferFrom(schan, 0, schan.size()) + "/" + schan.size()
					+ " bytes");
		} finally {
			if (schan != null) {
				schan.close();
			}
			if (dchan != null) {
				dchan.close();
			}
		}
	}
}
