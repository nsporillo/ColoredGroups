package net.milkycraft.addons;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.milkycraft.ColoredGroups;

public class AddonLoader {

	protected ColoredGroups cg;
	private ClassLoader loader;

	public AddonLoader(ColoredGroups cg) {
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
			cg.log("ClassLoader encountered an exception: ");
			ex.printStackTrace();
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
				Addon a = (Addon) object;		
				a.enable();
				addons.add(a);
				cg.log("Loaded " + a.getName() + " v"
						+ a.getVersion() + " by "
						+ a.getAuthors().get(0));
			} catch (Exception ex) {
				cg.log("A " + ex.getLocalizedMessage() + " caused " + name + " to fail to load!");
			} catch (Error ex) {
				cg.log("A " + ex.getLocalizedMessage() + " caused " + name + " to fail to load!");
			}
		}
		return addons;
	}
}
