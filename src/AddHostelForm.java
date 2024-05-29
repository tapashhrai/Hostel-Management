// Import necessary packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Class representing a form for adding a hostel
public class AddHostelForm extends JFrame {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Constructor to initialize the form
    public AddHostelForm() {
        // Set frame properties
        setTitle("Add Hostel");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create panel with grid layout
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add hostel name label and text field to the panel
        JLabel nameLabel = new JLabel("Hostel Name:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        // Add hostel location label and text field to the panel
        JLabel locationLabel = new JLabel("Hostel Location:");
        JTextField locationField = new JTextField();
        panel.add(locationLabel);
        panel.add(locationField);

        // Add button to add hostel to the panel
        JButton addButton = new JButton("Add Hostel");
        panel.add(addButton);

        // Action listener for the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve hostel name and location from text fields
                String hostelName = nameField.getText();
                String hostelLocation = locationField.getText();

                // Validate input fields
                if (hostelName.isEmpty() || hostelLocation.isEmpty()) {
                    JOptionPane.showMessageDialog(AddHostelForm.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Attempt to add hostel to the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "INSERT INTO hostels (hostel_name, hostel_location) VALUES (?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, hostelName);
                        pstmt.setString(2, hostelLocation);
                        pstmt.executeUpdate();
                        // Show success message and close the form
                        JOptionPane.showMessageDialog(AddHostelForm.this, "Hostel added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } catch (SQLException ex) {
                    // Handle SQL exceptions and show error message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AddHostelForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add panel to the frame
        add(panel);
    }
}
