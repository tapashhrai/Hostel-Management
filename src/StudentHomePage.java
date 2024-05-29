import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import HostelManagement.*;

public class StudentHomePage extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private String studentEmail;
    private Mediator mediator;

    public StudentHomePage(String email) {
        this.studentEmail = email;
        this.mediator = new FeedbackMediator();
        setTitle("Student Home Page");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String realName = getRealName(email);

        JLabel welcomeLabel = new JLabel("Welcome back, " + realName + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 20, 20));
        
        JButton profileButton = new JButton("View Profile");
        profileButton.addActionListener(e -> viewProfile());

        JButton viewRoomButton = new JButton("View Room");
        viewRoomButton.addActionListener(e -> viewRoom());

        JButton paymentButton = new JButton("Payment");
        paymentButton.addActionListener(e -> makePayment());

        JButton feedbackButton = new JButton("Send Feedback");
        feedbackButton.addActionListener(e -> sendFeedback());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        buttonPanel.add(profileButton);
        buttonPanel.add(viewRoomButton);
        buttonPanel.add(paymentButton);
        buttonPanel.add(feedbackButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
    }

    private String getRealName(String email) {
        String username = "User"; // Default value
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username FROM students WHERE email = ?";
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

    private void viewProfile() {
        JFrame profileFrame = new JFrame("Student Profile");
        profileFrame.setSize(400, 350);
        profileFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username, enrollmentNumber, email, gender, year, state FROM students WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, studentEmail);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        panel.add(new JLabel("Username:"));
                        panel.add(new JLabel(rs.getString("username")));

                        panel.add(new JLabel("Enrollment Number:"));
                        panel.add(new JLabel(rs.getString("enrollmentNumber")));

                        panel.add(new JLabel("Email:"));
                        panel.add(new JLabel(rs.getString("email")));

                        panel.add(new JLabel("Gender:"));
                        panel.add(new JLabel(rs.getString("gender")));

                        panel.add(new JLabel("Year:"));
                        panel.add(new JLabel(rs.getString("year")));
                        
                        panel.add(new JLabel("State:"));
                        panel.add(new JLabel(rs.getString("state")));
                    } else {
                        System.out.println("Profile not found for email: " + studentEmail);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        profileFrame.add(panel);
        profileFrame.setVisible(true);
    }

    private void viewRoom() {
        JFrame roomFrame = new JFrame("Room Information");
        roomFrame.setSize(400, 350);
        roomFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT h.hostel_name, h.hostel_location, r.room_id, r.room_number " +
                           "FROM students s " +
                           "JOIN rooms r ON s.room_id = r.room_id " +
                           "JOIN hostels h ON r.hostel_id = h.hostel_id " +
                           "WHERE s.email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, studentEmail);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        panel.add(new JLabel("Hostel Name:"));
                        panel.add(new JLabel(rs.getString("hostel_name")));

                        panel.add(new JLabel("Hostel Location:"));
                        panel.add(new JLabel(rs.getString("hostel_location")));

                        panel.add(new JLabel("Room ID:"));
                        panel.add(new JLabel(rs.getString("room_id")));

                        panel.add(new JLabel("Room Number:"));
                        panel.add(new JLabel(rs.getString("room_number")));
                    } else {
                        System.out.println("Room not found for email: " + studentEmail);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        roomFrame.add(panel);
        roomFrame.setVisible(true);
    }

    private void makePayment() {
        Payment paymentProxy = new PaymentProxy();
        paymentProxy.makePayment(studentEmail);
    }

    private void sendFeedback() {
        String feedback = JOptionPane.showInputDialog(this, "Enter your feedback:");
        if (feedback != null && !feedback.trim().isEmpty()) {
            mediator.sendFeedback(studentEmail, feedback);
            JOptionPane.showMessageDialog(this, "Feedback sent successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Feedback cannot be empty.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentHomePage("example@example.com").setVisible(true));
    }
}


