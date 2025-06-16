package TimeCapsuleMessagingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCapsuleLoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public TimeCapsuleLoginUI() {
        setTitle("Time Capsule Messaging System");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Time Capsule Messaging System", JLabel.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 28));
        header.setForeground(new Color(0, 70, 140));
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (USERSERVICE.Login(username, password)) {
                    int userId = USERSERVICE.getUserId(username);
                    new TimeCapsuleHomeUI(userId, username);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(TimeCapsuleLoginUI.this,
                            "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (USERSERVICE.Register(username, password)) {
                    JOptionPane.showMessageDialog(TimeCapsuleLoginUI.this,
                            "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showLoadingScreen();
            new TimeCapsuleLoginUI();
        });
    }

    private static void showLoadingScreen() {
        JWindow loadingScreen = new JWindow();
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        JLabel label = new JLabel("\u23F3 Loading Time Capsule...", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(label);
        loadingScreen.getContentPane().add(content);
        loadingScreen.setSize(300, 100);
        loadingScreen.setLocationRelativeTo(null);
        loadingScreen.setVisible(true);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        loadingScreen.setVisible(false);
    }
}


// Separate class for the home screen after login
// Separate class for the home screen after login
class TimeCapsuleHomeUI extends JFrame {
    public TimeCapsuleHomeUI(int userId, String username) {
        setTitle("Welcome to Time Capsule");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcome = new JLabel("\u2728 Welcome, " + username + "!", JLabel.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setForeground(new Color(0, 100, 85));
        welcome.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton sendButton = new JButton("Send Message");
        JButton viewButton = new JButton("View Messages");
        JButton logoutButton = new JButton("Logout");

        JPanel centerPanel = new JPanel();
        centerPanel.add(sendButton);
        centerPanel.add(viewButton);
        centerPanel.add(logoutButton);

        add(welcome, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        sendButton.addActionListener(e -> {
            String receiver = JOptionPane.showInputDialog(this, "Enter receiver's username:");
            String message = JOptionPane.showInputDialog(this, "Enter message:");
            String unlock = JOptionPane.showInputDialog(this, "Enter unlock time (yyyy/MM/dd HH:mm):");

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime time = LocalDateTime.parse(unlock, formatter);
                MESSAGES.sendMessage(userId, receiver, message, time);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Date Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewButton.addActionListener(e -> {
            try {
                MESSAGES.viewMessage(userId, false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error viewing messages: " + ex.getMessage());
            }
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out!");
            new TimeCapsuleLoginUI();
            dispose();
        });

        setVisible(true);
    }
}
