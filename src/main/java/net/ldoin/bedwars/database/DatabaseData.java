package net.ldoin.bedwars.database;

public final class DatabaseData {

    private final String user;
    private final String password;
    private final String host;
    private final String port;
    private final String database;

    public DatabaseData(String user, String password, String host, String port, String database) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public final String getUser() {
        return user;
    }

    public final String getPassword() {
        return password;
    }

    public final String getDatabase() {
        return database;
    }

    public String getConnectionString() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&characterEncoding=utf8";
    }

    public final String toString() {
        return "DatabaseData{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}