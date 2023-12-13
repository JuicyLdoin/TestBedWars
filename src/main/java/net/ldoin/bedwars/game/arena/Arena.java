package net.ldoin.bedwars.game.arena;

import net.ldoin.bedwars.game.arena.team.ArenaTeam;
import net.ldoin.bedwars.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arena {

    private final World world;
    private final List<ArenaMaterialSpawner> spawners;
    private final List<ArenaTeam> teams;
    private final List<Location> shopLocations;
    private final int minPlayers;
    private final int maxPlayers;

    public Arena(World world, List<ArenaMaterialSpawner> spawners, List<ArenaTeam> teams, List<Location> shopLocations, int minPlayers, int maxPlayers) {
        this.world = world;
        this.spawners = spawners;
        this.teams = teams;
        this.shopLocations = shopLocations;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Arena(ConfigurationSection configurationSection) {
        this(
                Bukkit.getWorld(configurationSection.getString("world", "world")),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                configurationSection.getInt("min_players"),
                configurationSection.getInt("max_players"));

        ConfigurationSection spawnersSection = configurationSection.getConfigurationSection("spawners");
        for (String spawnerSection : spawnersSection.getKeys(false)) {
            spawners.add(new ArenaMaterialSpawner(spawnersSection.getConfigurationSection(spawnerSection)));
        }
        ConfigurationSection teamsSection = configurationSection.getConfigurationSection("teams");
        for (String teamSection : teamsSection.getKeys(false)) {
            teams.add(new ArenaTeam(teamsSection.getConfigurationSection(teamSection)));
        }
        for (String rawShopLocation : configurationSection.getStringList("shops")) {
            shopLocations.add(LocationUtil.getLocation(rawShopLocation));
        }
    }

    public World getWorld() {
        return world;
    }

    public List<ArenaMaterialSpawner> getSpawners() {
        return Collections.unmodifiableList(spawners);
    }

    public List<ArenaTeam> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public List<Location> getShopLocations() {
        return Collections.unmodifiableList(shopLocations);
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}