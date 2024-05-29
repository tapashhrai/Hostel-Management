// Import necessary libraries
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import HostelManagement.*;

public class AdminHomePage extends JFrame {

    // Database connection constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Constructor for AdminHomePage
    public AdminHomePage(String email) {
        setTitle("Admin Home Page"); // Set the title of the frame
        setSize(600, 400); // Set the size of the frame
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Define the default close operation

        // Create and set up the main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Retrieve the real name of the admin using their email
        String realName = getRealName(email);

        // Welcome label displaying the admin's name
        JLabel welcomeLabel = new JLabel("Welcome back, " + realName + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Create the panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        
        // Define buttons for various functionalities
        JButton addHostelButton = new JButton("Add Hostel");
        JButton addRoomButton = new JButton("Add Room");
        JButton yearAllocationButton = new JButton("Allocate by Year");
        JButton viewAllocationsButton = new JButton("View Allocations");
        JButton viewStructureButton = new JButton("View Structure");
        JButton suspendStudentButton = new JButton("Suspend Student");
        JButton viewPaymentButton = new JButton("View Payment");
        JButton viewFeedbackButton = new JButton("View Feedback");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the button panel
        buttonPanel.add(addHostelButton);
        buttonPanel.add(addRoomButton);
        buttonPanel.add(yearAllocationButton);
        buttonPanel.add(viewAllocationsButton);
        buttonPanel.add(viewStructureButton);
        buttonPanel.add(suspendStudentButton);
        buttonPanel.add(viewPaymentButton);
        buttonPanel.add(viewFeedbackButton);
        buttonPanel.add(logoutButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for the "Add Hostel" button
        addHostelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormFactory hostelFactory = new HostelFormFactory();
                JFrame hostelForm = hostelFactory.createForm();
                hostelForm.setVisible(true);
            }
        });

