package ru.x1ndi.x1statmine; // Замените на ваш пакет

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetStatsCommand implements CommandExecutor {
    private final X1statmine plugin;

    public ResetStatsCommand(X1statmine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверка прав пользователя
        if (sender.hasPermission("statmine.reset")) {
            try (Connection connection = plugin.getConnection(); Statement statement = connection.createStatement()) {
                // Сброс статистики
                statement.execute("DELETE FROM player_stats");
                sender.sendMessage("Статистика добытых блоков была сброшена.");
            } catch (SQLException e) {
                sender.sendMessage("Произошла ошибка при сбросе статистики.");
                e.printStackTrace();
            }
            return true;
        } else {
            sender.sendMessage("У вас нет прав для выполнения этой команды.");
            return false;
        }
    }
}