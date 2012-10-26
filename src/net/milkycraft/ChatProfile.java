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

	public final ChatColor getPrefix() {
		return ChatColor.getByChar(prefix);
	}

	public final ChatColor getSuffix() {
		return ChatColor.getByChar(suffix);
	}
	
	public final ChatColor getMuffix() {
		return ChatColor.getByChar(this.muffix);
	}
	public final String getGroup() {
		return g;
	}

}
