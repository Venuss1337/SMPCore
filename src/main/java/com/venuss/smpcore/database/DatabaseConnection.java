package com.venuss.smpcore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {

    // TODO: REPLACE WITH CONFIG
    private static final String JDBCURL = "jdbc:mariadb://localhost:3306/smpcore";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "testdb";
    private static final String USERNAME = "smpcore";
    private static final String PASSWORD = "smpcore";

    public static HikariDataSource connect() {
        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName(org.mariadb.jdbc.Driver.class.getName());
        cfg.setJdbcUrl(JDBCURL);
        cfg.setUsername(USERNAME);
        cfg.setPassword(PASSWORD);

        // Pool tuning
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30_000);
        cfg.setLeakDetectionThreshold(10_000);

        // more tuning
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(cfg);
    }
}
