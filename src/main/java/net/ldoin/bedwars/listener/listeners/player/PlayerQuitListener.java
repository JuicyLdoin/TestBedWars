package net.ldoin.bedwars.listener.listeners.player;

import net.ldoin.bedwars.game.Game;
import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import net.ldoin.bedwars.game.waiting.Waiting;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

    private final UserManager userManager;
    private final GameManager gameManager;

    public PlayerQuitListener(UserManager userManager, GameManager gameManager) {
        this.userManager = userManager;
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GameState gameState = gameManager.getGameState();
        if (gameState == GameState.WAITING) {
            Waiting waiting = gameManager.getWaiting();
            waiting.removePlayer(player);
            Bukkit.broadcastMessage(String.format("Игрок §e%s §fпокинул игру", player.getName()));
        } else if (gameState == GameState.END) {
            Game game = gameManager.getGame();
            game.removePlayer(player);
        }
        gameManager.getScoreboardManager().clear(player);
        userManager.unloadUser(player);
    }
}