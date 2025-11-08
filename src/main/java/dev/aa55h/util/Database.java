package dev.aa55h.util;

import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static Connection connection;

    public static void connect() {
        try {
            if (connection != null) return;

            connection = DriverManager.getConnection(Environment.POSTGRES_DB);
            log.info("🔌 Connected to PostgreSQL!");
        } catch (SQLException e) {
            log.error("Error while connecting to PostgreSQL", e);
            return;
        }
        initialize();
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("🔌 Disconnected from PostgreSQL!");
            }
        } catch (SQLException e) {
            log.error("Error while disconnecting from PostgreSQL", e);
        }
    }

    private static void initialize() {
        try {
            connection.prepareStatement("""
                        create table if not exists members(
                            id serial primary key,
                            discord_id varchar(20) not null unique,
                            email varchar(255) not null unique,
                            code varchar(10) not null,
                            verified_at timestamp not null default current_timestamp
                        )
                    """).execute();
            log.info("✅ Database initialized successfully");
        } catch (SQLException e) {
            log.error("Error while initializing database", e);
        }
    }
    
    public static void addUser(String id, String email, String code) {
        try (var ps = getConnection().prepareStatement("insert into members(discord_id, email, code) values (?, ?, ?)")) {;
            ps.setString(1, id);
            ps.setString(2, email);
            ps.setString(3, code);
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while adding user to database", e);
        }
    }
    
    public static boolean exists(String id) {
        try (var ps = getConnection().prepareStatement("select 1 from members where discord_id = ?")) {
            ps.setString(1, id);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            log.error("Error while checking if user exists in database", e);
        }
        return false;
    }
    
    public static void removeUser(String id) {
        try (var ps = getConnection().prepareStatement("delete from members where discord_id = ?")) {
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while removing user from database", e);
        }
    }
    
    public static List<String> getAll() {
        try (var ps = getConnection().prepareStatement("select discord_id from members")) {
            var rs = ps.executeQuery();
            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString("discord_id"));
            }
            return ids;
        } catch (SQLException e) {
            log.error("Error while retrieving all users from database", e);
        }
        return new ArrayList<>();
    }
    
    public static UserEntry getUser(String discordId) {
        try (var ps = getConnection().prepareStatement("select discord_id, email, code, verified_at from members where discord_id = ?")) {
            ps.setString(1, discordId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return new UserEntry(
                        rs.getString("discord_id"),
                        rs.getString("email"),
                        rs.getString("code"),
                        rs.getString("verified_at")
                );
            }
        } catch (SQLException e) {
            log.error("Error while retrieving user from database", e);
        }
        return null;
    }
    
    public record UserEntry(String id, String email, String code, String verifiedAt) {
    }
}