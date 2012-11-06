import java.util.ArrayList;
import java.util.List;

import net.milkycraft.addons.Addon;
import net.milkycraft.addons.AddonConfig;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GroupControlAddon extends Addon implements CommandExecutor {

	private boolean enabled;
	private String version = "1.1";
	private String name = "GroupControlAddon";
	private String description = "Adds useful commands to ColoredGroups";
	private String configRepo = "http://milkycraft.net/config.yml";
	private List<String> authors = new ArrayList<String>();
	private AddonConfig config;
	
	@Override
	public void enable() {
		authors.add("milkywayz");
		try {			
			super.plugin.getCommand("gca").setExecutor(this);
			config = new AddonConfig(super.plugin, this, "config.yml");
			config.installConfigFromUrl(configRepo);
			test();
		} catch (Exception exe) {
			System.out.println("Enabling of " + this.getClass().getSimpleName()
					+ " has failed due to " + exe.getLocalizedMessage());
		}	
		enabled = true;
	}
	
	public void test() {
		super.plugin.log(this, "Test: " + config.getConfig().getString("test"));
	}
	
	@Override
	public void disable() {
		enabled = false;
	}
	

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public List<String> getAuthors() {
		return authors;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public AddonConfig getConfig() {
		return config;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(!enabled) {
			return enabled;
		}
		if (args.length == 0) {
			test();
			sender.sendMessage(ChatColor.GREEN
					+ "== GroupControlAddon Commands ==");
			sender.sendMessage(ChatColor.GREEN
					+ "[1] /gca new --cache <group> <prefix> <suffix> <muffix>");
			sender.sendMessage(ChatColor.GREEN
					+ "[2] /gca new --config <group> <prefix> <suffix> <muffix>");
			sender.sendMessage(ChatColor.GREEN
					+ "[3] /gca delete --cache <group>");
			sender.sendMessage(ChatColor.GREEN
					+ "[4] /gca delete --config <group>");
			return true;
		}
		if (args[0].equalsIgnoreCase("new")) {
			// cg new Owner &4 &5 &f [%g]%p: %m
			try {
				if (args[1].equalsIgnoreCase("--cache")) {
					if (sender.hasPermission("coloredgroups.create.cache")) {
						super.plugin.createTempChatProfile(args[2], args[2] ,args[3], args[4],
								args[5], "[%g]%p: %m");
						sender.sendMessage(ChatColor.GREEN
								+ "Created temp group: " + args[2] + " with "
								+ a(args[3]) + a(args[3]).name().toLowerCase()
								+ ChatColor.GREEN + " prefix, " + a(args[4])
								+ a(args[4]).name().toLowerCase()
								+ ChatColor.GREEN + " suffix, and "
								+ a(args[5]) + a(args[5]).name().toLowerCase()
								+ ChatColor.GREEN + " muffix.");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "You dont have permission to create cached groups");
					}
				} else if (args[1].equalsIgnoreCase("--config")) {
					if (sender.hasPermission("coloredgroups.create.config")) {
						super.plugin.createChatProfile(args[2], args[3], args[4],
								args[5], "[%g]%p: %m");
						sender.sendMessage(ChatColor.GREEN
								+ "Created config group: " + args[2] + " with "
								+ a(args[3]) + a(args[3]).name().toLowerCase()
								+ ChatColor.GREEN + " prefix, " + a(args[4])
								+ a(args[4]).name().toLowerCase()
								+ ChatColor.GREEN + " suffix, and "
								+ a(args[5]) + a(args[5]).name().toLowerCase()
								+ ChatColor.GREEN + " muffix.");
					} else {
						sender.sendMessage(ChatColor.RED
								+ "You dont have permission to create hard groups");
					}
				} else {
					sender.sendMessage(ChatColor.RED
							+ "Specify either --cache or --config after new");
				}
			} catch (ArrayIndexOutOfBoundsException aio) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have enough arguments!");
			} catch (UnsupportedOperationException uoe) {
				sender.sendMessage(ChatColor.RED + args[2]
						+ " is already in config, delete it first");
			}
		} else if (args[0].equalsIgnoreCase("--delete")) {
			try {
				if (args[1].equalsIgnoreCase("config")) {
					super.plugin.deleteChatProfile(args[2]);
					sender.sendMessage(ChatColor.GREEN + "Removed " + args[2]
							+ " from config!");
				} else if (args[1].equalsIgnoreCase("--cache")) {
					super.plugin.deleteTempChatProfile(args[2]);
					sender.sendMessage(ChatColor.GREEN + "Removed " + args[2]
							+ " from cache!");
				}
			} catch (ArrayIndexOutOfBoundsException exe) {
				sender.sendMessage(ChatColor.RED + "Specify a group to delete!");
			} catch (NullPointerException npe) {
				sender.sendMessage(ChatColor.RED + "That group does not exist");
			}
		}
		return false;
	}

	public ChatColor a(final String b) {
		return b(ChatColor.translateAlternateColorCodes('&', b)
				.replace("§", "").replace("$", ""));
	}

	public ChatColor b(final String c) {
		return ChatColor.getByChar(c);
	}

}
