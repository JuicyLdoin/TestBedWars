package net.ldoin.bedwars.game;

import net.ldoin.bedwars.BedWarsPlugin;
import net.ldoin.bedwars.game.waiting.Waiting;
import net.ldoin.bedwars.scoreboard.ScoreboardManager;

public class GameManager {

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

    public Waiting getWaiting() {
        return waiting;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}