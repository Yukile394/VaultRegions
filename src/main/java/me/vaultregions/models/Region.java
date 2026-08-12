package me.vaultregions.models;

import org.bukkit.Location;

public class Region {
    private final String id;
    private final String worldName;
    private String requiredRole;
    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;

    public Region(String id, String worldName, String requiredRole, Location pos1, Location pos2) {
        this.id = id;
        this.worldName = worldName;
        this.requiredRole = requiredRole;
        updateBounds(pos1, pos2);
    }

    public Region(String id, String worldName, String requiredRole, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.worldName = worldName;
        this.requiredRole = requiredRole;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void updateBounds(Location pos1, Location pos2) {
        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        this.maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }

    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().getName().equals(this.worldName)) return false;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    // Getters
    public String getId() { return id; }
    public String getWorldName() { return worldName; }
    public String getRequiredRole() { return requiredRole; }
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }
}
