package net.ldoin.bedwars.game.arena.team;

import net.ldoin.bedwars.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public final class ArenaTeam {

    private final String name;
    private final String color;
    private final Location spawnLocation;
    private final Location firstBedLocation;
    private final Location secondBedLocation;

    public ArenaTeam(String name, String color, Location spawnLocation, Location bedLocation) {
        this.name = name;
        this.color = color;
        this.spawnLocation = spawnLocation;
        this.firstBedLocation = bedLocation;
        Block firstBedBlock = bedLocation.getBlock();
        Location secondBedLocation = null;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block secondBedBlock = firstBedBlock.getRelative(x, 0, z);
                if (!secondBedBlock.getLocation().equals(bedLocation)) {
                    if (secondBedBlock.getType() == Material.BED_BLOCK) {
                        secondBedLocation = secondBedBlock.getLocation();
                        break;
                    }
                }
            }
        }
        this.secondBedLocation = secondBedLocation;
    }

    public ArenaTeam(ConfigurationSection configurationSection) {
        this(
                configurationSection.getString("name"),
                configurationSection.getString("color"),
                LocationUtil.getLocation(configurationSection.getString("spawn_location")),
                LocationUtil.getLocation(configurationSection.getString("bed_location"))
        );
    }

    public final String getDisplayName() {
        return getPrefix() + name;
    }

    private String getPrefix() {
        return "§" + color;
    }

    public final Location getSpawnLocation() {
        return spawnLocation;
    }

    public final Location getFirstBedLocation() {
        return firstBedLocation;
    }

    public final Location getSecondBedLocation() {
        return secondBedLocation;
    }
}