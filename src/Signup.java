import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import HostelManagement.*;


// Concrete Factory for Student
class StudentFactory extends UserFactory {
    @Override
    public User createUser(String username, String enrollmentNumber, String email, String hashedPassword, String gender, int year) {
        return new Student(username, enrollmentNumber, email, hashedPassword, gender, year);
    }
}

// Concrete Factory for Admin
class AdminFactory extends UserFactory {
    @Override
    public User createUser(String username, String enrollmentNumber, String email, String hashedPassword, String gender, int year) {
        return new Admin(username, enrollmentNumber, email, hashedPassword);
    }
}


// Concrete Product Student
class Student extends User {
    private String gender;
    private int year;

    public Student(String username, String enrollmentNumber, String email, String hashedPassword, String gender, int year) {
        super(username, enrollmentNumber, email, hashedPassword);
        this.gender = gender;
        this.year = year;
    }
}

// Concrete Product Admin
class Admin extends User {
    public Admin(String username, String enrollmentNumber, String email, String hashedPassword) {
        super(username, enrollmentNumber, email, hashedPassword);
    }
}

public class Signup extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private final UserFactory userFactory;
    private JTextField genderField;
    private JTextField yearField;

    public Signup(UserFactory userFactory) {
        this.userFactory = userFactory;
        setTitle("Signup Form");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Username:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel phoneLabel = new JLabel("Enrollment Number:");
        JTextField phoneField = new JTextField();
        panel.add(phoneLabel);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Student", "Admin"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedIndex(0);
        panel.add(roleLabel);
        panel.add(roleComboBox);

        // Gender and Year fields
        JLabel genderLabel = new JLabel("Gender:");
        genderField = new JTextField();
        panel.add(genderLabel);
        panel.add(genderField);

        JLabel yearLabel = new JLabel("Year:");
        yearField = new JTextField();
        panel.add(yearLabel);
        panel.add(yearField);

        // Hide gender and year fields initially if the role is Admin
        if (roleComboBox.getSelectedItem().equals("Admin")) {
            genderLabel.setVisible(false);
            genderField.setVisible(false);
            yearLabel.setVisible(false);
            yearField.setVisible(false);
        }

        roleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roleComboBox.getSelectedItem().equals("Student")) {
                    genderLabel.setVisible(true);
                    genderField.setVisible(true);
                    yearLabel.setVisible(true);
                    yearField.setVisible(true);
                } else {
                    genderLabel.setVisible(false);
                    genderField.setVisible(false);
                    yearLabel.setVisible(false);
                    yearField.setVisible(false);
                }
            }
        });

        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nameField.getText();
                String enrollmentNumber = phoneField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                // Validate input
                if (username.isEmpty() || enrollmentNumber.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(Signup.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!username.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(Signup.this, "Username must contain only letters", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!enrollmentNumber.matches("\\d{8}")) {
                    JOptionPane.showMessageDialog(Signup.this, "Enrollment number must contain exactly 8 digits", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    JOptionPane.showMessageDialog(Signup.this, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(Signup.this, "Password must be at least 8 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(Signup.this, "Password and confirm password do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hash the password
                String hashedPassword = hashPassword(password);

                // Perform database operations
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    String insertQuery;
                    PreparedStatement pstmt;
                    if (role.equals("Student")) {
                        String gender = genderField.getText();
                        int year = Integer.parseInt(yearField.getText());

                        if (gender.isEmpty() || yearField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(Signup.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        insertQuery = "INSERT INTO students (username, enrollmentNumber, email, hashedpassword, gender, year) VALUES (?, ?, ?, ?, ?, ?)";
                        pstmt = conn.prepareStatement(insertQuery);
                        pstmt.setString(1, username);
                        pstmt.setString(2, enrollmentNumber);
                        pstmt.setString(3, email);
                        pstmt.setString(4, hashedPassword);
                        pstmt.setString(5, gender);
                        pstmt.setInt(6, year);
                    } else {
                        insertQuery = "INSERT INTO admin (username, enrollmentNumber, email, hashedpassword) VALUES (?, ?, ?, ?)";
                        pstmt = conn.prepareStatement(insertQuery);
                        pstmt.setString(1, username);
                        pstmt.setString(2, enrollmentNumber);
                        pstmt.setString(3, email);
                        pstmt.setString(4, hashedPassword);
                    }

                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();

                    JOptionPane.showMessageDialog(Signup.this, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Redirect to login page
                    dispose(); // Close the current window
                    Login loginPage = new Login(); // Create a new login page
                    loginPage.setVisible(true); // Show the login page
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Signup.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(signupButton);

        JLabel loginLabel = new JLabel("Already have an account! Login");
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Redirect to login page
                dispose(); // Close the current window
                Login loginPage = new Login(); // Create a new login page
                loginPage.setVisible(true); // Show the login page
            }
        });
        panel.add(loginLabel);

        add(panel);
    }

        private String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password.getBytes());
                return Base64.getEncoder().encodeToString(hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                // Handle the exception appropriately
                return null;
            }
        }
    
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // Choose factory based on role
                UserFactory factory;
                String[] roles = {"Student", "Admin"};
                String selectedRole = (String) JOptionPane.showInputDialog(null, "Choose role:", "Role Selection", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
    
                if (selectedRole != null) {
                    if (selectedRole.equals("Student")) {
                        factory = new StudentFactory();
                    } else {
                        factory = new AdminFactory();
                    }
    
                    new Signup(factory).setVisible(true);
                } else {
                    // Handle if user cancels role selection
                    JOptionPane.showMessageDialog(null, "Signup cancelled", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
}
