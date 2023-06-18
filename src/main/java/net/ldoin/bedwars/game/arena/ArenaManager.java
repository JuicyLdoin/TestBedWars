package net.ldoin.bedwars.game.arena;

import net.ldoin.bedwars.BedWarsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ArenaManager {

    private final List<Arena> arenas;
    private Arena arena;

    public ArenaManager(BedWarsPlugin plugin) {
        List<Arena> arenas = new ArrayList<>();
        File parent = new File(plugin.getDataFolder(), "arenas");
        if (!parent.exists()) {
            parent.mkdirs();
        } else {
            File[] arenaFiles = parent.listFiles();
            if (arenaFiles != null) {
                for (File arenaFile : arenaFiles) {
                    FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
                    ConfigurationSection configurationSection = arenaConfig.getConfigurationSection("arena");
                    if (configurationSection != null) {
                        arenas.add(new Arena(configurationSection));
                    }
                }
            }
        }
        this.arenas = Collections.unmodifiableList(arenas);
    }

    public final Arena getArena() {
        if (arena == null) {
            loadRandomArena();
        }
        return arena;
    }

    public final void loadRandomArena() {
        if (arenas.size() == 1) {
            arena = arenas.get(0);
            return;
        }
        arena = arenas.get(ThreadLocalRandom.current().nextInt(arenas.size() - 1));
    }
}