package net.ldoin.bedwars.game;

import net.ldoin.bedwars.game.arena.team.ArenaTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameTeam {

    private final ArenaTeam arenaTeam;
    private final List<Player> players;
    private boolean hasBed;

    public GameTeam(ArenaTeam arenaTeam) {
        this.arenaTeam = arenaTeam;
        players = new ArrayList<>();
        hasBed = true;
    }

    public final ArenaTeam getArenaTeam() {
        return arenaTeam;
    }

    public final List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public final boolean isHasBed() {
        return hasBed;
    }

    public final void addPlayer(Player player) {
        players.add(player);
    }

    public final void removePlayer(Player player) {
        players.remove(player);
    }

    public final void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }
}