package com.rentaltool.view;

import com.rentaltool.controller.AuthController;
import com.rentaltool.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LoginView {

    private AuthController authController = new AuthController();

    public void show(Stage stage) {
        stage.setTitle("Construction Rental Tool — Login");

        // ── Title ───────────────────────────────────────
        Label lblTitle = new Label("CONSTRUCTION RENTAL TOOL");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTitle.setTextFill(Color.web("#FFA500"));
        lblTitle.setTextAlignment(TextAlignment.CENTER);

        Label lblSub = new Label("Login to your account");
        lblSub.setFont(Font.font("Arial", 13));
        lblSub.setTextFill(Color.LIGHTGRAY);

        VBox header = new VBox(6, lblTitle, lblSub);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 0, 20, 0));

        // ── Form Fields ─────────────────────────────────
        Label lblUser = styledLabel("Username:");
        TextField txtUsername = styledTextField("Enter username");

        Label lblPass = styledLabel("Password:");
        PasswordField txtPassword = styledPassword("Enter password");

        Label lblError = new Label(" ");
        lblError.setTextFill(Color.web("#FF5050"));
        lblError.setFont(Font.font("Arial", 11));

        // ── Buttons ─────────────────────────────────────
        Button btnLogin = new Button("LOGIN");
        styleButton(btnLogin, "#DC7800", 200);

        Button btnRegister = linkButton("Don't have an account? Register");

        // ── Layout ──────────────────────────────────────
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(10, 40, 20, 40));

        form.add(lblUser,     0, 0);
        form.add(txtUsername, 1, 0);
        form.add(lblPass,     0, 1);
        form.add(txtPassword, 1, 1);

        VBox centerBox = new VBox(10, form, lblError, btnLogin, btnRegister);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20, 40, 30, 40));
        centerBox.setStyle("-fx-background-color: #282840; -fx-background-radius: 10;");

        VBox root = new VBox(header, centerBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #191928;");
        root.setPadding(new Insets(20, 40, 30, 40));

        // ── Actions ─────────────────────────────────────
        btnLogin.setOnAction(e -> {
            lblError.setText(" ");
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            String result = authController.login(username, password);
            if (result.equals("SUCCESS")) {
                User user = authController.getLoggedInUser(username);
                new DashboardView().show(stage, user);
            } else {
                lblError.setText(result);
            }
        });

        btnRegister.setOnAction(e -> new RegisterView().show(stage));

        // ── Scene ────────────────────────────────────────
        Scene scene = new Scene(root, 460, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // ── Helpers ───────────────────────────────────────────
    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Arial", 13));
        return lbl;
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(200);
        tf.setStyle("-fx-background-color: #373750; -fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #888; -fx-border-color: #646496; " +
                    "-fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6 10;");
        return tf;
    }

    private PasswordField styledPassword(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefWidth(200);
        pf.setStyle("-fx-background-color: #373750; -fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #888; -fx-border-color: #646496; " +
                    "-fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6 10;");
        return pf;
    }

    private void styleButton(Button btn, String hex, int width) {
        btn.setPrefWidth(width);
        btn.setPrefHeight(36);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + hex + "; -fx-background-radius: 5; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + hex + ", -15%); -fx-background-radius: 5; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + hex + "; -fx-background-radius: 5; -fx-cursor: hand;"));
    }

    private Button linkButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #64B4FF; -fx-cursor: hand;");
        btn.setFont(Font.font("Arial", 12));
        return btn;
    }
}