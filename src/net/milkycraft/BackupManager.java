package net.milkycraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.*;

public class BackupManager {

	DateFormat dateFormat;
	File original;
	File backupdir;
	ColoredGroups cg;

	public BackupManager(ColoredGroups cg) {
		this.cg = cg;
		this.original = new File(cg.getDataFolder(), File.separator
				+ "config.yml");
		this.backupdir = new File(cg.getDataFolder(), File.separator
				+ "backups" + File.separator);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd hh_mm");
	}

	public void create(Date date) {
		if (!this.backupdir.exists()) {
			this.backupdir.mkdir();
		}
		File backup = new File(this.backupdir, File.separator + "config_"
				+ dateFormat.format(date) + ".yml");
		try {
			this.copyFile(original, backup);
		} catch (IOException e) {
			cg.log("[Error] IOException occured during backup process");
			e.printStackTrace();
		}
	}

	public void purge() {
		Path dir = Paths.get(this.backupdir.getAbsolutePath());
		try {
			Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return CONTINUE;
				}
				@Override
				public FileVisitResult postVisitDirectory(Path dir,
						IOException exc) throws IOException {
					if (exc == null) {
						Files.delete(dir);
						return CONTINUE;
					}
					throw exc;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			destination.transferFrom(source, 0, source.size());
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
