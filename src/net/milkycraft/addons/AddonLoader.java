package net.milkycraft.addons;

import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import net.milkycraft.ColoredGroups;

public class AddonLoader {

	protected ColoredGroups cg;
	private ClassLoader loader;

	public AddonLoader(ColoredGroups cg) {
		this.loader = null;
		this.cg = cg;
	}

	/**
	 * Loads addons Based off of the listener loader in Votifier All credit
	 * where credit is due
	 * 
	 * @param directory
	 * @return
	 */
	public List<Addon> load(String directory) {
		List<Addon> addons = new ArrayList<Addon>();
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			loader = new URLClassLoader(new URL[] { dir.toURI().toURL() },
					Addon.class.getClassLoader());
		} catch (MalformedURLException ex) {
			cg.log("Error with class loader");
			return addons;
		}
		for (File file : dir.listFiles()) {
			if (!file.getName().endsWith(".class")) {
				continue; 
			}
			String name = file.getName().substring(0,
					file.getName().lastIndexOf("."));
			try {
				Class<?> aclass = loader.loadClass(name);
				Object object = aclass.newInstance();
				if (!(object instanceof Addon)) {
					cg.log("Not a valid add-on: " + aclass.getSimpleName());
					continue;
				}
				Addon addon = (Addon) object;
				addons.add(addon);
				cg.log("Loaded " + addon.getName() + " v"
						+ addon.getVersion() + " by "
						+ addon.getAuthors().get(0));
			} catch (Exception ex) {
				cg.log("Failed to load " + name + " : "
						+ ex.getLocalizedMessage());
			} catch (Error ex) {
				cg.log("Failed to load " + name + " : "
						+ ex.getLocalizedMessage());
			}
		}
		return addons;
	}

}
