package net.ldoin.bedwars.listener;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.arena.ArenaManager;
import net.ldoin.bedwars.listener.listeners.BlockListeners;
import net.ldoin.bedwars.listener.listeners.player.AsyncPlayerPreLoginListener;
import net.ldoin.bedwars.listener.listeners.player.PlayerDeathListener;
import net.ldoin.bedwars.listener.listeners.player.PlayerJoinListener;
import net.ldoin.bedwars.listener.listeners.player.PlayerQuitListener;
import net.ldoin.bedwars.listener.listeners.shop.ShopClickListener;
import net.ldoin.bedwars.listener.listeners.shop.ShopOpenListener;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public final class ListenerManager {

    public ListenerManager(BedWarsPlugin plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        UserManager userManager = plugin.getUserManager();
        ArenaManager arenaManager = plugin.getArenaManager();
        GameManager gameManager = plugin.getGameManager();

        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(userManager, arenaManager, gameManager), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(gameManager), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(gameManager), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(userManager, gameManager), plugin);
        pluginManager.registerEvents(new ShopClickListener(gameManager), plugin);
        pluginManager.registerEvents(new ShopOpenListener(gameManager), plugin);
        pluginManager.registerEvents(new BlockListeners(gameManager, userManager), plugin);
    }
}