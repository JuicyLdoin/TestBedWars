package net.ldoin.bedwars.listener.listeners.player;

import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;
import java.util.Set;

public final class PlayerDeathListener implements Listener {

    private final GameManager gameManager;
    private final Set<Player> respawnAsSpectator;

    public PlayerDeathListener(GameManager gameManager) {
        this.gameManager = gameManager;
        respawnAsSpectator = new HashSet<>();
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        event.setKeepInventory(true);
        if (gameManager.getGameState() != GameState.GAME) {
            return;
        }
        Player player = event.getEntity();
        if (gameManager.getGame().playerDeath(player)) {
            respawnAsSpectator.add(player);
        }
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        if (respawnAsSpectator.contains(player)) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }
}