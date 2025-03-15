package ru.x1ndi.x1statmine;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholder extends PlaceholderExpansion {
    private final X1statmine plugin;

    public Placeholder(X1statmine plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "minestat"; // Идентификатор для плейсхолдера
    }

    @Override
    public String getAuthor() {
        return "X1nDi"; // Ваше имя или ник
    }

    @Override
    public String getVersion() {
        return "1.0"; // Версия вашего плагина
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return null; // Если игрок null, возвращаем null
        }

        if (identifier.startsWith("mined_blocks-watch_")) {
            // Получаем никнейм игрока из плейсхолдера
            String targetPlayerName = identifier.substring("mined_blocks-watch_".length());
            // Получаем количество добытых блоков для указанного игрока
            return formatMinedBlocks(plugin.getMinedBlocks(targetPlayerName));
        }

        if (identifier.startsWith("mined_blocks_")) {
            // Получаем никнейм игрока из плейсхолдера
            String targetPlayerName = identifier.substring("mined_blocks_".length());
            // Получаем количество добытых блоков для указанного игрока
            return String.valueOf(plugin.getMinedBlocks(targetPlayerName));
        }

        if (identifier.equals("mined_blocks-watch")) {
            return formatMinedBlocks(plugin.getMinedBlocks(player.getName())); // Возвращаем количество добытых блоков
        }

        if (identifier.equals("mined_blocks")) {
            return String.valueOf(plugin.getMinedBlocks(player.getName())); // Возвращаем количество добытых блоков
        }

        return null; // Возвращаем null, если плейсхолдер не распознан
    }

    private String formatMinedBlocks(int minedBlocks) {
        if (minedBlocks < 1000) {
            return "&c" + minedBlocks; // Если меньше 1000, возвращаем с красной приставкой
        } else {
            return "&a1000"; // Если 1000 или больше, возвращаем 1000 с зелёной приставкой
        }
    }
}
