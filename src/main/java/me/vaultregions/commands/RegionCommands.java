package me.vaultregions.commands;

import me.vaultregions.VaultRegions;
import me.vaultregions.models.PlayerSelection;
import me.vaultregions.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class RegionCommands implements CommandExecutor {

    private final VaultRegions plugin;
    public static final NamespacedKey WAND_KEY = new NamespacedKey("vaultregions", "wand");

    public RegionCommands(VaultRegions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtils.sendMessage(sender, "<#FF5C5C>Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "vipwand":
                giveWand(player);
                break;

            case "vipmadenolustur":
                if (args.length != 2) {
                    MessageUtils.sendMessage(player, "<#FF5C5C>Kullanım: /vipmadenolustur <Rol> <ID>");
                    return true;
                }
                createRegion(player, args[0], args[1]);
                break;

            case "vipsinirayarla":
                if (args.length != 2) {
                    MessageUtils.sendMessage(player, "<#FF5C5C>Kullanım: /vipsinirayarla <Rol> <ID>");
                    return true;
                }
                updateRegion(player, args[0], args[1]);
                break;
        }
        return true;
    }

    private void giveWand(Player player) {
        ItemStack wand = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = wand.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<gradient:#FFE066:#FFFFFF><bold>VIP Bölge Seçim Baltası</bold></gradient>"));
        meta.lore(List.of(
            MiniMessage.miniMessage().deserialize("<white>Sol tık <dark_gray>→ <#FFE066><bold>İlk noktayı belirler.</bold>"),
            MiniMessage.miniMessage().deserialize("<white>Sağ tık <dark_gray>→ <#FFE066><bold>İkinci noktayı belirler.</bold>")
        ));

        meta.getPersistentDataContainer().set(WAND_KEY, PersistentDataType.BYTE, (byte) 1);
        wand.setItemMeta(meta);

        player.getInventory().addItem(wand);
        MessageUtils.sendMessage(player, "<#4ADE80>Seçim baltası envanterinize eklendi.");
    }

    private void createRegion(Player player, String role, String id) {
        PlayerSelection selection = plugin.getRegionManager().getSelection(player);
        if (!selection.isComplete()) {
            MessageUtils.sendMessage(player, "<#FF5C5C>Öncelikle baltayla her iki noktayı da seçmelisiniz.");
            return;
        }

        if (plugin.getRegionManager().hasRegion(id)) {
            MessageUtils.sendMessage(player, "<#FF5C5C>Bu maden ID'si zaten kullanımda.");
            return;
        }

        plugin.getRegionManager().addRegion(id, role, selection.getPos1(), selection.getPos2());
        MessageUtils.sendMessage(player, "<#4ADE80>Maden bölgesi başarıyla oluşturuldu! <dark_gray>(<white>ID: <#FFE066><bold>" + id + "</bold><white>, Rol: <#FFE066><bold>" + role + "</bold><dark_gray>)");
    }

    private void updateRegion(Player player, String role, String id) {
        PlayerSelection selection = plugin.getRegionManager().getSelection(player);
        if (!selection.isComplete()) {
            MessageUtils.sendMessage(player, "<#FF5C5C>Öncelikle baltayla her iki noktayı da seçmelisiniz.");
            return;
        }

        plugin.getRegionManager().updateRegion(id, role, selection.getPos1(), selection.getPos2());
        MessageUtils.sendMessage(player, "<#4ADE80>Maden bölgesi sınırları ve yetkisi başarıyla güncellendi! <dark_gray>(<white>ID: <#FFE066><bold>" + id + "</bold><white>, Rol: <#FFE066><bold>" + role + "</bold><dark_gray>)");
    }
}
