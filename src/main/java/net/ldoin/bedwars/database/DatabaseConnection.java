package net.ldoin.bedwars.database;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class DatabaseConnection {

    private final Connection connection;

    private final ExecutorService executor;

    public DatabaseConnection(DatabaseData dataBaseData, int threads) throws SQLException {
        connection = DriverManager.getConnection(dataBaseData.getConnectionString(), dataBaseData.getUser(), dataBaseData.getPassword());
        executor = Executors.newFixedThreadPool(threads);
    }

    public final void prepareStatementUpdate(String query, Object... objects) {
        executor.submit(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                return statement.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public final Future<ResultSet> prepareStatement(String query, Object... objects) {
        return executor.submit(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                return statement.executeQuery();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public final void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}