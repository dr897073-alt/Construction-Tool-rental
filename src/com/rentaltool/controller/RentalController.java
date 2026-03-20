package com.rentaltool.controller;

import com.rentaltool.model.Rental;
import com.rentaltool.model.Tool;
import com.rentaltool.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalController {

    // ── GET ALL RENTALS ───────────────────────────────────
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, t.name AS tool_name FROM rentals r " +
                     "JOIN tools t ON r.tool_id = t.id ORDER BY r.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rentals.add(new Rental(
                    rs.getInt("id"),
                    rs.getInt("tool_id"),
                    rs.getString("tool_name"),
                    rs.getString("customer_name"),
                    rs.getString("customer_phone"),
                    rs.getString("rent_date"),
                    rs.getString("return_date"),
                    rs.getInt("days"),
                    rs.getDouble("total_price"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching rentals: " + e.getMessage());
        }
        return rentals;
    }

    // ── GET AVAILABLE TOOLS FOR RENTING ──────────────────
    public List<Tool> getAvailableTools() {
        List<Tool> tools = new ArrayList<>();
        String sql = "SELECT * FROM tools WHERE status = 'AVAILABLE' AND quantity > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tools.add(new Tool(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("quantity"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tools: " + e.getMessage());
        }
        return tools;
    }

    // ── RENT A TOOL ───────────────────────────────────────
    public String rentTool(int toolId, String customerName,
                           String customerPhone, String rentDate, int days,
                           double pricePerDay) {

        if (customerName.isEmpty()) return "Customer name cannot be empty.";
        if (customerPhone.isEmpty()) return "Customer phone cannot be empty.";
        if (!customerPhone.matches("\\d{7,15}")) return "Invalid phone number.";
        if (days <= 0) return "Days must be at least 1.";

        double totalPrice = days * pricePerDay;

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Insert rental
            String sqlInsert = "INSERT INTO rentals (tool_id, customer_name, customer_phone, " +
                               "rent_date, days, total_price, status) VALUES (?, ?, ?, ?, ?, ?, 'RENTED')";
            PreparedStatement stmt1 = conn.prepareStatement(sqlInsert);
            stmt1.setInt(1, toolId);
            stmt1.setString(2, customerName);
            stmt1.setString(3, customerPhone);
            stmt1.setString(4, rentDate);
            stmt1.setInt(5, days);
            stmt1.setDouble(6, totalPrice);
            stmt1.executeUpdate();

            // Update tool quantity
            String sqlUpdate = "UPDATE tools SET quantity = quantity - 1, " +
                               "status = CASE WHEN quantity - 1 = 0 THEN 'RENTED' ELSE 'AVAILABLE' END " +
                               "WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sqlUpdate);
            stmt2.setInt(1, toolId);
            stmt2.executeUpdate();

            conn.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error renting tool: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── RETURN A TOOL ─────────────────────────────────────
    public String returnTool(int rentalId, int toolId, String returnDate) {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Update rental status
            String sqlRental = "UPDATE rentals SET status = 'RETURNED', return_date = ? WHERE id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sqlRental);
            stmt1.setString(1, returnDate);
            stmt1.setInt(2, rentalId);
            stmt1.executeUpdate();

            // Restore tool quantity
            String sqlTool = "UPDATE tools SET quantity = quantity + 1, status = 'AVAILABLE' WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sqlTool);
            stmt2.setInt(1, toolId);
            stmt2.executeUpdate();

            conn.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error returning tool: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── REPORT STATS ──────────────────────────────────────
    public int getTotalRentals() {
        String sql = "SELECT COUNT(*) FROM rentals";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public int getActiveRentals() {
        String sql = "SELECT COUNT(*) FROM rentals WHERE status = 'RENTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_price) FROM rentals WHERE status = 'RETURNED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0.0;
    }
}