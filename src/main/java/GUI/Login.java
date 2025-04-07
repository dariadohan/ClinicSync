package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Data.Role;
import Data.User;
import dao.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {
    private JPanel loginP;
    private JPanel mainP;
    private JTextField nameTF;
    private JButton logInButton;
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
                String password = passwordField1.getText();
                User user = loginUser(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    switch (user.getRole()) {
                        case ADMIN:
                            dispose();
                            DashBoard dashBoard=new DashBoard();
                            break;
                        case DOCTOR:
                            System.out.println("Doctor logged in.");
                            break;
                        case RECEPTIONIST:
                            DashBoardR dashBoardR=new DashBoardR();
                            dispose();
                            break;
                        case PATIENT:
                            System.out.println("Patient logged in.");
                            break;
                        default:
                            System.out.println("Unknown role.");
                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public User loginUser(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String cnp = rs.getString("cnp");
                    String phoneNo = rs.getString("phoneNo");
                    String roleStr = rs.getString("role");
                    String specialtyStr = rs.getString("specialty");

                    Role role = roleStr != null ? Role.valueOf(roleStr.toUpperCase()) : null;

                    User user = new User(name, email, username,"", cnp, phoneNo, role);
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

}
