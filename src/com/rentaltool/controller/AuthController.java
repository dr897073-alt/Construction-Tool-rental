package com.rentaltool.controller;

import com.rentaltool.model.User;
import com.rentaltool.util.DBConnection;
import com.rentaltool.util.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthController {

    // ── GET ALL USERS ─────────────────────────────────────
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    // ── LOGIN ─────────────────────────────────────────────
    public String login(String username, String password) {
        // Validate inputs first
        String err = Validator.validateUsername(username);
        if (err != null) return err;

        err = Validator.validatePassword(password);
        if (err != null) return err;

        // Check credentials in DB
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "SUCCESS";
            } else {
                return "Invalid username or password.";
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── GET LOGGED IN USER OBJECT ─────────────────────────
    public User getLoggedInUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    // ── REGISTER ──────────────────────────────────────────
    public String register(String username, String email,
                           String password, String confirm, String role) {
        // Validate all fields
        String err;
        err = Validator.validateUsername(username);                 if (err != null) return err;
        err = Validator.validateEmail(email);                       if (err != null) return err;
        err = Validator.validatePassword(password);                 if (err != null) return err;
        err = Validator.validateConfirmPassword(password, confirm); if (err != null) return err;

        // Check duplicate username
        if (usernameExists(username)) return "Username already taken. Choose another.";

        // Insert into DB
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "SUCCESS";
            else return "Registration failed. Try again.";

        } catch (SQLException e) {
            System.out.println("Register error: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── CHECK USERNAME EXISTS ─────────────────────────────
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
        return false;
    }
}
