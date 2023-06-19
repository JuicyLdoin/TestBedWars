package net.ldoin.bedwars.scoreboard;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.GameManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ScoreboardManager {

    private final BedWarsPlugin plugin;
    private final GameManager gameManager;
    private final List<Player> displayed;
    private int task;

    public ScoreboardManager(BedWarsPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        displayed = new ArrayList<>();
        task = -1;
    }

    public final void start() {
        task = new BukkitRunnable() {
            public void run() {
                List<String> lines = getLines();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerConnection playerConnection = getConnection(player);
                    int index = 0;
                    for (String line : lines) {
                        if (displayed.contains(player)) {
                            sendScore(player, line, index);
                        } else {
                            displayed.add(player);
                            playerConnection.sendPacket(createObjectivePacket(player, 0, "bedboard"));
                            playerConnection.sendPacket(setObjectiveSlot(player));
                        }
                        index++;
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20).getTaskId();
    }

    public final void stop() {
        if (task == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(task);
    }

    public final void clear(Player player) {
        getConnection(player).sendPacket(createObjectivePacket(player, 1, null));
        displayed.remove(player);
    }

    public final void clear() {
        Bukkit.getOnlinePlayers().forEach(this::clear);
    }

    private PlayerConnection getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    private List<String> getLines() {
        return Collections.unmodifiableList(gameManager.getGame().getTeams().stream()
                .map(gameTeam -> gameTeam.getArenaTeam().getDisplayName() + " - " + gameTeam.getPlayers().size() +
                        (gameTeam.isHasBed() ? "§a✔" : "§cX"))
                .collect(Collectors.toList()));
    }

    private PacketPlayOutScoreboardObjective createObjectivePacket(Player player, int mode, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", player.getName());
        setField(packet, "d", mode);
        if (mode == 0 || mode == 2) {
            setField(packet, "b", displayName);
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }
        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot(Player player) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        setField(packet, "a", 1);
        setField(packet, "b", player.getName());
        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(Player player, String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setField(packet, "b", player.getName());
        setField(packet, "c", score);
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        return packet;
    }

    private void setField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}