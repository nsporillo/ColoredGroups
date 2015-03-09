package net.porillo;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultImporter {

    private final ColoredGroups cg;
    private final Plugin vault;

    protected VaultImporter(ColoredGroups cg) {
        this.cg = cg;
        this.vault = cg.getServer().getPluginManager().getPlugin("Vault");
    }

    protected void run(boolean override) {
        if (cg.getConfiguration().importer || override) {
            this.runImport(getPerms(), getChat());
        }
    }

    private Permission getPerms() {
        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Permission> rsp = cg.getServer().getServicesManager().getRegistration(Permission.class);
            return rsp.getProvider();
        }
        return null;
    }

    private Chat getChat() {
        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Chat> rsp = cg.getServer().getServicesManager().getRegistration(Chat.class);
            return rsp.getProvider();
        }
        return null;
    }

    private void runImport(Permission perms, Chat chat) {
        Config conf = cg.getConfiguration();
        if (perms == null || chat == null) {
            cg.warn("Vault ");          
        } else {
            for (String section : cg.getConfig().getConfigurationSection("groups").getKeys(false)) {
                conf.deleteGroup(section);
                cg.debug("Deleting " + section);
            }
            conf.reload();
            for (String group : perms.getGroups()) {
                if(conf.existsGroup(group)) {
                    cg.debug("Skipping " + group);
                    continue;
                }
                conf.createNewGroup(group);
                cg.debug("Imported " + group);
            }
            conf.reload();
        }
        conf.set("options", "import", false);
    }
    
}
