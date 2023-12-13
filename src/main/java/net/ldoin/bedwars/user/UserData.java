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

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getGames() {
        return wins + loses;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBrokenBeds() {
        return brokenBeds;
    }

    public void addWin() {
        wins++;
    }

    public void addLose() {
        loses++;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addBrokenBed() {
        brokenBeds++;
    }
}