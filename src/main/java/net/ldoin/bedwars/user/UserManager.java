package net.ldoin.bedwars.user;

import net.ldoin.bedwars.database.DatabaseConnection;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserManager {

    private final Logger logger;
    private final DatabaseConnection databaseConnection;
    private final Map<UUID, UserData> users;

    public UserManager(Logger logger, DatabaseConnection databaseConnection) {
        this.logger = logger;
        this.databaseConnection = databaseConnection;
        users = new HashMap<>();
    }

    public final UserData getUser(Player player) {
        UUID uuid = player.getUniqueId();
        if (!users.containsKey(uuid))
            return loadUser(uuid, player.getName());
        return users.get(uuid);
    }

    public final UserData loadUser(UUID uuid, String name) {
        UserData userData = new UserData(name);
        try {
            ResultSet resultSet = databaseConnection.prepareStatement("SELECT * FROM `bedwars` WHERE uuid=?", uuid.toString())
                    .get(10, TimeUnit.SECONDS);
            if (!resultSet.next()) {
                createUser(uuid, name);
            } else {
                userData = new UserData(
                        name,
                        resultSet.getInt("wins"),
                        resultSet.getInt("loses"),
                        resultSet.getInt("kills"),
                        resultSet.getInt("deaths"),
                        resultSet.getInt("brokenBeds")
                );
            }
        } catch (InterruptedException | ExecutionException | TimeoutException exception) {
            logger.log(Level.WARNING, String.format("Cannot load user %s with uuid %s", name, uuid), exception);
        } catch (SQLException exception) {
            logger.log(Level.WARNING, "Error on invoke SQL query", exception);
        }
        users.put(uuid, userData);
        return userData;
    }

    private void createUser(UUID uuid, String name) throws ExecutionException, InterruptedException {
        databaseConnection.prepareStatementUpdate("INSERT INTO `bedwars` VALUES(?,?,?,?,?,?,?)",
                uuid.toString(), name, 0, 0, 0, 0, 0);
    }

    public final void saveUser(Player player) {
        UserData userData = getUser(player);
        databaseConnection.prepareStatementUpdate("UPDATE `bedwars` SET wins=?, loses=?, kills=?, deaths=?, brokenBeds=? WHERE uuid=?",
                userData.getWins(), userData.getLoses(),
                userData.getKills(), userData.getDeaths(),
                userData.getBrokenBeds(),
                player.getUniqueId().toString()
        );
    }

    public final void unloadUser(Player player) {
        saveUser(player);
        users.remove(player.getUniqueId());
    }
}