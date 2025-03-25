package GUI;

import Data.User;
import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel mainP;
    private JButton logInButton;
    private JButton registerButton;
    private JTextField nameTF;
    private JTextField cnpTF;
    private JTextField emailTF;
    private JTextField usernameTF;
    private JPasswordField passwTF; // Use JPasswordField for security

    public MainFrame() {
        super();
        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        // Login Button Action
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
            }
        });

        // Register Button Action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameTF.getText().trim();
        String cnp = cnpTF.getText().trim();
        String email = emailTF.getText().trim();
        String username = usernameTF.getText().trim();
        String password = new String(passwTF.getPassword()).trim(); // Convert password field to String

        if (name.isEmpty() || cnp.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create User Object
        User user = new User(0, name, email, username, password, cnp);

        // Register user
        UserDAO userDAO = new UserDAO();
        if (userDAO.registerUser(user)) {
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
