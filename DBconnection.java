package TimeCapsuleMessagingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/TIME_CAPSULE";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2005";

    static {
        try {
            // Optional: explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found!");
        }
    }

    public static Connection getconnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return null; // Avoid throwing unchecked exceptions unless necessary
        }
    }
}
