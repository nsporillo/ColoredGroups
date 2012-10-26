package net.milkycraft;

import org.bukkit.ChatColor;

public class ChatProfile {

	private String g;
	private String prefix;
	private String suffix;
	private String muffix;

	public ChatProfile(String g, String prefix2, String suffix2, String muffix2) {
		this.g = g;
		this.prefix = prefix2;
		prefix = prefix.replace("&", "");
		this.suffix = suffix2;
		suffix = suffix.replace("&", "");
		this.muffix = muffix2;	
		muffix = muffix.replace("&", "");
	}

	public ChatColor getPrefix() {
		return ChatColor.getByChar(prefix);
	}

	public ChatColor getSuffix() {
		return ChatColor.getByChar(suffix);
	}
	
	public ChatColor getMuffix() {
		return ChatColor.getByChar(this.muffix);
	}
	public String getGroup() {
		return g;
	}

}
