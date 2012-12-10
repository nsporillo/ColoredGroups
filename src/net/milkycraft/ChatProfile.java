package net.milkycraft;

import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public final class ChatProfile {

	private final String group;
	private String showngroup;
	private String prefix;
	private String suffix;
	private String muffix;
	private String format;

	protected ChatProfile(String g, String sg, String prefix2, String suffix2,
			String muffix2, String format2) {
		this.group = g;
		this.setShownGroup(sg);
		this.setPrefix(prefix2);
		this.setSuffix(suffix2);
		this.setMuffix(muffix2);
		this.format(format2);
	}

	/**
	 * Gets the prefix
	 * 
	 * @return
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * Gets the suffix
	 * 
	 * @return
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Gets the muffix
	 * 
	 * @return
	 */
	public String getMuffix() {
		return this.muffix;
	}

	/**
	 * Gets the shown group
	 * 
	 * @return
	 */
	public String getShownGroup() {
		return this.showngroup;
	}

	/**
	 * Gets an example of the profile in chat
	 * 
	 * @return
	 */
	public String getExample() {
		return this.format.replace("%1$s", "player").replace("%2$s", "message");
	}

	/**
	 * <p>
	 * Get the Colored format
	 * </p>
	 * <p>
	 * Following code will break
	 * </p>
	 * <code> getFormat("Example %") </code>
	 * <p>
	 * Cannot correct parse %'s in message
	 * <p>
	 * 
	 * @param message
	 * @return
	 */
	public String getFormat(String message) {
		return this.format.replace("%2$s",
				translateAlternateColorCodes('&', message));
	}

	/**
	 * <p>
	 * Get the non-colored-coded format</a>
	 * <p>
	 * Can handle all(?) text
	 * </p>
	 * 
	 * @return
	 */
	public String getFormat() {
		return this.format;
	}

	/**
	 * <p>
	 * Gets the group
	 * </p>
	 * <b>Group is matched with a persons permission group<b>
	 * 
	 * @return ChatProfiles's constant group
	 */
	public String getGroup() {
		return this.group;
	}

	/**
	 * <p>
	 * Set the prefix
	 * </p>
	 * <p>
	 * Uses '&' to denote color
	 * </p>
	 * 
	 * @param newprefix
	 */
	public void setPrefix(String newprefix) {
		this.prefix = translateAlternateColorCodes('&', newprefix);
	}

	/**
	 * <p>
	 * Set the suffix
	 * </p>
	 * <p>
	 * Uses '&' to denote color
	 * </p>
	 * 
	 * @param newsuffix
	 */
	public void setSuffix(String newsuffix) {
		this.suffix = translateAlternateColorCodes('&', newsuffix);
	}

	/**
	 * <p>
	 * Set the message color
	 * </p>
	 * <p>
	 * Uses '&' to denote color
	 * </p>
	 * 
	 * @param newmuffix
	 */
	public void setMuffix(String newmuffix) {
		this.muffix = translateAlternateColorCodes('&', newmuffix);
	}

	/**
	 * <p>
	 * Sets the group name in chat
	 * </p>
	 * <p>
	 * Doesn't affect group matching in permissions layer
	 * <p>
	 * 
	 * @param newgroup
	 */
	public void setShownGroup(String newgroup) {
		this.showngroup = translateAlternateColorCodes('&', newgroup);
	}

	private void format(String mat) {
		this.format = mat
				.replace(mat.substring(0, 1) + "%g",
						getPrefix() + mat.substring(0, 1) + getShownGroup())
				.replace("%p", getSuffix() + "%1$s")
				.replace("%m", getMuffix() + "%2$s")
				.replace("null", WHITE.toString());
	}
}
