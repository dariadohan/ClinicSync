package dao;

import Data.User;
import Data.Role;
import Data.Specialty;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    // Hash password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    // Register a new user
    public boolean registerUser(User user) {
        String checkQuery = "SELECT id FROM user WHERE email = ? OR username = ?";
        String insertQuery = "INSERT INTO user (name, username, password, email, CNP, specialty, role, phoneNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            insertStmt.setString(2, user.getUsername());
            insertStmt.setString(3, hashedPassword);
            insertStmt.setString(4, user.getEmail());
            insertStmt.setString(5, user.getCNP());
            insertStmt.setString(6, user.getSpecialty() != null ? user.getSpecialty().toString() : null);
            insertStmt.setString(7, user.getRole().toString());
            insertStmt.setString(8, user.getPhoneNo());


            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    // Validate login credentials
    public User getUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String cnp = rs.getString("cnp");
                String phoneNo = rs.getString("phone_no");

                Role role = Role.valueOf(rs.getString("role"));
                Specialty specialty = null;
                if (role == Role.DOCTOR) {
                    specialty = Specialty.valueOf(rs.getString("specialty"));
                }

                return new User(id, name, email, username, password, cnp, phoneNo, specialty, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CNP"),
                        rs.getString("phoneNo"),
                        null, // specialty placeholder
                        Role.valueOf(rs.getString("role").toUpperCase()) // assuming stored as string
                );

                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public User findById(int id) {
        String query = "SELECT * FROM user WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CNP"),
                        rs.getString("phoneNo"),
                        null, // specialty placeholder
                        Role.valueOf(rs.getString("role").toUpperCase())
                );

                user.setId(id); // if your User class has a setter for id
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Load specialty if applicable
                Specialty specialty = null;
                String specialtyStr = rs.getString("specialty");
                if (specialtyStr != null) {
                    specialty = Specialty.valueOf(specialtyStr.toUpperCase());
                }

                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CNP"),
                        rs.getString("phoneNo"),
                        specialty,
                        Role.valueOf(rs.getString("role").toUpperCase())
                );

                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }



    public List<User> getAllUsers(String excludedEmail) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE email != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, excludedEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Load specialty if available
                Specialty specialty = null;
                String specialtyStr = rs.getString("specialty");
                if (specialtyStr != null) {
                    specialty = Specialty.valueOf(specialtyStr.toUpperCase());
                }

                User user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CNP"),
                        rs.getString("phoneNo"),
                        specialty,
                        Role.valueOf(rs.getString("role").toUpperCase())
                );

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching users excluding email: " + excludedEmail);
        }

        return users;
    }


    public boolean updateUser(User user) {
        String query = "UPDATE user SET name = ?, email = ?, username = ?, phoneNo = ?, role = ?, specialty = ? WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPhoneNo());
            stmt.setString(5, user.getRole().toString()); // Assuming role is an enum
            stmt.setString(6, (user.getSpecialty() != null) ? user.getSpecialty().toString() : null); // Optional for specialty
            stmt.setString(7, user.getEmail()); // Update by email

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating user with email: " + user.getEmail());
            return false;
        }
    }

    public List<User> findUsersByRole(Role role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role.toString()); // Use the role enum's string representation
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Load specialty if available
                Specialty specialty = null;
                String specialtyStr = rs.getString("specialty");
                if (specialtyStr != null) {
                    specialty = Specialty.valueOf(specialtyStr.toUpperCase());
                }

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("CNP"),
                        rs.getString("phoneNo"),
                        specialty,
                        Role.valueOf(rs.getString("role").toUpperCase())
                );

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching users with role: " + role);
        }

        return users;
    }

    public boolean deleteUserByEmail(String email) {
        String query = "DELETE FROM user WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting user with email: " + email);
            return false;
        }
    }

    public String getDoctorNameById(int doctorId) {
        String name = null;
        String sql = "SELECT name FROM user WHERE id = ? AND role = 'DOCTOR'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

}
