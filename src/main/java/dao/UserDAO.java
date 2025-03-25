package dao;

import Data.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Hash password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // Secure hashing with 12 rounds
    }

    // Register a new user
    public boolean registerUser(User user) {
        String checkQuery = "SELECT id FROM user WHERE email = ? OR username = ?";
        String insertQuery = "INSERT INTO user (name, email, username, password, CNP) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Check if email or username already exists
            checkStmt.setString(1, user.getEmail());
            checkStmt.setString(2, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("⚠ Email or username already exists.");
                return false;
            }

            // Hash the password before saving
            String hashedPassword = hashPassword(user.getPassword());

            // Insert user details into database
            insertStmt.setString(1, user.getName());
            insertStmt.setString(2, user.getEmail());
            insertStmt.setString(3, user.getUsername());
            insertStmt.setString(4, hashedPassword);
            insertStmt.setString(5, user.getCNP());

            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0; // Return true if insertion is successful

        } catch (SQLException e) {
            e.printStackTrace(); // Log the exact SQL error
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }

    }

    // Validate login credentials
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
