package net.ldoin.bedwars;

import net.ldoin.bedwars.database.DatabaseConnection;
import net.ldoin.bedwars.database.DatabaseData;
import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.arena.ArenaManager;
import net.ldoin.bedwars.listener.ListenerManager;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class BedWarsPlugin extends JavaPlugin {

    private ArenaManager arenaManager;
    private GameManager gameManager;
    private UserManager userManager;

    public void onEnable() {
        Logger logger = getLogger();

        ConfigurationSection databaseSection = getConfig().getConfigurationSection("database");
        DatabaseData databaseData = new DatabaseData(
                databaseSection.getString("login", "root"),
                databaseSection.getString("password", ""),
                databaseSection.getString("host", "localhost"),
                databaseSection.getString("port", "3306"),
                databaseSection.getString("database", "bedwars")
        );
        DatabaseConnection databaseConnection;
        try {
            databaseConnection = new DatabaseConnection(databaseData, 8);
        } catch (Exception exception) {
            logger.log(Level.WARNING, "Database isn`t connected\n" + databaseData, exception);
            Bukkit.getServer().shutdown();
            return;
        }

        arenaManager = new ArenaManager(this);
        if (arenaManager.getArena() == null) {
            logger.log(Level.WARNING, "None of the arenas isn`t loaded");
            Bukkit.getServer().shutdown();
            return;
        }
        gameManager = new GameManager(this);
        userManager = new UserManager(logger, databaseConnection);

        new ListenerManager(this);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}