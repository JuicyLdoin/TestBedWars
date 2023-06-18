package net.ldoin.bedwars.user;

public final class UserData {

    private final String name;
    private int wins;
    private int loses;
    private int kills;
    private int deaths;
    private int brokenBeds;

    public UserData(String name) {
        this.name = name;
        wins = 0;
        loses = 0;
        kills = 0;
        deaths = 0;
        brokenBeds = 0;
    }

    public UserData(String name, int wins, int loses, int kills, int deaths, int brokenBeds) {
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.kills = kills;
        this.deaths = deaths;
        this.brokenBeds = brokenBeds;
    }

    public final String getName() {
        return name;
    }

    public final int getWins() {
        return wins;
    }

    public final int getLoses() {
        return loses;
    }

    public final int getGames() {
        return wins + loses;
    }

    public final int getKills() {
        return kills;
    }

    public final int getDeaths() {
        return deaths;
    }

    public final int getBrokenBeds() {
        return brokenBeds;
    }

    public final void addWin() {
        wins++;
    }

    public final void addLose() {
        loses++;
    }

    public final void addKill() {
        kills++;
    }

    public final void addDeath() {
        deaths++;
    }

    public final void addBrokenBed() {
        brokenBeds++;
    }
}