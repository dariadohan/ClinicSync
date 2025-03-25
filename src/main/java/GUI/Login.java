package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {
    private JPanel loginP;
    private JPanel mainP;
    private JTextField nameTF;
    private JTextField cnpTF;
    private JButton logInButton;
    private JButton registerButton;
    private JPasswordField passwordField1;

    public Login() {
        super();
        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nameTF.getText();
                String password = passwordField1.getText(); // Ensure this is a password field in GUI
                if (loginUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println(password);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainFrame mainFrame = new MainFrame();
            }
        });
    }

    public boolean loginUser(String username, String password) {
        String query = "SELECT password FROM user WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, storedHashedPassword); // Verify password
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
