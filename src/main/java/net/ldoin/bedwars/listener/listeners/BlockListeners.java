package net.ldoin.bedwars.listener.listeners;

import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.GameState;
import net.ldoin.bedwars.game.GameTeam;
import net.ldoin.bedwars.game.arena.team.ArenaTeam;
import net.ldoin.bedwars.game.cache.BlockCache;
import net.ldoin.bedwars.user.UserData;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockListeners implements Listener {

    private final GameManager gameManager;
    private final UserManager userManager;

    public BlockListeners(GameManager gameManager, UserManager userManager) {
        this.gameManager = gameManager;
        this.userManager = userManager;
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (gameManager.getGameState() != GameState.GAME) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();
        BlockCache blockCache = gameManager.getGame().getBlockCache();
        blockCache.addBlock(block);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (gameManager.getGameState() != GameState.GAME) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == Material.BED_BLOCK) {
            Player player = event.getPlayer();
            UserData userData = userManager.getUser(player);
            userData.addBrokenBed();
            GameTeam gameTeam = gameManager.getGame().getTeamByBedLocation(block.getLocation());
            event.setCancelled(true);
            gameTeam.setHasBed(false);
            ArenaTeam arenaTeam = gameTeam.getArenaTeam();
            setAir(arenaTeam.getFirstBedLocation());
            setAir(arenaTeam.getSecondBedLocation());
            Bukkit.broadcastMessage("§fКровать команды " + arenaTeam.getDisplayName() + " §fбыла сломана");
            return;
        }
        BlockCache blockCache = gameManager.getGame().getBlockCache();
        if (!blockCache.containsBlock(block)) {
            event.setCancelled(true);
            return;
        }
        blockCache.removeBlock(block);
    }

    private void setAir(Location location) {
        location.getBlock().setType(Material.AIR);
    }
}