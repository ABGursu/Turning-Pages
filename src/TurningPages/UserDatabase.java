package TurningPages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static final String DB_URL = "jdbc:sqlite:Users.db";
    
    public static void initializeDatabase() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                // Create users table with read_only column
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "read_only INTEGER DEFAULT 0)";  // Added read_only column
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    
                    // Check if read_only column exists, if not add it
                    DatabaseMetaData md = conn.getMetaData();
                    ResultSet rs = md.getColumns(null, null, "users", "read_only");
                    if (!rs.next()) {
                        System.out.println("Adding read_only column to users table");
                        stmt.execute("ALTER TABLE users ADD COLUMN read_only INTEGER DEFAULT 0");
                    }
                    
                    // Insert admin user if not exists
                    String checkAdmin = "SELECT * FROM users WHERE username = 'Admin'";
                    rs = stmt.executeQuery(checkAdmin);
                    if (!rs.next()) {
                        String insertAdmin = "INSERT INTO users (username, password, read_only) VALUES ('Admin', '123.321.', 0)";
                        stmt.execute(insertAdmin);
                        System.out.println("Admin user created successfully");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static boolean validateUser(String username, String password) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    
                    try (ResultSet rs = pstmt.executeQuery()) {
                        boolean isValid = rs.next();
                        System.out.println("Login attempt - Username: " + username + ", Valid: " + isValid);
                        return isValid;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Login validation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean createUser(String username, String password) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                    System.out.println("User created successfully: " + username);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("User creation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                // First verify the old password
                if (!validateUser(username, oldPassword)) {
                    System.out.println("Old password verification failed");
                    return false;
                }
                
                String sql = "UPDATE users SET password = ? WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, newPassword);
                    pstmt.setString(2, username);
                    int affected = pstmt.executeUpdate();
                    System.out.println("Password updated successfully for user: " + username);
                    return affected > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Password update error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<UserPrivilegeInfo> getAllUsersExceptAdmin() {
        List<UserPrivilegeInfo> users = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                // Debug: Print database path and connection status
                System.out.println("Database URL: " + DB_URL);
                System.out.println("Database connection successful: " + !conn.isClosed());

                // Simple query to list all users first
                try (Statement debugStmt = conn.createStatement();
                     ResultSet debugRs = debugStmt.executeQuery("SELECT username FROM users")) {
                    System.out.println("All users in database:");
                    while (debugRs.next()) {
                        System.out.println("- " + debugRs.getString("username"));
                    }
                }

                // Main query to get users except admin
                String sql = "SELECT username, COALESCE(read_only, 0) as read_only " +
                           "FROM users " +
                           "WHERE LOWER(username) != LOWER('Admin')";
                
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    
                    while (rs.next()) {
                        String username = rs.getString("username");
                        boolean readOnly = rs.getInt("read_only") == 1;
                        System.out.println("Adding user to list: " + username + " (Read-only: " + readOnly + ")");
                        users.add(new UserPrivilegeInfo(username, readOnly));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting users list: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Final list size: " + users.size() + " users");
        return users;
    }

    public static boolean setUserReadOnly(String username, boolean readOnly) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "UPDATE users SET read_only = ? WHERE username = ? AND LOWER(username) != LOWER('Admin')";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, readOnly ? 1 : 0);  // Convert boolean to INTEGER (0 or 1)
                    pstmt.setString(2, username);
                    
                    int affected = pstmt.executeUpdate();
                    System.out.println("Toggle read-only update affected " + affected + " rows for user: " + username); // Debug
                    
                    if (affected > 0) {
                        System.out.println("Successfully set read-only=" + readOnly + " for user: " + username);
                        return true;
                    } else {
                        System.out.println("No rows updated for user: " + username);
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error setting user read-only status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(String username) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "DELETE FROM users WHERE username = ? AND LOWER(username) != LOWER('Admin')";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    int affected = pstmt.executeUpdate();
                    System.out.println("Deleted user " + username + ": " + affected + " rows affected");
                    return affected > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAllUsers() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "DELETE FROM users WHERE LOWER(username) != LOWER('Admin')";
                try (Statement stmt = conn.createStatement()) {
                    int affected = stmt.executeUpdate(sql);
                    System.out.println("Deleted all users: " + affected + " rows affected");
                    return affected > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error deleting all users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUserReadOnly(String username) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT read_only FROM users WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("read_only") == 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking read-only status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;  // Default to false if any error occurs or user not found
    }

    public static String getUserPassword(String username) {
        String password = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            String sql = "SELECT password FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving password: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return password;
    }
} 