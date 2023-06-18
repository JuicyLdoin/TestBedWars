package net.ldoin.bedwars.listener.listeners.player;

import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import net.ldoin.bedwars.game.waiting.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final GameManager gameManager;

    public PlayerJoinListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gameManager.getGameState() != GameState.WAITING) {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }
        Waiting waiting = gameManager.getWaiting();
        waiting.addPlayer(player);
        player.teleport(waiting.getLobbyLocation());
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.broadcastMessage(String.format("Игрок §e%s §fприсоединился к игре", player.getName()));
    }
}