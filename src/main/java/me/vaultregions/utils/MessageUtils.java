package me.vaultregions.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    private static final String PREFIX = "<gold>Silvera</gold> <dark_gray>|</dark_gray> <gray>";
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
