package ru.x1ndi.x1statmine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class X1statmine extends JavaPlugin implements Listener {
    private Connection connection;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        setupDatabase();
        getCommand("statmine-reset").setExecutor(new ResetStatsCommand(this));

        // Регистрация плейсхолдера
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholder(this).register();
        } else {
            getLogger().warning("PlaceholderAPI не найден! Плейсхолдеры не будут работать.");
        }
    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:mine_stats.db");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS player_stats (player_name TEXT PRIMARY KEY, blocks_mined INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        updatePlayerStats(playerName);
    }

    private void updatePlayerStats(String playerName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO player_stats (player_name, blocks_mined) VALUES (?, 1) ON CONFLICT(player_name) DO UPDATE SET blocks_mined = blocks_mined + 1");
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public int getMinedBlocks(String playerName) {
        int minedBlocks = 0;
        try {
            if (connection == null || connection.isClosed()) {
                setupDatabase(); // Попробуйте заново установить соединение
            }
            PreparedStatement statement = connection.prepareStatement("SELECT blocks_mined FROM player_stats WHERE player_name = ?");
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                minedBlocks = resultSet.getInt("blocks_mined");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minedBlocks;
    }
}