        // Action listener for the "View Payment" button
        viewPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPayments();
            }
        });

        // Action listener for the "Add Room" button
        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormFactory roomFactory = new RoomFormFactory();
                JFrame roomForm = roomFactory.createForm();
                roomForm.setVisible(true);
            }
        });

        // Action listener for the "Allocate by Year" button
        yearAllocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoomAllocator allocator = new RoomAllocator();
                allocator.setStrategy(new YearWiseAllocationStrategy());
                allocator.allocate();
                JOptionPane.showMessageDialog(AdminHomePage.this, "Rooms allocated by year successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Action listener for the "View Allocations" button
        viewAllocationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRoomAllocations();
            }
        });

        // Action listener for the "View Structure" button
        viewStructureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStructure();
            }
        });

        // Action listener for the "Suspend Student" button
        suspendStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enrollmentNumber = JOptionPane.showInputDialog("Enter enrollment number to suspend:");
                if (enrollmentNumber != null && !enrollmentNumber.trim().isEmpty()) {
                    if (isEnrollmentNumberValid(enrollmentNumber)) {
                        suspendStudent(enrollmentNumber);
                    } else {
                        JOptionPane.showMessageDialog(AdminHomePage.this, "Invalid enrollment number!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminHomePage.this, "Enrollment number cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener for the "View Feedback" button
        viewFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewFeedback();
            }
        });

        // Action listener for the "Logout" button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });

        // Add the main panel to the frame
        add(panel);
    }

    // Method to retrieve the real name of the admin based on email
    private String getRealName(String email) {
        String username = "User"; // Default value
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username FROM admin WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    } else {
                        System.out.println("User not found for email: " + email);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return username;
    }

    // Method to view feedback from students
    private void viewFeedback() {
        JFrame feedbackFrame = new JFrame("Student Feedback");
        feedbackFrame.setSize(800, 400);
        feedbackFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        String[] columnNames = {"Enrollment Number", "Username", "Year", "Hostel Name", "Room Number", "Feedback"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
    
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        feedbackFrame.add(scrollPane);
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT s.enrollmentNumber, s.username, s.year, h.hostel_name, r.room_number, s.feedback " +
                           "FROM students s " +
                           "JOIN rooms r ON s.room_id = r.room_id " +
                           "JOIN hostels h ON r.hostel_id = h.hostel_id";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
    
                while (rs.next()) {
                    String enrollmentNumber = rs.getString("enrollmentNumber");
                    String username = rs.getString("username");
                    String year = rs.getString("year");
                    String hostelName = rs.getString("hostel_name");
                    String roomNumber = rs.getString("room_number");
                    String feedback = rs.getString("feedback");
    
                    model.addRow(new Object[]{enrollmentNumber, username, year, hostelName, roomNumber, feedback});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        feedbackFrame.setVisible(true);
    }

    // Method to check if the enrollment number is valid
    private boolean isEnrollmentNumberValid(String enrollmentNumber) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT enrollmentNumber FROM students WHERE enrollmentNumber = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, enrollmentNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Method to suspend a student based on their enrollment number
    private void suspendStudent(String enrollmentNumber) {
        StudentContext student = new StudentContext(enrollmentNumber);
        student.setState(new SuspendedState());
        student.applyState();
        JOptionPane.showMessageDialog(this, "Student with enrollment number " + enrollmentNumber + " has been suspended.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to view payments made by students
    private void viewPayments() {
        JFrame paymentFrame = new JFrame("Student Payments");
        paymentFrame.setSize(800, 400);
        paymentFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnNames = {"Username", "Enrollment Number", "Email", "Year", "Payment"};
        DefaultTableModel model = new DefaultTableModel();
                // Set column names for the payment table
                model.setColumnIdentifiers(columnNames);

                // Create a table to display the payments and add it to a scroll pane
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                paymentFrame.add(scrollPane);
        
                // Retrieve payment data from the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT username, enrollmentNumber, email, year, payment FROM students";
                    try (PreparedStatement pstmt = conn.prepareStatement(query);
                         ResultSet rs = pstmt.executeQuery()) {
        
                        // Populate the table model with data from the result set
                        while (rs.next()) {
                            String username = rs.getString("username");
                            String enrollmentNumber = rs.getString("enrollmentNumber");
                            String email = rs.getString("email");
                            String year = rs.getString("year");
                            String payment = rs.getString("payment");
        
                            model.addRow(new Object[]{username, enrollmentNumber, email, year, payment});
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
        
                // Display the payment frame
                paymentFrame.setVisible(true);
            }
        
            // Method to view room allocations
            private void viewRoomAllocations() {
                JFrame frame = new JFrame("Room Allocations");
                frame.setSize(800, 400);
                frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
                // Set column names for the room allocations table
                String[] columnNames = {"Student ID", "Username", "Gender", "Hostel Name", "Year", "Room ID", "Room Number", "State"};
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(columnNames);
            
                // Create a table to display room allocations and add it to a scroll pane
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane);
            
                // Retrieve room allocation data from the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT s.enrollmentNumber, s.username, s.gender, h.hostel_name, s.year, r.room_id, r.room_number, s.state " +
                                   "FROM students s " +
                                   "JOIN rooms r ON s.room_id = r.room_id " +
                                   "JOIN hostels h ON r.hostel_id = h.hostel_id";
                    try (PreparedStatement pstmt = conn.prepareStatement(query);
                         ResultSet rs = pstmt.executeQuery()) {
            
                        // Populate the table model with data from the result set
                        while (rs.next()) {
                            int id = rs.getInt("enrollmentNumber");
                            String username = rs.getString("username");
                            String gender = rs.getString("gender");
                            String hostelName = rs.getString("hostel_name");
                            String year = rs.getString("year");
                            int roomId = rs.getInt("room_id");
                            int roomNumber = rs.getInt("room_number");
                            String state = rs.getString("state");
            
                            model.addRow(new Object[]{id, username, gender, hostelName, year, roomId, roomNumber, state});
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            
                // Display the room allocations frame
                frame.setVisible(true);
            }
        
            // Method to view the structure of the hostel including rooms
            private void viewStructure() {
                List<HostelComponent> hostels = new ArrayList<>();
            
                // Retrieve the hostel and room data from the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String hostelQuery = "SELECT hostel_id, hostel_name FROM hostels";
                    try (PreparedStatement hostelStmt = conn.prepareStatement(hostelQuery);
                         ResultSet hostelRs = hostelStmt.executeQuery()) {
            
                        // For each hostel, retrieve its rooms
                        while (hostelRs.next()) {
                            int hostelId = hostelRs.getInt("hostel_id");
                            String hostelName = hostelRs.getString("hostel_name");
            
                            List<HostelComponent> rooms = new ArrayList<>();
                            String roomQuery = "SELECT room_id, room_number, capacity, state FROM rooms WHERE hostel_id = ?";
                            try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
                                roomStmt.setInt(1, hostelId);
                                try (ResultSet roomRs = roomStmt.executeQuery()) {
                                    // Create Room objects and set their states based on the database values
                                    while (roomRs.next()) {
                                        int roomId = roomRs.getInt("room_id");
                                        String roomNumber = roomRs.getString("room_number");
                                        int capacity = roomRs.getInt("capacity");
                                        String state = roomRs.getString("state");
            
                                        Room room = new Room(roomId, roomNumber, capacity);
                                        if ("occupied".equals(state)) {
                                            room.setState(new OccupiedState());
                                        } else {
                                            room.setState(new VacantState());
                                        }
                                        rooms.add(room);
                                    }
                                }
                            }
                            hostels.add(new Hostel(hostelId, hostelName, rooms));
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            
                // Display the hostel structure in a text area
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
                StringBuilder builder = new StringBuilder();
                for (HostelComponent hostel : hostels) {
                    hostel.display(builder, "");
                }
                textArea.setText(builder.toString());
            
                // Create a frame to display the hostel structure and add the text area to it
                JFrame frame = new JFrame("Hostel Structure");
                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                frame.add(new JScrollPane(textArea));
                frame.setVisible(true);
            }
            
            // Main method to launch the admin home page
            public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> new AdminHomePage("example@example.com").setVisible(true));
            }
        }
        
