package GUI;

import Data.User;
import Data.Role;
import Data.Specialty;
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
    private JComboBox<Specialty> comboBox1; // JComboBox for selecting role
    private JComboBox<Role> comboBox3; // JComboBox for selecting specialty
    private JTextField phoneNoTF;

    public MainFrame() {
        super();
        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        comboBox3.setModel(new DefaultComboBoxModel<>(Role.values()));
        comboBox1.setModel(new DefaultComboBoxModel<>(Specialty.values()));

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
        String phoneNumber = phoneNoTF.getText().trim(); // Get phone number from the text field
        Role role = (Role) comboBox3.getSelectedItem(); // Get selected role from combo box
        Specialty specialty = (Specialty) comboBox1.getSelectedItem(); // Get selected specialty from combo box

        // Check if fields are empty
        if (name.isEmpty() || cnp.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if password is at least 6 characters
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email or username already exists
        UserDAO userDAO = new UserDAO();
        if (userDAO.findByEmail(email).isPresent()) {
            JOptionPane.showMessageDialog(this, "Email is already associated with an account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDAO.findByUsername(username).isPresent()) {
            JOptionPane.showMessageDialog(this, "Username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create User Object
        User user = new User(name, email, username, password, cnp, phoneNumber, specialty, role);

        // Register user
        boolean registrationSuccess = userDAO.registerUser(user);

        if (registrationSuccess) {
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
