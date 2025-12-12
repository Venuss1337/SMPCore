package com.venuss.smpcore.database;

import com.venuss.smpcore.exceptions.DEException;
import com.venuss.smpcore.exceptions.DEExceptionType;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseRepository {

    private final Map<String, String> requiredTables = new LinkedHashMap<>();

    private final DSLContext dsl;
    private final HikariDataSource dataSource;

    public DatabaseRepository() {
        dataSource = DatabaseConnection.connect();
        if (dataSource.isClosed()) {
            throw new DEException(DEExceptionType.FAILED_TO_CONNECT);
        }

        this.dsl = DSL.using(dataSource,  SQLDialect.MARIADB);

        requiredTables.put("user", "migrations/001_create_user_table.sql");
        requiredTables.put("player_inventory_backup", "migrations/002_create_player_inventory_backup_table.sql");
    }

    public DatabaseRepository initialize() throws DEException {
        try (var connection = dataSource.getConnection()) {
            for (Map.Entry<String, String> entry : requiredTables.entrySet()) {
                String tableName = entry.getKey();
                String sqlPath = entry.getValue();

                if (!tableExists(connection, tableName)) {
                    try (InputStream in = getClass().getClassLoader().getResourceAsStream(sqlPath)) {
                        if (in == null) {
                            throw new DEException(DEExceptionType.MIGRATIONS_NOT_FOUND);
                        }

                        String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                        dsl.execute(sql);

                        if (!tableExists(connection, tableName)) {
                            throw new DEException(DEExceptionType.FAILED_TO_CREATE_TABLES);
                        }
                    }
                }
            }
        } catch(SQLException | IOException e) {
            throw new DEException(DEExceptionType.UNEXPECTED_EXCEPTION);
        }

        return this;
    }

    private boolean tableExists(Connection connection, String tableName) throws DEException {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "smpcore");
            preparedStatement.setString(2, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch(SQLException e) {
            throw new DEException(DEExceptionType.UNEXPECTED_EXCEPTION);
        }
    }

    public void shutdown() {
        dataSource.close();
    }

    public DSLContext getContext() {
        return dsl;
    }
}
