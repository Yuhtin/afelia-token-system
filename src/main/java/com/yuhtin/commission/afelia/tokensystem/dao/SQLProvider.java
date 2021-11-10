package com.yuhtin.commission.afelia.tokensystem.dao;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.SQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import lombok.Data;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.File;

@Data(staticConstructor = "of")
public final class SQLProvider {

    private final Plugin plugin;

    public SQLConnector setup() {
        val configuration = plugin.getConfig();
        val databaseConfiguration = configuration.getConfigurationSection("database");
        if (databaseConfiguration == null) {
            plugin.getLogger().info("Database section in config is null.");
            return null;
        }

        val sqlType = databaseConfiguration.getString("type", "");
        if (sqlType.equalsIgnoreCase("")) {
            plugin.getLogger().info("Database type in config is null.");
            return null;
        }

        val logger = plugin.getLogger();

        SQLConnector sqlConnector;
        if (sqlType.equalsIgnoreCase("mysql")) {
            ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");
            if (mysqlSection == null) {
                plugin.getLogger().info("MySQL database section in config is null.");
                return null;
            }

            sqlConnector = mysqlDatabaseType(mysqlSection).connect();

            logger.info("Conection with the data bank (MySQL) successfully.");
        } else if (sqlType.equalsIgnoreCase("sqlite")) {
            ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");
            if (sqliteSection == null) {
                plugin.getLogger().info("SQLite database section in config is null.");
                return null;
            }

            sqlConnector = sqliteDatabaseType(sqliteSection).connect();

            logger.info("Conection with the data bank (SQLite) successfully.");
            logger.warning("Recommended use MySQL.");
        } else {
            logger.severe("Database type is invalid.");
            return null;
        }

        return sqlConnector;
    }

    private SQLDatabaseType sqliteDatabaseType(ConfigurationSection section) {
        return SQLiteDatabaseType.builder()
                .file(new File(plugin.getDataFolder(), section.getString("file", "database/database.db")))
                .build();
    }

    private SQLDatabaseType mysqlDatabaseType(ConfigurationSection section) {
        return MySQLDatabaseType.builder()
                .address(section.getString("address"))
                .username(section.getString("username"))
                .password(section.getString("password"))
                .database(section.getString("database"))
                .build();
    }

}
