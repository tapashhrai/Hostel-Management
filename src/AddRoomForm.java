// Import necessary packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Class representing a form for adding a room
public class AddRoomForm extends JFrame {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Constructor to initialize the form
    public AddRoomForm() {
        // Set frame properties
        setTitle("Add Room");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create panel with grid layout
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add hostel label and combo box to select hostel
        JLabel hostelLabel = new JLabel("Select Hostel:");
        JComboBox<String> hostelComboBox = new JComboBox<>();
        panel.add(hostelLabel);
        panel.add(hostelComboBox);

        // Add room number label and text field
        JLabel roomNumberLabel = new JLabel("Room Number:");
        JTextField roomNumberField = new JTextField();
        panel.add(roomNumberLabel);
        panel.add(roomNumberField);

        // Add capacity label and text field
        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField();
        panel.add(capacityLabel);
        panel.add(capacityField);

        // Add button to add room
        JButton addButton = new JButton("Add Room");
        panel.add(addButton);

        // Populate hostel combo box with hostel names
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT hostel_id, hostel_name FROM hostels";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    hostelComboBox.addItem(rs.getInt("hostel_id") + ": " + rs.getString("hostel_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(AddRoomForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Action listener for the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve selected hostel, room number, and capacity from input fields
                String selectedHostel = (String) hostelComboBox.getSelectedItem();
                if (selectedHostel == null || roomNumberField.getText().isEmpty() || capacityField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(AddRoomForm.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int hostelId = Integer.parseInt(selectedHostel.split(":")[0]);
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                int capacity = Integer.parseInt(capacityField.getText());

                // Attempt to add room to the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "INSERT INTO rooms (hostel_id, room_number, capacity) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, hostelId);
                        pstmt.setInt(2, roomNumber);
                        pstmt.setInt(3, capacity);
                        pstmt.executeUpdate();
                        // Show success message and close the form
                        JOptionPane.showMessageDialog(AddRoomForm.this, "Room added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } catch (SQLException ex) {
                    // Handle SQL exceptions and show error message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AddRoomForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add panel to the frame
        add(panel);
    }
}
