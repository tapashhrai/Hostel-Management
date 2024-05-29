import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import HostelManagement.*;

public class Login extends JFrame {

    // List to hold observers for login events
    private final List<LoginObserver> observers = new ArrayList<>();
    private JLabel statusLabel;

    // Constructor to initialize the login frame
    public Login() {
        setTitle("Login Page");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create and configure the main panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add a title label to the panel
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);
        panel.add(new JLabel());

        // Add email input field and label to the panel
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        // Add password input field and label to the panel
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Add login button to the panel
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        // Add signup label to redirect to the signup page
        JLabel signupLabel = new JLabel("Don't have an account? Sign up");
        signupLabel.setForeground(Color.BLUE);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Redirect to signup page
                dispose(); // Close the current window
                Signup signupPage = new Signup(null); // Create a new signup page
                signupPage.setVisible(true); // Show the signup page
            }
        });
        panel.add(signupLabel);

        // Add status label to display messages
        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Check if email and password fields are filled
                if (email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Please fill in all fields");
                    return;
                }

                // Authenticate the user
                AuthenticationManager authManager = AuthenticationManager.getInstance();
                String result = authManager.authenticate(email, password);

                // Check authentication result and handle accordingly
                if (result.equals("Student")) {
                    notifyObservers("Student");
                    JOptionPane.showMessageDialog(Login.this, "Login successful as student", "Success", JOptionPane.INFORMATION_MESSAGE);
                    StudentHomePage studentHomePage = new StudentHomePage(email);
                    studentHomePage.setVisible(true);
                    dispose();
                } else if (result.equals("Admin")) {
                    notifyObservers("Admin");
                    JOptionPane.showMessageDialog(Login.this, "Login successful as admin", "Success", JOptionPane.INFORMATION_MESSAGE);
                    AdminHomePage adminHomePage = new AdminHomePage(email);
                    adminHomePage.setVisible(true);
                    dispose();
                } else {
                    statusLabel.setText(result);
                    notifyObservers(null);
                }
            }
        });

        // Add the main panel to the frame
        add(panel);
    }

    // Method to add an observer for login events
    public void addObserver(LoginObserver observer) {
        observers.add(observer);
    }

    // Method to remove an observer for login events
    public void removeObserver(LoginObserver observer) {
        observers.remove(observer);
    }

    // Method to notify observers about login success or failure
    private void notifyObservers(String userType) {
        for (LoginObserver observer : observers) {
            if (userType != null) {
                observer.onLoginSuccess(userType);
            } else {
                observer.onLoginFailure();
            }
        }
    }

    // Main method to create and display the login page
    public static void main(String[] args) {
        Login loginPage = new Login();

        // Create a login observer to handle login events
        LoginObserver observer = new LoginObserver() {
            @Override
            public void onLoginSuccess(String userType) {
                System.out.println("Login successful as " + userType);
            }

            @Override
            public void onLoginFailure() {
                System.out.println("Login failed");
            }
        };

        // Add the observer to the login page
        loginPage.addObserver(observer);

        // Make the login page visible
        SwingUtilities.invokeLater(() -> loginPage.setVisible(true));
    }
}
