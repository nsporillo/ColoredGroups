package net.milkycraft;

import org.bukkit.ChatColor;

public final class ChatProfile {

	private String g;
	private String sg;
	private String prefix;
	private String suffix;
	private String muffix;
	private String format;

	protected ChatProfile(final String g, final String sg,
			final String prefix2, final String suffix2, final String muffix2,
			final String format2) {
		this.g = g;
		this.sg = sg;
		this.prefix = prefix2;
		prefix = prefix.replace("&", "");
		this.suffix = suffix2;
		suffix = suffix.replace("&", "");
		this.muffix = muffix2;
		muffix = muffix.replace("&", "");
		this.format = format2;
		format();
	}

	public final ChatColor getPrefix() {
		return ChatColor.getByChar(this.prefix);
	}

	public final ChatColor getSuffix() {
		return ChatColor.getByChar(this.suffix);
	}

	public final ChatColor getMuffix() {
		return ChatColor.getByChar(this.muffix);
	}

	public String getGroup() {
		return this.g;
	}

	public String getShownGroup() {
		return this.sg;
	}

	public void setShownGroup(final String newgroup) {
		this.sg = newgroup;
	}

	public final String getFormat() {
		return this.format;
	}

	private void format() {
		this.format = this.format
				.replace(format.substring(0, 1) + "%g",
						getPrefix() + format.substring(0, 1) + getShownGroup())
				.replace("%p", getSuffix() + "%s")
				.replace("%m", getMuffix() + "%s")
				.replace("null", ChatColor.WHITE.toString());
	}

	public String getExample() {
		return this.format.replace("%s", "test");
	}
}
