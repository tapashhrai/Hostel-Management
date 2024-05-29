// Import necessary packages for SQL and HostelManagement
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import HostelManagement.*;

// Implementation of the Mediator interface to handle feedback operations
public class FeedbackMediator implements Mediator {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Overridden method to send feedback for a specific student by email
    @Override
    public void sendFeedback(String email, String feedback) {
        // Establish connection to the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to update feedback for a student based on email
            String query = "UPDATE students SET feedback = ? WHERE email = ?";
            // Prepare the SQL statement
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Set the feedback and email parameters in the query
                pstmt.setString(1, feedback);
                pstmt.setString(2, email);
                // Execute the update statement
                int rowsUpdated = pstmt.executeUpdate();
                // Check if the update was successful
                if (rowsUpdated > 0) {
                    System.out.println("Feedback updated successfully for email: " + email);
                } else {
                    System.out.println("Failed to update feedback for email: " + email);
                }
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            ex.printStackTrace();
        }
    }
}
