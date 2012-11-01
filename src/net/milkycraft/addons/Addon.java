package net.milkycraft.addons;

import java.util.List;

/**
 * Interface to tag all addons
 * @author milky
 *
 */
public interface Addon {
	public String getVersion();
	public String getName();
	public String getDescription();
	public List<String> getAuthors();
}