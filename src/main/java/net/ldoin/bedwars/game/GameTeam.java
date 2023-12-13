package net.ldoin.bedwars.game;

import net.ldoin.bedwars.game.arena.team.ArenaTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTeam {

    private final ArenaTeam arenaTeam;
    private final List<Player> players;
    private boolean hasBed;

    public GameTeam(ArenaTeam arenaTeam) {
        this.arenaTeam = arenaTeam;
        players = new ArrayList<>();
        hasBed = true;
    }

    public ArenaTeam getArenaTeam() {
        return arenaTeam;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public boolean isHasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}