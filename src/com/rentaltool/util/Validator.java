package com.rentaltool.util;

public class Validator {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String validateUsername(String username) {
        if (isEmpty(username)) return "Username cannot be empty.";
        if (username.length() < 3) return "Username must be at least 3 characters.";
        if (username.length() > 20) return "Username must not exceed 20 characters.";
        if (!username.matches("[a-zA-Z0-9_]+"))
            return "Username can only contain letters, numbers, and underscores.";
        return null;
    }

    public static String validatePassword(String password) {
        if (isEmpty(password)) return "Password cannot be empty.";
        if (password.length() < 6) return "Password must be at least 6 characters.";
        return null;
    }

    public static String validateConfirmPassword(String password, String confirm) {
        if (isEmpty(confirm)) return "Please confirm your password.";
        if (!password.equals(confirm)) return "Passwords do not match.";
        return null;
    }

    public static String validateEmail(String email) {
        if (isEmpty(email)) return "Email cannot be empty.";
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return "Invalid email format.";
        return null;
    }

    public static String validateToolName(String name) {
        if (isEmpty(name)) return "Tool name cannot be empty.";
        if (name.length() > 50) return "Tool name must not exceed 50 characters.";
        return null;
    }

    public static String validatePrice(String priceStr) {
        if (isEmpty(priceStr)) return "Price cannot be empty.";
        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) return "Price must be greater than 0.";
        } catch (NumberFormatException e) {
            return "Price must be a valid number.";
        }
        return null;
    }

    public static String validateQuantity(String qtyStr) {
        if (isEmpty(qtyStr)) return "Quantity cannot be empty.";
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) return "Quantity must be at least 1.";
        } catch (NumberFormatException e) {
            return "Quantity must be a whole number.";
        }
        return null;
    }

    public static String validateCategory(String category) {
        if (isEmpty(category)) return "Category cannot be empty.";
        return null;
    }
}