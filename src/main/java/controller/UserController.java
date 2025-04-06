package controller;

import Data.Role;
import Data.Specialty;
import Data.User;
import services.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {

    private static UserService userService;

    public UserController() {
        userService = new UserService();
    }

    // Create a new user
    public boolean createUser(String name, String email, String username,
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

        if (!userService.isEmailUnique(email)) {
            System.out.println("\033[31mEmail is already associated with an existing account.\033[0m");
            return false;
        }

        if (!userService.isUsernameUnique(username)) {
            System.out.println("\033[31mUsername is already taken.\033[0m");
            return false;
        }

        return userService.createUser(name, email, username, password, cnp, phoneNumber, role, specialty);
    }

    public User authenticateUser(String username, String password) {
        System.out.println("Attempting to authenticate user...");
        return userService.authenticateUser(username, password);
    }


    public boolean removeUserByUsername(String username) {
        System.out.println("Attempting to remove user with email: " + username);
        return userService.removeUserByEmail(username);
    }

    public List<User> getAllUsersExceptEmail(String excludedEmail) {
        System.out.println("Fetching all users excluding the user with email: " + excludedEmail);
        return userService.getAllUsersExceptEmail(excludedEmail);
    }

    public boolean editUserDetailsByEmail(String email, String username, String newEmail,
                                          String name, String phoneNumber, Specialty specialty) {
        System.out.println("Attempting to edit user details for email: " + email);
        return userService.editUserDetailsByEmail(email, username, newEmail, name, phoneNumber, specialty);
    }


    public static List<User> fetchUsersByRole(Role role) {
        System.out.println("Fetching users with role: " + role);
        return userService.fetchUsersByRole(role);
    }

    public Optional<User> getUserByEmail(String email) {
        System.out.println("Fetching user with email: " + email);
        return userService.getUserByEmail(email);
    }
}
