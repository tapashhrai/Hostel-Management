import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class AuthenticationManager {
    // Singleton instance of the AuthenticationManager
    private static AuthenticationManager instance;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Private constructor to prevent instantiation
    private AuthenticationManager() {
    }

    // Method to get the singleton instance of AuthenticationManager
    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    // Method to authenticate a user by email and password
    public String authenticate(String email, String password) {
        // Hash the password using SHA-256
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return "Error hashing password";
        }

        // Connect to the database and verify credentials
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL queries to check for students and admins
            String studentQuery = "SELECT * FROM students WHERE email=? AND hashedpassword=?";
            String adminQuery = "SELECT * FROM admin WHERE email=? AND hashedpassword=?";
            try (PreparedStatement studentPstmt = conn.prepareStatement(studentQuery);
                 PreparedStatement adminPstmt = conn.prepareStatement(adminQuery)) {

                // Set parameters for the prepared statements
                studentPstmt.setString(1, email);
                studentPstmt.setString(2, hashedPassword);

                adminPstmt.setString(1, email);
                adminPstmt.setString(2, hashedPassword);

                // Execute the queries and check results
                try (ResultSet studentRs = studentPstmt.executeQuery();
                     ResultSet adminRs = adminPstmt.executeQuery()) {

                    // If a student is found, return "Student"
                    if (studentRs.next()) {
                        return "Student";
                    // If an admin is found, return "Admin"
                    } else if (adminRs.next()) {
                        return "Admin";
                    // If no user is found, return an error message
                    } else {
                        return "Invalid email or password";
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    // Method to hash a password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
