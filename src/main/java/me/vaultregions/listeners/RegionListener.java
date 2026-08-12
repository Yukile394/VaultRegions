package me.vaultregions.listeners;

import me.vaultregions.VaultRegions;
import me.vaultregions.commands.RegionCommands;
import me.vaultregions.models.PlayerSelection;
import me.vaultregions.models.Region;
import me.vaultregions.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class RegionListener implements Listener {

    private final VaultRegions plugin;

    public RegionListener(VaultRegions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();

        if (player.hasPermission("vaultregions.bypass")) return;

        for (Region region : plugin.getRegionManager().getRegions()) {
            if (region.contains(loc)) {
                if (!plugin.getRegionManager().hasPermission(player, region.getRequiredRole())) {
                    event.setCancelled(true);

                    String roleName = region.getRequiredRole().toUpperCase();
                    MessageUtils.sendMessage(player, "<#FF5C5C>Bu madeni kırabilmek için <gradient:#FFE066:#FFFFFF><bold>" + roleName + "</bold></gradient> <#FF5C5C>veya daha üstü bir rütbeye sahip olmalısın.");
                }
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isAir() || !item.hasItemMeta()) return;
        if (!item.getItemMeta().getPersistentDataContainer().has(RegionCommands.WAND_KEY, PersistentDataType.BYTE)) return;

        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {

            event.setCancelled(true);

            PlayerSelection selection = plugin.getRegionManager().getSelection(player);
            Location loc = event.getClickedBlock().getLocation();

            if (action == Action.LEFT_CLICK_BLOCK) {
                selection.setPos1(loc);
                MessageUtils.sendMessage(player, "<#4ADE80>İlk nokta seçildi: <#FFE066><bold>" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "</bold>");
            } else {
                selection.setPos2(loc);
                MessageUtils.sendMessage(player, "<#4ADE80>İkinci nokta seçildi: <#FFE066><bold>" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "</bold>");
            }
        }
    }
}
