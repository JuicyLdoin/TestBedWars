package net.ldoin.bedwars.listener.listeners.player;

import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import net.ldoin.bedwars.game.arena.ArenaManager;
import net.ldoin.bedwars.game.waiting.Waiting;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public final class AsyncPlayerPreLoginListener implements Listener {

    private final UserManager userManager;
    private final ArenaManager arenaManager;
    private final GameManager gameManager;

    public AsyncPlayerPreLoginListener(UserManager userManager, ArenaManager arenaManager, GameManager gameManager) {
        this.userManager = userManager;
        this.arenaManager = arenaManager;
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onPreLogin(AsyncPlayerPreLoginEvent event) {
        GameState gameState = gameManager.getGameState();
        if (gameState == GameState.END) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Сервер перезапускается");
            return;
        }
        Waiting waiting = gameManager.getWaiting();
        if (waiting.getPlayers().size() >= arenaManager.getArena().getMaxPlayers()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Сервер заполнен");
            return;
        }
        userManager.loadUser(event.getUniqueId(), event.getName());
    }
}