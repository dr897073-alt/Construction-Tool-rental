package com.rentaltool.controller;

import com.rentaltool.model.Tool;
import com.rentaltool.util.DBConnection;
import com.rentaltool.util.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToolController {

    // ── GET ALL TOOLS ─────────────────────────────────────
    public List<Tool> getAllTools() {
        List<Tool> tools = new ArrayList<>();
        String sql = "SELECT * FROM tools";

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

    // ── GET TOOL BY ID ────────────────────────────────────
    public Tool getToolById(int id) {
        String sql = "SELECT * FROM tools WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tool(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("quantity"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tool: " + e.getMessage());
        }
        return null;
    }

    // ── CREATE — Add New Tool ─────────────────────────────
    public String addTool(String name, String category,
                          String priceStr, String qtyStr, String status) {
        // Validate inputs
        String err;
        err = Validator.validateToolName(name);     if (err != null) return err;
        err = Validator.validateCategory(category); if (err != null) return err;
        err = Validator.validatePrice(priceStr);    if (err != null) return err;
        err = Validator.validateQuantity(qtyStr);   if (err != null) return err;

        String sql = "INSERT INTO tools (name, category, price_per_day, quantity, status) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, Double.parseDouble(priceStr));
            stmt.setInt(4, Integer.parseInt(qtyStr));
            stmt.setString(5, status);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "SUCCESS";
            else return "Failed to add tool.";

        } catch (SQLException e) {
            System.out.println("Error adding tool: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── UPDATE — Edit Existing Tool ───────────────────────
    public String updateTool(int id, String name, String category,
                             String priceStr, String qtyStr, String status) {
        // Validate inputs
        String err;
        err = Validator.validateToolName(name);     if (err != null) return err;
        err = Validator.validateCategory(category); if (err != null) return err;
        err = Validator.validatePrice(priceStr);    if (err != null) return err;
        err = Validator.validateQuantity(qtyStr);   if (err != null) return err;

        String sql = "UPDATE tools SET name = ?, category = ?, price_per_day = ?, " +
                     "quantity = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, Double.parseDouble(priceStr));
            stmt.setInt(4, Integer.parseInt(qtyStr));
            stmt.setString(5, status);
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "SUCCESS";
            else return "Tool not found.";

        } catch (SQLException e) {
            System.out.println("Error updating tool: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }

    // ── DELETE — Remove Tool ──────────────────────────────
    public String deleteTool(int id) {
        String sql = "DELETE FROM tools WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "SUCCESS";
            else return "Tool not found.";
S
        } catch (SQLException e) {
            System.out.println("Error deleting tool: " + e.getMessage());
            return "Database error. Please try again.";
        }
    }
}