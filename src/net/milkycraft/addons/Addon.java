package net.milkycraft.addons;

import java.util.List;

import net.milkycraft.ColoredGroups;

/**
 * 
 * @author milky
 *
 */
public abstract class Addon{
	public ColoredGroups plugin = ColoredGroups.getPlugin();
	public abstract void enable();
	public abstract void disable();
	public abstract String getVersion();
	public abstract String getName();
	public abstract String getDescription();
	public abstract List<String> getAuthors();
	
	public AddonConfig getConfig() {
		return null;
	}
}
