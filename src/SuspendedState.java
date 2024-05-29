// Import necessary package
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import HostelManagement.*;

// Implementation of the StudentState interface representing a suspended state of a student
public class SuspendedState implements StudentState {
    
    // Overridden method to handle the suspended state of the student
    @Override
    public void handleState(StudentContext context) {
        // Print a message indicating the student is currently suspended
        System.out.println("Student is currently suspended.");
        // Update the database state to reflect the suspension
        updateDatabaseState(context.getEnrollmentNumber(), "suspended");
    }

    // Private helper method to update the database state for the student
    private void updateDatabaseState(String enrollmentNumber, String newState) {
        // Database connection details
        String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        
        // Attempt to establish connection to the database and update the state
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to update the student's state based on enrollment number
            String query = "UPDATE students SET state = ? WHERE enrollmentNumber = ?";
            // Prepare the SQL statement
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Set the new state and enrollment number parameters in the query
                pstmt.setString(1, newState);
                pstmt.setString(2, enrollmentNumber);
                // Execute the update statement
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            ex.printStackTrace();
        }
    }
}
