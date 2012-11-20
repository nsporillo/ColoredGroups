package net.milkycraft;

import org.bukkit.ChatColor;

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

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public String getMuffix() {
		return this.muffix;
	}

	public String getShownGroup() {
		return this.showngroup;
	}

	public String getExample() {
		return this.format.replace("%1$s", "player").replace("%2$s", "message");
	}

	public String getFormat() {
		return this.format;
	}

	public String getGroup() {
		return this.group;
	}

	public void setPrefix(String newprefix) {
		this.prefix = ChatColor.translateAlternateColorCodes('&', newprefix);
	}

	public void setSuffix(String newsuffix) {
		this.suffix = ChatColor.translateAlternateColorCodes('&', newsuffix);
	}

	public void setMuffix(String newmuffix) {
		this.muffix = ChatColor.translateAlternateColorCodes('&', newmuffix);
	}

	public void setName(String newname) {
		this.showngroup = newname;
	}

	private void setShownGroup(String newgroup) {
		this.showngroup = ChatColor.translateAlternateColorCodes('&', newgroup);
	}

	private void format(String mat) {
		this.format = mat
				.replace(mat.substring(0, 1) + "%g",
						getPrefix() + mat.substring(0, 1) + getShownGroup())
				.replace("%p", getSuffix() + "%1$s")
				.replace("%m", getMuffix() + "%2$s")
				.replace("null", ChatColor.WHITE.toString());
	}
}
