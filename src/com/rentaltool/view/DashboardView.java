package com.rentaltool.view;

import com.rentaltool.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardView {

    public void show(Stage stage, User user) {
        stage.setTitle("Dashboard — " + user.getUsername());

        // ── Top Bar ─────────────────────────────────────
        Label lblTitle = new Label("Construction Rental Tool");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblTitle.setTextFill(Color.web("#FFA500"));

        Label lblUser = new Label("User: " + user.getUsername() + "   |   Role: " + user.getRole());
        lblUser.setTextFill(Color.LIGHTGRAY);
        lblUser.setFont(Font.font("Arial", 12));

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #B42828; -fx-text-fill: white; " +
                           "-fx-background-radius: 4; -fx-cursor: hand;");
        btnLogout.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btnLogout.setOnAction(e -> new LoginView().show(stage));

        HBox topBar = new HBox(lblTitle, lblUser, btnLogout);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(20);
        HBox.setHgrow(lblUser, Priority.ALWAYS);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle("-fx-background-color: #232337;");

        // ── Menu Buttons ─────────────────────────────────
        Button btnTools   = dashButton("Manage Tools",               "#2864B4");
        Button btnRentals = dashButton("Rentals ",      "#464646");
        Button btnReports = dashButton("Reports ",      "#464646");
        Button btnUsers   = dashButton("User Management ", "#464646");

        btnRentals.setDisable(true);
        btnReports.setDisable(true);
        btnUsers.setDisable(true);

        btnTools.setOnAction(e -> new ToolView().show(stage, user));

        // 2x2 grid of buttons
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(40));

        grid.add(btnTools,   0, 0);
        grid.add(btnRentals, 1, 0);
        grid.add(btnReports, 0, 1);
        grid.add(btnUsers,   1, 1);

        // ── Footer ───────────────────────────────────────
        Label lblFooter = new Label("Construction Rental Tool © 2025");
        lblFooter.setTextFill(Color.web("#5A5A78"));
        lblFooter.setFont(Font.font("Arial", 11));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(grid);
        root.setBottom(lblFooter);
        BorderPane.setAlignment(lblFooter, Pos.CENTER);
        BorderPane.setMargin(lblFooter, new Insets(8));
        root.setStyle("-fx-background-color: #191928;");

        stage.setScene(new Scene(root, 620, 420));
        stage.setResizable(false);
        stage.show();
    }

    private Button dashButton(String text, String hex) {
        Button btn = new Button(text);
        btn.setPrefSize(220, 80);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + hex + "; -fx-background-radius: 8; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + hex + ", -15%); -fx-background-radius: 8; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + hex + "; -fx-background-radius: 8; -fx-cursor: hand;"));
        return btn;
    }
}