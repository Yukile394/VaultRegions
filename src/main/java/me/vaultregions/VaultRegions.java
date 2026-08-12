package me.vaultregions;

import me.vaultregions.commands.RegionCommands;
import me.vaultregions.listeners.RegionListener;
import me.vaultregions.managers.RegionManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultRegions extends JavaPlugin {

    private LuckPerms luckPerms;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        // LuckPerms Bağlantısı
        if (!setupLuckPerms()) {
            getLogger().severe("LuckPerms bulunamadı! VaultRegions devre dışı bırakılıyor.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Yöneticileri Başlat
        this.regionManager = new RegionManager(this);

        // Komut ve Event Kayıtları
        RegionCommands commandExecutor = new RegionCommands(this);
        getCommand("vipwand").setExecutor(commandExecutor);
        getCommand("vipmadenolustur").setExecutor(commandExecutor);
        getCommand("vipsinirayarla").setExecutor(commandExecutor);

        getServer().getPluginManager().registerEvents(new RegionListener(this), this);

        getLogger().info("VaultRegions (v" + getDescription().getVersion() + ") başarıyla aktifleştirildi!");
    }

    @Override
    public void onDisable() {
        if (regionManager != null) {
            regionManager.saveRegions();
        }
        getLogger().info("VaultRegions devre dışı bırakıldı.");
    }

    private boolean setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            return true;
        }
        return false;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
