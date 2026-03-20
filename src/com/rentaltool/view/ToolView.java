package com.rentaltool.view;

import com.rentaltool.controller.ToolController;
import com.rentaltool.model.Tool;
import com.rentaltool.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ToolView {

    private ToolController toolController = new ToolController();
    private int selectedToolId = -1;

    // Form fields
    private TextField txtName, txtCategory, txtPrice, txtQty;
    private ComboBox<String> cmbStatus;
    private Label lblMsg;

    // Table
    private TableView<Tool> tableView;
    private ObservableList<Tool> toolData;

    public void show(Stage stage, User user) {
        stage.setTitle("Tool Management — " + user.getUsername());

        // ── Top Bar ─────────────────────────────────────
        Label lblTitle = new Label("Tool Inventory Management");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblTitle.setTextFill(Color.web("#FFA500"));

        Button btnBack = new Button("← Back to Dashboard");
        btnBack.setStyle("-fx-background-color: #3C3C5A; -fx-text-fill: white; " +
                         "-fx-background-radius: 4; -fx-cursor: hand;");
        btnBack.setFont(Font.font("Arial", 12));
        btnBack.setOnAction(e -> new DashboardView().show(stage, user));

        HBox topBar = new HBox(lblTitle, btnBack);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lblTitle, Priority.ALWAYS);
        topBar.setSpacing(10);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle("-fx-background-color: #232337;");

        // ── Left Form Panel ──────────────────────────────
        VBox formPanel = buildFormPanel();
        formPanel.setPrefWidth(240);

        // ── Right Table Panel ────────────────────────────
        tableView = buildTableView();
        loadTable();

        // Click row → fill form
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldTool, selected) -> {
            if (selected != null) fillForm(selected);
        });

        // ── Main Layout ──────────────────────────────────
        HBox body = new HBox(10, formPanel, tableView);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(body);
        root.setStyle("-fx-background-color: #191928;");

        stage.setScene(new Scene(root, 900, 580));
        stage.setResizable(true);
        stage.show();
    }

    // ── Form Panel ────────────────────────────────────────
    private VBox buildFormPanel() {
        Label lblHeader = new Label("Tool Details");
        lblHeader.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblHeader.setTextFill(Color.web("#FFA500"));

        txtName     = styledTextField("e.g. Excavator");
        txtCategory = styledTextField("e.g. Heavy Equipment");
        txtPrice    = styledTextField("e.g. 1500.00");
        txtQty      = styledTextField("e.g. 3");

        cmbStatus = new ComboBox<>();
        cmbStatus.getItems().addAll("AVAILABLE", "RENTED");
        cmbStatus.setValue("AVAILABLE");
        cmbStatus.setPrefWidth(Double.MAX_VALUE);
        cmbStatus.setStyle("-fx-background-color: #373750; -fx-text-fill: white;");

        lblMsg = new Label(" ");
        lblMsg.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        lblMsg.setTextFill(Color.web("#64E664"));
        lblMsg.setWrapText(true);

        Button btnAdd    = actionButton("Add Tool",  "#228B22");
        Button btnUpdate = actionButton("Update",    "#1E64B4");
        Button btnDelete = actionButton("Delete",    "#B42828");
        Button btnClear  = actionButton("Clear",     "#505064");

        btnAdd.setOnAction(e -> handleAdd());
        btnUpdate.setOnAction(e -> handleUpdate());
        btnDelete.setOnAction(e -> handleDelete());
        btnClear.setOnAction(e -> clearForm());

        HBox btnRow1 = new HBox(8, btnAdd, btnUpdate);
        HBox btnRow2 = new HBox(8, btnDelete, btnClear);
        btnRow1.setAlignment(Pos.CENTER);
        btnRow2.setAlignment(Pos.CENTER);

        VBox panel = new VBox(8,
            lblHeader,
            formRow("Tool Name:",    txtName),
            formRow("Category:",     txtCategory),
            formRow("Price/Day Rs:", txtPrice),
            formRow("Quantity:",     txtQty),
            formRow("Status:",       cmbStatus),
            lblMsg,
            btnRow1,
            btnRow2
        );
        panel.setPadding(new Insets(14));
        panel.setStyle("-fx-background-color: #282840; -fx-background-radius: 8;");
        return panel;
    }

    private VBox formRow(String labelText, Control field) {
        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.LIGHTGRAY);
        lbl.setFont(Font.font("Arial", 12));
        VBox row = new VBox(3, lbl, field);
        return row;
    }

    // ── Table ─────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private TableView<Tool> buildTableView() {
        TableView<Tool> tv = new TableView<>();
        tv.setStyle("-fx-background-color: #2D2D41; -fx-text-fill: white;");
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tool, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(45);

        TableColumn<Tool, String> colName = new TableColumn<>("Tool Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Tool, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Tool, Double> colPrice = new TableColumn<>("Price/Day");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        TableColumn<Tool, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQty.setMaxWidth(55);

        TableColumn<Tool, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tv.getColumns().addAll(colId, colName, colCat, colPrice, colQty, colStatus);

        // Style rows
        tv.setRowFactory(r -> {
            TableRow<Tool> row = new TableRow<>();
            row.setStyle("-fx-background-color: #2D2D41;");
            row.selectedProperty().addListener((obs, wasSelected, isSelected) ->
                row.setStyle(isSelected
                    ? "-fx-background-color: #5078C8;"
                    : "-fx-background-color: #2D2D41;")
            );
            return row;
        });

        toolData = FXCollections.observableArrayList();
        tv.setItems(toolData);
        return tv;
    }

    // ── Load / Refresh Table ──────────────────────────────
    private void loadTable() {
        toolData.clear();
        List<Tool> tools = toolController.getAllTools();
        toolData.addAll(tools);
    }

    // ── Fill form when row clicked ────────────────────────
    private void fillForm(Tool tool) {
        selectedToolId = tool.getId();
        txtName.setText(tool.getName());
        txtCategory.setText(tool.getCategory());
        txtPrice.setText(String.valueOf(tool.getPricePerDay()));
        txtQty.setText(String.valueOf(tool.getQuantity()));
        cmbStatus.setValue(tool.getStatus());
        lblMsg.setText(" ");
    }

    // ── CRUD Handlers ─────────────────────────────────────
    private void handleAdd() {
        String result = toolController.addTool(
            txtName.getText().trim(),
            txtCategory.getText().trim(),
            txtPrice.getText().trim(),
            txtQty.getText().trim(),
            cmbStatus.getValue()
        );
        if (result.equals("SUCCESS")) { showSuccess("Tool added successfully!"); loadTable(); clearForm(); }
        else showError(result);
    }

    private void handleUpdate() {
        if (selectedToolId == -1) { showError("Select a tool from the table first."); return; }
        String result = toolController.updateTool(
            selectedToolId,
            txtName.getText().trim(),
            txtCategory.getText().trim(),
            txtPrice.getText().trim(),
            txtQty.getText().trim(),
            cmbStatus.getValue()
        );
        if (result.equals("SUCCESS")) { showSuccess("Tool updated successfully!"); loadTable(); clearForm(); }
        else showError(result);
    }

    private void handleDelete() {
        if (selectedToolId == -1) { showError("Select a tool from the table first."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this tool?",
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        Optional<ButtonType> response = confirm.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String result = toolController.deleteTool(selectedToolId);
            if (result.equals("SUCCESS")) { showSuccess("Tool deleted."); loadTable(); clearForm(); }
            else showError(result);
        }
    }

    private void clearForm() {
        txtName.clear(); txtCategory.clear();
        txtPrice.clear(); txtQty.clear();
        cmbStatus.setValue("AVAILABLE");
        selectedToolId = -1;
        tableView.getSelectionModel().clearSelection();
        lblMsg.setText(" ");
    }

    // ── Helpers ───────────────────────────────────────────
    private void showSuccess(String msg) {
        lblMsg.setTextFill(Color.web("#64E664"));
        lblMsg.setText(msg);
    }

    private void showError(String msg) {
        lblMsg.setTextFill(Color.web("#FF5050"));
        lblMsg.setText(msg);
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-background-color: #373750; -fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #888; -fx-border-color: #646496; " +
                    "-fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 5 8;");
        return tf;
    }

    private Button actionButton(String text, String hex) {
        Button btn = new Button(text);
        btn.setPrefWidth(100);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + hex + "; -fx-background-radius: 4; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + hex + ", -15%); -fx-background-radius: 4; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + hex + "; -fx-background-radius: 4; -fx-cursor: hand;"));
        return btn;
    }
}