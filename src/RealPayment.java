// Import necessary packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import HostelManagement.*;

// Actual object that the proxy represents, handling real payment processing
public class RealPayment implements Payment {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Overridden method to make a payment for a student
    @Override
    public void makePayment(String studentEmail) {
        // Establish connection to the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to update the payment status for a student based on email
            String query = "UPDATE students SET payment = 'paid' WHERE email = ?";
            // Prepare the SQL statement
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Set the email parameter in the query
                pstmt.setString(1, studentEmail);
                // Execute the update statement
                int rowsUpdated = pstmt.executeUpdate();
                // Check if the update was successful
                if (rowsUpdated > 0) {
                    // Show success message if payment was updated
                    JOptionPane.showMessageDialog(null, "Payment of NU.1000 successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show error message if the student was not found
                    JOptionPane.showMessageDialog(null, "Payment failed: student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions and show error message
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
