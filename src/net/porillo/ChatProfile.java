package net.porillo;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public final class ChatProfile {

    private final String group;
    private String showngroup;
    private String prefix;
    private String suffix;
    private String muffix;
    private String format;
    private ChatColor tagColor;

    public ChatProfile(String... a) {
        this.group = a[0];
        this.tagColor = ChatColor.getByChar(a[6].replace("&", ""));
        this.setShownGroup(a[1]);
        this.setPrefix(a[2]);
        this.setSuffix(a[3]);
        this.setMuffix(a[4]);
        this.format(a[5]);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String newprefix) {
        this.prefix = translateAlternateColorCodes('&', newprefix);
    }

    public ChatColor getTagColor() {
        return tagColor;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String newsuffix) {
        this.suffix = translateAlternateColorCodes('&', newsuffix);
    }

    public String getMuffix() {
        return this.muffix;
    }

    public void setMuffix(String newmuffix) {
        this.muffix = translateAlternateColorCodes('&', newmuffix);
    }

    public String getShownGroup() {
        return this.showngroup;
    }

    public void setShownGroup(String newgroup) {
        this.showngroup = translateAlternateColorCodes('&', newgroup);
    }

    public String getExample() {
        return this.format.replace("%1$s", "player").replace("%2$s", "message");
    }

    public String getFormat(String message) {
        if (message.contains("&")) {
            return this.format.replace("%2$s", translateAlternateColorCodes('&', message));
        }
        return this.format;
    }

    public String getFormat() {
        return this.format;
    }

    public String getGroup() {
        return this.group;
    }

    private void format(String mat) {
        String m = mat.substring(0, 1);
        this.format = mat.replace(m + "%g", prefix + m + showngroup).replace("%p", suffix + "%1$s").replace("%m", muffix + "%2$s").replace("null", WHITE.toString());
    }
}
