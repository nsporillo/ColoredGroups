package net.milkycraft;

import org.bukkit.ChatColor;

public final class ChatProfile {

	private String group;
	private String showngroup;
	private String prefix;
	private String suffix;
	private String muffix;
	private String format;

	protected ChatProfile(String g, String sg, String prefix2, String suffix2,
			String muffix2, String format2) {
		this.group = g;
		this.setShownGroup(sg);
		this.prefix = prefix2.replace("&", "");
		this.suffix = suffix2.replace("&", "");
		this.muffix = muffix2.replace("&", "");
		this.format(format2);
	}

	public ChatColor getPrefix() {
		return ChatColor.getByChar(this.prefix);
	}

	public ChatColor getSuffix() {
		return ChatColor.getByChar(this.suffix);
	}

	public ChatColor getMuffix() {
		return ChatColor.getByChar(this.muffix);
	}

	public String getGroup() {
		return this.group;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatProfile other = (ChatProfile) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (muffix == null) {
			if (other.muffix != null)
				return false;
		} else if (!muffix.equals(other.muffix))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (showngroup == null) {
			if (other.showngroup != null)
				return false;
		} else if (!showngroup.equals(other.showngroup))
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatProfile [group=" + group + ", showngroup=" + showngroup
				+ ", prefix=" + prefix + ", suffix=" + suffix + ", muffix="
				+ muffix + ", format=" + ChatColor.translateAlternateColorCodes('&', format) + "]";
	}


}
