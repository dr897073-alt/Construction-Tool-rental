package com.rentaltool.view;

import com.rentaltool.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegisterView {

    private AuthController authController = new AuthController();

    public void show(Stage stage) {
        stage.setTitle("Construction Rental Tool — Register");

        // ── Title ───────────────────────────────────────
        Label lblTitle = new Label("CREATE ACCOUNT");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTitle.setTextFill(Color.web("#FFA500"));

        // ── Fields ──────────────────────────────────────
        TextField txtUsername = styledTextField("Enter username");
        TextField txtEmail    = styledTextField("Enter email");
        PasswordField txtPassword = styledPassword("Min 6 characters");
        PasswordField txtConfirm  = styledPassword("Re-enter password");

        ComboBox<String> cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("USER", "ADMIN");
        cmbRole.setValue("USER");
        cmbRole.setPrefWidth(220);
        cmbRole.setStyle("-fx-background-color: #373750; -fx-text-fill: white;");

        // ── Error Label ──────────────────────────────────
        Label lblError = new Label(" ");
        lblError.setTextFill(Color.web("#FF5050"));
        lblError.setFont(Font.font("Arial", 11));
        lblError.setMinHeight(20);

        // ── Form Grid ───────────────────────────────────
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setAlignment(Pos.CENTER);

        form.add(styledLabel("Username:"),         0, 0);
        form.add(txtUsername,                      1, 0);
        form.add(styledLabel("Email:"),            0, 1);
        form.add(txtEmail,                         1, 1);
        form.add(styledLabel("Password:"),         0, 2);
        form.add(txtPassword,                      1, 2);
        form.add(styledLabel("Confirm Password:"), 0, 3);
        form.add(txtConfirm,                       1, 3);
        form.add(styledLabel("Role:"),             0, 4);
        form.add(cmbRole,                          1, 4);

        // ── Buttons ─────────────────────────────────────
        Button btnRegister = new Button("REGISTER");
        btnRegister.setPrefWidth(220);
        btnRegister.setPrefHeight(40);
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnRegister.setTextFill(Color.WHITE);
        btnRegister.setStyle("-fx-background-color: #228B22; " +
                             "-fx-background-radius: 5; " +
                             "-fx-cursor: hand;");

        Button btnBack = new Button("Already have an account? Login");
        btnBack.setStyle("-fx-background-color: transparent; " +
                         "-fx-text-fill: #64B4FF; " +
                         "-fx-cursor: hand;");
        btnBack.setFont(Font.font("Arial", 12));

        // ── Actions ─────────────────────────────────────
        btnRegister.setOnAction(e -> {
            lblError.setText(" ");

            String username = txtUsername.getText().trim();
            String email    = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            String confirm  = txtConfirm.getText().trim();
            String role     = cmbRole.getValue();

            System.out.println("Register clicked! Username: " + username);

            String result = authController.register(username, email, password, confirm, role);

            System.out.println("Result: " + result);

            if (result.equals("SUCCESS")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account created successfully! Please login.");
                alert.showAndWait();
                new LoginView().show(stage);
            } else {
                lblError.setText(result);
            }
        });

        btnBack.setOnAction(e -> new LoginView().show(stage));

        // ── Layout ──────────────────────────────────────
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(25, 40, 25, 40));
        formBox.setStyle("-fx-background-color: #282840; -fx-background-radius: 10;");
        formBox.getChildren().addAll(
            lblTitle,
            form,
            lblError,
            btnRegister,
            btnBack
        );

        // ── Root ────────────────────────────────────────
        VBox root = new VBox(formBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #191928;");

        // ── Scene ────────────────────────────────────────
        Scene scene = new Scene(root, 480, 520);
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
        tf.setPrefWidth(220);
        tf.setStyle("-fx-background-color: #373750; -fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #888; -fx-border-color: #646496; " +
                    "-fx-border-radius: 4; -fx-background-radius: 4; " +
                    "-fx-padding: 6 10;");
        return tf;
    }

    private PasswordField styledPassword(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefWidth(220);
        pf.setStyle("-fx-background-color: #373750; -fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #888; -fx-border-color: #646496; " +
                    "-fx-border-radius: 4; -fx-background-radius: 4; " +
                    "-fx-padding: 6 10;");
        return pf;
    }
}