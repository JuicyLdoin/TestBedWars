package net.ldoin.bedwars.game;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.arena.Arena;
import net.ldoin.bedwars.game.arena.ArenaMaterialSpawner;
import net.ldoin.bedwars.game.arena.team.ArenaTeam;
import net.ldoin.bedwars.game.cache.BlockCache;
import net.ldoin.bedwars.game.shop.Shop;
import net.ldoin.bedwars.scoreboard.ScoreboardManager;
import net.ldoin.bedwars.user.UserData;
import net.ldoin.bedwars.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {

    private final BedWarsPlugin plugin;
    private final GameManager gameManager;
    private final UserManager userManager;
    private final Arena arena;
    private final List<GameTeam> teams;
    private final Map<Location, GameTeam> bedTeams;
    private final Map<Player, GameTeam> playerTeams;
    private final Shop shop;
    private final BlockCache blockCache;

    public Game(BedWarsPlugin plugin, GameManager gameManager, Arena arena) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        userManager = plugin.getUserManager();
        this.arena = arena;
        teams = new ArrayList<>();
        bedTeams = new HashMap<>();

        for (ArenaTeam arenaTeam : arena.getTeams()) {
            GameTeam gameTeam = new GameTeam(arenaTeam);
            bedTeams.put(arenaTeam.getFirstBedLocation(), gameTeam);
            bedTeams.put(arenaTeam.getSecondBedLocation(), gameTeam);
            teams.add(gameTeam);
        }
        playerTeams = new HashMap<>();
        shop = new Shop(plugin.getConfig().getConfigurationSection("shop"));
        blockCache = new BlockCache();
    }

    public Arena getArena() {
        return arena;
    }

    public List<GameTeam> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public GameTeam getTeamByBedLocation(Location bedLocation) {
        return bedTeams.get(bedLocation);
    }

    public GameTeam getPlayerTeam(Player player) {
        return playerTeams.get(player);
    }

    public Shop getShop() {
        return shop;
    }

    public BlockCache getBlockCache() {
        return blockCache;
    }

    public void removePlayer(Player player) {
        playerTeams.remove(player);
        if (playerTeams.isEmpty()) {
            end(new ArrayList<>());
        }
    }

    public boolean playerDeath(Player player) {
        UserData playerUser = userManager.getUser(player);
        playerUser.addDeath();

        Player killer = player.getKiller();
        if (killer != null) {
            UserData killerUser = userManager.getUser(killer);
            killerUser.addKill();
            Bukkit.broadcastMessage(String.format("Игрок %s был убит игроком %s", player.getName(), killer.getName()));
        } else {
            Bukkit.broadcastMessage(String.format("Игрок %s умер", player.getName()));
        }

        GameTeam team = getPlayerTeam(player);
        if (!team.isHasBed()) {
            playerUser.addLose();
            team.removePlayer(player);
            removePlayer(player);

            if (team.getPlayers().size() == 0) {
                teams.remove(team);
                Bukkit.broadcastMessage(String.format("§fКоманда %s §fбыла уничтожена", team.getArenaTeam().getDisplayName()));
                if (teams.size() == 1) {
                    end(teams.get(0).getPlayers());
                }
            }

            return true;
        }

        return false;
    }

    private void balancePlayers(List<Player> players) {
        Collections.shuffle(players);
        Queue<Player> balance = new LinkedList<>(players);
        while (!balance.isEmpty()) {
            for (GameTeam gameTeam : teams) {
                Player player = balance.poll();
                gameTeam.addPlayer(player);
                playerTeams.put(player, gameTeam);
            }
        }
    }

    private void spawnPlayers() {
        for (Map.Entry<Player, GameTeam> playerEntry : playerTeams.entrySet()) {
            playerEntry.getKey().teleport(playerEntry.getValue().getArenaTeam().getSpawnLocation());
        }
    }

    public void start(List<Player> players) {
        for (Location shopLocation : arena.getShopLocations()) {
            shop.spawn(shopLocation);
        }
        balancePlayers(players);
        spawnPlayers();

        arena.getSpawners().forEach(arenaMaterialSpawner -> arenaMaterialSpawner.start(plugin));
        gameManager.getScoreboardManager().start();
    }

    public void end(List<Player> winners) {
        if (!winners.isEmpty()) {
            for (Player player : winners) {
                UserData userData = userManager.getUser(player);
                userData.addWin();
            }
        }
        arena.getSpawners().forEach(ArenaMaterialSpawner::stop);

        ScoreboardManager scoreboardManager = gameManager.getScoreboardManager();
        scoreboardManager.stop();
        scoreboardManager.clear();
        gameManager.setGameState(GameState.END);

        new BukkitRunnable() {
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Сервер перезапускается"));
                arena.getWorld().getEntities().forEach(Entity::remove);
                blockCache.clear();
                Bukkit.getServer().shutdown();
            }
        }.runTaskLater(plugin, 20 * 20);
    }
}