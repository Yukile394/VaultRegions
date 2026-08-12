package me.vaultregions.managers;

import me.vaultregions.VaultRegions;
import me.vaultregions.models.PlayerSelection;
import me.vaultregions.models.Region;
import net.luckperms.api.model.user.User;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionManager {

    private final VaultRegions plugin;
    private final Map<String, Region> regions = new HashMap<>();
    private final Map<UUID, PlayerSelection> selections = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public RegionManager(VaultRegions plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "regions.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("regions.yml dosyası oluşturulamadı!");
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadRegions();
    }

    public void loadRegions() {
        regions.clear();
        ConfigurationSection section = config.getConfigurationSection("regions");
        if (section == null) return;

        for (String id : section.getKeys(false)) {
            String world = section.getString(id + ".world");
            String role = section.getString(id + ".role");
            int minX = section.getInt(id + ".minX");
            int minY = section.getInt(id + ".minY");
            int minZ = section.getInt(id + ".minZ");
            int maxX = section.getInt(id + ".maxX");
            int maxY = section.getInt(id + ".maxY");
            int maxZ = section.getInt(id + ".maxZ");

            regions.put(id, new Region(id, world, role, minX, minY, minZ, maxX, maxY, maxZ));
        }
        plugin.getLogger().info(regions.size() + " bölge başarıyla yüklendi.");
    }

    public void saveRegions() {
        config.set("regions", null);
        for (Region region : regions.values()) {
            String path = "regions." + region.getId();
            config.set(path + ".world", region.getWorldName());
            config.set(path + ".role", region.getRequiredRole());
            config.set(path + ".minX", region.getMinX());
            config.set(path + ".minY", region.getMinY());
            config.set(path + ".minZ", region.getMinZ());
            config.set(path + ".maxX", region.getMaxX());
            config.set(path + ".maxY", region.getMaxY());
            config.set(path + ".maxZ", region.getMaxZ());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("regions.yml kaydedilirken hata oluştu!");
        }
    }

    public boolean addRegion(String id, String role, Location p1, Location p2) {
        if (regions.containsKey(id)) return false;
        
        Region region = new Region(id, p1.getWorld().getName(), role, p1, p2);
        regions.put(id, region);
        saveRegions();
        return true;
    }

    public void updateRegion(String id, String role, Location p1, Location p2) {
        if (regions.containsKey(id)) {
            Region region = regions.get(id);
            region.setRequiredRole(role);
            region.updateBounds(p1, p2);
        } else {
            addRegion(id, role, p1, p2);
        }
        saveRegions();
    }

    public boolean hasPermission(Player player, String requiredGroup) {
        User user = plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission("group." + requiredGroup.toLowerCase()).asBoolean();
    }

    public Collection<Region> getRegions() {
        return regions.values();
    }

    public boolean hasRegion(String id) {
        return regions.containsKey(id);
    }

    public PlayerSelection getSelection(Player player) {
        return selections.computeIfAbsent(player.getUniqueId(), k -> new PlayerSelection());
    }
}
