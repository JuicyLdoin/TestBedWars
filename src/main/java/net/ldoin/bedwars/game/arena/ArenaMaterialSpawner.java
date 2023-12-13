package net.ldoin.bedwars.game.arena;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.util.ItemUtil;
import net.ldoin.bedwars.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaMaterialSpawner {

    private final int delay;
    private final Location location;
    private final ItemStack itemStack;
    private int taskId;

    public ArenaMaterialSpawner(int delay, Location location, ItemStack itemStack) {
        this.delay = delay;
        this.location = location;
        this.itemStack = itemStack;
        taskId = -1;
    }

    public ArenaMaterialSpawner(ConfigurationSection configurationSection) {
        this(
                configurationSection.getInt("delay", 20),
                LocationUtil.getLocation(configurationSection.getString("location", "world 0 0 0")),
                ItemUtil.buildItemFromConfigSection(configurationSection)
        );
    }

    public void start(BedWarsPlugin plugin) {
        taskId = new BukkitRunnable() {
            public void run() {
                location.getWorld().dropItemNaturally(location, itemStack);
            }
        }.runTaskTimer(plugin, 0, delay).getTaskId();
    }

    public void stop() {
        if (taskId == -1)
            return;

        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }
}