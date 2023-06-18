package net.ldoin.bedwars.game.waiting;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.Game;
import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import net.ldoin.bedwars.game.arena.Arena;
import net.ldoin.bedwars.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Waiting {

    private final BedWarsPlugin plugin;
    private final GameManager gameManager;
    private final Game game;
    private final Arena arena;
    private final List<Player> players;
    private int toStart;
    private final Location lobbyLocation;

    public Waiting(BedWarsPlugin plugin, GameManager gameManager, Arena arena) {
        this.plugin = plugin;
        game = new Game(plugin, gameManager, arena);
        this.gameManager = gameManager;
        gameManager.setGame(game);
        this.arena = arena;
        players = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();
        toStart = config.getInt("waiting.toStart", 30);
        lobbyLocation = LocationUtil.getLocation(config.getString("waiting.lobby_location"));
    }

    public final List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public final Location getLobbyLocation() {
        return lobbyLocation;
    }

    public final void addPlayer(Player player) {
        players.add(player);
    }

    public final void removePlayer(Player player) {
        players.remove(player);
    }

    private void startGame() {
        gameManager.setGameState(GameState.GAME);
        game.start(players);
    }

    public final void startCount() {
        new BukkitRunnable() {
            public void run() {
                if (players.size() < arena.getMinPlayers()) {
                    return;
                }
                toStart--;
                if (toStart == 0) {
                    startGame();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}