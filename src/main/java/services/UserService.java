package services;

import Data.Role;
import Data.Specialty;
import Data.User;
import dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {

    private static final Pattern VALID_EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final UserDAO userDAO = new UserDAO();

    public static Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public static boolean createUser(String name, String email, String username,
                                     String password, String cnp, String phoneNumber,
                                     Role role, Specialty specialty) {

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            System.out.println("\033[31mUsername, email, and password are required fields.\033[0m");
            return false;
        }

        if (password.length() < 6) {
            System.out.println("\033[31mPassword must be at least 6 characters long.\033[0m");
            return false;
        }

        if (!VALID_EMAIL_REGEX.matcher(email).matches()) {
            System.out.println("\033[31mInvalid email format.\033[0m");
            return false;
        }

        if (!isEmailUnique(email)) {
            System.out.println("\033[31mEmail is already associated with an existing account.\033[0m");
            return false;
        }

        if (!isUsernameUnique(username)) {
            System.out.println("\033[31mUsername is already taken.\033[0m");
            return false;
        }

        User newUser = new User(name, email, username, password, cnp, phoneNumber, specialty, role);
        return userDAO.registerUser(newUser);
    }

    public static User authenticateUser(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            System.out.println("\033[31mBoth username and password must be provided.\033[0m");
            return null;
        }

        User user = userDAO.getUserByCredentials(username, password);
        if (user != null) {
            System.out.println("Authentication successful.");
            return user;
        } else {
            System.out.println("\033[31mAuthentication failed.\033[0m");
            return null;
        }
    }


    public static boolean isEmailUnique(String email) {
        return userDAO.findByEmail(email).isEmpty();
    }

    public static boolean isUsernameUnique(String username) {
        return userDAO.findByUsername(username).isEmpty();
    }

    public boolean removeUserByEmail(String email) {
        return userDAO.deleteUserByEmail(email);
    }

    public List<User> getAllUsersExceptEmail(String excludedEmail) {
        return userDAO.getAllUsers(excludedEmail);
    }

    public boolean editUserDetailsByEmail(String email, String username, String newEmail,
                                          String name, String phoneNumber, Specialty specialty) {
        Optional<User> userOpt = userDAO.findByEmail(email);

        if (userOpt.isEmpty()) {
            System.out.println("\033[31mUser not found for update. Email: " + email + "\033[0m");
            return false;
        }

        User user = userOpt.get();
        user.setUsername(username);
        user.setEmail(newEmail);
        user.setName(name);
        user.setPhoneNo(phoneNumber);
        user.setSpecialty(specialty);

        return userDAO.updateUser(user);
    }

    public static List<User> fetchUsersByRole(Role role) {
        return userDAO.findUsersByRole(role);
    }

    public String getDoctorNameById(int doctorId) {
        return userDAO.getDoctorNameById(doctorId);
    }
}
