package net.ldoin.bedwars.game;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.waiting.Waiting;
import net.ldoin.bedwars.scoreboard.ScoreboardManager;

public final class GameManager {

    private final Waiting waiting;
    private final ScoreboardManager scoreboardManager;
    private GameState gameState;
    private Game game;

    public GameManager(BedWarsPlugin plugin) {
        waiting = new Waiting(plugin, this, plugin.getArenaManager().getArena());
        waiting.startCount();
        scoreboardManager = new ScoreboardManager(plugin, this);
        gameState = GameState.WAITING;
    }

    public final Waiting getWaiting() {
        return waiting;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public final GameState getGameState() {
        return gameState;
    }

    public final Game getGame() {
        return game;
    }

    public final void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public final void setGame(Game game) {
        this.game = game;
    }
}