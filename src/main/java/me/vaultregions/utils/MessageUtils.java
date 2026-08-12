package me.vaultregions.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    private static final String PREFIX = "<gradient:#FFE066:#FFFFFF><bold>Silvera</bold></gradient> <dark_gray>»</dark_gray> <white>";
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static void sendMessage(CommandSender sender, String message) {
        Component component = MINI_MESSAGE.deserialize(PREFIX + message);
        sender.sendMessage(component);
    }

    public static void sendRawMessage(CommandSender sender, String message) {
        Component component = MINI_MESSAGE.deserialize(message);
        sender.sendMessage(component);
    }
}
